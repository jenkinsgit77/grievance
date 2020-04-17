package com.iocl.lpg.customer.bean.selectAccount;


import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;
import com.iocl.lpg.customer.utils.UCMUtil;

import ioclcommonproj.com.iocl.beans.AddressParams;
import ioclcommonproj.com.iocl.beans.CustomerRelationBean;
import ioclcommonproj.com.iocl.beans.EmailParams;
import ioclcommonproj.com.iocl.beans.IdentityParams;
import ioclcommonproj.com.iocl.beans.MobileNoParams;
import ioclcommonproj.com.iocl.beans.ServiceParam;
import ioclcommonproj.com.iocl.beans.UserProfileBean;

import ioclcommonproj.com.iocl.beans.VehicleDetailsParam;

import ioclcommonproj.com.iocl.beans.VehicleDetailProfileParam;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.nav.RichButton;

import oracle.binding.OperationBinding;

import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.event.PollEvent;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.Row;


public class ShowRelationPopup implements Serializable {
    @SuppressWarnings("compatibility:-1014389671202177574")
    private static final long serialVersionUID = 1L;

    private static Logger log ;
    private RichPopup lpgConnPopUpBinding;
    private RichButton lpgSectionSubmitBtnBinding;
    private RichSelectOneRadio lpgConnBinding;

