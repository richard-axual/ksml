package io.axual.ksml.data.schema;

/*-
 * ========================LICENSE_START=================================
 * KSML Data Library
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

/**
 * Record containing a range of reserved tags
 *
 * @param beginInclusive the first tag of the range to exclude
 * @param endInclusive   the last tag of the range to exclude
 */
public record ReservedTagRange(int beginInclusive, int endInclusive) {
    public ReservedTagRange {
        if (beginInclusive < 1 || endInclusive < 1) {
            throw new IllegalArgumentException("Begin/end values must be positive (non-zero) integers");
        }
        if (beginInclusive > endInclusive) {
            throw new IllegalArgumentException("beginInclusive must be less than endInclusive");
        }
    }

    /**
     *
     * Create a record containing a range of reserved tags
     *
     * @param beginInclusive the first tag of the range to reserve
     * @param endInclusive   the last tag of the range to reserve
     * @return a new instance of the {@code ReservedTagRange}
     */
    public static ReservedTagRange of(int beginInclusive, int endInclusive) {
        return new ReservedTagRange(beginInclusive, endInclusive);
    }

    /**
     *
     * Create a record containing a single reserved tag
     *
     * @param reserved the first and only tag exclude
     * @return a new instance of the {@code ReservedTagRange}
     */
    public static ReservedTagRange of(int reserved) {
        return new ReservedTagRange(reserved, reserved);
    }
}
