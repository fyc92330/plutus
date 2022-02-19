package org.chun.plutus.common.vo.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentUserBaseVo extends BaseVo {

  /** 使用者流水號 */
  private Long userNum;

  /** 使用者姓名 */
  private String userName;

  /** 使用者性別 */
  private String userGender;

  /** 使用者手機 */
  private String userMobile;

  /** 使用者信箱 */
  private String userEmail;

  /** 使用者LINE ID */
  private String lineUserId;

}
