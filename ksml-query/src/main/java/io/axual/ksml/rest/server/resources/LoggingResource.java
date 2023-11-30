package io.axual.ksml.rest.server.resources;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;

/**
 * Implements endpoints for managing log levels at runtime. The settings are not persisted
 */
@Slf4j
@Path("admin/loggers")
public class LoggingResource {
    final LoggerContext loggerContext;

    public LoggingResource() {
        var iLoggerFactory = LoggerFactory.getILoggerFactory();
        if (iLoggerFactory instanceof LoggerContext context) {
            this.loggerContext = context;
        } else {
            log.warn("No Logback LoggerContext found, disabled logging rest service");
            this.loggerContext = null;
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<LoggerInfo> getLoggerInfoList(
            @QueryParam("name") List<String> names) {
        guard();
        final List<String> cleanedNames;
        if (names == null) {
            cleanedNames = new ArrayList<>(0);
        } else {
            // remove null items and set to lowercase
            cleanedNames = names.stream().filter(Objects::nonNull).map(String::toLowerCase).toList();
        }


        var nameMatcher = (Predicate<Logger>) logger -> cleanedNames.isEmpty() || cleanedNames.stream().anyMatch(name -> logger.getName().toLowerCase().contains(name));

        var list = loggerContext.getLoggerList().stream()
                .filter(nameMatcher)
                .map(this::transform)
                .toList();
        return list;
    }

    @GET
    @Path("/{logger}")
    @Produces(MediaType.APPLICATION_JSON)
    public LoggerInfo getLoggerInfo(
            @PathParam("logger") String loggerName) {
        guard();
        if (StringUtils.isEmpty(loggerName)) {
            throw new BadRequestException();
        }
        return Optional.ofNullable(loggerContext.exists(loggerName.trim()))
                .map(this::transform)
                .orElseThrow(NotFoundException::new);
    }

    @PUT
    @Path("/{logger}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UpdatedLoggers setLoggerLevel(
            @PathParam("logger") String loggerName, Map<String, String> levelMap) {
        guard();
        if (StringUtils.isEmpty(loggerName)) {
            throw new BadRequestException("Missing or invalid logger");
        }
        final var newLevelStr = levelMap.get("level");
        if (newLevelStr == null) {
            throw new BadRequestException("No 'level' parameter was specified in request.");
        }
        final var updateChildrenStr = levelMap.get("includeChildren");
        final var updateChildren = Boolean.parseBoolean(updateChildrenStr);

        final var newLevel = Optional.ofNullable(Level.toLevel(newLevelStr, null))
                .orElseThrow(() -> new BadRequestException("Unknown 'level' type was specified in request."));

        final var cleanLoggerName = loggerName.trim();
        final var ancestorLogger = loggerContext.getLogger(cleanLoggerName);
        ancestorLogger.setLevel(newLevel);

        final var updatedLoggerBuilder = UpdatedLoggers.builder()
                .logLevel(String.valueOf(newLevel))
                .logger(ancestorLogger.getName());

        if (updateChildren) {
            loggerContext.getLoggerList().stream()
                    .filter(logger -> logger.getName().startsWith(cleanLoggerName))
                    .filter(logger -> !logger.getName().contentEquals(cleanLoggerName))
                    .forEach(logger -> {
                        logger.setLevel(newLevel);
                        updatedLoggerBuilder.logger(logger.getName());
                    });
        }

        return updatedLoggerBuilder.build();
    }

    private LoggerInfo transform(Logger logger) {
        return logger == null ? null : LoggerInfo.builder()
                .name(logger.getName())
                .logLevel(String.valueOf(logger.getEffectiveLevel()))
                .build();
    }


    private void guard() {
        if (loggerContext == null) {
            throw new ClientErrorException("Not available for this deployment", Response.Status.NOT_IMPLEMENTED);
        }
    }
    @Builder
    @Jacksonized
    @Getter
    public static class LoggerInfo {
        final String name;
        final String logLevel;
    }

    @Getter
    public static class UpdatedLoggers {
        final String logLevel;
        final List<String> loggers;

        @Builder
        @Jacksonized
        public UpdatedLoggers(@NonNull String logLevel, @Singular List<String> loggers) {
            this.logLevel = logLevel;
            var sortedLoggers = new ArrayList<>(loggers);
            sortedLoggers.sort(Comparator.naturalOrder());
            this.loggers = sortedLoggers;
        }
    }
}
