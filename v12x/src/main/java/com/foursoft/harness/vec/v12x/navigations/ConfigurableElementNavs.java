/*-
 * ========================LICENSE_START=================================
 * vec-v12x
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
package com.foursoft.harness.vec.v12x.navigations;

import com.foursoft.harness.vec.v12x.VecConfigurableElement;
import com.foursoft.harness.vec.v12x.VecVariantConfiguration;

import java.util.Optional;
import java.util.function.Function;

/**
 * Navigation methods for the {@link VecConfigurableElement}.
 */
public final class ConfigurableElementNavs {

    private ConfigurableElementNavs() {
        // hide default constructor
    }

    public static Function<VecConfigurableElement, Optional<VecVariantConfiguration>> variantConfiguration() {
        return configurableElement -> Optional.ofNullable(configurableElement)
                .map(VecConfigurableElement::getConfigInfo);
    }

    public static Function<VecConfigurableElement, Optional<String>> controlInformation() {
        return configurableElement -> Optional.ofNullable(configurableElement)
                .map(VecConfigurableElement::getConfigInfo)
                .map(VecVariantConfiguration::getLogisticControlString);
    }

    public static Function<VecConfigurableElement, Optional<String>> controlExpression() {
        return configurableElement -> Optional.ofNullable(configurableElement)
                .map(VecConfigurableElement::getConfigInfo)
                .map(VecVariantConfiguration::getLogisticControlExpression);
    }

}
