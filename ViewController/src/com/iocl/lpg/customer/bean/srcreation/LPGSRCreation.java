package com.iocl.lpg.customer.bean.srcreation;

import com.iocl.lpg.customer.bean.loyalty.XtraRewards;
import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.util.List;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.binding.OperationBinding;

import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class LPGSRCreation {
    private static Logger log;
    private RichInputText txtComlaint;

    public LPGSRCreation() {
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(LPGSRCreation.class);

        } else {
            log = Logger.getLogger(LPGSRCreation.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }

    public void changeCategory(ValueChangeEvent valueChangeEvent) {
        log.info("inside changeDistributor"+ valueChangeEvent.getNewValue());
        String retSting = null;
        java.util.Map<String,Object> param = ADFContext.getCurrent().getPageFlowScope();
       try {
           param.put("pAllTransPahal", null);
           if(EPICConstant.DBTL_CAT.equalsIgnoreCase(String.valueOf(valueChangeEvent.getNewValue())))
           {
           
           
           OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
           java.util.List<Object> lstInput = new java.util.ArrayList<Object>();
           lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.DBTL_SERVICE));

           JSONObject jsonInput = new JSONObject();
           jsonInput.put("ConsumerId", "7000000038392344");
           
           log.info("==>" + jsonInput.toString());

           lstInput.add(1, jsonInput);

           java.util.Map<String,Object> params = ob.getParamsMap();
           params.put(EPICConstant.SERVICELIST, lstInput);
           params.put(EPICConstant.SERVICEMETHOD, EPICConstant.DBTL_SERVICE);
           ob.execute();
           if (!ob.getErrors().isEmpty()) {
               retSting = EPICConstant.ERROR;
              throw new Exception("Some Error Eccored");
           }

           List result = (List) ob.getResult();

           if (result == null) {
               retSting =EPICConstant.ERROR;
               throw new Exception("Some Error Eccored");
           }
           
           
           List<ConsumerTransaction> consumerTransaction = new java.util.ArrayList<ConsumerTransaction>();
           
               if (result.size() > 0 && String.valueOf(result.get(0)).equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                   retSting = "success";
                   JSONObject jsonObjectDBTL=(JSONObject)result.get(1);
                  JSONArray jsnDBTLArry = jsonObjectDBTL.getJSONArray("DBTLTransaction");
                  for (int i = 0; i < jsnDBTLArry.length(); ++i) {
                      JSONObject rec = jsnDBTLArry.getJSONObject(i);
                      ConsumerTransaction consumerTransn = new ConsumerTransaction();
                      consumerTransn.setSerialNumber(i + 1);
                      if (!rec.isNull("ConsumerId"))
                          consumerTransn.setConsumerId(rec.getString("ConsumerId"));
                      if (!rec.isNull("DistCode"))
                          consumerTransn.setDistCode(rec.getString("DistCode"));
                      if (!rec.isNull("BookNo"))
                          consumerTransn.setBookNo(rec.getString("BookNo"));
                      if (!rec.isNull("BookDate"))
                          consumerTransn.setBookDate(rec.getString("BookDate"));
                      if (!rec.isNull("EqCode"))
                          consumerTransn.setEqCode(rec.getString("EqCode"));
                      if (!rec.isNull("Qty"))
                          consumerTransn.setQty(rec.getString("Qty"));
                      if (!rec.isNull("CashmemoNo"))
                          consumerTransn.setCashmemoNo(rec.getString("CashmemoNo"));
                      if (!rec.isNull("CashmemoDate"))
                          consumerTransn.setCashmemoDate(rec.getString("CashmemoDate"));
                      if (!rec.isNull("DeliveredDate"))
                          consumerTransn.setDeliveredDate(rec.getString("DeliveredDate"));
                      if (!rec.isNull("SubsidyAmt"))
                          consumerTransn.setSubsidyAmt(rec.getString("SubsidyAmt"));
                      consumerTransn.setSelectedTransaction(null);
                      if (!rec.isNull("IOCBankTID"))
                          consumerTransn.setIocBankTID(rec.getString("IOCBankTID"));
                      if (!rec.isNull("BankAccountNumber"))
                          consumerTransn.setBankAccountNumber(rec.getString("BankAccountNumber"));
                      if (!rec.isNull("IFSCCode"))
                          consumerTransn.setIfscCode(rec.getString("IFSCCode"));
                      if (!rec.isNull("CTCStatus"))
                          consumerTransn.setCtcStatus(rec.getString("CTCStatus"));
                      if (!rec.isNull("BankUTR"))
                          consumerTransn.setBankUTR(rec.getString("BankUTR"));
                      if (!rec.isNull("BankIIN"))
                          consumerTransn.setBankIIN(rec.getString("BankIIN"));
                      if (!rec.isNull("BankDOS"))
                          consumerTransn.setBankDOS(rec.getString("BankDOS"));
                      if (!rec.isNull("BankTFLAG"))
                          consumerTransn.setBankTFLAG(rec.getString("BankTFLAG"));
                      if (!rec.isNull("BankFailureReason"))
                          consumerTransn.setBankFailureReason(rec.getString("BankFailureReason"));
                      if (!rec.isNull("BatchDate"))
                          consumerTransn.setBatchDate(rec.getString("BatchDate"));
                      if (!rec.isNull("ConsumerName"))
                          consumerTransn.setConsumerName(rec.getString("ConsumerName"));
                      if (!rec.isNull("BatchID"))
                          consumerTransn.setBatchID(rec.getString("BatchID"));
                      if (!rec.isNull("NoOfAttempts"))
                          consumerTransn.setNumofAttempts(rec.getString("NoOfAttempts"));
                      if (!rec.isNull("Status"))
                          consumerTransn.setStatus(rec.getString("Status"));
                      if (!rec.isNull("BranchName"))
                          consumerTransn.setBranchName(rec.getString("BranchName"));
                      if (!rec.isNull("SchemeCode"))
                          consumerTransn.setSchemecode(rec.getString("SchemeCode"));
                      if (!rec.isNull("SubsidyType"))
                          consumerTransn.setSubsidytype(rec.getString("SubsidyType"));
                      if (!rec.isNull("OrderNo"))
                          consumerTransn.setOrdernumber(rec.getString("OrderNo"));
                      if (!rec.isNull("IOCTIDNum"))
                          consumerTransn.setIOCTIDNumber(rec.getString("IOCTIDNum"));
                      if (!rec.isNull("ConsumerACNumber"))
                          consumerTransn.setConsumerAccntNumber(rec.getString("ConsumerACNumber"));
                      if (!rec.isNull("IOCBankTIDStatus"))
                          consumerTransn.setIocBankTIDStatus(rec.getString("IOCBankTIDStatus"));
                      consumerTransaction.add(consumerTransn);
                  }

                  param.put("pAllTransPahal", consumerTransaction);
                   
               } else {


                   retSting = EPICConstant.ERROR;
                   throw new Exception("Some Error Eccored");

               }
           }
           
       }catch(Exception e) {
          e.printStackTrace(); 
           retSting = EPICConstant.ERROR;
           log.error("exception occurred in changeDistributor "+e);
       }
    }

    public String createSRMethod() {
        log.info("inside createSRMethod");
        String retSting = null;
        java.util.Map<String,Object> param = ADFContext.getCurrent().getPageFlowScope();
        
        try {
          
           
           OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
           java.util.List<Object> lstInput = new java.util.ArrayList<Object>();
           lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.DBTL_SERVICE));

           JSONObject jsonInput = new JSONObject();
           jsonInput.put(EPICConstant.CONSUMERID, "7000000038392344");
           jsonInput.put(EPICConstant.CREATE_SR_INPT_PART_CD, "09999");//to be change
           jsonInput.put(EPICConstant.CREATE_SR_INPT_COMP_DTLS, txtComlaint.getValue());
           jsonInput.put(EPICConstant.CREATE_SR_INPT_FSTNAME, CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerName}"));
            jsonInput.put(EPICConstant.SOURCE, EPICConstant.SOURCE_PORTAL);
           jsonInput.put(EPICConstant.EmailAddress, CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerEmailId}"));
            jsonInput.put(EPICConstant.MOBILE_NUMBER, CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerEmailId}"));
            
            
            if (param.get("selCatagoeyVal") != null && param.get("selCatagoeyVal")
                                                            .toString()
                                                            .equalsIgnoreCase(EPICConstant.DBTL_CAT)) {
               
                JSONArray dbtlArr = new JSONArray();
             //   List<ConsumerTransaction> consumerTransaction = (List<ConsumerTransaction>) (tblTransaction.getValue());
             List<ConsumerTransaction> consumerTransaction=null;
                if(consumerTransaction!=null)
                {
                for (ConsumerTransaction obj : consumerTransaction) {
                  
                    log.info("Selected transaction==>" + obj.getSelectedTransaction());
                    if(obj.getSelectedTransaction() != null) {
                        log.info("Selected transaction==>" + obj.getSelectedTransaction().toString());
                    }
                    if (obj.getSelectedTransaction() != null && obj.getSelectedTransaction().toString().equalsIgnoreCase("true")) {
            
                        JSONObject jsonInputDbtl = new JSONObject();
                                   /*            jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_CYLDR_QTY, obj.getQty());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_UTRNO, obj.getBankUTR());
                                              
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_CTC_ST, obj.getCtcStatus());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_CONS_AC_NUM, obj.getConsumerAccntNumber());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_CASH_MEM_DT, obj.getCashmemoDate());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_IOCTIDNUM, obj.getIocBankTID());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_ORD_NO, obj.getBookNo());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_DELVR_DT, obj.getDeliveredDate());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_BANKINN, obj.getBankIIN());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_SCH_CD, obj.getEqCode());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_STATUS, obj.getIocBankTIDStatus()); 
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_SUBSD_AMT, obj.getSubsidyAmt());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_NO_ATTEMP, obj.getNumofAttempts());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_BATCHID, obj.getBatchID());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_CASH_MEM_NUM, obj.getCashmemoNo());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_BANK_DOS, obj.getBankDOS());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_ORD_DT, obj.getBookDate());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_INPT_RSN, obj.getBankFailureReason());
                                               jsonInputDbtl.put(EPICConstant.CREATE_SR_DBTL_DISTRIBUTORCODE, obj.getDistCode());
                                              jsonInputDbtl.put(EPICConstant.CREATE_SR_DBTL_BNKTFLAG, obj.getBankTFLAG());
                                              jsonInputDbtl.put(EPICConstant.CREATE_SR_DBTL_BANK_AC_NO, obj.getBankAccountNumber());
                                              jsonInputDbtl.put(EPICConstant.CREATE_SR_DBTL_IFSCCOD, obj.getIfscCode());*/
                                           
                                               
                                               dbtlArr.put(jsonInputDbtl);
                        

                    }
                }
                }
                if(dbtlArr.length()>0)
                jsonInput.put("DBTLTransactions", dbtlArr);
            }
            
           
           log.info("==>" + jsonInput.toString());

           lstInput.add(1, jsonInput);

           java.util.Map<String,Object> params = ob.getParamsMap();
           params.put(EPICConstant.SERVICELIST, lstInput);
           params.put(EPICConstant.SERVICEMETHOD, EPICConstant.DBTL_SERVICE);
           ob.execute();
           if (!ob.getErrors().isEmpty()) {
               retSting = EPICConstant.ERROR;
              return retSting;
           }

           List result = (List) ob.getResult();

           if (result == null) {
               retSting =EPICConstant.ERROR;
               return retSting;
           }
           
           
           List<ConsumerTransaction> consumerTransaction = new java.util.ArrayList<ConsumerTransaction>();
           
               if (result.size() > 0 && String.valueOf(result.get(0)).equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                   retSting = "success";
                   JSONObject jsonObjectDBTL=(JSONObject)result.get(1);
                  JSONArray jsnDBTLArry = jsonObjectDBTL.getJSONArray("DBTLTransaction");
                  for (int i = 0; i < jsnDBTLArry.length(); ++i) {
                      JSONObject rec = jsnDBTLArry.getJSONObject(i);
                      ConsumerTransaction consumerTransn = new ConsumerTransaction();
                      consumerTransn.setSerialNumber(i + 1);
                      if (!rec.isNull("ConsumerId"))
                          consumerTransn.setConsumerId(rec.getString("ConsumerId"));
                      if (!rec.isNull("DistCode"))
                          consumerTransn.setDistCode(rec.getString("DistCode"));
                      if (!rec.isNull("BookNo"))
                          consumerTransn.setBookNo(rec.getString("BookNo"));
                      if (!rec.isNull("BookDate"))
                          consumerTransn.setBookDate(rec.getString("BookDate"));
                      if (!rec.isNull("EqCode"))
                          consumerTransn.setEqCode(rec.getString("EqCode"));
                      if (!rec.isNull("Qty"))
                          consumerTransn.setQty(rec.getString("Qty"));
                      if (!rec.isNull("CashmemoNo"))
                          consumerTransn.setCashmemoNo(rec.getString("CashmemoNo"));
                      if (!rec.isNull("CashmemoDate"))
                          consumerTransn.setCashmemoDate(rec.getString("CashmemoDate"));
                      if (!rec.isNull("DeliveredDate"))
                          consumerTransn.setDeliveredDate(rec.getString("DeliveredDate"));
                      if (!rec.isNull("SubsidyAmt"))
                          consumerTransn.setSubsidyAmt(rec.getString("SubsidyAmt"));
                      consumerTransn.setSelectedTransaction(null);
                      if (!rec.isNull("IOCBankTID"))
                          consumerTransn.setIocBankTID(rec.getString("IOCBankTID"));
                      if (!rec.isNull("BankAccountNumber"))
                          consumerTransn.setBankAccountNumber(rec.getString("BankAccountNumber"));
                      if (!rec.isNull("IFSCCode"))
                          consumerTransn.setIfscCode(rec.getString("IFSCCode"));
                      if (!rec.isNull("CTCStatus"))
                          consumerTransn.setCtcStatus(rec.getString("CTCStatus"));
                      if (!rec.isNull("BankUTR"))
                          consumerTransn.setBankUTR(rec.getString("BankUTR"));
                      if (!rec.isNull("BankIIN"))
                          consumerTransn.setBankIIN(rec.getString("BankIIN"));
                      if (!rec.isNull("BankDOS"))
                          consumerTransn.setBankDOS(rec.getString("BankDOS"));
                      if (!rec.isNull("BankTFLAG"))
                          consumerTransn.setBankTFLAG(rec.getString("BankTFLAG"));
                      if (!rec.isNull("BankFailureReason"))
                          consumerTransn.setBankFailureReason(rec.getString("BankFailureReason"));
                      if (!rec.isNull("BatchDate"))
                          consumerTransn.setBatchDate(rec.getString("BatchDate"));
                      if (!rec.isNull("ConsumerName"))
                          consumerTransn.setConsumerName(rec.getString("ConsumerName"));
                      if (!rec.isNull("BatchID"))
                          consumerTransn.setBatchID(rec.getString("BatchID"));
                      if (!rec.isNull("NoOfAttempts"))
                          consumerTransn.setNumofAttempts(rec.getString("NoOfAttempts"));
                      if (!rec.isNull("Status"))
                          consumerTransn.setStatus(rec.getString("Status"));
                      if (!rec.isNull("BranchName"))
                          consumerTransn.setBranchName(rec.getString("BranchName"));
                      if (!rec.isNull("SchemeCode"))
                          consumerTransn.setSchemecode(rec.getString("SchemeCode"));
                      if (!rec.isNull("SubsidyType"))
                          consumerTransn.setSubsidytype(rec.getString("SubsidyType"));
                      if (!rec.isNull("OrderNo"))
                          consumerTransn.setOrdernumber(rec.getString("OrderNo"));
                      if (!rec.isNull("IOCTIDNum"))
                          consumerTransn.setIOCTIDNumber(rec.getString("IOCTIDNum"));
                      if (!rec.isNull("ConsumerACNumber"))
                          consumerTransn.setConsumerAccntNumber(rec.getString("ConsumerACNumber"));
                      if (!rec.isNull("IOCBankTIDStatus"))
                          consumerTransn.setIocBankTIDStatus(rec.getString("IOCBankTIDStatus"));
                      consumerTransaction.add(consumerTransn);
                  }

                  param.put("pAllTransPahal", consumerTransaction);
                   
               } else {


                   retSting = EPICConstant.ERROR;
                   return retSting;

               }
           
           
        }catch(Exception e) {
          e.printStackTrace(); 
           retSting = EPICConstant.ERROR;
           log.error("exception occurred in changeDistributor "+e);
        }
        return retSting;
    }

    public String cancelSR() {
        // Add event code here...
        return null;
    }

    public void setTxtComlaint(RichInputText txtComlaint) {
        this.txtComlaint = txtComlaint;
    }

    public RichInputText getTxtComlaint() {
        return txtComlaint;
    }
}
