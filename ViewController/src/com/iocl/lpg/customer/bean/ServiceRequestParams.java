package com.iocl.lpg.customer.bean;

import java.io.Serializable;

public class ServiceRequestParams implements Serializable {
    public ServiceRequestParams() {
        super();
    }
    
    private String srStatus;
    private String srNumber;
    private String srOpenDate;

    public void setSrStatus(String srStatus) {
        this.srStatus = srStatus;
    }

    public String getSrStatus() {
        return srStatus;
    }

    public void setSrNumber(String srNumber) {
        this.srNumber = srNumber;
    }

    public String getSrNumber() {
        return srNumber;
    }

    public void setSrOpenDate(String srOpenDate) {
        this.srOpenDate = srOpenDate;
    }

    public String getSrOpenDate() {
        return srOpenDate;
    }
}
