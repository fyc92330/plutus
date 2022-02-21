package org.chun.plutus.common.mo;

import lombok.Getter;
import lombok.Setter;
import org.chun.plutus.common.vo.PaymentTagVo;

import java.util.List;

@Getter
@Setter
public class TagGroupMo {

  /** 標籤群流水號 */
  private Long tagGroupNum;
  /** 標籤群名稱 */
  private String tagGroupName;
  /** 標籤群顏色 */
  private String tagGroupColor;
  /** 底下標籤 */
  private List<PaymentTagVo> paymentTagVoList;
  /** 移除的標籤流水號 */
  private List<Long> removeTagNumList;

}
