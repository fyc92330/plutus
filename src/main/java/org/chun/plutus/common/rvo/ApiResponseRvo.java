package org.chun.plutus.common.rvo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApiResponseRvo {

  /** 回傳結果 */
  private List<Map<String, Object>> results;
  /** 回傳錯誤訊息 */
  private List<String> errors;
  /** 狀態碼號 */
  private Integer httpStatusCode;
  /** 狀態碼 */
  private String httpStatus;

  public ApiResponseRvo(Map<String, Object> result) {
    Optional.ofNullable(results).orElseGet(ArrayList::new).add(result);
  }

  public ApiResponseRvo(String errorMsg) {
    Optional.ofNullable(errors).orElseGet(ArrayList::new).add(errorMsg);
  }

}
