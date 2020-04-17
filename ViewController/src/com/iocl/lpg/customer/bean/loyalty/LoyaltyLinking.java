package com.iocl.lpg.customer.bean.loyalty;

import com.iocl.lpg.customer.bean.subscriptionvoucher.SubscriptionVouch;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.event.PopupCanceledEvent;

import oracle.binding.OperationBinding;

import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONObject;

import org.json.JSONException;

public class LoyaltyLinking implements Serializable{

    private RichInputText xtraRewardNoBinding;
    private RichPopup linkingPopUpBinding;

    public LoyaltyLinking() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(LoyaltyLinking.class);

        } else {
            log = Logger.getLogger(LoyaltyLinking.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    
    private static Logger log;
    java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
    java.util.Map sessionScopeParam = ADFContext.getCurrent().getSessionScope();

    public String linkingSubmitAction() {
        // Add event code here...
        String retString = null;
        /**------------------Resetting the xtra Reward Card No Validation----------**/
        pageflowParam.put("xtraRewardCardErrorMsg",null);
        xtraRewardNoBinding.setStyleClass("");
        /**------------------------------------End Here----------------------------**/
        
        if (CustomerValidation.isNull(xtraRewardNoBinding.getValue())) {
            pageflowParam.put("xtraRewardCardErrorMsg",
                              CommonHelper.getValueFromRsBundle("ENTER_XTRA_REWARD_CARD_NUMBER"));
            xtraRewardNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            return null;
        }
        
        JSONObject jsonInput = new JSONObject();
        java.util.List lst = new java.util.ArrayList();
        
//        jsonInput.put("XRCardNumber","29031900002");
//        jsonInput.put("IDAMUserMobileNo","9161666642");
        jsonInput.put("XRCardNumber",xtraRewardNoBinding.getValue().toString());
        jsonInput.put("IDAMUserMobileNo",CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserMobileNo}"));
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
            /**-------------------Setting Session Parameter to null----------------**/
            sessionScopeParam.put("duplicationFlag",null); // Setting Service Response in DuplicateFound Pageflow Scope Parameter
            sessionScopeParam.put("XRUCMId",null);
            sessionScopeParam.put("LPGUCMId",null);
            sessionScopeParam.put("CRMId",null);
            sessionScopeParam.put("navigateTo",null);
            /**-------------------------------End Here-----------------------------**/
            jsonResponse = new JSONObject(ReturnList.get(1).toString());
            log.info("XRUCMId Set in linkingSubmitAction "+pageflowParam.get("XRUCMId"));
            log.info("LPGUCMId Set in linkingSubmitAction "+pageflowParam.get("LPGUCMId"));
            log.info("------------Setting Attribute in Sessionscope as well------------");
            sessionScopeParam.put("duplicationFlag",jsonResponse.isNull("DuplicateFound") ? "" : jsonResponse.get("DuplicateFound")); // Setting Service Response in DuplicateFound Pageflow Scope Parameter
            sessionScopeParam.put("XRUCMId", jsonResponse.isNull("XRUCMId") ? "" :jsonResponse.get("XRUCMId"));
            sessionScopeParam.put("LPGUCMId", jsonResponse.isNull("LPGUCMId")? "" : jsonResponse.get("LPGUCMId"));
            sessionScopeParam.put("CRMId", jsonResponse.isNull("CRMId")? "" : jsonResponse.get("CRMId"));
        } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                   (ReturnList.get(0).toString().equalsIgnoreCase("false") &&
                    ReturnList.size() > 1)) {
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

        log.info("duplicationFlag "+sessionScopeParam.get("duplicationFlag"));
        if (sessionScopeParam.get("duplicationFlag")
                         .toString()
                         .equalsIgnoreCase("Y")) {
            sessionScopeParam.put("navigateTo","recordExist");
        } else if (sessionScopeParam.get("duplicationFlag")
                                .toString()
                                .equalsIgnoreCase("N")) {
            log.info("Setting EnrichmentFlag to Y");
            sessionScopeParam.put("navigateTo","recordNotExist");
        }
        
//        sessionScopeParam.put("loyaltyFlow", "Enrichment"); // Setting loyaltyFlow Attribute
        log.info("navigateTo: "+sessionScopeParam.get("navigateTo"));
        String trimURL = "/webcenter/portal/Customer/pages_loyaltyenrichment";
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(trimURL);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        retString = "externalNav"; // For Deployment
//        retString = "next"; // For Temporary Testing
        return retString;
    }
    
    public void linkingCancelActionLis(ActionEvent actionEvent) {
        // Add event code here...
        log.info("Inside linkingCancelActionLis");
        linkingPopUpBinding.hide();
        xtraRewardNoBinding.setValue(null);
        pageflowParam.put("pValidCodeFetchXR",null);
        pageflowParam.put("pValidMsgFetchXR",null);
        /**------------------Resetting the xtra Reward Card No Validation----------**/
        pageflowParam.put("xtraRewardCardErrorMsg",null);
        xtraRewardNoBinding.setStyleClass("");
        /**------------------------------------End Here----------------------------**/
    }

    public void linkingPopUpCancelLis(PopupCanceledEvent popupCanceledEvent) {
        // Add event code here...
        log.info("Inside linkingPopUpCancelLis");
        xtraRewardNoBinding.setValue(null);
        pageflowParam.put("pValidCodeFetchXR",null);
        pageflowParam.put("pValidMsgFetchXR",null);
        /**------------------Resetting the xtra Reward Card No Validation----------**/
        pageflowParam.put("xtraRewardCardErrorMsg",null);
        xtraRewardNoBinding.setStyleClass("");
        /**------------------------------------End Here----------------------------**/
    }
    public void insertVehicleRecordActionLis(ActionEvent actionEvent) {
        // Add event code here...
        log.info("Inside insertVehicleRecordActionLis start");
        OperationBinding op = CommonHelper.findOperation("insertVehicleTypeRecord");
        op.getParamsMap().put("count", "1");
        op.execute();  
        log.info("Inside insertVehicleRecordActionLis end");
    }

    public void setXtraRewardNoBinding(RichInputText xtraRewardNoBinding) {
        this.xtraRewardNoBinding = xtraRewardNoBinding;
    }

    public RichInputText getXtraRewardNoBinding() {
        return xtraRewardNoBinding;
    }


    public void setLinkingPopUpBinding(RichPopup linkingPopUpBinding) {
        this.linkingPopUpBinding = linkingPopUpBinding;
    }

    public RichPopup getLinkingPopUpBinding() {
        return linkingPopUpBinding;
    }

    public void linkYourXtraRewardActionLis(ActionEvent actionEvent) {
        // Add event code here...
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        linkingPopUpBinding.show(hints);
    }

    public String linkingOnLoad() {
        String retString = "goToLinking";
        try {
            JSONObject jsonInput = new JSONObject();
            java.util.List lst = new java.util.ArrayList();

            /**--------------------Temporary Code---------------**/
//            jsonInput.put("MobileNo","0966525143");
//            jsonInput.put("UCMId","PR200000000014256");
            /**----------------------End Here-------------------**/
            jsonInput.put("RequestType", "CheckDuplicate");
            jsonInput.put("MobileNo", CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserMobileNo}"));

            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("PROFILE_ENROLLMENT"));
            lst.add(1, jsonInput);

            OperationBinding opRC = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            opRC.getParamsMap().put("inputList", lst);
            opRC.getParamsMap().put("method", EPICConstant.PROFILE_ENROLLMENT_DUPLICATE);
            opRC.execute();

            JSONObject jsonResponse = new JSONObject();
            java.util.List ReturnList = (java.util.List) opRC.getResult();
            if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                                  .toString()
                                                                                  .equalsIgnoreCase("true"))) {
                jsonResponse = new JSONObject(ReturnList.get(1).toString());
                JSONObject duplicateJson =
                    jsonResponse.isNull("DuplicateProfileData") ? null : jsonResponse.getJSONObject("DuplicateProfileData");
                
                log.info("------------Setting Attribute in Sessionscope as well------------");

                if (duplicateJson != null) {
                    sessionScopeParam.put("sXRCardNumber",
                                          duplicateJson.isNull("XRCardNumber") ? null :
                                          duplicateJson.get("XRCardNumber"));
                    sessionScopeParam.put("sXRPoints",
                                          duplicateJson.isNull("XRPoints") ? null : duplicateJson.get("XRPoints"));
                    sessionScopeParam.put("sMemberNumber",
                                          duplicateJson.isNull("MemberNumber") ? null :
                                          duplicateJson.get("MemberNumber"));
                    sessionScopeParam.put("sDisplayCardNo",
                                          sessionScopeParam.get("sXRCardNumber") != null ?
                                          sessionScopeParam.get("sXRCardNumber") : sessionScopeParam.get("sMemberNumber"));
                    sessionScopeParam.put("sLoyaltyCardNoFound", "Y");
                }
                log.info("We Got Loyalty Card Details in PROFILE_ENROLLMENT_DUPLICATE Service");
            } else {
                log.info("NO Loyalty Card Details in PROFILE_ENROLLMENT_DUPLICATE Service");
                return retString;
            }
        } catch (JSONException jsone) {
            // TODO: Add catch code
            log.info("Some Error in WebService Response");
            log.info(jsone);
            retString = "ERROR";
            return retString;
        }
        return retString;
    }
}
