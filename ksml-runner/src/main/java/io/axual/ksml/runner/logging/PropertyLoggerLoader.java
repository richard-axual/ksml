package io.axual.ksml.runner.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

@Slf4j
@NoArgsConstructor
public class PropertyLoggerLoader {

  private static final String ENVIRONMENT_KEY_PREFIX_1 = "KSML_LOGGER_";

  private static final String SYSTEM_PROP_PREFIX_1 = "ksml.logger.";
  private static final String SYSTEM_PROP_PREFIX_2 = "ksml_logger_";


  public void load() {
    if (LoggerFactory.getILoggerFactory() instanceof LoggerContext context) {
      var fromEnvLoggers = String.join(System.lineSeparator(), loadFromEnv());
      var fromSysLoggers = loadFromSystemProperties();

      var props = new Properties();
      try (var stringReader = new StringReader(fromEnvLoggers)) {
        props.load(stringReader);
      } catch (IOException e) {
        log.warn("Could not parse loggers from environment variables", e);
      }
      // Add all sys
      props.putAll(fromSysLoggers);
      for (var loggerName : props.stringPropertyNames()) {
        var logLevelStr = props.getProperty(loggerName);
        var logLevel = Level.toLevel(logLevelStr, null);
        if (logLevel != null) {
          log.debug("Setting logger {} to level {}", loggerName, logLevel);
          context.getLogger(loggerName).setLevel(logLevel);
        } else {
          log.warn("Invalid log level found. Logger {}, level {}", loggerName, logLevelStr);
        }
      }
    } else {
      log.warn("No Logback LoggerContext found");
    }
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