    public ShowRelationPopup() {
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(ShowRelationPopup.class);

        } else {
            log = Logger.getLogger(ShowRelationPopup.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    java.util.Map sessionscope = ADFContext.getCurrent().getSessionScope();
    java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();

    private java.util.List<CustomerRelationBean> customerDetailsLst = new java.util.ArrayList<CustomerRelationBean>();
    private String lpgConnecErrorMsg;

    public String showRelationPopupOnLoad() {
       // String retString = "goToRouter";
        String retString = null;
        pageflowParam.put("ExternalNavigation", null);
        log.info("ExternalNavigation set to null");
        Map<String, Object> mapSession = FacesContext.getCurrentInstance()
                                                             .getExternalContext()
                                                             .getSessionMap();
        log.info("------------------Inside showRelationPopupOnLoad Customer Start-------------------");
        initializeMethod();
        Boolean popUpCall = true;
        Boolean multipleActiveConn = false;
        Integer sameStatusCount = 0;
        String vSelecConsumerId = null;
        String lpgMultipleConn = null;
        Boolean connectionSelected = false;
        /*---------------------Code to get Multiple LPG Connection----------------------------------*/
        Map<String, CustomerRelationBean> lpgRelations = CommonHelper.evaluateEL("#{sessionScope.userDetails.lpgRelations}") == null ? null :
            (HashMap<String, CustomerRelationBean>) CommonHelper.evaluateEL("#{sessionScope.userDetails.lpgRelations}");

        log.info("LPGCurrSection in showRelationPopupOnLoad in LPG : " + sessionscope.get("LPGCurrSection"));
        if (sessionscope.get("LPGCurrSection") == null && lpgRelations != null) {

            lpgMultipleConn =
                String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.lpgMultipleConnection}"));
            log.info("lpgMultipleConn in showRelationPopupOnLoad " + lpgMultipleConn);
            if (lpgMultipleConn != null && lpgMultipleConn.equalsIgnoreCase("Y")) {

                if (popUpCall) {
                    for (Map.Entry<String, CustomerRelationBean> entry : lpgRelations.entrySet()) {
                        CustomerRelationBean obj = entry.getValue();
                        
                        /**-------Code to Check Active Relationship and Set it by Default--**/
                        if (obj.getConnectionStatus() != null && obj.getConnectionStatus().equalsIgnoreCase("1")) {
                            if (obj.getConnectionSubStatus().equalsIgnoreCase("102") && connectionSelected.equals(false)) {
                                obj.setConnectionSelected(true);
                                connectionSelected = true;
                            }
                        }
                        customerDetailsLst.add(obj); // Adding All Customer Details to List to Show in PopUp
                    }
                    
                    // Setting userprofile in session //
                    UserProfileBean userBn = null;
                    if (mapSession.get("userDetails") == null) {
                        userBn = new UserProfileBean();
                    } else {
                        userBn = (UserProfileBean) mapSession.get("userDetails");
                    }
                    
                    userBn.setCustomerDetails(customerDetailsLst);
                    mapSession.put("userDetails", userBn); // recommended by Vikas U
                }
            } else if (lpgMultipleConn.equalsIgnoreCase("N")) {
               retString = updateActiveLpgConn(); //Method to Update Customer Profile Parameters
            }

        }

        /*-------------------------End Here----------------------------*/
        log.info("showRelationPopupOnLoad in End lpgMultipleConn: " +
                 String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.lpgMultipleConnection}")));
        log.info("LPGCurrSection: "+sessionscope.get("LPGCurrSection"));

        log.info("-----------------------------------------End Here----------------------------------------------------------");
        if (pageflowParam.get("ExternalNavigation") != null && pageflowParam.get("ExternalNavigation")
                                                                            .toString()
                                                                            .equals("Y")) {
            log.info("----------------retString Set to externalNav---------------");
            retString = "externalNav";
        }
        return (retString == null ? "goToRouter" : retString );
    }


    public void initializeMethod() {
        log.info("--------------------Inside initializeMethod in Customer Start------------------");
        //this is foe BI testing to be removed
        //CommonHelper.setEL("1234", "{sessionScope.PCOSNID}");
        //CommonHelper.setEL("2017", "{sessionScope.PYEAR}");
        
        
        boolean fbExistFlag = false;
        boolean twitExistFlag = false;
        boolean gmailExistFlag = false;
        boolean user_auth = false; //Parameter to Check whether user is authenticated or not
        boolean blank_ucmId = false;
        Integer lpgRelationsCount = 0;
        
        CommonHelper.setEL("#{sessionScope.PCOSNID}", "1001");

        try{
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
                if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_UCMID) &&(value.equalsIgnoreCase(EPICConstant.NOT_FOUND) || value.equalsIgnoreCase("null"))) {
                    log.info("-------------------UCM Id  Value is Blank blank_ucmId is set :true---------------------");
                    blank_ucmId = true;
                }
                if (key.equalsIgnoreCase(EPICConstant.HEADER_OAM_UCMID) &&
                                                (!StringUtils.isBlank(value) && !value.equalsIgnoreCase(EPICConstant.NOTFOUND)) &&
                                                (!value.equalsIgnoreCase("null")&& mapSession.get(EPICConstant.SESSION_IS_LOGGED_IN) == null)) {
                    log.info("--------------------Got UCMId ------------------");
                    /**----------Loop 3 Start-----------**/
                    
                    log.info("UserTrackingId inside Service loop : "+mapSession.get(EPICConstant.USER_TRACKING_ID));
                    jsonHeaderMap.put(EPICConstant.USER_TRACKING_ID,mapSession.get(EPICConstant.USER_TRACKING_ID)); //Setting USER_TRACKING_ID to insert from bean

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
                        returnList.get(0).toString().equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
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
                                                                              relationAr.isNull("PartnerCode") ? "" :
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
                                            //                                        break; // To break from outer loop once we get LPG Type Relationship
                                        }
                                    }
                                    if ((relationAr.isNull("Type") == false &&
                                         relationAr.getString("Type").equalsIgnoreCase("Loyalty"))) {
                                             log.info("Setting Loyalty Relationship in 360 degree to Y");
                                             mapSession.put("LoyaltyRelationship","Y");
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
                            userBn.setBalancePoints(loyMemObj.isNull("BalancePoints") ? EPICConstant.NO_VALUE :
                                                    String.valueOf(loyMemObj.get("BalancePoints")));
                        }
                    }
                    JSONArray vehicleDetailsArr =
                        jsonObject.isNull("VehicleList") ? null : jsonObject.getJSONArray("VehicleList");
                    if (vehicleDetailsArr != null) {
                        for (int vd = 0; vd < vehicleDetailsArr.length(); ++vd) {
                            JSONObject vehDetailsObj = vehicleDetailsArr.getJSONObject(vd);
                            VehicleDetailsParam vehObj = new VehicleDetailsParam();
                            vehObj.setVehicleType(vehDetailsObj.isNull("VehicleType") ? EPICConstant.NO_VALUE :
                                                  String.valueOf(vehDetailsObj.get("VehicleType")));
                            vehObj.setVehicleMake(vehDetailsObj.isNull("FuelType") ? EPICConstant.NO_VALUE :
                                                  String.valueOf(vehDetailsObj.get("FuelType")));
                            vehObj.setRegistrationNo(vehDetailsObj.isNull("RegistrationNo") ? EPICConstant.NO_VALUE :
                                                     String.valueOf(vehDetailsObj.get("RegistrationNo")));
                            userBn.getVehicleDetails().add(vehObj);
                        }
                        mapSession.put("sVehicleDetailsExist", "Y");
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
                                userBn.setConsumerId(obj.getConsumerId());//Setting ConsumerId Early for Report Section
                                userBn.setSelectedRelationshipRowId(obj.getRelationshipRowId());
                                log.info("ConsumerId for Single LPG Connection " + obj.getConsumerId()+" & relationshipRowId: "+obj.getRelationshipRowId());
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
                                identityObj.setIdentityValue(listIdAr.isNull("Number") ? EPICConstant.NOT_APPLICABLE :
                                                             CommonHelper.maskedMobileNumber(String.valueOf(listIdAr.get("Number"))));
                                identityObj.setIdentityContentId(listIdAr.isNull("WCCContentId") ?
                                                                 EPICConstant.NOT_APPLICABLE :
                                                                 String.valueOf(listIdAr.get("WCCContentId")));
                                identityObj.setDocumentId(listIdAr.isNull("WCCDocId") ? EPICConstant.NOT_APPLICABLE :
                                                          String.valueOf(listIdAr.get("WCCDocId")));
                                
                                identityObj.setDocumentName(listIdAr.isNull("FileName") ? EPICConstant.NOT_APPLICABLE :
                                                            String.valueOf(listIdAr.get("FileName")));
                                String contentURL="";
                                String extensionFormed="";
                                        if (listIdAr.isNull("WCCContentId")) {
                                                identityObj.setDocumentExtension(extensionFormed);
                                                log.info("No profile identity document found,wcc content id is null");
                                        } else {
                                                //  if (null != listIdAr.get("WCCContentId")) {
                                                  log.info("getting profile identity doc location for content id::" +
                                                           String.valueOf(listIdAr.get("WCCContentId")));
                                                    try{
                                                          extensionFormed =
                                                              UCMUtil.returnDocExtension(String.valueOf(listIdAr.get("WCCContentId")));                                                          
                                                    }catch(Exception exc){
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
                                if (listIdAr.isNull("FileName")== false && listIdAr.getString("FileName").equalsIgnoreCase("Aadhaar(UID)")) {
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
                                mobileObj.setMobilePrimary(mobAr.isNull("PrimaryFlag") ? EPICConstant.NOT_APPLICABLE :
                                                           String.valueOf(mobAr.get("PrimaryFlag")));
                                mobileObj.setMobileVerified(mobAr.isNull("VerifiedFlag") ? EPICConstant.NOT_APPLICABLE :
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
                                emailObj.setEmailVerified(emAr.isNull("VerifiedFlag") ? EPICConstant.NOT_APPLICABLE :
                                                          String.valueOf(emAr.get("VerifiedFlag")));
                                emailObj.setEmailPrimary(emAr.isNull("PrimaryFlag") ? EPICConstant.NOT_APPLICABLE :
                                                         String.valueOf(emAr.get("PrimaryFlag")));
                                userBn.getEmailDetails().add(emailObj);
                            
                            }
                        }

                        JSONArray socProfileArray =
                            jsonObject.isNull("SocialIdentities") ? null : jsonObject.getJSONArray("SocialIdentities");
                        if (socProfileArray != null) {
                            for (int sP = 0; sP < socProfileArray.length(); ++sP) {
                                JSONObject socPArr = socProfileArray.getJSONObject(sP);
                                userBn.setFbId(socPArr.isNull("FacebookEmail") ? EPICConstant.NOT_APPLICABLE :
                                               String.valueOf(socPArr.get("FacebookEmail")));
                                userBn.setGmailId(socPArr.isNull("GmailEmail") ? EPICConstant.NOT_APPLICABLE :
                                                  String.valueOf(socPArr.get("GmailEmail")));
                                userBn.setTwitterId(socPArr.isNull("TwitterEmail") ? EPICConstant.NOT_APPLICABLE :
                                                    String.valueOf(socPArr.get("TwitterEmail")));

                                if (String.valueOf(userBn.getFbId()).equalsIgnoreCase(EPICConstant.NOT_APPLICABLE)) {
                                    fbExistFlag = false;
                                } else {
                                    fbExistFlag = true;
                                }
                                if (String.valueOf(userBn.getGmailId()).equalsIgnoreCase(EPICConstant.NOT_APPLICABLE)) {
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
                                                (String.valueOf(jsonObject.get("MiddleName")).equalsIgnoreCase("null") ?
                                                 "" : jsonObject.getString("MiddleName"))) + " " +
                                               (jsonObject.isNull("LastName") == true ? "" :
                                                (jsonObject.get("LastName") == null ? "" :
                                                 jsonObject.getString("LastName"))));
                        userBn.setConsumerStatus(jsonObject.isNull("ConsumerStatus") == true ? "" :
                                                 jsonObject.getString("ConsumerStatus"));
                        userBn.setBankkycStatus(jsonObject.isNull("BankKYCStatus") == true ? "" :
                                                jsonObject.getString("BankKYCStatus"));
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
                        userBn.setPrefix(jsonObject.isNull("Name_Status")? "" : jsonObject.getString("Name_Status"));

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
                                userBn.setIdamUserEmailId(map.get(EPICConstant.HEADER_OAM_EMAIL)); //This Store Email Id from Idam header
                            }


                            log.info("setting ends===" + mapSession.get(EPICConstant.SESSION_USER_DETAILS));

                          mapSession.put("userDetails",userBn); // recommended by Vikas U
//                        CommonHelper.setEL("#{sessionScope.userDetails}",userBn); // mapSession Paramter cause NPE in case of reexecution in 30sec
                        log.info("UserBean===" + mapSession.get(EPICConstant.SESSION_USER_DETAILS));

                        /**----------Loop 4 End-----------**/
                    }else if ((returnList != null && returnList.get(0) != null) &&
                                   (returnList.get(0).toString().equalsIgnoreCase("false") &&
                                    returnList.get(1) != null)) {
                            JSONObject jsonResponse = new JSONObject(returnList.get(1).toString());

                            pageflowParam.put("pShowRelValidCode",
                                      jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                                      jsonResponse.getString(EPICConstant.ERROR_CODE));
                            pageflowParam.put("pShowRelValidMsg",
                                      jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                                      jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


                            if ((pageflowParam.get("pShowRelValidCode") != null &&
                                pageflowParam.get("pShowRelValidCode").toString().equalsIgnoreCase(EPICConstant.SBL100)) && (pageflowParam.get("pShowRelValidMsg") != null &&
                                pageflowParam.get("pShowRelValidMsg").toString().equalsIgnoreCase(EPICConstant.UCM_ID_NOT_FOUND))){
                                log.info("Business Validation Issue SBL-100 ErrorCode Received");
                                log.info("UCM Id Not Found Case Executed");
                                log.info("LPGCurrSection set to NewConnectionInProcess");
                                
                                sessionscope.put("LPGCurrSection","NewConnectionInProcess");
                                if (pageflowParam.get("SourceFlow") != null && pageflowParam.get("SourceFlow").equals("NormalFlow")) {
                                    } else {
                                        mapSession.put("currentTabInCustomer", "lpg");
                                    }
                                /*-------------------Code to Move to External Context-----------*/
                                log.info("initializeMethod Taskflow SourceFlow Paramter Value is: "+pageflowParam.get("SourceFlow"));
                                    if (pageflowParam.get("SourceFlow") != null &&
                                        (pageflowParam.get("SourceFlow").equals("CustomerLPG") || pageflowParam.get("SourceFlow").equals("NormalFlow"))) {
                                    } else {
                                        String trimURL = "/webcenter/portal/Customer/pages_lpg";
                        //                        String openURL = "window.location.replace(\"" + trimURL + "\");";
                        //                        CommonHelper.runJavaScript(openURL);
                        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
                        try {
                            exct.redirect(trimURL);
                            FacesContext.getCurrentInstance().responseComplete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        pageflowParam.put("ExternalNavigation", "Y");
                                    }
                                /*----------------------------End Here-------------------------*/
                            } else {
                                log.info("Some Error in WebService Response");
                                mapSession.put("LPGCurrSection",null);// Parameter Set to Route to Error Page
                            }
                        } else {
                            log.info("Error from Service ReturnList.get(0) is not true");
                            mapSession.put("LPGCurrSection",null);// Parameter Set to Route to Error Page
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
        
        log.info("isKYCclicked: "+mapSession.get("isKYCclicked"));
        
        if (blank_ucmId && user_auth && mapSession.get("isKYCclicked") == null) {
            // Setting userprofile in session //
            log.info("------------Blank UCM Id Case Executed----------");
            UserProfileBean userBn = null;
            if (mapSession.get("userDetails") == null) {
                userBn = new UserProfileBean();
            } else {
                userBn = (UserProfileBean) mapSession.get("userDetails");
            }
            
            userBn.setFirstName(map.get(EPICConstant.HEADER_OAM_FIRST_NAME));
            userBn.setIdamUserFirstName(map.get(EPICConstant.HEADER_OAM_FIRST_NAME));//Setting Idam First Name
            
            userBn.setLastName(map.get(EPICConstant.HEADER_OAM_LAST_NAME));
            userBn.setIdamUserLastName(map.get(EPICConstant.HEADER_OAM_LAST_NAME));//Setting Idam Last Name
            
            if (!StringUtils.isBlank(map.get(EPICConstant.HEADER_OAM_MOBILE)) &&
                !map.get(EPICConstant.HEADER_OAM_MOBILE).equalsIgnoreCase(EPICConstant.NOTFOUND)) {
                userBn.setMobile(map.get(EPICConstant.HEADER_OAM_MOBILE));
                userBn.setIdamUserMobileNo(map.get(EPICConstant.HEADER_OAM_MOBILE));//This Store mobile No. from Idam header
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
            tempName =  map.get(EPICConstant.HEADER_OAM_FIRST_NAME)+" "+map.get(EPICConstant.HEADER_OAM_LAST_NAME);
            log.info("----------Temp Consumer Name is--------"+tempName);
            userBn.setTempConsumerName(tempName);// Temp Name is Set
            userBn.setConsumerName(tempName);//Consumer Name is Set
            
            mapSession.put("userDetails", userBn); // added this recommended by Vikas
//            CommonHelper.setEL("#{sessionScope.userDetails}", userBn); // commented this as recommended by Vikas

            mapSession.put("LPGCurrSection", "NewConnection");
            if (pageflowParam.get("SourceFlow") != null && pageflowParam.get("SourceFlow").equals("NormalFlow")) {
                } else {
                    mapSession.put("currentTabInCustomer", "lpg");
                }
           
            /*-------------------Code to Move to External Context-----------*/
            log.info("initializeMethod Taskflow SourceFlow Paramter Value is: "+pageflowParam.get("SourceFlow"));
                if (pageflowParam.get("SourceFlow") != null &&
                    (pageflowParam.get("SourceFlow").equals("CustomerLPG") || pageflowParam.get("SourceFlow").equals("NormalFlow"))) {
                } else {
                    String trimURL = "/webcenter/portal/Customer/pages_lpg";
                    //                    String openURL = "window.location.replace(\"" + trimURL + "\");";
                    //                    CommonHelper.runJavaScript(openURL);
                    ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
                    try {
                        exct.redirect(trimURL);
                        FacesContext.getCurrentInstance().responseComplete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pageflowParam.put("ExternalNavigation", "Y");
                }
            /*----------------------------End Here-------------------------*/
        }
        
        Integer relCounts = CommonHelper.evaluateEL("#{sessionScope.userDetails.lpgRelationsCount}") == null ? -1 : Integer.parseInt(CommonHelper.evaluateEL("#{sessionScope.userDetails.lpgRelationsCount}").toString());
        if (relCounts == 0 && blank_ucmId == false && user_auth && mapSession.get("isKYCclicked") == null) {
            log.info("-----------LPG 0 Relationship Case Executed------------"); 
            if (pageflowParam.get("SourceFlow") != null && pageflowParam.get("SourceFlow").equals("NormalFlow")) {

                } else {
                    mapSession.put("LPGCurrSection", "NewConnection");
                    mapSession.put("currentTabInCustomer", "lpg");
                }
            /*-------------------Code to Move to External Context-----------*/
            log.info("initializeMethod Taskflow SourceFlow Paramter Value is: "+pageflowParam.get("SourceFlow"));
                if (pageflowParam.get("SourceFlow") != null &&
                    (pageflowParam.get("SourceFlow").equals("CustomerLPG") || pageflowParam.get("SourceFlow").equals("NormalFlow"))) {
                } else if(sessionscope.get("sDestinationTo") != null && (sessionscope.get("sDestinationTo").toString().equalsIgnoreCase("CustomerProfilePage"))){ 
                    sessionscope.put("sDestinationTo", null);
                    log.info("Passing Navigation as sDestinationTo equals CustomerProfilePage");
                }else {
                    String trimURL = "/webcenter/portal/Customer/pages_lpg";
                    //                    String openURL = "window.location.replace(\"" + trimURL + "\");";
                    //                    CommonHelper.runJavaScript(openURL);
                    ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
                    try {
                        exct.redirect(trimURL);
                        FacesContext.getCurrentInstance().responseComplete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pageflowParam.put("ExternalNavigation", "Y");
                }
            /*----------------------------End Here-------------------------*/
        }        
        } catch (Exception e) {
            log.info("error Inside initializeMethod");
            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
            e.printStackTrace();
        }
        log.info("here==ends..");
        log.info("--------------------Inside initializeMethod in Customer End------------------");
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
    

    private String changeLocale(String language) {
        System.out.println("iNSIDE Change Locale***");
        System.out.println("changeLocale " + language);
        String retVal = null;

        Locale newLocale = new Locale(language);
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(newLocale);
        if ("en".equalsIgnoreCase(language)) {
            //Change application locale to English
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_EN.properties");

        } else if ("hi".equalsIgnoreCase(language)) {
            //Change application locale to Hindi
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_HI.properties");

        } else if ("tel".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_TEL.properties");

        } else if ("or".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_OR.properties");

        } else if ("as".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_AS.properties");

        } else if ("bn".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_BN.properties");

        } else if ("gu".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_GU.properties");

        } else if ("kn".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_KN.properties");

        } else if ("ml".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_ML.properties");

        } else if ("mr".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_MR.properties");

        } else if ("pa".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_PA.properties");

        }

        else if ("ta".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_TA.properties");

        } else if ("ur".equalsIgnoreCase(language)) {
            //Change application locale to telugu
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("resName", "lpgBundle_UR.properties");

        }
        ResourceBundle bundle = ResourceBundle.getBundle("com.iocl.lpg.utils.Resource");
        bundle.clearCache();

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                                                                      .getExternalContext()
                                                                      .getRequest();
        String url = request.getRequestURL().toString();
        System.out.println("url----" + url);
        String uri = request.getRequestURI();
        System.out.println("uri----" + uri);

        return retVal;
        

    }

    public void handleFragmentOnLoad(PollEvent pollEvent) {

        //                lpgSectionSubmitBtnBinding.setDisabled(false);
        //                CommonHelper.refreshLayout(lpgSectionSubmitBtnBinding);
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        lpgConnPopUpBinding.show(hints);
    }


    /**-----selectActiveLpgConn Method Set selected LPG Connection
     **------------------ details to Customer Session Parameter**/
    public String updateActiveLpgConn() {
        
        String retString = null;
        log.info("Start of updateActiveLpgConn Method");
        Map<String, Object> mapSession = FacesContext.getCurrentInstance()
                                                     .getExternalContext()
                                                     .getSessionMap();
        
        String ConsumerProfileService =
            String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerProfileService}"));

        String lpgSelectedRelRowId =
            String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.selectedRelationshipRowId}"));

        JSONObject jsonObject = null;
        if (ConsumerProfileService != null) {
            jsonObject = new JSONObject(ConsumerProfileService);
        }
        // Setting userprofile in session //

        UserProfileBean userBn = null;
        if (mapSession.get("userDetails") == null) {
            userBn = new UserProfileBean();
        } else {
            userBn = (UserProfileBean) mapSession.get("userDetails");
        }
        
        CustomerRelationBean selectedCurrLPGConn = null; // this variable represent Selected LPG Connection Relationship

        Map<String, CustomerRelationBean> lpgRelations =
            (HashMap<String, CustomerRelationBean>) CommonHelper.evaluateEL("#{sessionScope.userDetails.lpgRelations}");

        log.info("lpgSelectedRelUCMId " + lpgSelectedRelRowId);
        if (lpgSelectedRelRowId != null) {
            selectedCurrLPGConn = lpgRelations.get(lpgSelectedRelRowId);
        }

        log.info("Start of updateActiveLpgConn Method4");

        log.info("selectedCurrLPGConn " + selectedCurrLPGConn);

        if (selectedCurrLPGConn != null && jsonObject != null) {
            log.info("Consumer Paramter is being Set");
            /** New fetch customer profile response fields**/
            String mobileRowId = selectedCurrLPGConn.getMobileRowId();
            String emailRowId = selectedCurrLPGConn.getEmailRowId();
            String addressRowId = selectedCurrLPGConn.getAddressRowId();

            /**--------User Attributes are set based on selected LPG Connection------------**/
            userBn.setMobileRowId(mobileRowId);
            userBn.setEmailId(emailRowId);
            userBn.setAddressRowId(addressRowId);
            userBn.setConsumerId(selectedCurrLPGConn.getConsumerId());
            /** RelationshipUCM id set in the session***/    
            userBn.setSelectedRelationshipUCMId(selectedCurrLPGConn.getRelationshipUCMId());
            userBn.setDbcStatus(selectedCurrLPGConn.getDbcStatus());
            userBn.setPartnercode(selectedCurrLPGConn.getPartnercode());
            userBn.setCashandCarryFlag(selectedCurrLPGConn.getCashandCarryFlag());
            userBn.setConsumerNumber(selectedCurrLPGConn.getConsumerNumber());
            userBn.setPartnerAdd(selectedCurrLPGConn.getPartnerAdd());
            userBn.setPartnerDeliveryrating(selectedCurrLPGConn.getPartnerDeliveryrating());
            userBn.setPartnerContactNum(CommonHelper.maskedGenericNumber(selectedCurrLPGConn.getPartnerContactNum()));
            userBn.setPartnerName(selectedCurrLPGConn.getPartnerName());
            userBn.setSubsidyEligible(selectedCurrLPGConn.getSubsidyEligible());
            userBn.setConnectionStatus(selectedCurrLPGConn.getConnectionStatus());
            userBn.setConnectionSubStatus(selectedCurrLPGConn.getConnectionSubStatus());
            userBn.setPtdStatus(selectedCurrLPGConn.getPtdStatus());
            userBn.setSubsidyGiveup(selectedCurrLPGConn.getSubsidyGiveFlag());
            userBn.setAadhaarLinkedtoLPG(selectedCurrLPGConn.getAadhaarLinkedtoLPG());
            userBn.setAadhaarLinkedtoBank(selectedCurrLPGConn.getAadhaarLinkedtoBank());
            userBn.setBankLinkedtoLPG(selectedCurrLPGConn.getBankLinkedtoLPG());
            userBn.setDistributorProduct(selectedCurrLPGConn.getDistributorProduct());
            userBn.setKycLevel(selectedCurrLPGConn.getKycLevel());
            userBn.setRelationshipAgainst(selectedCurrLPGConn.getRelationshipAgainst());
            userBn.setPromotionEligible(selectedCurrLPGConn.getPromotionEligible());
            /**-------------------------------------End Here-------------------------------**/

            
            /**--------Code to Check Coexistence Service and navigate to error Page if required so------**/
            if (userBn.getCoexistenceExist() == null) {
                /**-----------------Code to Check CoExistence Case--------------------**/
                log.info("Code to Check CoExistence Case Start");
                java.util.List lstInputCE = new java.util.ArrayList();
                JSONObject jsonInputCE = new JSONObject();

                if (userBn.getPartnercode() != null) {
                    jsonInputCE.put("DistributorCode",userBn.getPartnercode());
                } else {
                    jsonInputCE.put("DistributorCode", "");
                }
                lstInputCE.add(0, EPICIOCLResourceCustBundle.findKeyValue("COEXISTENCE"));
                lstInputCE.add(1, jsonInputCE);
            
                
                OperationBinding opCE = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                opCE.getParamsMap().put("inputList", lstInputCE);
                opCE.getParamsMap().put("method", EPICConstant.COEXISTENCE);
                opCE.execute();

                java.util.List returnListCE = (java.util.List) opCE.getResult();   

                if ((returnListCE != null) && (returnListCE.get(0) != null) &&
                    returnListCE.get(0).toString().equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                    JSONObject jsonObjectCE = new JSONObject(returnListCE.get(1).toString());
                    userBn.setCoexistenceExist(jsonObjectCE.has("IsExist") ? jsonObjectCE.get("IsExist").toString() : null);
                log.info("CoexistenceExist: "+userBn.getCoexistenceExist());
                } else {
                    log.info("---------------------Error in COEXISTENCE Service--------------------------");
                    retString = "ERROR";
                    log.info("Error in Coexistence Service. Navigate to Error Page");
                    return retString; // added Now in respect of above statement
                }
                log.info("Code to Check CoExistence Case End");
            }
            /**------------------------------End Here----------------------------**/
            
            JSONArray mobileArr =
                jsonObject.isNull("ContactListofMobile") ? null : jsonObject.getJSONArray("ContactListofMobile");
            if (mobileArr != null) {
                for (int mob = 0; mob < mobileArr.length(); ++mob) {
                    JSONObject mobAr = mobileArr.getJSONObject(mob);
                    if (String.valueOf(mobAr.get("MobileRowId")).equalsIgnoreCase(mobileRowId)) {
                        if (!String.valueOf(mobAr.get("MobileNumber")).equalsIgnoreCase("null")) {
                            userBn.setConsumerContactNumber(CommonHelper.maskedGenericNumber(String.valueOf(mobAr.get("MobileNumber"))));
                            userBn.setMobile(String.valueOf(mobAr.get("MobileNumber")));
                        } else {
                            userBn.setConsumerContactNumber(jsonObject.isNull(EPICConstant.SESSION_CONS_CONTNO) ?
                                                            EPICConstant.NOT_APPLICABLE :
                                                            jsonObject.getString(EPICConstant.SESSION_CONS_CONTNO)
                                                            .toString());
                        }
                    }
                }
            }

            JSONArray emailArr =
                jsonObject.isNull("ContactListofEmails") ? null : jsonObject.getJSONArray("ContactListofEmails");
            if (emailArr != null) {
                for (int em = 0; em < emailArr.length(); ++em) {
                    JSONObject emAr = emailArr.getJSONObject(em);
                    if (String.valueOf(emAr.get("EmailRowId")).equalsIgnoreCase(emailRowId)) {
                        if (!String.valueOf(emAr.get("EmailId")).equalsIgnoreCase("null")) {
                            userBn.setConsumerEmailId(String.valueOf(emAr.get("EmailId")));
                        } else {
                            userBn.setConsumerEmailId(jsonObject.isNull(EPICConstant.SESSION_CONSUMEREMAILID) ?
                                                      EPICConstant.NOT_APPLICABLE :
                                                      jsonObject.getString(EPICConstant.SESSION_CONSUMEREMAILID)
                                                      .toString());
                        }
                    }
                }
            }

            JSONArray addArr =
                jsonObject.isNull("ContactListofAddress") ? null : jsonObject.getJSONArray("ContactListofAddress");
            if (addArr != null) {


                for (int ad = 0; ad < addArr.length(); ++ad) {
                    JSONObject adAr = addArr.getJSONObject(ad);
                    AddressParams addressObj = new AddressParams();

                    addressObj.setAddRowId(adAr.isNull("AddressRowId") ? EPICConstant.NOT_APPLICABLE :
                                           String.valueOf(adAr.get("AddressRowId")));
                    addressObj.setAddPrimary(adAr.isNull("PrimaryFlag") ? EPICConstant.NOT_APPLICABLE :
                                             String.valueOf(adAr.get("PrimaryFlag")));
                    addressObj.setAddress1(adAr.isNull("Address1") ? "" : String.valueOf(adAr.get("Address1")));
                    addressObj.setAddress2(adAr.isNull("Address2") ? "" : String.valueOf(adAr.get("Address2")));
                    addressObj.setAddress3(adAr.isNull("Address3") ? "" : String.valueOf(adAr.get("Address3")));
                    addressObj.setCity(adAr.isNull("City") ? "" : String.valueOf(adAr.get("City")));
                    addressObj.setState(adAr.isNull("State") ? "" : String.valueOf(adAr.get("State")));
                    addressObj.setDistrict(adAr.isNull("District") ? "" : String.valueOf(adAr.get("District")));
                    addressObj.setPinCode(adAr.isNull("Pincode") ? "" : String.valueOf(adAr.get("Pincode")));
                    addressObj.setLandmark(adAr.isNull("Landmark") ? "" : String.valueOf(adAr.get("Landmark")));

                    addressObj.setAddress(addressObj.getAddress1() + " " + addressObj.getAddress2() + " " +
                                          addressObj.getAddress3() + " " + addressObj.getLandmark() + " " +
                                          addressObj.getCity() + " " + addressObj.getDistrict() + " " +
                                          addressObj.getState() + " " + addressObj.getPinCode());


                    if (String.valueOf(adAr.get("AddressRowId")).equalsIgnoreCase(addressRowId)) {
                        addressObj.setChangeAddressFlowCall(true);
                        addressObj.setAddPrimary("Y");
                        if (!String.valueOf(adAr.get("Address1")).equalsIgnoreCase("null")) {
                            log.info("Setting Consumer address");
                            userBn.setConsumerAddress(getConsAddress(adAr));

                            userBn.setConsumerCity(adAr.isNull("City") ? EPICConstant.NOT_APPLICABLE :
                                                   String.valueOf(adAr.get("City")));
                            userBn.setConsumerZipCode(adAr.isNull("Pincode") ? EPICConstant.NOT_APPLICABLE :
                                                      String.valueOf(adAr.get("Pincode")));
                            userBn.setConsumerState(adAr.isNull("State") ? EPICConstant.NOT_APPLICABLE :
                                                    String.valueOf(adAr.get("State")));
                            userBn.setCustomerAddress1(adAr.isNull("Address1") ? EPICConstant.NOT_APPLICABLE :
                                                    String.valueOf(adAr.get("Address1")));
                            userBn.setCustomerAddress2(adAr.isNull("Address2") ? EPICConstant.NOT_APPLICABLE :
                                                    String.valueOf(adAr.get("Address2")));
                            userBn.setCustomerAddress3(adAr.isNull("Address3") ? EPICConstant.NOT_APPLICABLE :
                                                    String.valueOf(adAr.get("Address3")));
                            userBn.setCustomerLandMark(adAr.isNull("Landmark") ? EPICConstant.NOT_APPLICABLE :
                                                    String.valueOf(adAr.get("Landmark")));
                            userBn.setCustomerDistrict(adAr.isNull("District") ? EPICConstant.NOT_APPLICABLE :
                                                    String.valueOf(adAr.get("District")));
                        } else {
                            userBn.setConsumerAddress(jsonObject.isNull(EPICConstant.SESSION_CONSUMERADDRESS) ?
                                                      EPICConstant.NOT_APPLICABLE :
                                                      jsonObject.getString(EPICConstant.SESSION_CONSUMERADDRESS));
                            userBn.setConsumerCity(jsonObject.isNull(EPICConstant.SESSION_CONSUMERCITY) ?
                                                   EPICConstant.NOT_APPLICABLE :
                                                   jsonObject.getString(EPICConstant.SESSION_CONSUMERCITY).toString());
                            userBn.setConsumerZipCode(jsonObject.isNull(EPICConstant.SESSION_CONS_ZIPCODE) ?
                                                      EPICConstant.NOT_APPLICABLE :
                                                      jsonObject.getString(EPICConstant.SESSION_CONS_ZIPCODE)
                                                      .toString());
                        }

                    } else {
                        addressObj.setChangeAddressFlowCall(false);
                    }
                    log.info("###Setting address inside showrelation popup");
                    userBn.getAddressDetails().add(addressObj);
                }


            }

            log.info("LPGMulipleConnection Paramter Set to N");
            userBn.setLpgMultipleConnection("N");
//            mapSession.put(EPICConstant.SESSION_USER_DETAILS, userBn); //Updated parameter is set in session
            
            mapSession.put("userDetails", userBn);// added this as recommended by Vikas U
//            CommonHelper.setEL("#{sessionScope.userDetails}", userBn); // commented this as recommended by Vikas U
            

            prepareLPGSection();
            //After getting Single LPG Connection this method prepare LPG Section
            
        }
             return retString;
    }

    /**-------------------prepareLPGSection Method Prepare LPG Section i.e show section such as New Connection
     * -------------------Process Stage,Suspended Connection------------------------------------------------**/
    public void prepareLPGSection() {

        log.info("Inside prepareLPGSection Method");
        sessionscope.put("LPGConnectionType",null); //This Flag signify LPG Connection Type such as Active,Cancelled etc
        String vConnStatus = null;
        String vConnSubStatus = null;
        String vStageStatus = null;
        String vRelationshipAgainst = null;
        
        if (CommonHelper.evaluateEL("#{sessionScope.userDetails.connectionStatus}") != null) {
            vConnStatus = String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.connectionStatus}"));
        }
        if (CommonHelper.evaluateEL("#{sessionScope.userDetails.connectionSubStatus}") != null) {
            vConnSubStatus = String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.connectionSubStatus}"));
        }
        if (CommonHelper.evaluateEL("#{sessionScope.userDetails.kycLevel}") != null) {
            vStageStatus = String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.kycLevel}"));
        }
        if (CommonHelper.evaluateEL("#{sessionScope.userDetails.relationshipAgainst}") != null) {
            vRelationshipAgainst = String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.relationshipAgainst}"));
        }

        /*-----------------Below is logic to show current Section in LPG---------------*/
        /*------------Logic is based on Connection Status and SubStatus Field----------*/
        log.info("connection Status vConnStatus: " + vConnStatus);
        log.info("connection SubStatus vConnSubStatus: " + vConnSubStatus);

        if (vConnStatus != null && vConnStatus.equalsIgnoreCase("1")) {
            if (vConnSubStatus.equalsIgnoreCase("102")) {
                log.info("User Will follow the Normal Navigation");
                sessionscope.put("LPGCurrSection", "LPGDashboard");
                sessionscope.put("LPGConnectionType","Active");
            } else if (vConnSubStatus != null && vConnSubStatus.equalsIgnoreCase("101")) {
                log.info("User Will remain in Customer Portal with DeActive Status");
                sessionscope.put("LPGCurrSection", "Deactive");
                sessionscope.put("LPGConnectionType","Deactivated");
            } else if (vConnSubStatus != null && vConnSubStatus.equalsIgnoreCase("103")) {
                log.info("User Will remain in Customer Portal with Suspended Status");
                sessionscope.put("LPGCurrSection", "Suspended");
                sessionscope.put("LPGConnectionType","Suspended");
            }
        } else if (vConnStatus != null && vConnStatus.equalsIgnoreCase("3")) {
            if (vConnSubStatus != null && vConnSubStatus.equalsIgnoreCase("105")) {
                log.info("This will be treated like a new customer only having an option to apply for KYC or e-Kyc on LPG Dashboard.");
                sessionscope.put("LPGCurrSection", "NewConnection");
                sessionscope.put("LPGConnectionType","Surrendered");
            }
        } else if (vConnStatus != null && vConnStatus.equalsIgnoreCase("2")) {
            if (vConnSubStatus != null && vConnSubStatus.equalsIgnoreCase("106")) {
                log.info("This is a transferred connection case. User Need to contact distributer.");
                sessionscope.put("LPGCurrSection", "Transferred");
                sessionscope.put("LPGConnectionType","Transferred");
            } 
//            else if (vConnSubStatus != null && vConnSubStatus.equalsIgnoreCase("Bulk Out")) {
//                log.info("This will be treated like a new customer only having an option to apply for KYC or e-Kyc on LPG Dashboard.");
//                sessionscope.put("LPGCurrSection", "NewConnection");
//            }
        } else if (vConnStatus != null && vConnStatus.equalsIgnoreCase("4")) {
            if (vConnSubStatus != null && vConnSubStatus.equalsIgnoreCase("104")) {
                log.info("This will be treated like a new customer only having an option to apply for KYC or e-Kyc on LPG Dashboard.");
                sessionscope.put("LPGCurrSection", "NewConnection");
                sessionscope.put("LPGConnectionType","Cancelled");
            }
        } else if (vConnStatus != null && vConnStatus.equalsIgnoreCase("5")) {
            if ((vConnSubStatus != null && vConnSubStatus.equalsIgnoreCase("107")) &&
                (vRelationshipAgainst != null &&
                 vRelationshipAgainst.equalsIgnoreCase(EPICConstant.RELATIONSHIP_AGAINST_VAL))) {
                log.info("This will show the LPG Dashboard having KYC & e-KYC option with the option to Track the status of his KYC.");
                sessionscope.put("LPGCurrSection", "KycTrainStatus");
                sessionscope.put("LPGStageStatus", vStageStatus);
                sessionscope.put("LPGConnectionType","InProcess");
                CommonHelper.kycStatusImage(vStageStatus);
            } else if (vConnSubStatus != null && vConnSubStatus.equalsIgnoreCase("107")) {
                log.info("Below is the case of TTV like flows.So Inspite of having ConnectionStatus and SubStatus in Process.User is in Transferred State");
                sessionscope.put("LPGCurrSection", "Transferred");
                sessionscope.put("LPGConnectionType","InProcess");
            }
        } else if (vConnStatus != null && vConnStatus.equalsIgnoreCase("6")) {
            if (vConnSubStatus != null && vConnSubStatus.equalsIgnoreCase("109")) {
                log.info("Bulk Out Case Executed - Same as Transferred Case");
                sessionscope.put("LPGCurrSection", "Transferred");
                sessionscope.put("LPGConnectionType","BulkOut");
            }
        }
        log.info("LPGCurrSection is: " + sessionscope.get("LPGCurrSection"));
        log.info("LPGConnectionType is: " + sessionscope.get("LPGConnectionType"));
        /*-------------------Code to Move to External Context-----------*/
        log.info("prepareLPGSection Taskflow SourceFlow Paramter Value is: "+pageflowParam.get("SourceFlow"));
        if (sessionscope.get("LPGCurrSection") != null && !sessionscope.get("LPGCurrSection").equals("LPGDashboard")) {
            if (pageflowParam.get("SourceFlow") != null && (pageflowParam.get("SourceFlow").equals("CustomerLPG") || pageflowParam.get("SourceFlow").equals("NormalFlow"))) {
            } else if(sessionscope.get("sDestinationTo") != null && (sessionscope.get("sDestinationTo").toString().equalsIgnoreCase("CustomerProfilePage"))){ 
                    sessionscope.put("sDestinationTo", null);
                    log.info("Passing Navigation as sDestinationTo equals CustomerProfilePage");
                }
            else {
                sessionscope.put("currentTabInCustomer", "lpg");
                String trimURL = "/webcenter/portal/Customer/pages_lpg";
//                String openURL = "window.location.replace(\"" + trimURL + "\");";
//                CommonHelper.runJavaScript(openURL);
                log.info("Before Redirection in prepareLPGSection");
                ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    exct.redirect(trimURL);
                    FacesContext.getCurrentInstance().responseComplete();
                } catch (IOException e) {
                    log.info("Error: "+e);
//                    e.printStackTrace();
                }
                log.info("After Redirection in prepareLPGSection");
                pageflowParam.put("ExternalNavigation", "Y");
            }
        }
        /*--------------------------------------End Here--------------------------------*/
    }

    public void setLpgConnPopUpBinding(RichPopup lpgConnPopUpBinding) {
        this.lpgConnPopUpBinding = lpgConnPopUpBinding;
    }

    public RichPopup getLpgConnPopUpBinding() {
        return lpgConnPopUpBinding;
    }

    public void setLpgSectionSubmitBtnBinding(RichButton lpgSectionSubmitBtnBinding) {
        this.lpgSectionSubmitBtnBinding = lpgSectionSubmitBtnBinding;
    }

    public RichButton getLpgSectionSubmitBtnBinding() {
        return lpgSectionSubmitBtnBinding;
    }


    public void setLpgConnBinding(RichSelectOneRadio lpgConnBinding) {
        this.lpgConnBinding = lpgConnBinding;
    }

    public RichSelectOneRadio getLpgConnBinding() {
        return lpgConnBinding;
    }

