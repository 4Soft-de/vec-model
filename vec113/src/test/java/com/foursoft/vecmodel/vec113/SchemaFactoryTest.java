/*-
 * ========================LICENSE_START=================================
 * vec113
 * %%
 * Copyright (C) 2020 - 2021 4Soft GmbH
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
package com.foursoft.vecmodel.vec113;

import com.foursoft.xml.io.validation.LogValidator;
import com.foursoft.xml.io.validation.XMLValidation;
import org.junit.jupiter.api.Test;

import javax.xml.validation.Schema;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class SchemaFactoryTest {

    @Test
    void testStrictSchema() {
        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.1.3");


        final VecWriter vecWriter = new VecWriter();
        final String result = vecWriter.writeToString(root);

        final Schema schema = SchemaFactory.getStrictSchema();
        final Collection<LogValidator.ErrorLocation> errorLocations =
                new XMLValidation(schema).validateXML(result, StandardCharsets.UTF_8);

        assertThat(errorLocations).isEmpty();

    }

    @Test
    void testInvalidSchema() {
        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.1.3");

        final VecPartVersion partVersion = new VecPartVersion();
        partVersion.setXmlId("id_1001_0");
        partVersion.setPartNumber("123_456_789");
        root.getPartVersions().add(partVersion);

        final VecWriter vecWriter = new VecWriter();
        final String result = vecWriter.writeToString(root);

        final Schema schema = SchemaFactory.getSchema();
        final Collection<LogValidator.ErrorLocation> errorLocations =
                new XMLValidation(schema).validateXML(result, StandardCharsets.UTF_8);

        assertThat(errorLocations).isNotEmpty();
    }
}
