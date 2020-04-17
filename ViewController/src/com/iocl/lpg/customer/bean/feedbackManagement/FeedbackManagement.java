package com.iocl.lpg.customer.bean.feedbackManagement;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import ioclcommonproj.com.iocl.utils.JSONObject;

import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.share.ADFContext;

import oracle.adf.view.faces.bi.component.gauge.UIRatingGauge;

import oracle.binding.OperationBinding;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class FeedbackManagement {
    private  Integer overallRatingValue=5;
    private  Integer productQualityValue=5;
    private  Integer quantityDeliveredValue=5;
    private  Integer serviceValue=5;
    private  Integer cleanlinessValue=5;
    private  Integer facilitiesValue=5;
    private  Integer cleanToiletValue=5;
    java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
    private static Logger log;
    private UIRatingGauge bindFacilities;
    private UIRatingGauge bindCleanliness;
    private UIRatingGauge bindService;
    private UIRatingGauge bindQuantityDelivered;
    private UIRatingGauge bindProductQuality;
    private UIRatingGauge bindCleanToilet;
    private UIRatingGauge bindOverallRating;

    public FeedbackManagement() {
        String logFlag= EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
           if(logFlag!=null && "Y".equalsIgnoreCase(logFlag))
           {

               log = Logger.getLogger(FeedbackManagement.class);

           }
           else {

               log = Logger.getLogger(FeedbackManagement.class);

               Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);

           }
    }

    public String conSubmitFeedback() {
        
        String errorMessage=null;
        String errorCode=null;
        String retString=null;
         try {
             java.util.List lstInput = new java.util.ArrayList();
             JSONObject jsonInputA = new JSONObject();
             JSONObject jsonInput = new JSONObject();
            
             jsonInput.put("OverallRating",String.valueOf(param.get("overallRatingKey")).equals("null")?5:String.valueOf(param.get("overallRatingKey")));
             jsonInput.put("ProductQuality",String.valueOf(param.get("productQualityValueKey")).equals("null")?5:String.valueOf(param.get("productQualityValueKey")));
             jsonInput.put("QuantityDelivered", String.valueOf(param.get("quantityDeliveredValueKey")).equals("null")?5:String.valueOf(param.get("quantityDeliveredValueKey")));
             jsonInput.put("Service",String.valueOf(param.get("serviceValueKey")).equals("null")?5:String.valueOf(param.get("serviceValueKey")));
             jsonInput.put("Cleanliness",String.valueOf(param.get("cleanlinessValueKey")).equals("null")?5:String.valueOf(param.get("cleanlinessValueKey")));
             jsonInput.put("Facilities",String.valueOf(param.get("facilitiesValueKey")).equals("null")?5:String.valueOf(param.get("facilitiesValueKey")));
             jsonInput.put("CleanToilet",String.valueOf(param.get("cleanToiletValueKey")).equals("null")?5:String.valueOf(param.get("cleanToiletValueKey")));
             jsonInputA.put("FeedbackType", "3");
             jsonInputA.put("LOB", "Retail");
             jsonInputA.put("EPICRetailCustomerContact", jsonInput); 
             jsonInputA.put(EPICConstant.CONSUMERID, param.get("consumerIdkey"));
             jsonInputA.put(EPICConstant.TRACKING_ID, CommonHelper.createUniqueID());
             jsonInputA.put(EPICConstant.SOURCE,"Portal");
             jsonInputA.put("PartnerCode", param.get("partnerCodekey"));
             
             System.out.println("JsonObject"+jsonInputA);                                                          
             lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("RD_FEEDBACK"));
             lstInput.add(1, jsonInputA);
             OperationBinding ob = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
             ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
             ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.GET_CUSTOMER_DETAILS);
             ob.execute();
             java.util.List returnList = (java.util.List) ob.getResult();
             System.out.println("returnList"+returnList);
              if ((returnList != null) && (returnList.get(0) != null) && returnList.get(0).toString().equalsIgnoreCase("TRUE"))
            {
             JSONObject jsonObject =new JSONObject( returnList.get(1).toString());
             errorMessage= jsonObject.getString("ErrorMessage").toString();
             if(errorMessage.equalsIgnoreCase("SUCCESS"))
              param.put("confMessage",CommonHelper.getValueFromRsBundle("FEEDBACK_HEADER"));
             retString="toConfirmation";
             }
         else if((returnList != null) && (returnList.get(0) != null) && returnList.get(0).toString().equalsIgnoreCase("FALSE")) {
               JSONObject jsonObject =new JSONObject( returnList.get(1).toString());
               errorCode=jsonObject.get("ErrorCode").toString();
               if(errorCode.equalsIgnoreCase(EPICConstant.SBL100)) {
                errorMessage= jsonObject.getString("ErrorMessage").toString();
                System.out.println("errorMessage"+errorMessage);
                param.put("errorBusinessValidationMSG",errorMessage);
                param.put("errorBusinessValidationCode",EPICConstant.SBL100);
                
                
                retString=null;    
               }
               else {
                   retString = EPICConstant.ERROR;
               }
               
         }
          else {
              retString = EPICConstant.ERROR;
          }
         
              
         return retString;  
        }
         catch(Exception onclicksubmite) {
             onclicksubmite.printStackTrace();
             retString=EPICConstant.ERROR;
         
         }
         
         
        return retString;
          
        
    }

    public String fetchCustIdfromURL() {
        String returnString=null;
        System.out.println("Inside fetchCustIdfromURL");
        String consumerId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("consumerId");
        String partnerCode =FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("partnerCode");
        System.out.println("consumerId"+consumerId);
        System.out.println("partnerCode"+partnerCode);
        if(consumerId!=null && !StringUtils.isBlank(consumerId))
        {
         if(partnerCode!=null && !StringUtils.isBlank(partnerCode))
         {
         if(consumerId.matches("[0-9]+") && partnerCode.matches("[0-9]+"))
         {
         param.put("consumerIdkey", consumerId);
         param.put("partnerCodekey", partnerCode);
         System.out.println("consumerId"+consumerId);
         System.out.println("partnerCode"+partnerCode);
         returnString="tofeedback";
         }
         else {
            returnString="tobussiness";
            }
         }
         else
         {
             returnString="tobussiness";
         }
        }
        else {
            returnString="tobussiness"; 
        }
        return returnString;
    }

    public void setOverallRatingValue(Integer overallRatingValue) {
        this.overallRatingValue = overallRatingValue;
    }

    public Integer getOverallRatingValue() {
        return overallRatingValue;
    }

    public void setProductQualityValue(Integer productQualityValue) {
        this.productQualityValue = productQualityValue;
    }

    public Integer getProductQualityValue() {
        return productQualityValue;
    }

    public void setQuantityDeliveredValue(Integer quantityDeliveredValue) {
        this.quantityDeliveredValue = quantityDeliveredValue;
    }

    public Integer getQuantityDeliveredValue() {
        return quantityDeliveredValue;
    }

    public void setServiceValue(Integer serviceValue) {
        this.serviceValue = serviceValue;
    }

    public Integer getServiceValue() {
        return serviceValue;
    }

    public void setCleanlinessValue(Integer cleanlinessValue) {
        this.cleanlinessValue = cleanlinessValue;
    }

    public Integer getCleanlinessValue() {
        return cleanlinessValue;
    }

    public void setFacilitiesValue(Integer facilitiesValue) {
        this.facilitiesValue = facilitiesValue;
    }

    public Integer getFacilitiesValue() {
        return facilitiesValue;
    }

    public void setCleanToiletValue(Integer cleanToiletValue) {
        this.cleanToiletValue = cleanToiletValue;
    }

    public Integer getCleanToiletValue() {
        return cleanToiletValue;
    }

    public void setBindFacilities(UIRatingGauge bindFacilities) {
        this.bindFacilities = bindFacilities;
    }

    public UIRatingGauge getBindFacilities() {
        return bindFacilities;
    }

    public void getFacilities(ValueChangeEvent valueChangeEvent) {
        try
        {
        log.info("Inside getFacilities");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
                facilitiesValue=i.intValue();
                param.put("facilitiesValueKey", facilitiesValue);
                log.info("facilitiesValue"+facilitiesValue);
        }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setBindCleanliness(UIRatingGauge bindCleanliness) {
        this.bindCleanliness = bindCleanliness;
    }

    public UIRatingGauge getBindCleanliness() {
        return bindCleanliness;
    }

    public void getCleanliness(ValueChangeEvent valueChangeEvent) {
        try
        {
        log.info("Inside getCleanliness");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
                cleanlinessValue=i.intValue();
                param.put("cleanlinessValueKey", cleanlinessValue);
                log.info("cleanlinessValue"+cleanlinessValue);
        }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setBindService(UIRatingGauge bindService) {
        this.bindService = bindService;
    }

    public UIRatingGauge getBindService() {
        return bindService;
    }

    public void getService(ValueChangeEvent valueChangeEvent) {
        try
        {
        log.info("Inside getService");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
                serviceValue=i.intValue();
                param.put("serviceValueKey", serviceValue);
                log.info("serviceValue"+serviceValue);
        }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setBindQuantityDelivered(UIRatingGauge bindQuantityDelivered) {
        this.bindQuantityDelivered = bindQuantityDelivered;
    }

    public UIRatingGauge getBindQuantityDelivered() {
        return bindQuantityDelivered;
    }

    public void getQuantityDelivered(ValueChangeEvent valueChangeEvent) {
        try
        {
        log.info("Inside getProductQuality");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
                quantityDeliveredValue=i.intValue();
                param.put("quantityDeliveredValueKey", quantityDeliveredValue);
                log.info("quantityDeliveredValue"+quantityDeliveredValue);
        }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setBindProductQuality(UIRatingGauge bindProductQuality) {
        this.bindProductQuality = bindProductQuality;
    }

    public UIRatingGauge getBindProductQuality() {
        return bindProductQuality;
    }

    public void getProductQuality(ValueChangeEvent valueChangeEvent) {
        try
        {
        log.info("Inside getProductQuality");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
                productQualityValue=i.intValue();
                param.put("productQualityValueKey", productQualityValue);
                log.info("productQualityValue"+productQualityValue);
        }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setBindCleanToilet(UIRatingGauge bindCleanToilet) {
        this.bindCleanToilet = bindCleanToilet;
    }

    public UIRatingGauge getBindCleanToilet() {
        return bindCleanToilet;
    }

    public void getCleanToilet(ValueChangeEvent valueChangeEvent) {
        try
        {
        log.info("Inside getCleanToilet");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
                cleanToiletValue=i.intValue();
                param.put("cleanToiletValueKey", cleanToiletValue);
                log.info("cleanToiletValue"+cleanToiletValue);
        }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setBindOverallRating(UIRatingGauge bindOverallRating) {
        this.bindOverallRating = bindOverallRating;
    }

    public UIRatingGauge getBindOverallRating() {
        return bindOverallRating;
    }

    public void getOverallRating(ValueChangeEvent valueChangeEvent) {
        try
        {
        log.info("Inside getOverallRating");    
        if (valueChangeEvent.getNewValue() != null) {
            Double i=Double.parseDouble(valueChangeEvent.getNewValue().toString());
                overallRatingValue=i.intValue();
                param.put("overallRatingKey", overallRatingValue);
                log.info("overallRatingValue"+overallRatingValue);
        }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
