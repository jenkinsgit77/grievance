package com.iocl.lpg.customer.bean;

import java.io.Serializable;


public class SRData  implements Serializable{
    @SuppressWarnings("compatibility:7883798147841536751")
    private static final long serialVersionUID = 1L;
    private String mobileNumber;
    private String serviceId;

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
    private String customerId;
    private String emailAddress;
    private String CustomerName;
    private int id;
    private String owner;
    private String distResp;
    private String partnerName;
    private String channel;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setDistResp(String distResp) {
        this.distResp = distResp;
    }

    public String getDistResp() {
        return distResp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    private String Distributor;

    public void setDistributor(String Distributor) {
        this.Distributor = Distributor;
    }

    public String getDistributor() {
        return Distributor;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setComplaintDetails(String complaintDetails) {
        this.complaintDetails = complaintDetails;
    }

    public String getComplaintDetails() {
        return complaintDetails;
    }
    private String serviceStatus;
    private String resolutionRemark;
    private String dateOfClosure;
    private String complaintDate;
    private String complaintLOB;
    private String feedback;
    private String complaintDetails;


    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setResolutionRemark(String resolutionRemark) {
        this.resolutionRemark = resolutionRemark;
    }

    public String getResolutionRemark() {
        return resolutionRemark;
    }

    public void setDateOfClosure(String dateOfClosure) {
        this.dateOfClosure = dateOfClosure;
    }

    public String getDateOfClosure() {
        return dateOfClosure;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintLOB(String complaintLOB) {
        this.complaintLOB = complaintLOB;
    }

    public String getComplaintLOB() {
        return complaintLOB;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerName() {
        return partnerName;
    }
}
