package io.axual.ksml.data.notation.xml;

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

import io.axual.ksml.data.notation.Notation;
import io.axual.ksml.data.object.DataObject;
import io.axual.ksml.data.object.DataString;
import io.axual.ksml.data.object.DataStruct;
import io.axual.ksml.data.type.DataType;
import io.axual.ksml.data.type.StructType;

public class XmlDataObjectConverter implements Notation.Converter {
    private static final XmlDataObjectMapper DATA_OBJECT_MAPPER = new XmlDataObjectMapper(true);

    @Override
    public DataObject convert(DataObject value, DataType targetType) {
        // Convert from Struct to String
        if (value instanceof DataStruct && targetType == DataString.DATATYPE) {
            return new DataString(DATA_OBJECT_MAPPER.fromDataObject(value));
        }

        // Convert from String to Struct
        if (value instanceof DataString str && targetType instanceof StructType) {
            return DATA_OBJECT_MAPPER.toDataObject(targetType, str.value());
        }

        // Return null if there is no conversion possible
        return null;
    }
}
