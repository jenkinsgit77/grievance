package com.iocl.lpg.customer.bean.subscriptionvoucher;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;

import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;
import org.json.JSONException;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class SubscriptionVouch implements Serializable {
    @SuppressWarnings("compatibility:960710921003088716")
    private static final long serialVersionUID = 1L;
    private RichSelectOneRadio svConnectionBinding;
    private RichPanelGroupLayout steelTopLpgPanelBinding;
    private RichPanelGroupLayout glassTopLpgPanelBinding;
    private RichPanelGroupLayout stovePanelBinding;
    private String lpgStoveError;
    private String itemLengthError;
    private boolean errorInsubsVoucher;
    private RichPopup lpgStovePopUpBinding;
    private RichPopup lpgHosePopUpBinding;
    private String productSelectionError;
    private String svNeedConnError;
    private List<SelectItem> distributerProdLst;

    public void setDistributerProdLst(List<SelectItem> distributerProdLst) {
        this.distributerProdLst = distributerProdLst;
    }

    public List<SelectItem> getDistributerProdLst() {
        return distributerProdLst;
    }

    public void setSvConnectionBinding(RichSelectOneRadio svConnectionBinding) {
        this.svConnectionBinding = svConnectionBinding;
    }

    public RichSelectOneRadio getSvConnectionBinding() {
        return svConnectionBinding;
    }
    
    public void setSvNeedConnError(String svNeedConnError) {
        this.svNeedConnError = svNeedConnError;
    }

    public String getSvNeedConnError() {
        return svNeedConnError;
    }
    
    public void setProductSelectionError(String productSelectionError) {
        this.productSelectionError = productSelectionError;
    }

    public String getProductSelectionError() {
        return productSelectionError;
    }

    public void setErrorInsubsVoucher(boolean errorInsubsVoucher) {
        this.errorInsubsVoucher = errorInsubsVoucher;
    }

    public boolean isErrorInsubsVoucher() {
        return errorInsubsVoucher;
    }

    public void setLpgStoveError(String lpgStoveError) {
        this.lpgStoveError = lpgStoveError;
    }

    public String getLpgStoveError() {
        return lpgStoveError;
    }

    public void setItemLengthError(String itemLengthError) {
        this.itemLengthError = itemLengthError;
    }

    public String getItemLengthError() {
        return itemLengthError;
    }

    public SubscriptionVouch() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(SubscriptionVouch.class);

        } else {
            log = Logger.getLogger(SubscriptionVouch.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    private static Logger log ;
    private java.util.List<SubscriptionVouchItems> itemDetails = new java.util.ArrayList<SubscriptionVouchItems>();
    private java.util.List<MandatoryCharges> MandChargesDetails = new java.util.ArrayList<MandatoryCharges>();
    private java.util.List<OtherCharges> OtherChargesDetails = new java.util.ArrayList<OtherCharges>();
    private java.util.List<SVProductCharges> SVProductChargesDetails = new java.util.ArrayList<SVProductCharges>();
    java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();

    public void setMandChargesDetails(List<MandatoryCharges> MandChargesDetails) {
        this.MandChargesDetails = MandChargesDetails;
    }

    public List<MandatoryCharges> getMandChargesDetails() {
        return MandChargesDetails;
    }

    public void setOtherChargesDetails(List<OtherCharges> OtherChargesDetails) {
        this.OtherChargesDetails = OtherChargesDetails;
    }

    public List<OtherCharges> getOtherChargesDetails() {
        return OtherChargesDetails;
    }

    public void setItemDetails(List<SubscriptionVouchItems> itemDetails) {
        this.itemDetails = itemDetails;
    }

    public List<SubscriptionVouchItems> getItemDetails() {
        return itemDetails;
    }

    public String subsVoucherOnload() {
        String retString = "goToSubsVouch";
        log.info("Inside subsVoucherOnload");
        
        try {
            JSONObject jsonInput = new JSONObject();

            java.util.List lst = new java.util.ArrayList();
            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("SUBSCRIPTION_VOUCHER_REQUEST"));
            // 1-jsonInput
            lst.add(1, jsonInput);

            OperationBinding op = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            op.getParamsMap().put("inputList", lst);
            op.getParamsMap().put("method", EPICConstant.SUBSCRIPTION_VOUCHER_REQUEST);
            op.execute();


            java.util.List ReturnList = (java.util.List) op.getResult();
            if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                                  .toString()
                                                                                  .equalsIgnoreCase("true"))) {
                log.info("ReturnList Value at 0 index is " + ReturnList.get(0));
            } else {
                log.info("Error from Service ReturnList.get(0) is not true");
                retString = "ERROR";
            }
        } catch (JSONException jsone) {
            // TODO: Add catch code
            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
            retString = "ERROR";
            jsone.printStackTrace();
            log.info("Error in subsVoucherOnload Method");
        }
        CommonHelper.runJavaScript("dataTableInvoke()");
        return retString;
    }

    public void insertProduct(String jsonString) {
        log.info("Inside insertProduct Start");
        JSONObject jsonResponse = new JSONObject(jsonString);
        JSONArray arrObj = jsonResponse.getJSONArray("Items");
        System.out.println("arrObj" + arrObj);
        /*-----------Code to Populate Subscription Items--------*/
        // Items default
        for (int i = 0; i < arrObj.length(); i++) {
            JSONObject objOD = arrObj.getJSONObject(i);

            SubscriptionVouchItems subsVouchObj = new SubscriptionVouchItems();
            subsVouchObj.setItemName(objOD.isNull(EPICConstant.ITEM_NAME) ? EPICConstant.NOT_APPLICABLE :
                                     objOD.getString(EPICConstant.ITEM_NAME).toString());
            subsVouchObj.setItemCode(objOD.isNull(EPICConstant.ITEM_CODE) ? EPICConstant.NOT_APPLICABLE :
                                     objOD.getString(EPICConstant.ITEM_CODE).toString());
            subsVouchObj.setItemDesc(objOD.isNull(EPICConstant.DESCRIPTION) ? EPICConstant.NOT_APPLICABLE :
                                     objOD.getString(EPICConstant.DESCRIPTION).toString());
            subsVouchObj.setItemMrp(objOD.isNull(EPICConstant.MRP) ? EPICConstant.NOT_APPLICABLE :
                                    objOD.getString(EPICConstant.MRP).toString());
            /*-----------------------*/
            JSONArray itemDtlsAr = objOD.getJSONArray("LengthType");
            System.out.println("itemDtlsAr Length" + itemDtlsAr.length());
            List<SelectItem> itemLst = null;
            itemLst = new ArrayList<SelectItem>();
            for (int j = 0; j < itemDtlsAr.length(); j++) {
                JSONObject rec = itemDtlsAr.getJSONObject(j);
                String code = rec.isNull("Length") == true ? "" : rec.getString("Length");
                String partnerName = rec.isNull("Length") == true ? "" : rec.getString("Length");
                itemLst.add(new SelectItem(code, partnerName));
            }
            System.out.println("itemLst " + itemLst);
            subsVouchObj.setItemLst(itemLst);
            //            param.put("pDistributorLst", itemLst);
            /*-----------------------*/
            itemDetails.add(subsVouchObj);
        }
        for (SubscriptionVouchItems abc : itemDetails) {
            log.info("itemCode" + abc.getItemCode());
        }
        pageflowParam.put("itemDetailsPF", itemDetails);


        //        log.info("Start of Pageflow Scope");
        //        for (SubscriptionVouchItems abc : ( List<SubscriptionVouchItems>)pageflowParam.get("itemDetailsPF")) {
        //            log.info("ItemCode "+abc.getItemCode()+" ItemName: "+abc.getItemName());
        //        }
        /*-------------------------End Here---------------------*/
        log.info("Inside insertProduct End");
    }

    public boolean subsVoucherValidation() {
        log.info("subsVoucherValidation method Start");
        errorInsubsVoucher = false;

        Boolean lpgStoveSelected = false;
        Boolean lpgHoseSelected = false;
        
        if (CustomerValidation.isNull(svConnectionBinding.getValue())) {
                    errorInsubsVoucher = true;
                    svNeedConnError = CommonHelper.getValueFromRsBundle("SELECT_CONNECTION_TYPE");
//                    pageflowParam.put("svNeedConnError", svNeedConnError);
            }
            
        /*DCIteratorBinding stoveIter = CommonHelper.findIterator("LPGStoveProductVo1Iterator");
        Row[] stoveRow = stoveIter.getAllRowsInRange();
        for (int i = 0; i < stoveRow.length; i++) {
            if (stoveRow[i].getAttribute("ProductSelected") != null &&
                stoveRow[i].getAttribute("ProductSelected").equals(true)) {
                lpgStoveSelected = true;
            }
        }

        
        DCIteratorBinding hoseIter = CommonHelper.findIterator("LPGHoseProductVo1Iterator");
        Row[] hoseRow = hoseIter.getAllRowsInRange();
        for (int i = 0; i < hoseRow.length; i++) {
            if (hoseRow[i].getAttribute("ProductSelected") != null &&
                hoseRow[i].getAttribute("ProductSelected").equals(true)) {
                lpgHoseSelected = true;
            }
        }

        if (lpgStoveSelected.equals(false) && lpgHoseSelected.equals(false)) {
            productSelectionError = CommonHelper.getValueFromRsBundle("SELECT_PRODUCT");
//            pageflowParam.put("productSelectionError", productSelectionError);s            
            errorInsubsVoucher = true;
        }*/

        log.info("errorInsubsVoucher" + errorInsubsVoucher);
        log.info("subsVoucherValidation method Start");
        return errorInsubsVoucher;
    }

    public void resetSubsVoucherValidation() {
        productSelectionError = null;
        svNeedConnError = null;
        
//        pageflowParam.put("svNeedConnError", null);
//        pageflowParam.put("productSelectionError", null);
    }

    public String SubsVouchContinueAction() {
        // Add event code here...
        log.info("Inside SubsVouchContinueAction Start");
        String vretString = "goToSubsOrder";
        try {
            resetSubsVoucherValidation();

            errorInsubsVoucher = subsVoucherValidation();
            if (errorInsubsVoucher == true) {
                log.info("Case1");
//                CommonHelper.refreshPage();
                return null;
            }
           vretString =  subsVouchOrderOnload();

        } catch (Exception e) {
            // TODO: Add catch code
            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
            e.printStackTrace();
            return "ERROR";
        }
        return vretString;
    }

    public String subsVouchOrderOnload() {
        // Items default
        String retString = "goToSubsOrder";
        log.info("Inside subsVouchOrderOnload");

        JSONArray productArrayObj = new JSONArray();

        DCIteratorBinding iter = CommonHelper.findIterator("SvLpgStoveProductVo1Iterator");
        ViewObject vo = iter.getViewObject();
        Row[] row = vo.getAllRowsInRange();
        for (int i = 0; i < row.length; i++) {
            if (row[i].getAttribute("ProductSelected") != null && row[i].getAttribute("ProductSelected")
                                                                        .toString()
                                                                        .equalsIgnoreCase("true")) {
                JSONObject jsonProduct = new JSONObject();
                jsonProduct.put("ProductCode",row[i].getAttribute("ProductCode"));
                jsonProduct.put("ProductName",row[i].getAttribute("ProductName"));
                jsonProduct.put("Price",row[i].getAttribute("Mrp"));
                productArrayObj.put(jsonProduct);
                break;
            }
        }

        iter = CommonHelper.findIterator("SvLpgHoseProductVo1Iterator");
        vo = iter.getViewObject();
        row = vo.getAllRowsInRange();
        for (int i = 0; i < row.length; i++) {
            if (row[i].getAttribute("ProductSelected") != null && row[i].getAttribute("ProductSelected")
                                                                        .toString()
                                                                        .equalsIgnoreCase("true")) {
                JSONObject jsonProduct = new JSONObject();
                jsonProduct.put("ProductCode",row[i].getAttribute("ProductCode"));
                jsonProduct.put("ProductName",row[i].getAttribute("ProductName"));
                jsonProduct.put("Price",row[i].getAttribute("Mrp"));
                productArrayObj.put(jsonProduct);
                break;
            }
        }

        try {

            log.info("productArrayObj" + productArrayObj);
            JSONObject jsonInput = new JSONObject();
            jsonInput.put(EPICConstant.SV_CONNECTION, svConnectionBinding.getValue());
            //            jsonInput.put(EPICConstant.EQUIPMENT_TYPE, svEquipmentBinding.getValue());
            jsonInput.put(EPICConstant.SV_PRODUCT, productArrayObj);

            java.util.List lst = new java.util.ArrayList();
            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("SUBSCRIPTION_VOUCHER_ORDER"));
            // 1-jsonInput
            lst.add(1, jsonInput);

            OperationBinding op = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            op.getParamsMap().put("inputList", lst);
            op.getParamsMap().put("method", EPICConstant.SUBSCRIPTION_VOUCHER_ORDER);
            op.execute();
            java.util.List ReturnList = (java.util.List) op.getResult();
            if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                                  .toString()
                                                                                  .equalsIgnoreCase("true"))) {
                log.info("ReturnList Value at 0 index is " + ReturnList.get(0));
                if (ReturnList.get(1) != null) {
                    insertSvOrder(ReturnList.get(1).toString());
                    /**updatePaymentDetails method is called here instead of at make Payment**/
                    updatePaymentDetails(ReturnList.get(1).toString());
                }
            } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                       (ReturnList.get(0).toString().equalsIgnoreCase("false") && 
                        ReturnList.get(1) != null)) {
                JSONObject jsonResponse = new JSONObject(ReturnList.get(1).toString());

                pageflowParam.put("pValidCodeSubVoucher",
                                  jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_CODE));
                pageflowParam.put("pValidMsgSubVoucher",
                                  jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


                if (pageflowParam.get("pValidCodeSubVoucher") != null && pageflowParam.get("pValidCodeSubVoucher")
                                                                            .toString()
                                                                            .equalsIgnoreCase(EPICConstant.SBL100)) {
                    log.info("Business Validation Issue SBL-100 ErrorCode Received");
                    return null;
                } else {
                    log.info("Some Error in WebService Response");
                    retString = "ERROR";
                    return retString;
                }
            } else {
                log.info("Error from Service ReturnList.get(0) is not true");
                return "ERROR";
            }
        } catch (JSONException jsone) {
            // TODO: Add catch code
            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
            jsone.printStackTrace();
            log.info("Error in subsVouchOrderOnload Method");
        }
        return retString;
    }


    public void insertSvOrder(String jsonString) {
        log.info("Inside insertSvOrder method start");
        JSONObject jsonResponse = new JSONObject(jsonString);
        JSONArray arrObj = jsonResponse.getJSONArray("Product");
        for (int i = 0; i < arrObj.length(); i++) {
            JSONObject objOD = arrObj.getJSONObject(i);

            SVProductCharges obj = new SVProductCharges();
            obj.setItemCode(objOD.isNull(EPICConstant.SV_PRODUCT_CODE) ? EPICConstant.NOT_APPLICABLE :
                                      objOD.getString(EPICConstant.SV_PRODUCT_CODE).toString());
            obj.setItemName(objOD.isNull(EPICConstant.SV_PRODUCT_NAME) ? EPICConstant.NOT_APPLICABLE :
                                      objOD.getString(EPICConstant.SV_PRODUCT_NAME).toString());
            obj.setItemPrice(objOD.isNull(EPICConstant.SV_ITEM_PRICE) ? EPICConstant.NOT_APPLICABLE :
                                       objOD.getString(EPICConstant.SV_ITEM_PRICE).toString());

            SVProductChargesDetails.add(obj);

        }
        pageflowParam.put("svProductChargesDetailsPF", SVProductChargesDetails);
        pageflowParam.put("svProductTotalPF", jsonResponse.isNull("TotalPrice") ? 0 : jsonResponse.get("TotalPrice"));
        pageflowParam.put("svOrderRefNoPF", jsonResponse.isNull("OrderReferenceNumber") ? null : jsonResponse.get("OrderReferenceNumber"));
        
        log.info("Inside insertSvOrder method end");
    }


    public void setSteelTopLpgPanelBinding(RichPanelGroupLayout steelTopLpgPanelBinding) {
        this.steelTopLpgPanelBinding = steelTopLpgPanelBinding;
    }

    public RichPanelGroupLayout getSteelTopLpgPanelBinding() {
        return steelTopLpgPanelBinding;
    }

    public void setGlassTopLpgPanelBinding(RichPanelGroupLayout glassTopLpgPanelBinding) {
        this.glassTopLpgPanelBinding = glassTopLpgPanelBinding;
    }

    public RichPanelGroupLayout getGlassTopLpgPanelBinding() {
        return glassTopLpgPanelBinding;
    }

    public void setStovePanelBinding(RichPanelGroupLayout stovePanelBinding) {
        this.stovePanelBinding = stovePanelBinding;
    }

    public RichPanelGroupLayout getStovePanelBinding() {
        return stovePanelBinding;
    }

    public void lpgStoveClickActionLis(ActionEvent actionEvent) {
        // Add event code here...
        CommonHelper.invokePopup(lpgStovePopUpBinding);
    }

    public void setLpgStovePopUpBinding(RichPopup lpgStovePopUpBinding) {
        this.lpgStovePopUpBinding = lpgStovePopUpBinding;
    }

    public RichPopup getLpgStovePopUpBinding() {
        return lpgStovePopUpBinding;
    }

    public void lpgHoseClickActionLis(ActionEvent actionEvent) {
        // Add event code here...
//        CommonHelper.invokePopup(lpgHosePopUpBinding);
        CommonHelper.invokePopup(lpgStovePopUpBinding); // We are calling same popUp as lpgStove 
    }

    public void setLpgHosePopUpBinding(RichPopup lpgHosePopUpBinding) {
        this.lpgHosePopUpBinding = lpgHosePopUpBinding;
    }

    public RichPopup getLpgHosePopUpBinding() {
        return lpgHosePopUpBinding;
    }

    public void lpgHoseCloseActionLis(ActionEvent actionEvent) {
        // Add event code here...
        lpgHosePopUpBinding.hide();
    }

    public void lpgStoveCloseActionLis(ActionEvent actionEvent) {
        // Add event code here...
        lpgStovePopUpBinding.hide();
    }

    public void SubsVouchResetActionLis(ActionEvent actionEvent) {
        // Add event code here...
        resetSubsVoucherValidation();
        
        /**---Below Code is commented as this causing table to apper multiple times as we click on reset--**/
        DCIteratorBinding stoveIter = CommonHelper.findIterator("LPGStoveProductVo1Iterator");
        Row[] stoveRow = stoveIter.getAllRowsInRange();
        for (int i = 0; i < stoveRow.length; i++) {
            stoveRow[i].setAttribute("ProductSelected",null);
        }

        
        DCIteratorBinding hoseIter = CommonHelper.findIterator("LPGHoseProductVo1Iterator");
        Row[] hoseRow = hoseIter.getAllRowsInRange();
        for (int i = 0; i < hoseRow.length; i++) {
                hoseRow[i].setAttribute("ProductSelected",null);
        }
        
//        CommonHelper.refreshPage();//To bring back Table Search and Pagination
    }

    public String subsVouchPaymentAction() {
        // Add event code here...
        log.info("Inside subsVouchPaymentAction");
        String retString = "goToPayment";
//        try {
//            OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
//            java.util.List lst = new java.util.ArrayList();
//            JSONObject jsonInput = new JSONObject();
//            jsonInput.put(EPICConstant.ORDER_NUM, pageflowParam.get("svOrderRefNoPF"));
//            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("BOOKREFILL_ORDERPRICINGTY"));
//            lst.add(1, jsonInput);
//            ob.getParamsMap().put("inputList", lst);
//            ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.BOOKREFILLPRICELIST);
//            ob.execute();
//            java.util.List ReturnList = (java.util.List) ob.getResult();
//            if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
//                                                                                  .toString()
//                                                                                  .equalsIgnoreCase("true"))) {
//                if (ReturnList.get(1) != null) {
//                    updatePaymentDetails(ReturnList.get(1).toString());
//                }
//            } else if ((ReturnList != null && ReturnList.get(0) != null) &&
//                       (ReturnList.get(0).toString().equalsIgnoreCase("false") && ReturnList.get(1) != null)) {
//                JSONObject jsonResponse = new JSONObject(ReturnList.get(1).toString());
//
//                pageflowParam.put("pValidCodeSubVouchOrder",
//                                  jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
//                                  jsonResponse.getString(EPICConstant.ERROR_CODE));
//                pageflowParam.put("pValidMsgSubVouchOrder",
//                                  jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
//                                  jsonResponse.getString(EPICConstant.ERROR_MESSAGE));
//
//
//                if (pageflowParam.get("pValidCodeSubVouchOrder") != null && pageflowParam.get("pValidCodeSubVouchOrder")
//                                                                            .toString()
//                                                                            .equalsIgnoreCase(EPICConstant.SBL100)) {
//                    log.info("Business Validation Issue SBL-100 ErrorCode Received");
//                    return null;
//                } else {
//                    log.info("Some Error in WebService Response");
//                    retString = "ERROR";
//                    return retString;
//                }
//            } else {
//                log.info("Error from Service ReturnList.get(0) is not true");
//                return "ERROR";
//            }
//
//        } catch (JSONException jsone) {
//            jsone.printStackTrace();
//            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
//            retString = "ERROR";
//        }
        log.info("ends fetchpricingDetails");
        return retString;
    }
    
    
    public void updatePaymentDetails(String jsonString)
    {
            JSONObject jsonResponse = new JSONObject(jsonString);
//            JSONArray paymentArrayObj =
//                jsonResponse.isNull("PaymentDetails") ? null :
//                jsonResponse.getJSONArray("PaymentDetails");
//            for (int i = 0; i < paymentArrayObj.length(); i++) {
//                JSONObject paymentObj = paymentArrayObj.getJSONObject(i);
//                if (paymentObj.get("Description") != null &&
//                    paymentObj.getString("Description").equalsIgnoreCase("Final Price")) {
//                    pageflowParam.put("amountSV", paymentObj.isNull("Amount") ? 0 : paymentObj.get("Amount"));
//                }
//            }
          /**------------Below attributes are set from FetchPricingDetails Service earlier-----**/  
//            pageflowParam.put("orderNum", jsonResponse.isNull("OrderNo") ? "" : jsonResponse.get("OrderNo"));
//            pageflowParam.put("paymentTxnSV",jsonResponse.isNull("PaymentReferenceRowNum") ? "" :jsonResponse.get("PaymentReferenceRowNum"));
//            pageflowParam.put("amountSV", jsonResponse.isNull("OrderTotal") ? "" : jsonResponse.get("OrderTotal"));
          /**----------------------------------End Here-----------------------**/
            
        pageflowParam.put("orderNum", jsonResponse.isNull("OrderReferenceNumber") ? "" : jsonResponse.get("OrderReferenceNumber"));
        pageflowParam.put("paymentTxnSV",jsonResponse.isNull("PaymentReferenceRowNum") ? "" :jsonResponse.get("PaymentReferenceRowNum"));
        pageflowParam.put("amountSV", jsonResponse.isNull("TotalPrice") ? "" : jsonResponse.get("TotalPrice"));
        
    }
}
