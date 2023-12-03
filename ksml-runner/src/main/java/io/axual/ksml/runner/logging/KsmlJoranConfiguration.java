package io.axual.ksml.runner.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.DefaultJoranConfigurator;
import ch.qos.logback.core.LogbackException;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class KsmlJoranConfiguration extends DefaultJoranConfigurator {

  private static final String ENVIRONMENT_KEY_PREFIX_1 = "KSML_LOGGER_";
  private static final String SYSTEM_PROP_PREFIX_1 = "ksml.logger.";
  private static final String SYSTEM_PROP_PREFIX_2 = "ksml_logger_";

  @Override
  public ExecutionStatus configure(LoggerContext context) {
    var executionStatus = super.configure(context);
    var fromEnvLoggers = String.join(System.lineSeparator(), loadFromEnv());
    var fromSysLoggers = loadFromSystemProperties();

    var props = new Properties();
    try (var stringReader = new StringReader(fromEnvLoggers)) {
      props.load(stringReader);
    } catch (IOException e) {
      throw new LogbackException("Could not parse loggers from environment variables", e);
    }
    // Add all the system environment loggers
    props.putAll(fromSysLoggers);

    for (var loggerName : props.stringPropertyNames()) {
      var logLevelStr = props.getProperty(loggerName);
      var logLevel = Level.toLevel(logLevelStr, null);
      if (logLevel != null) {
        context.getLogger(loggerName).setLevel(logLevel);
      } else {
        throw new LogbackException(
            String.format("Invalid log level found. Logger %s, level %s", loggerName, logLevelStr));
      }
    }

    return executionStatus;
  }


  private Collection<String> loadFromEnv() {
    return Maps.filterKeys(System.getenv(), this::isEnvProperty).values();
  }

  Map<String, String> loadFromSystemProperties() {
    final var loggerProps = new HashMap<String, String>();

    final var props = System.getProperties();
    for (var key : props.stringPropertyNames()) {
      var sysKey = createSysProperty(key);
      var val = props.get(key);
      if (sysKey != null && val != null) {
        loggerProps.put(sysKey, String.valueOf(val));
      }
    }

    return loggerProps;
  }

  private String createSysProperty(String sysKey) {
    if (StringUtils.isBlank(sysKey)) {
      return null;
    }
    var cleanKey = sysKey.toLowerCase();
    if (cleanKey.startsWith(SYSTEM_PROP_PREFIX_1)) {
      return sysKey.substring(SYSTEM_PROP_PREFIX_1.length());
    } else if (cleanKey.startsWith(SYSTEM_PROP_PREFIX_2)) {
      return sysKey.substring(SYSTEM_PROP_PREFIX_2.length()).replaceAll("_", ".");
    }
    return null;
  }

  private boolean isEnvProperty(String envKey) {
    if (StringUtils.isBlank(envKey)) {
      return false;
    }
    var cleanKey = envKey.trim().toUpperCase();
    return cleanKey.startsWith(ENVIRONMENT_KEY_PREFIX_1);
  }

}
