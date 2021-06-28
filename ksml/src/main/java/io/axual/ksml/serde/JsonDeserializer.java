package io.axual.ksml.serde;

/*-
 * ========================LICENSE_START=================================
 * KSML
 * %%
 * Copyright (C) 2021 Axual B.V.
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.HashMap;

public class JsonDeserializer implements Deserializer<Object> {
    private final ObjectMapper mapper = new ObjectMapper();
    private final TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
    };

    @Override
    public Object deserialize(String s, byte[] data) {
        if (data == null) return null;

        try {
            return mapper.readValue(data, typeRef);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }
}