package com.foursoft.vecmodel.vec120.visitor;

import com.foursoft.vecmodel.vec120.*;

import java.util.stream.Collectors;

public class ToStringVisitor extends BaseVisitor<String, RuntimeException> {

    @Override
    public String visitVecCopyrightInformation(VecCopyrightInformation aBean) throws RuntimeException {
        final String copyrightNotes = aBean.getCopyrightNotes().stream()
                .map(VecAbstractLocalizedString::getValue)
                .collect(Collectors.joining(", "));
        final String referencedItems = aBean.getRefItemVersion().stream()
                .map(VecExtendableElement::getXmlId)
                .collect(Collectors.joining(", "));

        return "VecCopyrightInformation=" + (copyrightNotes.isEmpty()
                ? "None"
                : String.format("Copyright Notes: %s; Referenced Items: %s", copyrightNotes, referencedItems));
    }

    @Override
    public String visitVecContract(VecContract aBean) throws RuntimeException {
        return String.format("VecContract with Company: %s, Role: %s, Item Versions: %s",
                aBean.getCompanyName(), aBean.getContractRole(), aBean.getRefItemVersion());
    }

    @Override
    public String visitVecDocumentVersion(VecDocumentVersion aBean) throws RuntimeException {
        return String.format("VecDocument with Document Number: %s, Document Version: %s, Document Type: %s, " +
                        "Document Location: %s, Linked Document: %s, Amount of sheets: %s",
                aBean.getDocumentNumber(), aBean.getDocumentVersion(), aBean.getDocumentType(),
                aBean.getLocation(), aBean.getFileName(), aBean.getNumberOfSheets());
    }
}
