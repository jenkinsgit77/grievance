package com.iocl.lpg.customer.utils;


public class EPICConstant {
    public EPICConstant() {
        super();
    }

    public static final int NON_MASKED_ACCOUNTCHAR = 2;
    public static final int NON_MASKED_MOBILECHAR = 4;
    public static final String MASKEDCHAR = "X";
    
//    public static final String PROPERTIES_FILEPATH = "C:\\CUSTOMER_BUNDLE.properties";
//  public static final String PROPERTIES_FILEPATH = "/portal/app/oracle/config/CUSTOMER_BUNDLE.properties";//for Ho st and uat
    public static final String PROPERTIES_FILEPATH =  "/u02/app/oracle/config/config_wcp/CUSTOMER_BUNDLE.properties"; //for nextra st and uat
       public static String ERROR_CODE = "ErrorCode";
    public static String ERRORMESSAGE = "ErrorMessage";
   
    public static final String FETCH_DEALER_DETAILS="FETCH_DEALER_DETAILS";
    public static final String CHECK_APPLICATION_STATUS = "CHECK_APPLICATION_STATUS";
    public static final String DEGREE_OVERVIEW = "DEGREE_OVERVIEW";
    public static final String NOT_APPLICABLE = "NA";
    public static final String GENERATE_EMAILOTP = "GENERATE_EMAILOTP";
    public static final String VALIDATE_EMAILOTP="VALIDATE_EMAILOTP";
    public static final String EMAIL_OTP="OTP";
    public static final String PARTNER_SERVICE_REQUEST = "PARTNER_SERVICE_REQUEST";
    public static final String PARTNERIMGPATH ="/PortalContent/LPG/Home/PartnerHomePage";
    public static final String PARTNERNONLOGINIMGPATH ="/PortalContent/LPG/Home/PartnerHomePage/PartnerNonloginImage";
    public static final String BANNERIMGPATH = "/PortalContent/LPG/Home/HomePage";
    public static final String NEWSIMGPATH = "/PortalContent/LPG/Home/HomePage/News";
    public static final String PRODUCT_CAT_PATH ="/PortalContent/LPG/Home/NFR/Images/Category";
    public static final String FETCH_CONSUMER_PROFILE_DETAILS = "FETCH_CONSUMER_PROFILE_DETAILS";
     public static final String  SUBMIT_DEALER_FEEDBACK="SUBMIT_DEALER_FEEDBACK";
    public static final String PARTNERNEWSIMGPATHNONLOGIN ="/PortalContent/LPG/Home/PartnerNews/PartnerNewsNL";
    public static final String PARTNERNEWSIMGPATH = "/PortalContent/LPG/Home/PartnerNews";
    public static final int VALID_CASE = 0;
    public static final int BLANK_CASE = 1;
    public static final int INVALID_CASE = 2;
    public static final String UPDATE_PAYMENT_DETAILS = "UPDATE_PAYMENT_DETAILS";
    public static final String INITIATE_PAYMENT = "INITIATE_PAYMENT";
    public static final String ERROR="ERROR";
    public static final String FETCHCURRENTDEBITBALANCE = "FETCHCURRENTDEBITBALANCE";
    public static final String SERVICELIST = "inputList";
    public static final String SERVICEMETHOD = "method";
    public static final String SERVICEPARTNERNAME = "servicePartnerCall";
    public static final String GETSR = "GETSR";
    public static final String MOBILE_EXPRESSION = "^$|[0-9]{10}";
    public static final String EMAIL_EXPRESSSION =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String SESSION_USER_DETAILS = "userDetails";
    public static final String SESSION_CONSUMERADDRESS = "ConsumerAddress";
    public static final String SESSION_CONSUMERCITY = "ConsumerCity";
    public static final String SESSION_CONS_CONTNO = "ConsumerContactNumber";
    public static final String SESSION_CONSUMEREMAILID = "ConsumerEmailId";
    public static final String SESSION_CONSUMERNUM = "ConsumerNumber";
    public static final String SESSION_CONSUMERNAME = "ConsumerName";
    public static final String SESSION_PARTNERNAME = "PartnerName";
    public static final String SESSION_PARTNERADDRESS = "PartnerAddress";
    public static final String SESSION_PARTNER_CONTNO = "PartnerContactNumber";
    public static final String SESSION_PARTNER_DELR_RAT = "PartnerDeliveryRating";
    public static final String SESSION_CONS_ZIPCODE = "ConsumerZipCode";
    public static final String SESSION_CONS_PIC = "ProfilePhoto";
    public static final String SESSION_LAST_FIVE_SR = "Last5ServiceRequest";
    public static final String SESSION_DBCStatus = "DBCStatus";
    public static final String CASH_AND_CARRY= "CashAndCarry";
    public static final String CONSUMERID = "ConsumerId";
    public static final String SV_NUMBER="SVNumber";
    public static final String AADHAR_NUM="AadharNumber";
    public static final String CASH_MEMO_NUM="CashMemoNumber";
    
