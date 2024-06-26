package io.axual.ksml.operation.parser;

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


import io.axual.ksml.definition.parser.KeyValueToValueListTransformerDefinitionParser;
import io.axual.ksml.dsl.KSMLDSL;
import io.axual.ksml.generator.TopologyResources;
import io.axual.ksml.operation.TransformKeyValueToValueListOperation;
import io.axual.ksml.parser.StructsParser;

public class TransformKeyValueToValueListOperationParser extends OperationParser<TransformKeyValueToValueListOperation> {
    public TransformKeyValueToValueListOperationParser(TopologyResources resources) {
        super(KSMLDSL.Operations.TRANSFORM_KEY_VALUE_TO_VALUE_LIST, resources);
    }

    @Override
    protected StructsParser<TransformKeyValueToValueListOperation> parser() {
        return structsParser(
                TransformKeyValueToValueListOperation.class,
                "",
                "Convert every record in the stream to a list of output records with the same key",
                operationNameField(),
                functionField(KSMLDSL.Operations.Transform.MAPPER, "A function that converts every key/value into a list of result values, each of which will be combined with the original key to form a new message in the output stream", new KeyValueToValueListTransformerDefinitionParser(false)),
                (name, mapper, tags) -> new TransformKeyValueToValueListOperation(operationConfig(name, tags), mapper));
    }
}
