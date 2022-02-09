/*-
 * ========================LICENSE_START=================================
 * vec120
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.vecmodel.vec120.navigations;

import com.foursoft.vecmodel.common.HasSpecifications;
import com.foursoft.vecmodel.common.annotations.RequiresBackReferences;
import com.foursoft.vecmodel.vec120.*;
import com.foursoft.vecmodel.vec120.utils.StreamUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Navigation methods for the {@link VecSpecification}.
 */
public final class SpecificationNavs {

    private SpecificationNavs() {
        // hide default constructor
    }

    /**
     * Gets the first specification with the given type.
     * <b>Warning: There might be multiple specifications with the given type.
     * Only use this method if you are sure there will just be one element!</b>
     *
     * @param type Class of T.
     * @param <T>  Type of Specification to filter for.
     * @return The first specification with the given type if found, else an empty optional.
     */
    public static <T extends VecSpecification> Function<HasSpecifications<VecSpecification>, Optional<T>> specificationOf(
            final Class<T> type) {
        return hasSpecification -> hasSpecification.getSpecificationsWithType(type)
                .stream().collect(StreamUtils.findOneOrNone());
    }

    public static <T extends VecSpecification> Function<HasSpecifications<VecSpecification>, Optional<T>> specificationOf(
            final Class<T> type, final String specificationType) {
        return hasSpecification -> hasSpecification.getSpecificationsWithType(type)
                .stream()
                .filter(spec -> specificationType.equals(spec.getIdentification()))
                .collect(StreamUtils.findOneOrNone());
    }

    @RequiresBackReferences
    public static Function<VecSpecification, String> parentDocumentNumber() {
        return spec -> parentDocumentVersion().apply(spec).getDocumentNumber();
    }

    public static Function<HasSpecifications<VecSpecification>, Stream<VecOccurrenceOrUsage>> allOccurrenceOrUsages() {
        return hasSpecifications -> Stream.concat(
                hasSpecifications.getSpecificationsWithType(VecCompositionSpecification.class)
                        .stream()
                        .map(VecCompositionSpecification::getComponents)
                        .flatMap(Collection::stream),
                hasSpecifications.getSpecificationsWithType(VecPartUsageSpecification.class)
                        .stream()
                        .map(VecPartUsageSpecification::getPartUsages)
                        .flatMap(Collection::stream));
    }

    @RequiresBackReferences
    public static Function<VecSpecification, VecDocumentVersion> parentDocumentVersion() {
        return specification -> {
            final VecSheetOrChapter sheetOrChapter = specification.getParentSheetOrChapter();
            final VecDocumentVersion documentVersion = specification.getParentDocumentVersion();
            if (documentVersion != null) {
                return documentVersion;
            } else {
                return sheetOrChapter.getParentDocumentVersion();
            }
        };
    }

}
