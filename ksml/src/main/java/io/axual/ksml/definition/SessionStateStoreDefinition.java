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

import io.axual.ksml.data.type.UserType;
import io.axual.ksml.store.StoreType;

import java.time.Duration;
import java.util.Objects;

public class SessionStateStoreDefinition extends StateStoreDefinition {
    private final Duration retention;

    public SessionStateStoreDefinition(String name, Boolean persistent, Boolean timestamped, Duration retention, UserType keyType, UserType valueType, Boolean caching, Boolean logging) {
        super(StoreType.SESSION_STORE, name, persistent, timestamped, keyType, valueType, caching, logging);
        this.retention = retention;
    }

    public Duration retention() {
        return retention;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof SessionStateStoreDefinition def)) return false;
        return (super.equals(other)
                && Objects.equals(retention, def.retention));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), retention);
    }
}