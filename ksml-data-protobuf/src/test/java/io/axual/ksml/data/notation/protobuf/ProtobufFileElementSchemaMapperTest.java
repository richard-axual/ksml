package io.axual.ksml.data.notation.protobuf;

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

import com.squareup.wire.schema.internal.parser.OptionElement;
import com.squareup.wire.schema.internal.parser.ProtoFileElement;
import com.squareup.wire.schema.internal.parser.TypeElement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import io.axual.ksml.data.notation.protobuf.testdata.Proto3Schemas;
import io.axual.ksml.data.notation.protobuf.testdata.TestSchemaData;

import static io.axual.ksml.data.notation.protobuf.testdata.ProtoComparators.FILE_ELEMENT_COMPARATOR;
import static io.axual.ksml.data.notation.protobuf.testdata.ProtoComparators.OPTION_ELEMENT_COMPARATOR;
import static io.axual.ksml.data.notation.protobuf.testdata.ProtoComparators.TYPE_ELEMENT_COMPARATOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

class ProtobufFileElementSchemaMapperTest {
    private final ProtobufFileElementSchemaMapper mapper = new ProtobufFileElementSchemaMapper();

    @ParameterizedTest
    @MethodSource(value = "proto3Schemas")
    @DisplayName("Converts proto file element to dataschema")
    void testProtobufToDataSchema(TestSchemaData schemaData) {
        //
        final var protobuf = schemaData.getProtoFileElement();
        final var schemaNamespace = schemaData.getNameSpace();
        final var schemaName = schemaData.getName();
        final var expectedDataSchema = schemaData.getDataSchema();
        final var mappedStruct = mapper.toDataSchema(schemaNamespace, schemaName, protobuf);
        assertThat(mappedStruct)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("fields")
                .isEqualTo(expectedDataSchema);
    }

    @ParameterizedTest
    @MethodSource(value = "proto3Schemas")
    @DisplayName("Converts dataschema to proto file element")
    void testDataSchemaToProtobuf(TestSchemaData schemaData) {
        //
        final var expectedProtobuf = schemaData.getExpectedProtoFileElement();
        final var dataSchema = schemaData.getDataSchema();
        final var mappedElement = mapper.fromDataSchema(dataSchema);
        assertThat(mappedElement)
                .isNotNull()
                .usingRecursiveComparison()
                .withComparatorForType(FILE_ELEMENT_COMPARATOR, ProtoFileElement.class)
                .withComparatorForType(TYPE_ELEMENT_COMPARATOR, TypeElement.class)
                .withComparatorForType(OPTION_ELEMENT_COMPARATOR, OptionElement.class)
                .isEqualTo(expectedProtobuf);
    }

    public static Stream<Arguments> proto3Schemas() {
        return Stream.of(
                argumentSet("Simple Object", Proto3Schemas.SIMPLE_SCHEMA),
                argumentSet("Object with Optional Scalars", Proto3Schemas.OPTIONAL_SCALARS_SCHEMA),
                argumentSet("Object with Implicit Scalars", Proto3Schemas.IMPLICIT_SCALARS_SCHEMA),
                argumentSet("Object with Repeated Scalars", Proto3Schemas.REPEAT_SCALARS_SCHEMA),
                argumentSet("Reserved fields", Proto3Schemas.RESERVED_MESSAGE_SCHEMA)
//                argumentSet("OneOf", Proto3Schemas.WRAP_ONEOF_MESSAGE_SCHEMA)
//                argumentSet("Nested Object", Proto3Schemas.NESTED_MESSAGE_SCHEMA) // Nested object naming is issue
        );
    }

}
