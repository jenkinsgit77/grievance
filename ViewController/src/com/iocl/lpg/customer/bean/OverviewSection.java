package com.iocl.lpg.customer.bean;

import com.iocl.customer.model.service.AppModuleImpl;
import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;
import com.iocl.lpg.customer.utils.UCMUtil;

import ioclcommonproj.com.iocl.beans.CustomerRelationBean;
import ioclcommonproj.com.iocl.beans.EmailParams;
import ioclcommonproj.com.iocl.beans.IdentityParams;
import ioclcommonproj.com.iocl.beans.MobileNoParams;
import ioclcommonproj.com.iocl.beans.ServiceParam;
import ioclcommonproj.com.iocl.beans.UserProfileBean;

import ioclcommonproj.com.iocl.beans.VehicleDetailProfileParam;
import ioclcommonproj.com.iocl.beans.VehicleDetailsParam;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.binding.OperationBinding;

import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;

import org.json.JSONException;

import ioclcommonproj.com.iocl.utils.JSONObject;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.Row;

public class OverviewSection implements Serializable {
    public OverviewSection() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(OverviewSection.class);

        } else {
            log = Logger.getLogger(OverviewSection.class);
            Logger.getRootLogger().setLevel(org.apache
                                               .log4j
                                               .Level
                                               .OFF);
        }
    }
    private static Logger log;
    private java.util.List<ServiceRequestParams> SrDetails = new java.util.ArrayList<ServiceRequestParams>();
    private Boolean isServiceReqNull;
    private RichPopup detailsPopup;
    java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
    java.util.Map sessionParam = ADFContext.getCurrent().getSessionScope();

    public void setDetailsPopup(RichPopup detailsPopup) {
        this.detailsPopup = detailsPopup;
    }

    public RichPopup getDetailsPopup() {
        return detailsPopup;
    }


    public void setIsServiceReqNull(Boolean isServiceReqNull) {
        this.isServiceReqNull = isServiceReqNull;
    }

    public Boolean getIsServiceReqNull() {
        return isServiceReqNull;
    }

    public void setSrDetails(List<ServiceRequestParams> SrDetails) {
        this.SrDetails = SrDetails;
    }

    public List<ServiceRequestParams> getSrDetails() {
        return SrDetails;
    }
    private Integer lpgRelationsCount = 0; //to be removed
    private List<SelectItem> lpgConnectionLst;

    public String overviewOnLoad() {
        param.put("hideBackButton", "Y"); //hide back button in OverviewSection
        String retString = "goToServiceReq";
        Map<String, Object> mapSession = FacesContext.getCurrentInstance()
                                                     .getExternalContext()
                                                     .getSessionMap();
        try {
            log.info("Inside overviewOnLoad Start");
            
            retString = initializeMethod();
            final String resp =
                CommonHelper.evaluateEL("#{sessionScope.responseCustChart}") == null ? null :
                CommonHelper.evaluateEL("#{sessionScope.responseCustChart}").toString();
            if (StringUtils.isBlank(resp) || resp == null) {

                final AppModuleImpl res = CommonHelper.getRootApplicationModule();
                log.info("For report consumption consID within thread sync:" +
                         CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerId}"));
                final String custId =
                    CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerId}") == null ? null :
                    String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerId}"));
                final String userId =
                    CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserMobileNo}") == null ? null :
                    String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserMobileNo}"));
                final String userSessionTrackingId =
                    CommonHelper.evaluateEL("#{sessionScope.UserTrackingId}") == null ? null :
                    String.valueOf(CommonHelper.evaluateEL("#{sessionScope.UserTrackingId}"));
                final String relationshipUCMId =
                    CommonHelper.evaluateEL("#{sessionScope.userDetails.selectedRelationshipUCMId}") == null ? null :
                    String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.selectedRelationshipUCMId}"));
                final String ucmID =
                    CommonHelper.evaluateEL("#{sessionScope.userDetails.ucmID}") == null ? null :
                    String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.ucmID}"));
                final String url = EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.GENERATE_REPORTCHART);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Callable<String> callable = new Callable<String>() {
                    @Override
                    public String call() {
                        String ret = "-1";
                        log.info("consumption Thread am..ret" + ret);

                        try {
                            List lstInput = new ArrayList();
                            JSONObject jsonInputGenRep = new JSONObject();
                            jsonInputGenRep.put(EPICConstant.RELATIONSHIPUCMID, relationshipUCMId);
                            jsonInputGenRep.put(EPICConstant.UCMID, ucmID);
                            jsonInputGenRep.put(EPICConstant.CONSUMERID, custId);
                            jsonInputGenRep.put(EPICConstant.TRACKING_ID, userSessionTrackingId);
                            jsonInputGenRep.put(EPICConstant.SOURCE, EPICConstant.SOURCE_PORTAL);

                            System.out.println("Thread am..jsonInputGenRep1" + jsonInputGenRep);


                            DateFormat formatter = new SimpleDateFormat("MM/dd/YYYY");
                            Calendar cal = Calendar.getInstance(); //Get current date/month
                            cal.add(Calendar.MONTH, -11); //Go to date, 12 months ago
                            cal.set(Calendar.DAY_OF_MONTH, 1);
                            java.util.Date date = cal.getTime();
                            String startDt = formatter.format(date);
                            jsonInputGenRep.put("StartDate", startDt);

                            System.out.println("Thread am..jsonInputGenRep2" + jsonInputGenRep);

                            Calendar endDtCal = Calendar.getInstance();
                            date = endDtCal.getTime();
                            String today = formatter.format(date);
                            jsonInputGenRep.put("EndDate", today);

                            System.out.println("Thread am..jsonInputGenRep3" + jsonInputGenRep);


                            lstInput.add(jsonInputGenRep);
                            System.out.println("Thread am..jsonInputGenRep4" + url);
                            lstInput.add(url);
                            System.out.println("Thread am..jsonInputGenRep5" + url);
                            lstInput.add(userId);
                            lstInput.add(userSessionTrackingId);
                            System.out.println("Thread am..lstInput" + lstInput);
                            // lstInput.add(CommonHelper.findOperation(EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.GENERATE_REPORTCHART));


                            //   OperationBinding op = CommonHelper.findOperation(EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.GENERATE_REPORTCHART));
                            // op.getParamsMap().put("inputList", lstInput);
                            //op.execute();

                            java.util.List returnList = res.callChartAsync(lstInput);

                            log.info("Thread am returnList.get(0) " + returnList.get(0));


                            if (String.valueOf(returnList.get(0)).equalsIgnoreCase("0")) {
                                log.info("Thread am returnList.get(0)** " + returnList.get(0));
                                log.info("Thread am returnList.get(1) " + returnList.get(1));

                                JSONObject objSR = new JSONObject(returnList.get(1).toString());
                                log.info("Thread am objSR " + objSR);
                                ret = objSR.toString();
                                System.out.println("Thread am objSR" + ret);
                            }
                        } catch (Exception e) {
                            ret = "0";
                            System.out.println("Exception in my method" + e);
                            e.printStackTrace();
                        }
                        return ret;
                    }
                };
                Future<String> future = executor.submit(callable);
                CommonHelper.setEL("#{sessionScope.responseCustChart}", future);
            }


            log.info("retString from initializeMethod " + retString);
            if (retString.equals("ERROR")) {
                return retString;
            }
            /**-------------Start of Latest 5 SR Code-------------**/
            java.util.List lstInputSR = new java.util.ArrayList();
            JSONObject jsonInputSR = new JSONObject();
            OperationBinding opSR = null;
            java.util.List returnListSR = null;
            jsonInputSR.put("MobileNumber", "");
            jsonInputSR.put("SRCount", "5");

            lstInputSR.add(0, EPICIOCLResourceCustBundle.findKeyValue("GET_SR_LIST"));
            lstInputSR.add(1, jsonInputSR);
            if (sessionParam.get("GET_SR_LIST_RESPONSE") == null ||
                StringUtils.isBlank(String.valueOf(CommonHelper.evaluateEL("#{sessionScope.GET_SR_LIST_RESPONSE}")))) {
                log.info("session Variable GET_SR_LIST_RESPONSE is NULL");
                opSR = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                opSR.getParamsMap().put("inputList", lstInputSR);
                opSR.getParamsMap().put("method", EPICConstant.GET_SR_LIST);
                opSR.execute();
                returnListSR = (java.util.List) opSR.getResult();
                sessionParam.put("GET_SR_LIST_RESPONSE", returnListSR); //Storing Serivce Response in Session Variable
            } else {
                log.info("session Variable GET_SR_LIST_RESPONSE is NOT NULL");
                returnListSR = (List) sessionParam.get("GET_SR_LIST");
            }


            if ((returnListSR != null) && (returnListSR.get(0) != null) &&
                returnListSR.get(0)
                                                                                       .toString()
                                                                                       .equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                JSONObject jsonObjectSR = new JSONObject(returnListSR.get(1).toString());
                UserProfileBean userBn = null;
                if (mapSession.get("userDetails") == null) {
                    userBn = new UserProfileBean();
                } else {
                    userBn = (UserProfileBean) mapSession.get("userDetails");
                }
                /**-----Last5ServiceRequest added---------**/
                JSONArray srLstArray =
                    jsonObjectSR.isNull(EPICConstant.GRIEVANCE_SR_DETAILS) ? null :
                    jsonObjectSR.getJSONArray(EPICConstant.GRIEVANCE_SR_DETAILS);
                java.util.List<SRStatus> srLst = new java.util.ArrayList<SRStatus>();
                if (srLstArray != null && srLstArray.length() > 0) {
                    userBn.setHasLast5ServiceRequest(true);
                }
                if (srLstArray != null) {
                    for (int counter = 0; counter < 5 && counter < srLstArray.length(); counter++) {
                        JSONObject objSR = srLstArray.getJSONObject(counter);
                        SRStatus obj = new SRStatus();
                        obj.setSrNumber(objSR.isNull("SRNumber") ? "NA" : objSR.getString("SRNumber"));
                        obj.setSrCurrStatus(objSR.isNull("Status") ? "NA" : objSR.getString("Status"));
                        srLst.add(obj);
                    }
                    userBn.setLast5ServiceRequest(srLst);
                }
                mapSession.put("userDetails", userBn); // added this  as recommended by Vikas U
                //                CommonHelper.setEL("#{sessionScope.userDetails}", userBn); // commented this as recommended by Vikas U
                /**-----------------End Here----------**/
            } else {
                //                retString = "ERROR";
                log.info("ErrorCode is not 0. Error case in GetSR");
            }
            /**---------------------End Here----------------------**/
            log.info("----------End of overviewOnLoad method------");
        } catch (JSONException jsone) {
            // TODO: Add catch code
            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
            retString = "ERROR";
            jsone.printStackTrace();
        }
        log.info("LPGCurrSection value at end of Overview Section: " + sessionParam.get("LPGCurrSection"));
        return retString;
    }

    public String initializeMethod() {
        String retString = "goToServiceReq";
        log.info("--------------------Inside initializeMethod in Customer overviewOnLoad Start------------------");

        boolean fbExistFlag = false;
        boolean twitExistFlag = false;
        boolean gmailExistFlag = false;
        boolean user_auth = false; //Parameter to Check whether user is authenticated or not
        boolean blank_ucmId = false;
        Integer lpgRelationsCount = 0;

        CommonHelper.setEL("#{sessionScope.PCOSNID}", "1001");

        try {
            Map<String, String> map = FacesContext.getCurrentInstance()
                                                  .getExternalContext()
                                                  .getRequestHeaderMap();
            Map<String, Object> mapSession = FacesContext.getCurrentInstance()
                                                         .getExternalContext()
                                                         .getSessionMap();
            /**-----------Below Method Set db Service table to user Session Bean--------**/
            setServiceDetails();
            /**--------------------------------End Here---------------------------------**/
            
            java.util.Set keys = map.keySet();
            log.info("log.starts");
            /**-------------Setting header attribute in jsonObject to be stored in db Start---------**/
            JSONObject jsonHeaderMap = new JSONObject();
            for (java.util.Iterator j = keys.iterator(); j.hasNext();) {
                String key = (String) j.next();
                String value = (String) map.get(key);
                if (key != null && value != null) {
                    if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_UCMID)) {
                        jsonHeaderMap.put(EPICConstant.HEADER_OAM_UCMID, String.valueOf(value));
                    }
                    if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_UID)) {
                        jsonHeaderMap.put(EPICConstant.HEADER_OAM_UID, String.valueOf(value));
                    }
                    if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_FIRST_NAME)) {
                        jsonHeaderMap.put(EPICConstant.HEADER_OAM_FIRST_NAME, String.valueOf(value));
                    }
                    if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_LAST_NAME)) {
                        jsonHeaderMap.put(EPICConstant.HEADER_OAM_LAST_NAME, String.valueOf(value));
                    }
                    if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_MOBILE)) {
                        jsonHeaderMap.put(EPICConstant.HEADER_OAM_MOBILE, String.valueOf(value));
                    }
                    if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_EMAIL)) {
                        jsonHeaderMap.put(EPICConstant.HEADER_OAM_EMAIL, String.valueOf(value));
                    }
                    if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_ROLE)) {
                        jsonHeaderMap.put(EPICConstant.HEADER_OAM_ROLE, String.valueOf(value));
                    }
                    if (jsonHeaderMap.isNull("SourceApp")) {
                        jsonHeaderMap.put("SourceApp", "Customer");
                    }
                }
            }
            /**-------------Setting header attribute in jsonObject to be stored in db End---------**/

            for (java.util.Iterator i = keys.iterator(); i.hasNext();) {
                /**----------Loop 1 Start-----------**/
                String key = (String) i.next();
                String value = (String) map.get(key);
                if (key != null && value != null) {
                    /**----------Loop 2 Start-----------**/
                    log.info(key + " ====" + value);

                    /**------------------Below Code set basic Paramter in Session irrespective of header attribute-----------**/
                    if (mapSession.get(EPICConstant.USER_TRACKING_ID) == null) {
                        mapSession.put(EPICConstant.USER_TRACKING_ID,
                                       CommonHelper.createUniqueID()); //Setting Unique Tracking Id once User login
                    }
                    if (mapSession.get(EPICConstant.DEBUG_MODE_ON) == null) {
                        mapSession.put(EPICConstant.DEBUG_MODE_ON,
                                       EPICIOCLResourceCustBundle.findKeyValue("DEBUG_MODE_ON")); //Setting Debug Mode
                    }
                    if (mapSession.get("ShowMarqueeNotify") == null) {
                        mapSession.put("ShowMarqueeNotify",
                                       EPICIOCLResourceCustBundle.findKeyValue("SHOW_MARQUEE_NOFIFY")); //Setting Marquee Setting
                    }
                    /**-----------------------------------------------End Here------------------------------------------------**/


                    if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_UID) && value != null) {
                        user_auth = true;
                    }
                    if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_UCMID) &&
                        (value.equalsIgnoreCase(EPICConstant.NOT_FOUND) || value.equalsIgnoreCase("null"))) {
                        log.info("-------------------UCM Id  Value is Blank---------------------");
                        blank_ucmId = true;
                    }
                    if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_UCMID) &&
                        (!StringUtils.isBlank(value) && !value.equalsIgnoreCase(EPICConstant.NOTFOUND)) &&
                        (!value.equalsIgnoreCase("null") &&
                         mapSession.get(EPICConstant.SESSION_IS_LOGGED_IN) == null)) {
                        log.info("--------------------Got UCMId------------------");
                        /**----------Loop 3 Start-----------**/

                        log.info("UserTrackingId inside Service loop : " +
                                 mapSession.get(EPICConstant.USER_TRACKING_ID));
                        jsonHeaderMap.put(EPICConstant.USER_TRACKING_ID,
                                          mapSession.get(EPICConstant.USER_TRACKING_ID)); //Setting USER_TRACKING_ID to insert from bean

                        log.info("session start..");
                        //                    mapSession.put(EPICConstant.SESSION_IS_LOGGED_IN, EPICConstant.IS_LOGGED_IN_VAL);
                        java.util.List lstInput = new java.util.ArrayList();
                        JSONObject jsonInput = new JSONObject();
                        //jsonInput.put(EPICConstant.SESSION_BN_USER_ID, "3465");
                        jsonInput.put(EPICConstant.SOURCE, EPICConstant.SOURCE_PORTAL);
                        jsonInput.put(EPICConstant.TRACKING_ID, CommonHelper.createUniqueID());
                        lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("FETCH_CONSUMER_PROFILE_DETAILS"));
                        lstInput.add(1, jsonInput);
                        lstInput.add(2, jsonHeaderMap);

                        OperationBinding op = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                        op.getParamsMap().put("inputList", lstInput);
                        op.getParamsMap().put("method", EPICConstant.FETCH_CONSUMER_PROFILE_DETAILS);
                        op.execute();

                        java.util.List returnList = (java.util.List) op.getResult();

                        log.info("returnList.get(0) " + returnList.get(0));
                        if ((returnList != null) && (returnList.get(0) != null) &&
                            returnList.get(0)
                                                                                             .toString()
                                                                                             .equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                                                          /**----------Loop 4 Start-----------**/
                                                          log.info("Paramter are set in UserProfileBean");
                            JSONObject jsonObject = new JSONObject(returnList.get(1).toString());

                            // Setting userprofile in session //
                            UserProfileBean userBn = null;
                            if (mapSession.get("userDetails") == null) {
                                userBn = new UserProfileBean();
                            } else {
                                userBn = (UserProfileBean) mapSession.get("userDetails");
                            }

                            /**-----Service Response is added-----**/

                            userBn.setConsumerProfileService(jsonObject.toString());
                            log.info("Service Response is Set in ConsumerProfileService Paramter");

                            /** New fetch customer profile response fields**/
                            String mobileRowId = "";
                            String emailRowId = "";
                            String addressRowId = "";


                            /*------------------------Customer Logic -----------------------*/
                            // Below Code set Relationship type="LPG" having Connection Status = "Active" first above all in lpgRelations
                            // After this we can compare other LPG ConsumerId with it for unique value to store

                            JSONArray relationShipArr =
                                jsonObject.isNull("Relationship1") ? null : jsonObject.getJSONArray("Relationship1");
                            if (relationShipArr != null) {
                                log.info("relationShipArr.length" + relationShipArr.length());
                                log.info("(20-Sep)Relationship Active Priority Case Removed.Only first LPG Relationship is stored");
                                //                            &&(relationAr.isNull("ConnectionStatus") == false && relationAr.getString("ConnectionStatus").equalsIgnoreCase("1"))
                                for (int rel = 0; rel < relationShipArr.length(); ++rel) {
                                    JSONObject relationAr = relationShipArr.getJSONObject(rel);
                                    if ((relationAr.isNull("Type") == false &&
                                         relationAr.getString("Type").equalsIgnoreCase("LPG"))) {
                                        if (mapSession.get("LPGRelationship") == null) {
                                            log.info("Setting LPG Relationship in 360 degree to Y");
                                            mapSession.put("LPGRelationship", "Y");
                                        }
                                        if (!userBn.getLpgRelations()
                                            .containsKey(String.valueOf(relationAr.get("RelationshipRowId")))) {
                                            userBn.getLpgRelations()
                                                .put(String.valueOf(relationAr.get("RelationshipRowId")),
                                                     new CustomerRelationBean(relationAr.isNull("ConsumerId") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("ConsumerId")),
                                                                              relationAr.isNull("RelationshipUCMId") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("RelationshipUCMId")),
                                                                              relationAr.isNull("DBCStatus") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("DBCStatus")),
                                                                              relationAr.isNull("PartnerCode") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("PartnerCode")),
                                                                              relationAr.isNull("CashandCarryFlag") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("CashandCarryFlag")),
                                                                              relationAr.isNull("ConsumerNumber") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("ConsumerNumber")),
                                                                              relationAr.isNull("PartnerAddress") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("PartnerAddress")),
                                                                              Integer.parseInt(relationAr.isNull("PartnerDeliveryRating") ?
                                                                                               "0" :
                                                                                               (relationAr.get("PartnerDeliveryRating") ==
                                                                                                null ? "0" :
                                                                                                String.valueOf(relationAr.get("PartnerDeliveryRating")))),
                                                                              CommonHelper.maskedGenericNumber(relationAr.isNull("PartnerContactNumber") ?
                                                                                                               EPICConstant.NOT_APPLICABLE :
                                                                                                               String.valueOf(relationAr.get("PartnerContactNumber"))),
                                                                              relationAr.isNull("PartnerName") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("PartnerName")),
                                                                              relationAr.isNull("ConnectionStatus") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("ConnectionStatus")),
                                                                              relationAr.isNull("ConnectionSubStatus") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("ConnectionSubStatus")),
                                                                              relationAr.isNull("PTDStatus") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("PTDStatus")),
                                                                              relationAr.isNull("SubsidyGiveFlag") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("SubsidyGiveFlag")),
                                                                              relationAr.isNull("AadhaarLinkedtoLPG") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("AadhaarLinkedtoLPG")),
                                                                              relationAr.isNull("MobileRowId") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("MobileRowId")),
                                                                              relationAr.isNull("EmailRowId") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("EmailRowId")),
                                                                              relationAr.isNull("AddressRowId") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("AddressRowId")),
                                                                              relationAr.isNull("AadhaarLinkedtoBank") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("AadhaarLinkedtoBank")),
                                                                              relationAr.isNull("BankLinkedtoLPG") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("BankLinkedtoLPG")),
                                                                              relationAr.isNull("DistributorProduct") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("DistributorProduct")),
                                                                              relationAr.isNull("KYCLevel") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("KYCLevel")),
                                                                              relationAr.isNull("RelationshipAgainst") ?
                                                                              EPICConstant.NOT_APPLICABLE :
                                                                              String.valueOf(relationAr.get("RelationshipAgainst")),
                                                                              relationAr.isNull("CampaignCode") ? "Y" :
                                                                              "N",
                                                                              relationAr.isNull("ConnectionStatus") ?
                                                                              "" :
                                                                              CommonHelper.getValueFromRsBundle(String.valueOf(relationAr.get("ConnectionStatus"))),
                                                                              relationAr.isNull("ConnectionSubStatus") ?
                                                                              "" :
                                                                              CommonHelper.getValueFromRsBundle(String.valueOf(relationAr.get("ConnectionSubStatus"))),
                                                                              relationAr.isNull("RelationshipRowId") ?
                                                                              "" :
                                                                              String.valueOf(relationAr.get("RelationshipRowId"))));
                                            //                                    break; // To break from outer loop once we get LPG Type Relationship
                                        }
                                    }

                                    if ((relationAr.isNull("Type") == false &&
                                         relationAr.getString("Type").equalsIgnoreCase("Loyalty"))) {
                                        log.info("Setting Loyalty Relationship in 360 degree to Y");
                                        mapSession.put("LoyaltyRelationship", "Y");
                                    }
                                    
                                    if ((relationAr.isNull("Type") == false &&
                                         relationAr.getString("Type").equalsIgnoreCase("Retail"))) {
                                             mapSession.put("RetailRelationship","Y");
                                             userBn.setLobType("R");
                                         }
                                   
                                }
                            }
                            /**------------Code to Set Loyalty Related Parameters Start-----------**/
                            if (mapSession.get("LoyaltyRelationship") != null &&
                                mapSession.get("LoyaltyRelationship").equals("Y")) {
                                JSONArray loyMemberArr =
                                    jsonObject.isNull("LoyMember") ? null : jsonObject.getJSONArray("LoyMember");
                                if (loyMemberArr != null) {
                                    for (int lm = 0; lm < loyMemberArr.length(); ++lm) {
                                        JSONObject loyMemObj = loyMemberArr.getJSONObject(lm);
                                        userBn.setCardNumber(loyMemObj.isNull("CardNumber") ? EPICConstant.NO_VALUE :
                                                             String.valueOf(loyMemObj.get("CardNumber")));
                                        userBn.setBalancePoints(loyMemObj.isNull("BalancePoints") ?
                                                                EPICConstant.NO_VALUE :
                                                                CommonHelper.maskedGenericNumber(String.valueOf(loyMemObj.get("BalancePoints"))));
                                    }
                                }
                                JSONArray vehicleDetailsArr =
                                    jsonObject.isNull("VehicleList") ? null : jsonObject.getJSONArray("VehicleList");
                                if (vehicleDetailsArr != null) {

                                    for (int vd = 0; vd < vehicleDetailsArr.length(); ++vd) {
                                        JSONObject vehDetailsObj = vehicleDetailsArr.getJSONObject(vd);
                                        VehicleDetailsParam vehObj = new VehicleDetailsParam();
                                        vehObj.setVehicleType(vehDetailsObj.isNull("VehicleType") ?
                                                              EPICConstant.NO_VALUE :
                                                              String.valueOf(vehDetailsObj.get("VehicleType")));
                                        vehObj.setVehicleMake(vehDetailsObj.isNull("FuelType") ? EPICConstant.NO_VALUE :
                                                              String.valueOf(vehDetailsObj.get("FuelType")));
                                        vehObj.setRegistrationNo(vehDetailsObj.isNull("RegistrationNo") ?
                                                                 EPICConstant.NO_VALUE :
                                                                 String.valueOf(vehDetailsObj.get("RegistrationNo")));
                                        userBn.getVehicleDetails().add(vehObj);
                                    }

                                }
                            }
                            /**------------Code to Set Loyalty Related Parameters End-----------**/

                            lpgRelationsCount = userBn.getLpgRelations().size();
                            log.info("----------------------------------------------------------------------");
                            log.info("lpgRelations Map Size Count " + lpgRelationsCount);
                            userBn.setLpgRelationsCount(lpgRelationsCount);
                            log.info("----------------------------------------------------------------------");
                            if (lpgRelationsCount == 0) {
                                log.info("---------Consumer has No LPG Relationship---------------");
                                log.info("---------LPGCurrSection set to NewConnection-----------");
                                mapSession.put("LPGCurrSection", "NewConnection");
                            } else if (lpgRelationsCount > 1) {
                                userBn.setLpgMultipleConnection("Y");
                                log.info("Multiple LPG Relationship exist.User Will get an Option to Select one");
                            } else if (lpgRelationsCount == 1) {
                                Map<String, CustomerRelationBean> lpgRelations =
                                    (HashMap<String, CustomerRelationBean>) userBn.getLpgRelations();
                                for (Map.Entry<String, CustomerRelationBean> entry : lpgRelations.entrySet()) {
                                    CustomerRelationBean obj = entry.getValue();
                                    userBn.setLpgSelectedConsumerId(obj.getConsumerId());
                                    userBn.setConsumerId(obj.getConsumerId()); //Setting ConsumerId Early for Report Section
                                    userBn.setSelectedRelationshipRowId(obj.getRelationshipRowId());
                                    log.info("ConsumerId for Single LPG Connection " + obj.getConsumerId() +
                                             " & relationshipRowId: " + obj.getRelationshipRowId());
                                }

                                userBn.setLpgMultipleConnection("N");
                            }
                            /*-------------------------End Here----------------------------*/


                            JSONArray listofIdentitiesArr =
                                jsonObject.isNull("ContactListofIdentities") ? null :
                                jsonObject.getJSONArray("ContactListofIdentities");
                            if (listofIdentitiesArr != null) {

                                for (int listid = 0; listid < listofIdentitiesArr.length(); ++listid) {
                                    JSONObject listIdAr = listofIdentitiesArr.getJSONObject(listid);
                                    IdentityParams identityObj = new IdentityParams();
                                    identityObj.setIdentityType(listIdAr.isNull("Type") ? EPICConstant.NOT_APPLICABLE :
                                                                String.valueOf(listIdAr.get("Type")));
                                    identityObj.setIdentityValue(listIdAr.isNull("Number") ?
                                                                 EPICConstant.NOT_APPLICABLE :
                                                                 CommonHelper.maskedMobileNumber(String.valueOf(listIdAr.get("Number"))));
                                    identityObj.setIdentityContentId(listIdAr.isNull("WCCContentId") ?
                                                                     EPICConstant.NOT_APPLICABLE :
                                                                     String.valueOf(listIdAr.get("WCCContentId")));
                                    identityObj.setDocumentId(listIdAr.isNull("WCCDocId") ?
                                                              EPICConstant.NOT_APPLICABLE :
                                                              String.valueOf(listIdAr.get("WCCDocId")));

                                    identityObj.setDocumentName(listIdAr.isNull("FileName") ?
                                                                EPICConstant.NOT_APPLICABLE :
                                                                String.valueOf(listIdAr.get("FileName")));
                                    String contentURL = "";
                                    String extensionFormed = "";
                                    if (listIdAr.isNull("WCCContentId")) {
                                        identityObj.setDocumentExtension(extensionFormed);
                                        log.info("No profile identity document found,wcc content id is null");
                                    } else {
                                        //  if (null != listIdAr.get("WCCContentId")) {
                                        log.info("getting profile identity doc location for content id::" +
                                                 String.valueOf(listIdAr.get("WCCContentId")));
                                        try {
                                            extensionFormed =
                                                UCMUtil.returnDocExtension(String.valueOf(listIdAr.get("WCCContentId")));
                                        } catch (Exception exc) {
                                            log.info("No content found in WCC although wccId exists, setting default extension blank");
                                        }
                                        log.info("profile identity extension::" + extensionFormed);
                                        identityObj.setDocumentExtension(extensionFormed);
                                    }
                                    userBn.getIdentityDetails().add(identityObj);


                                    if (listIdAr.isNull("Type") == false &&
                                        listIdAr.getString("Type").equalsIgnoreCase("PROFILE IMAGE")) {
                                        userBn.setProfilePhoto(listIdAr.isNull("WCCContentId") ?
                                                               EPICConstant.NOT_APPLICABLE :
                                                               String.valueOf(listIdAr.get("WCCContentId")));
                                        log.info("Profile Photo id::" + listIdAr.get("ProfilePhoto"));
                                    }
                                    if (listIdAr.isNull("FileName") == false &&
                                        listIdAr.getString("FileName").equalsIgnoreCase("Aadhaar(UID)")) {
                                        userBn.setAadhaarNumber(listIdAr.isNull("Number") ?
                                                                EPICConstant.NOT_APPLICABLE :
                                                                String.valueOf(listIdAr.get("Number")));
                                        log.info("Aadhaar Number" + listIdAr.get("Number"));
                                    }


                                }

                            }
                            JSONArray contactPaymentProfileArr =
                                jsonObject.isNull("ContactPaymentProfile") ? null :
                                jsonObject.getJSONArray("ContactPaymentProfile");
                            if (contactPaymentProfileArr != null) {
                                for (int cpp = 0; cpp < contactPaymentProfileArr.length(); ++cpp) {
                                    JSONObject cppAr = contactPaymentProfileArr.getJSONObject(cpp);
                                    userBn.setBankAccountNumber(cppAr.isNull("BankAccountNumber") ?
                                                                EPICConstant.NOT_APPLICABLE :
                                                                CommonHelper.maskedGenericNumber(String.valueOf(cppAr.get("BankAccountNumber"))));
                                    userBn.setBankIfscCode(cppAr.isNull("BankIFSCCode") ? EPICConstant.NOT_APPLICABLE :
                                                           CommonHelper.maskedGenericNumber(String.valueOf(cppAr.get("BankIFSCCode"))));
                                    userBn.setBankName(cppAr.isNull("BankName") ? EPICConstant.NOT_APPLICABLE :
                                                       String.valueOf(cppAr.get("BankName")));
                                }
                            }
                            JSONArray mobileArr =
                                jsonObject.isNull("ContactListofMobile") ? null :
                                jsonObject.getJSONArray("ContactListofMobile");
                            if (mobileArr != null) {
                                for (int mob = 0; mob < mobileArr.length(); ++mob) {
                                    JSONObject mobAr = mobileArr.getJSONObject(mob);
                                    MobileNoParams mobileObj = new MobileNoParams();
                                    mobileObj.setMobileNo(mobAr.isNull("MobileNumber") ? EPICConstant.NOT_APPLICABLE :
                                                          String.valueOf(mobAr.get("MobileNumber")));
                                    mobileObj.setMaskedMobileNum(mobAr.isNull("MobileNumber") ?
                                                                 EPICConstant.NOT_APPLICABLE :
                                                                 CommonHelper.maskedMobileNumber(String.valueOf(mobAr.get("MobileNumber"))));
                                    mobileObj.setMobileRowId(mobAr.isNull("MobileRowId") ? EPICConstant.NOT_APPLICABLE :
                                                             String.valueOf(mobAr.get("MobileRowId")));
                                    mobileObj.setMobilePrimary(mobAr.isNull("PrimaryFlag") ?
                                                               EPICConstant.NOT_APPLICABLE :
                                                               String.valueOf(mobAr.get("PrimaryFlag")));
                                    mobileObj.setMobileVerified(mobAr.isNull("VerifiedFlag") ?
                                                                EPICConstant.NOT_APPLICABLE :
                                                                String.valueOf(mobAr.get("VerifiedFlag")));
                                    userBn.getMobileDetails().add(mobileObj);


                                }
                            }
                            JSONArray emailArr =
                                jsonObject.isNull("ContactListofEmails") ? null :
                                jsonObject.getJSONArray("ContactListofEmails");
                            if (emailArr != null) {

                                for (int em = 0; em < emailArr.length(); ++em) {
                                    JSONObject emAr = emailArr.getJSONObject(em);
                                    EmailParams emailObj = new EmailParams();
                                    emailObj.setEmailId(emAr.isNull("EmailId") ? EPICConstant.NOT_APPLICABLE :
                                                        String.valueOf(emAr.get("EmailId")));
                                    emailObj.setEmailRowId(emAr.isNull("EmailRowId") ? EPICConstant.NOT_APPLICABLE :
                                                           String.valueOf(emAr.get("EmailRowId")));
                                    emailObj.setEmailVerified(emAr.isNull("VerifiedFlag") ?
                                                              EPICConstant.NOT_APPLICABLE :
                                                              String.valueOf(emAr.get("VerifiedFlag")));
                                    emailObj.setEmailPrimary(emAr.isNull("PrimaryFlag") ? EPICConstant.NOT_APPLICABLE :
                                                             String.valueOf(emAr.get("PrimaryFlag")));
                                    userBn.getEmailDetails().add(emailObj);

                                }
                            }

                            JSONArray socProfileArray =
                                jsonObject.isNull("SocialIdentities") ? null :
                                jsonObject.getJSONArray("SocialIdentities");
                            if (socProfileArray != null) {
                                for (int sP = 0; sP < socProfileArray.length(); ++sP) {
                                    JSONObject socPArr = socProfileArray.getJSONObject(sP);
                                    userBn.setFbId(socPArr.isNull("FacebookEmail") ? EPICConstant.NOT_APPLICABLE :
                                                   String.valueOf(socPArr.get("FacebookEmail")));
                                    userBn.setGmailId(socPArr.isNull("GmailEmail") ? EPICConstant.NOT_APPLICABLE :
                                                      String.valueOf(socPArr.get("GmailEmail")));
                                    userBn.setTwitterId(socPArr.isNull("TwitterEmail") ? EPICConstant.NOT_APPLICABLE :
                                                        String.valueOf(socPArr.get("TwitterEmail")));

                                    if (String.valueOf(userBn.getFbId())
                                        .equalsIgnoreCase(EPICConstant.NOT_APPLICABLE)) {
                                        fbExistFlag = false;
                                    } else {
                                        fbExistFlag = true;
                                    }
                                    if (String.valueOf(userBn.getGmailId())
                                        .equalsIgnoreCase(EPICConstant.NOT_APPLICABLE)) {
                                        gmailExistFlag = false;
                                    } else {
                                        gmailExistFlag = true;
                                    }
                                    if (String.valueOf(userBn.getTwitterId())
                                        .equalsIgnoreCase(EPICConstant.NOT_APPLICABLE)) {
                                        twitExistFlag = false;
                                    } else {
                                        twitExistFlag = true;
                                    }
                                }
                            }


                            /** TO REMOVE COMMENTS ONCE RETAIL ACTIVE 
                                /**------------Code to Set Retail Related Parameters Start  
                                if (mapSession.get("RetailRelationship") != null &&
                                    mapSession.get("RetailRelationship").equals("Y")) {
                                    
                                    java.util.List lstRetInput = new java.util.ArrayList();
                                    JSONObject jsonRetInput = new JSONObject();                               
                                    jsonRetInput.put(EPICConstant.SOURCE, EPICConstant.SOURCE_PORTAL);
                                    jsonRetInput.put(EPICConstant.TRACKING_ID, CommonHelper.createUniqueID());
                                    lstRetInput.add(0, EPICIOCLResourceBundle.findKeyValue(EPICConstant.FETCH_CUSTOMER_VEHICLE_DETAILS));
                                    lstRetInput.add(1, jsonRetInput);                                
                                    OperationBinding opRet = CommonHelper.findOperation(EPICConstant.SERVICELPGNAME);
                                    opRet.getParamsMap().put(EPICConstant.SERVICELIST, lstRetInput);
                                    opRet.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.FETCH_CUSTOMER_VEHICLE_DETAILS);
                                    opRet.execute();

                                    java.util.List returnRetVehList = (java.util.List) opRet.getResult();
                                    if ((returnRetVehList != null) && (returnRetVehList.get(0) != null) &&
                                        returnRetVehList.get(0).toString().equalsIgnoreCase(EPICConstant.TRUE_VAl)) {                                                                
                                        log.info("Vehicle Paramter are set in UserProfileBean");
                                        JSONObject jsonObjectVeh = new JSONObject(returnRetVehList.get(1).toString());
                                        }
                                    }
                                ****/
                            if (mapSession.get("RetailRelationship") != null &&
                                mapSession.get("RetailRelationship").equals("Y")) {
                            JSONArray listofVehiclesArr =
                                            jsonObject.isNull("VehicleList") ? null :
                                            jsonObject.getJSONArray("VehicleList");
                            //                                  JSONArray listofVehiclesArr =
                            //                                    jsonObjectVeh.isNull("ListOfVehicles") ? null :
                            //                                    jsonObjectVeh.getJSONArray("ListOfVehicles");
                                
                            if (listofVehiclesArr != null) {
                                    for (int veh = 0; veh < listofVehiclesArr.length(); ++veh) {
                                        JSONObject vehAr = listofVehiclesArr.getJSONObject(veh);
                                        VehicleDetailProfileParam vehObj = new VehicleDetailProfileParam();
                                        vehObj.setVehicleMake((vehAr.isNull("Make")) ? "" :
                                                            String.valueOf(vehAr.get("Make")));
                                        vehObj.setVehicleType((vehAr.isNull("VehicleType")) ? "" :
                                                            String.valueOf(vehAr.get("VehicleType")));
                                        vehObj.setLicensePlateNum((vehAr.isNull("RegistrationNo")) ? "" :
                                                                  String.valueOf(vehAr.get("RegistrationNo")));
                                        vehObj.setVehicleFuelType((vehAr.isNull("FuelType")) ? "" :
                                                                 String.valueOf(vehAr.get("FuelType")));                                    
                                        vehObj.setVehicleModel((vehAr.isNull("VehicleModel")) ? "" :
                                                                 String.valueOf(vehAr.get("VehicleModel")));
                                        //VehTypeId has status as active/inactive
                                        vehObj.setVehicleTypeId((vehAr.isNull("VehicleStatus")) ? "" :
                                                                 String.valueOf(vehAr.get("VehicleStatus")));                                    
                                        vehObj.setVehicleRowId((vehAr.isNull("VehicleModelId")) ? "" :
                                                                 String.valueOf(vehAr.get("VehicleModelId")));                                     
                                        vehObj.setVehicleTaxExpDt((vehAr.isNull("TaxExpiryDate")) ? "" :
                                                                 String.valueOf(vehAr.get("TaxExpiryDate")));
                                        vehObj.setVehicleInsExp((vehAr.isNull("InsuranceExpiryDate")) ? "" :
                                                                 String.valueOf(vehAr.get("InsuranceExpiryDate")));
                                        vehObj.setVehiclePUC((vehAr.isNull("PUCExpiryDate")) ? "" :
                                                                 String.valueOf(vehAr.get("PUCExpiryDate")));
                                        vehObj.setIsPrimaryVehicle((vehAr.isNull("PrimaryVehicleFlg")) ? "" :
                                                                 String.valueOf(vehAr.get("PrimaryVehicleFlg")));                                    
                                        vehObj.setVehicleOwnership((vehAr.isNull("VehicleOwnershipType")) ? "" :
                                                                 String.valueOf(vehAr.get("VehicleOwnershipType")));                       
                                        userBn.getVehicleProfileDetails().add(vehObj);
                                    }
                                  }
                                }
                                
                                /**------------End of Code to Set Retail Related Parameters-----------**/


                            userBn.setConsumerName((jsonObject.isNull("FirstName") == true ? "" :
                                                    (jsonObject.get("FirstName") == null ? "" :
                                                     jsonObject.getString("FirstName"))) + " " +
                                                   (jsonObject.isNull("MiddleName") == true ? "" :
                                                    (String.valueOf(jsonObject.get("MiddleName"))
                                                     .equalsIgnoreCase("null") ? "" :
                                                     jsonObject.getString("MiddleName"))) + " " +
                                                   (jsonObject.isNull("LastName") == true ? "" :
                                                    (jsonObject.get("LastName") == null ? "" :
                                                     jsonObject.getString("LastName"))));
                            userBn.setConsumerStatus(jsonObject.isNull("ConsumerStatus") == true ? "" :
                                                     jsonObject.getString("ConsumerStatus"));
                            userBn.setBankkycStatus(jsonObject.isNull("BankKYCStatus") == true ? "" :
                                                    jsonObject.getString("BankKYCStatus"));
                            userBn.setBankkycStatus(jsonObject.isNull("BankLinkedtoLPG") == true ? "" :
                                                    jsonObject.getString("BankLinkedtoLPG"));
                            userBn.setAadhaarLinkedtoBank(jsonObject.isNull("AadhaarLinkedtoBank") == true ? "" :
                                                          jsonObject.getString("AadhaarLinkedtoBank"));
                            userBn.setKycLevel(jsonObject.isNull("KYCLevel") == true ? "" :
                                               jsonObject.getString("KYCLevel"));
                            userBn.setFirstName((jsonObject.isNull("FirstName") == true ? "" :
                                                 (jsonObject.get("FirstName") == null ? "" :
                                                  jsonObject.getString("FirstName"))));
                            userBn.setMiddleName((jsonObject.isNull("MiddleName") == true ? "" :
                                                  (jsonObject.get("MiddleName") == null ? "" :
                                                   jsonObject.getString("MiddleName"))));
                            userBn.setLastName((jsonObject.isNull("LastName") == true ? "" :
                                                (jsonObject.get("LastName") == null ? "" :
                                                 jsonObject.getString("LastName"))));
                            userBn.setNameChangeEligibility(String.valueOf(jsonObject.getString("ContactNameChangeEligible"))
                                                            .equalsIgnoreCase("Y") ? true : false);
                            userBn.setPrefix(jsonObject.isNull("Name_Status") ? "" :
                                             jsonObject.getString("Name_Status"));

                            userBn.setTempConsumerName(jsonObject.isNull("FirstName") ? "" :
                                                       jsonObject.getString("FirstName") + " " +
                                                       (jsonObject.isNull("LastName") ? "" :
                                                        jsonObject.getString("LastName")));


                            userBn.setRole(map.get(EPICConstant.HEADER_OAM_ROLE));
                            userBn.setUcmID(map.get(EPICConstant.HEADER_OAM_UCMID));
                            userBn.setIdamUId(map.get(EPICConstant.HEADER_OAM_UID));

                            if (map.get(EPICConstant.HEADER_OAM_FIRST_NAME) != null) {
                                userBn.setIdamUserFirstName(map.get(EPICConstant.HEADER_OAM_FIRST_NAME));
                            }
                            if (map.get(EPICConstant.HEADER_OAM_LAST_NAME) != null) {
                                userBn.setIdamUserLastName(map.get(EPICConstant.HEADER_OAM_LAST_NAME));
                            }

                            if (map.get(EPICConstant.HEADER_OAM_MOBILE) != null) {
                                userBn.setMobile(map.get(EPICConstant.HEADER_OAM_MOBILE)); //This Value get overrided with Relationship Mobile
                                userBn.setIdamUserMobileNo(map.get(EPICConstant.HEADER_OAM_MOBILE)); //This Store mobile No. from Idam header
                            }
                            if (map.get(EPICConstant.HEADER_OAM_EMAIL) != null) {
                                userBn.setEmail(map.get(EPICConstant.HEADER_OAM_EMAIL));
                                userBn.setIdamUserEmailId(map.get(EPICConstant.HEADER_OAM_EMAIL));
                            }
                            log.info("setting ends===" + mapSession.get(EPICConstant.SESSION_USER_DETAILS));

                            mapSession.put("userDetails", userBn); // added this  as recommended by Vikas U

                            //                        CommonHelper.setEL("#{sessionScope.userDetails}",userBn); // mapSession Paramter cause NPE in case of reexecution in 30sec
                            log.info("UserBean===" + mapSession.get(EPICConstant.SESSION_USER_DETAILS));

                            /**----------Loop 4 End-----------**/
                        } else if ((returnList != null && returnList.get(0) != null) &&
                                   (returnList.get(0)
                                                                                                   .toString()
                                                                                                   .equalsIgnoreCase("false") &&
                                    returnList.get(1) != null)) {
                            JSONObject jsonResponse = new JSONObject(returnList.get(1).toString());

                            param.put("pOverviewValidCode",
                                      jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                                      jsonResponse.getString(EPICConstant.ERROR_CODE));
                            param.put("pOverviewValidMsg",
                                      jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                                      jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


                            if ((param.get("pOverviewValidCode") != null &&
                                 param.get("pOverviewValidCode")
                                                                                 .toString()
                                                                                 .equalsIgnoreCase(EPICConstant.SBL100)) &&
                                                                   (param.get("pOverviewValidMsg") != null &&
                                                                    param.get("pOverviewValidMsg")
                                                                                                                   .toString()
                                                                                                                   .equalsIgnoreCase(EPICConstant.UCM_ID_NOT_FOUND))) {
                                log.info("Business Validation Issue SBL-100 ErrorCode Received");
                                log.info("UCM Id Not Found Case Executed");
                                log.info("LPGCurrSection set to NewConnectionInProcess");
                                sessionParam.put("LPGCurrSection", "NewConnectionInProcess");
                            } else {
                                log.info("Some Error in WebService Response");
                                retString = "ERROR";
                            }
                        } else {
                            log.info("Error from Service ReturnList.get(0) is not true");
                            retString = "ERROR";
                        }
                        log.info("session end..");
                        /**----------Loop 3 End-----------**/
                    }


                    /**----------Loop 2 End-----------**/
                }
                /**----------Loop 1 End-----------**/
            }

            if (user_auth) {
                log.info("user is Authorised. Setting IS_LOGGED_IN Parameter Value to True");
                mapSession.put(EPICConstant.SESSION_IS_LOGGED_IN, EPICConstant.IS_LOGGED_IN_VAL);
            }

            log.info("isKYCclicked: " + mapSession.get("isKYCclicked"));

            if (blank_ucmId && user_auth && mapSession.get("isKYCclicked") == null) {
                // Setting userprofile in session //
                log.info("------------Blank UCM Id Case Executed----------");
                UserProfileBean userBn = new UserProfileBean();
                userBn.setFirstName(map.get(EPICConstant.HEADER_OAM_FIRST_NAME));
                userBn.setIdamUserFirstName(map.get(EPICConstant.HEADER_OAM_FIRST_NAME)); //Setting Idam First Name

                userBn.setLastName(map.get(EPICConstant.HEADER_OAM_LAST_NAME));
                userBn.setIdamUserLastName(map.get(EPICConstant.HEADER_OAM_LAST_NAME)); //Setting Idam Last Name

                if (!StringUtils.isBlank(map.get(EPICConstant.HEADER_OAM_MOBILE)) &&
                    !map.get(EPICConstant.HEADER_OAM_MOBILE).equalsIgnoreCase(EPICConstant.NOTFOUND)) {
                    userBn.setMobile(map.get(EPICConstant.HEADER_OAM_MOBILE));
                    userBn.setIdamUserMobileNo(map.get(EPICConstant.HEADER_OAM_MOBILE)); //This Store mobile No. from Idam header
                }
                if (!StringUtils.isBlank(map.get(EPICConstant.HEADER_OAM_MOBILE)) &&
                    !map.get(EPICConstant.HEADER_OAM_MOBILE).equalsIgnoreCase(EPICConstant.NOTFOUND)) {
                    userBn.setConsumerContactNumber(map.get(EPICConstant.HEADER_OAM_MOBILE));
                }
                if (!StringUtils.isBlank(map.get(EPICConstant.HEADER_OAM_EMAIL)) &&
                    !map.get(EPICConstant.HEADER_OAM_EMAIL).equalsIgnoreCase(EPICConstant.NOTFOUND)) {
                    userBn.setConsumerEmailId(map.get(EPICConstant.HEADER_OAM_EMAIL));
                    userBn.setIdamUserEmailId(map.get(EPICConstant.HEADER_OAM_EMAIL));
                }
                userBn.setIdamUId(map.get(EPICConstant.HEADER_OAM_UID));
                userBn.setHasBlankUCMId("Y");
                log.info("-----User Will move to Customer Portal with Option of Link Your LPGID-----");
                String tempName = null;
                tempName =
                    map.get(EPICConstant.HEADER_OAM_FIRST_NAME) + " " + map.get(EPICConstant.HEADER_OAM_LAST_NAME);
                log.info("----------Temp Consumer Name is--------" + tempName);

                userBn.setTempConsumerName(tempName); // Temp Name is Set
                userBn.setConsumerName(tempName); //Consumer Name is Set

                mapSession.put("userDetails", userBn); // added this  as recommended by Vikas U
                //                CommonHelper.setEL("#{sessionScope.userDetails}", userBn);


                //            mapSession.put("LPGCurrSection", "NewConnection");
                //            mapSession.put("currentTabInCustomer", "lpg");
                //            /*-------------------Code to Move to External Context-----------*/
                //            log.info("Taskflow SourceFlow Paramter Value is: "+pageflowParam.get("SourceFlow"));
                //                if (pageflowParam.get("SourceFlow") != null &&
                //                    pageflowParam.get("SourceFlow").equals("CustomerLPG")) {
                //                } else {
                //                    String trimURL = "/webcenter/portal/Customer/pages_lpg";
                //                    String openURL = "window.location.replace(\"" + trimURL + "\");";
                //                    CommonHelper.runJavaScript(openURL);
                //                }
                /*----------------------------End Here-------------------------*/
            }

            //        Integer relCounts = CommonHelper.evaluateEL("#{sessionScope.userDetails.lpgRelationsCount}") == null ? 0 : Integer.parseInt(CommonHelper.evaluateEL("#{sessionScope.userDetails.lpgRelationsCount}").toString());
            //        log.info("relCounts "+relCounts);
            //        if (relCounts == 0 && blank_ucmId == false && user_auth && mapSession.get("isKYCclicked") == null) {
            //            log.info("lpgRelationsCount == 0 so User Move to NewConnection");
            //            mapSession.put("LPGCurrSection", "NewConnection");
            //            mapSession.put("currentTabInCustomer", "lpg");
            //            /*-------------------Code to Move to External Context-----------*/
            //            log.info("Taskflow SourceFlow Paramter Value is: "+pageflowParam.get("SourceFlow"));
            //                if (pageflowParam.get("SourceFlow") != null &&
            //                    pageflowParam.get("SourceFlow").equals("CustomerLPG")) {
            //                } else {
            //                    String trimURL = "/webcenter/portal/Customer/pages_lpg";
            //                    String openURL = "window.location.replace(\"" + trimURL + "\");";
            //                    CommonHelper.runJavaScript(openURL);
            //                }
            //            /*----------------------------End Here-------------------------*/
            //        }
        } catch (Exception e) {
            log.info("error Inside initializeMethod");
            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
            e.printStackTrace();
        }
        log.info("here==ends..");
        log.info("--------------------Inside initializeMethod in Customer End------------------");

        return retString;
    }

    public String openDetailsPopup() {
        String retString = null;
        try {
            String selectedRowID = (String) param.get("passServiceId");

            SRData objSRData = new SRData();

            OperationBinding ob = CommonHelper.findOperation("serviceCustomerCall");
            List lstInput = new ArrayList();
            lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("GET_SR"));

            JSONObject jsonInput = new JSONObject();

            jsonInput.put("SRNumber", selectedRowID);

            System.out.println("==>" + jsonInput);

            lstInput.add(1, jsonInput);

            java.util.Map params = ob.getParamsMap();
            params.put(EPICConstant.SERVICELIST, lstInput);
            params.put(EPICConstant.SERVICEMETHOD, EPICConstant.GETSR);

            List retValue = new ArrayList();
            retValue = (List) ob.execute();

            if (!ob.getErrors().isEmpty()) {
                retString = "ERROR";
                return retString;
            }
            List result = (List) ob.getResult();
            if (result == null) {
                retString = "ERROR";
                return retString;
            }
            if (result.size() > 0) {

                if (CustomerValidation.isNull(result.get(0)) || result.get(0)
                                                                      .toString()
                                                                      .equalsIgnoreCase("TRUE")) {

                    JSONObject jsonObject = (JSONObject) retValue.get(1);

                    JSONArray arrObj = jsonObject.getJSONArray("SRDetails");
                    JSONObject objSR = arrObj.getJSONObject(0);

                    String closureDate = objSR.isNull("SRClosedDate") ? "NA" : objSR.getString("SRClosedDate");
                    if (closureDate != "NA") {
                        param.put("selectClosureDate", CommonHelper.convertDateFormat(closureDate));
                    } else {
                        param.put("selectClosureDate", closureDate);
                    }

                    param.put("selectConsumerName",
                              objSR.isNull("ConsumerName") ? "NA" : objSR.getString("ConsumerName"));
                    param.put("selectEmailAddress",
                              objSR.isNull("EmailAddress") ? "NA" : objSR.getString("EmailAddress"));
                    param.put("selectCreationDate",
                              objSR.isNull("SRCreatedDate") ? "NA" : objSR.getString("SRCreatedDate"));
                    param.put("selectComplaintDetails",
                              objSR.isNull("ComplaintDetails") ? "NA" : objSR.getString("ComplaintDetails"));
                    param.put("selectResolutionRemark",
                              objSR.isNull("ResolutionRemarks") ? "NA" : objSR.getString("ResolutionRemarks"));

                    RichPopup.PopupHints hints = new RichPopup.PopupHints();
                    detailsPopup.show(hints);

                } else {

                    retString = "ERROR";
                    return retString;
                }

            } else {

                retString = "ERROR";
                return retString;
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            retString = "ERROR";

        }
        return retString;
    }


    public void closeDetailsPopup() {
        detailsPopup.hide();
    }
    
    /**
     * setServiceDetails Method get list of services
     * from db table and set in userSession Bean
     * **/
    public void setServiceDetails() {
        Map<String, Object> mapSession = FacesContext.getCurrentInstance()
                                                     .getExternalContext()
                                                     .getSessionMap();
        // Setting userprofile in session //
        UserProfileBean userBn = null;
        if (mapSession.get("userDetails") == null) {
            userBn = new UserProfileBean();
        } else {
            userBn = (UserProfileBean) mapSession.get("userDetails");
        }

        if (userBn.getServiceDetails().isEmpty()) {
            log.info("Inside setServiceDetails Setting Service URL Details");
            DCIteratorBinding iter = CommonHelper.findIterator("ServiceLogsSaveVO1Iterator");
            Row[] row = iter.getAllRowsInRange();
            for (int i = 0; i < row.length; i++) {
                if (!userBn.getServiceDetails()
                    .containsKey(String.valueOf(row[i].getAttribute("SvcShortUrl").toString()))) {
                    userBn.getServiceDetails()
                        .put(row[i].getAttribute("SvcShortUrl").toString(),
                             new ServiceParam(row[i].getAttribute("Id").toString(),
                                              row[i].getAttribute("SvcShortUrl").toString(),
                                              row[i].getAttribute("IsSaveLogs") == null ? "N" :
                                              row[i].getAttribute("IsSaveLogs").toString()));

                }

            }

            mapSession.put("userDetails", userBn); // recommended by Vikas U
            
//            for (Map.Entry<String, ServiceParam> entry : userBn.getServiceDetails().entrySet()) 
//            {
//                System.out.println("Key = " + entry.getKey()); 
//                ServiceParam obj =  entry.getValue();
//                log.info("Id: "+obj.getId());
//                log.info("ServiceShortURl: "+obj.getServiceShortURL());
//                log.info("LogEnabled: "+obj.getIsLogEnabled());
//            }
            
        }

    }
}
