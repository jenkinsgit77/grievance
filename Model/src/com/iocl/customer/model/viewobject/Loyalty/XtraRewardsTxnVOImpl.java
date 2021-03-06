package com.iocl.customer.model.viewobject.Loyalty;

import com.iocl.customer.model.utils.EPICIOCLResourceModel;

import java.sql.ResultSet;

import oracle.jbo.Row;
import oracle.jbo.server.ViewObjectImpl;
import oracle.jbo.server.ViewRowImpl;
import oracle.jbo.server.ViewRowSetImpl;

import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;
import com.iocl.customer.model.utils.ModelUtils;

import oracle.jbo.ViewObject;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Dec 10 11:49:15 IST 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class XtraRewardsTxnVOImpl extends ViewObjectImpl {
    
    private static Logger log ;
    /**
     * This is the default constructor (do not remove).
     */
    public XtraRewardsTxnVOImpl() {
        String logFlag = EPICIOCLResourceModel.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(XtraRewardsTxnVOImpl.class);

        } else {
            log = Logger.getLogger(XtraRewardsTxnVOImpl.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }

    /**
     * executeQueryForCollection - for custom java data source support.
     */
    @Override
    protected void executeQueryForCollection(Object qc, Object[] params, int noUserParams) {
        super.executeQueryForCollection(qc, params, noUserParams);
    }

    /**
     * hasNextForCollection - for custom java data source support.
     */
    @Override
    protected boolean hasNextForCollection(Object qc) {
        boolean bRet = super.hasNextForCollection(qc);
        return bRet;
    }

    /**
     * createRowFromResultSet - for custom java data source support.
     */
    @Override
    protected ViewRowImpl createRowFromResultSet(Object qc, ResultSet resultSet) {
        ViewRowImpl value = super.createRowFromResultSet(qc, resultSet);
        return value;
    }

    /**
     * getQueryHitCount - for custom java data source support.
     */
    @Override
    public long getQueryHitCount(ViewRowSetImpl viewRowSet) {
        long value = super.getQueryHitCount(viewRowSet);
        return value;
    }

    /**
     * getCappedQueryHitCount - for custom java data source support.
     */
    @Override
    public long getCappedQueryHitCount(ViewRowSetImpl viewRowSet, Row[] masterRows, long oldCap, long cap) {
        long value = super.getCappedQueryHitCount(viewRowSet, masterRows, oldCap, cap);
        return value;
    }
    
    /**
     * getXRCustomerTxns
     * @param xrInputTxnJson
     */
    public void getXRCustomerTxns(JSONObject xrInputTxnJson, String Txntype) {
        this.getViewObject().executeEmptyRowSet();
        this.getViewObject().clearCache();
        this.getViewObject().setQueryMode(ViewObject.QUERY_MODE_SCAN_VIEW_ROWS);
        this.getViewObject().executeQuery();
        try{
            log.info("return input json obj for transactions for txn type "+ Txntype+":::" + xrInputTxnJson);
            Row row1 = null;
            JSONObject jsonObjectXRTxnDet = xrInputTxnJson.getJSONObject("ResponseData");
            JSONArray  arrObj = jsonObjectXRTxnDet.getJSONArray("TransactionDetails");
            for (int i = 1; i <= arrObj.length(); i++) {
                int k=i-1;
                JSONObject jsonObjectXRTxnDetRow = arrObj.getJSONObject(k);
//            int cnt=0;
//            if(Txntype.equalsIgnoreCase("TXN")){
//                cnt=3;
//            }else{
//                cnt=2;
//            }
//                for (int i = 1; i <= cnt; i++) {
//                    int k=i-1;
//                    JSONObject jsonObjectXRTxnDetRow = arrObj.getJSONObject(0);
                if(!String.valueOf(jsonObjectXRTxnDetRow.get("TID")).equalsIgnoreCase("null")){
                    row1 = this.createRow();
                    row1.setAttribute("MerchantId",  String.valueOf(jsonObjectXRTxnDetRow.get("MID")).equalsIgnoreCase("null")
                                                        ?"NA":String.valueOf(jsonObjectXRTxnDetRow.get("MID")));
                    row1.setAttribute("MerchantLocation",  String.valueOf(jsonObjectXRTxnDetRow.get("MerchantLocation")).equalsIgnoreCase("null")
                                                        ?"NA":String.valueOf(jsonObjectXRTxnDetRow.get("MerchantLocation")));
                    row1.setAttribute("TxnId",  String.valueOf(jsonObjectXRTxnDetRow.get("TID")).equalsIgnoreCase("null")
                                                        ?"NA":String.valueOf(jsonObjectXRTxnDetRow.get("TID")));
                    row1.setAttribute("TxnAmt",  String.valueOf(jsonObjectXRTxnDetRow.get("TransactionAmount")).equalsIgnoreCase("null")
                                                        ?"NA":String.valueOf(jsonObjectXRTxnDetRow.get("TransactionAmount")));
                    row1.setAttribute("TxnProductName",  String.valueOf(jsonObjectXRTxnDetRow.get("ProductName")).equalsIgnoreCase("null")
                                                        ?"NA":String.valueOf(jsonObjectXRTxnDetRow.get("ProductName")));
                    row1.setAttribute("MerchantName",  String.valueOf(jsonObjectXRTxnDetRow.get("MerchantName")).equalsIgnoreCase("null")
                                                        ?"NA":String.valueOf(jsonObjectXRTxnDetRow.get("MerchantName")));
                    row1.setAttribute("TxnType",  Txntype);
                    if(Txntype.equalsIgnoreCase("TXN")){
                        row1.setAttribute("TxnPointsEarned",  String.valueOf(jsonObjectXRTxnDetRow.get("Points")).equalsIgnoreCase("null")
                                                            ?"NA":String.valueOf(jsonObjectXRTxnDetRow.get("Points")));
                        row1.setAttribute("TxnPointsReedeem", "0");
                    }else{
                        row1.setAttribute("TxnPointsReedeem",  String.valueOf(jsonObjectXRTxnDetRow.get("Points")).equalsIgnoreCase("null")
                                                            ?"NA":String.valueOf(jsonObjectXRTxnDetRow.get("Points")));
                        row1.setAttribute("TxnPointsEarned",  "0");
                    }
                    row1.setAttribute("TxnPts",  "500");
                    row1.setAttribute("TxnDateTime",  String.valueOf(jsonObjectXRTxnDetRow.get("TransactionDateTime")).equalsIgnoreCase("null")
                                                        ?"NA":ModelUtils.changeDateParse(String.valueOf(jsonObjectXRTxnDetRow.get("TransactionDateTime"))));
    //                row1.setAttribute("TxnTime",  String.valueOf(jsonObjectXRTxnDetRow.get("TransactionDateTime")).equalsIgnoreCase("null")
    //                                                    ?"NA":String.valueOf(jsonObjectXRTxnDetRow.get("TransactionDateTime")));
    //                row1.setAttribute("TxnYear",  String.valueOf(jsonObjectXRTxnDetRow.get("TransactionDateTime")).equalsIgnoreCase("null")
    //                                                    ?"NA":String.valueOf(jsonObjectXRTxnDetRow.get("TransactionDateTime")));
                    
    //                row1.setAttribute("TxnDateTime",  "03 Oct");
    //                row1.setAttribute("TxnTime",  "07:4"+k);
    //                row1.setAttribute("TxnYear",  "2018");
                    this.insertRow(row1);
                }
            }
        }
        catch(Exception exc){
                    log.info("error in getXRCustomerTxns");
                    exc.printStackTrace();
                }
    }
}

