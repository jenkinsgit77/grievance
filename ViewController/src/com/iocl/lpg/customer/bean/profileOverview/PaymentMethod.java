package com.iocl.lpg.customer.bean.profileOverview;

import com.iocl.lpg.customer.bean.subscriptionvoucher.SubscriptionVouch;
import com.iocl.lpg.customer.utils.CommonHelper;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.fragment.RichRegion;

import org.apache.log4j.Logger;

public class PaymentMethod implements Serializable{
    
    @SuppressWarnings("compatibility:-5032307954707168878")
    private static final long serialVersionUID = 1L;

    private RichRegion onlinePaymentRegionBinding;
    private RichRegion paymentRegionBinding;

    public PaymentMethod() {
        super();
    }
    private static Logger log = Logger.getLogger(PaymentMethod.class);
    private boolean paymentSBI;
    private boolean paymentCcAvenue;
    private String paymentMode;
    private String paymentAmount;
    private String paymentTxnId;
    java.util.Map pageflowparam = ADFContext.getCurrent().getPageFlowScope();
    
    public void setPaymentTxnId(String paymentTxnId) {
        this.paymentTxnId = paymentTxnId;
    }

    public String getPaymentTxnId() {
        return paymentTxnId;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }


    public void setPaymentSBI(boolean paymentSBI) {
        this.paymentSBI = paymentSBI;
    }

    public boolean isPaymentSBI() {
        return paymentSBI;
    }

    public void setPaymentCcAvenue(boolean paymentCcAvenue) {
        this.paymentCcAvenue = paymentCcAvenue;
    }

    public boolean isPaymentCcAvenue() {
        return paymentCcAvenue;
    }
    
    public void makeOnlinePaymentAction(ActionEvent actionEvent) {
            // Add event code here...
            java.util.Map pageflowparam = ADFContext.getCurrent().getPageFlowScope();
            paymentMode = "sbi";
            if (paymentSBI) {
                log.info("paymentSBI" + paymentSBI);
                paymentMode = "sbi";
            }
            if (paymentCcAvenue) {
                log.info("paymentCcAvenue" + paymentCcAvenue);
                paymentMode = "cc";
            }
            paymentTxnId = CommonHelper.createUniqueID();
            paymentAmount = "12345";
            //        pageflowparam.put("paymentTxnCAS",pageflowparam.get("PaymentRefCAS"));
            //        pageflowparam.put("amountCAS",pageflowparam.get("amountCAS"));
        CommonHelper.refreshLayout(onlinePaymentRegionBinding);

        }

    public void setOnlinePaymentRegionBinding(RichRegion onlinePaymentRegionBinding) {
        this.onlinePaymentRegionBinding = onlinePaymentRegionBinding;
    }

    public RichRegion getOnlinePaymentRegionBinding() {
        return onlinePaymentRegionBinding;
    }

    public void paymentModeValueCl(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != null) {
            log.info("New Value: " + valueChangeEvent.getNewValue());
            
            log.info("Paramter Receive from Subscription Voucher Flow");
            log.info("paymentamount: "+pageflowparam.get("paymentamount"));
            log.info("paymentTxn: "+pageflowparam.get("paymentTxn"));
            log.info("paymentMode: "+pageflowparam.get("paymentMode"));
            
            if (valueChangeEvent.getNewValue()
                                .toString()
                                .equalsIgnoreCase("SBI")) {
                pageflowparam.put("paymentMode","sbi");
                CommonHelper.refreshLayout(paymentRegionBinding);
            } else if (valueChangeEvent.getNewValue()
                                       .toString()
                                       .equalsIgnoreCase("CCAVENUE")) {
                pageflowparam.put("paymentMode","cc");
                CommonHelper.refreshLayout(paymentRegionBinding);
            }
        }
    }

    public void setPaymentRegionBinding(RichRegion paymentRegionBinding) {
        this.paymentRegionBinding = paymentRegionBinding;
    }

    public RichRegion getPaymentRegionBinding() {
        return paymentRegionBinding;
    }

    public String codSubmitAction() {
        // Add event code here...
        String retString = "goToConfirmation";
        log.info("orderNumber: "+pageflowparam.get("orderNumber"));
        pageflowparam.put("subConfMessage",CommonHelper.getValueFromRsBundle("PAYMENT_CONFIRMATION_MESSAGE"));
        pageflowparam.put("subRefNo",pageflowparam.get("orderNumber"));
        return retString;
    }
}
