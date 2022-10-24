package org.chun.plutus.api.mod;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.chun.plutus.common.dao.ActivityBasicDao;
import org.chun.plutus.common.dao.ActivityDtDao;
import org.chun.plutus.common.dao.ActivitySetDao;
import org.chun.plutus.common.dao.AppUserDao;
import org.chun.plutus.common.dto.LineUserDto;
import org.chun.plutus.common.dto.PaymentTimestampDto;
import org.chun.plutus.common.dto.QrcodeUrlDto;
import org.chun.plutus.common.enums.ActivityEnum;
import org.chun.plutus.common.enums.MenuEnum;
import org.chun.plutus.common.exceptions.ActivityClosedException;
import org.chun.plutus.common.exceptions.ActivityDifferentException;
import org.chun.plutus.common.exceptions.ActivityDtNotFoundException;
import org.chun.plutus.common.exceptions.ActivityNotFoundException;
import org.chun.plutus.common.exceptions.CustomValueEmptyException;
import org.chun.plutus.common.exceptions.EmptyPartnerException;
import org.chun.plutus.common.exceptions.FunctionNotSupportException;
import org.chun.plutus.common.exceptions.HostLeavingException;
import org.chun.plutus.common.exceptions.MultiActivityException;
import org.chun.plutus.common.exceptions.PayTypeChangeAlreadyException;
import org.chun.plutus.common.exceptions.UserNotHostException;
import org.chun.plutus.common.exceptions.UserWithoutActivityException;
import org.chun.plutus.common.rvo.ActivityViewRvo;
import org.chun.plutus.common.vo.ActivityBasicVo;
import org.chun.plutus.common.vo.ActivityDtVo;
import org.chun.plutus.common.vo.ActivitySetVo;
import org.chun.plutus.common.vo.AppUserVo;
import org.chun.plutus.util.DaoValidationUtil;
import org.chun.plutus.util.JsonBean;
import org.chun.plutus.util.MapUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.chun.plutus.common.constant.LineChannelViewConst.ACTIVITY_VIEW;
import static org.chun.plutus.common.constant.LineChannelViewConst.GLOBAL_VIEW;
import static org.chun.plutus.common.constant.LineChannelViewConst.GUEST_VIEW;
import static org.chun.plutus.common.constant.LineChannelViewConst.NONE_CLOSE;
import static org.chun.plutus.common.constant.LineCommonMessageConst.COST_SETTING_ALREADY;
import static org.chun.plutus.common.constant.LineCommonMessageConst.PAYER_SETTING_ALREADY;
import static org.chun.plutus.common.constant.LineCommonMessageConst.TITLE_SETTING_ALREADY;
import static org.chun.plutus.common.constant.LineCommonMessageConst.TYPE_AVERAGE_STR;
import static org.chun.plutus.common.constant.LineCommonMessageConst.TYPE_CHOICE_STR;
import static org.chun.plutus.common.constant.LineCommonMessageConst.TYPE_SCALE_STR;
import static org.chun.plutus.common.constant.LineCommonMessageConst.TYPE_SETTING_ALREADY;
import static org.chun.plutus.common.enums.MenuEnum.Setting.COST;
import static org.chun.plutus.common.enums.MenuEnum.Setting.PAYER;
import static org.chun.plutus.common.enums.MenuEnum.Setting.TITLE;
import static org.chun.plutus.common.enums.MenuEnum.Setting.TYPE_AVERAGE;
import static org.chun.plutus.common.enums.MenuEnum.Setting.TYPE_CHOICE;
import static org.chun.plutus.common.enums.MenuEnum.Setting.TYPE_SCALE;
import static org.chun.plutus.util.MomentUtil.Date.yyyyMMdd;
import static org.chun.plutus.util.MomentUtil.Date.yyyy_MM_dd;
import static org.chun.plutus.util.MomentUtil.DateTime.yyyy_MM_dd_HH_mm_ss;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActivityMod {

  private final ActivityBasicDao activityBasicDao;
  private final ActivitySetDao activitySetDao;
  private final ActivityDtDao activityDtDao;
  private final AppUserDao appUserDao;

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
          vo.setEndDate(now);
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

    // 有一個參加者就將活動改為進行中
    if (ActivityEnum.Status.PREPARE == ActivityEnum.Status.getEnum(activityBasicVo.getActStatus())) {
      activityBasicVo.setActStatus(ActivityEnum.Status.PROGRESS.val());
    }

    return activityBasicVo;
  }

  /**
   * 離開活動 狀態壓成離開
   *
   * @param lineUserDto
   */
  public void leaveActivityByJoinCode(LineUserDto lineUserDto) {
    ActivitySetVo activitySetVo = activitySetDao.getByUserNumAndJoinCode(lineUserDto.getUserNum(), lineUserDto.getJoinCode());
    activitySetVo.setStatus(ActivityEnum.SetStatus.LEAVE.val());
    activitySetVo.setEndDate(yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
    DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(activitySetVo), activitySetVo);
  }

  /**
   * 關閉活動
   *
   * @param lineUserDto
   */
  public void closeActivityByJoinCode(LineUserDto lineUserDto) {
    final String endDate = yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now());
    ActivityBasicVo activityBasicVo = activityBasicDao.getByJoinCode(lineUserDto.getJoinCode());
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

    // 最後時間點為最後一個活動節點
    this.saveActivityNode(lineUserDto);
  }

  /**
   * 儲存活動節點
   *
   * @param lineUserDto
   */
  public void saveActivityNode(LineUserDto lineUserDto) {
    final String joinCode = lineUserDto.getJoinCode();
    ActivityDtVo lastActivityDtVo = activityDtDao.getLastActivityByJoinCode(joinCode);
    ActivityDtVo nextActivityDtVo = new ActivityDtVo();
    nextActivityDtVo.setPrePaidUser(lineUserDto.getUserNum()); //v2調整,可選擇
    nextActivityDtVo.setPayType(ActivityEnum.PayType.DEFAULT.val());
    if (lastActivityDtVo == null) {
      ActivityBasicVo activityBasicVo = activityBasicDao.getByJoinCode(joinCode);
      nextActivityDtVo.setActNum(activityBasicVo.getActNum());
      nextActivityDtVo.setAcdTitle(activityBasicVo.getActTitle());
      nextActivityDtVo.setStartDate(activityBasicVo.getStartDate());
      nextActivityDtVo.setEndDate(activityBasicVo.getEndDate());
    } else {
      nextActivityDtVo.setActNum(lastActivityDtVo.getActNum());
      nextActivityDtVo.setAcdTitle(lastActivityDtVo.getAcdTitle());
      nextActivityDtVo.setStartDate(lastActivityDtVo.getEndDate());
      //把前一個關掉
      lastActivityDtVo.setEndDate(yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()));
      DaoValidationUtil.validateResultIsOne(() -> activityDtDao.update(lastActivityDtVo), lastActivityDtVo);
    }
    activityDtDao.insert(nextActivityDtVo);
  }

  /**
   * 取得使用者當下正在主持&進行的活動
   *
   * @param userNum
   * @return
   */
  public ActivityDtVo getUserCurrentActivityNode(Long userNum) {
    ActivityDtVo activityDtVo = activityDtDao.getLastActivityByUserNum(userNum);
    if (ActivityEnum.Status.PREPARE == ActivityEnum.Status.getEnum(activityDtVo.getActStatus())) {
      throw new EmptyPartnerException();
    }
    return activityDtVo;
  }

  /**
   * 設定活動節點
   *
   * @param settingEnum
   * @param commandValue
   * @param acdNum
   */
  public String setNodeWithCustomValue(MenuEnum.Setting settingEnum, String commandValue, Long acdNum) {
    ActivityDtVo activityDtVo = new ActivityDtVo();
    activityDtVo.setAcdNum(acdNum);
    String messageContent;
    switch (settingEnum) {
      case TITLE:
        messageContent = String.format(TITLE_SETTING_ALREADY, commandValue);
        activityDtVo.setAcdTitle(commandValue);
        break;
      case COST:
        messageContent = String.format(COST_SETTING_ALREADY, commandValue);
        activityDtVo.setCost(BigDecimal.valueOf(Double.parseDouble(commandValue)));
        break;
      case PAYER:
        messageContent = String.format(PAYER_SETTING_ALREADY, commandValue);
        final Long payerUserNum = getPayerUserNum(commandValue);
        if (payerUserNum != null) {
          activityDtVo.setPrePaidUser(payerUserNum);
        }
        break;
      default:
        String typeName;
        if (settingEnum == TYPE_AVERAGE) {
          typeName = TYPE_AVERAGE_STR;
          activityDtVo.setPayType(TYPE_AVERAGE.val());
        } else if (settingEnum == TYPE_SCALE) {
          typeName = TYPE_SCALE_STR;
          activityDtVo.setPayType(TYPE_SCALE.val());
        } else if (settingEnum == TYPE_CHOICE) {
          typeName = TYPE_CHOICE_STR;
          activityDtVo.setPayType(TYPE_CHOICE.val());
        } else {
          typeName = Strings.EMPTY;
        }
        messageContent = TYPE_SETTING_ALREADY.concat(typeName);
    }

    DaoValidationUtil.validateResultIsOne(() -> activityDtDao.update(activityDtVo), activityDtVo);
    return messageContent;
  }

  /**
   * 依據角色取得不同的檢視資訊
   *
   * @param joinCode
   * @param userNum
   * @return
   */
  @SneakyThrows
  public String sendActivityViewDistinguishRole(String joinCode, Long userNum) {
    ActivityViewRvo activityViewRvo = activityBasicDao.getCurrentActivityView(joinCode, userNum);
    log.info("{}", JsonBean.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(activityViewRvo));
    if (activityViewRvo == null) {
      ActivityBasicVo activityQueryVo = new ActivityBasicVo();
      activityQueryVo.setJoinCode(joinCode);
      // 1:沒有活動 2:沒有參加活動
      if (0 == activityBasicDao.count(activityQueryVo)) {
        throw new ActivityNotFoundException();
      }
      throw new UserWithoutActivityException();
    }

    // 子活動標題
    final List<ActivityDtVo> activityDtVoList = activityViewRvo.getActivityDtVoList();
    List<String> acdTitleList = activityDtVoList.stream()
        .map(ActivityDtVo::getAcdTitle)
        .collect(Collectors.toList());
    // 處理個人消費金額
    BigDecimal personalPay = handleSubActivityPersonalPayment(activityDtVoList, userNum, activityViewRvo.getActNum());
    // 取出變數組裝訊息
    final String actTitle = activityViewRvo.getActTitle();
    final String hostUserName = activityViewRvo.getHostUserName();
    final Long userCount = activityViewRvo.getUserCount();
    final String nowDate = yyyy_MM_dd.format(LocalDate.now());
    final String userName = activityViewRvo.getUserName();
    final BigDecimal currentCost = activityViewRvo.getCurrentCost();
    // 收入支出邏輯
    long pay = personalPay.longValue();
    final boolean isPayer = pay > 0;
    if (!isPayer) {
      pay = -pay;
    }

    StringBuilder view = new StringBuilder();
    view.append(String.format(GLOBAL_VIEW, actTitle, hostUserName, nowDate, userCount, currentCost.longValue(),
        yyyy_MM_dd_HH_mm_ss.format(LocalDateTime.now()), userName, pay));

    // 組裝子活動
    if (!activityDtVoList.isEmpty()) {
      view.append(ACTIVITY_VIEW);
      if (activityViewRvo.getIsHost()) {
        activityDtVoList.forEach(dtVo -> {
          String endDate = dtVo.getEndDate();
          if (endDate == null) {
            endDate = NONE_CLOSE;
          }
          view.append(String.format(GUEST_VIEW, dtVo.getAcdTitle(), dtVo.getStartDate(), endDate,
              dtVo.getCost().longValue(), ActivityEnum.PayType.getEnum(dtVo.getPayType()).getSimpleName(), dtVo.getPayerName()));
        });

      } else {
        acdTitleList.forEach(title -> view.append(title.concat("\n")));
      }
    }

    final String rtnStr = view.toString();
    return isPayer
        ? rtnStr.replace("目前消費", "目前收款")
        : rtnStr;
  }

  /**
   * 取得參加中的邀請碼
   *
   * @param userNum
   * @return
   */
  public String getJoinCodeBySetUserNum(Long userNum) {
    return Optional.ofNullable(activityBasicDao.getActivityBySetUserNum(userNum))
        .map(ActivityBasicVo::getJoinCode)
        .orElse(null);
  }

  /**
   * 取得活動邀請qrcode的路徑
   *
   * @param joinCode
   * @return
   */
  public QrcodeUrlDto getQrcodeUrlByJoinCode(String joinCode) {
    ActivityBasicVo activityBasicVo = Optional.ofNullable(activityBasicDao.getByJoinCode(joinCode))
        .orElseThrow(ActivityNotFoundException::new);
    return new QrcodeUrlDto(activityBasicVo.getActNum(), activityBasicVo.getQrcodeUrl());
  }

  /**
   * qrcode路徑寫入db
   *
   * @param actNum
   * @param qrcodeUrl
   */
  public void saveQrcodeUrlInActivityVo(Long actNum, String qrcodeUrl) {
    ActivityBasicVo activityBasicVo = new ActivityBasicVo();
    activityBasicVo.setActNum(actNum);
    activityBasicVo.setQrcodeUrl(qrcodeUrl);
    DaoValidationUtil.validateResultIsOne(() -> activityBasicDao.update(activityBasicVo), activityBasicVo);
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
    if (hasMultiActivity) {
      throw new MultiActivityException();
    }
  }

  /**
   * 檢核是否有此活動
   *
   * @param joinCode
   */
  public void validActivityExists(String joinCode) {
    ActivityBasicVo activityBasicVo = activityBasicDao.getByJoinCode(joinCode);
    if (activityBasicVo == null) {
      throw new ActivityNotFoundException();
    }
    if (ActivityEnum.Status.getEnum(activityBasicVo.getActStatus()) == ActivityEnum.Status.FINISH) {
      throw new ActivityClosedException();
    }
  }

  /**
   * 檢核參與者所屬的活動是否正確
   *
   * @param joinCode
   * @param userNum
   */
  public void validActivitySetExists(String joinCode, Long userNum, boolean isLeave) {
    ActivitySetVo activitySetVo = activitySetDao.getInProgressActivity(userNum, ActivityEnum.SetStatus.JOIN.val());
    if (activitySetVo == null) {
      throw new UserWithoutActivityException();
    }
    if (!joinCode.equals(activitySetVo.getJoinCode())) {
      throw new ActivityDifferentException();
    }
    if (isLeave && userNum.equals(activitySetVo.getHostUserNum())) {
      throw new HostLeavingException();
    }
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

  /**
   * 檢核使用者有正在進行中的活動
   *
   * @param userNum
   */
  public void validUserActivityExists(Long userNum) {
    ActivityBasicVo activityQueryVo = new ActivityBasicVo();
    activityQueryVo.setUserNum(userNum);
    activityQueryVo.setActStatus(ActivityEnum.Status.PROGRESS.val());
    if (0 == activityBasicDao.count(activityQueryVo)) {
      throw new ActivityNotFoundException();
    }
  }

  /**
   * 檢核menu的值
   *
   * @param settingEnum
   * @param commandValue
   * @param originPayType
   */
  public void validSubMenuAction(MenuEnum.Setting settingEnum, String commandValue, String originPayType) {
    MenuEnum.Setting[] settingSettings = new MenuEnum.Setting[]{TITLE, COST, PAYER};
    MenuEnum.Setting[] payTypeSettings = new MenuEnum.Setting[]{TYPE_AVERAGE, TYPE_SCALE, TYPE_CHOICE};
    if (Arrays.asList(settingSettings).contains(settingEnum) && StringUtils.isBlank(commandValue)) {
      throw new CustomValueEmptyException();
    }
    if (Arrays.asList(payTypeSettings).contains(settingEnum) &&
        ActivityEnum.PayType.getEnum(originPayType) != ActivityEnum.PayType.DEFAULT) {
      throw new PayTypeChangeAlreadyException();
    }
  }

  /**
   * 檢核節點是否建立
   *
   * @param lineUserDto
   */
  public void validActivityDtExists(LineUserDto lineUserDto) {
    final ActivityDtVo activityDtVo = activityDtDao.getLastActivityByUserNum(lineUserDto.getUserNum());
    if (activityDtVo == null) {
      throw new ActivityDtNotFoundException();
    }
    if (ActivityEnum.Status.PREPARE == ActivityEnum.Status.getEnum(activityDtVo.getActStatus())) {
      throw new EmptyPartnerException();
    }
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
    // 將所有被邀請的群狀態都壓成拒絕
    activitySetDao.query(MapUtil.newHashMap("userNum", userNum)).stream()
        .filter(set -> {
          ActivityEnum.SetStatus setStatusEnum = ActivityEnum.SetStatus.getEnum(set.getStatus());
          return ActivityEnum.SetStatus.JOIN == setStatusEnum || ActivityEnum.SetStatus.INVITE == setStatusEnum;
        })
        .map(set -> {
          set.setStatus(ActivityEnum.SetStatus.LEAVE.val());
          return set;
        })
        .forEach(set -> DaoValidationUtil.validateResultIsOne(() -> activitySetDao.update(set), set));

    // 新增現有加入狀態
    ActivitySetVo activitySetVo = new ActivitySetVo();
    activitySetVo.setUserNum(userNum);
    activitySetVo.setActNum(actNum);
    activitySetVo.setStartDate(startDate);
    activitySetVo.setStatus(ActivityEnum.SetStatus.JOIN.val());
    activitySetDao.insert(activitySetVo);
  }

  /**
   * 取得預先付款人員
   *
   * @param commandValue
   * @return
   */
  private Long getPayerUserNum(String commandValue) {
    final String userName = commandValue.substring(1).trim();
    return appUserDao.query(MapUtil.newHashMap("userLineName", userName)).stream()
        .findAny()
        .map(AppUserVo::getUserNum)
        .orElse(null);
  }

  private BigDecimal handleSubActivityPersonalPayment(List<ActivityDtVo> activityDtVoList, Long userNum, Long actNum) {
    // 取得參與者清單
    List<ActivitySetVo> activitySetVoList = activitySetDao.query(MapUtil.newHashMap("actNum", actNum)).stream()
        .filter(vo -> vo.getEndDate() == null
            || ActivityEnum.SetStatus.LEAVE == ActivityEnum.SetStatus.getEnum(vo.getStatus()))
        .collect(Collectors.toList());
    BigDecimal personalPayment = BigDecimal.ZERO;


    for (ActivityDtVo activityDtVo : activityDtVoList) {
      BigDecimal pay = BigDecimal.ZERO;

      // 先判斷是不是先付錢的人
      final boolean isPayer = userNum.equals(activityDtVo.getPrePaidUser());
      final BigDecimal cost = activityDtVo.getCost();
      final ActivityEnum.PayType payTypeEnum = ActivityEnum.PayType.getEnum(activityDtVo.getPayType());
      final LocalDateTime startDate = yyyy_MM_dd_HH_mm_ss.parse(activityDtVo.getStartDate());
      final String endDateStr = activityDtVo.getEndDate();
      final LocalDateTime endDate = endDateStr == null
          ? LocalDateTime.now()
          : yyyy_MM_dd_HH_mm_ss.parse(endDateStr);

      switch (payTypeEnum) {
        case DEFAULT:
        case AVERAGE:
          long userCount = activitySetVoList.stream()
              .filter(vo -> {
                final String userLeaveDate = vo.getEndDate();
                return userLeaveDate == null || yyyy_MM_dd_HH_mm_ss.parse(userLeaveDate).compareTo(startDate) >= 0;
              })
              .count();
          pay = cost.divide(BigDecimal.valueOf(userCount), RoundingMode.HALF_UP);
          break;
        case SCALE:
          PaymentTimestampDto paymentTimestampDto = handlePaymentWithTimestamp(activitySetVoList, startDate, endDate, userNum);
          pay = cost.multiply(BigDecimal.valueOf(paymentTimestampDto.getUserTimestamp()))
              .divide(BigDecimal.valueOf(paymentTimestampDto.getTotalTimestamp()), RoundingMode.HALF_UP);
          break;
        case CHOICE:
          throw new FunctionNotSupportException();
      }

      personalPayment = isPayer
          ? personalPayment.add(pay)
          : personalPayment.subtract(pay);
    }

    return personalPayment;
  }

  /**
   * 依時間比例取得運算物件
   *
   * @param activitySetVoList
   * @param startDate
   * @param endDate
   * @param userNum
   * @return
   */
  private PaymentTimestampDto handlePaymentWithTimestamp(List<ActivitySetVo> activitySetVoList, LocalDateTime startDate, LocalDateTime endDate, Long userNum) {
    long totalTime = 0L;
    long userTime = 0L;
    final long joinStartTimestamp = Timestamp.valueOf(startDate).getTime();
    for (ActivitySetVo activitySetVo : activitySetVoList) {
      final String userLeaveDate = activitySetVo.getEndDate();
      if (userLeaveDate == null || yyyy_MM_dd_HH_mm_ss.parse(userLeaveDate).compareTo(startDate) >= 0) {

        final long joinEndTimestamp = userLeaveDate == null
            ? Timestamp.valueOf(endDate).getTime()
            : Timestamp.valueOf(userLeaveDate).getTime();
        final long userJoinTimestamp = joinEndTimestamp - joinStartTimestamp;
        if (userNum.equals(activitySetVo.getUserNum())) {
          userTime = userJoinTimestamp;
        }
        totalTime = totalTime + userJoinTimestamp;
      }
    }

    return new PaymentTimestampDto(userTime, totalTime);
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
