package com.chun.plutus.common.ro;

import com.chun.plutus.util.MapUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class PlutusApiRo {
  /** 回傳結果 */
  private List<Map<String, Object>> results;
  /** 回傳錯誤訊息 */
  private List<Map<String, Object>> errors;
  /** 狀態碼號 */
  private Integer httpStatusCode;
  /** 狀態碼 */
  private String httpStatus;

  public PlutusApiRo() {
  }

  public PlutusApiRo(Map<String, Object> result) {
    this.results = new ArrayList<>();
    this.results.add(result);
  }

  public PlutusApiRo(String errorMsg){
    if(this.errors==null){
      this.errors = new ArrayList<>();
      this.errors.add(MapUtil.newHashMap("message", errorMsg));
    }else{
      this.errors.get(0).put("message", errorMsg);
    }
  }
}
