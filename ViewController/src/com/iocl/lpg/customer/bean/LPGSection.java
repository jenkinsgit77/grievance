package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichButton;

import oracle.binding.OperationBinding;

import org.apache.log4j.Logger;

import org.json.JSONException;
import ioclcommonproj.com.iocl.utils.JSONObject;


public class LPGSection implements Serializable {
    private RichPopup popUpBinding;
    private RichPopup lpgConnPopUpBinding;
    private RichSelectOneRadio lpgConnBinding;
    private RichPanelGroupLayout lpgPageMainPgBinding;
    private RichButton lpgSectionSubmitBtnBinding;

    public LPGSection() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(LPGSection.class);

        } else {
            log = Logger.getLogger(LPGSection.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    private static Logger log ;
    java.util.Map sessionscope = ADFContext.getCurrent().getSessionScope();
    java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
    private String reactivationErrorMsg;

    public void setReactivationErrorMsg(String reactivationErrorMsg) {
        this.reactivationErrorMsg = reactivationErrorMsg;
    }

    public String getReactivationErrorMsg() {
        return reactivationErrorMsg;
    }

    public String onClickLinkAction() {
        // Add event code here...
        return null;
    }

    public String LPGSectionOnLoad() {
        String retString = "goToLPG";
        String trimURL = null;
        String vkycLevel = null;
        log.info("Inside LPGSectionOnLoad Start");
        if (sessionscope.get("LPGCurrSection") != null && sessionscope.get("LPGCurrSection")
                                                                      .toString()
                                                                      .equalsIgnoreCase("LPGDashboard")) {
            log.info("Move from Customer Portal to LPG Portal");
            log.info("LPGCurrSection is LPGDashboard Move directly to LPG Portal");
            /*-------------------Code to Move to External Context-----------*/
            if (String.valueOf(sessionscope.get("redirectCheck")).equalsIgnoreCase("lpgOver")) {
                trimURL = "/webcenter/portal/LPG/pages_dashboard";
            } else if (String.valueOf(sessionscope.get("redirectCheck")).equalsIgnoreCase("myprofile")) {
                trimURL = "/webcenter/portal/LPG/pages_profile360";
            } else {
               
                trimURL = "/webcenter/portal/LPG/pages_dashboard";

            }
//                        String openURL = "window.location.replace(\"" + trimURL + "\");";
//                        log.info("openURL in LPGSectionOnLoad: " + openURL);
//                        CommonHelper.runJavaScript(openURL);

            log.info("Before Redirection in LPGSectionOnLoad");
            ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
            try {
                exct.redirect(trimURL);
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("After Redirection in LPGSectionOnLoad");
            retString = "externalNav";
            /*----------------------------End Here-------------------------*/
        }

        if (CommonHelper.evaluateEL("#{sessionScope.userDetails.hasBlankUCMId}") != null && CommonHelper.evaluateEL("#{sessionScope.userDetails.hasBlankUCMId}").toString().equalsIgnoreCase("Y")) {
        /**--------------Code to Check SV Case------------**/
        log.info("Code to Check Subscription Voucher Case for coexistence");
        java.util.List lstInputLpgS = new java.util.ArrayList();
        JSONObject jsonInputLpgS = new JSONObject();
        if (CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}") != null) {
            jsonInputLpgS.put("IdamUId", CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}"));
        } else {
            jsonInputLpgS.put("IdamUId", "");
        }

        lstInputLpgS.add(0, EPICIOCLResourceCustBundle.findKeyValue("NEW_CONNECTION_STATUS"));
        lstInputLpgS.add(1, jsonInputLpgS);

        OperationBinding opLpgS = CommonHelper.findOperation("serviceCustomerCall");
        opLpgS.getParamsMap().put("inputList", lstInputLpgS);
        opLpgS.getParamsMap().put("method", EPICConstant.NEW_CONNECTION_STATUS);
        opLpgS.execute();

        java.util.List returnListLpgS = (java.util.List) opLpgS.getResult();
        if ((returnListLpgS != null) && (returnListLpgS.get(0) != null) &&
            returnListLpgS.get(0).toString().equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
            JSONObject jsonObjectLpgS = new JSONObject(returnListLpgS.get(1).toString());
            CommonHelper.setEL("#{sessionScope.userDetails.kycLevel}",
                               jsonObjectLpgS.has("KYCLevel") ? jsonObjectLpgS.get("KYCLevel") : "");
            vkycLevel = jsonObjectLpgS.isNull("KYCLevel") ? null :jsonObjectLpgS.get("KYCLevel").toString();
            pageflowParam.put("connectionCancelledMsg",jsonObjectLpgS.isNull("Reason") ? null :jsonObjectLpgS.get("Reason").toString());
                if (vkycLevel.equalsIgnoreCase("NA")) {
                    sessionscope.put("LPGCurrSection", "ConnectionCancelled");
                } else {
                    CommonHelper.kycStatusImage(vkycLevel);
                    sessionscope.put("LPGCurrSection", "KycTrainStatus");
                }
            
        } else if ((returnListLpgS != null && returnListLpgS.get(0) != null) &&
                   (returnListLpgS.get(0).toString().equalsIgnoreCase("false") &&
                    returnListLpgS.get(1) != null)) {
            JSONObject jsonObjectLpgS = new JSONObject(returnListLpgS.get(1).toString());

            pageflowParam.put("pValidCode",
                              jsonObjectLpgS.isNull(EPICConstant.ERROR_CODE) ? null :
                              jsonObjectLpgS.getString(EPICConstant.ERROR_CODE));
            pageflowParam.put("pValidMsg",
                              jsonObjectLpgS.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                              jsonObjectLpgS.getString(EPICConstant.ERROR_MESSAGE));

            if (pageflowParam.get("pValidCode") != null && pageflowParam.get("pValidCode")
                                                                        .toString()
                                                                        .equalsIgnoreCase(EPICConstant.SBL100)) {
                log.info("Business Validation Issue SBL-100 ErrorCode Received");
                log.info("LPGCurrSection set to NewConnection");
                sessionscope.put("LPGCurrSection","NewConnection");
                
            } else {
                log.info("Some Error in WebService Response");
                retString = "ERROR";
                return retString;
            }
        } else {
            retString = "ERROR";
        }
        /**-----------------End Here----------------------**/
                }

        log.info("Inside LPGSectionOnLoad End");
        return retString;
    }

    public void setPopUpBinding(RichPopup popUpBinding) {
        this.popUpBinding = popUpBinding;
    }

    public RichPopup getPopUpBinding() {
        return popUpBinding;
    }


    public void KycConnActionLis(ActionEvent actionEvent) {
        // Add event code here...
        log.info("Inside KycConnActionLis");
        CommonHelper.refreshPage();

        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            sessionscope.put("isKYCclicked", "Y");
            exct.redirect("/webcenter/portal/LPG/pages_loginkyc");

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public String reactivateNowAction() {
        // Add event code here...
        String vretString = "goToReactivation";
//        JSONObject jsonInput = new JSONObject();
//        
//        if (sessionscope.get("LPGCurrSection") != null) {
//            jsonInput.put(EPICConstant.RELATIONSHIP_SUB_STATUS, sessionscope.get("LPGCurrSection").toString());
//        }
//        pageflowParam.put(EPICConstant.REACTIVATE_LPG_JSON, jsonInput.toString());
//        String mobileNo = null;
//        if (CommonHelper.evaluateEL("#{sessionScope.userDetails.mobile}") != null) {
//            mobileNo = String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.mobile}"));
//        }
//
//        log.info("Consumer Mobile No for OTP: " +mobileNo);
//        if (mobileNo == null) {
//            log.info("Consumer Mobile No. is null");
//            log.info("OTP Cannot be send");
//            reactivationErrorMsg = CommonHelper.getValueFromRsBundle("REACTIVATION_FAIL_MESSAGE");
//            return null;
//        }
//
//        reactivationErrorMsg = null;
//        CommonHelper.sendOTP(mobileNo);
//        pageflowParam.put(EPICConstant.MOBILE_PAGE_FLOW_PARAM,mobileNo);
//        pageflowParam.put(EPICConstant.OPT_FLOW_PAGE_FLOW_PARAM, "ReactivateConnection");

        return vretString;
    }

    public void setLpgConnPopUpBinding(RichPopup lpgConnPopUpBinding) {
        this.lpgConnPopUpBinding = lpgConnPopUpBinding;
    }

    public RichPopup getLpgConnPopUpBinding() {
        return lpgConnPopUpBinding;
    }

    public void setLpgConnBinding(RichSelectOneRadio lpgConnBinding) {
        this.lpgConnBinding = lpgConnBinding;
    }

    public RichSelectOneRadio getLpgConnBinding() {
        return lpgConnBinding;
    }

    public void setLpgPageMainPgBinding(RichPanelGroupLayout lpgPageMainPgBinding) {
        this.lpgPageMainPgBinding = lpgPageMainPgBinding;
    }

    public RichPanelGroupLayout getLpgPageMainPgBinding() {
        return lpgPageMainPgBinding;
    }

    public void setLpgSectionSubmitBtnBinding(RichButton lpgSectionSubmitBtnBinding) {
        this.lpgSectionSubmitBtnBinding = lpgSectionSubmitBtnBinding;
    }

    public RichButton getLpgSectionSubmitBtnBinding() {
        return lpgSectionSubmitBtnBinding;
    }

    public String linkYourLPGAction() {
        // Add event code here...
        CommonHelper.refreshPage();
        return "goToLinkLPG";
    }

    public void loginRefresh(ActionEvent actionEvent) {
        // Add event code here...
       // logger.info("Inside loginRefresh  method in LPG Section");
        customerNavigate();
        CommonHelper.refreshPage();
    }
    
    
    public void customerNavigate() {
        
        String destination = "/webcenter";
        String appPath = null;
        if (ADFContext.getCurrent()
                      .getRequestScope()
                      .get("ActionName") != null) {
            appPath = ADFContext.getCurrent()
                                .getRequestScope()
                                .get("ActionName")
                                .toString();
        //    logger.info("appPath from db is: " + appPath);
        }
        destination = destination + appPath;
        //logger.info("final destination app Path is: " + destination);

        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(destination);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    public String cancelApplicationReqAction() {
        // Add event code here...
        String retString = "SUCCESS";
        try {
            java.util.List lstInput = new java.util.ArrayList();
            JSONObject jsonInput = new JSONObject();

            lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("NEW_CONNECTION_CANCEL_REQUEST"));
            lstInput.add(1, jsonInput);

            OperationBinding opSR = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            opSR.getParamsMap().put("inputList", lstInput);
            opSR.getParamsMap().put("method", EPICConstant.NEW_CONNECTION_CANCEL_REQUEST);
            opSR.execute();
            java.util.List returnList = (java.util.List) opSR.getResult();
            if ((returnList != null) && (returnList.get(0) != null) &&
                returnList.get(0).toString().equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                JSONObject jsonObject = new JSONObject(returnList.get(1).toString());
                
                pageflowParam.put("confMessage",CommonHelper.getValueFromRsBundle("CANCEL_APPLICATION_SUCCESS_MESSAGE"));

            } else if ((returnList != null && returnList.get(0) != null) &&
                       (returnList.get(0).toString().equalsIgnoreCase("false") &&
                        returnList.get(1) != null)) {
                JSONObject jsonResponse = new JSONObject(returnList.get(1).toString());

                pageflowParam.put("pValidCode",
                                  jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_CODE));
                pageflowParam.put("pValidMsg",
                                  jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


                if (pageflowParam.get("pValidCode") != null && pageflowParam.get("pValidCode")
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
                retString = "ERROR";
            }
        } catch (JSONException jsone) {
            // TODO: Add catch code
            retString = "ERROR";
            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
            jsone.printStackTrace();
        }

        return retString;
    }
}
