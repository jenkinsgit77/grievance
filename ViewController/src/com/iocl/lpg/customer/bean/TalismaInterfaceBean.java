package com.iocl.lpg.customer.bean;

import com.iocl.customer.model.utils.EPICIOCLResourceModel;
import com.iocl.lpg.customer.utils.CommonHelper;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class TalismaInterfaceBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public TalismaInterfaceBean() {
        super();
        String logFlag = EPICIOCLResourceModel.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            logger = Logger.getLogger(TalismaInterfaceBean.class);

        } else {
            logger = Logger.getLogger(TalismaInterfaceBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    
    //pre chat page   
    public static final String PARAM_CUSTOMER_NAME="CUSTOMER_NAME";
    public static final String PARAM_CUSTOMER_EMAILID="CUSTOMER_EMAILID";
    public static final String PARAM_CUSTOMER_MOBNUMBER="CUSTOMER_MOBILE_NUMBER";
    public static final String PARAM_CUSTOMER_LOB="CUSTOMER_LOB";
    public static final String PARAM_CUSTOMER_STATE="CUSTOMER_STATE";
    public static final String PARAM_CUSTOMER_DISTRICT="CUSTOMER_DISTRICT";
    
    public static final String PARAM_EQUALISE="=";
    
    
    //Talisma Access
    public static final String PARAM_TALISMA_ACCESS_ID="talisma_access_id";
    public static final String PARAM_TALISMA_ENCODED_REQUEST="talisma_enc_request";
    public static final String PARAM_TALISMA_ACCESS_CODE="talisma_access_code";
    
    public static final String SOURCE_PARAM = "Customer Portal";
    public static final String PARAM_CUSTOMERTYPE = "Customer";
    
    private static Logger logger ;
    
    private String talismaAuthLink=EPICIOCLResourceModel.findKeyValue("TALISMA_INTERGRATION_AUTHURL_CUST");
    private String talismaUnAuthLink=EPICIOCLResourceModel.findKeyValue("TALISMA_INTERGRATION_UNAUTHURL_CUST");
    
    /**
     * Setting talisma redirect
     * Auth  users
     * @return
     */
    public String getTalismaRedirectUrl(){
        logger.info("***Customer portal Request url Inside talismaRedirectUrl***");
        String returnUrl = "";
        String jspUrl = EPICIOCLResourceModel.findKeyValue("TALISMA_JSP_PATH");
        try {
                   
            logger.info("***Request url For LoggedInUser as session attri consumerid::***" +
                        CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerId}"));
            Object cid = CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerId}");
         //   Object cid= "C1234";
            Object cName = CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerName}");
            Object cEmail = CommonHelper.evaluateEL("#{sessionScope.userDetails.email}");
            Object cMob = CommonHelper.evaluateEL("#{sessionScope.userDetails.mobile}");
            Object cLob = "Customer";
            Object state = CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerAddress}");            
            Object district = CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerCity}");

            logger.info("cid::" + cid + "::cName::" + cName+ "::cEmail::" + cEmail+ "::cMob::" + cMob);
            
            returnUrl = jspUrl + "?CID=" + cid + "&talismaLink=" + talismaAuthLink+ "&sourceParam=" + SOURCE_PARAM+ "&customerType=" + PARAM_CUSTOMERTYPE+
                 "&cName=" + cName+ "&cEmail=" + cEmail+ "&cMob=" + cMob+ "&cLob=" + cLob+ "&state=" + state+ "&district=" + district;
            
        } catch (Exception talEx) {
            returnUrl = jspUrl + "?talismaLink=" + talismaUnAuthLink;
            logger.info("***Inside error returnUrl***" + returnUrl +"::::");
            talEx.printStackTrace();
        }
        logger.info("***returnUrl***" + returnUrl);
        return returnUrl;
    }
    
    /**
     * Setting talisma redirect
     * UnAuth  users
     * @return
     */
    public String getUnauthTalismaRedirectUrl(){
        logger.info("***Customer portal Request Unauth Inside talismaRedirectUrl***");
        String returnUrl = "";
        String jspUrl = EPICIOCLResourceModel.findKeyValue("TALISMA_JSP_PATH");
        try {
                logger.info("***Request url For Unauth User***");
                returnUrl = jspUrl + "?talismaLink=" + talismaUnAuthLink+ "&sourceParam=" + SOURCE_PARAM+ "&customerType=" + PARAM_CUSTOMERTYPE;
           
        } catch (Exception talEx) {
            returnUrl = jspUrl + "?talismaLink=" + talismaUnAuthLink;
            logger.info("***Inside error unauth returnUrl***" + returnUrl +"::::");
            talEx.printStackTrace();
        }
        logger.info("***Unauth returnUrl***" + returnUrl);
        return returnUrl;
    }

    public void setTalismaAuthLink(String talismaAuthLink) {
        this.talismaAuthLink = talismaAuthLink;
    }

    public String getTalismaAuthLink() {
        return talismaAuthLink;
    }

    public void setTalismaUnAuthLink(String talismaUnAuthLink) {
        this.talismaUnAuthLink = talismaUnAuthLink;
    }

    public String getTalismaUnAuthLink() {
        return talismaUnAuthLink;
    }
}
