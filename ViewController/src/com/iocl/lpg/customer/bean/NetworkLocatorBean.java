package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.PopupCanceledEvent;
import oracle.adf.view.rich.render.ClientEvent;
import oracle.binding.OperationBinding;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

import java.util.HashMap;

import javax.faces.model.SelectItem;

import oracle.adf.share.ADFContext;
import oracle.adf.view.faces.bi.component.gauge.UIRatingGauge;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.component.rich.output.RichOutputFormatted;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class NetworkLocatorBean implements Serializable {

    private RichInputText fetchZipInputBinding;


    private String latitudeBean;
    private String longitudeBean;
    private String nodataDistMaster;
    private String nodataAutoLPG;
    private RichInputText latitudeBinding;
    private RichInputText longitudeBinding;
    private RichInputText searchLocationBinding;

    @SuppressWarnings("compatibility:1699335356781066117")

    private static Logger logger;
    private RichPopup feedbackPopupBinding;
    private RichSelectOneChoice categoryTypeBinding;
    private RichOutputFormatted networklocatorbeanBinding;
    private RichOutputFormatted categoryTypeErrMsgBinding;

    public NetworkLocatorBean() {
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            logger = Logger.getLogger(NetworkLocatorBean.class);

        } else {

            logger = Logger.getLogger(NetworkLocatorBean.class);

            Logger.getRootLogger().setLevel(org.apache
                                               .log4j
                                               .Level
                                               .OFF);

        }
    }


    public void setNodataDistMaster(String nodataDistMaster) {
        this.nodataDistMaster = nodataDistMaster;
    }

    public String getNodataDistMaster() {
        return nodataDistMaster;
    }

    public void setNodataAutoLPG(String nodataAutoLPG) {
        this.nodataAutoLPG = nodataAutoLPG;
    }

    public String getNodataAutoLPG() {
        return nodataAutoLPG;
    }


    public String getMapKey() {
        return EPICIOCLResourceCustBundle.findKeyValue("G_MAP_KEY");
    }

    private static final long serialVersionUID = 1L;
    private RichTable mapTableBinding;
    private RichOutputText jsonValueBinding;
    private RichOutputText fetchZipcodeBinding;
    private String zipcodeBean;

    public void setZipcodeBean(String zipcodeBean) {
        this.zipcodeBean = zipcodeBean;
    }

    public String getZipcodeBean() {
        return zipcodeBean;
    }

    public void setAutoLPG(boolean autoLPG) {
        this.autoLPG = autoLPG;
    }

    public boolean isAutoLPG() {
        return autoLPG;
    }

    public void setDistributor(boolean distributor) {
        this.distributor = distributor;
    }

    public boolean isDistributor() {
        return distributor;
    }

    public void setFtl(boolean ftl) {
        this.ftl = ftl;
    }

    public boolean isFtl() {
        return ftl;
    }

    public void setAreaOffice(boolean areaOffice) {
        this.areaOffice = areaOffice;
    }

    public boolean isAreaOffice() {
        return areaOffice;
    }
    private boolean autoLPG;
    private boolean distributor;
    private boolean ftl;
    private boolean areaOffice;
    private boolean retailOutlet;


    public void setMapTableBinding(RichTable mapTableBinding) {
        this.mapTableBinding = mapTableBinding;
    }

    public RichTable getMapTableBinding() {
        return mapTableBinding;
    }

    private String jsonStringLocations;


    private java.util.List locationList;

    public void setLocationList(List locationList) {
        this.locationList = locationList;
    }

    public List getLocationList() {
        return locationList;
    }

    private JSONObject jsonLocationsInput;

    public void setJsonLocationsInput(JSONObject jsonLocationsInput) {
        this.jsonLocationsInput = jsonLocationsInput;
    }

    public JSONObject getJsonLocationsInput() {
        return jsonLocationsInput;
    }

    public void setLocationJsonArray(JSONArray locationJsonArray) {
        this.locationJsonArray = locationJsonArray;
    }

    public JSONArray getLocationJsonArray() {
        return locationJsonArray;
    }

    private JSONArray locationJsonArray;
    private JSONArray locationJsonArrayFinal;
    private List<SelectItem> categoryList;
    
    public String getSiteKey() {
        return EPICIOCLResourceCustBundle.findKeyValue("G_CAPACHA_SITE_KEY");
    }


    public void initializerTaskFlow() {
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        String categoryValPF = null;
        String categoryTypePF = null;
        if (param.get("CategoryType")!=null) {
            categoryTypePF = param.get("CategoryType").toString();
        }
        System.out.println("categoryTypePF "+categoryTypePF);        
        DCBindingContainer bindings = null;
        DCIteratorBinding dbcIterator = null;
        ViewObject categoryVo=null;
        bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        dbcIterator = bindings.findIteratorBinding("LocationCategoryVo1Iterator");
        categoryVo = dbcIterator.getViewObject();
        dbcIterator.executeQuery();
        String filterCategory="CATEGORYTYPE='"+categoryTypePF+"'";
        categoryVo.setWhereClause(filterCategory);
        categoryVo.executeQuery();
        Row[] rr = categoryVo.getAllRowsInRange();
        System.out.println("rr.length "+rr.length);
        HashMap<String,String> categoryMap = new HashMap<String,String>();
        if(rr.length>0){
            categoryList = new ArrayList<SelectItem>();
            String categoryValue=null;
            for(int i=0;i<rr.length;i++){
                categoryValue = rr[i].getAttribute("Categoryname") == null ? "" : rr[i].getAttribute("Categoryname").toString();
                categoryMap.put(categoryValue,categoryValue);
            }
            categoryList.add(new SelectItem(categoryValue,categoryValue));
        }
        
        param.put("CategoryList",categoryMap);
        OperationBinding op = CommonHelper.findOperation("initLpgDistVo");
        op.getParamsMap().put("ftlFlag", categoryTypePF);
        op.getParamsMap().put("zipcodeFilter", null);
        op.execute();
        
        if(categoryTypePF.equalsIgnoreCase("IO")){
            if (param.get("CategoryVal") != null) {
                categoryValPF = param.get("CategoryVal").toString();            
                if (categoryValPF.equalsIgnoreCase("LPG Area Office")) {
                    param.put("LobValue", "Distributor");
                    onLoadAreaOffice();
                }
                if(categoryValPF.equalsIgnoreCase("Retail Divisional Office")){
                    param.put("LobValue", "Dealer");
                    onLoadAreaOffice();
                }
                if(categoryValPF.equalsIgnoreCase("Lubes State Office")){
                    param.put("LobValue", "Stockist");
                    onLoadAreaOffice();
                }
            }
            
        }else if(categoryTypePF.equalsIgnoreCase("IP")){
            if (param.get("CategoryVal") != null) {
                categoryValPF = param.get("CategoryVal").toString();            
                if (categoryValPF.equalsIgnoreCase("Distributors")) {
                    param.put("LobValue", "Distributor");
                    onLoadDistributor();
                }
                if (categoryValPF.equalsIgnoreCase("Fuel Stations")) {
                        onLoadRetailOutlet();
                }
                if(categoryValPF.equalsIgnoreCase("Stockist")){
                    param.put("LobValue", "Stockist");
                    onLoadDistributor();
                }
            }
        }else{
            if(param.get("CategoryVal") != null){
                categoryValPF = param.get("CategoryVal").toString();
                logger.info("categoryValPF "+categoryValPF);
                if (categoryValPF.equalsIgnoreCase("Distributor selling 5 KG FTL cylinders")) {
                    onLoadFtl();
                }

                if (categoryValPF.equalsIgnoreCase("Auto LPG Outlet")) {
                    onLoadAutoLPGOutlet();
                }
            }
            
        }
        
 
 /*
  * previous code
  */
        /**
        if (param.get("CategoryVal") != null) {
            categoryValPF = param.get("CategoryVal").toString();            
            if (categoryValPF.equalsIgnoreCase("LPG Area Office")) {
                onLoadAreaOffice();
            }
            if (categoryValPF.equalsIgnoreCase("LPG distributors")) {

                onLoadDistributor();
            }

            if (categoryValPF.equalsIgnoreCase("Distributor selling 5 KG FTL cylinders")) {
                onLoadFtl();
            }

            if (categoryValPF.equalsIgnoreCase("Auto LPG Outlet")) {
                onLoadAutoLPGOutlet();
            }

            if (categoryValPF.equalsIgnoreCase("Fuel Stations")) {
                onLoadRetailOutlet();
            }
        } else {

        }
**/


    }


    public void getDistributorDetails() {
        DCBindingContainer bindings = null;
        DCIteratorBinding dbcIterator = null;
        ViewObject vo = null;
        String whereClauseString = null;
    }

    public void getFTLDetails() {
        DCBindingContainer bindings = null;
        DCIteratorBinding dbcIterator = null;
        ViewObject vo = null;
        String whereClauseString = null;
    }

    public void getAutoLPGDetails() {
        DCBindingContainer bindings = null;
        DCIteratorBinding dbcIterator = null;
        ViewObject vo = null;
        String whereClauseString = null;
    }


