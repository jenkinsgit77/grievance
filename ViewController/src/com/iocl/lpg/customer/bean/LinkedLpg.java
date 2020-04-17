package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;

import com.iocl.lpg.customer.utils.EPICConstant;

import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.IOException;
import java.io.Serializable;

import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.adf.view.rich.component.rich.output.RichOutputFormatted;

import oracle.binding.OperationBinding;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.event.PopupCanceledEvent;

public class LinkedLpg implements Serializable{
    private Boolean linkedLpgError = false;
    private Boolean lpgIdError = false;
    private Boolean svNumError = false;
    private static Pattern aadhaarPattern = Pattern.compile("^[2-9]{1}[0-9]{11}$");
    String mobileNumber=null;
    String emailAddress=null;
    private StringBuffer strCons;
    private String lpgIdErrorMsg;
    private String svNumErrorMsg;
    String errorCode=null;
    String errorMessage=null; 
    private  String errorOTP = null;
    private String errorEmail=null;
    private RichInputText bindBox1;
    private RichInputText bindBox2;
    private RichInputText bindBox3;
    private RichInputText bindBox4;
    private RichInputText bindEmailotp1;
    private RichInputText bindEmailotp2;
    private RichInputText bindEmailotp3;
    private RichInputText bindEmailotp4;
    private RichInputText bindmobileotp1;
    private RichInputText bindMobileotp2;
    private RichInputText bindMobileotp3;
    private RichInputText bindMobileotp4;
    java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
    
    private RichInputText bindSVNumber;
    private RichInputText bindAadharNumber;
    private RichInputText bindCashMemoNumber;
    private static Logger logger ;
    private RichOutputFormatted ucmIDbinding;
    private RichPopup bindpopUp;

