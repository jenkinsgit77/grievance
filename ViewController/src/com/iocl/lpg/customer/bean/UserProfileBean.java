package com.iocl.lpg.customer.bean;



import com.iocl.lpg.customer.bean.profileOverview.AddressParams;
import com.iocl.lpg.customer.bean.profileOverview.EmailParams;
import com.iocl.lpg.customer.bean.profileOverview.IdentityParams;
import com.iocl.lpg.customer.bean.profileOverview.MobileNoParams;


import ioclcommonproj.com.iocl.beans.VehicleDetailProfileParam;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserProfileBean implements Serializable{   

    private String consumerCity;
    private String consumerState;
    private String consumerId;
    private String consumerContactNumber;
    private String consumerName;
    private String consumerAddress;
    private String consumerEmailId;
    private String partnerName;
    private String partnerContactNum;
    private Integer partnerDeliveryrating;
    private String partnercode;
    private String partnerAdd;
    private String consumerZipCode;
    private String profilePhoto;
    private java.util.List last5ServiceRequest;
    private String dbcStatus;
    private String consumerNumber;
    private String role;
    private String ucmID;
    private String mobile;
    private String email;
    private String cashandCarryFlag;
    private String connectionStatus;
    private String connectionSubStatus;
    private String kycLevel;
    private String sapCode;
    private String subsidyEligible;
    private String consumerStatus;
    private String bankkycStatus;
    private String ptdStatus;
    private String subsidyGiveup;
    private String bankLinkedtoLPG;
    private String bankAccountNumber;
    private String bankIfscCode;
    private String bankName;
    private String aadhaarLinkedtoLPG;
    private String aadhaarLinkedtoBank;
    private String aadhaarNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fbId;
    private String gmailId;
    private String twitterId;
    private Boolean nameChangeEligibility;
    private java.util.List<MobileNoParams> mobileDetails = new java.util.ArrayList<MobileNoParams>(); 
    private java.util.List<AddressParams> addressDetails = new java.util.ArrayList<AddressParams>();
    private java.util.List<EmailParams> emailDetails = new java.util.ArrayList<EmailParams>();
    private java.util.List<IdentityParams> identityDetails = new java.util.ArrayList<IdentityParams>();
    private java.util.List<VehicleDetailProfileParam> vehicleProfileDetails = new java.util.ArrayList<VehicleDetailProfileParam>();
    private Map<String, CustomerRelationBean> lpgRelations = new HashMap<String, CustomerRelationBean>();
    private String consumerProfileService;
    private String lpgSelectedConsumerId;//This field store Current connection(consumerId selected In Case of multiple Connection)
    private String selectedRelationshipUCMId;//This field store Current connection(relationshipucm id selected In Case of multiple Connection)
    private String updateLpgConnMethodCalled = "N";// This filed tell weather method updateActiveLpgConn is called or not
    private String emailId;
    private String emailRowId;
    private String emailVerified;
    private String emailPrimary;
    private String mobileNo;
    
    private String lpgMultipleConnection;// Use this field to check weather customer has multiple connection or not.
    
    private String tempConsumerName;
    private String mobileRowId;
    private String addressRowId;
    private Boolean hasLast5ServiceRequest;
    private String idamUId;
    private Integer lpgRelationsCount;
    private String distributorProduct;
    private String hasBlankUCMId;//Possible Values Y/N
    private String coexistenceExist;
    private String relationshipAgainst;
    
    private String customerAddress1;
    private String customerAddress2;
    private String customerAddress3;
    private String customerLandMark;
    private String customerDistrict;
    private String idamUserMobileNo;
    private String idamUserEmailId;
    private String idamUserFirstName;
    private String idamUserLastName;


    public void setIdamUserFirstName(String idamUserFirstName) {
        this.idamUserFirstName = idamUserFirstName;
    }

    public String getIdamUserFirstName() {
        return idamUserFirstName;
    }

    public void setIdamUserLastName(String idamUserLastName) {
        this.idamUserLastName = idamUserLastName;
    }

    public String getIdamUserLastName() {
        return idamUserLastName;
    }

    public void setIdamUserEmailId(String idamUserEmailId) {
        this.idamUserEmailId = idamUserEmailId;
    }

    public String getIdamUserEmailId() {
        return idamUserEmailId;
    }

    public void setIdamUserMobileNo(String idamUserMobileNo) {
        this.idamUserMobileNo = idamUserMobileNo;
    }

    public String getIdamUserMobileNo() {
        return idamUserMobileNo;
    }

    public void setRelationshipAgainst(String relationshipAgainst) {
        this.relationshipAgainst = relationshipAgainst;
    }

    public String getRelationshipAgainst() {
        return relationshipAgainst;
    }

    public void setCoexistenceExist(String coexistenceExist) {
        this.coexistenceExist = coexistenceExist;
    }

    public String getCoexistenceExist() {
        return coexistenceExist;
    }

    public void setHasBlankUCMId(String hasBlankUCMId) {
        this.hasBlankUCMId = hasBlankUCMId;
    }

    public String getHasBlankUCMId() {
        return hasBlankUCMId;
    }

    public void setDistributorProduct(String distributorProduct) {
        this.distributorProduct = distributorProduct;
    }

    public String getDistributorProduct() {
        return distributorProduct;
    }

    public void setLpgRelationsCount(Integer lpgRelationsCount) {
        this.lpgRelationsCount = lpgRelationsCount;
    }

    public Integer getLpgRelationsCount() {
        return lpgRelationsCount;
    }

    public void setIdamUId(String idamUId) {
        this.idamUId = idamUId;
    }

    public String getIdamUId() {
        return idamUId;
    }
    public void setHasLast5ServiceRequest(Boolean hasLast5ServiceRequest) {
        this.hasLast5ServiceRequest = hasLast5ServiceRequest;
    }

    public Boolean getHasLast5ServiceRequest() {
        return hasLast5ServiceRequest;
    }

    public void setMobileRowId(String mobileRowId) {
        this.mobileRowId = mobileRowId;
    }

    public String getMobileRowId() {
        return mobileRowId;
    }

    public void setAddressRowId(String addressRowId) {
        this.addressRowId = addressRowId;
    }

    public String getAddressRowId() {
        return addressRowId;
    }

    public void setUpdateLpgConnMethodCalled(String updateLpgConnMethodCalled) {
        this.updateLpgConnMethodCalled = updateLpgConnMethodCalled;
    }

    public String getUpdateLpgConnMethodCalled() {
        return updateLpgConnMethodCalled;
    }

    public void setLpgSelectedConsumerId(String lpgSelectedConsumerId) {
        this.lpgSelectedConsumerId = lpgSelectedConsumerId;
    }

    public String getLpgSelectedConsumerId() {
        return lpgSelectedConsumerId;
    }

    public void setConsumerProfileService(String consumerProfileService) {
        this.consumerProfileService = consumerProfileService;
    }

    public String getConsumerProfileService() {
        return consumerProfileService;
    }

    public void setLpgRelations(Map<String, CustomerRelationBean> lpgRelations) {
        this.lpgRelations = lpgRelations;
    }

    public Map<String, CustomerRelationBean> getLpgRelations() {
        return lpgRelations;
    }

    public void setLpgMultipleConnection(String lpgMultipleConnection) {
        this.lpgMultipleConnection = lpgMultipleConnection;
    }

    public String getLpgMultipleConnection() {
        return lpgMultipleConnection;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionSubStatus(String connectionSubStatus) {
        this.connectionSubStatus = connectionSubStatus;
    }

    public String getConnectionSubStatus() {
        return connectionSubStatus;
    }

    public void setKycLevel(String kycLevel) {
        this.kycLevel = kycLevel;
    }

    public String getKycLevel() {
        return kycLevel;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }

    public String getSapCode() {
        return sapCode;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerContactNum(String partnerContactNum) {
        this.partnerContactNum = partnerContactNum;
    }

    public String getPartnerContactNum() {
        return partnerContactNum;
    }

   

    public void setPartnercode(String partnercode) {
        this.partnercode = partnercode;
    }

    public String getPartnercode() {
        return partnercode;
    }

    public void setPartnerAdd(String partnerAdd) {
        this.partnerAdd = partnerAdd;
    }

    public String getPartnerAdd() {
        return partnerAdd;
    }

    public UserProfileBean() {
        super();
    }
    
    public void setConsumerCity(String consumerCity) {
        this.consumerCity = consumerCity;
    }

    public String getConsumerCity() {
        return consumerCity;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerContactNumber(String consumerContactNumber) {
        this.consumerContactNumber = consumerContactNumber;
    }

    public String getConsumerContactNumber() {
        return consumerContactNumber;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerAddress(String consumerAddress) {
        this.consumerAddress = consumerAddress;
    }

    public String getConsumerAddress() {
        return consumerAddress;
    }

    public void setConsumerEmailId(String consumerEmailId) {
        this.consumerEmailId = consumerEmailId;
    }

    public String getConsumerEmailId() {
        return consumerEmailId;
    }

    public void setConsumerZipCode(String consumerZipCode) {
        this.consumerZipCode = consumerZipCode;
    }

    public String getConsumerZipCode() {
        return consumerZipCode;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setLast5ServiceRequest(List last5ServiceRequest) {
        this.last5ServiceRequest = last5ServiceRequest;
    }

    public List getLast5ServiceRequest() {
        return last5ServiceRequest;
    }

    public void setDbcStatus(String dbcStatus) {
        this.dbcStatus = dbcStatus;
    }

    public String getDbcStatus() {
        return dbcStatus;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }


    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setUcmID(String ucmID) {
        this.ucmID = ucmID;
    }

    public String getUcmID() {
        return ucmID;
    }



    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public void setCashandCarryFlag(String cashandCarryFlag) {
        this.cashandCarryFlag = cashandCarryFlag;
    }

    public String getCashandCarryFlag() {
        return cashandCarryFlag;
    }

    public void setConsumerState(String consumerState) {
        this.consumerState = consumerState;
    }

    public String getConsumerState() {
        return consumerState;
    }

    public void setSubsidyEligible(String subsidyEligible) {
        this.subsidyEligible = subsidyEligible;
    }

    public String getSubsidyEligible() {
        return subsidyEligible;
    }


    public void setConsumerStatus(String consumerStatus) {
        this.consumerStatus = consumerStatus;
    }

    public String getConsumerStatus() {
        return consumerStatus;
    }

    public void setBankkycStatus(String bankkycStatus) {
        this.bankkycStatus = bankkycStatus;
    }

    public String getBankkycStatus() {
        return bankkycStatus;
    }

    public void setPtdStatus(String ptdStatus) {
        this.ptdStatus = ptdStatus;
    }

    public String getPtdStatus() {
        return ptdStatus;
    }

    public void setSubsidyGiveup(String subsidyGiveup) {
        this.subsidyGiveup = subsidyGiveup;
    }

    public String getSubsidyGiveup() {
        return subsidyGiveup;
    }

    public void setBankLinkedtoLPG(String bankLinkedtoLPG) {
        this.bankLinkedtoLPG = bankLinkedtoLPG;
    }

    public String getBankLinkedtoLPG() {
        return bankLinkedtoLPG;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankIfscCode(String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }

    public String getBankIfscCode() {
        return bankIfscCode;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setAadhaarLinkedtoLPG(String aadhaarLinkedtoLPG) {
        this.aadhaarLinkedtoLPG = aadhaarLinkedtoLPG;
    }

    public String getAadhaarLinkedtoLPG() {
        return aadhaarLinkedtoLPG;
    }

    public void setAadhaarLinkedtoBank(String aadhaarLinkedtoBank) {
        this.aadhaarLinkedtoBank = aadhaarLinkedtoBank;
    }

    public String getAadhaarLinkedtoBank() {
        return aadhaarLinkedtoBank;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setTempConsumerName(String tempConsumerName) {
        this.tempConsumerName = tempConsumerName;
    }

    public String getTempConsumerName() {
        return tempConsumerName;
    }

    public void setPartnerDeliveryrating(Integer partnerDeliveryrating) {
        this.partnerDeliveryrating = partnerDeliveryrating;
    }

    public Integer getPartnerDeliveryrating() {
        return partnerDeliveryrating;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFbId() {
        return fbId;
    }

    public void setGmailId(String gmailId) {
        this.gmailId = gmailId;
    }

    public String getGmailId() {
        return gmailId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setNameChangeEligibility(Boolean nameChangeEligibility) {
        this.nameChangeEligibility = nameChangeEligibility;
    }

    public Boolean getNameChangeEligibility() {
        return nameChangeEligibility;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailRowId(String emailRowId) {
        this.emailRowId = emailRowId;
    }

    public String getEmailRowId() {
        return emailRowId;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailPrimary(String emailPrimary) {
        this.emailPrimary = emailPrimary;
    }

    public String getEmailPrimary() {
        return emailPrimary;
    }

    public void setMobileDetails(List<MobileNoParams> mobileDetails) {
        this.mobileDetails = mobileDetails;
    }

    public List<MobileNoParams> getMobileDetails() {
        return mobileDetails;
    }

    public void setAddressDetails(List<AddressParams> addressDetails) {
        this.addressDetails = addressDetails;
    }

    public List<AddressParams> getAddressDetails() {
        return addressDetails;
    }

    public void setEmailDetails(List<EmailParams> emailDetails) {
        this.emailDetails = emailDetails;
    }

    public List<EmailParams> getEmailDetails() {
        return emailDetails;
    }

    public void setIdentityDetails(List<IdentityParams> identityDetails) {
        this.identityDetails = identityDetails;
    }

    public List<IdentityParams> getIdentityDetails() {
        return identityDetails;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setSelectedRelationshipUCMId(String selectedRelationshipUCMId) {
        this.selectedRelationshipUCMId = selectedRelationshipUCMId;
    }

    public String getSelectedRelationshipUCMId() {
        return selectedRelationshipUCMId;
    }

    public void setCustomerAddress1(String customerAddress1) {
        this.customerAddress1 = customerAddress1;
    }

    public String getCustomerAddress1() {
        return customerAddress1;
    }

    public void setCustomerAddress2(String customerAddress2) {
        this.customerAddress2 = customerAddress2;
    }

    public String getCustomerAddress2() {
        return customerAddress2;
    }

    public void setCustomerAddress3(String customerAddress3) {
        this.customerAddress3 = customerAddress3;
    }

    public String getCustomerAddress3() {
        return customerAddress3;
    }

    public void setCustomerLandMark(String customerLandMark) {
        this.customerLandMark = customerLandMark;
    }

    public String getCustomerLandMark() {
        return customerLandMark;
    }

    public void setCustomerDistrict(String customerDistrict) {
        this.customerDistrict = customerDistrict;
    }

    public String getCustomerDistrict() {
        return customerDistrict;
    }

    public void setVehicleProfileDetails(List<VehicleDetailProfileParam> vehicleProfileDetails) {
        this.vehicleProfileDetails = vehicleProfileDetails;
    }

    public List<VehicleDetailProfileParam> getVehicleProfileDetails() {
        return vehicleProfileDetails;
    }


}
