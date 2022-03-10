package org.chun.plutus.api.mod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.chun.plutus.common.dao.ActivityBasicDao;
import org.chun.plutus.common.dao.ActivitySetDao;
import org.chun.plutus.common.dto.JoinCodeDto;
import org.chun.plutus.common.enums.ActivityEnum;
import org.chun.plutus.common.exceptions.ActivityClosedException;
import org.chun.plutus.common.exceptions.ActivityDifferentException;
import org.chun.plutus.common.exceptions.ActivityNotFoundException;
import org.chun.plutus.common.exceptions.HostLeavingException;
import org.chun.plutus.common.exceptions.MultiActivityException;
import org.chun.plutus.common.exceptions.UserNotHostException;
import org.chun.plutus.common.exceptions.UserWithoutActivityException;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.ActivitySetVo;
import org.chun.plutus.util.DaoValidationUtil;
import org.chun.plutus.util.MapUtil;
import org.chun.uploadcc.IUploadCcService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.chun.plutus.util.MomentUtil.Date.yyyyMMdd;
import static org.chun.plutus.util.MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityMod {

  private static final String URL = "https://line.me/R/oaMessage/@530vubeg/?";
  private final ActivityBasicDao activityBasicDao;
  private final ActivitySetDao activitySetDao;
  private final MessageMod messageMod;
  private final IUploadCcService uploadCcService;

  /**
   * 直接建立活動
   *
   * @param userNum
   */
  public String forceCreateActivity(Long userNum) {
    final String now = yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now());
    activityBasicDao.query(MapUtil.newHashMap("userNum", userNum)).stream()
        .map(vo -> {
          vo.setActStatus(ActivityEnum.Status.FINISH.val());
          return vo;
        })
        .forEach(vo -> DaoValidationUtil.validateResultIsOne(() -> activityBasicDao.update(vo), vo));

    final String joinCode = RandomStringUtils.random(8, true, true);
    ActivityBasicVo activityBasicVo = new ActivityBasicVo();
    activityBasicVo.setActTitle(String.format("%s的活動", yyyyMMdd.format(LocalDate.now())));
    activityBasicVo.setActDesc("");
    activityBasicVo.setUserNum(userNum);
    activityBasicVo.setJoinCode(joinCode);
    activityBasicVo.setActStatus(ActivityEnum.Status.PREPARE.val());
    activityBasicVo.setCreateDate(now);
    activityBasicVo.setStartDate(now);
    activityBasicDao.insert(activityBasicVo);

    // 主持人為第一個參與者
    this.saveHostActivitySet(userNum, activityBasicVo.getActNum(), now);
    return joinCode;
  }

  /**
   * 參加活動
   *
   * @param userNum
   * @param joinCode
   * @return
   */
  public ActivityBasicVo joinActivityByJoinCode(Long userNum, String joinCode) {
    ActivityBasicVo activityBasicVo = activityBasicDao.getByJoinCode(joinCode);

    ActivitySetVo activitySetVo = new ActivitySetVo();
    activitySetVo.setActNum(activityBasicVo.getActNum());
    activitySetVo.setUserNum(userNum);
    activitySetVo.setStatus(ActivityEnum.SetStatus.JOIN.val());
    activitySetVo.setStartDate(yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
    activitySetDao.insert(activitySetVo);

    return activityBasicVo;
  }

  /**
   * 離開活動 狀態壓成離開
   *
   * @param joinCodeDto
   */
  public void leaveActivityByJoinCode(JoinCodeDto joinCodeDto) {
    ActivitySetVo activitySetVo = activitySetDao.getByUserNumAndJoinCode(joinCodeDto.getUserNum(), joinCodeDto.getJoinCode());
    activitySetVo.setStatus(ActivityEnum.SetStatus.LEAVE.val());
    activitySetVo.setEndDate(yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
    DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(activitySetVo), activitySetVo);
  }

  /**
   * 關閉活動
   *
   * @param joinCodeDto
   */
  public void closeActivityByJoinCode(JoinCodeDto joinCodeDto) {
    final String endDate = yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now());
    ActivityBasicVo activityBasicVo = activityBasicDao.getByJoinCode(joinCodeDto.getJoinCode());
    activityBasicVo.setActStatus(ActivityEnum.Status.FINISH.val());
    activityBasicVo.setEndDate(endDate);
    DaoValidationUtil.validateResultIsOne(() -> activityBasicDao.update(activityBasicVo), activityBasicVo);

    // 所有參加者強制高歌離席
    final ActivityEnum.SetStatus statusEnum = ActivityEnum.SetStatus.LEAVE;
    activitySetDao.query(MapUtil.newHashMap("actNum", activityBasicVo.getActNum())).stream()
        .filter(vo -> statusEnum != ActivityEnum.SetStatus.getEnum(vo.getStatus()))
        .map(vo -> {
          vo.setStatus(statusEnum.val());
          vo.setEndDate(endDate);
          return vo;
        })
        .forEach(vo -> DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(vo), vo));
  }

  /** ================================================= validation ================================================= */

  /**
   * 檢核主辦人有沒有尚未結束的活動
   *
   * @param userNum
   */
  public void validMultiActivity(Long userNum) {
    final boolean hasMultiActivity = activityBasicDao.query(MapUtil.newHashMap("userNum", userNum)).stream()
        .map(ActivityBasicVo::getActStatus)
        .map(ActivityEnum.Status::getEnum)
        .anyMatch(e -> ActivityEnum.Status.FINISH != e);
    if (hasMultiActivity) throw new MultiActivityException();
  }

  /**
   * 檢核是否有此活動
   *
   * @param joinCode
   */
  public void validActivityExists(String joinCode) {
    ActivityBasicVo activityBasicVo = activityBasicDao.getByJoinCode(joinCode);
    if (activityBasicVo == null) throw new ActivityNotFoundException();
    if (ActivityEnum.Status.getEnum(activityBasicVo.getActStatus()) == ActivityEnum.Status.FINISH)
      throw new ActivityClosedException();
  }

  /**
   * 檢核參與者所屬的活動是否正確
   *
   * @param joinCode
   * @param userNum
   */
  public void validActivitySetExists(String joinCode, Long userNum) {
    ActivitySetVo activitySetVo = activitySetDao.getInProgressActivity(userNum, ActivityEnum.SetStatus.JOIN.val());
    if (activitySetVo == null) throw new UserWithoutActivityException();
    if (!joinCode.equals(activitySetVo.getJoinCode())) throw new ActivityDifferentException();
    if (userNum.equals(activitySetVo.getHostUserNum())) throw new HostLeavingException();
  }

  /**
   * 檢核是否為主持人
   *
   * @param userNum
   * @param joinCode
   */
  public void validUserIsHost(Long userNum, String joinCode) {
    Optional.ofNullable(activityBasicDao.getByJoinCode(joinCode))
        .map(ActivityBasicVo::getUserNum)
        .filter(userNum::equals)
        .orElseThrow(UserNotHostException::new);
  }

  /** =================================================== private ================================================== */


  /**
   * 活動第一位參與者
   *
   * @param userNum
   * @param actNum
   * @param startDate
   */
  private void saveHostActivitySet(Long userNum, Long actNum, String startDate) {
    ActivitySetVo activitySetVo = new ActivitySetVo();
    activitySetVo.setUserNum(userNum);
    activitySetVo.setActNum(actNum);
    activitySetVo.setStartDate(startDate);
    activitySetVo.setStatus(ActivityEnum.SetStatus.JOIN.val());
    activitySetDao.insert(activitySetVo);

    // 將所有被邀請的群狀態都壓成拒絕
    activitySetDao.query(MapUtil.newHashMap("userNum", userNum)).stream()
        .filter(set -> ActivityEnum.SetStatus.INVITE.val().equals(set.getStatus()))
        .map(set -> {
          set.setStatus(ActivityEnum.SetStatus.CANCEL.val());
          return set;
        })
        .forEach(set -> DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(set), set));
  }


