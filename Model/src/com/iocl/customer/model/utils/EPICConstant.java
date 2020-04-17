package com.iocl.customer.model.utils;

import java.util.ArrayList;

public class EPICConstant {

    public static java.util.List<String> CACHE_SVC_LST;
    static {
        CACHE_SVC_LST = new ArrayList<String>();
        CACHE_SVC_LST.add("0"); //to be change
    }

    public EPICConstant() {
        super();
    }

// public static final String PROPERTIES_FILEPATH = "C:\\CUSTOMER_BUNDLE.properties";
//    public static final String PROPERTIES_FILEPATH = "/portal/app/oracle/config/CUSTOMER_BUNDLE.properties";//for Ho st and uat
  public static final String PROPERTIES_FILEPATH =  "/u02/app/oracle/config/config_wcp/CUSTOMER_BUNDLE.properties"; //for nextra st and uat
   
    public static String ERROR_CODE = "ErrorCode";
    public static String ERRORMESSAGE = "ErrorMessage";
    public static final int NON_MASKED_MOBILECHAR = 4;
    public static final String MASKEDCHAR = "X";
    public static final String FETCHCURRENTDEBITBALANCE = "FETCHCURRENTDEBITBALANCE";
    public static final String FETCH_CONSUMER_PROFILE_DETAILS = "FETCH_CONSUMER_PROFILE_DETAILS";
    public static final String NOT_APPLICABLE = "NA";
    public static final String CHECK_APPLICATION_STATUS = "CHECK_APPLICATION_STATUS";
    public static final String DEGREE_OVERVIEW = "DEGREE_OVERVIEW";
    public static final String PARTNER_SERVICE_REQUEST = "PARTNER_SERVICE_REQUEST";
    public static final String GET_CUSTOMER_DETAILS="GET_CUSTOMER_DETAILS";
    public static final String GENERATE_EMAILOTP = "GENERATE_EMAILOTP";
    public static final String VALIDATE_EMAILOTP="VALIDATE_EMAILOTP";
    public static final String PARTNERCODE = "PartnerCode";
    public static final String RELATIONSHIPUCMID = "RelationshipUCMId";
    public static final String LOGINID = "LoginId";
    public static final String TRACKING_ID = "TrackingId";
    public static final String CONSUMERID = "ConsumerId";
    public static final String HEADER_OAM_ROLE = "defaultrole";
    public static final String HEADER_OAM_UCMID = "UCMID";
    public static final String HEADER_OAM_SAPCODE = "partnersapcode";
    public static final String SOURCE = "Source";
    public static final String UCMID = "UCMId";
    public static final String SOURCE_PORTAL = "Portal";
    public static final String G_MAP_IMGURL ="G_MAP_IMGURL";
    //---------------Subscription Voucher Service Parameter------//
    public static final String CODE = "Code";
    public static final String TYPE = "Type";
    public static final String MRP = "MRP";
    public static final String MAKE = "Make";
    public static final String BRAND = "Brand";
    public static final String MANUFACTURING = "Manufacturing";
    public static final String DESCRIPTION = "Description";
    public static final String PRODUCT = "Product";
    
    // below Parameter are defined for Customer Portal Only
    public static final String SUBSCRIPTION_VOUCHER_REQUEST = "SUBSCRIPTION_VOUCHER_REQUEST";
    public static final String SUBSCRIPTION_VOUCHER_ORDER = "SUBSCRIPTION_VOUCHER_ORDER";

    public static final String GETSR = "GETSR";
    
    public static final String SENDOTP = "SENDOTP";
    public static final String VALIDATEOTP = "VALIDATEOTP";
    public static final String REACTIVATE_LPG_CONNECTION = "REACTIVATE_LPG_CONNECTION";
    public static final int NON_SECURED_TOKEN = 0;
    public static final int SECURED_TOKEN = 1;
    public static final String FETCH_DEALER_DETAILS="FETCH_DEALER_DETAILS";
    public static final String FETCHERRORMESSAGE="fetchErrorMessage";
    public static final String ERRORCODE="errorCode";
    public static final String SBL1="SBL-1";
    public static final String SBL500="SBL-500";
    public static final String OSB380000="OSB-380000";
    public static final String OTH1="OTH-1";
    public static final String OTH2="OTH-2";
    public static final String ERROR_HTTP_500="500";
    
    public static final String SV_PROD_TYPE_LPGSTOVE="LPG-Stove"; 
    public static final String SV_PROD_TYPE_LPGHOSE="LPG-Hose";
    public static final String ZERO_VAL_SUCCES_CASE="0";
    
    public static final String BOOKREFILLPRICELIST = "BOOKREFILLPRICELIST";
    public static final String IOEXP="IOEXP";
    public static final String G_MAP_KEY="G_MAP_KEY";
    public static final String IDAM_CONTACT="IDAMCONTACT";
    public static final String DBTL_SERVICE="DBTL_SERVICE";
    public static final String SUCCES_CASE_DBTL="SUCCESS";
    public static final String LINK_CONS_DETLS="LINKCONSUMERDETAILS";
    public static final String UPDATE_LINKING="UPDATE_LINKING";
    public static final String GET_SR_LIST="GET_SR_LIST";
    public static final String COEXISTENCE="COEXISTENCE";
    public static final String NEW_CONNECTION_STATUS="NEW_CONNECTION_STATUS";
    public static final String NEW_CONNECTION_CANCEL_REQUEST="NEW_CONNECTION_CANCEL_REQUEST";
    public static final String GET_PROMOTIONS="GET_PROMOTIONS";
    public static final String USER_TRACKING_ID="UserTrackingId";
    public static final String EXCEPTION_INTERNAL_500="500";
    
