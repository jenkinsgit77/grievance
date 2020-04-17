package com.iocl.lpg.customer.bean.servo;

import com.iocl.lpg.customer.bean.LeadCreation;
import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;

import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import ioclcommonproj.com.iocl.utils.JSONObject;

import java.io.Serializable;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;

import oracle.binding.OperationBinding;

import org.apache.log4j.Logger;

public class FindYourStockistBean implements Serializable {
    @SuppressWarnings("compatibility:-5574585519942258372")
    private static final long serialVersionUID = 1L;
    private static Logger logger ;
    private boolean errorInStockist = false;
    private boolean radioFlag=false;
    private RichSelectOneChoice stateBinding;
    private RichSelectOneChoice districtBinding;
    private String stateErrorMsg = "";
    private String districtErrorMsg = "";
    private String stockistTypeErrorMsg = "";
    private String stockistValidationErrorMsg = "";
    private String stockistBusinessValCode = ""; 

    private RichPopup bindpopUp;
    private RichSelectOneRadio industrialBinding;

    public FindYourStockistBean() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            logger = Logger.getLogger(FindYourStockistBean.class);

        } else {
            logger = Logger.getLogger(FindYourStockistBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }


    public String onClickSearchStockist() {
        String retString = null;
        logger.info("Inside FindYourStockistBean onClickSearchStockist Start");
        try {
            errorInStockist = validateStockist();
            logger.info("errorInStockist value" + errorInStockist);
            if (errorInStockist == true) {
                logger.info("Case1");
                return retString;
            } else {
                java.util.List lstInput = new java.util.ArrayList();
                JSONObject jsonInput = new JSONObject();

                if (industrialBinding.getValue() != null) {
                    logger.info("industrialBinding  "+industrialBinding.getValue());
                    if (industrialBinding.getValue()
                                         .toString()
                                         .equalsIgnoreCase("Industrial")) {
                        //                        jsonInput.put("Type", "Industrial");
                        jsonInput.put("LOB", "104");
                    } else {
                        //                        jsonInput.put("Type", "retail");
                        jsonInput.put("LOB", "105");
                    }
                }
                //          jsonInput.put( "LOB","Stockiest");
                //            jsonInput.put("State","HR002");

                jsonInput.put("State", String.valueOf(stateBinding.getValue()));
                jsonInput.put("District", String.valueOf(districtBinding.getValue()));
                lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("STOCKIST_INFO"));
                lstInput.add(1, jsonInput);
                OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
                ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.SEARCH_STOCKIST);
                ob.execute();
                java.util.List returnList = (java.util.List) ob.getResult();
                JSONObject jsonObject = null;
                if ((returnList != null) && (returnList.get(0) != null) && (returnList.get(0)
                                                                                      .toString()
                                                                                      .equalsIgnoreCase("true"))) {
                    jsonObject = new JSONObject(returnList.get(1).toString());
                    logger.info("returnList" + returnList);
                    CommonHelper.runJavaScript("dataTableInvoke()");
                    RichPopup.PopupHints hints = new RichPopup.PopupHints();
                    getBindpopUp().show(hints);
                    stockistBusinessValCode = "";
                    retString = null;

                } else if ((returnList != null && returnList.get(0) != null) &&(returnList.get(0).toString()
                                 .equalsIgnoreCase("false") && returnList.size() > 1)) {
                    jsonObject = new JSONObject(returnList.get(1).toString());

                    stockistBusinessValCode = 
                        jsonObject.has(EPICConstant.ERROR_CODE) ? jsonObject.getString(EPICConstant.ERROR_CODE) : null;
                    stockistValidationErrorMsg =
                        jsonObject.has(EPICConstant.ERRORMESSAGE) ?
                        jsonObject.get(EPICConstant.ERRORMESSAGE).toString() : null;

                    if (stockistBusinessValCode != null &&
                        stockistBusinessValCode.equalsIgnoreCase(EPICConstant.SBL100)) {
                        logger.info("Business Validation Issue SBL-100 ErrorCode Received");
                        return null;
                    } else {
                        retString = "ERROR";
                        logger.info("Some Error in WebService Response");
                    }

                }
            }
        } catch (Exception e) {
            retString = EPICConstant.ERROR;
            return retString;

        }


        return retString;
    }
    
    
    
    public Boolean validateStockist() {
        logger.info("Inside Start validateStockist");

        if (CustomerValidation.isNull(stateBinding.getValue())) {
            stateErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_STATE");
            stateBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInStockist = true;
        }
        else {
            stateBinding.setStyleClass("");
            stateErrorMsg = "";
        }
        if (CustomerValidation.isNull(districtBinding.getValue())) {
            districtErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_DISTRICT");
            districtBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInStockist = true;
        }
        else {
            districtBinding.setStyleClass("");
            districtErrorMsg = "";
        }
        
        if (CustomerValidation.isNull(districtBinding.getValue())) {
            districtErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_DISTRICT");
            districtBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInStockist = true;
        }
        else {
            districtBinding.setStyleClass("");
            districtErrorMsg = "";
        }
        
        logger.info("IndustrialBinding"+industrialBinding.getValue());
        if (industrialBinding.getValue()==null) {
            stockistTypeErrorMsg = CommonHelper.getValueFromRsBundle("STOCKIST_TYPE");
            errorInStockist = true;
        }
        else {
            stockistTypeErrorMsg = "";
        }
        
        


       

        logger.info("errorInStockist"+ errorInStockist);
        logger.info("Inside End ValidateLeadPersonalDetails");

        return errorInStockist;
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

    public void setRadioFlag(boolean radioFlag) {
        this.radioFlag = radioFlag;
    }

    public boolean isRadioFlag() {
        return radioFlag;
    }


    public void setStockistTypeErrorMsg(String stockistTypeErrorMsg) {
        this.stockistTypeErrorMsg = stockistTypeErrorMsg;
    }

    public String getStockistTypeErrorMsg() {
        return stockistTypeErrorMsg;
    }

    public void setBindpopUp(RichPopup bindpopUp) {
        this.bindpopUp = bindpopUp;
    }

    public RichPopup getBindpopUp() {
        return bindpopUp;
    }

    public void setStockistValidationErrorMsg(String stockistValidationErrorMsg) {
        this.stockistValidationErrorMsg = stockistValidationErrorMsg;
    }

    public String getStockistValidationErrorMsg() {
        return stockistValidationErrorMsg;
    }

    public void setStockistBusinessValCode(String stockistBusinessValCode) {
        this.stockistBusinessValCode = stockistBusinessValCode;
    }

    public String getStockistBusinessValCode() {
        return stockistBusinessValCode;
    }

    public void stockistTypeVCL(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if(valueChangeEvent.getNewValue()!=null){
            CommonHelper.setEL("#{bindings.Col2.inputValue}", null);
            CommonHelper.setEL("#{bindings.Col3.inputValue}", null);
        }
    }

    public void setIndustrialBinding(RichSelectOneRadio industrialBinding) {
        this.industrialBinding = industrialBinding;
    }

    public RichSelectOneRadio getIndustrialBinding() {
        return industrialBinding;
    }
}