//  /**
//   * 建立活動並回傳邀請碼v1
//   * todo v2重構
//   *
//   * @param activityBasicVo
//   * @return
//   */
//  public String saveActivityFirstVersion(ActivityBasicVo activityBasicVo) {
//    // 組裝邀請碼
//    final String joinCode = RandomStringUtils.random(8, true, true).toLowerCase();
//    activityBasicVo.setJoinCode(joinCode);
//    activityBasicDao.insert(activityBasicVo);
//    // 建立人員成為第一名參與者
//    saveFirstActSet(activityBasicVo.getUserNum(), activityBasicVo.getActNum(), activityBasicVo.getCreateDate());
//    return joinCode;
//  }
//
//
//  /** =================================================== private ================================================== */
//
//  /**
//   * 建立一筆活動
//   * todo v2重構
//   *
//   * @param activityBasicVo
//   */
//  public Long saveActivity(ActivityBasicVo activityBasicVo) {
//    final String nowDate = yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now());
//    final String startDate = activityBasicVo.getStartDate();
//    final Long userNum = RequestScopeUtil.getUserNum();
//    activityBasicVo.setUserNum(userNum);
//    activityBasicVo.setActStatus(ActivityEnum.Status.PREPARE.val());
//    activityBasicVo.setStartDate(startDate == null ? nowDate : startDate);
//    activityBasicVo.setCreateDate(nowDate);
//    // 組裝邀請碼
//    activityBasicVo.setJoinCode(RandomStringUtils.random(8, true, true).toLowerCase());
//    activityBasicDao.insert(activityBasicVo);
//    // 建立人員成為第一名參與者
//    final Long actNum = activityBasicVo.getActNum();
//    saveFirstActSet(userNum, actNum, nowDate);
//    return actNum;
//  }
//
//  /**
//   * 取得當前活動的資料
//   *
//   * @param userNum
//   * @return
//   */
//  public ActivityViewRvo getCurrentActivity(Long userNum) {
//    return activityBasicDao.getCurrentActivityView(userNum);
//  }
//
//  /**
//   * 取得邀請名單
//   *
//   * @param userNum
//   * @return
//   */
//  public List<AppUserVo> getUserListToInviteList(Long userNum) {
//    return activitySetDao.listUserListByActivityHistory(userNum).stream()
//        .filter(user -> !userNum.equals(user.getUserNum()))
//        .collect(Collectors.toList());
//  }
//
//  /**
//   * 取得現有的活動邀請碼
//   *
//   * @param userNumList
//   * @return
//   */
//  public InviteJoinCodeMo getJoinCodeByCurrentActivity(List<Long> userNumList) {
//    ActivityBasicVo activityBasicVo = activityBasicDao.getOwnerActivityInfo(RequestScopeUtil.getUserNum());
//    final Long actNum = activityBasicVo.getActNum();
//    // 邀請名單建立邀請物件
//    userNumList.stream()
//        .filter(num -> !Objects.equals(RequestScopeUtil.getUserNum(), num))
//        .map(num -> {
//          ActivitySetVo activitySetVo = new ActivitySetVo();
//          activitySetVo.setUserNum(num);
//          activitySetVo.setStatus(ActivityEnum.SetStatus.INVITE.val());
//          activitySetVo.setStartDate(yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
//          activitySetVo.setActNum(actNum);
//          return activitySetVo;
//        })
//        .forEach(activitySetDao::insert);
//    // 回傳參加邀請碼
//    return new InviteJoinCodeMo(
//        activityBasicVo.getJoinCode(), activityBasicVo.getActTitle(), activityBasicVo.getHostUserName(), userNumList);
//  }
//
//  /**
//   * 上傳暫存圖片,發送圖片訊息
//   *
//   * @param event
//   * @param userNum
//   */
//  public void replyQrcode(MessageEvent<TextMessageContent> event, Long userNum) {
//    final String replyToken = event.getReplyToken();
//    final String userId = event.getSource().getUserId();
//    // 製作QRcode上傳
//    final URI imagePath = activityBasicDao.query(MapUtil.newHashMap("userNum", userNum)).stream()
//        .filter(act -> act.getEndDate() == null && !ActivityEnum.Status.FINISH.val().equals(act.getActStatus()))
//        .findAny()
//        .map(ActivityBasicVo::getJoinCode)
//        .map(JoinCodeUtil::genJoinCode)
//        .map(URL::concat)
//        .map(QrcodeUtil::generateQrcode)
//        .map(ByteArrayOutputStream::toByteArray)
//        .map(UploadImageRequestBody::new)
//        .map(uploadCcService::upload)
//        .map(URI::create)
//        .orElseThrow(QrcodeUploadException::new);
//
//    messageMod.replyQrcode(imagePath, replyToken, userId);
//  }
//
//  /** ================================================= validation ================================================= */
//
//  /**
//   * 檢核使用者是否有正在進行的活動
//   *
//   * @param userNum
//   */
//  public void validActivityInProgress(Long userNum, boolean needExists) {
//    ActivitySetVo activitySetVo = activitySetDao.getInProgressActivity(userNum, ActivityEnum.SetStatus.JOIN.val());
//    final boolean isUserJoinActivity = activitySetVo.getAcsNum() != null;
//    if (!needExists && isUserJoinActivity) {
//      throw new ActivityInProgressException(activitySetVo.getActTitle(), activitySetVo.getHostUserName(), true);
//    } else if (needExists && !isUserJoinActivity) {
//      throw new ActivityNotFoundException();
//    }
//  }
//
//  /**
//   * 檢核過去有沒有一同參與活動的使用者
//   *
//   * @param userNum
//   */
//  public void validHistoryActivitySetUserList(Long userNum) {
//    final long userNumCount = activitySetDao.listUserNumByActivityHistory(userNum).stream()
//        .filter(num -> !num.equals(userNum))
//        .count();
//    if (userNumCount == 0) {
//      throw new UserNotFoundException();
//    }
//  }
//
//  /** =================================================== private ================================================== */
//


}
