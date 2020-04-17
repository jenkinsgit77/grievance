package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import ioclcommonproj.com.iocl.utils.JSONObject;

import java.util.ArrayList;

import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.binding.OperationBinding;

import org.apache.log4j.Logger;

public class CustomerSubscription {
    private static Logger logger = Logger.getLogger(CustomerSubscription.class);
    private boolean errorInsubscribe = false;
    private RichInputText bindFirstName;
    private String firstNameErrorMsg = "";
    private String lastNameErrorMsg="";
    private String emailErrorMsg="";
    private String prefrenceErrorMsg="";
    private String mobileNoErrorMsg="";
    private List<SelectItem> preferenceItemList;
    private List preferenceList;

    public void setPreferenceItemList(List<SelectItem> preferenceItemList) {
        this.preferenceItemList = preferenceItemList;
    }

    public List<SelectItem> getPreferenceItemList() {
        return preferenceItemList;
    }

    public void setPreferenceList(List preferenceList) {
        this.preferenceList = preferenceList;
    }

    public List getPreferenceList() {
        return preferenceList;
    }
    java.util.Map param = ADFContext.getCurrent().getPageFlowScope();

    public void setMobileNoErrorMsg(String mobileNoErrorMsg) {
        this.mobileNoErrorMsg = mobileNoErrorMsg;
    }

    public String getMobileNoErrorMsg() {
        return mobileNoErrorMsg;
    }
    private RichInputText emailIdbinding;
    private RichInputText mobileNoBinding;

    public void setLastNameErrorMsg(String lastNameErrorMsg) {
        this.lastNameErrorMsg = lastNameErrorMsg;
    }

    public String getLastNameErrorMsg() {
        return lastNameErrorMsg;
    }
    private RichInputText lastNameBinding;

    public void setFirstNameErrorMsg(String firstNameErrorMsg) {
        this.firstNameErrorMsg = firstNameErrorMsg;
    }

    public String getFirstNameErrorMsg() {
        return firstNameErrorMsg;
    }

    public CustomerSubscription() {
        preferenceItemList = new ArrayList<SelectItem>();
        preferenceItemList.add(new SelectItem("Email", "Email"));
        preferenceItemList.add(new SelectItem("SMS", "SMS"));
        preferenceItemList.add(new SelectItem("Both", "Both"));
        preferenceList = new ArrayList();
    }

    public void setBindFirstName(RichInputText bindFirstName) {
        this.bindFirstName = bindFirstName;
    }

    public RichInputText getBindFirstName() {
        return bindFirstName;
    }

    public void setLastNameBinding(RichInputText lastNameBinding) {
        this.lastNameBinding = lastNameBinding;
    }

    public RichInputText getLastNameBinding() {
        return lastNameBinding;
    }

    public void setEmailIdbinding(RichInputText emailIdbinding) {
        this.emailIdbinding = emailIdbinding;
    }

    public RichInputText getEmailIdbinding() {
        return emailIdbinding;
    }

    public void setEmailErrorMsg(String emailErrorMsg) {
        this.emailErrorMsg = emailErrorMsg;
    }

    public String getEmailErrorMsg() {
        return emailErrorMsg;
    }

    public void setMobileNoBinding(RichInputText mobileNoBinding) {
        this.mobileNoBinding = mobileNoBinding;
    }

    public RichInputText getMobileNoBinding() {
        return mobileNoBinding;
    }
    public void onChangeValue(ValueChangeEvent valueChangeEvent)throws Exception {
        // Add event code here...
        
        List<String> objArr = (ArrayList<String>)valueChangeEvent.getNewValue();
        System.out.println("array"+objArr.size());
        
        if(objArr.size()>1) {
            prefrenceErrorMsg = "Please select one preferences.";  
            errorInsubscribe=true;
          }
        else {
             System.out.println("Obj"+objArr.get(0));
             param.put("Preference", objArr.get(0));
             errorInsubscribe=false;
        }
    }  
    
    public String onClickscbscribe() {
        String retString = null;
        logger.info("Inside onClickUnscbscribe Start");
        try{
            resetInsubscribeValidation();
            errorInsubscribe = ValidateSubscribeDetails();
        if (errorInsubscribe == true) {
            logger.info("Case1");
            return null;
        }
        else {
            if(errorInsubscribe == false) {
                    java.util.List lstInput = new java.util.ArrayList();
                    JSONObject jsonInput = new JSONObject();
                    jsonInput.put(EPICConstant.MOBILE_NUMBER, String.valueOf(mobileNoBinding.getValue()).trim());
                    jsonInput.put(EPICConstant.EMAIL, String.valueOf(emailIdbinding.getValue()).trim());
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
    
    public void resetInsubscribeValidation() {
        emailErrorMsg="";
        mobileNoErrorMsg = "";
        firstNameErrorMsg="";
        lastNameErrorMsg="";
        bindFirstName.setStyleClass("");
        lastNameBinding.setStyleClass("");
        mobileNoBinding.setStyleClass("");
        emailIdbinding.setStyleClass("");
        logger.info("End of resetInsubscribeValidation Method");
    }
    
    public Boolean ValidateSubscribeDetails() {
        
        resetInsubscribeValidation();
       
        if (CustomerValidation.isNull(bindFirstName.getValue())) {
            firstNameErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_FIRST_NAME");
            bindFirstName.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInsubscribe = true;
        }
        if (CustomerValidation.isNull(lastNameBinding.getValue())) {
            lastNameErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_LAST_NAME");
            lastNameBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInsubscribe = true;
        }
        if (CustomerValidation.isValidOrNullVal(emailIdbinding.getValue(), EPICConstant.EMAIL_EXPRESSSION) ==
            EPICConstant.INVALID_CASE) {
            emailErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_VALID_EMAIL_ID");
            emailIdbinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInsubscribe = true;
        } else if (CustomerValidation.isValidOrNullVal(emailIdbinding.getValue(), EPICConstant.EMAIL_EXPRESSSION) ==
                   EPICConstant.BLANK_CASE) {
            
            emailErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_EMAIL_ID");
            emailIdbinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInsubscribe = true;
        }
       
        
        if (CustomerValidation.isValidOrNullVal(mobileNoBinding.getValue(), EPICConstant.MOBILE_EXPRESSION) ==
            EPICConstant.INVALID_CASE) {
            mobileNoErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_VALID_MOBILENO");
            mobileNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInsubscribe = true;
        } else if (CustomerValidation.isValidOrNullVal(mobileNoBinding.getValue(), EPICConstant.MOBILE_EXPRESSION) ==
                   EPICConstant.BLANK_CASE) {
            mobileNoErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_MOBILE_NO");
            mobileNoBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            System.out.println("Preference"+param.get("Preference"));
            errorInsubscribe = true;
        }
       
        if (CustomerValidation.isNull(param.get("Preference"))) {
            prefrenceErrorMsg = "Please select one preferences.";
            errorInsubscribe = true;
        } 
         
                    
                    
           
         
        
        
        

        

        


       

        logger.info("errorInLead " + errorInsubscribe);
        logger.info("Inside End ValidateLeadPersonalDetails");

        return errorInsubscribe;
    }


    public void onClickReset(ActionEvent actionEvent) {
        resetInsubscribeValidation();
    }

    public void setPrefrenceErrorMsg(String prefrenceErrorMsg) {
        this.prefrenceErrorMsg = prefrenceErrorMsg;
    }

    public String getPrefrenceErrorMsg() {
        return prefrenceErrorMsg;
    }
}
