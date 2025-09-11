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

import com.squareup.wire.schema.internal.parser.ProtoFileElement;
import com.squareup.wire.schema.internal.parser.TypeElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.axual.ksml.data.exception.SchemaException;
import io.axual.ksml.data.notation.protobuf.ProtobufConstants;
import io.axual.ksml.data.schema.DataField;
import io.axual.ksml.data.schema.DataSchema;
import io.axual.ksml.data.schema.EnumSchema;
import io.axual.ksml.data.schema.ListSchema;
import io.axual.ksml.data.schema.ReservedTagRange;
import io.axual.ksml.data.schema.StructSchema;
import io.axual.ksml.data.type.Symbol;

public class Proto3Schemas implements TestSchemaData {
    public static final ProtoFileElement PROTO3_SCHEMAS_FILE_ELEMENT = ProtoTestLoader.loadFileElement("/proto/basics_toDataSchema.proto");
    public static final ProtoFileElement PROTO3_EXPECTED_SCHEMAS_FILE_ELEMENT = ProtoTestLoader.loadFileElement("/proto/basics_fromDataSchema.proto");
    protected static final String NAMESPACE = "ksml.test.common";
    public static final String PROTO_DOC_DEFAULT = "";
    protected static final StructSchema SIMPLE_MESSAGE_DATASCHEMA = StructSchema.builder()
            .name("SimpleMessage")
            .namespace(NAMESPACE)
            .doc(PROTO_DOC_DEFAULT)
            .allowAdditionalFields(false)
            .field(new DataField("id", DataSchema.STRING_SCHEMA, PROTO_DOC_DEFAULT, 1, false))
            .build();
    protected static final EnumSchema COLORS_ENUM_DATASCHEMA = new EnumSchema(
            NAMESPACE, "Colors", PROTO_DOC_DEFAULT,
            List.of(
                    Symbol.of("COLORS_TYPE_NAME_UNKNOWN", PROTO_DOC_DEFAULT, 0),
                    Symbol.of("RED", PROTO_DOC_DEFAULT, 1),
                    Symbol.of("GREEN", PROTO_DOC_DEFAULT, 2),
                    Symbol.of("BLUE", PROTO_DOC_DEFAULT, 3)
            ),
            Symbol.of("COLORS_TYPE_NAME_UNKNOWN", PROTO_DOC_DEFAULT, 0));
    public static final StructSchema OPTIONAL_SCALARS_DATASCHEMA = StructSchema.builder()
            .name("OptionalScalars")
            .namespace(NAMESPACE)
            .doc(PROTO_DOC_DEFAULT)
            .allowAdditionalFields(false)
            .field(new DataField("optDouble", DataSchema.DOUBLE_SCHEMA, PROTO_DOC_DEFAULT, 1, false))
            .field(new DataField("optFloat", DataSchema.FLOAT_SCHEMA, PROTO_DOC_DEFAULT, 2, false))
            .field(new DataField("optInt32", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 3, false))
            .field(new DataField("optInt64", DataSchema.LONG_SCHEMA, PROTO_DOC_DEFAULT, 4, false))
            .field(new DataField("optUInt32", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 5, false))
            .field(new DataField("optUInt64", DataSchema.LONG_SCHEMA, PROTO_DOC_DEFAULT, 6, false))
            .field(new DataField("optSInt32", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 7, false))
            .field(new DataField("optSInt64", DataSchema.LONG_SCHEMA, PROTO_DOC_DEFAULT, 8, false))
            .field(new DataField("optFixed32", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 9, false))
            .field(new DataField("optFixed64", DataSchema.LONG_SCHEMA, PROTO_DOC_DEFAULT, 10, false))
            .field(new DataField("optSFixed32", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 11, false))
            .field(new DataField("optSFixed64", DataSchema.LONG_SCHEMA, PROTO_DOC_DEFAULT, 12, false))
            .field(new DataField("optBool", DataSchema.BOOLEAN_SCHEMA, PROTO_DOC_DEFAULT, 13, false))
            .field(new DataField("optString", DataSchema.STRING_SCHEMA, PROTO_DOC_DEFAULT, 14, false))
            .field(new DataField("optBytes", DataSchema.BYTES_SCHEMA, PROTO_DOC_DEFAULT, 15, false))
            .field(new DataField("optColor", COLORS_ENUM_DATASCHEMA, PROTO_DOC_DEFAULT, 16, false))
            .field(new DataField("optSimple", SIMPLE_MESSAGE_DATASCHEMA, PROTO_DOC_DEFAULT, 17, false))
            .build();
    public static final StructSchema IMPLICIT_SCALARS_DATASCHEMA = StructSchema.builder()
            .name("ImplicitScalars")
            .namespace(NAMESPACE)
            .doc(PROTO_DOC_DEFAULT)
            .allowAdditionalFields(false)
            .field(new DataField("impDouble", DataSchema.DOUBLE_SCHEMA, PROTO_DOC_DEFAULT, 1, false))
            .field(new DataField("impFloat", DataSchema.FLOAT_SCHEMA, PROTO_DOC_DEFAULT, 2, false))
            .field(new DataField("impInt32", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 3, false))
            .field(new DataField("impInt64", DataSchema.LONG_SCHEMA, PROTO_DOC_DEFAULT, 4, false))
            .field(new DataField("impUInt32", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 5, false))
            .field(new DataField("impUInt64", DataSchema.LONG_SCHEMA, PROTO_DOC_DEFAULT, 6, false))
            .field(new DataField("impSInt32", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 7, false))
            .field(new DataField("impSInt64", DataSchema.LONG_SCHEMA, PROTO_DOC_DEFAULT, 8, false))
            .field(new DataField("impFixed32", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 9, false))
            .field(new DataField("impFixed64", DataSchema.LONG_SCHEMA, PROTO_DOC_DEFAULT, 10, false))
            .field(new DataField("impSFixed32", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 11, false))
            .field(new DataField("impSFixed64", DataSchema.LONG_SCHEMA, PROTO_DOC_DEFAULT, 12, false))
            .field(new DataField("impBool", DataSchema.BOOLEAN_SCHEMA, PROTO_DOC_DEFAULT, 13, false))
            .field(new DataField("impString", DataSchema.STRING_SCHEMA, PROTO_DOC_DEFAULT, 14, false))
            .field(new DataField("impBytes", DataSchema.BYTES_SCHEMA, PROTO_DOC_DEFAULT, 15, false))
            .field(new DataField("impColor", COLORS_ENUM_DATASCHEMA, PROTO_DOC_DEFAULT, 16, false))
            .field(new DataField("impSimple", SIMPLE_MESSAGE_DATASCHEMA, PROTO_DOC_DEFAULT, 17, false))
            .build();
    public static final StructSchema REPEATED_SCALARS_DATASCHEMA = StructSchema.builder()
            .name("RepeatedScalars")
            .namespace(NAMESPACE)
            .doc(PROTO_DOC_DEFAULT)
            .allowAdditionalFields(false)
            .field(new DataField("repDouble", new ListSchema(DataSchema.DOUBLE_SCHEMA), PROTO_DOC_DEFAULT, 1, false))
            .field(new DataField("repFloat", new ListSchema(DataSchema.FLOAT_SCHEMA), PROTO_DOC_DEFAULT, 2, false))
            .field(new DataField("repInt32", new ListSchema(DataSchema.INTEGER_SCHEMA), PROTO_DOC_DEFAULT, 3, false))
            .field(new DataField("repInt64", new ListSchema(DataSchema.LONG_SCHEMA), PROTO_DOC_DEFAULT, 4, false))
            .field(new DataField("repUInt32", new ListSchema(DataSchema.INTEGER_SCHEMA), PROTO_DOC_DEFAULT, 5, false))
            .field(new DataField("repUInt64", new ListSchema(DataSchema.LONG_SCHEMA), PROTO_DOC_DEFAULT, 6, false))
            .field(new DataField("repSInt32", new ListSchema(DataSchema.INTEGER_SCHEMA), PROTO_DOC_DEFAULT, 7, false))
            .field(new DataField("repSInt64", new ListSchema(DataSchema.LONG_SCHEMA), PROTO_DOC_DEFAULT, 8, false))
            .field(new DataField("repFixed32", new ListSchema(DataSchema.INTEGER_SCHEMA), PROTO_DOC_DEFAULT, 9, false))
            .field(new DataField("repFixed64", new ListSchema(DataSchema.LONG_SCHEMA), PROTO_DOC_DEFAULT, 10, false))
            .field(new DataField("repSFixed32", new ListSchema(DataSchema.INTEGER_SCHEMA), PROTO_DOC_DEFAULT, 11, false))
            .field(new DataField("repSFixed64", new ListSchema(DataSchema.LONG_SCHEMA), PROTO_DOC_DEFAULT, 12, false))
            .field(new DataField("repBool", new ListSchema(DataSchema.BOOLEAN_SCHEMA), PROTO_DOC_DEFAULT, 13, false))
            .field(new DataField("repString", new ListSchema(DataSchema.STRING_SCHEMA), PROTO_DOC_DEFAULT, 14, false))
            .field(new DataField("repBytes", new ListSchema(DataSchema.BYTES_SCHEMA), PROTO_DOC_DEFAULT, 15, false))
            .field(new DataField("repColors", new ListSchema(COLORS_ENUM_DATASCHEMA), PROTO_DOC_DEFAULT, 16, false))
            .field(new DataField("repSimple", new ListSchema(SIMPLE_MESSAGE_DATASCHEMA), PROTO_DOC_DEFAULT, 17, false))
            .build();
    protected static final EnumSchema NESTED_COLORS_ENUM_DATASCHEMA = new EnumSchema(
            NAMESPACE, "NestedDefinitions.NestedColors", PROTO_DOC_DEFAULT,
            List.of(
                    Symbol.of("NESTED_COLORS_TYPE_NAME_UNKNOWN", PROTO_DOC_DEFAULT, 0),
                    Symbol.of("CYAN", PROTO_DOC_DEFAULT, 1),
                    Symbol.of("MAGENTA", PROTO_DOC_DEFAULT, 2),
                    Symbol.of("YELLOW", PROTO_DOC_DEFAULT, 3)
            ),
            Symbol.of("NESTED_COLORS_TYPE_NAME_UNKNOWN", PROTO_DOC_DEFAULT, 0));

