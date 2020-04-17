package com.iocl.lpg.customer.bean.ContactUs;


import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;

import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.util.ArrayList;

import javax.faces.event.ActionEvent;

import javax.faces.model.SelectItem;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.binding.OperationBinding;

import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONObject;

public class Lead {
    private static Logger logger = Logger.getLogger(Lead.class);
    private RichInputText firstNameBinding;
    private String firstNameErrorMsg = "";
    private String lastNameErrorMsg = "";
    private String mobileNoErrorMsg="";
    private String address1ErrorMsg="";
    private String address2ErrorMsg="";
    private String pincodeErrorMsg="";
    private String cityErrorMsg="";
    private String lineofBussErrorMsg="";
    private boolean errorInLead = false;
    private RichInputText lastNameBinding;
    private RichInputText mobileNoBinding;
    private RichInputText address1Binding;
    private RichInputText address2Binding;
    private RichInputText cityBinding;
    private RichInputText pincodeBinding;
    private String multilineText = "Location to set-up a\nfuel station";
    private RichInputText middleNameBinding;
    private RichInputText remarksBinding;
    private ArrayList<SelectItem> _lovLineofbuss = new ArrayList<SelectItem>();
    private RichSelectOneChoice lineofbussbinding;
    private RichSelectBooleanCheckbox landbankbinding;
    java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();

    Lead() {
    }