    public static final String RO_LOCATOR="RO_LOCATOR";
    public static final String AO_LOCATOR="AO_LOCATOR";
    public static final String PARTNER_LOCATOR="PARTNER_LOCATOR";
    public static final String LATITUDE="latitude";
    public static final String LONGITUDE="longitude";
    public static final String RADIUS="radius";
    public static final String LATITUDENEW="Latitude";
    public static final String LONGITUDENEW="Longitude";
    public static final String RADIUSNEW="Radius";
    
    public static final String GENERATE_REPORTCHART = "GENERATE_REPORTCHART";
    
    public static final String XR_CUSTOMER_DETAILS = "XR_CUSTOMER_DETAILS";
    public static final String XR_CUSTOMER_BALANCE = "XR_CUSTOMER_BALANCE";
    public static final String XR_CUSTOMER_TRANSACTION = "XR_CUSTOMER_TRANSACTION";
    
    public static final String XR_CUSTOMER_ENROLLURL = "XR_CUSTOMER_ENROLLURL";
    public static final String SUBMIT_DEALER_FEEDBACK="SUBMIT_DEALER_FEEDBACK";
    public static final String LEAD_CREATION="LEAD_CREATION";
    
    public static final String FETCH_XR_RECORD = "FETCH_XR_RECORD";
    public static final String PROFILE_ENRICHMENT = "PROFILE_ENRICHMENT";
    
    public static final String FIRST_NAME = "FirstName";
    public static final String MIDDLE_NAME = "MiddleName";
    public static final String LAST_NAME = "LastName";
    public static final String DATE_OF_BIRTH = "DateOfBirth";
    public static final String MOBILE_NO = "MobileNo";
    public static final String MOBILE_NUMBER = "MobileNumber";
    public static final String RELATIONSHIP_UCMID = "RelUCMId";
    public static final String ADDRESS1 = "Address1";
    public static final String ADDRESS2 = "Address2";
    public static final String ADDRESS3 = "Address3";
    public static final String ADDRESS4 = "Address4";
    public static final String STATE = "State";
    public static final String DISTRICT = "District";
    public static final String CITY = "City";
    public static final String PINCODE = "Pincode";
    public static final String XR_POINTS = "XRPoints";
    public static final String XR_UCMID = "XRUCMId";
    public static final String LPG_UCMID = "LPGUCMId";
    public static final String ADDRESS = "Address";
    public static final String ADDRESS_LINE1 = "AddressLine1";
    public static final String ADDRESS_LINE2 = "AddressLine2";
    public static final String ADDRESS_LINE3 = "AddressLine3";
    public static final String STATE_NAME = "StateName";
    public static final String DISTRICT_NAME = "DistrictName";
    
    public static final String GENDER = "Gender";
    public static final String TITLE = "Title";
    public static final String EMAIL = "Email";
    public static final String ANNUAL_INCOME = "AnnualIncome";
    public static final String BALANCE_POINTS = "BalancePoints";
    public static final String RESIDENCE_PHONE = "ResidencePhone";
    
    
    //Added after Sonar issue List
    public static final String ROCODE = "ROCode";
    public static final String RONAME = "ROName";
    public static final String ROADDRESS = "ROaddress";
    public static final String PIN = "Pin";
//    public static final String STATE = "State";
    public static final String DOCODE = "DoCode";
    public static final String SOCODE = "SOCode";
    public static final String TELEPHONE = "Telephone";
    public static final String LATITUDE_VO = "Latitude";
    public static final String LONGITUDE_VO = "Longitude";
    public static final String DEALERNAME = "DealerName";
    public static final String WORKTIMIMGSTART = "WorkTimingStart";
    public static final String WORKTIMIMGEND = "WorkTimingEnd";
    public static final String MS = "MS";
    public static final String HSD = "HSD";
    public static final String XP = "XP";
    public static final String XM = "XM";
    public static final String TELF1 = "TelF1";
    public static final String SALESORGNAME = "SalesOrgName";
    public static final String SALESOFFICENAME = "SalesOffName";
    public static final String SALESAREANAME = "SalesAreaName";
    public static final String DISTRICTNAME = "DistrictName";
    public static final String CUSTSTATENAME = "CustStateName";
    public static final String ROMKTTYPE = "ROMktType";
//    public static final String EMAIL = "Email";
    public static final String ROSERVICES = "ROServices";
    public static final String ARCHERTYPE = "ArcheType";
    public static final String XR_LINKING="XR_LINKING";
    public static final String PROFILE_ENROLLMENT="PROFILE_ENROLLMENT";
    
//    public static final String NULL = "SalesAreaName";
//    public static final String NULL = "DistrictName";
//    public static final String NULL = "CustStateName";
//    public static final String NULL = "ROMktType";
//    public static final String NULL = "Email";
    
    public static final String ONBOARD_CUSTOMER_RETAIL = "ONBOARD_CUSTOMER_RETAIL";
    public static final String VEHICLE_LOVS = "VEHICLE_LOVS";
    public static final String LOYALTY_STATE_DISTRICT = "LOYALTY_STATE_DISTRICT";
    public static final String ERROR_500="500";
    public static final String  ERROR_MESSAGE="ErrorMessage";
    public static final String MESS_NOT_FOUND_FOR_ZERO_500="ErrorMessage is null here";
    public static final String PROFILE_ENROLLMENT_DUPLICATE="PROFILE_ENROLLMENT_DUPLICATE";
    public static final String KM_FIND_YOUR_OIL_SEARCH="KM_FIND_YOUR_OIL_SEARCH";
    public static final String KM_BROWSE_CHANNEL="KM_BROWSE_CHANNEL";
    public static final String SEARCH_STOCKIST="SEARCH_STOCKIST";
}