    public static final TestSchemaData SIMPLE_SCHEMA = new SimpleMessage();
    public static final TestSchemaData REPEAT_SCALARS_SCHEMA = new RepeatedScalars();
    public static final TestSchemaData OPTIONAL_SCALARS_SCHEMA = new OptionalScalars();
    public static final TestSchemaData IMPLICIT_SCALARS_SCHEMA = new ImplicitScalars();
    public static final TestSchemaData RESERVED_MESSAGE_SCHEMA = new ReservedMessage();

    private final String name;
    private final DataSchema dataSchema;
    private final ProtoFileElement protoFileElement;
    private final ProtoFileElement expectedProtoFileElement;

    protected Proto3Schemas(String name, DataSchema dataSchema, String... additionalObjectNames) {
        this.name = name;
        this.dataSchema = dataSchema;

        final var namesToFind = new ArrayList<String>();
        namesToFind.add(name);
        if (additionalObjectNames != null && additionalObjectNames.length > 0) {
            namesToFind.addAll(Arrays.asList(additionalObjectNames));
        }
        final var foundTypes = findElementByName(namesToFind, PROTO3_SCHEMAS_FILE_ELEMENT.getTypes());
        if (foundTypes.size() != namesToFind.size()) {
            throw new SchemaException("Not all names are found.%n  Wanted : %s%n  Found  : %s".formatted(String.join(",", namesToFind), String.join(",", foundTypes.stream().map(TypeElement::getName).toList())));
        }

        this.protoFileElement = new ProtoFileElement(
                PROTO3_SCHEMAS_FILE_ELEMENT.getLocation(),
                PROTO3_SCHEMAS_FILE_ELEMENT.getPackageName(),
                ProtobufConstants.DEFAULT_SYNTAX,
                Collections.emptyList(),
                Collections.emptyList(),
                foundTypes,
                Collections.emptyList(),
                Collections.emptyList(),
                new ArrayList<>());
        final var foundExpectedTypes = findElementByName(namesToFind, PROTO3_EXPECTED_SCHEMAS_FILE_ELEMENT.getTypes());

        this.expectedProtoFileElement = new ProtoFileElement(
                PROTO3_SCHEMAS_FILE_ELEMENT.getLocation(),
                PROTO3_SCHEMAS_FILE_ELEMENT.getPackageName(),
                ProtobufConstants.DEFAULT_SYNTAX,
                Collections.emptyList(),
                Collections.emptyList(),
                foundExpectedTypes,
                Collections.emptyList(),
                Collections.emptyList(),
                new ArrayList<>());
    }