   public static final String FEEDBACK_TYPE_DEALER="1";
    public static final String FEEDBACK_LOB_DEALER="Retail";
    public static final String RELATIONSHIPUCMID = "RelationshipUCMId";
    public static final String SESSION_CONSNO = "ConsumerNumber";
    public static final String SESSION_IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String G_MAP_IMGURL ="G_MAP_IMGURL";
    public static final String SESSION_BN_USER_ID = "UserId";
    
    

    public static final String  SESSION_PARTNER_NAME="PartnerName";
    public static final String  SESSION_PARTNER_STATUS="Status";
    public static final String  SESSION_PARTNER_CONTACTNUMBER="ContactNumber";
    public static final String  SESSION_PARTNER_DELIVERYRATING="DeliveryRating";
    public static final String  SESSION_PARTNER_PARTNERADDRESS="PartnerAddress";
    public static final String  SESSION_PARTNER_GSTNUMBER="GSTNumber";
    public static final String  SESSION_PARTNER_PINCODE="PinCode";
    public static final String  SESSION_PARTNER_DEALERPHONENUMBER="DealerPhoneNumber";
    public static final String  SESSION_PARTNER_STATE="State";
    public static final String  SESSION_PARTNER_DISTRICT="District";
    public static final String  SESSION_PARTNER_LOB="LOB";
    public static final String  SESSION_PARTNER_SRDETAILS="SRDetails";
    public static final String  SESSION_PARTNER_PAYERDETAILS="PayerDetails";
    public static final String  SESSION_PARTNER_SOLDTOPARTYDETAILS="SoldToPartyDetails";
    public static final String  SESSION_PARTNER_SHIPTOPARTYDETAILS="ShipToPartyDetails";
    public static final String GET_CUSTOMER_DETAILS="GET_CUSTOMER_DETAILS";
    public static final String LEAD_CREATION="LEAD_CREATION";
    public static final String UPDATE_LINKING="UPDATE_LINKING";
    

    public static final String HEADER_OAM_ROLE = "ROLE";
    public static final String HEADER_OAM_UCMID = "UCMId";
    public static final String HEADER_OAM_LPGID = "LPGID";
    public static final String HEADER_OAM_CONSUMERID = "ConsumerId";
    public static final String HEADER_OAM_MOBILE = "Mobile";
    public static final String HEADER_OAM_EMAIL = "eMail";
    public static final String HEADER_OAM_FIRST_NAME = "fname";
    public static final String HEADER_OAM_LAST_NAME = "lname";
    public static final String HEADER_OAM_UID = "oamid";
    
    public static final String SOURCE = "Source";
    public static final String IS_LOGGED_IN_VAL = "True";
    public static final String TRUE_VAl = "TRUE";
    public static final String FALSE_VAl = "FALSE";
    
    public static final String KYC_LEVEL = "kycLevel";
    public static final String CONNECTION_STATUS = "connectionStatus";
    
    
    // below Parameter are defined for Customer Portal Only
    public static final String SUBSCRIPTION_VOUCHER_REQUEST = "SUBSCRIPTION_VOUCHER_REQUEST";
    public static final String SUBSCRIPTION_VOUCHER_ORDER = "SUBSCRIPTION_VOUCHER_ORDER";
    public static final String SERVICE_CUSTOMER_NAME = "serviceCustomerCall";
    
    public static final String ITEM_NAME = "Name";
    public static final String ITEM_CODE = "Code";
    public static final String DESCRIPTION = "Description";
    public static final String MRP = "MRP";
    public static final String LENGTH_TYPE = "LengthType";
    public static final String LENGTH = "Length";
    
    public static final String SV_CONNECTION = "ConnectionType";
    public static final String EQUIPMENT_TYPE = "EqpType";
    public static final String HOT_PLATE_VALUE = "HotPlate";
    public static final String STEEL_LPG_STOVE_CODE = "SteelToLPGStoveCode";
    public static final String GLASS_LPG_STOVE_CODE = "GlassTopLPGStoveCode";
    public static final String LPG_STOVE_CODE = "LPGStoveCode";
    public static final String SV_PRODUCT = "Product";
    
    public static final String SV_ITEM_CODE = "ItemCode";
    public static final String SV_ITEM_PRICE = "Price";
    public static final String SV_ITEM_NAME = "ItemName";
    public static final String SV_PRODUCT_NAME = "ProductName";
    public static final String SV_PRODUCT_CODE = "ProductCode";
    public static final String SOURCE_PORTAL = "Portal";
    public static final String TRACKING_ID = "TrackingId";
    