    public Boolean ValidateLeadPersonalDetails() {
        logger.info("Inside Start ValidateLeadPersonalDetails");

        if (CustomerValidation.isNull(firstNameBinding.getValue())) {
            firstNameErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_FIRST_NAME");
            firstNameBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInLead = true;
        }
        if (CustomerValidation.isNull(lastNameBinding.getValue())) {
            lastNameErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_LAST_NAME");
            lastNameBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInLead = true;
        }
        if (CustomerValidation.isValidOrNullVal(mobileNoBinding.getValue(), EPICConstant.MOBILE_EXPRESSION) ==
            EPICConstant.INVALID_CASE) {
            mobileNoErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_VALID_MOBILENO");
            mobileNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInLead = true;
        } else if (CustomerValidation.isValidOrNullVal(mobileNoBinding.getValue(), EPICConstant.MOBILE_EXPRESSION) ==
                   EPICConstant.BLANK_CASE) {
            mobileNoErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_MOBILE_NO");
            mobileNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInLead = true;
        }
            if (CustomerValidation.isNull(address1Binding.getValue())) {
            address1ErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_ADDRESS_1");
            address1Binding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInLead = true;
        }
       
        if (CustomerValidation.isNull(cityBinding.getValue())) {
            cityErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_CITY");
            cityBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInLead = true;
        }
        
        
        if (CustomerValidation.isNull(lineofbussbinding.getValue())) {
            lineofBussErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_LINE_BUSSINESS");
            lineofbussbinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInLead = true;
        }

        

        if (CustomerValidation.isNull(pincodeBinding.getValue())) {
            errorInLead = true;
            pincodeErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_PINCODE");
            pincodeBinding.setStyleClass(EPICConstant.ERROR_CLASS);
        } else {

            int valid =
                CustomerValidation.isValidOrNullVal(pincodeBinding.getValue(),
                                               CommonHelper.getValueFromRsBundle("ZIP_CODE_VALIDATION"));
            if (valid != 0) {
                errorInLead = true;
                pincodeErrorMsg = CommonHelper.getValueFromRsBundle("PLS_ENTR_VALID_PIN");
                pincodeBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            }
        }


       

        logger.info("errorInLead " + errorInLead);
        logger.info("Inside End ValidateLeadPersonalDetails");

        return errorInLead;
    }
    
    
    public String onClickLeadSubmit() {
        String retString = null;
        logger.info("Inside Lead Bean onClickLeadSubmit Start");
        try{
            resetLeadValidation();
            errorInLead = ValidateLeadPersonalDetails();
        if (errorInLead == true) {
            logger.info("Case1");
            return null;
        }
        else {
            if(errorInLead == false) {
                    logger.info("Land Bank is Eligible"+String.valueOf(landbankbinding.getValue()).trim());
                    java.util.List lstInput = new java.util.ArrayList();
                    JSONObject jsonInput = new JSONObject();
                    jsonInput.put("FirstName", String.valueOf(firstNameBinding.getValue()).trim());
                    jsonInput.put("MiddleName", String.valueOf(middleNameBinding.getValue()).equalsIgnoreCase("null")?"" :String.valueOf(middleNameBinding.getValue()).trim());
                    jsonInput.put("LastName", String.valueOf(lastNameBinding.getValue()).trim());
                    jsonInput.put(EPICConstant.MOBILE_NUMBER, String.valueOf(mobileNoBinding.getValue()).trim());
                    jsonInput.put("Address1", String.valueOf(address1Binding.getValue()).trim());
                    jsonInput.put("Address2", String.valueOf(address2Binding.getValue()).equalsIgnoreCase("null")?"" :String.valueOf(address2Binding.getValue()).trim());
                    jsonInput.put("ZipCode", String.valueOf(pincodeBinding.getValue()).trim());
                    jsonInput.put("City", String.valueOf(cityBinding.getValue()).trim());
                    jsonInput.put("Remarks", String.valueOf(remarksBinding.getValue()).trim());
                    jsonInput.put("Landbank", String.valueOf(landbankbinding.getValue()).equalsIgnoreCase("true")?"Yes":"No");
                    jsonInput.put("LineOfBusiness", String.valueOf(lineofbussbinding.getValue()).trim());
                    jsonInput.put("Country", "India");
                    
                    lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("LEAD_CREATION"));
                    lstInput.add(1, jsonInput);
                    OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                    ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
                    ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.LEAD_CREATION);
                    ob.execute();
                    java.util.List returnList = (java.util.List) ob.getResult();
                    JSONObject jsonObject = new JSONObject(returnList.get(1).toString());
                    logger.info("returnList" + returnList); 
                    if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0).toString().equalsIgnoreCase("TRUE")){
                           
                            if(String.valueOf(jsonObject.get("ErrorCode")).equalsIgnoreCase("0"))
                            {
                            retString="toConfirm";
                            logger.info("ReturnList Value at 0 index is " + returnList.get(0));
                          
                            logger.info("lead is Successfull. Move to Confirmation Page");
                            pageflowParam.put("confMessage",
                                              CommonHelper.getValueFromRsBundle("YOUR_SERVICE_REQUEST_FOR_LEAD"));
                            pageflowParam.put("refNumber", jsonObject.isNull("Id") ? null : jsonObject.get("Id"));
                            }
                            else {
                                retString = EPICConstant.ERROR;
                                return retString;
                            }
                        
                        }
                    else {
                        retString = EPICConstant.ERROR;
                        return retString;
                    }
                   

                


                }
                else {
                retString = EPICConstant.ERROR;
                return retString;
                }


                }


                
            
                    }
        
        
        
        catch(Exception e) {
            retString = EPICConstant.ERROR;
            return retString;
            
        }
        
        
        return retString;
    }
    
    
    public void resetLeadValidation() {

        
        firstNameErrorMsg = "";
        lastNameErrorMsg = "";
        address1ErrorMsg = "";
        address2ErrorMsg = "";
        cityErrorMsg = "";
        pincodeErrorMsg = "";
        mobileNoErrorMsg = "";
        lineofBussErrorMsg="";
               
        firstNameBinding.setStyleClass("");
        lastNameBinding.setStyleClass("");
        cityBinding.setStyleClass("");
        pincodeBinding.setStyleClass("");
        mobileNoBinding.setStyleClass("");
        address1Binding.setStyleClass("");
        address2Binding.setStyleClass("");
        lineofbussbinding.setStyleClass("");
        logger.info("End of resetLeasValidation Method");
    }

    public void setFirstNameBinding(RichInputText firstNameBinding) {
        this.firstNameBinding = firstNameBinding;
    }

    public RichInputText getFirstNameBinding() {
        return firstNameBinding;
    }

    public void setFirstNameErrorMsg(String firstNameErrorMsg) {
        this.firstNameErrorMsg = firstNameErrorMsg;
    }

    public String getFirstNameErrorMsg() {
        return firstNameErrorMsg;
    }

    public void setErrorInLead(boolean errorInLead) {
        this.errorInLead = errorInLead;
    }

    public boolean isErrorInLead() {
        return errorInLead;
    }

    public void setLastNameBinding(RichInputText lastNameBinding) {
        this.lastNameBinding = lastNameBinding;
    }

    public RichInputText getLastNameBinding() {
        return lastNameBinding;
    }

    public void setLastNameErrorMsg(String lastNameErrorMsg) {
        this.lastNameErrorMsg = lastNameErrorMsg;
    }

    public String getLastNameErrorMsg() {
        return lastNameErrorMsg;
    }

    public void setMobileNoBinding(RichInputText mobileNoBinding) {
        this.mobileNoBinding = mobileNoBinding;
    }

    public RichInputText getMobileNoBinding() {
        return mobileNoBinding;
    }

    public void setMobileNoErrorMsg(String mobileNoErrorMsg) {
        this.mobileNoErrorMsg = mobileNoErrorMsg;
    }

    public String getMobileNoErrorMsg() {
        return mobileNoErrorMsg;
    }

    public void setAddress1Binding(RichInputText address1Binding) {
        this.address1Binding = address1Binding;
    }

    public RichInputText getAddress1Binding() {
        return address1Binding;
    }

    public void setAddress1ErrorMsg(String address1ErrorMsg) {
        this.address1ErrorMsg = address1ErrorMsg;
    }

    public String getAddress1ErrorMsg() {
        return address1ErrorMsg;
    }

    public void setAddress2Binding(RichInputText address2Binding) {
        this.address2Binding = address2Binding;
    }

    public RichInputText getAddress2Binding() {
        return address2Binding;
    }

    public void setAddress2ErrorMsg(String address2ErrorMsg) {
        this.address2ErrorMsg = address2ErrorMsg;
    }

    public String getAddress2ErrorMsg() {
        return address2ErrorMsg;
    }

    public void setCityBinding(RichInputText cityBinding) {
        this.cityBinding = cityBinding;
    }

    public RichInputText getCityBinding() {
        return cityBinding;
    }

    public void setCityErrorMsg(String cityErrorMsg) {
        this.cityErrorMsg = cityErrorMsg;
    }

    public String getCityErrorMsg() {
        return cityErrorMsg;
    }

    public void setPincodeBinding(RichInputText pincodeBinding) {
        this.pincodeBinding = pincodeBinding;
    }

    public RichInputText getPincodeBinding() {
        return pincodeBinding;
    }

    public void setPincodeErrorMsg(String pincodeErrorMsg) {
        this.pincodeErrorMsg = pincodeErrorMsg;
    }

    public String getPincodeErrorMsg() {
        return pincodeErrorMsg;
    }

    public void onClickLeadReset(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside onClickLeadReset Start");
        resetLeadValidation();
        }
    public String getMultilineText() {
        return multilineText;
    }

    public void setMiddleNameBinding(RichInputText middleNameBinding) {
        this.middleNameBinding = middleNameBinding;
    }

    public RichInputText getMiddleNameBinding() {
        return middleNameBinding;
    }

    public void setRemarksBinding(RichInputText remarksBinding) {
        this.remarksBinding = remarksBinding;
    }

    public RichInputText getRemarksBinding() {
        return remarksBinding;
    }
    
    
    public String getLob() {
        
        return null;
    }
    
    
