package io.axual.ksml.data.notation;

/*-
 * ========================LICENSE_START=================================
 * KSML
 * %%
 * Copyright (C) 2021 - 2023 Axual B.V.
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

import io.axual.ksml.data.object.DataObject;
import io.axual.ksml.data.schema.DataSchema;
import io.axual.ksml.data.type.DataType;
import org.apache.kafka.common.serialization.Serde;

public interface Notation {
    DataType defaultType();

    String name();

    String filenameExtension();

    Serde<Object> serde(DataType type, boolean isKey);

    interface Converter {
        DataObject convert(DataObject value, DataType targetType);
    }

    Converter converter();

    interface SchemaParser {
        DataSchema parse(String contextName, String schemaName, String schemaString);
    }

    SchemaParser schemaParser();
}
