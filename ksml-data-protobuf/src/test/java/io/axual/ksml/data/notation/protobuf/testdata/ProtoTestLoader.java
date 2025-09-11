package io.axual.ksml.data.notation.protobuf.testdata;

/*-
 * ========================LICENSE_START=================================
 * KSML Data Library - PROTOBUF
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

import com.squareup.wire.schema.Location;
import com.squareup.wire.schema.internal.parser.ProtoFileElement;
import com.squareup.wire.schema.internal.parser.ProtoParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Test helper to load .proto files from test resources and parse them into Wire's ProtoFileElement.
 * This avoids adding a protoc plugin and stays compatible with protobuf-java 3.25.
 */
public final class ProtoTestLoader {
    private ProtoTestLoader() {
    }

    public static ProtoFileElement loadFileElement(String resourcePath) {
        try (InputStream in = ProtoTestLoader.class.getResourceAsStream(resourcePath)) {
            if (in == null)
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            final String content;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                content = br.lines().collect(Collectors.joining("\n"));
            }
            return ProtoParser.Companion.parse(Location.get(resourcePath), content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + resourcePath, e);
        }
    }
}
