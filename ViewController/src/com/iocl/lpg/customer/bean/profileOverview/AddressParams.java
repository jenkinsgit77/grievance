package com.iocl.lpg.customer.bean.profileOverview;
import java.io.Serializable;

public class AddressParams implements Serializable{
    @SuppressWarnings("compatibility:-9016080036392720363")
    private static final long serialVersionUID = 1L;

    public AddressParams() {
        super();
    }
    
    private String address;
    private String addRowId;
    private String addPrimary;
    
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String district;
    private String state;
    private String landmark;
    private String pinCode;
    
    private String verifiedFlag;
    private boolean changeAddressFlowCall;
    
    public void setChangeAddressFlowCall(boolean changeAddressFlowCall) {
        this.changeAddressFlowCall = changeAddressFlowCall;
    }

    public boolean isChangeAddressFlowCall() {
        return changeAddressFlowCall;
    }



    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddRowId(String addRowId) {
        this.addRowId = addRowId;
    }

    public String getAddRowId() {
        return addRowId;
    }

    public void setAddPrimary(String addPrimary) {
        this.addPrimary = addPrimary;
    }

    public String getAddPrimary() {
        return addPrimary;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress3() {
        return address3;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPinCode() {
        return pinCode;
    }


    public void setVerifiedFlag(String verifiedFlag) {
        this.verifiedFlag = verifiedFlag;
    }

    public String getVerifiedFlag() {
        return verifiedFlag;
    }
}
