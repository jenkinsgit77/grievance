package com.iocl.lpg.customer.bean.loyalty;

import com.iocl.lpg.customer.bean.greivfeedback.GrievFeedback;
import com.iocl.lpg.customer.bean.subscriptionvoucher.SubscriptionVouch;

import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;

import oracle.adf.view.rich.component.rich.output.RichOutputFormatted;

import oracle.binding.OperationBinding;
import com.iocl.lpg.customer.utils.CommonHelper;

import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.Serializable;

import java.util.HashMap;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.domain.Date;

import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class LoyaltyEnrichment implements Serializable{
    private RichSelectOneChoice genderBinding;
    private RichInputDate dateOfBirthBinding;
    private RichInputText address1Binding;
    private RichInputText mobileNoBinding;
    private RichInputText pincodeBinding;
    private RichSelectOneChoice stateBinding;
    private RichSelectOneChoice districtBinding;
    private RichSelectOneChoice prefixBinding;
    private RichInputText firstNameBinding;
    private Boolean errorInEnrichment;
    private RichInputText emailIdBinding;
    private RichInputText aadharNoBinding;
    private RichSelectBooleanCheckbox agreementbinding;
    private RichInputText cityBinding;
    private RichPopup reloginPopUpBinding;

    public LoyaltyEnrichment() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(LoyaltyEnrichment.class);

        } else {
            log = Logger.getLogger(LoyaltyEnrichment.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    
    
    private static Logger log;
    java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
    java.util.Map sessionScopeParam = ADFContext.getCurrent().getSessionScope();
    private String prefixErrorMsg;
    private String firstNameErrorMsg;
    private String genderErrorMsg;
    private String dateOfBirthErrorMsg;
    private String address1ErrorMsg;
    private String stateErrorMsg;
    private String districtErrorMsg;
    private String cityErrorMsg;
    private String pincodeErrorMsg;
    private String mobileErrorMsg;
    private String emailIdErrorMsg;
    private String aadharNoErrorMsg;
    private String agreementErrorMsg;
    
    public String onloadProfileMethod()
    {
        String retString = "next";
        log.info("Inside onloadProfileMethod start");
        sessionScopeParam.put("sLoyaltyAadharNumber","");
        OperationBinding op = CommonHelper.findOperation("insertVehicleTypeRecord");
        op.getParamsMap().put("count", "0");
        op.execute();  
               
        populateIdamfields();
        log.info("Inside onloadProfileMethod end");
        return retString;
    }

    public void populateIdamfields() {
        OperationBinding ProfileOp = CommonHelper.findOperation("insertProfileEnrichInfo");


//        ProfileOp.getParamsMap().put("FirstName","Test");
//        ProfileOp.getParamsMap().put("LastName","Again");
//        ProfileOp.getParamsMap().put("MobileNo","8782212345");
        
        ProfileOp.getParamsMap()
            .put("FirstName", String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserFirstName}")));
        ProfileOp.getParamsMap()
            .put("LastName", String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserLastName}")));
        ProfileOp.getParamsMap()
            .put("MobileNo", String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserMobileNo}")));
        ProfileOp.getParamsMap()
            .put("EmailId", String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserEmailId}")));
        ProfileOp.execute();

    }

    public void insertVehicleRecordActionLis(ActionEvent actionEvent) {
        // Add event code here...
        log.info("Inside insertVehicleRecordActionLis start");
        OperationBinding op = CommonHelper.findOperation("insertVehicleTypeRecord");
        op.getParamsMap().put("count", "1");
        op.execute();  
        log.info("Inside insertVehicleRecordActionLis end");
    }

    public String YesButtonAction() {
        // Add event code here...
        String retString = "goToConfirmation";

        if (sessionScopeParam.get("loyaltyFlow") != null && sessionScopeParam.get("loyaltyFlow").equals("Enrichment")) {
            retString = enrichmentYesOps();
        } else if (sessionScopeParam.get("loyaltyFlow") != null &&
                   sessionScopeParam.get("loyaltyFlow").equals("Enrollment")) {
            retString = enrollmentYesOps();
        }
        /**----------------Setting Confirmation Message for Confirmation Page--------------**/
        pageflowParam.put("confMessage",
                          CommonHelper.getValueFromRsBundle("THANK_YOU_MESSAGE_XTRA_REWARDS_DETAILS"));
        
        /**-------Below Code Shows confirmation message in Pop Up and 
         * forced user to ReLogin-----------------**/
        pageflowParam.put("pConfirmMessage", CommonHelper.getValueFromRsBundle("THANK_YOU_MESSAGE_WITH_LOG_OUT"));
        if(retString.equalsIgnoreCase("goToConfirmation"))
        {
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            reloginPopUpBinding.show(hints);
        }
        return retString;
    }
    
    
    public String enrichmentYesOps()
    {
        String retString = "goToConfirmation";
        
        JSONObject jsonInput = new JSONObject();
        java.util.List lst = new java.util.ArrayList();
        
        jsonInput.put(EPICConstant.XR_UCMID,sessionScopeParam.get(EPICConstant.XR_UCMID));
        jsonInput.put(EPICConstant.LPG_UCMID,sessionScopeParam.get(EPICConstant.LPG_UCMID));
        jsonInput.put("IDAMUserKey",CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}"));
        jsonInput.put("EnrichmentFlag", "N");
        jsonInput.put("DuplicationFlag",sessionScopeParam.get("duplicationFlag"));
        jsonInput.put("CRMId",sessionScopeParam.get("CRMId"));
        
        lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("PROFILE_ENRICHMENT"));
        lst.add(1, jsonInput);

        OperationBinding opRC = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
        opRC.getParamsMap().put("inputList", lst);
        opRC.getParamsMap().put("method", EPICConstant.PROFILE_ENRICHMENT);
        opRC.execute();
        JSONObject jsonResponse = new JSONObject();
        java.util.List ReturnList = (java.util.List) opRC.getResult();
        if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                              .toString()
                                                                              .equalsIgnoreCase("true"))) {
            log.info("ReturnList Value at 0 index is " + ReturnList.get(0));
            jsonResponse = new JSONObject(ReturnList.get(1).toString());
            
        } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                   (ReturnList.get(0).toString().equalsIgnoreCase("false") &&
                    ReturnList.size() > 1)) {
            jsonResponse = new JSONObject(ReturnList.get(1).toString());
            pageflowParam.put("pValidCodeEnrichDetails",
                              jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                              jsonResponse.getString(EPICConstant.ERROR_CODE));
            pageflowParam.put("pValidMsgEnrichDetails",
                              jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                              jsonResponse.getString(EPICConstant.ERROR_MESSAGE));
            if (pageflowParam.get("pValidCodeEnrichDetails") != null && pageflowParam.get("pValidCodeEnrichDetails")
                                                                             .toString()
                                                                             .equalsIgnoreCase(EPICConstant.SBL100)) {
                log.info("Business Validation Issue SBL-100 ErrorCode Received");
                return null;
            } else {
                log.info("Some Error in WebService Response");
                retString = "ERROR";
                return retString;
            }
        } else {
            log.info("Some Error in WebService Response");
            retString = "ERROR";
            return retString;
        }
        /**----------------Setting Confirmation Message for Confirmation Page--------------**/
        pageflowParam.put("confMessage",CommonHelper.getValueFromRsBundle("THANK_YOU_MESSAGE_XTRA_REWARDS_DETAILS"));
        
        return retString;
    }
    
    public String enrollmentYesOps(){
            String retString = "goToConfirmation";
            JSONObject jsonInput = new JSONObject(sessionScopeParam.get("sEnrollmentJsonInput").toString());
            java.util.List lst = new java.util.ArrayList();
            
            jsonInput.put("DupUCMId",sessionScopeParam.get("sDupUCMId"));
            jsonInput.put("DataDuplicationCase","Y"); // Custom Attribute to identify whether "PROFILE_ENROLLMENT" Service is called 2nd time or not
            
            log.info("sMemberNumber: "+sessionScopeParam.get("sMemberNumber"));
            if (sessionScopeParam.get("sMemberNumber") != null) {
//                log.info("MemberNumber Attribute is not null. So User will directly move to Confirmation Page");
//                return retString;
                jsonInput.put("RequestType", "DuplicateMemberEnrollment");
            } else {
                jsonInput.put("RequestType", "Enrollment");
            }
            jsonInput.put("EnrollmentFlag","N");
                            
            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("PROFILE_ENROLLMENT"));
            lst.add(1, jsonInput);

            OperationBinding opRC = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            opRC.getParamsMap().put("inputList", lst);
            opRC.getParamsMap().put("method", EPICConstant.PROFILE_ENROLLMENT);
            opRC.execute();
            JSONObject jsonResponse = new JSONObject();
            java.util.List ReturnList = (java.util.List) opRC.getResult();
            if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                                  .toString()
                                                                                  .equalsIgnoreCase("true"))) {
                jsonResponse = new JSONObject(ReturnList.get(1).toString());
                /**----------------Setting Confirmation Message for Confirmation Page--------------**/
                pageflowParam.put("confMessage",
                                  CommonHelper.getValueFromRsBundle("THANK_YOU_MESSAGE_XTRA_REWARDS_DETAILS"));
                
            } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                       (ReturnList.get(0).toString().equalsIgnoreCase("false") &&
                        ReturnList.size() > 1)) {
                jsonResponse = new JSONObject(ReturnList.get(1).toString());

                pageflowParam.put("pValidCodeEnrichDetails",
                                  jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_CODE));
                pageflowParam.put("pValidMsgEnrichDetails",
                                  jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


                if (pageflowParam.get("pValidCodeEnrichDetails") != null &&
                    pageflowParam.get("pValidCodeEnrichDetails").toString().equalsIgnoreCase(EPICConstant.SBL100)) {
                    log.info("Business Validation Issue SBL-100 ErrorCode Received");
                    return null;
                } else {
                    log.info("Some Error in WebService Response");
                    retString = "ERROR";
                    return retString;
                }
            } else {
                log.info("Some Error in WebService Response");
                retString = "ERROR";
                return retString;
            }
            return retString;
        }
    
    public String NoButtonAction() {
        // Add event code here...
        String retString = "NewRecord";
        log.info("Inside NoButtonActionLis Start");
        log.info("loyaltyFlow: "+sessionScopeParam.get("loyaltyFlow"));
        if (sessionScopeParam.get("loyaltyFlow") != null && sessionScopeParam.get("loyaltyFlow").equals("Enrichment")) {
            log.info("navigateTo Parameter Val in NoButtonAction " + pageflowParam.get("navigateTo"));
            return "NewRecord";
        }else if(sessionScopeParam.get("loyaltyFlow") != null && sessionScopeParam.get("loyaltyFlow").equals("Enrollment")){
            retString = "goToConfirmation";
            
            JSONObject jsonInput = new JSONObject(sessionScopeParam.get("sEnrollmentJsonInput").toString());
            java.util.List lst = new java.util.ArrayList();
            
            jsonInput.put("DupUCMId",sessionScopeParam.get("sDupUCMId"));
            jsonInput.put("DataDuplicationCase","Y"); // Custom Attribute to identify whether "PROFILE_ENROLLMENT" Service is called 2nd time or not
            
            log.info("sMemberNumber: "+sessionScopeParam.get("sMemberNumber"));
            if (sessionScopeParam.get("sMemberNumber") != null) {
                jsonInput.put("RequestType", "DuplicateMemberEnrollment");
            } else {
                jsonInput.put("RequestType", "Enrollment");
            }
            jsonInput.put("EnrollmentFlag","Y");
                            
            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("PROFILE_ENROLLMENT"));
            lst.add(1, jsonInput);

            OperationBinding opRC = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            opRC.getParamsMap().put("inputList", lst);
            opRC.getParamsMap().put("method", EPICConstant.PROFILE_ENROLLMENT);
            opRC.execute();
            JSONObject jsonResponse = new JSONObject();
            java.util.List ReturnList = (java.util.List) opRC.getResult();
            if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                                  .toString()
                                                                                  .equalsIgnoreCase("true"))) {
                jsonResponse = new JSONObject(ReturnList.get(1).toString());
                /**----------------Setting Confirmation Message for Confirmation Page--------------**/
                pageflowParam.put("confMessage",
                                  CommonHelper.getValueFromRsBundle("THANK_YOU_MESSAGE_XTRA_REWARDS_DETAILS"));
                
            } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                       (ReturnList.get(0).toString().equalsIgnoreCase("false") &&
                        ReturnList.size() > 1)) {
                jsonResponse = new JSONObject(ReturnList.get(1).toString());

                pageflowParam.put("pValidCodeEnrichDetails",
                                  jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_CODE));
                pageflowParam.put("pValidMsgEnrichDetails",
                                  jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


                if (pageflowParam.get("pValidCodeEnrichDetails") != null &&
                    pageflowParam.get("pValidCodeEnrichDetails").toString().equalsIgnoreCase(EPICConstant.SBL100)) {
                    log.info("Business Validation Issue SBL-100 ErrorCode Received");
                    return null;
                } else {
                    log.info("Some Error in WebService Response");
                    retString = "ERROR";
                    return retString;
                }
            } else {
                log.info("Some Error in WebService Response");
                retString = "ERROR";
                return retString;
            }
            /**-------Below Code Shows confirmation message in Pop Up and
                     * forced user to ReLogin-----------------**/
            pageflowParam.put("pConfirmMessage", CommonHelper.getValueFromRsBundle("THANK_YOU_MESSAGE_WITH_LOG_OUT"));
            if (retString.equalsIgnoreCase("goToConfirmation")) {
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                reloginPopUpBinding.show(hints);
            }
        }
        
        return retString;
    }
    
    public Boolean validateEnrichment()
    {
        errorInEnrichment = false;
        if(CustomerValidation.isNull(prefixBinding.getValue()))
        {
            prefixErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_PREFIX");
            prefixBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;
        }
        if(CustomerValidation.isNull(firstNameBinding.getValue()))
        {
            firstNameErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_FIRST_NAME");
            firstNameBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;
        }
        if(CustomerValidation.isNull(genderBinding.getValue()))
        {
            genderErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_GENDER");
            genderBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;
        }
        
        if(CustomerValidation.isNull(dateOfBirthBinding.getValue()))
        {
            dateOfBirthErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_DATE_OF_BIRTH");
            dateOfBirthBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;
        }
        if(CustomerValidation.isNull(address1Binding.getValue()))
        {
            address1ErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_ADDRESS1");
            address1Binding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;
        }
//        if(CustomerValidation.isNull(stateBinding.getValue()))
//        {
//            stateErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_STATE");
//            stateBinding.setStyleClass(EPICConstant.ERROR_CLASS);
//            errorInEnrichment = true;
//        }
//        
//        if(CustomerValidation.isNull(districtBinding.getValue()))
//        {
//            districtErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_DISTRICT");
//            districtBinding.setStyleClass(EPICConstant.ERROR_CLASS);
//            errorInEnrichment = true;
//        }
        
        if (CustomerValidation.isNull(pincodeBinding.getValue())) {
            pincodeErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_PINCODE");
            pincodeBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;
        } else if (pageflowParam.get("ValidPinCode") != null && pageflowParam.get("ValidPinCode").equals("N")) {
            pincodeErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_VALID_PINCODE");
            pincodeBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;

        }
        
        if(CustomerValidation.isNull(cityBinding.getValue()))
        {
            cityErrorMsg = CommonHelper.getValueFromRsBundle("ENTER__LOYALTY_CITY");
            cityBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;
        }
        if(CustomerValidation.isNull(agreementbinding.getValue()) || agreementbinding.getValue().equals(false))
        {
            agreementErrorMsg = CommonHelper.getValueFromRsBundle("ACCEPT_TERMS_CONDITIONS");
            agreementbinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;
        }
        if(CustomerValidation.isNull(mobileNoBinding.getValue()))
        {
            mobileErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_MOBILE_NO");
            mobileNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;
        }else if (CustomerValidation.isValidOrNullVal(mobileNoBinding.getValue(), EPICConstant.MOBILE_EXPRESSION) ==
            EPICConstant.INVALID_CASE) {
            mobileErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_VALID_MOBILE_NO");
            mobileNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInEnrichment = true;
        }
        
        if (!CustomerValidation.isNull(emailIdBinding.getValue())) {
            if (CustomerValidation.isValidOrNullVal(emailIdBinding.getValue(), EPICConstant.EMAIL_EXPRESSSION) ==
                EPICConstant.INVALID_CASE) {
                emailIdErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_VALID_EMAIL_ID");
                emailIdBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                errorInEnrichment = true;
            }
        }
        
        if (!CustomerValidation.isNull(aadharNoBinding.getValue())) {
            if (!CustomerValidation.validateAadhaarNumber(aadharNoBinding.getValue().toString())) {
                aadharNoErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_VALID_AADHAR_NO");
                aadharNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                errorInEnrichment = true;
            }
        }
        
        return errorInEnrichment;
    }
    
    public void resetValidateEnrichment()
    {
        prefixErrorMsg = null;
        firstNameErrorMsg = null;
        genderErrorMsg = null;
        dateOfBirthErrorMsg = null;
        address1ErrorMsg = null;
        stateErrorMsg = null;
        districtErrorMsg = null;
        pincodeErrorMsg = null;
        cityErrorMsg = null;
        mobileErrorMsg = null;
        emailIdErrorMsg = null;
        aadharNoErrorMsg = null;
            
        prefixBinding.setStyleClass("");
        firstNameBinding.setStyleClass("");
        genderBinding.setStyleClass("");
        dateOfBirthBinding.setStyleClass("");
        address1Binding.setStyleClass("");
        stateBinding.setStyleClass("");
        districtBinding.setStyleClass("");
        pincodeBinding.setStyleClass("");
        cityBinding.setStyleClass("");
        mobileNoBinding.setStyleClass("");
        emailIdBinding.setStyleClass("");
        aadharNoBinding.setStyleClass("");
        
    }
    
    public String linkingSubmitAction() {
        // Add event code here...
        String retString = null;
        
        JSONObject jsonInput = new JSONObject();
        java.util.List lst = new java.util.ArrayList();
        
        lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("FETCH_XR_RECORD"));
        lst.add(1, jsonInput);

        OperationBinding opRC = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
        opRC.getParamsMap().put("inputList", lst);
        opRC.getParamsMap().put("method", EPICConstant.FETCH_XR_RECORD);
        opRC.execute();

        JSONObject jsonResponse = new JSONObject();
        java.util.List ReturnList = (java.util.List) opRC.getResult();
        if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                              .toString()
                                                                              .equalsIgnoreCase("true"))) {
            log.info("ReturnList Value at 0 index is " + ReturnList.get(0));
            jsonResponse = new JSONObject(ReturnList.get(1).toString());
            pageflowParam.put("DuplicationFlag", jsonResponse.get("DuplicateFound")); // Setting Service Response in DuplicateFound Pageflow Scope Parameter
            pageflowParam.put("XRUCMId", jsonResponse.get("XRUCMId"));
            pageflowParam.put("LPGUCMId", jsonResponse.get("LPGUCMId"));
            
        } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                   (ReturnList.get(0).toString().equalsIgnoreCase("false") &&
                    ReturnList.get(1) != null)) {
            jsonResponse = new JSONObject(ReturnList.get(1).toString());

            pageflowParam.put("pValidCodeFetchXR",
                              jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                              jsonResponse.getString(EPICConstant.ERROR_CODE));
            pageflowParam.put("pValidMsgFetchXR",
                              jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                              jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


            if (pageflowParam.get("pValidCodeFetchXR") != null && pageflowParam.get("pValidCodeFetchXR")
                                                                             .toString()
                                                                             .equalsIgnoreCase(EPICConstant.SBL100)) {
                log.info("Business Validation Issue SBL-100 ErrorCode Received");
                return null;
            } else {
                log.info("Some Error in WebService Response");
                retString = "ERROR";
                return retString;
            }
        } else {
            log.info("Some Error in WebService Response");
            retString = "ERROR";
            return retString;
        }

        if (pageflowParam.get("DuplicationFlag")
                         .toString()
                         .equalsIgnoreCase("Y")) {
            retString = "recordExist";
        } else if (pageflowParam.get("DuplicationFlag")
                                .toString()
                                .equalsIgnoreCase("N")) {
            log.info("Setting EnrichmentFlag to Y");
            pageflowParam.put("EnrichmentFlag","Y");
            retString = "recordNotExist";
        }
        return retString;
    }

    public String enrichmentSubmitAction() {
        // Add event code here...
        String retString = "goToConfirmation";
        resetValidateEnrichment();

        errorInEnrichment = validateEnrichment();

        if (errorInEnrichment) {
            return null;
        }
//        /**--------------Temporary Code---------------**/
//        sessionScopeParam.put("loyaltyFlow", "Enrollment");
//        /**-------------------End Here----------------**/
        log.info("loyaltyFlow in enrichmentSubmitAction: "+sessionScopeParam.get("loyaltyFlow"));
        if (sessionScopeParam.get("loyaltyFlow") != null && sessionScopeParam.get("loyaltyFlow").equals("Enrichment")) {
            retString = enrichmentProfile();
        }else if(sessionScopeParam.get("loyaltyFlow") != null && sessionScopeParam.get("loyaltyFlow").equals("Enrollment")) {
            retString = enrollmentProfile();
        }
        
        /**-------Below Code Shows confirmation message in Pop Up and 
         * forced user to ReLogin-----------------**/
        pageflowParam.put("pConfirmMessage", CommonHelper.getValueFromRsBundle("THANK_YOU_MESSAGE_WITH_LOG_OUT"));
        if(retString != null && retString.equalsIgnoreCase("goToConfirmation"))
        {
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            reloginPopUpBinding.show(hints);
        }
        return retString;
    }


    public JSONObject vehicleDetails(JSONObject jsonObj) {
        /**------------------Code to Get Vehicles Details Start-----------------**/
        JSONArray vehicleArrayObj = new JSONArray();

        DCIteratorBinding iter = CommonHelper.findIterator("VehicleDetailsDbVo1Iterator");
        ViewObject vo = iter.getViewObject();
        Row[] row = vo.getAllRowsInRange();
        for (int i = 0; i < row.length; i++) {

            JSONObject jsonVehicle = new JSONObject();
            jsonVehicle.put("VehicleType", row[i].getAttribute("VehicleType"));
            jsonVehicle.put("FuelType", row[i].getAttribute("FuelType"));
            jsonVehicle.put("RegistrationNo", row[i].getAttribute("RegistrationNo"));
            log.info("length" + jsonVehicle.length());
            if (jsonVehicle.length() > 0) {
                vehicleArrayObj.put(jsonVehicle);
            }

        }
        /**------------------Code to Get Vehicles Details End-----------------**/
        log.info("vehicleArrayObj " + vehicleArrayObj);
        if (vehicleArrayObj.length() > 0) {
            jsonObj.put("VehicleDetails", vehicleArrayObj);
        }
        return jsonObj;
    }
    
    public String enrichmentProfile() {
        String retString = "goToConfirmation";
        JSONObject jsonInput = new JSONObject();
        java.util.List lst = new java.util.ArrayList();
        //        jsonInput.put("XRUCMId","PR200000000009004");
        //        jsonInput.put("LPGUCMId","PR000000164010590");
        //        jsonInput.put("CRMId","200000000106");
        //        jsonInput.put("IDAMUserKey","518005");

        jsonInput.put("XRUCMId", sessionScopeParam.get("XRUCMId"));
        jsonInput.put("LPGUCMId", sessionScopeParam.get("LPGUCMId"));
        jsonInput.put("CRMId", sessionScopeParam.get("CRMId"));
        jsonInput.put("IDAMUserKey", CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}"));
        jsonInput.put("EnrichmentFlag", "Y");

        log.info("sessionScopeParam DuplicationFlag in enrichmentSubmitAction" +
                 sessionScopeParam.get("duplicationFlag"));
        jsonInput.put("DuplicationFlag", sessionScopeParam.get("duplicationFlag"));
        
        /**---------------Storing Some field values(Aadhar) in Case of Error-----------**/
        pageflowParam.put("pAadharNumber",aadharNoBinding.getValue());
        /**---------------Storing Some field values(Aadhar) in Case of Error-----------**/

        jsonInput = vehicleDetails(jsonInput);
    
        lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("PROFILE_ENRICHMENT"));
        lst.add(1, jsonInput);

        OperationBinding opRC = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
        opRC.getParamsMap().put("inputList", lst);
        opRC.getParamsMap().put("method", EPICConstant.PROFILE_ENRICHMENT);
        opRC.execute();
        JSONObject jsonResponse = new JSONObject();
        java.util.List ReturnList = (java.util.List) opRC.getResult();
        
        
        /**---------------Setting Some field values in Case of Error-----------**/
        aadharNoBinding.setValue(pageflowParam.get("pAadharNumber"));
        /**-----------------------------End Here-------------------------------**/
        
        if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                              .toString()
                                                                              .equalsIgnoreCase("true"))) {
            log.info("ReturnList Value at 0 index is " + ReturnList.get(0));
            jsonResponse = new JSONObject(ReturnList.get(1).toString());

        } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                   (ReturnList.get(0).toString().equalsIgnoreCase("false") &&
                    ReturnList.size() > 1)) {
            jsonResponse = new JSONObject(ReturnList.get(1).toString());

            pageflowParam.put("pValidCodeEnrichNo",
                              jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                              jsonResponse.getString(EPICConstant.ERROR_CODE));
            pageflowParam.put("pValidMsgEnrichNo",
                              jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                              jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


            if (pageflowParam.get("pValidCodeEnrichNo") != null &&
                pageflowParam.get("pValidCodeEnrichNo").toString().equalsIgnoreCase(EPICConstant.SBL100)) {
                log.info("Business Validation Issue SBL-100 ErrorCode Received");
                return null;
            } else {
                log.info("Some Error in WebService Response");
                retString = "ERROR";
                return retString;
            }
        } else {
            log.info("Some Error in WebService Response");
            retString = "ERROR";
            return retString;
        }

        /**----------------Setting Confirmation Message for Confirmation Page--------------**/
        pageflowParam.put("confMessage", CommonHelper.getValueFromRsBundle("THANK_YOU_MESSAGE_XTRA_REWARDS_DETAILS"));

        return retString;

    }

    public String enrollmentProfile() {
        String retString = "goToConfirmation";
        String enrollmentCase = null;
        JSONObject jsonInput = new JSONObject();
        java.util.List lst = new java.util.ArrayList();
        
        if(CommonHelper.evaluateEL("#{sessionScope.userDetails.hasBlankUCMId}") != null && CommonHelper.evaluateEL("#{sessionScope.userDetails.hasBlankUCMId}").equals("Y"))
        {
            log.info("Loyalty Enrollment - Blank UCMId Case Executed");
            enrollmentCase = "blankUcmIdCase";
            
//          jsonInput.put("IDAMUserKey","805064");
            jsonInput.put("RequestType","EnrollNewProfile");
            jsonInput.put("DataDuplicationCase","N");
            jsonInput.put("IDAMUserKey",CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}"));
            
        }
        else if(CommonHelper.evaluateEL("#{sessionScope.userDetails.ucmID}") != null)
        {
            log.info("Loyalty Enrollment - UCMId Case Executed");
            enrollmentCase = "UCMIdCase";
            
            jsonInput.put("DupUCMId",CommonHelper.evaluateEL("#{sessionScope.userDetails.ucmID}"));
            jsonInput.put("RequestType","Enrollment");
            jsonInput.put("IDAMUserKey",CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}"));
            jsonInput.put("EnrollmentFlag","N");
            jsonInput.put("DataDuplicationCase","N");
            
        }
        
        jsonInput = vehicleDetails(jsonInput);
        /**---------------Storing Some field values(Aadhar) in Case of Error-----------**/
        pageflowParam.put("pAadharNumber",aadharNoBinding.getValue());
        /**---------------Storing Some field values(Aadhar) in Case of Error-----------**/
        
        lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("PROFILE_ENROLLMENT"));
        lst.add(1, jsonInput);

        OperationBinding opRC = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
        opRC.getParamsMap().put("inputList", lst);
        opRC.getParamsMap().put("method", EPICConstant.PROFILE_ENROLLMENT);
        opRC.execute();
        
        /**---------------Setting Some field values in Case of Error-----------**/
        aadharNoBinding.setValue(pageflowParam.get("pAadharNumber"));
        /**-----------------------------End Here-------------------------------**/
        JSONObject jsonResponse = new JSONObject();
        java.util.List ReturnList = (java.util.List) opRC.getResult();
        if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                              .toString()
                                                                              .equalsIgnoreCase("true"))) {
            jsonResponse = new JSONObject(ReturnList.get(1).toString());
            /**----------------Setting Confirmation Message for Confirmation Page--------------**/
            pageflowParam.put("confMessage",
                              CommonHelper.getValueFromRsBundle("THANK_YOU_MESSAGE_XTRA_REWARDS_DETAILS"));
        } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                   (ReturnList.get(0).toString().equalsIgnoreCase("false") &&
                    ReturnList.size() > 1)) {
            jsonResponse = new JSONObject(ReturnList.get(1).toString());
            
            /**---------------Case to Check for Duplication Profile-------------**/
            if (jsonResponse.get("ErrorSubCode") != null && (jsonResponse.get("ErrorSubCode").equals("402") || jsonResponse.get("ErrorSubCode").equals("403"))){
                sessionScopeParam.put("sEnrollmentJsonInput", ReturnList.get(2));
                
                JSONObject duplicateJson =  jsonResponse.isNull("DuplicateProfileData") ? null : jsonResponse.getJSONObject("DuplicateProfileData");
                sessionScopeParam.put("sMemberNumber",
                                      duplicateJson.isNull("MemberNumber") ? null : duplicateJson.get("MemberNumber"));
                sessionScopeParam.put("sDupUCMId",
                                      duplicateJson.isNull("DupUCMId") ? null : duplicateJson.get("DupUCMId"));
                retString = "goToProfileDetails";
                return retString;
            }
            /**-----------------------------End Here----------------------------**/
            pageflowParam.put("pValidCodeEnrichNo",
                              jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                              jsonResponse.getString(EPICConstant.ERROR_CODE));
            pageflowParam.put("pValidMsgEnrichNo",
                              jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                              jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


            if (pageflowParam.get("pValidCodeEnrichNo") != null &&
                pageflowParam.get("pValidCodeEnrichNo").toString().equalsIgnoreCase(EPICConstant.SBL100)) {
                log.info("Business Validation Issue SBL-100 ErrorCode Received");
                return null;
            } else {
                log.info("Some Error in WebService Response");
                retString = "ERROR";
                return retString;
            }
        } else {
            log.info("Some Error in WebService Response");
            retString = "ERROR";
            return retString;
        }
        
        return retString;
    }
    public void resetActionListener(ActionEvent actionEvent) {
        // Add event code here...
        agreementbinding.setValue(false);
        resetValidateEnrichment();
        onloadProfileMethod();
    }
    
    public boolean dobEligible(String dobString) {
        log.info("dobString"+dobString);
        Date doB = CommonHelper.dateCompToJBODate(dobString,String.valueOf(3));
        int age = CommonHelper.diffInYear(doB);
        log.info("age " + age);
        if (age < 18) {
            dateOfBirthErrorMsg = CommonHelper.getValueFromRsBundle("AGE_MUST_BE_GREATE_OR_EQUAL_EGHTEEN");
            dateOfBirthBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            return false;
        } else {
            dateOfBirthErrorMsg = "";
            dateOfBirthBinding.setStyleClass("");
            return true;
        }
    }
    public void dobValueChangeLis(ValueChangeEvent vc) {
        // Add event code here...
        if (vc.getNewValue() != null) {
            String dobString = vc.getNewValue().toString();
            if (!dobEligible(dobString)) {
                dateOfBirthErrorMsg = CommonHelper.getValueFromRsBundle("AGE_MUST_BE_GREATE_OR_EQUAL_EGHTEEN");
                dateOfBirthBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            }
        }
        
    }
    
    
    public void setGenderBinding(RichSelectOneChoice genderBinding) {
        this.genderBinding = genderBinding;
    }

    public RichSelectOneChoice getGenderBinding() {
        return genderBinding;
    }

    public void setDateOfBirthBinding(RichInputDate dateOfBirthBinding) {
        this.dateOfBirthBinding = dateOfBirthBinding;
    }

    public RichInputDate getDateOfBirthBinding() {
        return dateOfBirthBinding;
    }

    public void setAddress1Binding(RichInputText address1Binding) {
        this.address1Binding = address1Binding;
    }

    public RichInputText getAddress1Binding() {
        return address1Binding;
    }

    public void setMobileNoBinding(RichInputText mobileNoBinding) {
        this.mobileNoBinding = mobileNoBinding;
    }

    public RichInputText getMobileNoBinding() {
        return mobileNoBinding;
    }

    public void setPincodeBinding(RichInputText pincodeBinding) {
        this.pincodeBinding = pincodeBinding;
    }

    public RichInputText getPincodeBinding() {
        return pincodeBinding;
    }

    public void setStateBinding(RichSelectOneChoice stateBinding) {
        this.stateBinding = stateBinding;
    }

    public RichSelectOneChoice getStateBinding() {
        return stateBinding;
    }

    public void setDistrictBinding(RichSelectOneChoice districtBinding) {
        this.districtBinding = districtBinding;
    }

    public RichSelectOneChoice getDistrictBinding() {
        return districtBinding;
    }

    public void setPrefixBinding(RichSelectOneChoice prefixBinding) {
        this.prefixBinding = prefixBinding;
    }

    public RichSelectOneChoice getPrefixBinding() {
        return prefixBinding;
    }

    public void setFirstNameBinding(RichInputText firstNameBinding) {
        this.firstNameBinding = firstNameBinding;
    }

    public RichInputText getFirstNameBinding() {
        return firstNameBinding;
    }

    public void setErrorInEnrichment(Boolean errorInEnrichment) {
        this.errorInEnrichment = errorInEnrichment;
    }

    public Boolean getErrorInEnrichment() {
        return errorInEnrichment;
    }

    public void setEmailIdBinding(RichInputText emailIdBinding) {
        this.emailIdBinding = emailIdBinding;
    }

    public RichInputText getEmailIdBinding() {
        return emailIdBinding;
    }

    public void setAadharNoBinding(RichInputText aadharNoBinding) {
        this.aadharNoBinding = aadharNoBinding;
    }

    public RichInputText getAadharNoBinding() {
        return aadharNoBinding;
    }

    public void setPrefixErrorMsg(String prefixErrorMsg) {
        this.prefixErrorMsg = prefixErrorMsg;
    }

    public String getPrefixErrorMsg() {
        return prefixErrorMsg;
    }

    public void setFirstNameErrorMsg(String firstNameErrorMsg) {
        this.firstNameErrorMsg = firstNameErrorMsg;
    }

    public String getFirstNameErrorMsg() {
        return firstNameErrorMsg;
    }

    public void setGenderErrorMsg(String genderErrorMsg) {
        this.genderErrorMsg = genderErrorMsg;
    }

    public String getGenderErrorMsg() {
        return genderErrorMsg;
    }

    public void setDateOfBirthErrorMsg(String dateOfBirthErrorMsg) {
        this.dateOfBirthErrorMsg = dateOfBirthErrorMsg;
    }

    public String getDateOfBirthErrorMsg() {
        return dateOfBirthErrorMsg;
    }

    public void setAddress1ErrorMsg(String address1ErrorMsg) {
        this.address1ErrorMsg = address1ErrorMsg;
    }

    public String getAddress1ErrorMsg() {
        return address1ErrorMsg;
    }

    public void setStateErrorMsg(String stateErrorMsg) {
        this.stateErrorMsg = stateErrorMsg;
    }

    public String getStateErrorMsg() {
        return stateErrorMsg;
    }

    public void setDistrictErrorMsg(String districtErrorMsg) {
        this.districtErrorMsg = districtErrorMsg;
    }

    public String getDistrictErrorMsg() {
        return districtErrorMsg;
    }

    public void setPincodeErrorMsg(String pincodeErrorMsg) {
        this.pincodeErrorMsg = pincodeErrorMsg;
    }

    public String getPincodeErrorMsg() {
        return pincodeErrorMsg;
    }

    public void setMobileErrorMsg(String mobileErrorMsg) {
        this.mobileErrorMsg = mobileErrorMsg;
    }

    public String getMobileErrorMsg() {
        return mobileErrorMsg;
    }

    public void setEmailIdErrorMsg(String emailIdErrorMsg) {
        this.emailIdErrorMsg = emailIdErrorMsg;
    }

    public String getEmailIdErrorMsg() {
        return emailIdErrorMsg;
    }

    public void setAadharNoErrorMsg(String aadharNoErrorMsg) {
        this.aadharNoErrorMsg = aadharNoErrorMsg;
    }

    public String getAadharNoErrorMsg() {
        return aadharNoErrorMsg;
    }


    public void setAgreementbinding(RichSelectBooleanCheckbox agreementbinding) {
        this.agreementbinding = agreementbinding;
    }

    public RichSelectBooleanCheckbox getAgreementbinding() {
        return agreementbinding;
    }

    public void setCityErrorMsg(String cityErrorMsg) {
        this.cityErrorMsg = cityErrorMsg;
    }

    public String getCityErrorMsg() {
        return cityErrorMsg;
    }

    public void setCityBinding(RichInputText cityBinding) {
        this.cityBinding = cityBinding;
    }

    public RichInputText getCityBinding() {
        return cityBinding;
    }

    public void setAgreementErrorMsg(String agreementErrorMsg) {
        this.agreementErrorMsg = agreementErrorMsg;
    }

    public String getAgreementErrorMsg() {
        return agreementErrorMsg;
    }

    public void pincodeValueChangeLis(ValueChangeEvent vc) {
        // Add event code here...
        log.info("Inside pincodeValueChangeLis start");
        if (vc.getNewValue() != null) {
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("ZipCode", String.valueOf(vc.getNewValue()));
            java.util.List lst = new java.util.ArrayList();
            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("LOYALTY_STATE_DISTRICT"));
            lst.add(1, jsonInput);
            OperationBinding op = CommonHelper.findOperation("serviceCustomerCall");
            op.getParamsMap().put("inputList", lst);
            op.getParamsMap().put("method",EPICConstant.LOYALTY_STATE_DISTRICT);
            op.execute();

            java.util.List ReturnList = (java.util.List) op.getResult();
            if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                                  .toString()
                                                                                  .equalsIgnoreCase("true"))) {
                JSONObject jsonResponse = new JSONObject(ReturnList.get(1).toString());
                stateBinding.setValue(jsonResponse.getString("StateCode"));
                districtBinding.setValue(jsonResponse.getString("DistrictCode"));
                pincodeBinding.setStyleClass("");
                pageflowParam.put("ValidPinCode","Y");
            }else
            {
                stateBinding.setValue(null);
                districtBinding.setValue(null);
                pageflowParam.put("ValidPinCode","N");
                pincodeErrorMsg = CommonHelper.getValueFromRsBundle("PINCODE_GOT_TECHNICAL_ERROR");
                pincodeBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                errorInEnrichment = true;   
            }
        }
    }

    public void setReloginPopUpBinding(RichPopup reloginPopUpBinding) {
        this.reloginPopUpBinding = reloginPopUpBinding;
    }

    public RichPopup getReloginPopUpBinding() {
        return reloginPopUpBinding;
    }
}