    public static final String MOBILE_NUMBER = "MobileNumber";
    public static final String SENDOTP = "SENDOTP";
    public static final String VALIDATEOTP = "VALIDATEOTP";
    public static final String OPT_FLOW_PAGE_FLOW_PARAM = "pOTPFlow";
    public static final String REACTIVATE_LPG_CONNECTION = "REACTIVATE_LPG_CONNECTION";
    public static final String MOBILE_PAGE_FLOW_PARAM = "pOTPMOB";
    public static final String RELATIONSHIP_SUB_STATUS = "RelationshipSubStatus"; 
    public static final String REACTIVATE_LPG_JSON = "ReActivateJsonObject"; 
    public static final String NOTFOUND="NOT_FOUND";
    public static final String EmailAddress = "EmailAddress";
    public static final String SUCCESS = "SUCCESS";
    
    public static final String FETCHERRORMESSAGE="fetchErrorMessage";
    public static final String ERRORCODE="errorCode";
    public static final String SBL1="SBL-1";
    public static final String SBL2="2";
    public static final String SBL500="500";
    public static final String SBL100="100";
    public static final String OSB380000="OSB-380000";
    public static final String OTH1="OTH-1";
    public static final String OTH2="OTH-2";
    public static final String IDAM500="IDAM-500";
  public static final String NOT_FOUND_CASE= "SBL-2";
    public static final String G_MAP_KEY="G_MAP_KEY";
    public static final String NOT_FOUND="NOT_FOUND";
    public static final String BOOKREFILLPRICELIST = "BOOKREFILLPRICELIST";

    public static String ERROR_MESSAGE = "ErrorMessage";
    public static final String ERROR_NULL="null";
    public static final String ORDER_NUM="OrderNum";
    public static final String USER_KEY="UserKey";
    public static final String FIRST_NAME="FirstName";
    public static final String LAST_NAME="LastName";
    public static final String EMAIL_ADD="EmailAddress";
    public static final String IDAM_CONTACT="IDAMCONTACT";
    public static final String UCMID="UCMId";
    public static final String EMAIL="Email";
    public static final String VER_FLAG="VerifiedFlag";
    public static final String LINK_CONS_DETLS="LINKCONSUMERDETAILS";
    
    public static String XLS = "xls";
    public static String XLSX = "xlsx";
    public static String PNG = "png";
    public static String JPG = "jpg";
    public static String GIF = "gif";
    public static String SVG = "svg";
    public static String BMP = "bmp";
    public static String TXT = "txt";
    public static String DOC = "doc";
    public static String DOCX = "docx";
    public static String PDF = "pdf";
    public static String JPEG = "jpeg";
    public static final double FILE_SIZE = 0.5;
    public static final String DBTL_SERVICE="DBTL_SERVICE";
    public static String DBTL_CAT = "LPGCAT009";
    public static String CREATE_SR_INPT_PART_CD = "PartnerCode";
    public static final String CREATE_SR_INPT_COMP_DTLS = "ComplaintDetails";
    public static final String CREATE_SR_INPT_FSTNAME = "FirstName";
    
    public static final String GET_SR_LIST = "GET_SR_LIST";
    public static final String GRIEVANCE_SR_DETAILS = "SRDetails";
    public static final String COEXISTENCE="COEXISTENCE";
    public static final String NEW_CONNECTION_STATUS="NEW_CONNECTION_STATUS";
    public static final String NEW_CONNECTION_CANCEL_REQUEST="NEW_CONNECTION_CANCEL_REQUEST";
    
    public static final String GET_PROMOTIONS="GET_PROMOTIONS";
    public static final String SCHEME="Scheme";
    public static final String SCHEME_TYPE="SchemeType";
    
    public static final String USER_TRACKING_ID="UserTrackingId";
    public static final String DEBUG_MODE_ON="DebugModeOn";
    
    public static final String HTTPORHTTPSUPPORT = "//";
    public static final String HTTPORHTTPCOLONS = ":";
    public static final String IMAGE_SERVLET_PATH = "/viewimageservlet?path=";
    
    // Update Photo Status WebService Input Parameter
    public static final String PHOTO_TYPE = "PhotoType";
    public static final String PHOTO_DETAILS = "PhotoDetails";
    public static final String PROFILE_PHOTO_UPDATE = "PROFILE_PHOTO_UPDATE";
    