/*
    public String getAOLocationDetails(String aoType) {
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        ArrayList locations = new ArrayList();
        locationList = new java.util.ArrayList();
        logger.info("zipcodeSearch getLocationDetails " + "");
        logger.info("zipcodeBean getLocationDetails " + "");

        java.util.List lstInput = new java.util.ArrayList();
        JSONObject jsonInput = new JSONObject();


        OperationBinding op = CommonHelper.findOperation("serviceCustomerCall");
        op.getParamsMap().put("inputList", getListForServiceCall(aoType));
        op.getParamsMap().put("method", EPICConstant.AO_LOCATOR);
        op.execute();

        java.util.List returnList = (java.util.List) op.getResult();
        //            log.info("returnList.get(0) " + returnList.get(0));
        if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0)
                                                                             .toString()
                                                                             .equalsIgnoreCase(EPICConstant.TRUE_VAl)) {

        }

        bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        locationListIterator = bindings.findIteratorBinding("AOLocatorVO1Iterator");

        vo = locationListIterator.getViewObject();
        //            vo.setWhereClause(whereClauseString);
        //            locationListIterator.executeQuery();
        logger.info("Rowcount : " + vo.getRowCount());
        if (vo.getRowCount() < 1) {
            AdfFacesContext.getCurrentInstance()
                           .getPageFlowScope()
                           .put("NoDataMsg", "No Data to Display");
            jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
        } else {
            AdfFacesContext.getCurrentInstance()
                           .getPageFlowScope()
                           .put("NoDataMsg", null);
            Row[] rr = vo.getAllRowsInRange();
            logger.info("rr.length " + rr.length);
            if (rr.length > 0) {
                for (int i = 0; i < rr.length; i++) {
                    JSONObject jsonLocationsRowInput = new JSONObject();
                    logger.info(rr[i]);
                    locationList.add(rr[i]);
                    //                            System.out.println("ROCode" + rr[i].getAttribute("ROCode").toString());
                    logger.info("ROName" + rr[i].getAttribute("ROName").toString());
                    
                    logger.info("Sno" + i);
//                    String roServices = rr[i].getAttribute("ROServices")
//                                             .toString()
//                                             .replaceAll(",", ", ");
//                    logger.info("roServices " + roServices);
                    jsonLocationsRowInput.put("Sno", String.valueOf(i));
                    jsonLocationsRowInput.put("NetworkType", AdfFacesContext.getCurrentInstance()
                                                                            .getPageFlowScope()
                                                                            .get("NetworkFlag"));
                    jsonLocationsRowInput.put("PhoneNo", rr[i].getAttribute("PhoneNo").toString());
                    jsonLocationsRowInput.put("Latitude", rr[i].getAttribute("Latitude").toString());
                    jsonLocationsRowInput.put("Longitude", rr[i].getAttribute("Longitude").toString());
                    jsonLocationsRowInput.put("Email", rr[i].getAttribute("Email"));
                    jsonLocationsRowInput.put("Address", rr[i].getAttribute("Address"));
                    jsonLocationsRowInput.put("OfficeName", rr[i].getAttribute("OfficeName"));
                    jsonLocationsRowInput.put("DealerName", rr[i].getAttribute("DealerName"));//Zipcode
                    double distance =
                        CommonHelper.distanceBwLatLon(AdfFacesContext.getCurrentInstance().getPageFlowScope().get("LatitudeVal").toString(),
                                                      rr[i].getAttribute("Latitude").toString(),
                                                      AdfFacesContext.getCurrentInstance().getPageFlowScope().get("LongitudeVal").toString(),
                  rr[i].getAttribute("Longitude").toString());
                    jsonLocationsRowInput.put("Distance", distance);
                    locationJsonArray.put(jsonLocationsRowInput);
                }
                jsonLocationsInput.put("LocationDetails", CommonHelper.sortedJsonArray(locationJsonArray));

                CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);

                getListForInsertIntoROLocator(CommonHelper.sortedJsonArray(locationJsonArray));

                logger.info("jsonLocationsInput " + jsonLocationsInput);
            } else {
                jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
                CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);
            }
        }
        return locations;


    }  
**/
                                                       

    public void getAOLocationDetails() {
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        locationList = new java.util.ArrayList();
        
        DCIteratorBinding dbcIterator = null;
        DCIteratorBinding dbcIterator1 = null;
        ViewObject vo = null;
        ViewObject vo1 = null;
        String whereClauseString = null;
        DCBindingContainer bindings = null;
        bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        dbcIterator1 = bindings.findIteratorBinding("LPGAreaOfficeVO1Iterator");
        vo1 = dbcIterator1.getViewObject();
        dbcIterator1.executeQuery();
        String whereClause = "Zipcode in (";
        String zipcodeVal = "";
        Row[] rr1 = vo1.getAllRowsInRange();

        if (rr1.length > 0) {
            for (int j = 0; j < rr1.length; j++) {
                if (rr1[j].getAttribute("Zipcode") != null) {
                    zipcodeVal = zipcodeVal + rr1[j].getAttribute("Zipcode").toString() + ",";
                }
            }
            logger.info("zipcodeVal " + zipcodeVal);

            int index = zipcodeVal.lastIndexOf(",");
            logger.info(" 1---" + zipcodeVal.substring(0, index));
            logger.info("2---" + zipcodeVal.substring(index + 1));
            zipcodeVal = zipcodeVal.substring(0, index);
            logger.info("zipcodeVal " + zipcodeVal);
            whereClause = whereClause + zipcodeVal + ")";

        } else {
            whereClause = "Zipcode =-1";
        }

        logger.info("whereClause" + whereClause);
        dbcIterator = bindings.findIteratorBinding("LPGAreaOfficeMasterVO1Iterator");
        vo = dbcIterator.getViewObject();
        vo.setWhereClause(whereClause);
        dbcIterator.executeQuery();
        logger.info("Rowcount : " + vo.getRowCount());
        if (vo.getRowCount() < 1) {
            param.put("NoDataMsg", "No Data to Display");
            jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
        } else {
            param.put("NoDataMsg", null);
        }
        Row[] rr = vo.getAllRowsInRange();
        logger.info("rr.length " + rr.length);
        if (rr.length > 0) {
            for (int i = 0; i < rr.length; i++) {
                JSONObject jsonLocationsRowInput = new JSONObject();
                logger.info(rr[i]);
                locationList.add(rr[i]);
                jsonLocationsRowInput.put("Sno", String.valueOf(i));
                jsonLocationsRowInput.put("NetworkType", param.get("NetworkFlag"));
                jsonLocationsRowInput.put("Distributorname", rr[i].getAttribute("AreaOfficeName").toString());
                jsonLocationsRowInput.put("Address", rr[i].getAttribute("AreaOfficeAddress").toString());
                jsonLocationsRowInput.put("StateName", rr[i].getAttribute("StateOfficeName").toString());
                jsonLocationsRowInput.put("Latitude", rr[i].getAttribute("Latitude"));
                jsonLocationsRowInput.put("Longitude", rr[i].getAttribute("Longitude")); //Zipcode
                jsonLocationsRowInput.put("IvrsNum", "NA");
                jsonLocationsRowInput.put("MainPhNum", "NA");
                jsonLocationsRowInput.put("MainEmailAddr", "NA");
                jsonLocationsRowInput.put("Zipcode", "NA");

                logger.info("DistributorName" + rr[i].getAttribute("AreaOfficeName").toString());
                logger.info("Addr" + rr[i].getAttribute("AreaOfficeAddress").toString());
                logger.info("StateName" + rr[i].getAttribute("StateOfficeName").toString());
                logger.info("Longitude" + rr[i].getAttribute("Latitude"));
                logger.info("Latitude" + rr[i].getAttribute("Longitude"));
                logger.info("Sno" + i);
                logger.info("jsonLocationsRowInput" + jsonLocationsRowInput);
                locationJsonArray.put(jsonLocationsRowInput);
            }
            jsonLocationsInput.put("LocationDetails", locationJsonArray);
            CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);
            logger.info("jsonLocationsInput " + jsonLocationsInput);
            logger.info("jsonStringLocations  " + jsonStringLocations);
        } else {
            jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
            CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);
        }
    }
    
    
    public java.util.List getListForServiceCallAO(String aoType) {

        String vretString = null;
        JSONObject jsonInput = new JSONObject();
        if (param.get("LobValue")!=null) {
            aoType = param.get("LobValue").toString();
        }
        java.util.List lst = new java.util.ArrayList();
        logger.info("url  " + EPICIOCLResourceCustBundle.findKeyValue("AO_LOCATOR"));
        logger.info("url  " + EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.AO_LOCATOR));
        String trackingId = CommonHelper.createUniqueID();
        logger.info(" " + param.get("LatitudeVal"));
        logger.info(" " + param.get("LongitudeVal"));
        
