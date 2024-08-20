package ntq.server.common.script;

import ntq.server.common.exception.BusinessException;
import ntq.server.common.util.CommonErrorCode;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ClassBodyEvaluator;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.ScriptEvaluator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.StringReader;

public class ScriptFactory {
  private final ApplicationContext applicationContext;
  private final AutowireCapableBeanFactory autowireCapableBeanFactory;

  public ScriptFactory(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
  }

  public <T> T createScript(Class<T> clazz, CodeType codeType, String sourceCode, String... paramNames) throws CompileException {
    return switch (codeType) {
      case EXPRESSION -> fromExpression(clazz, sourceCode, paramNames);
      case SCRIPT -> fromScript(clazz, sourceCode, paramNames);
      case CLASS_BODY -> autowireBean(fromClassBody(new Class[]{clazz, Lifecycle.class}, sourceCode));
      case CLASS -> autowireBean(fromClass(sourceCode));
      case BEAN -> fromBean(sourceCode);
    };
  }

  public static <T> T fromExpression(Class<T> clazz, String sourceCode, String... paramNames) throws CompileException {
    var ee = new ExpressionEvaluator();
    ee.setTargetVersion(17);
    return ee.createFastEvaluator(sourceCode, clazz, paramNames);
  }

  public static <T> T fromScript(Class<T> clazz, String sourceCode, String... paramNames) throws CompileException {
    var se = new ScriptEvaluator();
    se.setTargetVersion(17);
    return se.createFastEvaluator(sourceCode, clazz, paramNames);
  }

  public static <T> T fromClassBody(Class<T> clazz, String sourceCode) throws CompileException {
    return fromClassBody(new Class[]{clazz}, sourceCode);
  }

  @SuppressWarnings("unchecked")
  private static <T> T fromClassBody(Class<?>[] implementedInterfaces, String sourceCode) throws CompileException {
    try {
      var cbe = new ClassBodyEvaluator();
      cbe.setImplementedInterfaces(implementedInterfaces);
      cbe.setTargetVersion(17);
      return (T) cbe.createInstance(new StringReader(sourceCode));
    } catch (IOException e) {
      throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "never happen error", e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T fromClass(String sourceCode) {
    try {
      var sourceClass = Class.forName(sourceCode);
      return (T) sourceClass.getConstructor().newInstance();
    } catch (Exception e) {
      throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "Invalid class name: " + sourceCode, e);
    }
  }

  @SuppressWarnings("unchecked")
  private <T> T fromBean(String sourceCode) {
    return (T) applicationContext.getBean(sourceCode);
  }

  private <T> T autowireBean(T t) {
    autowireCapableBeanFactory.autowireBean(t);
    if (t instanceof Lifecycle lifecycle) {
      try {
        lifecycle.init();
      } catch (RuntimeException e) {
        lifecycle.destroy();
        throw e;
      }
    }
    return t;
  }

  public void destroy(Object script) {
    if (script instanceof Lifecycle lifecycle) {
      lifecycle.destroy();
    }
  }
}
