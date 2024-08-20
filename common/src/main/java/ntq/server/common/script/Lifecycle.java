package ntq.server.common.script;

public interface Lifecycle {
  default void init() {
  }

  default void destroy() {
  }
}
