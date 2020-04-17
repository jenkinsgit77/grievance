package com.iocl.lpg.customer.bean.servo;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

public class FindYourOilLanding {
    private String destinationUrl="https://www.google.com";
    public FindYourOilLanding() {
    }

    public String getKMBaseUrl(){
        String url=EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.KM_FIND_YOUR_OIL_SEARCH);
        return url;
    }

    public String getSearchLubesAction() {
        // Add event code here...
        String kmUrl=null;
        String url=EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.KM_FIND_YOUR_OIL_SEARCH); 
        String vehicleType=null;
        String make=null;
        String model=null;
        //#{bindings.VehicleType.inputValue}
        //#{bindings.Make.inputValue}
        if (CommonHelper.evaluateEL("#{bindings.VehicleType.inputValue}")!=null)
        {
            vehicleType=CommonHelper.evaluateEL("#{bindings.VehicleType.inputValue}").toString();
        }
        if(CommonHelper.evaluateEL("#{bindings.Make.inputValue}")!=null){
            make=CommonHelper.evaluateEL("#{bindings.Make.inputValue}").toString();
        }
        
        if(CommonHelper.evaluateEL("#{bindings.Model.inputValue}")!=null){
            model=CommonHelper.evaluateEL("#{bindings.Model.inputValue}").toString();
        }
        String exUrl="&page=answers&startover=y&question_box_status=changed&restrict=&question_box=";
        System.out.println("url :"+url+"vehicleType :"+vehicleType+" "+"exUrl : "+exUrl +"make : "+make+"-"+"model : "+model);
        kmUrl=url + vehicleType + exUrl + make + "-" + model; 
        System.out.println("kmUrl : "+kmUrl);
        return kmUrl;
    }
        
    public void serviceCall(){
        
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }
}
