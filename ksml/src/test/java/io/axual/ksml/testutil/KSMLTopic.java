package io.axual.ksml.testutil;

/*-
 * ========================LICENSE_START=================================
 * KSML
 * %%
 * Copyright (C) 2021 - 2024 Axual B.V.
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which marks fields of type {@link org.apache.kafka.streams.TestInputTopic} and {@link org.apache.kafka.streams.TestOutputTopic}
 * which should be bound to a topic in the KSML topology under test.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KSMLTopic {

    /** topic name as found in the pipeline definition. */
    String topic();

    /** optional key serde, defaults to STRING. */
    SerdeType keySerde() default SerdeType.STRING;

    /** optional key serde, defaults to STRING. */
    SerdeType valueSerde() default SerdeType.STRING;

    /** Serde types the annotation supports. */
    enum SerdeType {STRING, AVRO, LONG, INTEGER}
}
