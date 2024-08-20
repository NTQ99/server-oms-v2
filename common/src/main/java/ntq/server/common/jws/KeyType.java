package ntq.server.common.jws;

public class KeyType {
  public static final String EC = "EC";
  public static final String RSA = "RSA";
  public static final String OCT = "OCT";

  private KeyType() {
    throw new UnsupportedOperationException();
  }
}
