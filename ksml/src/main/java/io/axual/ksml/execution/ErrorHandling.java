package io.axual.ksml.execution;

/*-
 * ========================LICENSE_START=================================
 * KSML
 * %%
 * Copyright (C) 2021 - 2025 Axual B.V.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TopicAuthorizationException;
import org.apache.kafka.streams.errors.DeserializationExceptionHandler;
import org.apache.kafka.streams.errors.ProductionExceptionHandler;
import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.stream.Collectors;

@Slf4j
public class ErrorHandling {
    private static final String DATA_MASK = "*****";
    private static final String DATA_NULL = "<NULL>";

    private ErrorHandler consumeHandler;
    private ErrorHandler processHandler;
    private ErrorHandler produceHandler;

    private Logger consumeExceptionLogger;
    private Logger produceExceptionLogger;
    private Logger processExceptionLogger;

    public void setConsumeHandler(ErrorHandler consumeHandler) {
        this.consumeHandler = consumeHandler;
        this.consumeExceptionLogger = LoggerFactory.getLogger(consumeHandler.loggerName());
    }

    public void setProcessHandler(ErrorHandler processHandler) {
        this.processHandler = processHandler;
        this.processExceptionLogger = LoggerFactory.getLogger(processHandler.loggerName());
    }

    public void setProduceHandler(ErrorHandler produceHandler) {
        this.produceHandler = produceHandler;
        this.produceExceptionLogger = LoggerFactory.getLogger(produceHandler.loggerName());
    }

    public StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse uncaughtException(Throwable throwable) {
        if (throwable instanceof StreamsException streamsException) {
            if (streamsException.getCause() instanceof TopicAuthorizationException topicAuthorizationException && !topicAuthorizationException.unauthorizedTopics().isEmpty()) {
                log.error("Topic authorization exception was thrown. Please create / grant access to the following topics: \n"
                        + topicAuthorizationException.unauthorizedTopics().stream().map(t -> "  * " + t + "\n").collect(Collectors.joining()));
            }
        } else {
            processExceptionLogger.error("Caught unhandled exception, stopping this KSML instance", throwable);
        }
        // Stop only the current instance of KSML
        return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_CLIENT;
    }

    public String bytesToString(byte[] data) {
        return data == null ? DATA_NULL : "(base64)" + Base64.getEncoder().encodeToString(data);
    }

    public DeserializationExceptionHandler.DeserializationHandlerResponse handle(ConsumerRecord<byte[], byte[]> record, Exception exception) {
        if (consumeHandler.log()) {
            // log record
            String key = consumeHandler.logPayload() ? bytesToString(record.key()) : DATA_MASK;
            String value = consumeHandler.logPayload() ? bytesToString(record.value()) : DATA_MASK;
            consumeExceptionLogger.error("Exception occurred while consuming a record from topic: {}, partition: {}, offset: {}, key: {}, value: {}", record.topic(), record.partition(), record.offset(), key, value, exception);
        }
        return consumeHandler.handlerType() == ErrorHandler.HandlerType.CONTINUE_ON_FAIL ? DeserializationExceptionHandler.DeserializationHandlerResponse.CONTINUE : DeserializationExceptionHandler.DeserializationHandlerResponse.FAIL;
    }

    public String maskData(Object data) {
        if (!processHandler.logPayload()) return DATA_MASK;
        return data.toString();
    }

    public ProductionExceptionHandler.ProductionExceptionHandlerResponse handle(ProducerRecord<byte[], byte[]> record, Exception exception) {
        if (produceHandler.log()) {
            // log record
            String key = produceHandler.logPayload() ? bytesToString(record.key()) : DATA_MASK;
            String value = produceHandler.logPayload() ? bytesToString(record.value()) : DATA_MASK;
            produceExceptionLogger.error("Exception occurred while producing a record with key: {}, value: {}", key, value, exception);
        }
        return produceHandler.handlerType() == ErrorHandler.HandlerType.CONTINUE_ON_FAIL ? ProductionExceptionHandler.ProductionExceptionHandlerResponse.CONTINUE : ProductionExceptionHandler.ProductionExceptionHandlerResponse.FAIL;
    }
}
