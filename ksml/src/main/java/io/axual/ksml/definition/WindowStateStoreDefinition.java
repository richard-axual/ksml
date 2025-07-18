package io.axual.ksml.definition;

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

import io.axual.ksml.store.StoreType;
import io.axual.ksml.type.UserType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Duration;

@Getter
@EqualsAndHashCode
public final class WindowStateStoreDefinition extends StateStoreDefinition {
    private final Duration retention;
    private final Duration windowSize;
    private final boolean retainDuplicates;

    public WindowStateStoreDefinition(String name, Boolean persistent, Boolean timestamped, Duration retention, Duration windowSize, Boolean retainDuplicates, UserType keyType, UserType valueType, Boolean caching, Boolean logging) {
        super(StoreType.WINDOW_STORE, name, persistent, timestamped, keyType, valueType, caching, logging);
        this.retention = retention != null ? retention : Duration.ZERO;
        this.windowSize = windowSize != null ? windowSize : Duration.ZERO;
        this.retainDuplicates = retainDuplicates != null && retainDuplicates;
    }
}
