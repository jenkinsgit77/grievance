package com.iocl.lpg.customer.bean.reactivate;

import com.iocl.lpg.customer.bean.otp.OTP;
import com.iocl.lpg.customer.bean.payment.PaymentRedirectBean;
import com.iocl.lpg.customer.utils.CommonHelper;

import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;

import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import com.iocl.lpg.customer.utils.UCMUtil;

import java.io.IOException;
import java.io.Serializable;

import java.util.List;

import java.util.Map;

import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.share.ADFContext;

import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.component.rich.output.RichOutputFormatted;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.binding.OperationBinding;

import oracle.stellent.ridc.IdcClientException;

import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class ReactivateBean implements Serializable {
    private RichSelectOneChoice poiTypeBinding;
    private RichInputText poiNoBinding;
    private RichSelectOneChoice poaTypeBinding;
    private RichInputText poaNoBinding;

    private RichOutputText poaDocTypeBinding;
    private RichOutputText poiDocTypeBinding;

    public ReactivateBean() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(ReactivateBean.class);

        } else {
            log = Logger.getLogger(ReactivateBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }

    private String proofofIdenErrorMsg;
    private String proofofIdenNoErrorMsg;
    private String proofofAddErrorMsg;
    private String proofofAddNoErrorMsg;
    private String poiFileErrorMsg;
    private String poaFileErrorMsg;

    private static Logger log ;
    java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
    java.util.Map sessionParam = ADFContext.getCurrent().getSessionScope();
    private Boolean errorInReactivate;
    private String attachmentErrorMsg;


    public void setAttachmentErrorMsg(String attachmentErrorMsg) {
        this.attachmentErrorMsg = attachmentErrorMsg;
    }

    public String getAttachmentErrorMsg() {
        return attachmentErrorMsg;
    }

    public void setProofofIdenErrorMsg(String proofofIdenErrorMsg) {
        this.proofofIdenErrorMsg = proofofIdenErrorMsg;
    }

    public String getProofofIdenErrorMsg() {
        return proofofIdenErrorMsg;
    }

    public void setProofofIdenNoErrorMsg(String proofofIdenNoErrorMsg) {
        this.proofofIdenNoErrorMsg = proofofIdenNoErrorMsg;
    }

    public String getProofofIdenNoErrorMsg() {
        return proofofIdenNoErrorMsg;
    }

    public void setProofofAddErrorMsg(String proofofAddErrorMsg) {
        this.proofofAddErrorMsg = proofofAddErrorMsg;
    }

    public String getProofofAddErrorMsg() {
        return proofofAddErrorMsg;
    }

    public void setProofofAddNoErrorMsg(String proofofAddNoErrorMsg) {
        this.proofofAddNoErrorMsg = proofofAddNoErrorMsg;
    }

    public String getProofofAddNoErrorMsg() {
        return proofofAddNoErrorMsg;
    }

    public void setPoiFileErrorMsg(String poiFileErrorMsg) {
        this.poiFileErrorMsg = poiFileErrorMsg;
    }

    public String getPoiFileErrorMsg() {
        return poiFileErrorMsg;
    }

    public void setPoaFileErrorMsg(String poaFileErrorMsg) {
        this.poaFileErrorMsg = poaFileErrorMsg;
    }

    public String getPoaFileErrorMsg() {
        return poaFileErrorMsg;
    }

    public String reactivationOnload() {
        String retString = "goToReactivate";

        try {
            List lstAtt = new java.util.ArrayList();
            lstAtt.add("ReactPoiName");
            lstAtt.add("ReactPoiExt");
            lstAtt.add("ReactPoiBase64");
            lstAtt.add("ReactPoaName");
            lstAtt.add("ReactPoaExt");
            lstAtt.add("ReactPoaBase64");

            lstAtt.add("a");
            lstAtt.add("b");
            lstAtt.add("c");
            lstAtt.add("d");
            lstAtt.add("e");
            lstAtt.add("f");
            CommonHelper.uploadReset(lstAtt);
        } catch (Exception e) {
            // TODO: Add catch code
            retString = "ERROR";
            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
            e.printStackTrace();
        }

        sessionParam.put(EPICConstant.FLOW_NAME, EPICConstant.REACTIVATION);
        return retString;
    }


    public boolean validateReactivation() {
        errorInReactivate = false;
        boolean poiSelected = true, poaSelected = true, validatedNum;

//        if (!(CommonHelper.evaluateEL("#{bindings.POADocType.inputValue}") != null &&
//              CommonHelper.evaluateEL("#{bindings.POADocType.inputValue}").equals("POA-POI"))) {
            if (poiTypeBinding.getValue() == null ||
                poiTypeBinding.getValue().equals(CommonHelper.getValueFromModelRsBundle("PLEASE_SELECT"))) {
                proofofIdenErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_ONE_PROOF_OF_IDENTITY_PLACEHOLDER");
                poiTypeBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                errorInReactivate = true;
                poiSelected = false;
            }
            if (CustomerValidation.isNull(poiNoBinding.getValue())) {
                proofofIdenNoErrorMsg =
                    CommonHelper.getValueFromRsBundle("ENTER_THE_SELECTED_POI_DOCUMENT_ID_NUMBER_PLACEHOLDER");
                poiNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                errorInReactivate = true;
                poiSelected = false;
            }

            if (sessionParam.get("ReactPoiName") == null || sessionParam.get("ReactPoiName") == "") {
                poiFileErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_A_POI_FILE");
                errorInReactivate = true;
            }

            if (poiSelected) {
                validatedNum = false;
                validatedNum =
                    CustomerValidation.validatePOI(poiNoBinding.getValue().toString(),
                                                   poiTypeBinding.getValue().toString());
                if (!validatedNum) {
                    proofofIdenNoErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_VALID_POI_NUMBER");
                    poiNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                    errorInReactivate = true;
                }
            }
//        }

//        if (!(CommonHelper.evaluateEL("#{bindings.POIDocType.inputValue}") != null &&
//              CommonHelper.evaluateEL("#{bindings.POIDocType.inputValue}").equals("POA-POI"))) {
            log.info("poaTypeBinding " + poaTypeBinding.getValue());
            if (poaTypeBinding.getValue() == null ||
                poaTypeBinding.getValue().equals(CommonHelper.getValueFromModelRsBundle("PLEASE_SELECT"))) {
                proofofAddErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_ONE_PROOF_OF_ADDRESS_PLACEHOLDER");
                poaTypeBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                errorInReactivate = true;
                poaSelected = false;
            }

            log.info("poaTypeBinding " + poaTypeBinding.getValue());
            if (CustomerValidation.isNull(poaNoBinding.getValue())) {
                proofofAddNoErrorMsg =
                    CommonHelper.getValueFromRsBundle("ENTER_THE_SELECTED_POA_DOCUMENT_ID_NUMBER_PLACEHOLDER");
                poaNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                errorInReactivate = true;
                poaSelected = false;
            }

            if (sessionParam.get("ReactPoaName") == null || sessionParam.get("ReactPoaName") == "") {
                poaFileErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_A_POA_FILE");
                errorInReactivate = true;
            }

            if (poaSelected) {
                validatedNum = false;
                validatedNum =
                    CustomerValidation.validatePOI(poaNoBinding.getValue().toString(),
                                                   poaTypeBinding.getValue().toString());

                log.info("poaNoBinding value " + poaNoBinding.getValue().toString());
                log.info("poaTypeBinding value" + poaTypeBinding.getValue().toString());
                if (!validatedNum) {
                    proofofAddNoErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_VALID_POA_NUMBER");
                    poaNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                    errorInReactivate = true;
                }
            }
//        }
        
        
        /**-----------Code to validate both POA & POI documents values for same Type of attachment-------**/
        if (!(CustomerValidation.isNull(poiTypeBinding.getValue()) ||
              CustomerValidation.isNull(poaTypeBinding.getValue()))) {
            if (poiTypeBinding.getValue()
                              .toString()
                              .equalsIgnoreCase(poaTypeBinding.getValue().toString())) {
                if (!(CustomerValidation.isNull(poiNoBinding.getValue()) ||
              CustomerValidation.isNull(poaNoBinding.getValue()))) {
                    if (!(poiNoBinding.getValue()
                                      .toString()
                                      .equalsIgnoreCase(poaNoBinding.getValue().toString()))) {
                        attachmentErrorMsg =
                            CommonHelper.getValueFromRsBundle("PLEASE_ENTER_CORRECT_VALUE_FOR_SAME_TYPE_OF_ATTACHMENT");
                        errorInReactivate = true;
                    }
                }

            }
        }
        return errorInReactivate;
    }


    public void resetValidateReactivation() {
        poiTypeBinding.setStyleClass("");
        poiNoBinding.setStyleClass("");
        poaTypeBinding.setStyleClass("");
        poaNoBinding.setStyleClass("");

        proofofIdenErrorMsg = null;
        proofofIdenNoErrorMsg = null;
        poiFileErrorMsg = null;
        proofofAddErrorMsg = null;
        proofofAddNoErrorMsg = null;
        poaFileErrorMsg = null;
    }

    public String reactivationAction() {
        // Add event code here...
        String retString = "goToConfirmation";
        /*------------------Code to Execute Reactivate Connection Service-----------*/
        log.info("Reactivate Connection Case");

        resetValidateReactivation();
        errorInReactivate = validateReactivation();

        if (errorInReactivate) {
            return null;
        }
        JSONObject jsonInput = new JSONObject();
        java.util.List lst = new java.util.ArrayList();

        /*-----------------------------------------*/
        JSONArray imgPoI = new JSONArray();
        JSONArray imgPoA = new JSONArray();

        JSONObject jsonInputPoiChild = new JSONObject();
        JSONObject jsonInputPoaChild = new JSONObject();

        Map<String, String> poiFileInfo =
            (Map<String, String>) (sessionParam.get("ReactPoIFileInfo") == null ? null :
                                   sessionParam.get("ReactPoIFileInfo"));
        Map<String, String> poaFileInfo =
            (Map<String, String>) (sessionParam.get("ReactPoaFileInfo") == null ? null :
                                   sessionParam.get("ReactPoaFileInfo"));


//        if (!(CommonHelper.evaluateEL("#{bindings.POADocType.inputValue}") != null &&
//              CommonHelper.evaluateEL("#{bindings.POADocType.inputValue}").equals("POA-POI"))) {
            jsonInputPoiChild.put(EPICConstant.ATTACHMENT_TYPE, poiTypeBinding.getValue());
            jsonInputPoiChild.put(EPICConstant.ATTACHMENT_NUMBER, poiNoBinding.getValue());
            jsonInputPoiChild.put(EPICConstant.ATTACHMENT_DOCTYPE,
                                  CommonHelper.evaluateEL("#{bindings.POIDocType.inputValue}"));
            jsonInputPoiChild.put(EPICConstant.ATTACHMENT_FILENAME, sessionParam.get("ReactPoiName"));
            jsonInputPoiChild.put(EPICConstant.ATTACHMENT_EXTENSION, sessionParam.get("ReactPoiExt"));
            if (poiFileInfo != null) {
                jsonInputPoiChild.put(EPICConstant.DOC_ID, poiFileInfo.get("dId"));
                jsonInputPoiChild.put(EPICConstant.CONTENT_ID, poiFileInfo.get("contentId"));
            }
            imgPoI.put(jsonInputPoiChild);
            jsonInput.put("POI", imgPoI);
//        }

//        if (!(CommonHelper.evaluateEL("#{bindings.POIDocType.inputValue}") != null &&
//              CommonHelper.evaluateEL("#{bindings.POIDocType.inputValue}").equals("POA-POI"))) {
            jsonInputPoaChild.put(EPICConstant.ATTACHMENT_TYPE, poaTypeBinding.getValue());
            jsonInputPoaChild.put(EPICConstant.ATTACHMENT_NUMBER, poaNoBinding.getValue());
            jsonInputPoaChild.put(EPICConstant.ATTACHMENT_DOCTYPE,
                                  CommonHelper.evaluateEL("#{bindings.POADocType.inputValue}"));
            jsonInputPoaChild.put(EPICConstant.ATTACHMENT_FILENAME, sessionParam.get("ReactPoaName"));
            jsonInputPoaChild.put(EPICConstant.ATTACHMENT_EXTENSION, sessionParam.get("ReactPoaExt"));
            if (poaFileInfo != null) {
                jsonInputPoaChild.put(EPICConstant.DOC_ID, poaFileInfo.get("dId"));
                jsonInputPoaChild.put(EPICConstant.CONTENT_ID, poaFileInfo.get("contentId"));
            }
            imgPoA.put(jsonInputPoaChild);
            jsonInput.put("POA", imgPoA);
//        }
        /*-----------------------------------------*/


        lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("REACTIVATE_LPG_CONNECTION"));
        lst.add(1, jsonInput);

        OperationBinding opRC = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
        opRC.getParamsMap().put("inputList", lst);
        opRC.getParamsMap().put("method", EPICConstant.REACTIVATE_LPG_CONNECTION);
        opRC.execute();

        JSONObject jsonResponse = new JSONObject();
        java.util.List ReturnList = (java.util.List) opRC.getResult();
        if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                              .toString()
                                                                              .equalsIgnoreCase("true"))) {
            log.info("ReturnList Value at 0 index is " + ReturnList.get(0));
            jsonResponse = new JSONObject(ReturnList.get(1).toString());
            /*----------------Flow to move Confirmation Page------------------*/
            log.info("ReActivation is Successfull. Move to Confirmation Page");
            pageflowParam.put("confMessage",
                              CommonHelper.getValueFromRsBundle("YOUR_SERVICE_REQUEST_FOR_REACTIVATE_CONNECTION_APPLICATION_IS"));
            pageflowParam.put("refNumber", jsonResponse.isNull("SRNumber") ? null : jsonResponse.get("SRNumber"));

            /*---------------------------End Here-------------------------*/
        } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                   (ReturnList.get(0).toString().equalsIgnoreCase("false") &&
                    ReturnList.size() > 1)) {
            jsonResponse = new JSONObject(ReturnList.get(1).toString());

            pageflowParam.put("pValidCodeReact",
                              jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                              jsonResponse.getString(EPICConstant.ERROR_CODE));
            pageflowParam.put("pValidMsgReact",
                              jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                              jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


            if (pageflowParam.get("pValidCodeReact") != null && pageflowParam.get("pValidCodeReact")
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
        /*-----------------------------------End Here------------------------*/
        return retString;
    }

    public void setPoiTypeBinding(RichSelectOneChoice poiTypeBinding) {
        this.poiTypeBinding = poiTypeBinding;
    }

    public RichSelectOneChoice getPoiTypeBinding() {
        return poiTypeBinding;
    }

    public void setPoiNoBinding(RichInputText poiNoBinding) {
        this.poiNoBinding = poiNoBinding;
    }

    public RichInputText getPoiNoBinding() {
        return poiNoBinding;
    }

    public void setPoaTypeBinding(RichSelectOneChoice poaTypeBinding) {
        this.poaTypeBinding = poaTypeBinding;
    }

    public RichSelectOneChoice getPoaTypeBinding() {
        return poaTypeBinding;
    }

    public void setPoaNoBinding(RichInputText poaNoBinding) {
        this.poaNoBinding = poaNoBinding;
    }

    public RichInputText getPoaNoBinding() {
        return poaNoBinding;
    }

    public void reactivateResetActionlis(ActionEvent actionEvent) {
        // Add event code here...
        resetValidateReactivation();

        poiTypeBinding.setValue("");
        CommonHelper.setEL("#{bindings.ProofOfIdentityType.inputValue}", null);
        poiNoBinding.setValue("");
        CommonHelper.setEL("#{bindings.ProofOfIdentityNo.inputValue}", null);
        poiDocTypeBinding.setValue("");
        CommonHelper.setEL("#{bindings.POIDocType.inputValue}", null);
        
        poaTypeBinding.setValue("");
        CommonHelper.setEL("#{bindings.ProofOfAddressType.inputValue}", null);
        poaNoBinding.setValue("");
        CommonHelper.setEL("#{bindings.ProofOfAddressNo.inputValue}", null);
        poaDocTypeBinding.setValue("");
        CommonHelper.setEL("#{bindings.POADocType.inputValue}", null);

        List lstAtt = new java.util.ArrayList();
        lstAtt.add("ReactPoiName");
        lstAtt.add("ReactPoiExt");
        lstAtt.add("ReactPoiBase64");
        lstAtt.add("ReactPoaName");
        lstAtt.add("ReactPoaExt");
        lstAtt.add("ReactPoaBase64");

        lstAtt.add("a");
        lstAtt.add("b");
        lstAtt.add("c");
        lstAtt.add("d");
        lstAtt.add("e");
        lstAtt.add("f");
        CommonHelper.uploadReset(lstAtt);
    }

    public void poiTypeValueCl(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        /**--------getNewValue not null conditionn removed as we have to perform action
         * --------Even on null value-----------**/
        //        if (valueChangeEvent.getNewValue() != null) {
        //            String poiType = String.valueOf(valueChangeEvent.getNewValue());
        //            sessionParam.put("selectedAttachmentDocType", poiType);
        /**--------Removing attachment from content server-------**/
        try {
            Map<String, String> poiFileInfo =
                (Map<String, String>) (sessionParam.get("ReactPoIFileInfo") == null ? null :
                                       sessionParam.get("ReactPoIFileInfo"));
            if (poiFileInfo != null) {
                UCMUtil.deleteAttatchment(poiFileInfo.get("contentId"), poiFileInfo.get("dId"));
            }
        } catch (IOException ioe) {
            // TODO: Add catch code
            ioe.printStackTrace();
        } catch (IdcClientException ice) {
            // TODO: Add catch code
            ice.printStackTrace();
        }
        /**-------------------------End Here---------------------**/

        /**--------Resetting POI attachment files------**/
        List lstAtt = new java.util.ArrayList();
        lstAtt.add("ReactPoiName");
        lstAtt.add("ReactPoiExt");
        lstAtt.add("ReactPoiBase64");

        lstAtt.add("a");
        lstAtt.add("b");
        lstAtt.add("c");
        CommonHelper.uploadReset(lstAtt);

        poiNoBinding.setValue("");
        poiNoBinding.setStyleClass("");
        /**-----------------End Here---------------**/
        //        }
    }


    public void poaTypeValueCl(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != null) {
            //            String poiId = String.valueOf(valueChangeEvent.getNewValue());
            //            sessionParam.put("selectedAttachmentDocId", poiId);
            /**--------Removing attachment from content server-------**/
            try {
                Map<String, String> poaFileInfo =
                    (Map<String, String>) (sessionParam.get("ReactPoaFileInfo") == null ? null :
                                           sessionParam.get("ReactPoaFileInfo"));
                if (poaFileInfo != null) {
                    UCMUtil.deleteAttatchment(poaFileInfo.get("contentId"), poaFileInfo.get("dId"));
                }
            } catch (IOException ioe) {
                // TODO: Add catch code
                ioe.printStackTrace();
            } catch (IdcClientException ice) {
                // TODO: Add catch code
                ice.printStackTrace();
            }
            /**-------------------------End Here---------------------**/

            /**--------Resetting POA attachment files------**/
            List lstAtt = new java.util.ArrayList();
            lstAtt.add("ReactPoaName");
            lstAtt.add("ReactPoaExt");
            lstAtt.add("ReactPoaBase64");

            lstAtt.add("d");
            lstAtt.add("e");
            lstAtt.add("f");
            CommonHelper.uploadReset(lstAtt);

            poaNoBinding.setValue("");
            poaNoBinding.setStyleClass("");
            /**-----------------End Here---------------**/
        }
    }


    public void setPoaDocTypeBinding(RichOutputText poaDocTypeBinding) {
        this.poaDocTypeBinding = poaDocTypeBinding;
    }

    public RichOutputText getPoaDocTypeBinding() {
        return poaDocTypeBinding;
    }

    public void poiNoTypeValueCl(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
//        if (valueChangeEvent.getNewValue() != null) {
            //            String poiId = String.valueOf(valueChangeEvent.getNewValue());
            //            sessionParam.put("selectedAttachmentDocId", poiId);
            /**--------Removing attachment from content server-------**/
            try {
                Map<String, String> poiFileInfo =
                    (Map<String, String>) (sessionParam.get("ReactPoIFileInfo") == null ? null :
                                           sessionParam.get("ReactPoIFileInfo"));
                if (poiFileInfo != null) {
                    UCMUtil.deleteAttatchment(poiFileInfo.get("contentId"), poiFileInfo.get("dId"));
                }
            } catch (IOException ioe) {
                // TODO: Add catch code
                ioe.printStackTrace();
            } catch (IdcClientException ice) {
                // TODO: Add catch code
                ice.printStackTrace();
            }
            /**-------------------------End Here---------------------**/

            /**--------Resetting POI attachment files------**/
            List lstAtt = new java.util.ArrayList();
            lstAtt.add("ReactPoiName");
            lstAtt.add("ReactPoiExt");
            lstAtt.add("ReactPoiBase64");

            lstAtt.add("a");
            lstAtt.add("b");
            lstAtt.add("c");
            CommonHelper.uploadReset(lstAtt);

            /**-----------------End Here---------------**/

//        }
    }

    public void poaTypeNoValueCl(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
//        if (valueChangeEvent.getNewValue() != null) {
            //            String poiId = String.valueOf(valueChangeEvent.getNewValue());
            //            sessionParam.put("selectedAttachmentDocId", poiId);
            /**--------Removing attachment from content server-------**/
            try {
                Map<String, String> poaFileInfo =
                    (Map<String, String>) (sessionParam.get("ReactPoaFileInfo") == null ? null :
                                           sessionParam.get("ReactPoaFileInfo"));
                if (poaFileInfo != null) {
                    UCMUtil.deleteAttatchment(poaFileInfo.get("contentId"), poaFileInfo.get("dId"));
                }
            } catch (IOException ioe) {
                // TODO: Add catch code
                ioe.printStackTrace();
            } catch (IdcClientException ice) {
                // TODO: Add catch code
                ice.printStackTrace();
            }
            /**-------------------------End Here---------------------**/
            /**--------Resetting POA attachment files------**/
            List lstAtt = new java.util.ArrayList();
            lstAtt.add("ReactPoaName");
            lstAtt.add("ReactPoaExt");
            lstAtt.add("ReactPoaBase64");

            lstAtt.add("d");
            lstAtt.add("e");
            lstAtt.add("f");
            CommonHelper.uploadReset(lstAtt);
            /**-----------------End Here---------------**/
//        }
    }

    public void setPoiDocTypeBinding(RichOutputText poiDocTypeBinding) {
        this.poiDocTypeBinding = poiDocTypeBinding;
    }

    public RichOutputText getPoiDocTypeBinding() {
        return poiDocTypeBinding;
    }
}
