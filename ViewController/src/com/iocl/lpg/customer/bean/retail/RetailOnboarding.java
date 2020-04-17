package com.iocl.lpg.customer.bean.retail;

import com.iocl.lpg.customer.bean.loyalty.LoyaltyEnrichment;
import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.CustomerValidation;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

import java.io.Serializable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.faces.model.SelectItem;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.fragment.RichRegion;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.output.RichOutputFormatted;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.binding.OperationBinding;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class RetailOnboarding implements Serializable {
    @SuppressWarnings("compatibility:-076788326081444111")
    private static final long serialVersionUID = 1L;
    private static Logger log ;
    
    private String prefixErrorMsg;
    private String genderErrorMsg;
    private String dateOfBirthErrorMsg;
    private String address1ErrorMsg;
    private String stateErrorMsg;
    private String districtErrorMsg;
    private String cityErrorMsg;
    private String emailErrorMsg;
    private String mobileErrorMsg;
    private String pincodeErrorMsg;
    private RichSelectOneChoice genderSelBind;
    private RichInputDate dobSelBind;
    private RichInputText addr1Bind;
    private RichInputText addr2Bind;
    private RichInputText addr3Bind;
    private RichInputText landmrk1Bind;
    private RichSelectOneChoice state1Bind;
    private RichSelectOneChoice district1Bind;
    private RichInputText cityBind;
    private RichInputText pinCodeBind;
    
    private RichPopup addVehPopUpBind;
    private RichInputText licensePlateNumBind;
    private RichOutputFormatted licensePlateNumErrorBind;
    private RichOutputFormatted vehicleTypeBind;
    private RichOutputFormatted vehicleMdErrBind;
    private RichOutputFormatted modelErrBind;
    private RichOutputFormatted fuelTypeErrBind;
    private RichOutputFormatted vehicleTypErrBind;
    private RichSelectOneRadio primaryVehSelectBind;
    private RichInputDate pucDtBind;
    private RichInputDate insExpDtBind;
    private RichInputDate taxexpDtBind;
    private RichSelectOneChoice vehTypeBind;
    private RichSelectOneChoice vehMakeBind;
    private RichSelectOneChoice vehModelBind;
    private RichSelectOneChoice vehFuelBind;
    
    
    java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
    java.util.Map sessionScopeParam = ADFContext.getCurrent().getSessionScope();
    
    private String confirmationPageMsg;
    private String confirmDestinationLink=CommonHelper.getValueFromRsBundle("BACK_TO_PROFILE");
    private String confirmDestinationLinkDirect="backFromConfirm";
    private String confirmReferenceNum;
    private RichSelectOneChoice vehOwnerBind;
    private RichOutputFormatted vehOwnErrBind;
    private RichRegion error100VehBoBind;
    
    java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
    private RichSelectOneChoice prefixBinding;
    private RichInputText emailBind;
    private RichOutputText dupUCMIdBinding;
    private RichInputText firstNameBinding;
    private RichInputText lastNameBinding;
    private RichInputText mobileBinding;


    public RetailOnboarding() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {
            log = Logger.getLogger(RetailOnboarding.class);
        } else {
            log = Logger.getLogger(RetailOnboarding.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    
    public String defaultCallLoadRetailOnboard(){
        String retStr="goToROBoard";
        try{
            resetOnboardForm(); 
            param.put("pVehicleTypeLst", fetchVehicleBasedLOVs(EPICConstant.EPIC_LOY_VEHICLE)); 
         //   param.put("pVehicleMakeLst", fetchVehicleBasedLOVs(EPICConstant.EPIC_ASSET_MAKE)); 
            //  param.put("pVehicleModelLst", fetchVehicleBasedLOVs(EPICConstant.EPIC_ASSET_MODEL));
            param.put("pVehicleFuelLst", fetchVehicleBasedLOVs(EPICConstant.AUTO_FUEL_TYPE));
            populateIdamfields();
        }catch(Exception e) {
            log.info("exception in defaultCallLoadRetailOnboard::"+e.getMessage());   
            retStr="ERROR";
        }
        return retStr;
    }
    
    public void populateIdamfields() {
        OperationBinding ProfileOp = CommonHelper.findOperation("insertRetailEnrichInfo");


//            ProfileOp.getParamsMap().put("FirstName","Kamal");
//            ProfileOp.getParamsMap().put("LastName","Tariyal");
//            ProfileOp.getParamsMap().put("MobileNo","9458668656");
        
        ProfileOp.getParamsMap()
            .put("FirstName", String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserFirstName}")));
        ProfileOp.getParamsMap()
            .put("LastName", String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserLastName}")));
        ProfileOp.getParamsMap()
            .put("MobileNo", String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserMobileNo}")));
        ProfileOp.getParamsMap()
            .put("EmailId", String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserEmailId}")));
        ProfileOp.execute();

    }
    
    public void setPrefixErrorMsg(String prefixErrorMsg) {
        this.prefixErrorMsg = prefixErrorMsg;
    }

    public String getPrefixErrorMsg() {
        return prefixErrorMsg;
    }

    public void setGenderErrorMsg(String genderErrorMsg) {
        this.genderErrorMsg = genderErrorMsg;
    }

    public String getGenderErrorMsg() {
        return genderErrorMsg;
    }

    public void setDateOfBirthErrorMsg(String dateOfBirthErrorMsg) {
        this.dateOfBirthErrorMsg = dateOfBirthErrorMsg;
    }

    public String getDateOfBirthErrorMsg() {
        return dateOfBirthErrorMsg;
    }

    public void setAddress1ErrorMsg(String address1ErrorMsg) {
        this.address1ErrorMsg = address1ErrorMsg;
    }

    public String getAddress1ErrorMsg() {
        return address1ErrorMsg;
    }

    public void setStateErrorMsg(String stateErrorMsg) {
        this.stateErrorMsg = stateErrorMsg;
    }

    public String getStateErrorMsg() {
        return stateErrorMsg;
    }

    public void setDistrictErrorMsg(String districtErrorMsg) {
        this.districtErrorMsg = districtErrorMsg;
    }

    public String getDistrictErrorMsg() {
        return districtErrorMsg;
    }

    public void setCityErrorMsg(String cityErrorMsg) {
        this.cityErrorMsg = cityErrorMsg;
    }

    public String getCityErrorMsg() {
        return cityErrorMsg;
    }

    public void setPincodeErrorMsg(String pincodeErrorMsg) {
        this.pincodeErrorMsg = pincodeErrorMsg;
    }

    public String getPincodeErrorMsg() {
        return pincodeErrorMsg;
    }

    public void setGenderSelBind(RichSelectOneChoice genderSelBind) {
        this.genderSelBind = genderSelBind;
    }

    public RichSelectOneChoice getGenderSelBind() {
        return genderSelBind;
    }

    public void setDobSelBind(RichInputDate dobSelBind) {
        this.dobSelBind = dobSelBind;
    }

    public RichInputDate getDobSelBind() {
        return dobSelBind;
    }

    public void dobValListener(ValueChangeEvent valueChangeEvent) {
        log.info("Check DOB eleigibiliy");
        if (valueChangeEvent.getNewValue() != null) {
            String dobString = valueChangeEvent.getNewValue().toString();
            if (!dobEligiblityCall(dobString)) {
                dateOfBirthErrorMsg = CommonHelper.getValueFromRsBundle("AGE_MUST_BE_GREATE_OR_EQUAL_EGHTEEN");
                dobSelBind.setStyleClass(EPICConstant.ERROR_CLASS);
            }
        }
    }

    public void setAddr1Bind(RichInputText addr1Bind) {
        this.addr1Bind = addr1Bind;
    }

    public RichInputText getAddr1Bind() {
        return addr1Bind;
    }

    public void setAddr2Bind(RichInputText addr2Bind) {
        this.addr2Bind = addr2Bind;
    }

    public RichInputText getAddr2Bind() {
        return addr2Bind;
    }

    public void setAddr3Bind(RichInputText addr3Bind) {
        this.addr3Bind = addr3Bind;
    }

    public RichInputText getAddr3Bind() {
        return addr3Bind;
    }

    public void setLandmrk1Bind(RichInputText landmrk1Bind) {
        this.landmrk1Bind = landmrk1Bind;
    }

    public RichInputText getLandmrk1Bind() {
        return landmrk1Bind;
    }

    public void setState1Bind(RichSelectOneChoice state1Bind) {
        this.state1Bind = state1Bind;
    }

    public RichSelectOneChoice getState1Bind() {
        return state1Bind;
    }

    public void setDistrict1Bind(RichSelectOneChoice district1Bind) {
        this.district1Bind = district1Bind;
    }

    public RichSelectOneChoice getDistrict1Bind() {
        return district1Bind;
    }

    public void setCityBind(RichInputText cityBind) {
        this.cityBind = cityBind;
    }

    public RichInputText getCityBind() {
        return cityBind;
    }

    public void setPinCodeBind(RichInputText pinCodeBind) {
        this.pinCodeBind = pinCodeBind;
    }

    public RichInputText getPinCodeBind() {
        return pinCodeBind;
    }
    
    public ArrayList<SelectItem> fetchVehicleBasedLOVs(String typeLOV) {
        log.info("inside VehicleBasedLOVs list");
        ArrayList<SelectItem> vehicleBasedLOVList=new ArrayList<SelectItem>();
        try { 
            OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            List lstInput = new ArrayList();
            lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.VEHICLE_LOVS));

            JSONObject jsonInput = new JSONObject();

            jsonInput.put("ListOfValType", typeLOV);
            if(typeLOV.equalsIgnoreCase(EPICConstant.EPIC_ASSET_MODEL)){
                jsonInput.put("ParentLOV", param.get("makeValParam"));
            }           
            log.info("jsonInput for getting VehicleBasedLOVs==>" + jsonInput);
            lstInput.add(1, jsonInput);                
            ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
            ob.getParamsMap().put(EPICConstant.SERVICEMETHOD,EPICConstant.VEHICLE_LOVS);
            ob.execute();
                
            java.util.List ReturnList = (java.util.List) ob.getResult();
                if ((ReturnList != null) && 
                      (ReturnList.get(0) != null) && 
                     ReturnList.get(0).toString().equalsIgnoreCase("true")) {
                
                     JSONObject jsonObject = (JSONObject) ReturnList.get(1);

                    JSONArray loValues = jsonObject.getJSONArray("ListOfValues");
                    JSONObject loValuesObj = loValues.getJSONObject(0);
                    JSONArray valuesArr = loValuesObj.getJSONArray("Values");
                    vehicleBasedLOVList = new ArrayList<SelectItem>();
                    for (int i = 0; i < valuesArr.length(); ++i) {
                        JSONObject valuesArrObj = valuesArr.getJSONObject(i);
                        String lovName = valuesArrObj.isNull("Name") == true ? "" : valuesArrObj.getString("Name");
                        String lovCode = valuesArrObj.isNull("Value") == true ? "" : valuesArrObj.getString("Value");

                      //  vehicleBasedLOVList.add(new SelectItem(lovCode, lovName));
                      if(typeLOV.equalsIgnoreCase(EPICConstant.EPIC_ASSET_MAKE)){
                          log.info("checking data for Makers for vehicle type as==>" + String.valueOf(param.get("makeValParam")));
                          if(String.valueOf(valuesArrObj.getString("SubType")).equalsIgnoreCase(String.valueOf(param.get("makeValParam")))) 
                           {
                             vehicleBasedLOVList.add(new SelectItem(lovName, lovCode));
                           }
                      }
                      else{
                          vehicleBasedLOVList.add(new SelectItem(lovName, lovCode)); 
                      }
                    }
                } else {
                    JSONObject jsonObject = (JSONObject) ReturnList.get(1);                   
                    log.info("Error While  getting VehicleBasedLOVs for type "+typeLOV+"::"+jsonObject.getString(EPICConstant.ERROR_CODE)+jsonObject.getString(EPICConstant.ERROR_MESSAGE));
                }
                if (!ob.getErrors().isEmpty()) {
                    log.info("error");
                }
           
        } catch (Exception e) {
            log.info("exception in method getting VehicleBasedLOVs for type "+typeLOV+"::"+e.getMessage());
        }
        return vehicleBasedLOVList;
    }
    
    public void resetOnboardForm(){
        licensePlateNumBind = new RichInputText();
        licensePlateNumErrorBind = new RichOutputFormatted();
        vehicleMdErrBind = new RichOutputFormatted();
        modelErrBind = new RichOutputFormatted();
        fuelTypeErrBind = new RichOutputFormatted();
        vehicleTypErrBind = new RichOutputFormatted();

        licensePlateNumBind.setStyleClass("");
        licensePlateNumBind.setValue("");
        licensePlateNumErrorBind.setValue("");
        vehicleMdErrBind.setValue("");
        modelErrBind.setValue("");
        fuelTypeErrBind.setValue("");
        vehicleTypErrBind.setValue("");
        primaryVehSelectBind=new RichSelectOneRadio();
        primaryVehSelectBind.setValue("N");
        param.put("pVehicleTypeLst", null); 
        param.put("pVehicleMakeLst", null); 
        param.put("pVehicleModelLst", null); 
        param.put("pVehicleFuelLst", null); 
        error100VehBoBind=new RichRegion();
        error100VehBoBind.setVisible(false);
        
    }

    public void setAddVehPopUpBind(RichPopup addVehPopUpBind) {
        this.addVehPopUpBind = addVehPopUpBind;
    }

    public RichPopup getAddVehPopUpBind() {
        return addVehPopUpBind;
    }

    public void setLicensePlateNumBind(RichInputText licensePlateNumBind) {
        this.licensePlateNumBind = licensePlateNumBind;
    }

    public RichInputText getLicensePlateNumBind() {
        return licensePlateNumBind;
    }

    public void setLicensePlateNumErrorBind(RichOutputFormatted licensePlateNumErrorBind) {
        this.licensePlateNumErrorBind = licensePlateNumErrorBind;
    }

    public RichOutputFormatted getLicensePlateNumErrorBind() {
        return licensePlateNumErrorBind;
    }

    public void setVehicleTypeBind(RichOutputFormatted vehicleTypeBind) {
        this.vehicleTypeBind = vehicleTypeBind;
    }

    public RichOutputFormatted getVehicleTypeBind() {
        return vehicleTypeBind;
    }

    public void setVehicleMdErrBind(RichOutputFormatted vehicleMdErrBind) {
        this.vehicleMdErrBind = vehicleMdErrBind;
    }

    public RichOutputFormatted getVehicleMdErrBind() {
        return vehicleMdErrBind;
    }

    public void setModelErrBind(RichOutputFormatted modelErrBind) {
        this.modelErrBind = modelErrBind;
    }

    public RichOutputFormatted getModelErrBind() {
        return modelErrBind;
    }

    public void setFuelTypeErrBind(RichOutputFormatted fuelTypeErrBind) {
        this.fuelTypeErrBind = fuelTypeErrBind;
    }

    public RichOutputFormatted getFuelTypeErrBind() {
        return fuelTypeErrBind;
    }

    public void setVehicleTypErrBind(RichOutputFormatted vehicleTypErrBind) {
        this.vehicleTypErrBind = vehicleTypErrBind;
    }

    public RichOutputFormatted getVehicleTypErrBind() {
        return vehicleTypErrBind;
    }

    public void setPrimaryVehSelectBind(RichSelectOneRadio primaryVehSelectBind) {
        this.primaryVehSelectBind = primaryVehSelectBind;
    }

    public RichSelectOneRadio getPrimaryVehSelectBind() {
        return primaryVehSelectBind;
    }

    public void setPucDtBind(RichInputDate pucDtBind) {
        this.pucDtBind = pucDtBind;
    }

    public RichInputDate getPucDtBind() {
        return pucDtBind;
    }

    public void setInsExpDtBind(RichInputDate insExpDtBind) {
        this.insExpDtBind = insExpDtBind;
    }

    public RichInputDate getInsExpDtBind() {
        return insExpDtBind;
    }

    public void setTaxexpDtBind(RichInputDate taxexpDtBind) {
        this.taxexpDtBind = taxexpDtBind;
    }

    public RichInputDate getTaxexpDtBind() {
        return taxexpDtBind;
    }

    public void setVehTypeBind(RichSelectOneChoice vehTypeBind) {
        this.vehTypeBind = vehTypeBind;
    }

    public RichSelectOneChoice getVehTypeBind() {
        return vehTypeBind;
    }

    public void setVehMakeBind(RichSelectOneChoice vehMakeBind) {
        this.vehMakeBind = vehMakeBind;
    }

    public RichSelectOneChoice getVehMakeBind() {
        return vehMakeBind;
    }

    public void setVehModelBind(RichSelectOneChoice vehModelBind) {
        this.vehModelBind = vehModelBind;
    }

    public RichSelectOneChoice getVehModelBind() {
        return vehModelBind;
    }

    public void setVehFuelBind(RichSelectOneChoice vehFuelBind) {
        this.vehFuelBind = vehFuelBind;
    }

    public RichSelectOneChoice getVehFuelBind() {
        return vehFuelBind;
    }

    public String submitRetailOnboardCall() {
        log.info("inside submitRetailOnboardCall service call");
        try{
            if (validateRetailOnboarding()) {
                return null;
            }
            OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);

            java.util.List lst = new java.util.ArrayList();            
            JSONObject jsonInput = new JSONObject();

            JSONObject jsonInputVehicle = new JSONObject();
            jsonInputVehicle.put("VehicleNumber", String.valueOf(licensePlateNumBind.getValue()));
            jsonInputVehicle.put("VehicleType", String.valueOf(vehTypeBind.getValue()));
            jsonInputVehicle.put("Make",String.valueOf(vehMakeBind.getValue()));
            jsonInputVehicle.put("Model",String.valueOf(vehModelBind.getValue()));
            jsonInputVehicle.put("FuleType", String.valueOf(vehFuelBind.getValue()));            
            if(!CustomerValidation.isNull(pucDtBind.getValue()))
            {
               jsonInputVehicle.put("PUCDate", dateConverter(pucDtBind.getValue()));
            }
            if(!CustomerValidation.isNull(taxexpDtBind.getValue()))
            {
               jsonInputVehicle.put("TaxExpiryDate",dateConverter(taxexpDtBind.getValue()));
            }
            if(!CustomerValidation.isNull(insExpDtBind.getValue()))
            {
               jsonInputVehicle.put("InsuranceExpiryDate", dateConverter(insExpDtBind.getValue()));   
            }
            jsonInputVehicle.put("VehicleOwnershipType", String.valueOf(vehOwnerBind.getValue()));  
            jsonInputVehicle.put("RetailVehicleStatus", EPICConstant.REL_ACT);
            
            jsonInput.put("Vehicle", jsonInputVehicle);
                        

            JSONObject jsonInputAddress = new JSONObject();
            jsonInputAddress.put("Address1", String.valueOf(addr1Bind.getValue()));
            jsonInputAddress.put("Address2", String.valueOf(addr2Bind.getValue()));
            jsonInputAddress.put("Address3", String.valueOf(addr3Bind.getValue()));
            jsonInputAddress.put("Landmark", String.valueOf(landmrk1Bind.getValue()));
            jsonInputAddress.put("State", String.valueOf(state1Bind.getValue()));
            jsonInputAddress.put("District", String.valueOf(district1Bind.getValue()));
            jsonInputAddress.put("Pincode", String.valueOf(pinCodeBind.getValue()));
            jsonInputAddress.put("City", String.valueOf(cityBind.getValue()));
            
            jsonInput.put("AddressDetails", jsonInputAddress);
            
            
            jsonInput.put("PrimaryVehicleFlg",String.valueOf(primaryVehSelectBind.getValue())); 
            jsonInput.put(EPICConstant.TRACKING_ID, CommonHelper.createUniqueID());
            jsonInput.put(EPICConstant.SOURCE, EPICConstant.SOURCE_PORTAL);
            jsonInput.put(EPICConstant.REQ_TYPE, EPICConstant.ONREQTYPE);
            jsonInput.put(EPICConstant.EXTSYS, EPICConstant.CAPortal);
            jsonInput.put(EPICConstant.FIRST_NAME,firstNameBinding.getValue());
            jsonInput.put(EPICConstant.LAST_NAME,lastNameBinding.getValue());
            jsonInput.put(EPICConstant.MOBILE_NUMBER,mobileBinding.getValue());
            jsonInput.put("DateOfBirth",dateConverter(dobSelBind.getValue()));
            jsonInput.put("Gender",String.valueOf(genderSelBind.getValue()));
            jsonInput.put("Operation","New");
            //   lst.add(0, EPICIOCLResourceBundle.findKeyValue(EPICConstant.ADD_CUSTOMER_VEHICLE_DETAILS));
            
            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.ONBOARD_CUSTOMER_RETAIL));
            lst.add(1, jsonInput);
            ob.getParamsMap().put(EPICConstant.SERVICELIST, lst);
            ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.ONBOARD_CUSTOMER_RETAIL);
            ob.execute();
            java.util.List ReturnList = (java.util.List) ob.getResult();
            log.info("onboarding retail returnList Value at 0 index is " + ReturnList.get(0));
            if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0).toString().equalsIgnoreCase("false"))) {
                log.info("onboarding retail Not worked, returnList Value at 0 index is " + ReturnList.get(0));
                JSONObject jsonResponse=(JSONObject)ReturnList.get(1);                    
//                CommonHelper.errorMessageCall(String.valueOf(jsonResponse.get(EPICConstant.ERROR_CODE)));
                pageflowParam.put("retailOnBErrorCode",
                                  jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_CODE));
                pageflowParam.put("retailOnBErrorMessage",
                                  jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_MESSAGE));


                if (pageflowParam.get("retailOnBErrorCode") != null && pageflowParam.get("retailOnBErrorCode")
                                                                                 .toString()
                                                                                 .equalsIgnoreCase(EPICConstant.SBL100)) {
                    
                    if ((!jsonResponse.isNull("UCMId") && jsonResponse.get("UCMId") != "")) {
                        log.info("Retail User Duplicate Data Found");
                        log.info("User will Navigate to Details Page");
                        sessionScopeParam.put("sRetailOnboardJson", jsonInput);
                        return "duplicateDetails";
                    }
                    
                    log.info("Business Validation Issue SBL-100 ErrorCode Received while Onboarding retail");
                    error100VehBoBind.setVisible(true);
                    return null;
                } else {
                    log.info("Some Error in WebService Response while Onboarding retail"); 
                    error100VehBoBind.setVisible(false);
                    return EPICConstant.ERROR;
                }
              
            } else {
                JSONObject jsonObject =new JSONObject(ReturnList.get(1).toString());  
                confirmationPageMsg="Thank you for submitting the details.";
                confirmReferenceNum="";
                return "ToConfirmation";
            }   
        }
        catch(Exception ex){
            log.info("error in submitRetailOnboardCall"+ex);
            return EPICConstant.ERROR;
        }
    }

    public void resetRetOnboardForm(ActionEvent actionEvent) {
        licensePlateNumBind.setStyleClass("");
        licensePlateNumBind.setValue("");
        licensePlateNumErrorBind.setValue("");
        vehicleMdErrBind.setValue("");
        modelErrBind.setValue("");
        vehMakeBind.setValue("");
        fuelTypeErrBind.setValue("");
        vehicleTypErrBind.setValue("");
        vehMakeBind.setStyleClass("");
        vehFuelBind.setStyleClass("");
        vehTypeBind.setStyleClass("");
        vehModelBind.setStyleClass("");
        param.put("pVehicleModelLst", null); 
        
        prefixErrorMsg="";
        genderErrorMsg="";
        dateOfBirthErrorMsg="";
        address1ErrorMsg="";
        stateErrorMsg="";
        districtErrorMsg="";
        cityErrorMsg="";
        pincodeErrorMsg="";
        genderSelBind.setValue("");
        dobSelBind.setValue("");
        addr1Bind.setValue("");
        addr2Bind.setValue("");
        addr3Bind.setValue("");
        landmrk1Bind.setValue("");
        state1Bind.setValue("");
        district1Bind.setValue("");
        cityBind.setValue("");
        pinCodeBind.setValue("");
        
        genderSelBind.setStyleClass("");
        dobSelBind.setStyleClass("");
        addr1Bind.setStyleClass("");
        state1Bind.setStyleClass("");
        district1Bind.setStyleClass("");
        cityBind.setStyleClass("");
        pinCodeBind.setStyleClass("");
        vehOwnerBind.setValue("");
        vehOwnerBind.setStyleClass("");
        vehOwnErrBind.setValue("");
        prefixErrorMsg="";
        emailErrorMsg="";
        emailBind.setStyleClass("");
        emailBind.setValue(CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUserEmailId}"));
        prefixBinding.setStyleClass("");
        
        error100VehBoBind=new RichRegion();
        error100VehBoBind.setVisible(false);
       
        
      
    }
    
    public Boolean validateRetailOnboarding(){
        Boolean errorInOnboarding = false;
        
        prefixErrorMsg ="";
        prefixBinding.setStyleClass("");
        emailErrorMsg ="";
        emailBind.setStyleClass("");
        mobileErrorMsg = null;
        mobileBinding.setStyleClass("");
        genderErrorMsg ="";
        genderSelBind.setStyleClass("");
        dateOfBirthErrorMsg ="";
        dobSelBind.setStyleClass("");
        address1ErrorMsg ="";
        addr1Bind.setStyleClass("");
        stateErrorMsg ="";
        state1Bind.setStyleClass("");
        districtErrorMsg = "";
        district1Bind.setStyleClass("");
        pincodeErrorMsg ="";
        pinCodeBind.setStyleClass("");
        cityErrorMsg ="";
        cityBind.setStyleClass("");
        licensePlateNumErrorBind.setValue("");
        licensePlateNumBind.setStyleClass("");
        vehicleTypErrBind.setValue("");
        vehTypeBind.setStyleClass("");
        vehicleMdErrBind.setValue("");
        vehMakeBind.setStyleClass("");
        modelErrBind.setValue("");
        vehModelBind.setStyleClass("");
        fuelTypeErrBind.setValue("");
        vehFuelBind.setStyleClass("");
        vehOwnErrBind.setValue("");
        vehOwnerBind.setStyleClass("");
        
        if(CustomerValidation.isNull(prefixBinding.getValue()))
        {
            prefixErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_PREFIX");
            prefixBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        if(CustomerValidation.isNull(emailBind.getValue()))
        {
            emailErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_EMAIL_ID");
            emailBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        if (!CustomerValidation.isNull(emailBind.getValue())) {
            if (CustomerValidation.isValidOrNullVal(emailBind.getValue(), EPICConstant.EMAIL_EXPRESSSION) ==
                EPICConstant.INVALID_CASE) {
                emailErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_VALID_EMAIL_ID");
                emailBind.setStyleClass(EPICConstant.ERROR_CLASS);
                errorInOnboarding = true;
            }
        }
        if(CustomerValidation.isNull(mobileBinding.getValue()))
        {
            mobileErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_MOBILE_NO");
            mobileBinding.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        if(CustomerValidation.isNull(genderSelBind.getValue()))
        {
            genderErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_GENDER");
            genderSelBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        
        if(CustomerValidation.isNull(dobSelBind.getValue()))
        {
            dateOfBirthErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_DATE_OF_BIRTH");
            dobSelBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        if(CustomerValidation.isNull(addr1Bind.getValue()))
        {
            address1ErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_ADDRESS1");
            addr1Bind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        if(CustomerValidation.isNull(state1Bind.getValue()))
        {
            stateErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_STATE");
            state1Bind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
       if(CustomerValidation.isNull(district1Bind.getValue()))
        {
            districtErrorMsg = CommonHelper.getValueFromRsBundle("SELECT_DISTRICT");
            district1Bind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        if(CustomerValidation.isNull(pinCodeBind.getValue()))
        {
            pincodeErrorMsg = CommonHelper.getValueFromRsBundle("ENTER_PINCODE");
            pinCodeBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        if(CustomerValidation.isNull(cityBind.getValue()))
        {
            cityErrorMsg = CommonHelper.getValueFromRsBundle("ENTER__LOYALTY_CITY");
            cityBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        if (StringUtils.isBlank(String.valueOf(licensePlateNumBind.getValue()))) {
            licensePlateNumErrorBind.setValue("Enter license registration number");
            licensePlateNumBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        } else if (!CommonHelper.invalidCharacter(licensePlateNumBind.getValue())) {
            licensePlateNumErrorBind.setValue("Enter valid license registration number");
            licensePlateNumBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        log.info("vehTypeBind value::"+String.valueOf(vehTypeBind.getValue()));
        if (CustomerValidation.isNull(vehTypeBind.getValue())) {
            vehicleTypErrBind.setValue("Select vehicle type");
            vehTypeBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        } 
        if (CustomerValidation.isNull(vehMakeBind.getValue())) {
            vehicleMdErrBind.setValue("Select vehicle make");
            vehMakeBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        } 
        if (CustomerValidation.isNull(vehModelBind.getValue())) {
            modelErrBind.setValue("Select vehicle model");
            vehModelBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        } 
        if (CustomerValidation.isNull(vehFuelBind.getValue())) {
            fuelTypeErrBind.setValue("Select fuel type");
            vehFuelBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        } 
        if(CustomerValidation.isNull(vehOwnerBind.getValue()))
        {
            vehOwnErrBind.setValue("Select vehicle ownership type");
            vehOwnerBind.setStyleClass(EPICConstant.ERROR_CLASS);
            errorInOnboarding = true;
        }
        
        
        return errorInOnboarding;
    }
    
    public String dateConverter(Object dateObj){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String datetime = dateFormat.format((java.util.Date)dateObj);
        log.info("datetime::"+datetime);
        return datetime;
    }

    public void setConfirmationPageMsg(String confirmationPageMsg) {
        this.confirmationPageMsg = confirmationPageMsg;
    }

    public String getConfirmationPageMsg() {
        return confirmationPageMsg;
    }

    public void setConfirmDestinationLink(String confirmDestinationLink) {
        this.confirmDestinationLink = confirmDestinationLink;
    }

    public String getConfirmDestinationLink() {
        return confirmDestinationLink;
    }

    public void setConfirmDestinationLinkDirect(String confirmDestinationLinkDirect) {
        this.confirmDestinationLinkDirect = confirmDestinationLinkDirect;
    }

    public String getConfirmDestinationLinkDirect() {
        return confirmDestinationLinkDirect;
    }

    public void setConfirmReferenceNum(String confirmReferenceNum) {
        this.confirmReferenceNum = confirmReferenceNum;
    }

    public String getConfirmReferenceNum() {
        return confirmReferenceNum;
    }

   

    public void setVehOwnerBind(RichSelectOneChoice vehOwnerBind) {
        this.vehOwnerBind = vehOwnerBind;
    }

    public RichSelectOneChoice getVehOwnerBind() {
        return vehOwnerBind;
    }

    public void setVehOwnErrBind(RichOutputFormatted vehOwnErrBind) {
        this.vehOwnErrBind = vehOwnErrBind;
    }

    public RichOutputFormatted getVehOwnErrBind() {
        return vehOwnErrBind;
    }

    public void setError100VehBoBind(RichRegion error100VehBoBind) {
        this.error100VehBoBind = error100VehBoBind;
    }

    public RichRegion getError100VehBoBind() {
        return error100VehBoBind;
    }
    
    public Date getCurrentSystemDate() {
        try {
            Calendar now = Calendar.getInstance();
            java.util.Date date = now.getTime();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = formatter.format(date);
            return formatter.parse(currentDate);
        } catch (Exception e) {
            return null;
         }
     }

    public void setPrefixBinding(RichSelectOneChoice prefixBinding) {
        this.prefixBinding = prefixBinding;
    }

    public RichSelectOneChoice getPrefixBinding() {
        return prefixBinding;
    }
    
    public boolean dobEligiblityCall(String dobEntered) {
        log.info("DOBEntered::"+dobEntered);
        oracle.jbo.domain.Date doB = CommonHelper.dateCompToJBODate(dobEntered,String.valueOf(2));
        
        int age = CommonHelper.diffInYear(doB);
        log.info("age " + age);
        if (age < 18) {
            return false;
        } else {
            dateOfBirthErrorMsg = "";
            dobSelBind.setStyleClass("");
            return true;
        }
    }

    public void setEmailErrorMsg(String emailErrorMsg) {
        this.emailErrorMsg = emailErrorMsg;
    }

    public String getEmailErrorMsg() {
        return emailErrorMsg;
    }

    public void setEmailBind(RichInputText emailBind) {
        this.emailBind = emailBind;
    }

    public RichInputText getEmailBind() {
        return emailBind;
    }
    
    public void vehicleTypeSelector(ValueChangeEvent valueChangeEvent) {
        String newValVehType=String.valueOf(valueChangeEvent.getNewValue());
        param.put("makeValParam",newValVehType);
        param.put("pVehicleMakeLst", fetchVehicleBasedLOVs(EPICConstant.EPIC_ASSET_MAKE)); 
    }
    
    public void fetchModelFromMakeListener(ValueChangeEvent valueChangeEvent) {
        String newValVehMake=String.valueOf(valueChangeEvent.getNewValue());
        param.put("makeValParam",newValVehMake);
        param.put("pVehicleModelLst", fetchVehicleBasedLOVs(EPICConstant.EPIC_ASSET_MODEL)); 
    }

    public void modelSelector(ValueChangeEvent valueChangeEvent) {
        String newValVehMdl=String.valueOf(valueChangeEvent.getNewValue());
        param.put("makeValParam",newValVehMdl);
        param.put("pVehicleFuelLst", fetchVehicleBasedLOVs(EPICConstant.AUTO_FUEL_TYPE)); 
    }

    public String YesButtonAction() {
        // Add event code here...
        log.info("Inside YesButtonAction method Start");
        String retString;
        JSONObject jsonInput = new JSONObject(sessionScopeParam.get("sRetailOnboardJson").toString());
        
        log.info("duplicate UCMId from previous Service is "+dupUCMIdBinding.getValue());
        jsonInput.put("UCMId", dupUCMIdBinding.getValue());
        jsonInput.put("IdamUId",CommonHelper.evaluateEL("#{sessionScope.userDetails.idamUId}"));
        
        retString = retailDetailAction(jsonInput);
        return retString;
    }

    public String NoButtonAction() {
        // Add event code here...
        log.info("Inside NoButtonAction method Start");
        String retString;
        JSONObject jsonInput = new JSONObject(sessionScopeParam.get("sRetailOnboardJson").toString());
        
        jsonInput.put("UCMId","");
        jsonInput.put("ActionType","NewContact");
        
        retString = retailDetailAction(jsonInput);
        return retString;
    }

    public String retailDetailAction(JSONObject jsonInput)
    {
            String retString = "ToConfirmation";
            java.util.List lst = new java.util.ArrayList();
            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue("ONBOARD_CUSTOMER_RETAIL"));
            lst.add(1, jsonInput);

            OperationBinding opRC = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            opRC.getParamsMap().put("inputList", lst);
            opRC.getParamsMap().put("method", EPICConstant.ONBOARD_CUSTOMER_RETAIL);
            opRC.execute();
            JSONObject jsonResponse = new JSONObject();
            java.util.List ReturnList = (java.util.List) opRC.getResult();
            if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                                  .toString()
                                                                                  .equalsIgnoreCase("true"))) {
                log.info("ReturnList Value at 0 index is " + ReturnList.get(0));
                jsonResponse = new JSONObject(ReturnList.get(1).toString());
                
            } else if ((ReturnList != null && ReturnList.get(0) != null) &&
                       (ReturnList.get(0).toString().equalsIgnoreCase("false") &&
                        ReturnList.size() > 1)) {
                jsonResponse = new JSONObject(ReturnList.get(1).toString());
                pageflowParam.put("pValidCodeRetailDetails",
                                  jsonResponse.isNull(EPICConstant.ERROR_CODE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_CODE));
                pageflowParam.put("pValidMsgRetailDetails",
                                  jsonResponse.isNull(EPICConstant.ERROR_MESSAGE) ? null :
                                  jsonResponse.getString(EPICConstant.ERROR_MESSAGE));
                if (pageflowParam.get("pValidCodeRetailDetails") != null && pageflowParam.get("pValidCodeRetailDetails")
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
                log.info("Some Error in WebService Response");
                retString = "ERROR";
                return retString;
            }
            /**----------------Setting Confirmation Message for Confirmation Page--------------**/
            confirmationPageMsg = CommonHelper.getValueFromRsBundle("THANK_YOU_MESSAGE_XTRA_REWARDS_DETAILS");
            return retString;
        
        }
    public void setDupUCMIdBinding(RichOutputText dupUCMIdBinding) {
        this.dupUCMIdBinding = dupUCMIdBinding;
    }

    public RichOutputText getDupUCMIdBinding() {
        return dupUCMIdBinding;
    }

    public void setFirstNameBinding(RichInputText firstNameBinding) {
        this.firstNameBinding = firstNameBinding;
    }

    public RichInputText getFirstNameBinding() {
        return firstNameBinding;
    }

    public void setLastNameBinding(RichInputText lastNameBinding) {
        this.lastNameBinding = lastNameBinding;
    }

    public RichInputText getLastNameBinding() {
        return lastNameBinding;
    }

    public void setMobileBinding(RichInputText mobileBinding) {
        this.mobileBinding = mobileBinding;
    }

    public RichInputText getMobileBinding() {
        return mobileBinding;
    }

    public void setMobileErrorMsg(String mobileErrorMsg) {
        this.mobileErrorMsg = mobileErrorMsg;
    }

    public String getMobileErrorMsg() {
        return mobileErrorMsg;
    }
}