    /**
     * Commom parameters for all types of Attachment
     */
    public static final String ATTACHMENT_TYPE = "Type";
    public static final String ATTACHMENT_NUMBER = "Number";
    public static final String ATTACHMENT_FILENAME = "FileName";
    public static final String ATTACHMENT_DOCTYPE="DocType";
    public static final String ATTACHMENT_DOC_TYPE = "DocType";
    public static final String ATTACHMENT_EXTENSION = "Extension";
    public static final String ATTACHMENT_WCC_CONTENTID = "WCCContentId";
    public static final String ATTACHMENT_WCC_DOCID = "WCCDocId";
    public static final String POI = "POI";
    public static final String POA = "POA";
    public static final String SUBSCR_FILE_TYPE = "SubscriptionFile";
    public static final String CONT_ID="contentId";
    public static final String DTD_ID="dId";
    public static final String AFFIDAVIT = "Affidavit";
    public static final String FIR = "FIR";
    public static final String NTR = "NTR";
    
    public static final String RO_LOCATOR="RO_LOCATOR";
    public static final String AO_LOCATOR="AO_LOCATOR";
    public static final String PARTNER_LOCATOR="PARTNER_LOCATOR";
    public static final String LATITUDE="latitude";
    public static final String LONGITUDE="longitude";
    public static final String RADIUS="radius";
    public static final String LATITUDENEW="Latitude";
    public static final String LONGITUDENEW="Longitude";
    public static final String RADIUSNEW="Radius";
    
    
    /**
     * Ends here
     */
    
    public static final String CONTENT_ID="WCCContentId";
    public static final String DOC_ID="WCCDocId";

    
    public static final String ERROR_CLASS = "errorClass";
    
    public static final String GENERATE_REPORTCHART = "GENERATE_REPORTCHART";
    public static final String PROFILEIMAGE="PROFILE-IMAGE";
    
    public static final String UCM_ID_NOT_FOUND="UCMId not found"; 
    public static final String RELATIONSHIP_AGAINST_VAL="New Connection";
    
    public static final String XR_CUSTOMER_DETAILS = "XR_CUSTOMER_DETAILS";
    public static final String XR_CUSTOMER_BALANCE = "XR_CUSTOMER_BALANCE";
    public static final String XR_CUSTOMER_TRANSACTION = "XR_CUSTOMER_TRANSACTION";
    
    public static final String XR_CUSTOMER_ENROLLURL = "XR_CUSTOMER_ENROLLURL";
    public static final String MERCHANT_PARAM1="merchant_param1";
    public static final String CALLCHARTASYNC="callChartAsync";
    public static final String MERCHANT_PARAM2="merchant_param2";
    public static final String MERCHANT_PARAM3="merchant_param3";
    
    public static final String FETCH_XR_RECORD = "FETCH_XR_RECORD";
    public static final String PROFILE_ENRICHMENT = "PROFILE_ENRICHMENT";
    
    public static final String XR_UCMID = "XRUCMId";
    public static final String LPG_UCMID = "LPGUCMId";
    public static final String NULL = "null";
    public static final String NO_VALUE = "";
    public static final String FLOW_NAME ="flowName";
    public static final String REACTIVATION ="reactivation_";
    public static final String SPECIAL_CHAR_EXP = "^[A-Za-z0-9 \\n ,.-]+$";
    public static final String XR_LINKING="XR_LINKING";
    
    public static final String FETCH_CUSTOMER_VEHICLE_DETAILS = "FETCH_CUSTOMER_VEHICLE_DETAILS";
    public static final String ONBOARD_CUSTOMER_RETAIL = "ONBOARD_CUSTOMER_RETAIL";
    public static final String VEHICLE_LOVS = "VEHICLE_LOVS";
    public static final String AUTO_FUEL_TYPE="AUTO_FUEL_TYPE";
    public static final String EPIC_LOY_VEHICLE="EPIC_LOY_VEHICLE";
    public static final String EPIC_ASSET_MAKE="EPIC_ASSET_MAKE";
    public static final String EPIC_ASSET_MODEL="EPIC_ASSET_MODEL";
    public static final String AVREQTYPE="Add Vehicle";
    public static final String ONREQTYPE="OnBoarding";
    public static final String EXTSYS="ExternalSystem";
    public static final String CAPortal="CA_Portal";
    public static final String REQ_TYPE="RequestType";
    public static final String REL_FLG="RelationshipFlag";
    public static final String REL_ACT="Active";
    public static final String PROFILE_ENROLLMENT="PROFILE_ENROLLMENT";
    public static final String LOYALTY_STATE_DISTRICT="LOYALTY_STATE_DISTRICT";
    public static final String IS_SHOW_EMAIL_LINK="IS_SHOW_EMAIL_LINK";
    public static final String PROFILE_ENROLLMENT_DUPLICATE="PROFILE_ENROLLMENT_DUPLICATE";
    public static final String KM_FIND_YOUR_OIL_SEARCH="KM_FIND_YOUR_OIL_SEARCH";
    public static final String KM_BROWSE_CHANNEL="KM_BROWSE_CHANNEL";
    public static final String SEARCH_STOCKIST="SEARCH_STOCKIST";
    public static final String FILTER_TYPE="FilterType";
    
}
