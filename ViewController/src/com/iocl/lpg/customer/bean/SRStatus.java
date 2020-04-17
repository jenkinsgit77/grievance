package com.iocl.lpg.customer.bean;

import java.io.Serializable;

public class SRStatus implements Serializable{
    public SRStatus() {
        super();
    }
    
    private String srNumber;
    private String srOpenDate;
    private String srCurrStatus;

    public void setSrCurrStatus(String srCurrStatus) {
        this.srCurrStatus = srCurrStatus;
    }

    public String getSrCurrStatus() {
        return srCurrStatus;
    }

    public void setSrOpenDate(String srOpenDate) {
        this.srOpenDate = srOpenDate;
    }

    public String getSrOpenDate() {
        return srOpenDate;
    }

    public void setSrNumber(String srNumber) {
        this.srNumber = srNumber;
    }

    public String getSrNumber() {
        return srNumber;
    }

}
