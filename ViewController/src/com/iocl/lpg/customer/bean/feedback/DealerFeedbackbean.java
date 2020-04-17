package com.iocl.lpg.customer.bean.feedback;

import com.iocl.lpg.customer.utils.CommonHelper;

import com.iocl.lpg.customer.utils.EPICConstant;

import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;

import javax.faces.model.SelectItem;

import oracle.adf.share.ADFContext;

import oracle.adf.view.faces.bi.component.gauge.UIRatingGauge;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.nav.RichButton;
import oracle.adf.view.rich.component.rich.output.RichOutputFormatted;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.OperationBinding;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class DealerFeedbackbean implements Serializable{
    @SuppressWarnings("compatibility:-1413063237425607037")
    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(DealerFeedbackbean.class);
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
    private  Integer overallRatingvalue=0;
    private  Integer cleanlinessRatingvalue=5;
     private  Integer facilitiesRatingvalue=5;
  private  Integer cleanToiletRatingvalue=5;
 private  Integer serviceRatingvalue=5;
  private  Integer  quantityRatingvalue=5;
   private  Integer productQualityRatingvalue=5;
    private String genericErrMsg ;
    private boolean errorInDetails;
    private UIRatingGauge cleanlinessdvtBind;
    private UIRatingGauge facilitiesdvtBind;
    private UIRatingGauge cleanToiletdvtBind;
    private UIRatingGauge servicedvtBind;
    private UIRatingGauge quantitydvtBind;
    private RichOutputFormatted productQualitybind;
    private UIRatingGauge productQualitydvtbind;
    private RichButton submitDealerbttnBinding;
    private String errorCapcha;

    public void setErrorCapcha(String errorCapcha) {
        this.errorCapcha = errorCapcha;
    }

    public String getErrorCapcha() {
        return errorCapcha;
    }

    public DealerFeedbackbean() {
    }

    public void fetchDealerCode() {
        // Add event code here...        
        String dealerCode=null;
        String trimmeddealerCode=null;
        String errorSAPDEALERNOTFound="";
        String retString="";
       
        dealerCode = FacesContext.getCurrentInstance()
                                     .getExternalContext()
                                     .getRequestParameterMap()
                                     .get("dc");
        
        
        param.put("dcNotExist","");
               
                try{
        if(dealerCode!=null && !StringUtils.isBlank(dealerCode) ){
                trimmeddealerCode=dealerCode.trim();
                log.info("pdealerCode value"+trimmeddealerCode);
            param.put("pdealerCode", trimmeddealerCode);
            param.put("serviceRatingvalue", 5);
            param.put("cleanlinessRatingvalue", 5);
            param.put("cleanToiletRatingvalue", 5);
            param.put("facilitiesRatingvalue", 5);
            param.put("quantityRatingvalue", 5);
            param.put("productQuantityRatingval",5);
            
            JSONObject jsonInputSAPCode = new JSONObject();
                            log.info("input==>" + jsonInputSAPCode.toString());
                         
                OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                       java.util.List lst = new java.util.ArrayList();
                jsonInputSAPCode.put("DealerCode", dealerCode);
                jsonInputSAPCode.put("LOB",  "Petrol Pump");
                
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
                if (result!=null && result.size() > 0) {
                    if ((result != null) && (result.get(0) != null) &&
                        result.get(0).toString().equalsIgnoreCase(EPICConstant.TRUE_VAl)){
                JSONObject jsonObject = (JSONObject) retValue.get(1);
                if(jsonObject.has("DealerDetails")){
            JSONArray partnerDtlsAr=jsonObject.getJSONArray("DealerDetails");
            for (int i = 0; i < partnerDtlsAr.length(); ++i) {
                JSONObject rec = partnerDtlsAr.getJSONObject(i);
             
                String partnerName = rec.isNull("PartnerName")?"":rec.getString("PartnerName");
             String   stateRetail= rec.isNull("PartnerState")?"":rec.getString("PartnerState");
             String   districtRetail= rec.isNull("PartnerDistrict")?"":rec.getString("PartnerDistrict");
               param.put("poutputDealer",partnerName);
                param.put("outputSAPState",stateRetail);
                param.put("outputSAPDistrict",districtRetail);
                break;
            }
            }
                
                        }
                
             
                else  if (result.get(0)==null || result.get(0)
                                                               .toString()
                                                               .equalsIgnoreCase("False") &&  retValue.get(1)!=null) {
                    JSONObject jsonObject = (JSONObject) retValue.get(1);
               if(!jsonObject.isNull("ErrorCode") &&
                    jsonObject.getString(EPICConstant.ERROR_CODE).equalsIgnoreCase(EPICConstant.NOT_FOUND_CASE)){
                   
                    errorSAPDEALERNOTFound=CommonHelper.getValueFromRsBundle("NO_DISTBT_FOUND_SAP");
                    param.put("pSAPnotFound",errorSAPDEALERNOTFound);
                        log.info("error message value"+errorSAPDEALERNOTFound);
                        param.put("partnerNF","Y");
                       
                    }
                }
                }
            else {
                param.put("dcNotExist","Y");
            }
            }
                else {
                    param.put("dcNotExist","Y");
                }
            }
            catch (Exception e) {
            e.printStackTrace();
            
            }
    }
    
    
    public String onSubmitDealerFeedback() {
        String retString ="toconfirm"; 
        
        String dealerCode ="";
        String quantityDel="";
        String service="";
        String cleanliness="";
        String cleanToilet="";
        String facilities="";
        String  pFlowFrom=null;
        
            String gRecaptchaResponse = FacesContext.getCurrentInstance()
                                                    .getExternalContext()
                                                    .getRequestParameterMap()
                                                    .get("g-recaptcha-response");
            errorInDetails = ValidateFeedback(gRecaptchaResponse);
       
   
     
        if (errorInDetails == true) {
            log.info("all questions not answered");
            return null;
        }
        dealerCode = (String)param.get("pdealerCode");
        log.info("pdealerCode value"+dealerCode);
//        if(!StringUtils.isEmpty((String)quantityDeliveredSOC.getValue())){
//         quantityDel=(String)quantityDeliveredSOC.getValue();
//        }
//        else {
//            
//                 quantityDel="NA";
//            }
//        
//        if(!StringUtils.isEmpty((String)serviceSOC.getValue())){
//         service=(String)serviceSOC.getValue();
//        }
//        else {
//            
//                 service="NA";
//            }
//        if(!StringUtils.isEmpty((String)cleanlinessSOC.getValue())){
//         cleanliness=(String)cleanlinessSOC.getValue();
//        }
//        else {
//            
//                 cleanliness="NA";
//            }
//        if(!StringUtils.isEmpty((String)cleanToiletSOC.getValue())){
//         cleanToilet=(String)cleanToiletSOC.getValue();
//        }
//        else {
//            
//                 cleanToilet="NA";
//            }
//        if(!StringUtils.isEmpty((String)facilitiesSOC.getValue())){
//         facilities=(String)facilitiesSOC.getValue();
//        }
//        else {
//            
//                 facilities="NA";
//            }
//       
//        log.info("values of quantityDel::" +quantityDel);
//        log.info("values of service  ::" +service);
//        log.info("values of cleanliness::" +cleanliness);
//        log.info("values of cleanToilet::" +cleanToilet);
//        log.info("values of facilities::" +facilities);
        
        String quantityDelOF=(String)quantityDelivered.getValue();
        String serviceOF=(String)serviceBind.getValue();
        String cleanlinessOF=(String)cleanlinessBind.getValue();
        String cleanToiletOF=(String)cleantoiletBind.getValue();
        String facilitiesOF=(String)facilitiesBind.getValue();
        String productQualOF=(String)productQualitybind.getValue();
        log.info("values of quantityDelOF::" +quantityDelOF);
        log.info("values of serviceOF  ::" +serviceOF);
        log.info("values of cleanlinessOF::" +cleanlinessOF);
        log.info("values of cleanToiletOF::" +cleanToiletOF);
        log.info("values of facilitiesOF::" +facilitiesOF);
        log.info("values of productQualOF::" +productQualOF);
        
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
                    jsonInputChild1.put("OverallRating",param.get("overallRatingvalue")); 
            jsonInputChild1.put("ProductQuality",param.get("productQuantityRatingval")); 
            jsonInputChild1.put("QuantityDelivered",param.get("quantityRatingvalue")); 
            jsonInputChild1.put("Service",param.get("serviceRatingvalue")); 
            jsonInputChild1.put("Cleanliness",param.get("cleanlinessRatingvalue")); 
            jsonInputChild1.put("Facilities",param.get("facilitiesRatingvalue")); 
            jsonInputChild1.put("CleanToilet",param.get("cleanToiletRatingvalue")); 
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
                    if (result!=null && result.size() > 0) {
                        if ((result != null) && (result.get(0) != null) &&
                            result.get(0).toString().equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                            JSONObject jsonObject = (JSONObject) retValue.get(1);
                            if (!jsonObject.isNull("ErrorCode") &&
                                jsonObject.getString(EPICConstant.ERROR_CODE).equalsIgnoreCase("0")) {
              
               return retString;
                }
                else {
                   
                    retString="ERROR";
                    return retString;
                }                
                }
                        else  if (result.get(0)==null || result.get(0)
                                                                       .toString()
                                                                       .equalsIgnoreCase("False") &&  retValue.get(1)!=null) {
                              JSONObject jsonObject = (JSONObject) retValue.get(1);
                              if (!jsonObject.isNull("ErrorCode") &&
                                  jsonObject.getString(EPICConstant.ERROR_CODE).equalsIgnoreCase(EPICConstant.SBL100)) {
                              String dealerFeedback =
                                  jsonObject.isNull(EPICConstant.ERROR_MESSAGE) ? "" :
                                  jsonObject.getString(EPICConstant.ERROR_MESSAGE);
                              param.put("perrorValidationMessage", dealerFeedback);
                              param.put("perrorValidationCode", EPICConstant.SBL100);
                              return null;
                          } 
                          else {
                              
                               retString = "ERROR";
                               return retString;
                          }
                        }
                        else {
                           
                            retString="ERROR";
                            return retString;
                        } 
                    }
        }catch(Exception dealerFeedback) {
                  
                        CommonHelper.setEL("#{pageFlowScope.epicSibelOrAppErrorCode}", EPICConstant.OTH2);
                    log.error("Exception is:"+dealerFeedback);
                    retString="ERROR";
                    
                    
                    }
            
        return retString;
    }
    
    
    public void overallRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try
        {
        log.info("Inside GetRating");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
            overallRatingvalue=i.intValue();
            param.put("overallRatingvalue", overallRatingvalue);
            log.info("overallRatingvalue"+overallRatingvalue);
        }
        
        }
        catch(Exception e) {
            e.printStackTrace();
        } 
    }
    public void cleanlinessRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try
        {
        log.info("Inside cleanlinessRatingVC");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
            cleanlinessRatingvalue=i.intValue();
            param.put("cleanlinessRatingvalue", cleanlinessRatingvalue);
            log.info("cleanlinessRatingvalue"+cleanlinessRatingvalue);
        }
        
        }
        catch(Exception e) {
            e.printStackTrace();
        } 
    }
    
    public void facilitiesRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try
        {
        log.info("Inside facilitiesRatingVC");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
            facilitiesRatingvalue=i.intValue();
            param.put("facilitiesRatingvalue", facilitiesRatingvalue);
            log.info("facilitiesRatingvalue"+facilitiesRatingvalue);
        }
        
        }
        catch(Exception e) {
            e.printStackTrace();
        } 
    }
    
    public void cleanToiletRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try
        {
        log.info("Inside cleanToiletRatingVC");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
            cleanToiletRatingvalue=i.intValue();
            param.put("cleanToiletRatingvalue", cleanToiletRatingvalue);
            log.info("cleanToiletRatingvalue"+cleanToiletRatingvalue);
        }
        
        }
        catch(Exception e) {
            e.printStackTrace();
        } 
    }
    
    public void serviceRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try
        {
        log.info("Inside serviceRatingVC");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
           serviceRatingvalue=i.intValue();
            param.put("serviceRatingvalue", serviceRatingvalue);
            log.info("serviceRatingvalue"+serviceRatingvalue);
        }
        
        }
        catch(Exception e) {
            e.printStackTrace();
        } 
    }
    public void quantityRatingVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try
        {
        log.info("Inside quantityRatingVC");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
          quantityRatingvalue=i.intValue();
            param.put("quantityRatingvalue", quantityRatingvalue);
            log.info("quantityRatingvalue"+quantityRatingvalue);
        }
        
        }
        catch(Exception e) {
            e.printStackTrace();
        } 
    }
    public void productQualityVC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        try
        {
        log.info("Inside productQualityVC");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
          productQualityRatingvalue=i.intValue();
            param.put("productQuantityRatingval", productQualityRatingvalue);
            log.info("productQuantityRatingval"+productQualityRatingvalue);
        }
        
        }
        catch(Exception e) {
            e.printStackTrace();
        } 
    }
    
    public Boolean ValidateFeedback(String gRecaptchaResponse) {
        errorInDetails=false;
        genericErrMsg="";
        if (StringUtils.isBlank(gRecaptchaResponse)) {
            errorCapcha = CommonHelper.getValueFromRsBundle("PLZ_SEL_CAPA");
            errorInDetails = true;
        }
        if( param.get("overallRatingvalue")==null  ){
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
    public String getSiteKey() {
        return EPICIOCLResourceCustBundle.findKeyValue("G_CAPACHA_SITE_KEY");
    }
}
