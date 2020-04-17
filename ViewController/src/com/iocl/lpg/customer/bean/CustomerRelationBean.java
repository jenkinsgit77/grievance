package com.iocl.lpg.customer.bean;


import com.iocl.lpg.customer.utils.CommonHelper;

import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class CustomerRelationBean implements Serializable{
    
    private String consumerId;
    private String relationshipUCMId;
    private String dbcStatus;
    private String partnercode;
    private String cashandCarryFlag;
    private String consumerNumber;
    private String partnerAdd;
    private Integer partnerDeliveryrating;
    private String partnerContactNum;
    private String partnerName;
    private String subsidyEligible;
    private String connectionStatus;
    private String connectionSubStatus;
    private String ptdStatus;
    private String aadhaarLinkedtoLPG;  
    private String mobileRowId;
    private String emailRowId;
    private String addressRowId ;
    private String lpgConnOption;
    private String subsidyGiveFlag;
    private String aadhaarLinkedtoBank;
    private String bankLinkedtoLPG;
    private String distributorProduct;
    private String kycLevel;
    private String relationshipAgainst;
    
    public CustomerRelationBean() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(CustomerRelationBean.class);

        } else {
            log = Logger.getLogger(CustomerRelationBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    private static Logger log ;

    public CustomerRelationBean(String consumerId, String relationshipUCMId, String dbcStatus, String partnercode,
                                String cashandCarryFlag, String consumerNumber, String partnerAdd,
                                Integer partnerDeliveryrating, String partnerContactNum, String partnerName,
                                String connectionStatus, String connectionSubStatus, String ptdStatus,
                                String subsidyGiveFlag, String aadhaarLinkedtoLPG, String mobileRowId,
                                String emailRowId, String addressRowId, String aadhaarLinkedtoBank,
                                String bankLinkedtoLPG, String distributorProduct, String kycLevel,String relationshipAgainst) {
        this.consumerId = consumerId;
        this.relationshipUCMId = relationshipUCMId;
        this.dbcStatus = dbcStatus;
        this.partnercode = partnercode;
        this.cashandCarryFlag = cashandCarryFlag;
        this.consumerNumber = consumerNumber;
        this.partnerAdd = partnerAdd;
        this.partnerDeliveryrating = partnerDeliveryrating;
        this.partnerContactNum = partnerContactNum;
        this.partnerName = partnerName;
        this.connectionStatus = connectionStatus;
        this.connectionSubStatus = connectionSubStatus;
        this.ptdStatus = ptdStatus;
        this.subsidyGiveFlag = subsidyGiveFlag;
        this.aadhaarLinkedtoLPG = aadhaarLinkedtoLPG;
        this.mobileRowId = mobileRowId;
        this.emailRowId = emailRowId;
        this.addressRowId = addressRowId;
        this.aadhaarLinkedtoBank = aadhaarLinkedtoBank;
        this.bankLinkedtoLPG = bankLinkedtoLPG;
        this.distributorProduct = distributorProduct;
        this.kycLevel = kycLevel;
        this.relationshipAgainst = relationshipAgainst;
    }


    public void setRelationshipAgainst(String relationshipAgainst) {
        this.relationshipAgainst = relationshipAgainst;
    }

    public String getRelationshipAgainst() {
        return relationshipAgainst;
    }

    public void setKycLevel(String kycLevel) {
        this.kycLevel = kycLevel;
    }

    public String getKycLevel() {
        return kycLevel;
    }

    public void setAadhaarLinkedtoBank(String aadhaarLinkedtoBank) {
        this.aadhaarLinkedtoBank = aadhaarLinkedtoBank;
    }

    public String getAadhaarLinkedtoBank() {
        return aadhaarLinkedtoBank;
    }

    public void setBankLinkedtoLPG(String bankLinkedtoLPG) {
        this.bankLinkedtoLPG = bankLinkedtoLPG;
    }

    public String getBankLinkedtoLPG() {
        return bankLinkedtoLPG;
    }

    public void setDistributorProduct(String distributorProduct) {
        this.distributorProduct = distributorProduct;
    }

    public String getDistributorProduct() {
        return distributorProduct;
    }

    public void setSubsidyGiveFlag(String subsidyGiveFlag) {
        this.subsidyGiveFlag = subsidyGiveFlag;
    }

    public String getSubsidyGiveFlag() {
        return subsidyGiveFlag;
    }

    public void setLpgConnOption(String lpgConnOption) {
        this.lpgConnOption = lpgConnOption;
    }

    public String getLpgConnOption() {
        log.info("LPGConnection Option"+relationshipUCMId +" : "+connectionStatus+" : "+connectionSubStatus);
        return (relationshipUCMId +" : "+ CommonHelper.getValueFromRsBundle(connectionStatus) +" : "+ CommonHelper.getValueFromRsBundle(connectionSubStatus));
//        return lpgConnOption;
    }

    public void setMobileRowId(String mobileRowId) {
        this.mobileRowId = mobileRowId;
    }

    public String getMobileRowId() {
        return mobileRowId;
    }

    public void setEmailRowId(String emailRowId) {
        this.emailRowId = emailRowId;
    }

    public String getEmailRowId() {
        return emailRowId;
    }

    public void setAddressRowId(String addressRowId) {
        this.addressRowId = addressRowId;
    }

    public String getAddressRowId() {
        return addressRowId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setDbcStatus(String dbcStatus) {
        this.dbcStatus = dbcStatus;
    }

    public String getDbcStatus() {
        return dbcStatus;
    }

    public void setPartnercode(String partnercode) {
        this.partnercode = partnercode;
    }

    public String getPartnercode() {
        return partnercode;
    }

    public void setCashandCarryFlag(String cashandCarryFlag) {
        this.cashandCarryFlag = cashandCarryFlag;
    }

    public String getCashandCarryFlag() {
        return cashandCarryFlag;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setPartnerAdd(String partnerAdd) {
        this.partnerAdd = partnerAdd;
    }

    public String getPartnerAdd() {
        return partnerAdd;
    }

    public void setPartnerDeliveryrating(Integer partnerDeliveryrating) {
        this.partnerDeliveryrating = partnerDeliveryrating;
    }

    public Integer getPartnerDeliveryrating() {
        return partnerDeliveryrating;
    }

    public void setPartnerContactNum(String partnerContactNum) {
        this.partnerContactNum = partnerContactNum;
    }

    public String getPartnerContactNum() {
        return partnerContactNum;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setSubsidyEligible(String subsidyEligible) {
        this.subsidyEligible = subsidyEligible;
    }

    public String getSubsidyEligible() {
        return subsidyEligible;
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

    public void setPtdStatus(String ptdStatus) {
        this.ptdStatus = ptdStatus;
    }

    public String getPtdStatus() {
        return ptdStatus;
    }

    public void setAadhaarLinkedtoLPG(String aadhaarLinkedtoLPG) {
        this.aadhaarLinkedtoLPG = aadhaarLinkedtoLPG;
    }

    public String getAadhaarLinkedtoLPG() {
        return aadhaarLinkedtoLPG;
    }

    public void setRelationshipUCMId(String relationshipUCMId) {
        this.relationshipUCMId = relationshipUCMId;
    }

    public String getRelationshipUCMId() {
        return relationshipUCMId;
    }
}
