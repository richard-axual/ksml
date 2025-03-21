package io.axual.ksml.schema.parser;

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

import io.axual.ksml.data.schema.DataField;
import io.axual.ksml.parser.BaseParser;
import io.axual.ksml.parser.ListParser;
import io.axual.ksml.parser.ParseNode;

import java.util.List;

public class DataFieldsParser extends BaseParser<List<DataField>> {
    @Override
    public List<DataField> parse(ParseNode node) {
        return new ListParser<>("field", "field", new DataFieldParser()).parse(node.get(DataSchemaDSL.STRUCT_SCHEMA_FIELDS_FIELD, "field"));
    }
}