//        jsonInput.put(EPICConstant.LATITUDENEW, "28.592626"); //comment this before Deployment
//        jsonInput.put(EPICConstant.LONGITUDENEW,"77.3865103"); //comment this before Deployment


        jsonInput.put(EPICConstant.LATITUDENEW, param.get("LatitudeVal")); //Comment this to run Local Only
        jsonInput.put(EPICConstant.LONGITUDENEW, param.get("LongitudeVal"));  //Comment this to run Local Only
        
        
        jsonInput.put(EPICConstant.RADIUSNEW, "500");
        jsonInput.put(EPICConstant.SESSION_PARTNER_LOB,aoType);
        lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("AO_LOCATOR"));
        lst.add(1, jsonInput);

        return lst;
    }
    
    public java.util.List getListForServiceCallPartner(String aoType) {

        String vretString = null;
        JSONObject jsonInput = new JSONObject();
        if (param.get("LobValue")!=null) {
            aoType = param.get("LobValue").toString();
        }
        java.util.List lst = new java.util.ArrayList();
        logger.info("url  " + EPICIOCLResourceCustBundle.findKeyValue("PARTNER_LOCATOR"));
        logger.info("url  " + EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.AO_LOCATOR));
        String trackingId = CommonHelper.createUniqueID();
        logger.info(" " + param.get("LatitudeVal"));
        logger.info(" " + param.get("LongitudeVal"));
        
    //        jsonInput.put(EPICConstant.LATITUDENEW, "28.592626"); //comment this before Deployment
    //        jsonInput.put(EPICConstant.LONGITUDENEW,"77.3865103"); //comment this before Deployment


        jsonInput.put(EPICConstant.LATITUDENEW, param.get("LatitudeVal")); //Comment this to run Local Only
        jsonInput.put(EPICConstant.LONGITUDENEW, param.get("LongitudeVal"));  //Comment this to run Local Only
        
        
        jsonInput.put(EPICConstant.RADIUSNEW, "500");
        jsonInput.put(EPICConstant.FILTER_TYPE,aoType);
        lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("PARTNER_LOCATOR"));
        lst.add(1, jsonInput);

        return lst;
    }

    public java.util.List getListForServiceCall() {

        String vretString = null;
        JSONObject jsonInput = new JSONObject();
        java.util.List lst = new java.util.ArrayList();
        logger.info("url  " + EPICIOCLResourceCustBundle.findKeyValue("RO_LOCATOR"));
        logger.info("url  " + EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.RO_LOCATOR));
        String trackingId = CommonHelper.createUniqueID();
        logger.info(" " + param.get("LatitudeVal"));
        logger.info(" " + param.get("LongitudeVal"));

        jsonInput.put(EPICConstant.LATITUDE, param.get("LatitudeVal")); //CYLINDER_SIZE
        jsonInput.put(EPICConstant.LONGITUDE, param.get("LongitudeVal"));
        jsonInput.put(EPICConstant.RADIUS, "5");
        lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("RO_LOCATOR"));
        lst.add(1, jsonInput);
        return lst;
    }

    public ArrayList getLocationDetails(String zipcodeSearch) {
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        locationJsonArrayFinal=new JSONArray();
        ArrayList locations = new ArrayList();
        locationList = new java.util.ArrayList();
        logger.info("zipcodeSearch getLocationDetails " + zipcodeSearch);
        logger.info("zipcodeBean getLocationDetails " + zipcodeBean);
        DCBindingContainer bindings = null;
        DCIteratorBinding locationListIterator = null;
        ViewObject vo = null;
        String whereClauseString = null;


        if (param.get("NetworkFlag")
                           .toString()
                           .equalsIgnoreCase("Ftl")) {
            bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            locationListIterator = bindings.findIteratorBinding("LpgDistMaster1Iterator1");
            whereClauseString = "DIST_5KG_FTL='Y'";
            vo = locationListIterator.getViewObject();
            vo.setWhereClause(whereClauseString);
            locationListIterator.executeQuery();
            logger.info("Rowcount : " + vo.getRowCount());
            if (vo.getRowCount() < 1) {
                param.put("NoDataMsg", "No Data to Display");
                jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
            } else {
                param.put("NoDataMsg", null);
            }
        }
        if (param.get("NetworkFlag")
                           .toString()
                           .equalsIgnoreCase("AutoLPG")) {
            logger.info("AutolpgDistMasterVo1Iterator1");
            bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            locationListIterator = bindings.findIteratorBinding("AutolpgDistMasterVo1Iterator1");
            vo = locationListIterator.getViewObject();
            vo.setWhereClause(whereClauseString);
            locationListIterator.executeQuery();
            logger.info("Rowcount : " + vo.getRowCount());
            if (vo.getRowCount() < 1) {
                param.put("NoDataMsg", "No Data to Display");
                jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
            } else {
                param.put("NoDataMsg", null);
            }

        }
        if (param.get("NetworkFlag")
                           .toString()
                           .equalsIgnoreCase("Distributor")) {
            String partnerType=null;
            if (param.get("LobValue")!=null) {
                partnerType = param.get("LobValue").toString();
            }
            java.util.List lstInput = new java.util.ArrayList();
            JSONObject jsonInput = new JSONObject();


            OperationBinding op = CommonHelper.findOperation("serviceCustomerCall");
            op.getParamsMap().put("inputList", getListForServiceCallPartner(partnerType));
            op.getParamsMap().put("method", EPICConstant.PARTNER_LOCATOR);
            op.execute();

            java.util.List returnList = (java.util.List) op.getResult();
            //            log.info("returnList.get(0) " + returnList.get(0));
            if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0)
                                                                                 .toString()
                                                                                 .equalsIgnoreCase(EPICConstant.TRUE_VAl)) {

            }
            bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            locationListIterator = bindings.findIteratorBinding("PartnerLocatorVO1Iterator");
            vo = locationListIterator.getViewObject();
            logger.info("Rowcount : " + vo.getRowCount());
            if (vo.getRowCount() < 1) {
                param.put("NoDataMsg", "No Data to Display");
                jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
            } else {
                param.put("NoDataMsg", null);
                Row[] rr = vo.getAllRowsInRange();
                logger.info("rr.length " + rr.length);
                if (rr.length > 0) {
                    for (int i = 0; i < rr.length; i++) {
                        JSONObject jsonLocationsRowInput = new JSONObject();
                        logger.info("rr[i] "+rr[i]);
                        locationList.add(rr[i]);
                        int j=i+1;
                        logger.info("PartnerName" + rr[i].getAttribute("PartnerName").toString());                        
                        logger.info("Sno" + i);
                        String partnerServices = rr[i].getAttribute("Services")
                                                 .toString()
                                                 .replaceAll(",", ", ");
                        logger.info("partnerServices " + partnerServices);
                        String address=null;
                        String state=null;
                        String district=null;
                        if(rr[i].getAttribute("State")!=null){
                            state=rr[i].getAttribute("State").toString();
                        }
                        if(rr[i].getAttribute("DistrictName")!=null){
                            district=rr[i].getAttribute("DistrictName").toString();
                        }
                        if(rr[i].getAttribute("Address")!=null){
                            address=state+" "+district+" "+rr[i].getAttribute("Address").toString();
                        }
                        logger.info("address " + address);
                        logger.info("PartnerName " + rr[i].getAttribute("PartnerName"));
                        logger.info("Telephone " + rr[i].getAttribute("Telephone"));
                        logger.info("Pin " + rr[i].getAttribute("Pin"));
                        logger.info("CustStateName " + rr[i].getAttribute("CustStateName"));

                        jsonLocationsRowInput.put("Sno", String.valueOf(i));
                        jsonLocationsRowInput.put("NetworkType", param.get("NetworkFlag"));
                        jsonLocationsRowInput.put("Telephone", rr[i].getAttribute("Telephone").toString());
                        jsonLocationsRowInput.put("Latitude", rr[i].getAttribute("Latitude").toString());
                        jsonLocationsRowInput.put("Longitude", rr[i].getAttribute("Longitude").toString());
                        jsonLocationsRowInput.put("Email", rr[i].getAttribute("Email"));
                        jsonLocationsRowInput.put("Address", address);
                        jsonLocationsRowInput.put("PartnerName", rr[i].getAttribute("PartnerName"));
                        jsonLocationsRowInput.put("Pin", rr[i].getAttribute("Pin"));
                        jsonLocationsRowInput.put("CustStateName", rr[i].getAttribute("CustStateName"));
                        jsonLocationsRowInput.put("Services", partnerServices);
//                        jsonLocationsRowInput.put("DealerName", rr[i].getAttribute("DealerName"));//Zipcode
                        double distance =
                            CommonHelper.distanceBwLatLon(param.get("LatitudeVal").toString(),
                                                          rr[i].getAttribute("Latitude").toString(),
                                                          param.get("LongitudeVal").toString(),
                                                          rr[i].getAttribute("Longitude").toString());
                        logger.info("Distance " +distance);
                        jsonLocationsRowInput.put("Distance", distance);
                        locationJsonArray.put(jsonLocationsRowInput);
                    }
                    logger.info("locationJsonArray.length() " +locationJsonArray.length());
                    logger.info("locationList.size() " +locationList.size());
                    logger.info("vo " +vo.getRowCount());//vo
                    if(vo.getRowCount()>1){
                        locationJsonArrayFinal=CommonHelper.sortedJsonArray(locationJsonArray);
                        jsonLocationsInput.put("LocationDetails", locationJsonArrayFinal);
                    }else{
                        locationJsonArrayFinal=locationJsonArray;
                        jsonLocationsInput.put("LocationDetails",locationJsonArray);
                        
                    }
                    /*------------------------------*/
                    /*------------------------------*/
                    CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);
                    getListForInsertIntoPartnerLocator(locationJsonArrayFinal);

                    logger.info("jsonLocationsInput " + jsonLocationsInput);
                } else {
                    jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
                    CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);
                }
            }
            return locations;
        }
       
        if (param.get("NetworkFlag")
                           .toString()
                           .equalsIgnoreCase("AreaOffice")) {
            String aoType=null;
            if (param.get("LobValue")!=null) {
                aoType = param.get("LobValue").toString();
            }
            java.util.List lstInput = new java.util.ArrayList();
            JSONObject jsonInput = new JSONObject();


            OperationBinding op = CommonHelper.findOperation("serviceCustomerCall");
            op.getParamsMap().put("inputList", getListForServiceCallAO(aoType));
            op.getParamsMap().put("method", EPICConstant.AO_LOCATOR);
            op.execute();

            java.util.List returnList = (java.util.List) op.getResult();
            //            log.info("returnList.get(0) " + returnList.get(0));
            if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0)
                                                                                 .toString()
                                                                                 .equalsIgnoreCase(EPICConstant.TRUE_VAl)) {

            }

            bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            locationListIterator = bindings.findIteratorBinding("AOLocatorVO1Iterator");

            vo = locationListIterator.getViewObject();
            //            vo.setWhereClause(whereClauseString);
            //            locationListIterator.executeQuery();
            logger.info("Rowcount : " + vo.getRowCount());
            if (vo.getRowCount() < 1) {
                param.put("NoDataMsg", "No Data to Display");
                jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
            } else {
                param.put("NoDataMsg", null);
                Row[] rr = vo.getAllRowsInRange();
                logger.info("rr.length " + rr.length);
                if (rr.length > 0) {
                    for (int i = 0; i < rr.length; i++) {
                        JSONObject jsonLocationsRowInput = new JSONObject();
                        logger.info("rr[i] "+rr[i]);
                        locationList.add(rr[i]);
                        int j=i+1;
                        logger.info("OfficeName" + rr[i].getAttribute("OfficeName").toString());                        
                        logger.info("Sno" + i);
                        jsonLocationsRowInput.put("Sno", String.valueOf(i));
                        jsonLocationsRowInput.put("NetworkType", param.get("NetworkFlag"));
                        jsonLocationsRowInput.put("PhoneNo", rr[i].getAttribute("PhoneNo").toString());
                        jsonLocationsRowInput.put("Latitude", rr[i].getAttribute("Latitude").toString());
                        jsonLocationsRowInput.put("Longitude", rr[i].getAttribute("Longitude").toString());
                        jsonLocationsRowInput.put("Email", rr[i].getAttribute("Email"));
                        jsonLocationsRowInput.put("Address", rr[i].getAttribute("Address"));
                        jsonLocationsRowInput.put("OfficeName", rr[i].getAttribute("OfficeName"));
//                        jsonLocationsRowInput.put("DealerName", rr[i].getAttribute("DealerName"));//Zipcode
                        double distance =
                            CommonHelper.distanceBwLatLon(param.get("LatitudeVal").toString(),
                                                          rr[i].getAttribute("Latitude").toString(),
                                                          param.get("LongitudeVal").toString(),
                                                          rr[i].getAttribute("Longitude").toString());
                        logger.info("Distance " +distance);
                        jsonLocationsRowInput.put("Distance", distance);
                        locationJsonArray.put(jsonLocationsRowInput);
                    }
                    logger.info("locationJsonArray.length() " +locationJsonArray.length());
                    logger.info("locationList.size() " +locationList.size());
                    logger.info("vo " +vo.getRowCount());//vo
                    if(vo.getRowCount()>1){
                        locationJsonArrayFinal=CommonHelper.sortedJsonArray(locationJsonArray);
                        jsonLocationsInput.put("LocationDetails", locationJsonArrayFinal);
                    }else{
                        locationJsonArrayFinal=locationJsonArray;
                        jsonLocationsInput.put("LocationDetails",locationJsonArray);
                        
                    }
                    /*------------------------------*/
                    /*------------------------------*/
                    CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);

                    getListForInsertIntoAOLocator(locationJsonArrayFinal);

                    logger.info("jsonLocationsInput " + jsonLocationsInput);
                } else {
                    jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
                    CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);
                }
            }
            return locations;
        } else if (param.get("NetworkFlag")
                                  .toString()
                                  .equalsIgnoreCase("RetailOutlet")) {

            java.util.List lstInput = new java.util.ArrayList();
            JSONObject jsonInput = new JSONObject();


            OperationBinding op = CommonHelper.findOperation("serviceCustomerCall");
            op.getParamsMap().put("inputList", getListForServiceCall());
            op.getParamsMap().put("method", EPICConstant.RO_LOCATOR);
            op.execute();

            java.util.List returnList = (java.util.List) op.getResult();
            //            log.info("returnList.get(0) " + returnList.get(0));
            if ((returnList != null) && (returnList.get(0) != null) &&
                returnList.get(0).toString().equalsIgnoreCase(EPICConstant.TRUE_VAl)) {

            }

            bindings = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
            locationListIterator = bindings.findIteratorBinding("ROLocatorVO1Iterator");

            vo = locationListIterator.getViewObject();
            //            vo.setWhereClause(whereClauseString);
            //            locationListIterator.executeQuery();
            logger.info("Rowcount : " + vo.getRowCount());
            if (vo.getRowCount() < 1) {
                param.put("NoDataMsg", "No Data to Display");
                jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
            } else {
                param.put("NoDataMsg", null);
                Row[] rr = vo.getAllRowsInRange();
                logger.info("rr.length " + rr.length);
                if (rr.length > 0) {
                    for (int i = 0; i < rr.length; i++) {
                        JSONObject jsonLocationsRowInput = new JSONObject();
                        logger.info(rr[i]);
                        locationList.add(rr[i]);
                        //                            System.out.println("ROCode" + rr[i].getAttribute("ROCode").toString());
                        logger.info("ROName" + rr[i].getAttribute("ROName").toString());
                        //                            System.out.println("ROaddress" + rr[i].getAttribute("ROaddress").toString());
                        //                            System.out.println("State" + rr[i].getAttribute("State").toString());
                        //                            System.out.println("Telephone" + rr[i].getAttribute("Telephone").toString());
                        //                            System.out.println("Pin" +rr[i].getAttribute("Pin"));
                        //                            System.out.println("DealerName" + rr[i].getAttribute("DealerName").toString());
                        //                            System.out.println("WorkTimingStart" + rr[i].getAttribute("WorkTimingStart").toString());
                        //                            System.out.println("WorkTimingEnd" + rr[i].getAttribute("WorkTimingEnd").toString());
                        //                            System.out.println("Longitude" + rr[i].getAttribute("Latitude").toString());
                        //                            System.out.println("Latitude" + rr[i].getAttribute("Longitude").toString());
                        //                            System.out.println("MS" +rr[i].getAttribute("MS"));
                        //                            System.out.println("HSD"+ rr[i].getAttribute("HSD"));
                        //                            System.out.println("XP" +rr[i].getAttribute("XP"));
                        //                            System.out.println("XM"+ rr[i].getAttribute("XM"));
                        //                            System.out.println("Email" +rr[i].getAttribute("Email"));
                        //                            System.out.println("ROServices"+ rr[i].getAttribute("ROServices"));
                        logger.info("Sno" + i);
                        String roServices = rr[i].getAttribute("ROServices")
                                                 .toString()
                                                 .replaceAll(",", ", ");
                        logger.info("roServices " + roServices);
                        jsonLocationsRowInput.put("Sno", String.valueOf(i));
                        jsonLocationsRowInput.put("NetworkType", param.get("NetworkFlag"));
                        jsonLocationsRowInput.put("ROCode", rr[i].getAttribute("ROCode").toString());
                        jsonLocationsRowInput.put("ROName", rr[i].getAttribute("ROName").toString());
                        jsonLocationsRowInput.put("ROaddress", rr[i].getAttribute("ROaddress").toString());
                        jsonLocationsRowInput.put("State", rr[i].getAttribute("State"));
                        jsonLocationsRowInput.put("Telephone", rr[i].getAttribute("Telephone"));
                        jsonLocationsRowInput.put("Pin", rr[i].getAttribute("Pin"));
                        jsonLocationsRowInput.put("DealerName", rr[i].getAttribute("DealerName"));
                        jsonLocationsRowInput.put("WorkTimingStart", rr[i].getAttribute("WorkTimingStart"));
                        jsonLocationsRowInput.put("WorkTimingEnd", rr[i].getAttribute("WorkTimingEnd"));
                        jsonLocationsRowInput.put("Latitude", rr[i].getAttribute("Latitude").toString());
                        jsonLocationsRowInput.put("Longitude", rr[i].getAttribute("Longitude").toString()); //Zipcode
                        jsonLocationsRowInput.put("MS", rr[i].getAttribute("MS").toString());
                        jsonLocationsRowInput.put("HSD", rr[i].getAttribute("HSD").toString()); //Zipcode
                        jsonLocationsRowInput.put("XM", rr[i].getAttribute("XM").toString());
                        jsonLocationsRowInput.put("XP", rr[i].getAttribute("XP").toString()); //Zipcode
                        jsonLocationsRowInput.put("Email", rr[i].getAttribute("Email").toString());
                        //                            String roServices=rr[i].getAttribute("ROServices").toString().replaceAll(",", ", ");
                        jsonLocationsRowInput.put("ROServices",
                                                  rr[i].getAttribute("ROServices")
                                                  .toString()); //Zipcode
                        double distance =
                            CommonHelper.distanceBwLatLon(param.get("LatitudeVal").toString(),
                                                          rr[i].getAttribute("Latitude").toString(),
                                                          param.get("LongitudeVal").toString(),
                      rr[i].getAttribute("Longitude").toString());

                        jsonLocationsRowInput.put("Distance", distance);
                        locationJsonArray.put(jsonLocationsRowInput);
                    }
                    jsonLocationsInput.put("LocationDetails", CommonHelper.sortedJsonArray(locationJsonArray));

                    /*------------------------------*/
                    /*------------------------------*/
                    CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);

                    getListForInsertIntoROLocator(CommonHelper.sortedJsonArray(locationJsonArray));

                    logger.info("jsonLocationsInput " + jsonLocationsInput);
                } else {
                    jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
                    CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);
                }
            }
            return locations;

        } else {

            Row[] rr = vo.getAllRowsInRange();
            logger.info("rr.length " + rr.length);
            if (rr.length > 0) {

                for (int i = 0; i < rr.length; i++) {
                    JSONObject jsonLocationsRowInput = new JSONObject();
                    logger.info(rr[i]);
                    locationList.add(rr[i]);
                    logger.info("DistributorName" + rr[i].getAttribute("DistName").toString());
                    logger.info("Addr" + rr[i].getAttribute("Addr").toString());
                    logger.info("StateName" + rr[i].getAttribute("StateName").toString());
                    logger.info("Longitude" + rr[i].getAttribute("Latitude").toString());
                    logger.info("Latitude" + rr[i].getAttribute("Longitude").toString());
                    logger.info("MainPhNum" + rr[i].getAttribute("MainPhNum"));
                    logger.info("MainEmailAddr" + rr[i].getAttribute("MainEmailAddr"));
                    logger.info("Sno" + i);

                    jsonLocationsRowInput.put("Sno", String.valueOf(i));
                    jsonLocationsRowInput.put("NetworkType", param.get("NetworkFlag"));
                    jsonLocationsRowInput.put("Distributorname", rr[i].getAttribute("DistName").toString());
                    jsonLocationsRowInput.put("Address", rr[i].getAttribute("Addr").toString());
                    jsonLocationsRowInput.put("StateName", rr[i].getAttribute("StateName").toString());
                    jsonLocationsRowInput.put("Latitude", rr[i].getAttribute("Latitude").toString());
                    jsonLocationsRowInput.put("Longitude",
                                              rr[i].getAttribute("Longitude")
                                              .toString()); //Zipcode
                    if (rr[i].getAttribute("MainPhNum") != null) {
                        jsonLocationsRowInput.put("MainPhNum", rr[i].getAttribute("MainPhNum"));
                    } else {
                        jsonLocationsRowInput.put("MainPhNum", "NA");
                    }
                    if (rr[i].getAttribute("MainEmailAddr") != null) {
                        jsonLocationsRowInput.put("MainEmailAddr", rr[i].getAttribute("MainEmailAddr"));
                    } else {
                        jsonLocationsRowInput.put("MainEmailAddr", "NA");
                    }
                    if (rr[i].getAttribute("Zipcode") != null) {
                        jsonLocationsRowInput.put("Zipcode", rr[i].getAttribute("Zipcode").toString()); //Zipcode
                    } else {
                        jsonLocationsRowInput.put("Zipcode", "NA");
                    }

                    /**Below Change made by Kamal Tariyal on 21-11-2018**/
                    /**------------IvrsNum Attribute doesn't exist in vo ----**/
                    //                    if(rr[i].getAttribute("IvrsNum")!=null){
                    //                        jsonLocationsRowInput.put("IvrsNum", rr[i].getAttribute("IvrsNum").toString()); //Zipcode
                    //                    }else{
                    //                        jsonLocationsRowInput.put("IvrsNum", "NA");
                    //                    }
                    //
                    jsonLocationsRowInput.put("IvrsNum", "NA");
                    locationJsonArray.put(jsonLocationsRowInput);
                }
                jsonLocationsInput.put("LocationDetails", locationJsonArray);
                CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);
                logger.info("jsonLocationsInput " + jsonLocationsInput);
            } else {
                jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
                CommonHelper.setEL("#{pageFlowScope.dffd}", jsonLocationsInput);
            }
        }
        return locations;
    }


    public String searchAction() {
        getLocationDetails("Distributor");
        CommonHelper.runJavaScript("func()");
        return null;
    }

    public void setJsonStringLocations(String jsonStringLocations) {
        this.jsonStringLocations = jsonStringLocations;
    }

    public String getJsonStringLocations() {
        return jsonStringLocations;
    }


    public String searchLinkAction() {
        // Add event code here...
        getLocationDetails("Distributor");
        return null;
    }

    public void setJsonValueBinding(RichOutputText jsonValueBinding) {
        this.jsonValueBinding = jsonValueBinding;
    }

    public RichOutputText getJsonValueBinding() {
        return jsonValueBinding;
    }


    public String showButtonAction() {
        if (validationNtrwkLocator()) {
            if (searchLocationBinding.getValue() != null) {
                jsonLocationsInput = new JSONObject();
                locationJsonArray = new JSONArray();
                logger.info("latitudeBinding  " + latitudeBinding.getValue());
                logger.info("longitudeBinding  " + longitudeBinding.getValue());
                param.put("LatitudeVal", latitudeBinding.getValue());
                param.put("LongitudeVal", longitudeBinding.getValue());

                if (param.get("LatitudeVal") != null && param.get("LongitudeVal") != null) {
                    if (param.get("NetworkFlag")
                             .toString()
                             .equalsIgnoreCase("Ftl")) {
                        getLocationDetails("FTL");
                    }
                    if (param.get("NetworkFlag")
                             .toString()
                             .equalsIgnoreCase("AutoLPG")) {
                        getLocationDetails("AutoLPG");
                    }
                    if (param.get("NetworkFlag")
                             .toString()
                             .equalsIgnoreCase("Distributor")) {
                        getLocationDetails("Distributor");
                    }
                    if (param.get("NetworkFlag")
                             .toString()
                             .equalsIgnoreCase("RetailOutlet")) {
                        getLocationDetails("RetailOutlet");
                    }
                    if (param.get("NetworkFlag")
                             .toString()
                             .equalsIgnoreCase("AreaOffice")) {
                        getLocationDetails("AreaOffice");
                    }

                    if (ftl) {
                        getLocationDetails("FTL");
                    }
                    if (autoLPG) {
                        getLocationDetails("AutoLPG");
                    }
                    if (distributor) {
                        getLocationDetails("Distributor");
                    }
                    if (areaOffice) {
                        getLocationDetails("AreaOffice");
                    }
                }

                String imageUrlMap = getImagePath();
                logger.info("NetworkFlag   --->" + param.get("NetworkFlag"));
                logger.info("NoDataMsg   --->" + param.get("NoDataMsg"));
                CommonHelper.runJavaScript("func('" + imageUrlMap + "')");
            }
        }
        return null;
    }


    public String getImagePath() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                                                                      .getExternalContext()
                                                                      .getRequest();
        String url = request.getRequestURL().toString();
        String host = request.getHeader("Host");
        int i = url.indexOf("/webcenter");
