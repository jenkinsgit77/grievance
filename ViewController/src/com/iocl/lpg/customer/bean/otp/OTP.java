package com.iocl.lpg.customer.bean.otp;


import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import ioclcommonproj.com.iocl.utils.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import java.util.List;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.OperationBinding;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class OTP implements Serializable {
    @SuppressWarnings("compatibility:392462335451957980")
    private static final long serialVersionUID = 1L;

    public OTP() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(OTP.class);

        } else {
            log = Logger.getLogger(OTP.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    private static Logger log;
    private RichInputText txtOTP1;
    private RichInputText txtOTP2;
    private RichInputText txtOTP3;
    private RichInputText txtOTP4;
    private String errorOTP;
    java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();

    public void setTxtOTP1(RichInputText txtOTP1) {
        this.txtOTP1 = txtOTP1;
    }

    public RichInputText getTxtOTP1() {
        return txtOTP1;
    }

    public void setTxtOTP2(RichInputText txtOTP2) {
        this.txtOTP2 = txtOTP2;
    }

    public RichInputText getTxtOTP2() {
        return txtOTP2;
    }

    public void setTxtOTP3(RichInputText txtOTP3) {
        this.txtOTP3 = txtOTP3;
    }

    public RichInputText getTxtOTP3() {
        return txtOTP3;
    }

    public void setTxtOTP4(RichInputText txtOTP4) {
        this.txtOTP4 = txtOTP4;
    }

    public RichInputText getTxtOTP4() {
        return txtOTP4;
    }

    public void setErrorOTP(String errorOTP) {
        this.errorOTP = errorOTP;
    }

    public String getErrorOTP() {
        return errorOTP;
    }


    public String confirmOTP() {
        String retString = null;
        java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
        errorOTP = null;
        if ((txtOTP1 == null || txtOTP1.getValue() == null || StringUtils.isBlank(txtOTP1.getValue().toString())) ||

            (txtOTP2 == null || txtOTP2.getValue() == null || StringUtils.isBlank(txtOTP2.getValue().toString())) ||

            (txtOTP3 == null || txtOTP3.getValue() == null || StringUtils.isBlank(txtOTP3.getValue().toString())) ||

            (txtOTP4 == null || txtOTP4.getValue() == null || StringUtils.isBlank(txtOTP4.getValue().toString()))) {
            errorOTP = CommonHelper.getValueFromRsBundle("PLEASE_GIVE_OTP");
            return null;
        }
        try {
            if (txtOTP1 != null && txtOTP2 != null && txtOTP3 != null && txtOTP4 != null) {
                if (txtOTP1.getValue() != null && txtOTP2.getValue() != null && txtOTP3.getValue() != null &&
                    txtOTP4.getValue() != null) {
                    String mob = param.get("pOTPMOB").toString();
                    String valFromUser =
                        txtOTP1.getValue().toString() + txtOTP2.getValue().toString() + txtOTP3.getValue().toString() +
                        txtOTP4.getValue().toString();
                    OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                    java.util.List lstInput = new java.util.ArrayList();
                    lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("VALIDATE_OTP"));
                    JSONObject jsonInput = new JSONObject();
                    jsonInput.put("MobileNumber", mob);
                    jsonInput.put("OTP", valFromUser);
                    lstInput.add(1, jsonInput);
                    java.util.Map params = ob.getParamsMap();
                    params.put(EPICConstant.SERVICELIST, lstInput);
                    params.put(EPICConstant.SERVICEMETHOD, EPICConstant.VALIDATEOTP);
                    ob.execute();
                    if (!ob.getErrors().isEmpty()) {
                        retString = "ERROR";
                        return retString;
                    }
                    List result = (List) ob.getResult();
                    if (result != null && result.size() > 0) {
                        if (CustomerValidation.isNull(result.get(0))) {
                            retString = "ERROR";
                            return retString;
                        } else {

                            JSONObject jsonObject = new JSONObject(String.valueOf(result.get(1)));
                            String isAuthenticated = jsonObject.getString("IsAuthenticated");
                            if (isAuthenticated != null && isAuthenticated.equalsIgnoreCase("Y")) {
                                retString = "SUCCESS";

                                if (pageflowParam.get(EPICConstant.OPT_FLOW_PAGE_FLOW_PARAM) != null &&
                                    pageflowParam.get(EPICConstant.OPT_FLOW_PAGE_FLOW_PARAM).toString().equalsIgnoreCase("ReactivateConnection")) {
                                                                          /*------------------Code to Execute Reactivate Connection Service-----------*/
                                                                          log.info("Reactivate Connection Case");
                                    JSONObject jsonResponse = new JSONObject();
                                    java.util.List lst = new java.util.ArrayList();
                                    lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("REACTIVATE_LPG_CONNECTION"));
                                    // 1-jsonInput
                                    if (pageflowParam.get(EPICConstant.REACTIVATE_LPG_JSON) != null) {
                                        lst.add(1,
                                                new JSONObject(pageflowParam.get(EPICConstant.REACTIVATE_LPG_JSON)
                                                               .toString()));
                                    }
                                    OperationBinding opRC =
                                        CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                                    opRC.getParamsMap().put("inputList", lst);
                                    opRC.getParamsMap().put("method", EPICConstant.REACTIVATE_LPG_CONNECTION);
                                    opRC.execute();

                                    java.util.List ReturnList = (java.util.List) opRC.getResult();
                                    if ((ReturnList != null) && (ReturnList.get(0) != null) &&
                                        (ReturnList.get(0).toString().equalsIgnoreCase("true"))) {
                                        log.info("ReturnList Value at 0 index is " + ReturnList.get(0));
                                        jsonResponse = new JSONObject(ReturnList.get(1).toString());
                                        pageflowParam.put("pConfirmationSRNumber",jsonResponse.isNull("SRNumber") ? null :jsonResponse.get("SRNumber"));
                                        /*----------------Flow to move Confirmation Page------------------*/
                                        log.info("ReActivation is Successfull. Move to Confirmation Page");
                                        pageflowParam.put("confMessage",
                                                          CommonHelper.getValueFromRsBundle("YOUR_SERVICE_REQUEST_FOR_REACTIVATE_CONNECTION_APPLICATION_IS"));
                                        pageflowParam.put("refNumber",jsonResponse.isNull("SRNumber") ? null :jsonResponse.get("SRNumber"));
                                        retString = "SUCCESS";
                                        /*---------------------------End Here-------------------------*/
                                    } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                                               (ReturnList.get(0).toString().equalsIgnoreCase("false") &&
                                                ReturnList.get(1) != null)) {
                                        jsonResponse = new JSONObject(ReturnList.get(1).toString());

                                        pageflowParam.put("pValidCode",
                                                          jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                                                          jsonResponse.getString(EPICConstant.ERROR_CODE));
                                        pageflowParam.put("pValidMsg",
                                                          jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                                                          jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


                                        if (pageflowParam.get("pValidCode") != null &&
                                            pageflowParam.get("pValidCode").toString().equalsIgnoreCase(EPICConstant.SBL100)) {
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
                                    /*-----------------------------------End Here------------------------*/
                                }
                            } else {
                                errorOTP = CommonHelper.getValueFromRsBundle("PLEASE_GIVE_CORRECT_OTP");
                                txtOTP1.setValue("");
                                txtOTP2.setValue("");
                                txtOTP3.setValue("");
                                txtOTP4.setValue("");
                                AdfFacesContext.getCurrentInstance().addPartialTarget(txtOTP1);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(txtOTP2);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(txtOTP3);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(txtOTP4);
                                return null;
                            }
                        }
                    } else {
                        retString = "ERROR";
                        return retString;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            retString = "ERROR";
        }
        return retString;

    }

    public void sendOtp() {
        java.util.Map<String, Object> param = ADFContext.getCurrent().getPageFlowScope();
        try {
            String mobileNumber = param.get("pOTPMOB") == null ? "" : param.get("pOTPMOB").toString();
            String isSent = sendOtptoPhone(mobileNumber);
            if (isSent == null || !isSent.equalsIgnoreCase("Y")) { // correct if condition by Poonam on 02Jan2018
                param.put("pErrorCode", "Exception");
                param.put("pErrorMessage", "Exception Occured");
                throw new Exception("Message was not sent");
            }
        } catch (UnsupportedEncodingException ue) {
            ue.printStackTrace();
            param.put("pErrorCode", "Exception");
            param.put("pErrorMessage", "Exception Occured");
        } catch (Exception e) {
            e.printStackTrace();
            param.put("pErrorCode", "Exception");
            param.put("pErrorMessage", "Exception Occured");
        }
    }

    public String resendOtp() {
        String retString = null;
        java.util.Map<String, Object> param = ADFContext.getCurrent().getPageFlowScope();
        try {
            String mobileNumber = param.get("pOTPMOB") == null ? "" : param.get("pOTPMOB").toString();
            String isSent = sendOtptoPhone(mobileNumber);
            if (isSent == null || !isSent.equalsIgnoreCase("Y")) {
                param.put("pErrorCode", "Exception");
                param.put("pErrorMessage", "Exception Occured");
                retString = "errorPage";
            } else {
                errorOTP = CommonHelper.getValueFromRsBundle("OTP_SENT_SUCCESSFULLY");
            }
        } catch (UnsupportedEncodingException ue) {
            ue.printStackTrace();
            param.put("pErrorCode", "Exception");
            param.put("pErrorMessage", "Exception Occured");
        } catch (Exception e) {
            e.printStackTrace();
            param.put("pErrorCode", "Exception");
            param.put("pErrorMessage", "Exception Occured");
        }
        return retString;
    }

    private String sendOtptoPhone(String mobileNum) throws UnsupportedEncodingException, Exception {
        String retString = "Y";
        try {
            OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            java.util.List lstInput = new java.util.ArrayList();
            lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("SEND_OTP"));
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("MobileNumber", mobileNum);
            lstInput.add(1, jsonInput.toString());
            java.util.Map params = ob.getParamsMap();
            params.put(EPICConstant.SERVICELIST, lstInput);
            params.put(EPICConstant.SERVICEMETHOD, EPICConstant.SENDOTP);
            ob.execute();
            if (!ob.getErrors().isEmpty()) {
                retString = "N";
                return retString;
            }
            List result = (List) ob.getResult();
            if (result != null && result.size() > 0) {
                if (CustomerValidation.isNull(result.get(0)) || result.get(0)
                                                                      .toString()
                                                                      .equalsIgnoreCase("FALSE")) {
                    retString = "N";
                    return retString;
                } else {

                    JSONObject jsonObject = (JSONObject) result.get(1);
                    String isSent = jsonObject.getString("IsSent");
                    retString = isSent;
                }
            } else {
                retString = "N";
                return retString;
            }
        } catch (Exception e) {
            retString = "N";
            throw e;
        }
        return retString;
    }
}


