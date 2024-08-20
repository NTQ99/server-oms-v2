package ntq.server.common.util;

import lombok.extern.log4j.Log4j2;
import ntq.server.common.exception.BusinessErrorCode;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ErrorCode implements CommonErrorCode {

  private static final Map<String, BusinessErrorCode> errorCodeMap;
  static {
    var codes = new HashMap<String, BusinessErrorCode>();
    var duplications = Arrays.stream(ErrorCode.class.getFields())
        .filter(f -> Modifier.isStatic(f.getModifiers()) && f.getType().equals(BusinessErrorCode.class))
        .map(f -> {
          try {
            return (BusinessErrorCode) f.get(null);
          } catch (IllegalAccessException e) {
            log.error("Can't load error code into map", e);
            throw new RuntimeException(e);
          }
        })
        .filter(c -> codes.put(Constant.PREFIX_RESPONSE_CODE + c.getCode(), c) != null)
        .toList();
    if (!duplications.isEmpty()) {
      throw new RuntimeException("Found error code duplication: " + duplications);
    }
    errorCodeMap = Map.copyOf(codes);
  }

  public static BusinessErrorCode lookup(String code) {
    return errorCodeMap.get(code);
  }

  public static BusinessErrorCode lookup(String code, BusinessErrorCode defaultErrorCode) {
    return errorCodeMap.getOrDefault(code, defaultErrorCode);
  }

  private ErrorCode() {
    throw new UnsupportedOperationException();
  }
}
