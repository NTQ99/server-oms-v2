package vn.com.mbbank.steady.core.util;

import vn.com.mbbank.steady.common.util.Json;

import java.lang.invoke.MethodHandles;

public class EnvironmentInitializer {
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
