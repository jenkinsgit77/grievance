package com.iocl.lpg.customer.bean.loyalty;

import com.iocl.lpg.customer.bean.AddAttchmentBean;

import com.iocl.lpg.customer.imageSliderView.view.Bannner;
import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.IOException;
import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputDate;

import oracle.adf.view.rich.component.rich.nav.RichLink;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.OperationBinding;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class XtraRewards implements Serializable {
    @SuppressWarnings("compatibility:35469098653097226")
    private static final long serialVersionUID = 1L;
    private static Logger log;
    private RichInputDate fromDtBind = new RichInputDate();
    private RichInputDate toDtBind = new RichInputDate();
    private String pointsRedeemTxnType = "IRD";
    private String pointsEarnTxnType = "TXN";
    private String timeFormatAppender = "000000";
    java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
    java.util.Map sessionScopeParam = ADFContext.getCurrent().getSessionScope();
    private java.util.List<VehicleDetailsParam> lstVehicleDetail = new java.util.ArrayList<VehicleDetailsParam>();
    private java.util.List<LoyaltyImages> lstAlliancePartImg = new java.util.ArrayList<LoyaltyImages>();
    
    private String toDtErr;
    private String fromDtErr;
    private RichLink resetLinkBind;


    public void setLstAlliancePartImg(List<LoyaltyImages> lstAlliancePartImg) {
        this.lstAlliancePartImg = lstAlliancePartImg;
    }

    public List<LoyaltyImages> getLstAlliancePartImg() {
        return lstAlliancePartImg;
    }

    public XtraRewards() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(XtraRewards.class);

        } else {
            log = Logger.getLogger(XtraRewards.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }

    public void setLstVehicleDetail(List<VehicleDetailsParam> lstVehicleDetail) {
        this.lstVehicleDetail = lstVehicleDetail;
    }

    public List<VehicleDetailsParam> getLstVehicleDetail() {
        return lstVehicleDetail;
    }

    public String fetchPointsDetails() {
        log.info("inside fetchXRewardsPointsDetails for xtrarewards loyalty");
        String returnXRDetails = "goToPointsPage";
        try {
            
            
            /**---------------------Code to Set Enroll Now URL---------------------**/
            if(CommonHelper.evaluateEL("#{sessionScope.enrollNowURL}") == null)
            {
                log.info("enrollNow URL is being Set");
                String enrollNow = null;
                enrollNow = EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.XR_CUSTOMER_ENROLLURL);   
                
                /**-------------Appended code is commented as per new Logic-----------**/
//                if(CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerId}") != null)
//                {
//                    log.info("consumerId is NOT NULL and value is "+CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerId}"));
//                    enrollNow = enrollNow + String.format("FN=%s&LN=%s&EM=%s&MN=%s",CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserFirstName}"), CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserLastName}"), CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserEmailId}"), CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserMobileNo}"));
//                }else
//                {
//                    log.info("consumerId is NULL");
//                    enrollNow = enrollNow + String.format("MN=%s&EM=%s",CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserMobileNo}"),CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserEmailId}"));
//                }
                log.info("final enrollNow URL is "+enrollNow);
                CommonHelper.setEL("#{sessionScope.enrollNowURL}", enrollNow);
            }else
            {
                log.info("final enrollNow URL is "+CommonHelper.evaluateEL("#{sessionScope.enrollNowURL}"));
            }
            /**------------------------------End Here------------------------------**/
            
            /**-----------------Condition to check whether Loyalty Relationship Exist otherwise move to Other page-----------**/
//            sessionScopeParam.put("LoyaltyRelationship","Y"); // Temporary
            log.info("LoyaltyRelationship Exist: "+sessionScopeParam.get("LoyaltyRelationship"));
            if (sessionScopeParam.get("LoyaltyRelationship") != null && sessionScopeParam.get("LoyaltyRelationship").equals("Y")) {
                populateAlliancePart();
                /*-------------------------------End Here----------------------------*/
            } else {
                    log.info("Inside fetchPointsDetails method. Customer Details Not Found");
                    log.info("User will navigate to Loyality Other Page");
                    String trimURL = EPICIOCLResourceCustBundle.findKeyValue("LOYALTY_OTHER_PATH");
                    
                    ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
                    try {
                    exct.redirect(trimURL);
                    FacesContext.getCurrentInstance().responseComplete();
                    } 
                    catch (IOException e) {
                    e.printStackTrace();
                    } 
                   pageflowParam.put("ExternalNavigation", "Y");
                   returnXRDetails = "externalNav";
                   return returnXRDetails;
                   
            }
            /**----------------------------------------------End Here-------------------------------------------------**/
            returnXRDetails = fetchPointsDataCall(); //Service Response check in function
            initializeVars();
            log.info("returnXRDetails after Transaction Services: "+returnXRDetails);            

        } catch (Exception ex) {
            log.info("Error in fetchPointsDetails method");
            ex.printStackTrace();
            returnXRDetails = "ERROR";
        }
        return returnXRDetails;
    }


    public void populateAlliancePart() {
    log.info("Inside populateAlliancePart");
        try {
            String imgList = EPICIOCLResourceCustBundle.findKeyValue("PUBLIC_ALLIANCE_PART_IMAGE_LIST");
            String[] imgListArray = imgList.split(",");
            List<String> varLst = Arrays.asList(imgListArray);
            if (varLst != null) {
                for (int i = 0; i < varLst.size(); i=i+3) {
                    System.out.println("i value Before is "+i);
                    LoyaltyImages obj = new LoyaltyImages();
                    if (i < varLst.size()) {
                        obj.setLoyalityImg1(varLst.get(i));
                    }
                    if ((i+1) < varLst.size() && varLst.get(i + 1) != null) {
                        obj.setLoyalityImg2(varLst.get(i + 1));
                    }
                    if ((i+2) < varLst.size() && varLst.get(i + 2) != null) {
                        obj.setLoyalityImg3(varLst.get(i + 2));
                    }
                    if ((i+3) < varLst.size() && varLst.get(i + 3) != null) {
                        obj.setLoyalityImg4(varLst.get(i + 3));
                    }
                    lstAlliancePartImg.add(obj);
                    System.out.println("i value After is "+i);
                }
                pageflowParam.put("pLstAlliancePartImg", lstAlliancePartImg);
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
    }

    public String fetchPointsDataCall() {
        log.info("inside fetchPointsDataCall for xtrarewards loyalty");
        //        String returnXRTxnDetails="Success";
        String returnXRTxnDetails = "goToPointsPage"; // Return value set to default navigation
        java.util.List lstInputGenRep = new java.util.ArrayList();
        JSONObject jsonInputXRTxnDetails = new JSONObject();
        try {
            
            jsonInputXRTxnDetails.put("MobileNumber",CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserMobileNo}"));
//            jsonInputXRTxnDetails.put("MobileNumber","6333333333");
            
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date dateobj = new Date();
            df = new SimpleDateFormat("yyyyMMddHHmmss");
            df = new SimpleDateFormat("yyyyMMdd");
            String today = df.format(dateobj);
            jsonInputXRTxnDetails.put("RequestDateTime", today + timeFormatAppender);
            Calendar cal = Calendar.getInstance();
            
//            jsonInputXRTxnDetails.put("TransactionType", pointsEarnTxnType); // Commented Temporary

            if ((null == fromDtBind.getValue() && null == toDtBind.getValue())
                 ||  
                ("" == fromDtBind.getValue() && "" == toDtBind.getValue())) {
                cal.add(Calendar.MONTH, -5); // getting details for previous 2 years
                //Date prevDateobj = new Date();
                DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
                Date prevDateobj = cal.getTime();
                String prevDate = df.format(prevDateobj);
                today = df.format(dateobj);
                log.info("Default Requested dates::" + prevDate + " to " + today);
                
                jsonInputXRTxnDetails.put("FromDate", prevDate + timeFormatAppender); 
                jsonInputXRTxnDetails.put("ToDate", today + timeFormatAppender);  

            } else {
                
                String fromVal = String.valueOf(fromDtBind.getValue());
                String toVal = String.valueOf(toDtBind.getValue());
                fromVal = df.format(fromDtBind.getValue());
                toVal = df.format(toDtBind.getValue());
                if(fromVal.equalsIgnoreCase("null") && !toVal.equalsIgnoreCase("null")){
                    fromDtErr="Choose From date";
                    toDtErr=""; 
                    fromDtBind.setStyleClass(EPICConstant.ERROR_CLASS);
                    return "";
                }else if(toVal.equalsIgnoreCase("null") && !fromVal.equalsIgnoreCase("null")){
                    fromDtErr="";
                    toDtErr="Choose To date";
                    toDtBind.setStyleClass(EPICConstant.ERROR_CLASS);
                    return "";
                }else{
                    DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
                    Date fD=df2.parse(fromVal);
                    Date tD=df2.parse(toVal);
                    log.info("fd::"+fD+"+tD::"+tD);
                    if(fD.compareTo(tD) > 0){
                        toDtErr="To date should be after from date";
                        toDtBind.setStyleClass(EPICConstant.ERROR_CLASS);
                        return "";
                    }
                    
                    
                    fromDtErr="";
                    toDtErr=""; 
                    fromDtBind.setStyleClass("");
                    toDtBind.setStyleClass("");
                    
                    log.info("Requested dates::" + fromVal + " to " + toVal);
                    jsonInputXRTxnDetails.put("FromDate", fromVal + timeFormatAppender);  
                    jsonInputXRTxnDetails.put("ToDate", toVal + timeFormatAppender);      
                }

            }


            lstInputGenRep.add(0, EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.XR_CUSTOMER_TRANSACTION));
            lstInputGenRep.add(1, jsonInputXRTxnDetails);
//            lstInputGenRep.add(2, jsonInputXRTxnDetails.get("TransactionType"));

            /** get customer details for points earn***/
            OperationBinding opTxn = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            opTxn.getParamsMap().put(EPICConstant.SERVICELIST, lstInputGenRep);
            opTxn.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.XR_CUSTOMER_TRANSACTION);
            opTxn.execute();

            java.util.List returnXRTxnDetailsList = (java.util.List) opTxn.getResult();

            /**-----------------Below code added to handle navigation in case errorCode 0,1,100 comes-----------**/
            
            
            pageflowParam.put("pCustomerTxnValidCode",null); // Resetting business Validation
            pageflowParam.put("pCustomerTxnValidMsg",null);  // Resetting business Validation
            pageflowParam.put("pBalancepoint",null);
            if ((returnXRTxnDetailsList != null) && (returnXRTxnDetailsList.get(0) != null) &&
                returnXRTxnDetailsList.get(0).toString().equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                JSONObject jsonObject = new JSONObject(returnXRTxnDetailsList.get(1).toString());
                pageflowParam.put("pBalancepoint", jsonObject.isNull("BalancePoints")? "0" : jsonObject.get("BalancePoints"));
            } else if ((returnXRTxnDetailsList != null && returnXRTxnDetailsList.size() >= 1) &&
                       (returnXRTxnDetailsList.get(0).toString().equalsIgnoreCase("false") &&
                        returnXRTxnDetailsList.size() > 1)) {
                JSONObject jsonResponse = new JSONObject(returnXRTxnDetailsList.get(1).toString());

                if(!jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) && 
                   jsonResponse.getString(EPICConstant.ERROR_MESSAGE).equalsIgnoreCase("Data Not found")){
                    log.info("Earned Txn Success Service Excecution, however not Data available for corresponding dates query");
                }else{
                    pageflowParam.put("pCustomerTxnValidCode",
                                      jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                                      jsonResponse.getString(EPICConstant.ERROR_CODE));
                    pageflowParam.put("pCustomerTxnValidMsg",
                                      jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                                      jsonResponse.getString(EPICConstant.ERROR_MESSAGE));
    
    
                    if (pageflowParam.get("pCustomerTxnValidCode") != null &&
                        pageflowParam.get("pCustomerTxnValidCode").toString().equalsIgnoreCase(EPICConstant.SBL100)) {
                        log.info("Business Validation Issue SBL-100 ErrorCode Received");
                    } else {
                        log.info("Some Error in WebService Response");
                        returnXRTxnDetails = "ERROR";
                        return returnXRTxnDetails;
                    }
                }
            } else {
                returnXRTxnDetails = "ERROR";
            }
            /**----------------------------------------------End Here-------------------------------------------------**/

        } catch (Exception ex) {
            log.info("Error in fetchPointsDataCall method");
            ex.printStackTrace();
            returnXRTxnDetails = "ERROR";
        }
        CommonHelper.runJavaScript("dataTableInvoke()");
        return returnXRTxnDetails;
    }

    public void setFromDtBind(RichInputDate fromDtBind) {
        this.fromDtBind = fromDtBind;
    }

    public RichInputDate getFromDtBind() {
        return fromDtBind;
    }

    public void setToDtBind(RichInputDate toDtBind) {
        this.toDtBind = toDtBind;
    }

    public RichInputDate getToDtBind() {
        return toDtBind;
    }

    public void setTimeFormatAppender(String timeFormatAppender) {
        this.timeFormatAppender = timeFormatAppender;
    }

    public String getTimeFormatAppender() {
        return timeFormatAppender;
    }

    public void setPointsRedeemTxnType(String pointsRedeemTxnType) {
        this.pointsRedeemTxnType = pointsRedeemTxnType;
    }

    public String getPointsRedeemTxnType() {
        return pointsRedeemTxnType;
    }

    public void setPointsEarnTxnType(String pointsEarnTxnType) {
        this.pointsEarnTxnType = pointsEarnTxnType;
    }

    public String getPointsEarnTxnType() {
        return pointsEarnTxnType;
    }

    public void setToDtErr(String toDtErr) {
        this.toDtErr = toDtErr;
    }

    public String getToDtErr() {
        return toDtErr;
    }

    public void setFromDtErr(String fromDtErr) {
        this.fromDtErr = fromDtErr;
    }

    public String getFromDtErr() {
        return fromDtErr;
    }
    
    public String initializeVars(){
        try{
            fromDtBind = new RichInputDate();
            toDtBind = new RichInputDate();
            fromDtBind.setStyleClass("");
            toDtBind.setStyleClass("");
            fromDtBind.resetValue();
            toDtBind.resetValue();
            fromDtBind.setValue("");
            toDtBind.setValue("");
            fromDtErr="";
            toDtErr=""; 
            pageflowParam.put("pFromDate", null);
            pageflowParam.put("pToDate", null);
        }
        catch(Exception et){
            log.info("Exception in initial date setting");
        }
        return "";
    }
    
    public String resetVars(){
        try{
            initializeVars();
            fetchPointsDataCall();
        }
        catch(Exception et){
            log.info("Exception in resetting date, getting default table");
        }
        return "";
    }
    
    public Date getUserDOBTo() {
            java.util.Calendar now =
              new java.util.GregorianCalendar();
              java.util.Calendar cal =
                new java.util.GregorianCalendar(
                  now.get(java.util.Calendar.YEAR),
                  now.get(java.util.Calendar.MONTH),
                  now.get(java.util.Calendar.DATE));
              cal.add(java.util.Calendar.DAY_OF_MONTH, +1);
              return cal.getTime();
        }
    
    public Date getUserDOBFrom() {
            java.util.Calendar now =
              new java.util.GregorianCalendar();
              java.util.Calendar calend =
                new java.util.GregorianCalendar(
                  now.get(java.util.Calendar.YEAR),
                  now.get(java.util.Calendar.MONTH),
                  now.get(java.util.Calendar.DATE));
              calend.add(java.util.Calendar.YEAR, -2);
              log.info("cal.getTime()::"+calend.getTime());
              return calend.getTime();
        }

    public void setResetLinkBind(RichLink resetLinkBind) {
        this.resetLinkBind = resetLinkBind;
    }

    public RichLink getResetLinkBind() {
        return resetLinkBind;
    }
}
