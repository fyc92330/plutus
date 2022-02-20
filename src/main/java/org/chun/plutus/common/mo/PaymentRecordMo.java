package org.chun.plutus.common.mo;

import lombok.Getter;
import lombok.Setter;
import org.chun.plutus.common.vo.PaymentTagGroupVo;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PaymentRecordMo {

  /** 帳本紀錄流水號 */
  private Long paymentNum;
  /** 使用者編號 */
  private Long userNum;
  /** 紀錄標題 */
  private String paymentTitle;
  /** 紀錄內容 */
  private String paymentDesc;
  /** 紀錄金額 */
  private BigDecimal paymentCost;
  /** 紀錄類型(1:收入,2:支出) */
  private String recordType;
  /** 紀錄標籤組 -> 標籤 */
  private List<PaymentTagGroupVo> paymentTagGroupVoList;

}
