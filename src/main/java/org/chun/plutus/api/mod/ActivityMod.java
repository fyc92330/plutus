package org.chun.plutus.api.mod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chun.plutus.common.dao.ActivityBasicDao;
import org.chun.plutus.common.dao.ActivitySetDao;
import org.chun.plutus.common.enums.ActivityEnum;
import org.chun.plutus.common.exceptions.MultiActivityException;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.ActivitySetVo;
import org.chun.plutus.util.DaoValidationUtil;
import org.chun.plutus.util.MapUtil;
import org.chun.plutus.util.MomentUtil;
import org.chun.uploadcc.IUploadCcService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
   * 直接建立活動
   *
   * @param userNum
   * @param joinCode
   */
  public void forceCreateActivity(Long userNum, String joinCode) {
    Long actNum = null;
    final String now = MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now());
    for (ActivityBasicVo activityBasicVo : activityBasicDao.query(MapUtil.newHashMap("userNum", userNum))) {
      ActivityEnum.Status statusEnum;
      if (joinCode.equals(activityBasicVo.getJoinCode())) {
        statusEnum = ActivityEnum.Status.PROGRESS;
        actNum = activityBasicVo.getActNum();
      } else {
        statusEnum = ActivityEnum.Status.FINISH;
      }
      activityBasicVo.setActStatus(statusEnum.val());
      activityBasicVo.setStartDate(now);
      DaoValidationUtil.validateResultIsOne(() -> activityBasicDao.update(activityBasicVo), activityBasicVo);
    }

    this.saveFirstActSet(userNum, actNum, now);
  }


  /**
   * 活動第一位參與者
   *
   * @param userNum
   * @param actNum
   * @param startDate
   */
  private void saveFirstActSet(Long userNum, Long actNum, String startDate) {
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
