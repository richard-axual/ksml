package io.axual.ksml.operation;

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

import io.axual.ksml.testutil.KSMLTest;
import io.axual.ksml.testutil.KSMLTestExtension;
import io.axual.ksml.testutil.KSMLTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.axual.ksml.operation.SensorData.SensorType.HUMIDITY;
import static io.axual.ksml.operation.SensorData.SensorType.TEMPERATURE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(KSMLTestExtension.class)
@Slf4j
public class KSMLTransformKeyValueTest {

    @KSMLTopic(topic = "input_topic", valueSerde = KSMLTopic.SerdeType.AVRO)
    TestInputTopic<String, GenericRecord> inputTopic;

    @KSMLTopic(topic = "output_topic")
    TestOutputTopic<String, String> outputTopic;

    List<GenericRecord> inputs = List.of(
            SensorData.builder().city("AMS").type(HUMIDITY).unit("%").value("80").build().toRecord(),
            SensorData.builder().city("UTR").type(HUMIDITY).unit("%").value("75").build().toRecord(),
            SensorData.builder().city("AMS").type(TEMPERATURE).unit("C").value("25").build().toRecord(),
            SensorData.builder().city("UTR").type(TEMPERATURE).unit("C").value("27").build().toRecord()
    );

    @KSMLTest(topology = "pipelines/test-transformkeyvalue-expression.yaml", schemaDirectory = "schemas")
    @DisplayName("Keys and values can be mapped with an expression")
    void testMapByExpression() {
        provideInputsAndCheckOutputs();
    }

    @KSMLTest(topology = "pipelines/test-transformkeyvalue-code.yaml", schemaDirectory = "schemas")
    @DisplayName("Keys and values can be mapped with a code block")
    void testMapByCode() {
        provideInputsAndCheckOutputs();
    }

    private void provideInputsAndCheckOutputs() {
        inputs.forEach(rec -> inputTopic.pipeInput(rec));

        List<KeyValue<String, String>> keyValues = outputTopic.readKeyValuesToList();
        assertEquals(4, keyValues.size(), "All records should be transformed");

        // verify first and last record key and value; the pipeline creates them from fields in the record value
        assertEquals("AMS", keyValues.get(0).key);
        assertEquals("HUMIDITY 80", keyValues.get(0).value);
        assertEquals("UTR", keyValues.get(3).key);
        assertEquals("TEMPERATURE 27", keyValues.get(3).value);
    }
}


