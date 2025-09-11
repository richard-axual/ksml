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

import com.squareup.wire.schema.Location;
import com.squareup.wire.schema.internal.parser.OptionElement;
import com.squareup.wire.schema.internal.parser.ProtoFileElement;
import com.squareup.wire.schema.internal.parser.TypeElement;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProtoComparators {

   public static class LocationComparator implements Comparator<Location> {

        // Always see locations as equal
        @Override
        public int compare(final Location o1, final Location o2) {
            return 0;
        }
    }


    public static class ListOfComparator<T> implements Comparator<List<T>> {
        private final Comparator<T> itemComparator;
        private final T[] toArray;
        public ListOfComparator(final Comparator<T> itemComparator, T[] toArray) {
            this.itemComparator = Objects.requireNonNull(itemComparator);
            this.toArray = Objects.requireNonNull(toArray);
        }
        @Override
        public int compare(final List<T> o1, final List<T> o2) {
            var result = o1.size()-o2.size();
            if( result != 0){
                return result;
            }

            var o1Data = (List<T>) Arrays.asList(toArray);
            o1Data.sort(itemComparator);
            var o2Data = Arrays.asList(o2.toArray(toArray));
            o2Data.sort(itemComparator);

            for(int i = 0; i < o1Data.size(); i++){
                var o1Type = o1Data.get(i);
                var o2Type = o2Data.get(i);
                var typeResult = itemComparator.compare(o1Type, o2Type);
                if( typeResult != 0){
                    return result;
                }
            }
            return 0;
        }
    }

    public static final Comparator<OptionElement> OPTION_ELEMENT_COMPARATOR = Comparator.comparing(OptionElement::getName)
            .thenComparing(OptionElement::getKind);

    public static final Comparator<List<OptionElement>> LIST_OPTION_ELEMENT_COMPARATOR = new ListOfComparator<>(OPTION_ELEMENT_COMPARATOR, new OptionElement[0]);

    public static final Comparator<List<TypeElement>> LIST_TYPE_ELEMENT_COMPARATOR = new Comparator<List<TypeElement>>() {
        @Override
        public int compare(final List<TypeElement> o1, final List<TypeElement> o2) {
            var result = o1.size()-o2.size();
            if( result != 0){
                return result;
            }

            var o1Data = Arrays.asList(o1.toArray(new TypeElement[0]));
            o1Data.sort(TYPE_ELEMENT_COMPARATOR);
            var o2Data = Arrays.asList(o2.toArray(new TypeElement[0]));
            o2Data.sort(TYPE_ELEMENT_COMPARATOR);

            for(int i = 0; i < o1Data.size(); i++){
                var o1Type = o1Data.get(i);
                var o2Type = o2Data.get(i);
                var typeResult = TYPE_ELEMENT_COMPARATOR.compare(o1Type, o2Type);
                if( typeResult != 0){
                    return result;
                }
            }
            return 0;
        }
    };

    public static final Comparator<TypeElement> TYPE_ELEMENT_COMPARATOR = Comparator.comparing(TypeElement::getName)
            .thenComparing(TypeElement::getDocumentation)
            .thenComparing(TypeElement::getOptions, LIST_OPTION_ELEMENT_COMPARATOR)
            .thenComparing(TypeElement::getNestedTypes, LIST_TYPE_ELEMENT_COMPARATOR);
    public static final Comparator<ProtoFileElement> FILE_ELEMENT_COMPARATOR = Comparator.comparing(ProtoFileElement::getPackageName)
            .thenComparing(ProtoFileElement::getOptions, LIST_OPTION_ELEMENT_COMPARATOR)
            .thenComparing(ProtoFileElement::getTypes,LIST_TYPE_ELEMENT_COMPARATOR);

}