//        int i=url.indexOf("/slider");  // for local
        String imghost = url.substring(0, i);
        logger.info("imghost " + imghost);
        logger.info("host ip---" + host);
        logger.info("url----" + url);
        String[] str = url.split("/", 3);
        logger.info("str " + str);
        logger.info("request " + request.getScheme());
        logger.info("ServerPort " + request.getServerPort());
        String uri = request.getRequestURI();
        logger.info("uri----" + uri);
        String imagePath = "/webcenter/images/Pointer-search.png?raw=true";
//        String imagePath="/slider/images/Pointer-search.png?raw=true"; //for local only comment this for deployment
        String finalPath = imghost + imagePath;
        logger.info("finalpath " + finalPath);
        return finalPath;
    }


    public void onLoadFtl() {
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        String whereClauseString = null;
        String zipcodeVal = null;
        DCBindingContainer bindings1 = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding distMasterIterator = bindings1.findIteratorBinding("LpgDistMaster1Iterator1");
        ViewObject vo = distMasterIterator.getViewObject();
        whereClauseString = "SAP_CODE='-1'";
        vo.setWhereClause(whereClauseString);
        distMasterIterator.executeQuery();
        if (vo.getRowCount() < 1) {
            param.put("NoDataMsg", "No Data to Display");
        } else {
            param.put("NoDataMsg", null);
        }
        param.put("FtlFlag", true);
        param.put("DistributorFlg", false);
        param.put("AutoLPGFlag", false);
        param.put("AreaOfficeFlag", false);
        param.put("RetailOutletFlag", false);
        param.put("NetworkFlag", "Ftl");
        param.put("CategoryVal", "Distributor selling 5 KG FTL cylinders");
        setFtl(true);
    }


    public void onLoadDistributor() {
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        String whereClauseString = null;
        String zipcodeVal = null;
        DCBindingContainer bindings1 = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding distMasterIterator = bindings1.findIteratorBinding("PartnerLocatorVO1Iterator");
        ViewObject vo = distMasterIterator.getViewObject();
        vo.executeEmptyRowSet();
        if (vo.getRowCount() < 1) {
            param.put("NoDataMsg", "No Data to Display");
        } else {
            param.put("NoDataMsg", null);
        }

        param.put("DistributorFlg", true);
        param.put("FtlFlag", false);
        param.put("AutoLPGFlag", false);
        param.put("AreaOfficeFlag", false);
        param.put("RetailOutletFlag", false);
        param.put("NetworkFlag", "Distributor");
        param.put("CategoryType", "IP");
        param.put("CategoryVal", "Distributors");
        setFtl(true);
    }


    public void onLoadRetailOutlet() {
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        String whereClauseString = null;
        String zipcodeVal = null;
        DCBindingContainer bindings1 = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding retailOutletIterator = bindings1.findIteratorBinding("ROLocatorVO1Iterator");
        ViewObject vo = retailOutletIterator.getViewObject();
        vo.executeEmptyRowSet();
        //        whereClauseString = "SAP_CODE='-1'";
        //        vo.setWhereClause(whereClauseString);
        //        distMasterIterator.executeQuery();
        if (vo.getRowCount() < 1) {
            param.put("NoDataMsg", "No Data to Display");
        } else {
            param.put("NoDataMsg", null);
        }

        param.put("DistributorFlg", false);
        param.put("FtlFlag", false);
        param.put("AutoLPGFlag", false);
        param.put("AreaOfficeFlag", false);
        param.put("RetailOutletFlag", true);
        param.put("NetworkFlag", "RetailOutlet");
        param.put("CategoryVal", "Fuel Stations");

    }

    public void onLoadAreaOffice(){        
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        locationList = new java.util.ArrayList();
        String imageUrlMap = null;
        String whereClauseString = null;
        String zipcodeVal = null;        
        DCBindingContainer bindings1 = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding aoIterator = bindings1.findIteratorBinding("AOLocatorVO1Iterator");
        ViewObject vo = aoIterator.getViewObject();
        vo.executeEmptyRowSet();
        if (vo.getRowCount() < 1) {
            param.put("NoDataMsg", "No Data to Display");
        } else {
           param.put("NoDataMsg", null);
        }
        param.put("DistributorFlg", false);
        param.put("FtlFlag", false);
        param.put("AutoLPGFlag", false);
        param.put("AreaOfficeFlag", true);
        param.put("RetailOutletFlag", false);
        param.put("NetworkFlag", "AreaOffice");
        param.put("CategoryType", "IO");
        param.put("CategoryVal", "Fuel Stations");

    }


    public void onLoadAutoLPGOutlet() {
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        String whereClauseString = null;
        String zipcodeVal = null;
        if (param.get("zipcodeVal") != null) {
            zipcodeVal = param.get("zipcodeVal")
                                        .toString();
        }
        DCBindingContainer bindings2 = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding autoLpgOutletIterator = bindings2.findIteratorBinding("AutolpgDistMasterVo1Iterator1");
        System.out.println("autoLpgOutletIterator " + autoLpgOutletIterator);
        ViewObject vo = autoLpgOutletIterator.getViewObject();
        whereClauseString = "SAP_CODE='-1'";
        vo.setWhereClause(whereClauseString);
        autoLpgOutletIterator.executeQuery();
        if (vo.getRowCount() < 1) {
            param.put("NoDataMsg", "No Data to Display");
        } else {
            param.put("NoDataMsg", null);
        }
        setAutoLPG(true);

        param.put("AutoLPGFlag", true);
        param.put("FtlFlag", false);
        param.put("RetailOutletFlag", false);
        param.put("DistributorFlg", false);
        param.put("AreaOfficeFlag", false);
        param.put("NetworkFlag", "AutoLPG");
        param.put("CategoryVal", "Auto LPG Outlet");
    }

    public void networkTypeVCL(ValueChangeEvent valueChangeEvent) {
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        // Add event code here...
        String whereClauseString = null;
        String zipcodeVal = null;
        if (valueChangeEvent.getNewValue() != null) {
            logger.info("" + valueChangeEvent.getNewValue());
            jsonLocationsInput.put("LocationDetails", JSONObject.NULL);
            logger.info("latitudeBinding  " + latitudeBinding.getValue());
            logger.info("longitudeBinding  " + longitudeBinding.getValue());
            if (valueChangeEvent.getNewValue()
                                .toString()
                                .contains("Distributor selling 5 KG FTL cylinders")) {
                onLoadFtl();
            } else if (valueChangeEvent.getNewValue()
                                       .toString()
                                       .equalsIgnoreCase("Auto LPG Outlet")) {
                onLoadAutoLPGOutlet();
            }else if (valueChangeEvent.getNewValue()
                                       .toString()
                                       .equalsIgnoreCase("Fuel Stations")) {
                onLoadRetailOutlet();
            }else if (valueChangeEvent.getNewValue()
                                       .toString()
                                       .equalsIgnoreCase("Distributors")) {
                param.put("LobValue", "Distributor");
                onLoadDistributor();
            }else if (valueChangeEvent.getNewValue()
                                       .toString()
                                       .equalsIgnoreCase("Stockist")) {
                param.put("LobValue", "Stockist");
                onLoadDistributor();
            } else if (valueChangeEvent.getNewValue()
                                       .toString()
                                       .equalsIgnoreCase("LPG Area Office")) {
                param.put("LobValue", "Distributor");
                onLoadAreaOffice();
            } else if (valueChangeEvent.getNewValue()
                                       .toString()
                                       .equalsIgnoreCase("Retail Divisional Office")) {
                param.put("LobValue", "Dealer");
                onLoadAreaOffice();
            } else if (valueChangeEvent.getNewValue()
                                       .toString()
                                       .equalsIgnoreCase("Lubes State Office")) {
                param.put("LobValue", "Stockist");
                onLoadAreaOffice();
            } 
            
            CommonHelper.runJavaScript("mapInit()");
        }
    }

    public void setFetchZipcodeBinding(RichOutputText fetchZipcodeBinding) {
        this.fetchZipcodeBinding = fetchZipcodeBinding;
    }

    public RichOutputText getFetchZipcodeBinding() {
        return fetchZipcodeBinding;
    }

    public void setFetchZipInputBinding(RichInputText fetchZipInputBinding) {
        this.fetchZipInputBinding = fetchZipInputBinding;
    }

    public RichInputText getFetchZipInputBinding() {
        return fetchZipInputBinding;
    }

    public void beanHandler(ClientEvent ce) {
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        // Add event code here...
        logger.info("beanHandler ---->");

        if (ce.getParameters().get("param1") != null) {
            logger.info("latitudeBinding  " + latitudeBinding.getValue());
            logger.info("longitudeBinding  " + longitudeBinding.getValue());

            String param1 = (String) ce.getParameters().get("param1");
            zipcodeBean = param1;
            param.put("zipcodeVal", param1);
            logger.info("PARAM" + zipcodeBean);
            String imageUrlMap = null;
            if (jsonLocationsInput != null) {

                //                if (EPICIOCLResourceCustBundle.findKeyValue("G_MAP_IMGURL") != null) {
                //                    imageUrlMap = EPICIOCLResourceCustBundle.findKeyValue("G_MAP_IMGURL").toString();
                //                }

                imageUrlMap = getImagePath();
                logger.info("NetworkFlag   --->" + param.get("NetworkFlag"));
                logger.info("NoDataMsg   --->" + param.get("NoDataMsg"));
                CommonHelper.runJavaScript("func('" + imageUrlMap + "')");
            }
        }
    }

    public void searchLocationVCL(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        jsonLocationsInput = new JSONObject();
        locationJsonArray = new JSONArray();
        logger.info("MYNAME");
        if (valueChangeEvent.getNewValue() != null) {
            logger.info("latitudeBinding  " + latitudeBinding.getValue());
            logger.info("longitudeBinding  " + longitudeBinding.getValue());
            param.put("LatitudeVal", latitudeBinding.getValue());
            param.put("LongitudeVal", longitudeBinding.getValue());

            if (param.get("LatitudeVal") != null && param.get("LongitudeVal") != null) {
                if (param.get("NetworkFlag")
                                   .toString()
                                   .equalsIgnoreCase("Ftl")) {
                    getLocationDetails("FTL");
                }
                if (param.get("NetworkFlag")
                                   .toString()
                                   .equalsIgnoreCase("Distributor")) {
                    getLocationDetails("Distributor");
                }
                if (param.get("NetworkFlag")
                                   .toString()
                                   .equalsIgnoreCase("AreaOffice")) {
                    getLocationDetails("AreaOffice");
                }
                if (param.get("NetworkFlag")
                                   .toString()
                                   .equalsIgnoreCase("AutoLPG")) {
                    getLocationDetails("AutoLPG");
                }
                if (ftl) {
                    getLocationDetails("FTL");
                }
                if (distributor) {
                    getLocationDetails("Distributor");
                }
                if (areaOffice) {
                    getLocationDetails("AreaOffice");
                }
                if (autoLPG) {
                    getLocationDetails("AutoLPG");
                }
            }
            logger.info("New Value -----> " + valueChangeEvent.getNewValue());
            String imageUrlMap = null;
            //            if (EPICIOCLResourceCustBundle.findKeyValue("G_MAP_IMGURL") != null) {
            //                imageUrlMap = EPICIOCLResourceCustBundle.findKeyValue("G_MAP_IMGURL").toString();
            //            }
            imageUrlMap = getImagePath();
            logger.info("NetworkFlag   --->" + param.get("NetworkFlag"));
            logger.info("NoDataMsg   --->" + param.get("NoDataMsg"));
            CommonHelper.runJavaScript("func('" + imageUrlMap + "')");
        }
    }


    public void setLatitudeBean(String latitudeBean) {
        this.latitudeBean = latitudeBean;
    }

    public String getLatitudeBean() {
        return latitudeBean;
    }

    public void setLongitudeBean(String longitudeBean) {
        this.longitudeBean = longitudeBean;
    }

    public String getLongitudeBean() {
        return longitudeBean;
    }

    public void setLatitudeBinding(RichInputText latitudeBinding) {
        this.latitudeBinding = latitudeBinding;
    }

    public RichInputText getLatitudeBinding() {
        return latitudeBinding;
    }

    public void setLongitudeBinding(RichInputText longitudeBinding) {
        this.longitudeBinding = longitudeBinding;
    }

    public RichInputText getLongitudeBinding() {
        return longitudeBinding;
    }

    public void setSearchLocationBinding(RichInputText searchLocationBinding) {
        this.searchLocationBinding = searchLocationBinding;
    }

    public RichInputText getSearchLocationBinding() {
        return searchLocationBinding;
    }

    public void setCategory(Object catType) {
        DCBindingContainer bindings1 = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding ntwkLocatorIterator = bindings1.findIteratorBinding("NetworkLocatorVo1Iterator");
        Row currRow = (Row) ntwkLocatorIterator.getViewObject().createRow();
        currRow.setAttribute("CategoryType", catType);
        ntwkLocatorIterator.getViewObject().setCurrentRow(currRow);
    }

    public void getListForInsertIntoROLocator(JSONArray arrayJson) {
        logger.info("arrayJson " + arrayJson);
        JSONObject jsonInput = new JSONObject();
        java.util.List lst = new java.util.ArrayList();
        jsonInput.put("Data", arrayJson); 
        logger.info("getListForInsertIntoROLocator");
        logger.info("jsonInput" + jsonInput);
        lst.add(0, jsonInput);
        OperationBinding op = CommonHelper.findOperation("insertIntoRoLocator");
        op.getParamsMap().put("jsonArrayList", lst);
        op.execute();
    }
    
    public void getListForInsertIntoAOLocator(JSONArray arrayJson) {
        logger.info("arrayJson " + arrayJson);
        logger.info("arrayJson.length()"+arrayJson.length());
        JSONObject jsonInput = new JSONObject();
        java.util.List lst = new java.util.ArrayList();
        jsonInput.put("Data", arrayJson); 
        logger.info("getListForInsertIntoAOLocator");
        logger.info("jsonInput" + jsonInput);
        lst.add(0, jsonInput);
        OperationBinding op = CommonHelper.findOperation("insertIntoAoLocator");
        op.getParamsMap().put("jsonArrayList", lst);
        op.execute();
    }
    
    public void getListForInsertIntoPartnerLocator(JSONArray arrayJson) {
        logger.info("arrayJson " + arrayJson);
        logger.info("arrayJson.length()"+arrayJson.length());
        JSONObject jsonInput = new JSONObject();
        java.util.List lst = new java.util.ArrayList();
        jsonInput.put("Data", arrayJson); 
        logger.info("getListForInsertIntoAOLocator");
        logger.info("jsonInput" + jsonInput);
        lst.add(0, jsonInput);
        if(param.get("LobValue")!=null){
            if(param.get("LobValue").toString().equalsIgnoreCase("Distributor")){
                OperationBinding op = CommonHelper.findOperation("insertIntoPartnerDistLocator");
                op.getParamsMap().put("jsonArrayList", lst);
                op.execute();
            }else{
                OperationBinding op = CommonHelper.findOperation("insertIntoPartnerStockLocator");
                op.getParamsMap().put("jsonArrayList", lst);
                op.execute();
            }
        }
        
    }
    
    public String feedbackLinkAction() {
        // Add event code here...
        System.out.println("pdealerCode " + param.get("pdealerCode"));
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        this.getFeedbackPopupBinding().show(hints);
        this.fetchDealerCodeMap(param.get("pdealerCode").toString());
        return null;
    }


    public void setFeedbackPopupBinding(RichPopup feedbackPopupBinding) {
        this.feedbackPopupBinding = feedbackPopupBinding;
    }

    public RichPopup getFeedbackPopupBinding() {
        return feedbackPopupBinding;
    }


    java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
    private RichOutputFormatted quantityDelivered;
    private RichSelectOneRadio quantityDeliveredSOC;
    private RichOutputFormatted serviceBind;
    private RichSelectOneRadio serviceSOC;
    private RichOutputFormatted cleanlinessBind;
    private RichSelectOneRadio cleanlinessSOC;
    private RichOutputFormatted facilitiesBind;
    private RichSelectOneRadio facilitiesSOC;
    private RichOutputFormatted cleantoiletBind;
    private RichSelectOneRadio cleanToiletSOC;
    private UIRatingGauge overallRatingbind;
    private Integer overallRatingvalue = 0;
    private Integer cleanlinessRatingvalue = 5;
    private Integer facilitiesRatingvalue = 5;
    private Integer cleanToiletRatingvalue = 5;
    private Integer serviceRatingvalue = 5;
    private Integer quantityRatingvalue = 5;
    private Integer productQualityRatingvalue = 5;
    private String genericErrMsg;
    private boolean errorInDetails;
    private UIRatingGauge cleanlinessdvtBind;
    private UIRatingGauge facilitiesdvtBind;
    private UIRatingGauge cleanToiletdvtBind;
    private UIRatingGauge servicedvtBind;
    private UIRatingGauge quantitydvtBind;
    private RichOutputFormatted productQualitybind;
    private UIRatingGauge productQualitydvtbind;
    private RichButton submitDealerbttnBinding;


    public String fetchDealerCodeMap(String pDealerCode) {
        // Add event code here...
        System.out.println("fetchDealerCodeMap");
        System.out.println("pDealerCode : " + pDealerCode);
        String dealerCode = pDealerCode;
        String trimmeddealerCode = null;

        String errorSAPDEALERNOTFound = "";
        String retString = "";
        String pFlowFrom = null;
        if (param.get("pFrom") != null) {
            pFlowFrom = param.get("pFrom")
                                       .toString();
            System.out.println(pFlowFrom);
        }

        System.out.println("pFlowFrom " + pFlowFrom);
        System.out.println("dealerCode " + dealerCode);

        param.put("dcNotExist", "");

        try {
            if (dealerCode != null && !StringUtils.isBlank(dealerCode)) {
                trimmeddealerCode = dealerCode.trim();
                logger.info("pdealerCode value" + trimmeddealerCode);
                param.put("pdealerCode", trimmeddealerCode);
                param.put("serviceRatingvalue", 5);
                param.put("cleanlinessRatingvalue", 5);
                param.put("cleanToiletRatingvalue", 5);
                param.put("facilitiesRatingvalue", 5);
                param.put("quantityRatingvalue", 5);
                param.put("productQuantityRatingval", 5);

                JSONObject jsonInputSAPCode = new JSONObject();
                logger.info("input==>" + jsonInputSAPCode.toString());

                OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                java.util.List lst = new java.util.ArrayList();
                jsonInputSAPCode.put("DealerCode", dealerCode);
                jsonInputSAPCode.put("LOB", "Petrol Pump");

                lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("GETDISTRICT_DEALERCODE_RETAIL"));

                lst.add(1, jsonInputSAPCode);
                ob.getParamsMap().put(EPICConstant.SERVICELIST, lst);
                ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.FETCH_DEALER_DETAILS);
                List retValue = new ArrayList();
                retValue = (List) ob.execute();
                List result = (List) ob.getResult();
                if (result == null) {
                    retString = "ERROR";
                    param.put("pError", "ERROR");
                }
                if (result != null && result.size() > 0) {
                    if ((result != null) && (result.get(0) != null) && result.get(0)
                                                                             .toString()
                                                                             .equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                        JSONObject jsonObject = (JSONObject) retValue.get(1);
                        if (jsonObject.has("DealerDetails")) {
                            JSONArray partnerDtlsAr = jsonObject.getJSONArray("DealerDetails");
                            for (int i = 0; i < partnerDtlsAr.length(); ++i) {
                                JSONObject rec = partnerDtlsAr.getJSONObject(i);

                                String partnerName = rec.isNull("PartnerName") ? "" : rec.getString("PartnerName");
                                String stateRetail = rec.isNull("PartnerState") ? "" : rec.getString("PartnerState");
                                String districtRetail =
                                    rec.isNull("PartnerDistrict") ? "" : rec.getString("PartnerDistrict");
                                param.put("poutputDealer", partnerName);
                                param.put("outputSAPState", stateRetail);
                                param.put("outputSAPDistrict", districtRetail);
                                retString = "toDealerFeedback";
                                break;
                            }
                        }

                    }


                    else if (result.get(0) == null || result.get(0)
                                                            .toString()
                                                            .equalsIgnoreCase("False") && retValue.get(1) != null) {
                        JSONObject jsonObject = (JSONObject) retValue.get(1);
                        if (!jsonObject.isNull("ErrorCode") &&
                            jsonObject.getString(EPICConstant.ERROR_CODE)
                            .equalsIgnoreCase(EPICConstant.NOT_FOUND_CASE)) {

                            errorSAPDEALERNOTFound = CommonHelper.getValueFromRsBundle("NO_DISTBT_FOUND_SAP");
                            param.put("pSAPnotFound", errorSAPDEALERNOTFound);
                            logger.info("error message value" + errorSAPDEALERNOTFound);
                            param.put("partnerNF", "Y");

                        }
                    }
                } else {
                    param.put("dcNotExist", "Y");
                }
            } else {
                param.put("dcNotExist", "Y");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return retString;
    }

    public String onSubmitDealerFeedback() {
        String retString = "toconfirm";

        String dealerCode = "";
        String quantityDel = "";
        String service = "";
        String cleanliness = "";
        String cleanToilet = "";
        String facilities = "";
        String pFlowFrom = null;

        if (errorInDetails == true) {
            logger.info("all questions not answered");
            return null;
        }
        dealerCode = (String) param.get("pdealerCode");
        logger.info("pdealerCode value" + dealerCode);

        String quantityDelOF = (String) quantityDelivered.getValue();
        String serviceOF = (String) serviceBind.getValue();
        String cleanlinessOF = (String) cleanlinessBind.getValue();
        String cleanToiletOF = (String) cleantoiletBind.getValue();
        String facilitiesOF = (String) facilitiesBind.getValue();
        String productQualOF = (String) productQualitybind.getValue();
        logger.info("values of quantityDelOF::" + quantityDelOF);
        logger.info("values of serviceOF  ::" + serviceOF);
        logger.info("values of cleanlinessOF::" + cleanlinessOF);
        logger.info("values of cleanToiletOF::" + cleanToiletOF);
        logger.info("values of facilitiesOF::" + facilitiesOF);
        logger.info("values of productQualOF::" + productQualOF);

        try {

            OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            java.util.List lst = new java.util.ArrayList();
            JSONObject jsonInput = new JSONObject();
            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("SUBMIT_DEALER_FEEDBACK"));
            jsonInput.put("PartnerCode", dealerCode);
            jsonInput.put("LOB", EPICConstant.FEEDBACK_LOB_DEALER);
            jsonInput.put("FeedbackType", EPICConstant.FEEDBACK_TYPE_DEALER);
            JSONArray catSubCAtArr = new JSONArray();
            JSONObject jsonInputChild1 = new JSONObject();
            jsonInputChild1.put("OverallRating", param.get("overallRatingvalue"));
            jsonInputChild1.put("ProductQuality", param.get("productQuantityRatingval"));
            jsonInputChild1.put("QuantityDelivered", param.get("quantityRatingvalue"));
            jsonInputChild1.put("Service", param.get("serviceRatingvalue"));
            jsonInputChild1.put("Cleanliness", param.get("cleanlinessRatingvalue"));
            jsonInputChild1.put("Facilities", param.get("facilitiesRatingvalue"));
            jsonInputChild1.put("CleanToilet", param.get("cleanToiletRatingvalue"));
            catSubCAtArr.put(jsonInputChild1);

            jsonInput.put("EPICDealerFeedback", catSubCAtArr);

            lst.add(1, jsonInput);
            ob.getParamsMap().put(EPICConstant.SERVICELIST, lst);
            ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.SUBMIT_DEALER_FEEDBACK);
            List retValue = new ArrayList();
            retValue = (List) ob.execute();
            List result = (List) ob.getResult();
            if (result == null) {
                retString = "ERROR";
                param.put("pError", "ERROR");
            }
            if (result != null && result.size() > 0) {
                if ((result != null) && (result.get(0) != null) && result.get(0)
                                                                         .toString()
                                                                         .equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                    JSONObject jsonObject = (JSONObject) retValue.get(1);
                    if (!jsonObject.isNull("ErrorCode") &&
                        jsonObject.getString(EPICConstant.ERROR_CODE).equalsIgnoreCase("0")) {

                        return retString;
                    } else {

                        retString = "ERROR";
                        return retString;
                    }
                } else if (result.get(0) == null || result.get(0)
                                                          .toString()
                                                          .equalsIgnoreCase("False") && retValue.get(1) != null) {
                    JSONObject jsonObject = (JSONObject) retValue.get(1);
                    if (!jsonObject.isNull("ErrorCode") &&
                        jsonObject.getString(EPICConstant.ERROR_CODE).equalsIgnoreCase(EPICConstant.SBL100)) {
                        logger.info("Inside onSubmitDealerFeedback ErrorCode is 100");
                        String dealerFeedback =
                            jsonObject.isNull(EPICConstant.ERROR_MESSAGE) ? "" :
                            jsonObject.getString(EPICConstant.ERROR_MESSAGE);
                        logger.info("dealerFeedback: " + dealerFeedback);
                        param.put("errorBusinessValidationMSG", dealerFeedback);
                       param.put("perrorValidationCode", EPICConstant.SBL100);                        
                        param.put("errorBusinessValidationCode", EPICConstant.SBL100);
                        param.put("perrorValidationMessage", dealerFeedback);
                        param.put("perrorValidationCode", EPICConstant.SBL100);
                        return null;
                    } else {

                        retString = "ERROR";
                        return retString;
                    }
                } else {

                    retString = "ERROR";
                    return retString;
                }
            }
        } catch (Exception dealerFeedback) {

            CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
            logger.error("Exception is:" + dealerFeedback);
            retString = "ERROR";


        }

        return retString;
    }


    public void overallRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try {
            logger.info("Inside GetRating");
            if (valueChangeEvent.getNewValue() != null) {
                Double i = Double.parseDouble(valueChangeEvent.getNewValue().toString());
                overallRatingvalue = i.intValue();
                param.put("overallRatingvalue", overallRatingvalue);
                logger.info("overallRatingvalue" + overallRatingvalue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanlinessRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try {
            logger.info("Inside cleanlinessRatingVC");
            if (valueChangeEvent.getNewValue() != null) {
                Double i = Double.parseDouble(valueChangeEvent.getNewValue().toString());
                cleanlinessRatingvalue = i.intValue();
                param.put("cleanlinessRatingvalue", cleanlinessRatingvalue);
                logger.info("cleanlinessRatingvalue" + cleanlinessRatingvalue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void facilitiesRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try {
            logger.info("Inside facilitiesRatingVC");
            if (valueChangeEvent.getNewValue() != null) {
                Double i = Double.parseDouble(valueChangeEvent.getNewValue().toString());
                facilitiesRatingvalue = i.intValue();
                param.put("facilitiesRatingvalue", facilitiesRatingvalue);
                logger.info("facilitiesRatingvalue" + facilitiesRatingvalue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanToiletRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try {
            logger.info("Inside cleanToiletRatingVC");
            if (valueChangeEvent.getNewValue() != null) {
                Double i = Double.parseDouble(valueChangeEvent.getNewValue().toString());
                cleanToiletRatingvalue = i.intValue();
                param.put("cleanToiletRatingvalue", cleanToiletRatingvalue);
                logger.info("cleanToiletRatingvalue" + cleanToiletRatingvalue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serviceRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try {
            logger.info("Inside serviceRatingVC");
            if (valueChangeEvent.getNewValue() != null) {
                Double i = Double.parseDouble(valueChangeEvent.getNewValue().toString());
                serviceRatingvalue = i.intValue();
                param.put("serviceRatingvalue", serviceRatingvalue);
                logger.info("serviceRatingvalue" + serviceRatingvalue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quantityRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try {
            logger.info("Inside quantityRatingVC");
            if (valueChangeEvent.getNewValue() != null) {
                Double i = Double.parseDouble(valueChangeEvent.getNewValue().toString());
                quantityRatingvalue = i.intValue();
                param.put("quantityRatingvalue", quantityRatingvalue);
                logger.info("quantityRatingvalue" + quantityRatingvalue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void productQualityVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try {
            logger.info("Inside productQualityVC");
            if (valueChangeEvent.getNewValue() != null) {
                Double i = Double.parseDouble(valueChangeEvent.getNewValue().toString());
                productQualityRatingvalue = i.intValue();
                param.put("productQuantityRatingval", productQualityRatingvalue);
                logger.info("productQuantityRatingval" + productQualityRatingvalue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean ValidateFeedback(String gRecaptchaResponse) {
        errorInDetails = false;
        genericErrMsg = "";

        if (param.get("overallRatingvalue") == null) {
            genericErrMsg = CommonHelper.getValueFromRsBundle("RATING_ERROR");
            errorInDetails = true;
        }

        return errorInDetails;
    }


    public void setQuantityDelivered(RichOutputFormatted quantityDelivered) {
        this.quantityDelivered = quantityDelivered;
    }

    public RichOutputFormatted getQuantityDelivered() {
        return quantityDelivered;
    }

    public void setQuantityDeliveredSOC(RichSelectOneRadio quantityDeliveredSOC) {
        this.quantityDeliveredSOC = quantityDeliveredSOC;
    }

    public RichSelectOneRadio getQuantityDeliveredSOC() {
        return quantityDeliveredSOC;
    }

    public void setServiceBind(RichOutputFormatted serviceBind) {
        this.serviceBind = serviceBind;
    }

    public RichOutputFormatted getServiceBind() {
        return serviceBind;
    }

    public void setServiceSOC(RichSelectOneRadio serviceSOC) {
        this.serviceSOC = serviceSOC;
    }

    public RichSelectOneRadio getServiceSOC() {
        return serviceSOC;
    }

    public void setCleanlinessBind(RichOutputFormatted cleanlinessBind) {
        this.cleanlinessBind = cleanlinessBind;
    }

    public RichOutputFormatted getCleanlinessBind() {
        return cleanlinessBind;
    }

    public void setCleanlinessSOC(RichSelectOneRadio cleanlinessSOC) {
        this.cleanlinessSOC = cleanlinessSOC;
    }

    public RichSelectOneRadio getCleanlinessSOC() {
        return cleanlinessSOC;
    }

    public void setFacilitiesBind(RichOutputFormatted facilitiesBind) {
        this.facilitiesBind = facilitiesBind;
    }

    public RichOutputFormatted getFacilitiesBind() {
        return facilitiesBind;
    }

    public void setFacilitiesSOC(RichSelectOneRadio facilitiesSOC) {
        this.facilitiesSOC = facilitiesSOC;
    }

    public RichSelectOneRadio getFacilitiesSOC() {
        return facilitiesSOC;
    }

    public void setCleantoiletBind(RichOutputFormatted cleantoiletBind) {
        this.cleantoiletBind = cleantoiletBind;
    }

    public RichOutputFormatted getCleantoiletBind() {
        return cleantoiletBind;
    }

    public void setCleanToiletSOC(RichSelectOneRadio cleanToiletSOC) {
        this.cleanToiletSOC = cleanToiletSOC;
    }

    public RichSelectOneRadio getCleanToiletSOC() {
        return cleanToiletSOC;
    }

    public void setOverallRatingbind(UIRatingGauge overallRatingbind) {
        this.overallRatingbind = overallRatingbind;
    }

    public UIRatingGauge getOverallRatingbind() {
        return overallRatingbind;
    }

    public void setOverallRatingvalue(Integer overallRatingvalue) {
        this.overallRatingvalue = overallRatingvalue;
    }

    public Integer getOverallRatingvalue() {
        return overallRatingvalue;
    }

    public void setGenericErrMsg(String genericErrMsg) {
        this.genericErrMsg = genericErrMsg;
    }

    public String getGenericErrMsg() {
        return genericErrMsg;
    }

    public void setCleanlinessRatingvalue(Integer cleanlinessRatingvalue) {
        this.cleanlinessRatingvalue = cleanlinessRatingvalue;
    }

    public Integer getCleanlinessRatingvalue() {
        return cleanlinessRatingvalue;
    }

    public void setCleanlinessdvtBind(UIRatingGauge cleanlinessdvtBind) {
        this.cleanlinessdvtBind = cleanlinessdvtBind;
    }

    public UIRatingGauge getCleanlinessdvtBind() {
        return cleanlinessdvtBind;
    }

    public void setFacilitiesdvtBind(UIRatingGauge facilitiesdvtBind) {
        this.facilitiesdvtBind = facilitiesdvtBind;
    }

    public UIRatingGauge getFacilitiesdvtBind() {
        return facilitiesdvtBind;
    }

    public void setFacilitiesRatingvalue(Integer facilitiesRatingvalue) {
        this.facilitiesRatingvalue = facilitiesRatingvalue;
    }

    public Integer getFacilitiesRatingvalue() {
        return facilitiesRatingvalue;
    }

    public void setCleanToiletdvtBind(UIRatingGauge cleanToiletdvtBind) {
        this.cleanToiletdvtBind = cleanToiletdvtBind;
    }

    public UIRatingGauge getCleanToiletdvtBind() {
        return cleanToiletdvtBind;
    }

    public void setCleanToiletRatingvalue(Integer cleanToiletRatingvalue) {
        this.cleanToiletRatingvalue = cleanToiletRatingvalue;
    }

    public Integer getCleanToiletRatingvalue() {
        return cleanToiletRatingvalue;
    }

    public void setServicedvtBind(UIRatingGauge servicedvtBind) {
        this.servicedvtBind = servicedvtBind;
    }

    public UIRatingGauge getServicedvtBind() {
        return servicedvtBind;
    }

    public void setServiceRatingvalue(Integer serviceRatingvalue) {
        this.serviceRatingvalue = serviceRatingvalue;
    }

    public Integer getServiceRatingvalue() {
        return serviceRatingvalue;
    }

    public void setQuantitydvtBind(UIRatingGauge quantitydvtBind) {
        this.quantitydvtBind = quantitydvtBind;
    }

    public UIRatingGauge getQuantitydvtBind() {
        return quantitydvtBind;
    }

    public void setQuantityRatingvalue(Integer quantityRatingvalue) {
        this.quantityRatingvalue = quantityRatingvalue;
    }

    public Integer getQuantityRatingvalue() {
        return quantityRatingvalue;
    }

    public void setProductQualitybind(RichOutputFormatted productQualitybind) {
        this.productQualitybind = productQualitybind;
    }

    public RichOutputFormatted getProductQualitybind() {
        return productQualitybind;
    }

    public void setProductQualitydvtbind(UIRatingGauge productQualitydvtbind) {
        this.productQualitydvtbind = productQualitydvtbind;
    }

    public UIRatingGauge getProductQualitydvtbind() {
        return productQualitydvtbind;
    }

    public void setProductQualityRatingvalue(Integer productQualityRatingvalue) {
        this.productQualityRatingvalue = productQualityRatingvalue;
    }

    public Integer getProductQualityRatingvalue() {
        return productQualityRatingvalue;
    }

    public void setSubmitDealerbttnBinding(RichButton submitDealerbttnBinding) {
        this.submitDealerbttnBinding = submitDealerbttnBinding;
    }

    public RichButton getSubmitDealerbttnBinding() {
        return submitDealerbttnBinding;
    }

    public void feedbackPopupCancelListener(PopupCanceledEvent popupCanceledEvent) {
        // Add event code here...
        CommonHelper.runJavaScript("mapInit()");
        String imageUrlMap = getImagePath();
        logger.info("NetworkFlag   --->" + param.get("NetworkFlag"));
        logger.info("NoDataMsg   --->" + param.get("NoDataMsg"));
        CommonHelper.runJavaScript("func('" + imageUrlMap + "')");
        
    }

    public void setLocationJsonArrayFinal(JSONArray locationJsonArrayFinal) {
        this.locationJsonArrayFinal = locationJsonArrayFinal;
    }

    public JSONArray getLocationJsonArrayFinal() {
        return locationJsonArrayFinal;
    }

    public void setCategoryList(List<SelectItem> categoryList) {
        this.categoryList = categoryList;
    }

    public List<SelectItem> getCategoryList() {
        return categoryList;
    }
    
    
    private String categoryTypeErrMsg;
    private String searchLocationErrMsg;
    
    
    public boolean validationNtrwkLocator() {
            boolean retVal = true;


            Object categoryType = categoryTypeBinding.getValue();
            Object searchLocation = searchLocationBinding.getValue();
            
            categoryTypeErrMsg = "";
            searchLocationErrMsg = "";
            categoryTypeBinding.setStyleClass("");
            searchLocationBinding.setStyleClass("");

            if (categoryType == null) {
                retVal = false;
                categoryTypeBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                categoryTypeErrMsg = CommonHelper.getValueFromRsBundle("CATEGORY_VALIDATION_MSG");
            } else {
                if (String.valueOf(categoryType).equalsIgnoreCase("Please Select")) {
                    retVal = false;
                    categoryTypeBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                    categoryTypeErrMsg = CommonHelper.getValueFromRsBundle("CATEGORY_VALIDATION_MSG");
                }
            }

            if (searchLocation == null) {
                retVal = false;
                searchLocationBinding.setStyleClass(EPICConstant.ERROR_CLASS);
                searchLocationErrMsg = CommonHelper.getValueFromRsBundle("LOCATION_VALIDATION_MSG");
            } else {
//                if (String.valueOf(searchLocation).equalsIgnoreCase("Please Select")) {
//                    retVal = true;
//                    searchLocationBinding.setStyleClass(EPICConstant.ERROR_CLASS);
//                    searchLocationErrMsg = CommonHelper.getValueFromRsBundle("LOCATION_VALIDATION_MSG");
//                }
            }
            return retVal;
    }

    public void setCategoryTypeErrMsg(String categoryTypeErrMsg) {
        this.categoryTypeErrMsg = categoryTypeErrMsg;
    }

    public String getCategoryTypeErrMsg() {
        return categoryTypeErrMsg;
    }

    public void setSearchLocationErrMsg(String searchLocationErrMsg) {
        this.searchLocationErrMsg = searchLocationErrMsg;
    }

    public String getSearchLocationErrMsg() {
        return searchLocationErrMsg;
    }

    public void setCategoryTypeBinding(RichSelectOneChoice categoryTypeBinding) {
        this.categoryTypeBinding = categoryTypeBinding;
    }

    public RichSelectOneChoice getCategoryTypeBinding() {
        return categoryTypeBinding;
    }

    public void setNetworklocatorbeanBinding(RichOutputFormatted networklocatorbeanBinding) {
        this.networklocatorbeanBinding = networklocatorbeanBinding;
    }

    public RichOutputFormatted getNetworklocatorbeanBinding() {
        return networklocatorbeanBinding;
    }

    public void setCategoryTypeErrMsgBinding(RichOutputFormatted categoryTypeErrMsgBinding) {
        this.categoryTypeErrMsgBinding = categoryTypeErrMsgBinding;
    }

    public RichOutputFormatted getCategoryTypeErrMsgBinding() {
        return categoryTypeErrMsgBinding;
    }
}
