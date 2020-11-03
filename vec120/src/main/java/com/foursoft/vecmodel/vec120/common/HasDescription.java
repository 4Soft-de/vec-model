/*-
 * ========================LICENSE_START=================================
 * vec120
 * %%
 * Copyright (C) 2020 4Soft GmbH
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
package com.foursoft.vecmodel.vec120.common;

import java.util.List;
import java.util.stream.Collectors;

@FunctionalInterface
public interface HasDescription<T> {
    /**
     * This method returns a {@link List } of all description objects this element owns. The list is of type
     * {@link HasLocalizedString }, because the descriptions can have a specific (technical) type and therefore
     * are of class {@link HasLocalizedTypeString }.
     *
     * @return {@link List } of {@link HasLocalizedString } including all description objects.
     */
    <R extends HasLocalizedString> List<R> getDescriptions();

    /**
     * This method returns a {@link List } of all typed descriptions of this element, filtered by the given
     * type as {@link String }.
     *
     * @return {@link List } of {@link HasLocalizedTypeString } including all description objects of the given type.
     */
    default List<HasLocalizedTypeString> getDescriptions(final String type) {
        return getDescriptions().stream()
                .filter(HasLocalizedTypeString.class::isInstance)
                .map(HasLocalizedTypeString.class::cast)
                .filter(typedString -> type.equals(typedString.getType()))
                .collect(Collectors.toList());
    }

    /**
     * This method returns a {@link List } of all description elements of this element, filtered by the
     * given  VecLanguageCode.
     *
     * @return {@link List } of {@link T } including all description objects with the
     * given VecLanguageCode.
     */
    default List<HasLocalizedString> getDescriptions(final T languageCode) {
        return getDescriptions().stream()
                .filter(typedString -> typedString.getLanguageCode() == languageCode)
                .collect(Collectors.toList());
    }

    /**
     * This method returns a {@link List } of all typed descriptions of this element, filtered by the given
     * type as {@link String } and the given VecLanguageCode.
     *
     * @return {@link List } of {@link HasLocalizedTypeString } including all description objects of the given type.
     */
    default List<HasLocalizedTypeString> getDescriptions(final T languageCode, final String type) {
        return getDescriptions(type).stream()
                .filter(typedString -> typedString.getLanguageCode() == languageCode)
                .collect(Collectors.toList());
    }
}