    static List<TypeElement> findElementByName(Collection<String> names, Collection<TypeElement> elements) {
        final var foundElements = new ArrayList<TypeElement>();
        names.forEach(name -> {
            var element = findElement(elements, name);
            if (element != null) {
                foundElements.add(element);
            }
        });
        return foundElements;
    }

    private static TypeElement findElement(Collection<TypeElement> elements, String name) {
        // Find the message by name in the schema's message types
        final var nameSplit = name.split("\\.");
        TypeElement foundElement = null;
        for (final String typeName : nameSplit) {
            var currentTypeElements = foundElement == null ? elements : foundElement.getNestedTypes();
            for (final var currentTypeElement : currentTypeElements) {
                if (currentTypeElement.getName().equals(typeName)) {
                    foundElement = currentTypeElement;
                    break;
                }
            }
            if (foundElement == null) {
                throw new SchemaException("Unable to find message element part %s for name %s ".formatted(typeName, name));
            }
        }
        return foundElement;
    }

    @Override
    public String getNameSpace() {
        return NAMESPACE;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ProtoFileElement getProtoFileElement() {
        return protoFileElement;
    }

    @Override
    public ProtoFileElement getExpectedProtoFileElement() {
        return expectedProtoFileElement;
    }

    @Override
    public DataSchema getDataSchema() {
        return dataSchema;
    }

    private static class SimpleMessage extends Proto3Schemas {
        private SimpleMessage() {
            super("SimpleMessage", SIMPLE_MESSAGE_DATASCHEMA);
        }
    }

    private static class OptionalScalars extends Proto3Schemas {
        private OptionalScalars() {
            super("OptionalScalars", OPTIONAL_SCALARS_DATASCHEMA, "SimpleMessage", "Colors");
        }
    }

    private static class ImplicitScalars extends Proto3Schemas {
        private ImplicitScalars() {
            super("ImplicitScalars", IMPLICIT_SCALARS_DATASCHEMA, "SimpleMessage", "Colors");
        }
    }

    private static class RepeatedScalars extends Proto3Schemas {
        private RepeatedScalars() {
            super("RepeatedScalars", REPEATED_SCALARS_DATASCHEMA, "SimpleMessage", "Colors");
        }
    }

    private static class ReservedMessage extends Proto3Schemas {
        private ReservedMessage() {
            super("ReservedMessage", StructSchema.builder()
                    .name("ReservedMessage")
                    .namespace(NAMESPACE)
                    .doc(PROTO_DOC_DEFAULT)
                    .allowAdditionalFields(false)
                    .field(new DataField("id", DataSchema.STRING_SCHEMA, PROTO_DOC_DEFAULT, 1, false))
                    .field(new DataField("count", DataSchema.INTEGER_SCHEMA, PROTO_DOC_DEFAULT, 4, false))
                    .reservedFieldName("reserved1")
                    .reservedFieldName("reserved2")
                    .reservedFieldName("reserved3")
                    .reservedFieldName("reserved4")
                    .reservedTag(ReservedTagRange.of(2))
                    .reservedTag(ReservedTagRange.of(3))
                    .reservedTag(ReservedTagRange.of(5, 10))
                    .reservedTag(ReservedTagRange.of(11))
                    .reservedTag(ReservedTagRange.of(12))
                    .build());
        }
    }

    private static class NestedMessage extends Proto3Schemas {
        private NestedMessage() {
            super("NestedDefinitions.NestedMessage", StructSchema.builder()
                    .name("NestedDefinitions.NestedMessage")
                    .namespace(NAMESPACE)
                    .doc(PROTO_DOC_DEFAULT)
                    .allowAdditionalFields(false)
                    .field(new DataField("id", DataSchema.STRING_SCHEMA, PROTO_DOC_DEFAULT, 1, false))
                    .field(new DataField("colors", new ListSchema(NESTED_COLORS_ENUM_DATASCHEMA), PROTO_DOC_DEFAULT, 2, false))
                    .field(new DataField("simple", SIMPLE_MESSAGE_DATASCHEMA, PROTO_DOC_DEFAULT, 3, false))
                    .field(new DataField("union", SIMPLE_MESSAGE_DATASCHEMA, PROTO_DOC_DEFAULT, 11, false))
                    .reservedFieldName("reserved1")
                    .reservedFieldName("reserved2")
                    .reservedTag(ReservedTagRange.of(4, 10))
                    .build(), "SimpleMessage", "NestedDefinitions.NestedColors", "NestedDefinitions.NestedWrapOneOf");
        }
    }
}
