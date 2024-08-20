package ntq.server.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private final transient BusinessErrorCode errorCode;

  public BusinessException(BusinessErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public BusinessException(BusinessErrorCode errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}
