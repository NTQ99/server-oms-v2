package ntq.server.common.util;

public class Constant {
  public static final String PREFIX_RESPONSE_CODE;
  public static final String SYSTEM_USER = "system";

  static {
    PREFIX_RESPONSE_CODE = System.getProperty("server.response.prefix-code", "CODE-");
  }

  private Constant() {
    throw new UnsupportedOperationException();
  }
}
