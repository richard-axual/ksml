package io.axual.ksml.operation;

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

import io.axual.ksml.testutil.KSMLTest;
import io.axual.ksml.testutil.KSMLTestExtension;
import io.axual.ksml.testutil.KSMLTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@ExtendWith({KSMLTestExtension.class})
class KSMLTransformToNoneTest {

    @KSMLTopic(topic = "ksml_sensordata_avro")
    protected TestInputTopic<String, String> inputTopic;

    @KSMLTopic(topic = "ksml_sensordata_copy")
    protected TestOutputTopic<String, String> outputTopic;

    @KSMLTest(topology = "pipelines/test-transform-to-none.yaml")
    void testSetNoneValueData() {
        log.debug("testSetNoneValueData()");

        inputTopic.pipeInput("key1", (String) null);
        assertFalse(outputTopic.isEmpty(), "record should be copied");
        var keyValue = outputTopic.readKeyValue();
        assertNull(keyValue.value);
        System.out.printf("Output topic key=%s, value=%s%n", keyValue.key, keyValue.value);
    }
}