    public LinkedLpg() {
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            logger = Logger.getLogger(LinkedLpg.class);

        } else {
            logger = Logger.getLogger(LinkedLpg.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }

    public String onClickSubmit() {
        // Add event code here...
        
        String retString=null;
        String mobNumber=null;
        String emailAdd=null;
       
        try {
            resetError();
            linkedLpgError = checkGiveUpSubValidation();
            logger.info("linkedLpgError"+linkedLpgError);
            if (linkedLpgError) {
                return null;
            } else {
                
                if (lpgIdError == false) {
                    java.util.List lstInput = new java.util.ArrayList();
                    JSONObject jsonInput = new JSONObject();
                    jsonInput.put(EPICConstant.SV_NUMBER, String.valueOf(bindSVNumber.getValue()).equalsIgnoreCase("null")?"":String.valueOf(bindSVNumber.getValue()).trim());
                    jsonInput.put(EPICConstant.AADHAR_NUM, String.valueOf(bindAadharNumber.getValue()).equalsIgnoreCase("null")?"" :String.valueOf(bindAadharNumber.getValue()).trim());
                    jsonInput.put(EPICConstant.CASH_MEMO_NUM, String.valueOf(bindCashMemoNumber.getValue()).equalsIgnoreCase("null")?"" :String.valueOf(bindCashMemoNumber.getValue()).trim());
                    jsonInput.put(EPICConstant.CONSUMERID,String.valueOf(param.get("ConsumerId")));
                    lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("GET_CUSTOMER_DETAILS"));
                    lstInput.add(1, jsonInput);
                    OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                    ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
                    ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.GET_CUSTOMER_DETAILS);
                    ob.execute();
                    java.util.List returnList = (java.util.List) ob.getResult();
                    
                    
                    logger.info("returnList"+returnList);
                    
                    if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0).toString().equalsIgnoreCase("TRUE"))
                    {
                    JSONObject jsonObject =new JSONObject( returnList.get(1).toString());
                       if(String.valueOf(jsonObject.get("ErrorCode")).equalsIgnoreCase("0")) {
                       /***/ Boolean mobMatched=false;    
                             JSONArray listofMobileArr =
                                 jsonObject.isNull("ListOfPhone") ? null :
                                 jsonObject.getJSONArray("ListOfPhone");
                             if (listofMobileArr != null) {
                                 for (int listid = 0; listid < listofMobileArr.length(); ++listid) {
                                     JSONObject listIdAr = listofMobileArr.getJSONObject(listid);
                                     mobNumber= String.valueOf(listIdAr.get("MobileNumber"));
                                     if(mobNumber.equalsIgnoreCase("null")){
                                         logger.info("Mobile num null");
//                                         String finalString= CommonHelper.getValueFromRsBundle("LINKING_MOB_NMB");                                
//                                         String finalString1= CommonHelper.getValueFromRsBundle("LINKING_MOB_NMB1")+ CommonHelper.getValueFromRsBundle("LINKING_MOB_NMB2");
                                         param.put("UCMId", String.valueOf(jsonObject.get("UCMId")));
                                         param.put("RelUCMId", String.valueOf(jsonObject.get("RelUCMId")));
//                                         param.put("finalStringKey", finalString);
//                                         param.put("finalStringKey1", finalString1);
                                         
                                         retString = "tonotfoundMobile";
                                         break;
                                     }
                                     else if(!mobNumber.equalsIgnoreCase(EPICConstant.NULL) && mobNumber.equalsIgnoreCase(String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerContactNumber}")))){
                                         //CommonHelper.sendOTP(mobNumber);
                                         logger.info("Checking Mobile num matched to cust contact num");
                                         retString = "toaccountDetails";
                                         mobMatched=true;
                                         mobileNumber=maskedGenericNumber(mobNumber);// for hard stop
                                         break;
                                     }
                                 }
                             }
                            
                  
                            
                            if(!mobMatched && !mobNumber.equalsIgnoreCase("null") && !mobNumber.equalsIgnoreCase(String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerContactNumber}")))){
//                                String finalString= CommonHelper.getValueFromRsBundle("LINKING_MOB_NM") +"("+param.get("maskedMobileNumber")+")"+
//                                CommonHelper.getValueFromRsBundle("LINKING_MOB_NM1")+"("+maskedGenericNumber(String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerContactNumber}")))+").";
//                                String finalString1= CommonHelper.getValueFromRsBundle("LINKING_MOB_NM2") + CommonHelper.getValueFromRsBundle("LINKING_MOB_NM3");
                                param.put("UCMId", String.valueOf(jsonObject.get("UCMId")));
                                param.put("RelUCMId", String.valueOf(jsonObject.get("RelUCMId")));
//                                param.put("finalStringKey", finalString);
//                                param.put("finalStringKey1", finalString1);
                                 
                                retString = "tonotfoundMobile"; 
                                }

                         }
                       return     retString;
                    }
                    else if((returnList != null) && (returnList.get(0) != null) && returnList.get(0).toString().equalsIgnoreCase("FALSE")) {
                        
                        
                        logger.info("Inside else condition----------------");
                        JSONObject jsonObject =new JSONObject( returnList.get(1).toString());
                        if(String.valueOf(jsonObject.get("ErrorCode")).equalsIgnoreCase(EPICConstant.SBL100)) {
                            
                            retString = "toInformationNot";
                            
                        }
                        
                        return retString; 
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
        
    }
    
    
    public boolean checkGiveUpSubValidation() {
        
        lpgIdError = false;
        svNumError=false;
        
       
        
     
       
        
        
        if ((lpgIdError == false) && (CustomerValidation.isNull(bindAadharNumber.getValue())) && 
            (CustomerValidation.isNull(bindSVNumber.getValue())) && (CustomerValidation.isNull(bindCashMemoNumber.getValue())))
        {
            lpgIdError = true;
            svNumErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_SV_NUM");
            logger.info("lpgIdErrorMsg"+lpgIdErrorMsg);
        }
        
        
        if ((lpgIdError == false) && 
            (CustomerValidation.isNull(bindSVNumber.getValue())) && (CustomerValidation.isNull(bindCashMemoNumber.getValue())))
        {
         if(!(CustomerValidation.isNull(bindAadharNumber.getValue())))   {
             boolean result = isValidName(String.valueOf(bindAadharNumber.getValue()));
             if (result == false) {
                 lpgIdError = true;
                 svNumErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_CORRECT_AADHAAR_NUMBER");
             }
         }
        }
        
        
        

        
        
        return lpgIdError;
    }
     


    public void setBindBox1(RichInputText bindBox1) {
        this.bindBox1 = bindBox1;
    }

    public RichInputText getBindBox1() {
        return bindBox1;
    }

    public void setBindBox2(RichInputText bindBox2) {
        this.bindBox2 = bindBox2;
    }

    public RichInputText getBindBox2() {
        return bindBox2;
    }

    public void setBindBox3(RichInputText bindBox3) {
        this.bindBox3 = bindBox3;
    }

    public RichInputText getBindBox3() {
        return bindBox3;
    }

    public void setBindBox4(RichInputText bindBox4) {
        this.bindBox4 = bindBox4;
    }

    public RichInputText getBindBox4() {
        return bindBox4;
    }
    
    private void resetError() {
     
        lpgIdErrorMsg = "";
        svNumErrorMsg= "";
        
    }

    public void setLpgIdErrorMsg(String lpgIdErrorMsg) {
        this.lpgIdErrorMsg = lpgIdErrorMsg;
    }

    public String getLpgIdErrorMsg() {
        return lpgIdErrorMsg;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String verifyEmailOtp() {
            String retString=null;
            java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
            if ((bindEmailotp1 == null || bindEmailotp1.getValue() == null || StringUtils.isBlank(bindEmailotp1.getValue().toString())) ||

                (bindEmailotp2 == null || bindEmailotp2.getValue() == null || StringUtils.isBlank(bindEmailotp2.getValue().toString())) ||

                (bindEmailotp3 == null || bindEmailotp3.getValue() == null || StringUtils.isBlank(bindEmailotp3.getValue().toString())) ||

                (bindEmailotp4 == null || bindEmailotp4.getValue() == null || StringUtils.isBlank(bindEmailotp4.getValue().toString()))) {
                errorEmail = CommonHelper.getValueFromRsBundle("PLEASE_GIVE_OTP");
                return null;
            }
         
         
        try {
            if (bindEmailotp1 != null && bindEmailotp2 != null && bindEmailotp3 != null && bindEmailotp4 != null) {
                if (bindEmailotp1.getValue() != null && bindEmailotp2.getValue() != null && bindEmailotp3.getValue() != null && bindEmailotp4.getValue() != null) {
            
                    String email =  String.valueOf(param.get("EmailAdd"));
                    String valFromUser =
                        bindEmailotp1.getValue().toString() + bindEmailotp2.getValue().toString() + bindEmailotp3.getValue().toString() +
                        bindEmailotp4.getValue().toString();
                    OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                    java.util.List lstInput = new java.util.ArrayList();
                    lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.VALIDATE_EMAILOTP));
                    JSONObject jsonInput = new JSONObject();
                        jsonInput.put(EPICConstant.EmailAddress, email);
                        jsonInput.put(EPICConstant.EMAIL_OTP, valFromUser);
                   
                    lstInput.add(1, jsonInput);
                    java.util.Map params = ob.getParamsMap();
                    params.put(EPICConstant.SERVICELIST, lstInput);
                    params.put(EPICConstant.SERVICEMETHOD, EPICConstant.VALIDATE_EMAILOTP);
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
                                retString = "toaccountDetails";
                              }
                            else {
                                 errorEmail = CommonHelper.getValueFromRsBundle("PLS_ETR_CORT_OTP");
                                 retString=null;
                             }} }}}
            
            return retString;
            }
         
         
         
         
         
         
         
         
         
         
         
         
         
         
        catch(Exception e) {
            return null;
        }
         
    }
          

    public void setBindEmailotp1(RichInputText bindEmailotp1) {
        this.bindEmailotp1 = bindEmailotp1;
    }

    public RichInputText getBindEmailotp1() {
        return bindEmailotp1;
    }

    public void setBindEmailotp2(RichInputText bindEmailotp2) {
        this.bindEmailotp2 = bindEmailotp2;
    }

    public RichInputText getBindEmailotp2() {
        return bindEmailotp2;
    }

    public void setBindEmailotp3(RichInputText bindEmailotp3) {
        this.bindEmailotp3 = bindEmailotp3;
    }

    public RichInputText getBindEmailotp3() {
        return bindEmailotp3;
    }

    public void setBindEmailotp4(RichInputText bindEmailotp4) {
        this.bindEmailotp4 = bindEmailotp4;
    }

    public RichInputText getBindEmailotp4() {
        return bindEmailotp4;
    }

    public void setBindmobileotp1(RichInputText bindmobileotp1) {
        this.bindmobileotp1 = bindmobileotp1;
    }

    public RichInputText getBindmobileotp1() {
        return bindmobileotp1;
    }

    public void setBindMobileotp2(RichInputText bindMobileotp2) {
        this.bindMobileotp2 = bindMobileotp2;
    }

    public RichInputText getBindMobileotp2() {
        return bindMobileotp2;
    }

    public void setBindMobileotp3(RichInputText bindMobileotp3) {
        this.bindMobileotp3 = bindMobileotp3;
    }

    public RichInputText getBindMobileotp3() {
        return bindMobileotp3;
    }

    public void setBindMobileotp4(RichInputText bindMobileotp4) {
        this.bindMobileotp4 = bindMobileotp4;
    }

    public RichInputText getBindMobileotp4() {
        return bindMobileotp4;
    }

    public String verifyMobileotp() {
        String retString = null;
        
       
        if ((bindmobileotp1 == null || bindmobileotp1.getValue() == null || StringUtils.isBlank(bindmobileotp1.getValue().toString())) ||

            (bindMobileotp2 == null || bindMobileotp2.getValue() == null || StringUtils.isBlank(bindMobileotp2.getValue().toString())) ||

            (bindMobileotp3 == null || bindMobileotp3.getValue() == null || StringUtils.isBlank(bindMobileotp3.getValue().toString())) ||

            (bindMobileotp4 == null || bindMobileotp4.getValue() == null || StringUtils.isBlank(bindMobileotp4.getValue().toString()))) {
            errorOTP = CommonHelper.getValueFromRsBundle("PLEASE_GIVE_OTP");
            return null;
        }
        try {
            if (bindmobileotp1 != null && bindMobileotp2 != null && bindMobileotp3 != null && bindMobileotp4 != null) {
                if (bindmobileotp1.getValue() != null && bindMobileotp2.getValue() != null && bindMobileotp3.getValue() != null &&
                    bindMobileotp4.getValue() != null) {
                    String mob =   String.valueOf(param.get("MobNum"));
                    String valFromUser =
                        bindmobileotp1.getValue().toString() + bindMobileotp2.getValue().toString() + bindMobileotp3.getValue().toString() +
                        bindMobileotp4.getValue().toString();
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
                                retString = "toaccountDetails";
                              }
                            else {
                                 errorOTP = CommonHelper.getValueFromRsBundle("PLS_ETR_CORT_OTP");
                             }
                        }
                    }
                    }
        }
        }
        catch(Exception e) {
                       retString = "ERROR";
                       return retString;
                   }
        
        return retString;
    }

  

    public void setSvNumErrorMsg(String svNumErrorMsg) {
        this.svNumErrorMsg = svNumErrorMsg;
    }

    public String getSvNumErrorMsg() {
        return svNumErrorMsg;
    }
    
    
    
    public static boolean isNumeric(String str)  
    {  
      try  
      {  
        double d = Double.parseDouble(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
    }


    public void setErrorOTP(String errorOTP) {
        this.errorOTP = errorOTP;
    }

    public String getErrorOTP() {
        return errorOTP;
    }
    
    
    public static String maskedGenericNumber(String genericNumber){
        // logger.info("Number to be masked: " + mobileNumber);
         StringBuilder masked = new StringBuilder();
         int maskDigits=genericNumber.length()-com.iocl.customer.model.utils.EPICConstant.NON_MASKED_MOBILECHAR;
         for (int i = 0; i < genericNumber.length(); i++) {
             char c = genericNumber.charAt(i);
             logger.info("character at i: "+i+" is :" + c);
             if(i<maskDigits){
                 masked.append(com.iocl.customer.model.utils.EPICConstant.MASKEDCHAR);
             }else{
                 masked.append(c);
             }
         }
         logger.info("Final String is :" + masked.toString());
         return masked.toString();
        
    }

    public void setErrorEmail(String errorEmail) {
        this.errorEmail = errorEmail;
    }

    public String getErrorEmail() {
        return errorEmail;
    }

    public void setBindSVNumber(RichInputText bindSVNumber) {
        this.bindSVNumber = bindSVNumber;
    }

    public RichInputText getBindSVNumber() {
        return bindSVNumber;
    }

    public void setBindAadharNumber(RichInputText bindAadharNumber) {
        this.bindAadharNumber = bindAadharNumber;
    }

    public RichInputText getBindAadharNumber() {
        return bindAadharNumber;
    }

    public void setBindCashMemoNumber(RichInputText bindCashMemoNumber) {
        this.bindCashMemoNumber = bindCashMemoNumber;
    }

    public RichInputText getBindCashMemoNumber() {
        return bindCashMemoNumber;
    }
    
    
    public static boolean isValidName(String aaadharNum) {

        Matcher matcher = aadhaarPattern.matcher(aaadharNum);
        return matcher.find();
    }
    
    public String onClickYes() {
        // Add event code here...
        String returnMsg=null;
        java.util.Map sessionparam = ADFContext.getCurrent().getSessionScope();
        try {
            java.util.List lst = new java.util.ArrayList();
            JSONObject jsonInput = new JSONObject();
            OperationBinding op = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            if((sessionparam.get("LPGRelationship")!="Y") && (sessionparam.get("LoyaltyRelationship")=="Y") )
            {   
                //jsonInput.put(EPICConstant.LPG_UCMID,String.valueOf(param.get("ucmId"))!="null"?String.valueOf(param.get("ucmId")):CommonHelper.evaluateEL("#{bindings.UCMId.inputValue}"));
                jsonInput.put(EPICConstant.LPG_UCMID,String.valueOf(sessionparam.get("sessionUCMID")));
                jsonInput.put(EPICConstant.XR_UCMID,CommonHelper.evaluateEL("#{sessionScope.userDetails.ucmID}")); // "ucmID" contain XR UCMId
                jsonInput.put("IDAMUserKey", (String)CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}"));   
                jsonInput.put("EnrichmentFlag", "N");
                jsonInput.put("DuplicationFlag","Y");
                logger.info("Json Input on Click Yes"+jsonInput);
                lst.add(EPICIOCLResourceCustBundle.findKeyValue("PROFILE_ENRICHMENT"));
                lst.add(1,jsonInput);
                op.getParamsMap().put(EPICConstant.SERVICELIST, lst);
                op.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.XR_LINKING);
                op.execute();    
                
            
            }
            else {
                jsonInput.put(EPICConstant.USER_KEY, (String)CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}"));
                logger.info("ucmId from pageFlowScope"+String.valueOf(param.get("ucmId")));
                logger.info("ucmId from Bindings"+CommonHelper.evaluateEL("#{bindings.UCMId.inputValue}"));
                logger.info("ucmId from page Binding"+String.valueOf(ucmIDbinding.getValue()));
                logger.info("ucmId from session"+String.valueOf(sessionparam.get("sessionUCMID")));
                jsonInput.put(EPICConstant.UCMID, String.valueOf(sessionparam.get("sessionUCMID")));
                //jsonInput.put(EPICConstant.UCMID, String.valueOf(param.get("ucmId"))!="null"?String.valueOf(param.get("ucmId")):CommonHelper.evaluateEL("#{bindings.UCMId.inputValue}"));
                lst.add(EPICIOCLResourceCustBundle.findKeyValue("IDAM_CONTACT"));
                lst.add(1,jsonInput);
                op.getParamsMap().put(EPICConstant.SERVICELIST, lst);
                op.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.IDAM_CONTACT);
                op.execute();    
            }
            
            
            
            
            java.util.List returnList = (java.util.List) op.getResult();
            logger.info("returnList"+returnList);
            if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0).toString().equalsIgnoreCase("true")) {
            JSONObject jsonObject = new JSONObject(returnList.get(1).toString());
            errorCode=jsonObject.getString("ErrorCode").toString();
            if(errorCode.equalsIgnoreCase("IDAM-500"))
            {    
            sessionparam.remove("sessionUCMID");    
            errorMessage = jsonObject.getString("ErrorMessage").toString();
            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.IDAM500);    
            returnMsg = "ERROR";
            return returnMsg;
            }
            else if(errorCode.equalsIgnoreCase("0"))
            {
                 String popUpmsg = CommonHelper.getValueFromRsBundle("CONF_MESSAGE_LINK");
                 param.put("popUpmsg",popUpmsg);
                 RichPopup.PopupHints hints = new RichPopup.PopupHints();
                 getBindpopUp().show(hints);
//            param.put("confMessage",CommonHelper.getValueFromRsBundle("CONF_MESSAGE_LINK"));
//            param.put("relogin",CommonHelper.getValueFromRsBundle("CONF_RELOGIN"));
//            sessionparam.remove("sessionUCMID"); 
//            logger.info("------------Relogin"+CommonHelper.getValueFromRsBundle("CONF_RELOGIN"));
            
           
            
            return null; 
             }
            else {
                returnMsg = "ERROR";
                sessionparam.remove("sessionUCMID"); 
                return returnMsg;
                }
            
            
            
                 }
            else
            {
            returnMsg = "ERROR";
            return returnMsg;
            
            }
                        
            }
            catch(Exception ex){
            ex.printStackTrace();
            
            returnMsg = "ERROR";
            
            return returnMsg;
            }
}
    
    
    public String callSiebelUpdate() {
          String returnMsg=null;
          try {
             
          java.util.List lst = new java.util.ArrayList();
          JSONObject jsonInput = new JSONObject();
          OperationBinding op = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
         
          jsonInput.put(EPICConstant.MOBILE_NUMBER, String.valueOf(param.get("mobNum")));
          jsonInput.put(EPICConstant.EMAIL, String.valueOf(param.get("emailid")));
          jsonInput.put(EPICConstant.VER_FLAG, "Y");
          lst.add(EPICIOCLResourceCustBundle.findKeyValue("LINK_CONS_DETLS"));
          lst.add(1,jsonInput);
          op.getParamsMap().put(EPICConstant.SERVICELIST, lst);
          op.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.LINK_CONS_DETLS);
          op.execute();
          java.util.List returnList = (java.util.List) op.getResult();
          logger.info("returnList"+returnList);
          if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0).toString().equalsIgnoreCase("true")) {
          JSONObject jsonObject = new JSONObject(returnList.get(1).toString());
          errorCode=jsonObject.getString("ErrorCode").toString();
          if(errorCode.equalsIgnoreCase("0"))
          {    
              returnMsg="toConfirm";
              return returnMsg;
          }
          
          else
          {
              returnMsg="toConfirm";
              return returnMsg;
              
          }
         
                             
          }
               else {
                   returnMsg="toConfirm";
                   
                   return returnMsg;
               }    
            
           }
       
          catch(Exception ex){
          ex.printStackTrace();
              returnMsg="toConfirm";
          
          return returnMsg;
          }
      }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void resendOTP(ActionEvent actionEvent) {
        // Add event code here...
         
        //param.put("MobNum", mobNumber);
        try
        {
        String mobNumber=String.valueOf(param.get("MobNum"));
        CommonHelper.sendOTP(mobNumber);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public void resendEmailOTP(ActionEvent actionEvent) {
        // Add event code here...
        try
        {
        String emailAdd=String.valueOf(param.get("EmailAdd"));
        CommonHelper.sendEmailOTP(emailAdd);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public String onSubmitLpgId() {
        logger.info("Inside Class linkedLPG Method onSubmitLpgId");
        String retString = null;
        String mobNumber=null;
        try {
            resetError();
            linkedLpgError = checkLpgIdValidation();
            logger.info("Linked LpgId Error" + linkedLpgError);
            if (linkedLpgError) {
                return null;
            } else {
                strCons = new StringBuffer();
                if (linkedLpgError == false) {
                    strCons.append(bindBox1.getValue().toString());
                    strCons.append(bindBox2.getValue().toString());
                    strCons.append(bindBox3.getValue().toString());
                    strCons.append(bindBox4.getValue().toString());
                    logger.info("strCons" + strCons);
                    param.put("ConsumerId", strCons);
                    java.util.List lstInput = new java.util.ArrayList();
                    JSONObject jsonInput = new JSONObject();
                    jsonInput.put(EPICConstant.CONSUMERID, strCons.toString().trim());
                    jsonInput.put(EPICConstant.MOBILE_NUMBER, String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserMobileNo}")));
                    lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("GET_CUSTOMER_DETAILS"));
                    lstInput.add(1, jsonInput);
                    OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                    ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
                    ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.GET_CUSTOMER_DETAILS);
                    ob.execute();
                    java.util.List returnList = (java.util.List) ob.getResult();
                    JSONObject jsonObject = new JSONObject(returnList.get(1).toString());
                    logger.info("returnList" + returnList);
                    if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0)
                                                                                         .toString()
                                                                                         .equalsIgnoreCase("TRUE")) 
                        {
                        if(String.valueOf(jsonObject.get("ErrorCode")).equalsIgnoreCase("0")) {
                        retString = "toaccountDetails";
                        }
                        else{
                            retString = EPICConstant.ERROR;
                            return retString; 
                        }
                    }
                    else if((returnList != null) && (returnList.get(0) != null) && returnList.get(0)
                                                                                         .toString()
                                                                                         .equalsIgnoreCase("FALSE")) {
                        if (String.valueOf(jsonObject.get("ErrorCode")).equalsIgnoreCase("100")) {
                           
                        retString = "toSV";
                        } else {
                            retString = EPICConstant.ERROR;
                            return retString;
                        } 
                    }
                        
                        
                    
                       
                       

                    


                } 
                else {
                    retString = EPICConstant.ERROR;
                    return retString;
                }


            }


            return retString;
        } catch (Exception e) {
            retString = EPICConstant.ERROR;
            return retString;
        }

    }
        
        
        
        
     

    private Boolean checkLpgIdValidation() {
        logger.info("Inside checkLpgIdValidation Start");
        lpgIdError = false;
        if ((lpgIdError == false) &&
            (CustomerValidation.isValidOrNullVal(bindBox1.getValue()) == EPICConstant.BLANK_CASE) ||  (CustomerValidation.isValidOrNullVal(bindBox2.getValue()) == EPICConstant.BLANK_CASE)
            || (CustomerValidation.isValidOrNullVal(bindBox3.getValue()) == EPICConstant.BLANK_CASE) || (CustomerValidation.isValidOrNullVal(bindBox4.getValue()) == EPICConstant.BLANK_CASE)) {
            lpgIdError = true;
            lpgIdErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_LPG_ID");
            logger.info("lpgIdErrorMsg"+lpgIdErrorMsg);
        }
        logger.info("Inside checkLpgIdValidation lpgIdError flag"+lpgIdError);
        return lpgIdError;
        
    }

    public void resetLpgId(ActionEvent actionEvent) {
        lpgIdErrorMsg = "";
        bindBox1.setValue("");
        bindBox2.setValue("");
        bindBox3.setValue("");
        bindBox4.setValue("");
        
        bindBox1.setStyleClass("");
        bindBox2.setStyleClass("");
        bindBox3.setStyleClass("");
        bindBox4.setStyleClass("");
    }

    public String submitServiceRequest() {
            String retString = null;
        try {
            java.util.List lstInput = new java.util.ArrayList();
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("EntityType", "Phone");
            jsonInput.put("EntityValue", String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerContactNumber}")));
            jsonInput.put(EPICConstant.CONSUMERID,String.valueOf(param.get("ConsumerId")));
            jsonInput.put(EPICConstant.RELATIONSHIPUCMID,String.valueOf(param.get("RelUCMId")));
            jsonInput.put(EPICConstant.UCMID,String.valueOf(param.get("UCMId")));
            lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("UPDATE_LINKING"));
            lstInput.add(1, jsonInput);
            OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
            ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.UPDATE_LINKING);
            ob.execute();
            java.util.List returnList = (java.util.List) ob.getResult();
            JSONObject jsonObject = new JSONObject(returnList.get(1).toString());
            logger.info("returnList" + returnList);
            if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0).toString().equalsIgnoreCase("TRUE"))
            {
            if(String.valueOf(jsonObject.get("ErrorCode")).equalsIgnoreCase("0")) {
                 param.put("confMessage",CommonHelper.getValueFromRsBundle("LINKING_SERVICE_REQUEST"));
                 param.put("refNumber", jsonObject.isNull("SR_Num") ? null : jsonObject.get("SR_Num"));
                 retString = "toConfirm";
             }
           else{
            retString = EPICConstant.ERROR;
            return retString; 
                }
            }
            else if((returnList != null) && (returnList.get(0) != null) && returnList.get(0).toString().equalsIgnoreCase("FALSE")) {
                             if(String.valueOf(jsonObject.get("ErrorCode")).equalsIgnoreCase(EPICConstant.SBL100)) {
                                String finalString = CommonHelper.getValueFromRsBundle("LINKING_NOTID_FOUND");
                                logger.info("Final String"+finalString);
                                param.put("finalStringKey", finalString);
                                retString = "toDistributor";
                            }
            else
                             {
                            retString = EPICConstant.ERROR;
                            return retString;
                             }  } 
                    
             else {
                    retString = EPICConstant.ERROR;
                    return retString;
                }
           return retString;
        } catch (Exception e) {
            retString = EPICConstant.ERROR;
            return retString;
        }

        
    }

    public void gotoNewConnection(ActionEvent actionEvent) {
        String trimURL = null;
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            trimURL = "/webcenter/portal/LPG/pages_loginkyc";
            exct.redirect(trimURL);
            FacesContext.getCurrentInstance().responseComplete();
            } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void setUcmIDbinding(RichOutputFormatted ucmIDbinding) {
        this.ucmIDbinding = ucmIDbinding;
    }

    public RichOutputFormatted getUcmIDbinding() {
        return ucmIDbinding;
    }

  

    public String mobileNotFoundClickYes() {
        String retString = null;
        String returnMsg=null;
        java.util.Map sessionparam = ADFContext.getCurrent().getSessionScope();
        try {
            logger.info("Inside mobileNotFoundClickYes");    
        java.util.List lstInput = new java.util.ArrayList();
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("EntityType", "Phone");
        jsonInput.put("EntityValue", String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerContactNumber}")));
        jsonInput.put(EPICConstant.CONSUMERID,String.valueOf(param.get("ConsumerId")));
        jsonInput.put(EPICConstant.RELATIONSHIPUCMID,String.valueOf(param.get("RelUCMId")));
        jsonInput.put(EPICConstant.UCMID,String.valueOf(param.get("UCMId")));
        lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("UPDATE_LINKING"));
        lstInput.add(1, jsonInput);
        OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
        ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
        ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.UPDATE_LINKING);
        ob.execute();
        java.util.List returnList = (java.util.List) ob.getResult();
        JSONObject jsonObject = new JSONObject(returnList.get(1).toString());
        logger.info("returnList" + returnList);
        if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0).toString().equalsIgnoreCase("TRUE"))
        {
        if(String.valueOf(jsonObject.get("ErrorCode")).equalsIgnoreCase("0")) {
            param.put("refNumber", jsonObject.isNull("SR_Num") ? null : jsonObject.get("SR_Num"));
               
            try {
                java.util.List lst = new java.util.ArrayList();
                JSONObject jsonInput1 = new JSONObject();
                OperationBinding op = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                if((sessionparam.get("LPGRelationship")!="Y") && (sessionparam.get("LoyaltyRelationship")=="Y") )
                {   
                    //jsonInput.put(EPICConstant.LPG_UCMID,String.valueOf(param.get("ucmId"))!="null"?String.valueOf(param.get("ucmId")):CommonHelper.evaluateEL("#{bindings.UCMId.inputValue}"));
                    jsonInput1.put(EPICConstant.LPG_UCMID,String.valueOf(sessionparam.get("sessionUCMID")));
                    jsonInput1.put(EPICConstant.XR_UCMID,CommonHelper.evaluateEL("#{sessionScope.userDetails.ucmID}")); // "ucmID" contain XR UCMId
                    jsonInput1.put("IDAMUserKey", (String)CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}"));   
                    jsonInput1.put("EnrichmentFlag", "N");
                    jsonInput1.put("DuplicationFlag","Y");
                    logger.info("Json Input on Click Yes"+jsonInput1);
                    lst.add(EPICIOCLResourceCustBundle.findKeyValue("PROFILE_ENRICHMENT"));
                    lst.add(1,jsonInput1);
                    op.getParamsMap().put(EPICConstant.SERVICELIST, lst);
                    op.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.XR_LINKING);
                    op.execute();    
                    
                
                }
                else {
                    jsonInput1.put(EPICConstant.USER_KEY, (String)CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}"));
                    logger.info("ucmId from pageFlowScope"+String.valueOf(param.get("ucmId")));
                    logger.info("ucmId from Bindings"+CommonHelper.evaluateEL("#{bindings.UCMId.inputValue}"));
                    logger.info("ucmId from page Binding"+String.valueOf(ucmIDbinding.getValue()));
                    logger.info("ucmId from session"+String.valueOf(sessionparam.get("sessionUCMID")));
                    jsonInput1.put(EPICConstant.UCMID, String.valueOf(sessionparam.get("sessionUCMID")));
                    //jsonInput.put(EPICConstant.UCMID, String.valueOf(param.get("ucmId"))!="null"?String.valueOf(param.get("ucmId")):CommonHelper.evaluateEL("#{bindings.UCMId.inputValue}"));
                    lst.add(EPICIOCLResourceCustBundle.findKeyValue("IDAM_CONTACT"));
                    lst.add(1,jsonInput1);
                    op.getParamsMap().put(EPICConstant.SERVICELIST, lst);
                    op.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.IDAM_CONTACT);
                    op.execute();    
                }
                
                
                
                
                java.util.List returnList1 = (java.util.List) op.getResult();
                logger.info("returnList"+returnList1);
                if ((returnList1 != null) && (returnList1.get(0) != null) && returnList1.get(0).toString().equalsIgnoreCase("true")) {
                JSONObject jsonObject1 = new JSONObject(returnList.get(1).toString());
                errorCode=jsonObject1.getString("ErrorCode").toString();
                if(errorCode.equalsIgnoreCase("IDAM-500"))
                {    
                sessionparam.remove("sessionUCMID");    
                errorMessage = jsonObject1.getString("ErrorMessage").toString();
                CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.IDAM500);    
                returnMsg = "ERROR";
                return returnMsg;
                }
                else if(errorCode.equalsIgnoreCase("0"))
                {
                     String popUpmsg = CommonHelper.getValueFromRsBundle("LINKING_POPUP_MSG");
                     param.put("popUpmsg",popUpmsg);
                     RichPopup.PopupHints hints = new RichPopup.PopupHints();
                     getBindpopUp().show(hints);
                     return null; 
                 }
                else {
                    returnMsg = "ERROR";
                    sessionparam.remove("sessionUCMID"); 
                    return returnMsg;
                    }
                
                
                
                     }
                else
                {
                returnMsg = "ERROR";
                return returnMsg;
                
                }
                            
                }
                catch(Exception ex){
                ex.printStackTrace();
                
                returnMsg = "ERROR";
                
                return returnMsg;
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
      
        } catch (Exception e) {
        retString = EPICConstant.ERROR;
        return retString;
        }
    }

    public void setBindpopUp(RichPopup bindpopUp) {
        this.bindpopUp = bindpopUp;
    }

    public RichPopup getBindpopUp() {
        return bindpopUp;
    }
    
    public void signout(PopupCanceledEvent popupCanceledEvent) {
        logger.info("Inside clickSignOut method");
        java.util.Map paramSesion = ADFContext.getCurrent().getSessionScope();
        FacesContext fctx = FacesContext.getCurrentInstance();  
        ExternalContext ectx = fctx.getExternalContext();  
        HttpSession session = (HttpSession)ectx.getSession(false);  
        session.invalidate(); 
        ExternalContext exct=FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("SIGN_OUT_URL")); 
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            logger.info("Error-Occured");
            e.printStackTrace();
        }
        paramSesion.clear();
    }
}
