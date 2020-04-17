package com.iocl.lpg.customer.bean.profileOverview;

import java.io.Serializable;

public class IdentityParams implements Serializable{
    @SuppressWarnings("compatibility:7543167878949078771")
    private static final long serialVersionUID = 1L;

    public IdentityParams() {
        super();
    }
    
    private String identityType;
    private String identityValue;
    private String identityContentId;
    private String documentName;
    private String documentExtension;
    private String documentId;
    private String documentLink;


    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentExtension(String documentExtension) {
        this.documentExtension = documentExtension;
    }

    public String getDocumentExtension() {
        return documentExtension;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityValue(String identityValue) {
        this.identityValue = identityValue;
    }

    public String getIdentityValue() {
        return identityValue;
    }

    public void setIdentityContentId(String identityContentId) {
        this.identityContentId = identityContentId;
    }

    public String getIdentityContentId() {
        return identityContentId;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }

    public String getDocumentLink() {
        return documentLink;
    }
}