//    public void kycStatusImage(String kycStatus) {
//        log.info("Inside kycStatusImage in LPGSection Start");
//        log.info("Input Parameter");
//        //        java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
//        log.info("vkycStatus parameter Value in kycStatusImage Method: " + kycStatus);
////        java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
//
//        String stageCheckPath = "/images/KycStatus/kyc_tick.svg";
//        String stageUnCheckPath = "/images/KycStatus/kyc_empty.svg";
//
//        if (kycStatus != null && kycStatus.equalsIgnoreCase("N")) {
//            sessionscope.put("kycSubmitDistributer", stageCheckPath);
//            sessionscope.put("kycReadyLPGConn", stageUnCheckPath);
//
//        } else if (kycStatus != null && kycStatus.equalsIgnoreCase("Y")) {
//            sessionscope.put("kycSubmitDistributer", stageCheckPath);
//            sessionscope.put("kycReadyLPGConn", stageCheckPath);
//
//        } else {
//            sessionscope.put("kycSubmitDistributer", stageUnCheckPath);
//            sessionscope.put("kycReadyLPGConn", stageUnCheckPath);
//        }
//        log.info("Inside kycStatusImage in CommonHelper End");
//    }

    public String lpgConnSubmitAction() {
        // Add event code here...
        String retString = null;
        log.info("Inside lpgConnSubmitAction");
        java.util.List<CustomerRelationBean> customerDetails =
            (java.util.ArrayList<CustomerRelationBean>) CommonHelper.evaluateEL("#{sessionScope.userDetails.customerDetails}");
        Boolean connSelected = false;

        /**-----------Code to Get RelationshipUCMId from Selected LPG Relationship Start------------**/

        for (CustomerRelationBean obj : customerDetails) {
            if (obj.getConnectionSelected().equals(true)) {
                CommonHelper.setEL("#{sessionScope.userDetails.selectedRelationshipRowId}", obj.getRelationshipRowId());
                connSelected = true;
            }
        }

        if (!connSelected) {
            lpgConnecErrorMsg = CommonHelper.getValueFromRsBundle("PLEASE_SELECT_ONE_CONNECTION");
            pageflowParam.put("lpgConnecErrorMsg", CommonHelper.getValueFromRsBundle("PLEASE_SELECT_ONE_CONNECTION"));
            return null;
        }
        /**-----------Code to Get RelationshipUCMId from Selected LPG Relationship End------------**/

        log.info("selectedRelationshipRowId: " +
                 CommonHelper.evaluateEL("#{sessionScope.userDetails.selectedRelationshipRowId}"));
        lpgConnPopUpBinding.hide();
        retString = updateActiveLpgConn(); //Method to Update Customer Profile Parameters

        return retString == null ? "backToFlow" : retString;
    }
    
    private String getConsAddress(JSONObject adAr){
        StringBuffer sbAddr=new StringBuffer();
        log.info("Address obje:"+adAr);
        sbAddr.append(String.valueOf(adAr.get("Address1")) == "null" ? "":String.valueOf(adAr.get("Address1"))+" ");
        sbAddr.append(String.valueOf(adAr.get("Address2")) == "null" ? "":String.valueOf(adAr.get("Address2"))+" ");
        sbAddr.append(String.valueOf(adAr.get("Address3")) == "null" ? "":String.valueOf(adAr.get("Address3"))+" ");
        sbAddr.append(String.valueOf(adAr.get("Landmark")) == "null" ? "":String.valueOf(adAr.get("Landmark"))+" ");
        sbAddr.append(String.valueOf(adAr.get("City")) == "null" ? "":String.valueOf(adAr.get("City"))+" ");
        sbAddr.append(String.valueOf(adAr.get("District")) == "null" ? "":String.valueOf(adAr.get("District"))+" ");
        sbAddr.append(String.valueOf(adAr.get("State")) == "null" ? "":String.valueOf(adAr.get("State"))+" ");
        sbAddr.append(String.valueOf(adAr.get("Pincode")) == "null" ? "":String.valueOf(adAr.get("Pincode"))+" ");
        log.info("Consumer address::"+sbAddr.toString());
        return sbAddr.toString();
                                                              
    }

    public void setLpgConnecErrorMsg(String lpgConnecErrorMsg) {
        this.lpgConnecErrorMsg = lpgConnecErrorMsg;
    }

    public String getLpgConnecErrorMsg() {
        return lpgConnecErrorMsg;
    }
}
