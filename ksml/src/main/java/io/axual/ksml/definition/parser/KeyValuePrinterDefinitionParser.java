package io.axual.ksml.definition.parser;

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


import io.axual.ksml.definition.KeyValuePrinterDefinition;
import io.axual.ksml.dsl.KSMLDSL;
import io.axual.ksml.parser.StructsParser;

public class KeyValuePrinterDefinitionParser extends FunctionDefinitionParser<KeyValuePrinterDefinition> {
    public KeyValuePrinterDefinitionParser(boolean requireType) {
        super(requireType);
    }

    @Override
    public StructsParser<KeyValuePrinterDefinition> parser() {
        return parserWithoutStores(
                KeyValuePrinterDefinition.class,
                KSMLDSL.Functions.TYPE_KEYVALUEPRINTER,
                "keyvalue printer",
                (function, tags) -> new KeyValuePrinterDefinition(function));
    }
}
