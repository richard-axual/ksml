package io.axual.ksml.parser;

/*-
 * ========================LICENSE_START=================================
 * KSML Data Library
 * %%
 * Copyright (C) 2021 - 2024 Axual B.V.
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

import com.fasterxml.jackson.databind.JsonNode;
import io.axual.ksml.exception.ExecutionException;
import io.axual.ksml.metric.MetricTags;
import lombok.Getter;

import java.util.*;

// This class helps to track the location of syntax/parsing errors in the JSON structure by maintaining a
// list of direct parents to a certain JsonNode.
public class ParseNode {
    private final JsonNode node;
    private final ParseNode parent;
    @Getter
    private final String name;
    @Getter
    private final MetricTags tags;

    public static ParseNode fromRoot(JsonNode root, String namespace) {
        return new ParseNode(null, root, namespace, new MetricTags().append("namespace", namespace));
    }

    private ParseNode(ParseNode parent, JsonNode child, String name, MetricTags tags) {
        this.parent = parent;
        this.node = child;
        this.name = name;
        this.tags = tags;
    }

    private String getPathInternal(boolean includeRoot, String separator) {
        if (parent == null) return includeRoot ? name : "";
        final var parentPath = parent.getPathInternal(includeRoot, separator);
        return !parentPath.isEmpty() ? parentPath + separator + name : name;
    }

    public String longName() {
        return getPathInternal(false, "_");
    }

    @Override
    public String toString() {
        return getPathInternal(true, "->");
    }

    public ParseNode get(String childName, String displayName) {
        return get(childName, displayName, tags);
    }

    public ParseNode get(String childName, String displayName, MetricTags tags) {
        JsonNode child = node.get(childName);
        if (child != null) {
            return new ParseNode(this, child, displayName, tags);
        }
        return null;
    }

    public ParseNode get(String childName) {
        return get(childName, childName);
    }

    public ParseNode appendName(String appendName) {
        // Trick to append "appendName" to the named output, without traversing to an actual child JsonNode
        return new ParseNode(this, node, appendName, tags);
    }

    public List<ParseNode> children(String childTagKey, String elementPrefix) {
        List<ParseNode> result = new ArrayList<>();
        Set<JsonNode> seen = new HashSet<>();
        for (Map.Entry<String, JsonNode> field : node.properties()) {
            seen.add(field.getValue());
            final var childTagValue = elementPrefix + field.getKey();
            result.add(new ParseNode(this, field.getValue(), childTagValue, childTagKey != null ? tags.append(childTagKey, childTagValue) : tags));
        }

        Iterator<JsonNode> elementIterator = node.elements();
        int index = 1;
        while (elementIterator.hasNext()) {
            JsonNode element = elementIterator.next();
            if (!seen.contains(element)) {
                final var childTagValue = elementPrefix + index++;
                result.add(new ParseNode(this, element, childTagValue, childTagKey != null ? tags.append(childTagKey, childTagValue) : tags));
            }
        }
        return result;
    }

    public boolean isNull() {
        return node.isNull();
    }

    public boolean isBoolean() {
        return node.isBoolean();
    }

    public boolean asBoolean() {
        return node.asBoolean();
    }

    public boolean isShort() {
        return node.isShort();
    }

    public short asShort() {
        return node.numberValue().shortValue();
    }

    public boolean isInt() {
        return node.isInt();
    }

    public int asInt() {
        return node.intValue();
    }

    public boolean isLong() {
        return node.isLong();
    }

    public long asLong() {
        return node.longValue();
    }

    public boolean isDouble() {
        return node.isDouble();
    }

    public double asDouble() {
        return node.doubleValue();
    }

    public boolean isFloat() {
        return node.isFloat();
    }

    public float asFloat() {
        return node.floatValue();
    }

    public boolean isBytes() {
        return node.isBinary();
    }

    public byte[] asBytes() {
        try {
            return node.binaryValue();
        } catch (Exception e) {
            throw new ExecutionException("Can not convert node to bytes", e);
        }
    }

    public boolean isString() {
        return node.isTextual();
    }

    public String asString() {
        return node.asText();
    }

    public boolean isArray() {
        return node.isArray();
    }

    public boolean isObject() {
        return node.isObject();
    }
}
