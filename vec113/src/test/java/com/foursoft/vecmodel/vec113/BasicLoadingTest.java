/*-
 * ========================LICENSE_START=================================
 * vec113
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
package com.foursoft.vecmodel.vec113;

import com.foursoft.vecmodel.vec113.visitor.ToStringVisitor;
import com.foursoft.xml.model.Identifiable;
import com.foursoft.vecmodel.vec113.common.EventConsumer;
import com.foursoft.xml.ExtendedUnmarshaller;
import com.foursoft.xml.JaxbModel;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicLoadingTest {

    @Test
    public void testLoadModel() throws IOException, JAXBException {
        final ExtendedUnmarshaller<VecContent, Identifiable> unmarshaller =
            new ExtendedUnmarshaller<VecContent, Identifiable>(VecContent.class)
                .withBackReferences()
                .withEventLogging(new EventConsumer())
                .withIdMapper(Identifiable.class, Identifiable::getXmlId);

        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final JaxbModel<VecContent, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(inputStream));
            assertThat(model).isNotNull();
        }
    }

    @Test
    public void testSelectorInheritance() throws JAXBException, IOException {
        try (final InputStream is = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final ExtendedUnmarshaller<VecContent, Identifiable> unmarshaller =
                new ExtendedUnmarshaller<VecContent, Identifiable>(VecContent.class)
                    .withBackReferences()
                    .withIdMapper(Identifiable.class, Identifiable::getXmlId);

            final JaxbModel<VecContent, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));

            assertThat(model).isNotNull();

            final VecApproval approval = model.getIdLookup()
                    .findById(VecApproval.class, "id_2014_0")
                    .orElse(null);

            assertThat(approval).isNotNull();

            final VecPermission vecPermission = model.getIdLookup()
                    .findById(VecPermission.class, "id_2185_0")
                    .orElse(null);

            assertThat(vecPermission).isNotNull();

            final List<VecPermission> permissions = approval.getPermissions();

            assertThat(permissions)
                    .isNotEmpty()
                    .hasSize(1)
                    .containsExactly(vecPermission);
        }
    }

    @Test
    public void validationTest() throws Exception {
        try (final InputStream is = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            final Source source = new StreamSource(getClass().getResourceAsStream("/vec113/vec_1.1.3.xsd"));

            final Schema schema = schemaFactory.newSchema(source);

            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setNamespaceAware(true);
            dbf.setSchema(schema);

            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document d = db.parse(new BufferedInputStream(is));
            assertThat(d).isNotNull();
        }
    }

    @Test
    public void testWithLogging() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final VecContent content = VecReader.read(inputStream);
            assertThat(content).isNotNull();
        }
    }

    @Test
    public void testVisitor() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final VecContent rootElement = VecReader.read(inputStream);
            final ToStringVisitor toStringVisitor = new ToStringVisitor();

            final String vecVersion = rootElement.getVecVersion();
            final String generatingSystemName = rootElement.getGeneratingSystemName();
            final XMLGregorianCalendar dateOfCreation = rootElement.getDateOfCreation();
            final String generatingSystemVersion = rootElement.getGeneratingSystemVersion();

            final VecCopyrightInformation standardCopyright = rootElement.getStandardCopyrightInformation();
            final String copyrightNotesAsString = standardCopyright == null
                    ? "None"
                    : standardCopyright.accept(toStringVisitor);

            final List<VecConformanceClass> compliantConformanceClasses = rootElement.getCompliantConformanceClasses();
            final String compliantConformanceClassesAsString = compliantConformanceClasses.stream()
                    .map(conformanceClass -> '\t' + conformanceClass.accept(toStringVisitor))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecContract> contracts = rootElement.getContracts();
            final String contractsAsString = contracts.stream()
                    .map(contract -> '\t' + contract.accept(toStringVisitor))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecCopyrightInformation> copyrightInformations = rootElement.getCopyrightInformations();
            final String copyrightInformationAsString = copyrightInformations.stream()
                    .map(copyrightInformation -> '\t' + copyrightInformation.accept(toStringVisitor))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecDocumentVersion> documentVersions = rootElement.getDocumentVersions();
            final String documentVersionsAsString = documentVersions.stream()
                    .map(documentVersion -> '\t' + documentVersion.accept(toStringVisitor))
                    .collect(Collectors.joining(System.lineSeparator()));

            final StringBuilder builder = new StringBuilder()
                    .append("VEC Version: ").append(vecVersion).append(System.lineSeparator())
                    .append("System name: ").append(generatingSystemName).append(System.lineSeparator())
                    .append("System version: ").append(generatingSystemVersion).append(System.lineSeparator())
                    .append("Date of creation: ").append(dateOfCreation).append(System.lineSeparator())
                    .append("Standard copyright: ").append(copyrightNotesAsString)
                    .append(System.lineSeparator());

            builder.append("Conformance classes:");
            if (compliantConformanceClassesAsString.isEmpty())  {
                builder.append(" None");
            }  else {
                builder
                        .append(System.lineSeparator())
                        .append(compliantConformanceClassesAsString);
            }
            builder.append(System.lineSeparator());

            builder.append("Contracts:");
            if (contractsAsString.isEmpty())  {
                builder.append(" None");
            }  else {
                builder
                        .append(System.lineSeparator())
                        .append(contractsAsString);
            }
            builder.append(System.lineSeparator());

            builder.append("Copyright information:");
            if (copyrightInformationAsString.isEmpty())  {
                builder.append(" None");
            }  else {
                builder
                        .append(System.lineSeparator())
                        .append(copyrightInformationAsString);
            }
            builder.append(System.lineSeparator());

            builder.append("Document versions:");
            if (documentVersionsAsString.isEmpty())  {
                builder.append(" None");
            }  else {
                builder
                        .append(System.lineSeparator())
                        .append(documentVersionsAsString);
            }

            final String output = builder.toString();

            System.out.println(output);
        }
    }

}
