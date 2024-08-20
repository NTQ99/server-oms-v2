package ntq.server.common.util;

import java.lang.invoke.MethodHandles;

public class EnvironmentInitializer {
  private EnvironmentInitializer() {
    throw new UnsupportedOperationException();
  }

  public static void initialize() {
    try {
      var lookup = MethodHandles.lookup();
      lookup.ensureInitialized(ErrorCode.class);
      lookup.ensureInitialized(Json.class);
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException("Can't init java class", e);
    }
  }
}
