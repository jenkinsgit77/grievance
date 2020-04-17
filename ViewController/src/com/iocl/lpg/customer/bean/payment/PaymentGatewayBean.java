package com.iocl.lpg.customer.bean.payment;

import com.ccavenue.security.AesCryptUtil;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import com.toml.dp.util.AES128Bit;

import java.io.IOException;
import java.io.Serializable;

import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;

import oracle.binding.OperationBinding;

import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONObject;

public class PaymentGatewayBean implements Serializable {
    @SuppressWarnings("compatibility:7646794310890046813")
    private static final long serialVersionUID = 1L;

    public PaymentGatewayBean() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(PaymentGatewayBean.class);

        } else {
            log = Logger.getLogger(PaymentGatewayBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    private static Logger log ;
            
    public static final String PARAM_AMOUNT="amount";
    public static final String PARAM_MERCHANT_ID="merchant_id";
    public static final String PARAM_CANCEL_URL="cancel_url";
    public static final String PARAM_CURRENCY="currency";
    public static final String PARAM_INTEGRATION_TYPE="integration_type";
    public static final String PARAM_LANGUAGE="language";
    public static final String PARAM_ORDER_ID="order_id";
    public static final String PARAM_REDIRECT_URL="redirect_url";
    public static final String PARAM_MERCHANT_PARAM1="merchant_param1";
    public static final String CCAVAENUE_BASE_URL =  EPICIOCLResourceCustBundle.findKeyValue("CCAVAENUE_BASE_URL");
    public static final String PARAM_ENCODED_REQUEST="encRequest";
    public static final String PARAM_ACCESS_CODE="access_code";
    public static final String PARAM_CUST_ID="customer_identifier";
    public static final String TID="tid";
    
    
    public void setInitiatePaymentLst(List initiatePaymentLst) {
        this.initiatePaymentLst = initiatePaymentLst;
    }

    public List getInitiatePaymentLst() {
        return initiatePaymentLst;
    }

    private java.util.List initiatePaymentLst = new java.util.ArrayList();
    java.util.Map mapSession = ADFContext.getCurrent().getSessionScope();
    
    public String getSbiPGSecureUrl(){
        Object ucmId =CommonHelper.evaluateEL("#{sessionScope.userDetails.ucmID}");
        
        String jspUrl = EPICIOCLResourceCustBundle.findKeyValue("SBI_JSP_PATH");
        String merchantId = EPICIOCLResourceCustBundle.findKeyValue("SBI_MERCHANT_ID");  
                String workingKey = EPICIOCLResourceCustBundle.findKeyValue("SBI_WORKING_KEY");  
        java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
        String amount =  String.valueOf(param.get("amount"));
        String orderId =  String.valueOf(param.get("paymentRowTxnId"));
                String requestparameter= merchantId+"|DOM|IN|INR|"+amount+"|OtherDetails|"+EPICIOCLResourceCustBundle.findKeyValue("REDIRECT_URL")+"|"+EPICIOCLResourceCustBundle.findKeyValue("REDIRECT_URL")+"|SBIEPAY|"+orderId+"|"+String.valueOf(ucmId)+"|NB|ONLINE|ONLINE";
                log.info("sbi requestparameter-->"+requestparameter);
                String encryptTrans   = AES128Bit.encrypt(requestparameter, workingKey);
        String encDataRefined = encryptTrans.replaceAll("\\+", "%2B");
        String returnUrl = jspUrl+"?merchantId="+merchantId+"&encryptTrans="+encDataRefined;
        return returnUrl;
    }
    
    
    public String getPGSecureUrl(){
        
        
        //UserProfileBean usrBean = (UserProfileBean) mapSession.get(EPICConstant.SESSION_USER_DETAILS);
        
       Object ucmId =CommonHelper.evaluateEL("#{sessionScope.userDetails.ucmID}");
       
       System.out.println("==="+CommonHelper.evaluateEL("#{sessionScope.userDetails.ucmID}"));
       
        System.out.println("==="+CommonHelper.evaluateEL("#{sessionScope.IS_LOGGED_IN}"));
        
        String jspUrl = EPICIOCLResourceCustBundle.findKeyValue("CCAVAENUE_JSP_PATH");
        
        String merchantId = EPICIOCLResourceCustBundle.findKeyValue("MERCHANT_ID");   
        String accessCode = EPICIOCLResourceCustBundle.findKeyValue("ACCESS_CODE");      // Put in the Access Code provided by CCAVENUES
        String workingKey = EPICIOCLResourceCustBundle.findKeyValue("WORKING_KEY");    // Put in the Working Key provided by CCAVENUES
        
        Object payTxRowId=CommonHelper.evaluateEL("#{sessionScope.paymentRowTxnId}") ;
        System.out.println("payTxRowId**************CUST="+String.valueOf(payTxRowId));
        
        System.out.println("workingKey: "+workingKey);
        java.util.Map param = ADFContext.getCurrent().getPageFlowScope();       
                
        StringBuffer ccaRequestBuffer = new StringBuffer("");
        ccaRequestBuffer.append(PARAM_AMOUNT);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append(String.valueOf(param.get("amount")));
        //ccaRequestBuffer.append("500.00");
        ccaRequestBuffer.append("&");
        
        ccaRequestBuffer.append(PARAM_CANCEL_URL);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append(EPICIOCLResourceCustBundle.findKeyValue("CANCEL_URL"));
        ccaRequestBuffer.append("&");
        
        ccaRequestBuffer.append(PARAM_CURRENCY);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append("INR");
        ccaRequestBuffer.append("&");
        
//        ccaRequestBuffer.append(PARAM_INTEGRATION_TYPE);
//        ccaRequestBuffer.append("=");
//        ccaRequestBuffer.append("iframe_normal");
//        ccaRequestBuffer.append("&");
        
        ccaRequestBuffer.append(PARAM_LANGUAGE);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append("EN");
        ccaRequestBuffer.append("&");
        
        ccaRequestBuffer.append(PARAM_MERCHANT_ID);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append(merchantId);
        ccaRequestBuffer.append("&");
        
        ccaRequestBuffer.append(PARAM_ORDER_ID);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append(String.valueOf(param.get("orderNo")));
        //ccaRequestBuffer.append("abcd12345");
        ccaRequestBuffer.append("&");
        
//        ccaRequestBuffer.append(PARAM_MERCHANT_PARAM1);
//        ccaRequestBuffer.append("=");
//        ccaRequestBuffer.append("MERCHANTPARAM1Value");
//        ccaRequestBuffer.append("&");
        
        ccaRequestBuffer.append(PARAM_REDIRECT_URL);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append(EPICIOCLResourceCustBundle.findKeyValue("REDIRECT_URL"));
        
        ccaRequestBuffer.append("&");
        
        ccaRequestBuffer.append(PARAM_CUST_ID);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append(String.valueOf(ucmId));
        
        ccaRequestBuffer.append("&");

        ccaRequestBuffer.append(TID);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append(String.valueOf(System.currentTimeMillis()));
        
        ccaRequestBuffer.append("&");

        ccaRequestBuffer.append(EPICConstant.MERCHANT_PARAM1);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append(String.valueOf(payTxRowId));
        
        ccaRequestBuffer.append("&");
        
        ccaRequestBuffer.append(EPICConstant.MERCHANT_PARAM2);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append("1");
        
        ccaRequestBuffer.append("&");
        
        ccaRequestBuffer.append(EPICConstant.MERCHANT_PARAM3);
        ccaRequestBuffer.append("=");
        ccaRequestBuffer.append("Portal");
        
        
        String ccaRequest = ccaRequestBuffer.toString();
        log.info("ccaRequest---"+ccaRequest);
                
        AesCryptUtil aesUtil=new AesCryptUtil(workingKey);
        String encRequest = aesUtil.encrypt(ccaRequest);
        log.info("encRequest---"+encRequest);
        //String url = CCAVAENUE_BASE_URL+PARAM_MERCHANT_ID+"="+merchantId+"&"+PARAM_ENCODED_REQUEST+"="+encRequest+"&"+PARAM_ACCESS_CODE+"="+accessCode;
        String returnUrl = jspUrl+"?encRequest="+encRequest+"&accessCode="+accessCode;
        return returnUrl;
    }
   
    public String createPaymentRecord() {
        String pmtRowId = null;
//        String orderId=null;
//        String invoiceID=null;
//        String  invoiceNum=null;
//        String  accountId=null;
//        String  onlineDiscount=null;
        log.info("createPaymentRecord start::");
        try {
            
            java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
            
            log.info("orderNo-"+String.valueOf(param.get("orderNo")));
            log.info("orderNo-"+String.valueOf(param.get("orderNo")));
            log.info("amount-"+String.valueOf(param.get("amount")));
            JSONObject jsonInput = new JSONObject();

            jsonInput.put("TrackingId", CommonHelper.createUniqueID());
            jsonInput.put("OrderNum", String.valueOf(param.get("orderNo")));
            jsonInput.put("GatewaySource", "1");

            String callService = EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.INITIATE_PAYMENT);

            initiatePaymentLst.add(0, callService);
            initiatePaymentLst.add(1, jsonInput);

            OperationBinding opBind = CommonHelper.findOperation("initiatePaymentDetails");
            List retList = (List) opBind.execute();
            log.info("retList--" + retList);

            if (retList.size() > 0) {
                if (CustomerValidation.isNull(retList.get(0)) || retList.get(0)
                                                                   .toString()
                                                                   .equalsIgnoreCase("TRUE")) {
                    JSONObject jsonObject = (JSONObject) retList.get(1);
                    log.info("values in jsonobject" + jsonObject);
                    if (!jsonObject.isNull("ErrorCode") &&
                        jsonObject.getString(EPICConstant.ERROR_CODE).equalsIgnoreCase("0")) {


                        pmtRowId = String.valueOf(jsonObject.get("PaymentReferenceRowNum"));
//                        orderId= jsonObject.isNull("OrderId") ? "NA" : jsonObject.getString("OrderId");
//                           mapSession.put("orderId", orderId);
//                            invoiceID= jsonObject.isNull("InvoiceId") ? "NA" : jsonObject.getString("InvoiceId");
//                           mapSession.put("invoiceId", invoiceID);
//                           invoiceNum= jsonObject.isNull("InvoiceNum") ? "NA" : jsonObject.getString("InvoiceNum");
//                           mapSession.put("invoiceNumber", invoiceNum);
//                           accountId= jsonObject.isNull("AccountId") ? "NA" : jsonObject.getString("AccountId");
//                           mapSession.put("accountID", accountId);
//                           onlineDiscount= jsonObject.isNull("OnlineDiscount") ? "" : jsonObject.getString("OnlineDiscount");
//                           mapSession.put("onlineDiscount", onlineDiscount);
                       // param.put("paymentRowTxnId", pmtRowId);
                       mapSession.put("paymentRowTxnId", pmtRowId);
                        log.info("payment row reference num from variable:" + pmtRowId);

                        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
                        try {
                            String url = getPGSecureUrl();
                            log.info("getPGSecureUrl--" + url);
                            exct.redirect(url);
                        } catch (IOException e) {
                            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
                        }


                    } else {
                        return EPICConstant.ERROR;
                    }


                } else {
                    return EPICConstant.ERROR;
                }
            } else {
                return EPICConstant.ERROR;
            }



        }

        catch (Exception ex) {
            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
            log.info("exception in payment update::" + ex);
            return EPICConstant.ERROR;

        }


        return null;


    }
}
