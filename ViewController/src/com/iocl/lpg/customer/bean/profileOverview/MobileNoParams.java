package com.iocl.lpg.customer.bean.profileOverview;
import java.io.Serializable;

public class MobileNoParams implements Serializable{
    @SuppressWarnings("compatibility:5610168290332216551")
    private static final long serialVersionUID = 1L;

    public MobileNoParams() {
        super();
    }
    
    private String mobileNo;
    private String maskedMobileNum;
    private String mobileRowId;
    private String mobilePrimary;
    private String mobileVerified;

    public void setMobileVerified(String mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getMobileVerified() {
        return mobileVerified;
    }

    public void setMaskedMobileNum(String maskedMobileNum) {
        this.maskedMobileNum = maskedMobileNum;
    }

    public String getMaskedMobileNum() {
        return maskedMobileNum;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileRowId(String mobileRowId) {
        this.mobileRowId = mobileRowId;
    }

    public String getMobileRowId() {
        return mobileRowId;
    }

    public void setMobilePrimary(String mobilePrimary) {
        this.mobilePrimary = mobilePrimary;
    }

    public String getMobilePrimary() {
        return mobilePrimary;
    }
}
