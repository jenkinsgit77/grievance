package com.iocl.lpg.customer.bean;

import java.io.Serializable;

public class NFRUCMImage implements Serializable{
    public NFRUCMImage() {
        super();
    }
    private String ucmUrl;
    private String ucmCategoryId;
    private String ucmCategoryName;
    private String ucmProductName;
    private String ucmProductId;

    public void setUcmCategoryId(String ucmCategoryId) {
        this.ucmCategoryId = ucmCategoryId;
    }

    public String getUcmCategoryId() {
        return ucmCategoryId;
    }

    public void setUcmCategoryName(String ucmCategoryName) {
        this.ucmCategoryName = ucmCategoryName;
    }

    public String getUcmCategoryName() {
        return ucmCategoryName;
    }

    public void setUcmProductName(String ucmProductName) {
        this.ucmProductName = ucmProductName;
    }

    public String getUcmProductName() {
        return ucmProductName;
    }

    public void setUcmProductId(String ucmProductId) {
        this.ucmProductId = ucmProductId;
    }

    public String getUcmProductId() {
        return ucmProductId;
    }

    public void setUcmUrl(String ucmUrl) {
        this.ucmUrl = ucmUrl;
    }

    public String getUcmUrl() {
        return ucmUrl;
    }     
    
    public NFRUCMImage(String ucmUrl) {
        this.ucmUrl = ucmUrl;
    }
}
