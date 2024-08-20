package ntq.server.common.exception;

import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
public class RestClientException extends NoStackTraceException {
  private final int httpStatus;
  private final byte[] body;
  private String message;

  public RestClientException(int httpStatus, byte[] body) {
    super(null);
    this.httpStatus = httpStatus;
    this.body = body;
  }

  @Override
  public String getMessage() {
    var m = message;
    if (m == null) {
      m = httpStatus + " " + new String(body, StandardCharsets.UTF_8);
      message = m;
    }
    return m;
  }
}