//    public static void main(String arg[]) {
//        BindingContext ctx = BindingContext.getCurrent();
//        DCBindingContainer bc = (DCBindingContainer)ctx.getCurrentBindingsEntry();
//        DCIteratorBinding iterator = bc.findIteratorBinding("ViewObjectIterator");
//        Row r = iterator.getCurrentRow();
//        String empNameValue = (String)r.getAttribute("EmpName");
//    }

    public void setLovLineofbuss(ArrayList<SelectItem> _lovLineofbuss) {
        this._lovLineofbuss = _lovLineofbuss;
    }

    public ArrayList<SelectItem> getLovLineofbuss() {
        return _lovLineofbuss;
    }

    public void setLineofbussbinding(RichSelectOneChoice lineofbussbinding) {
        this.lineofbussbinding = lineofbussbinding;
    }

    public RichSelectOneChoice getLineofbussbinding() {
        return lineofbussbinding;
    }

    public void setLandbankbinding(RichSelectBooleanCheckbox landbankbinding) {
        this.landbankbinding = landbankbinding;
    }

    public RichSelectBooleanCheckbox getLandbankbinding() {
        return landbankbinding;
    }

    public void setLineofBussErrorMsg(String lineofBussErrorMsg) {
        this.lineofBussErrorMsg = lineofBussErrorMsg;
    }

    public String getLineofBussErrorMsg() {
        return lineofBussErrorMsg;
    }
}

