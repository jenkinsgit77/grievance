package com.iocl.lpg.customer.bean.payment;

import com.ccavenue.security.AesCryptUtil;
import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;
import com.toml.dp.util.AES128Bit;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import oracle.adf.share.ADFContext;

import oracle.binding.OperationBinding;

import org.apache.log4j.Logger;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class PaymentRedirectBean implements Serializable {
    @SuppressWarnings("compatibility:-6692218487843553533")
    private static final long serialVersionUID = 1L;
    private static Logger log;
    private String bankRefNo = null;
    private String paymentStatus = null;
    private String pmtMode;
    private String ccAvResponse;

    public void setCcAvResponse(String ccAvResponse) {
        this.ccAvResponse = ccAvResponse;
    }

    public String getCcAvResponse() {
        return ccAvResponse;
    }

    public void setPmtMode(String pmtMode) {
        this.pmtMode = pmtMode;
    }

    public String getPmtMode() {
        return pmtMode;
    }

    private java.util.List paymentlst = new java.util.ArrayList();

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public PaymentRedirectBean() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(PaymentRedirectBean.class);

        } else {
            log = Logger.getLogger(PaymentRedirectBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }

    public void setBankRefNo(String bankRefNo) {
        this.bankRefNo = bankRefNo;
    }

    public String getBankRefNo() {
        return bankRefNo;
    }

    public String handlePgResponse() {
        String decResp = null;
        StringTokenizer tokenizer = null;
        String ccAvenuePGData = null;
       // String sbiPGData = null;
        Hashtable hs = new Hashtable();
        String pair = null, pname = null, pvalue = null;

        log.info("Reached handlePgResponse");
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request =
            (HttpServletRequest) ctx.getExternalContext().getRequest(); //String workingKey = "F16F5E2F01D6923A84F4EF3FA9578BD8";
        String ccAvWorkingKey =
            EPICIOCLResourceCustBundle.findKeyValue("WORKING_KEY"); // Put in the Working Key provided by CCAVENUES
        //String sbiWorkingKey = EPICIOCLResourceBundle.findKeyValue("SBI_WORKING_KEY");
        log.info("2)inside handlePgResponse+++++++++++++++++++++++++++++++++workingKey---" + ccAvWorkingKey);
       // log.info("2)inside handlePgResponse+++++++++++++++++++++++++++++++++sbiWorkingKey---" + sbiWorkingKey);
        //Put in the Working Key provided by CCAVENUES
        ccAvenuePGData = request.getParameter("encResp");
        //sbiPGData = request.getParameter("encData");
        log.info("3)inside handlePgResponse+ccAvenuePGData---" + ccAvenuePGData);
        this.setCcAvResponse(ccAvenuePGData);
        //log.info("3)inside handlePgResponse+sbiPGData---" + sbiPGData);

        if (ccAvenuePGData != null) {
            AesCryptUtil aesUtil = new AesCryptUtil(ccAvWorkingKey);
            decResp = aesUtil.decrypt(ccAvenuePGData);
            log.info("decCCAvenueResp---"+decResp);
            tokenizer = new StringTokenizer(decResp, "&");

            if (tokenizer != null) {
                while (tokenizer.hasMoreTokens()) {
                    pair = (String) tokenizer.nextToken();
                    if (pair != null) {
                        StringTokenizer strTok = new StringTokenizer(pair, "=");
                        pname = "";
                        pvalue = "";
                        if (strTok.hasMoreTokens()) {
                            pname = (String) strTok.nextToken();
                            if (strTok.hasMoreTokens())
                                pvalue = strTok.nextToken();

                            hs.put(pname, pvalue);
                            if (pname.equalsIgnoreCase("bank_ref_no")) {
                                this.setBankRefNo(pvalue);
                            }
                            if (pname.equalsIgnoreCase("order_status")) {
                                this.setPaymentStatus(pvalue);
                            }
                            if (pname.equalsIgnoreCase("payment_mode")) {
                                this.setPmtMode(pvalue);
                            }
                            log.info("pname--" + pname + "--pvalue--" + pvalue);

                        }

                    }
                }

            }
        }
     if (CommonHelper.evaluateEL("#{sessionScope.UserTrackingId}") != null) {
            log.info("Ensuring valid user session.UserTrackingId--->"+String.valueOf(CommonHelper.evaluateEL("#{sessionScope.UserTrackingId}")));
            updatePaymentInCRM(hs);
        }
        return "updatePayment";
    }


    public void updatePaymentInCRM(Hashtable bnkRet) {
        log.info("updating Paymentstatus In CRM::");
        java.util.Map mapSession = ADFContext.getCurrent().getSessionScope();
        try {

            JSONObject jsonInput = new JSONObject();

            jsonInput.put("TrackingId", CommonHelper.createUniqueID());

//            jsonInput.put("PaymentMode", "Digital");
//            jsonInput.put("GatewaySource", "1");
//            jsonInput.put("OrderNum", bnkRet.get("order_id"));
//            jsonInput.put("PaymentDate", bnkRet.get("trans_date"));
//            //jsonInput.put("PaymentDate", "19/09/2018"); // needs to be validated with siebel for date format - whether they will accept timestamp
//            jsonInput.put("AmountPaid", bnkRet.get("mer_amount"));
//            jsonInput.put("ReferenceNum", bnkRet.get("tracking_id"));
//            jsonInput.put("OrderType", "New"); // need to vet with siebel if required or not
//            jsonInput.put("PaymentStatus", String.valueOf(bnkRet.get("order_status")).equalsIgnoreCase("Aborted") ? "User Cancelled" : bnkRet.get("order_status")); //need to validate param value coming from ccavaenue
//            jsonInput.put("BankName", bnkRet.get("card_name"));
//            jsonInput.put("CardName", bnkRet.get("payment_mode"));
//            jsonInput.put("Amount", bnkRet.get("amount"));
//            jsonInput.put("PaymentReferenceRowNum", mapSession.get("paymentRowTxnId"));
//            jsonInput.put("BankRefNo", this.getBankRefNo());
            
            jsonInput.put("EncResp", this.getCcAvResponse());
            jsonInput.put("OrderId", bnkRet.get("order_id"));

            String callService = EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.UPDATE_PAYMENT_DETAILS);

            paymentlst.add(0, callService);
            paymentlst.add(1, jsonInput);
            mapSession.remove("paymentRowTxnId");
            OperationBinding opBind = CommonHelper.findOperation("updatePaymentDetailsInCRM");
            List retList = (List) opBind.execute();
            log.info("retList--"+retList);
            if (retList == null)
            return;
            String result = String.valueOf(retList.get(0));
            if(result.equalsIgnoreCase("false")){
                this.setPaymentStatus("Failure");
            }
            
        } catch (Exception ex) {
            log.info("exception in payment update::" + ex);
        }
    }
    
    public void setPaymentlst(List paymentlst) {
        this.paymentlst = paymentlst;
    }

    public List getPaymentlst() {
        return paymentlst;
    }

}
