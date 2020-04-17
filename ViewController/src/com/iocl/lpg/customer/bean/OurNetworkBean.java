package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;
//import javax.json.JsonObject;

public class OurNetworkBean implements Serializable{
    
    @SuppressWarnings("compatibility:1699335356781066117")
        
    private static final long serialVersionUID = 1L;
    private RichTable mapTableBinding;
    private RichOutputText jsonValueBinding;

    public OurNetworkBean() {
    }

    

    

    public void setMapTableBinding(RichTable mapTableBinding) {
        this.mapTableBinding = mapTableBinding;
    }

    public RichTable getMapTableBinding() {
        return mapTableBinding;
    }
    
    private String jsonStringLocations;


    private java.util.List locationList = new java.util.ArrayList();

    public void setLocationList(List locationList) {
        this.locationList = locationList;
    }

    public List getLocationList() {
        return locationList;
    }

    private JSONObject jsonLocationsInput = new JSONObject();

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
    
    private JSONArray locationJsonArray = new JSONArray(); 
    
    public String getSiteKey() {
        return EPICIOCLResourceCustBundle.findKeyValue("G_CAPACHA_SITE_KEY");
    }
    
    
    public ArrayList getLocationDetails(){
        //        JsonObject json=
        DCBindingContainer bindings1 = (DCBindingContainer) BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dbcIterator = bindings1.findIteratorBinding("locationMapVo1Iterator");
        ViewObject vo =dbcIterator.getViewObject();
        dbcIterator.executeQuery();
        Row[] rr=vo.getFilteredRows("Area", "Noida");
    //        Row[] rr=vo.getAllRowsInRange();
        System.out.println("rr.length "+rr.length);
        if(rr.length>0){
            
            for (int i=0 ; i<rr.length; i++) 
            {
                JSONObject jsonLocationsRowInput = new JSONObject();
                System.out.println(rr[i]);
                locationList.add(rr[i]);
                System.out.println("Category"+rr[i].getAttribute("Distributorname").toString());
                System.out.println("Area"+rr[i].getAttribute("Area").toString());
                System.out.println("Location Name"+rr[i].getAttribute("LocationName").toString());
                System.out.println("Longitude "+rr[i].getAttribute("Latitude").toString());
                System.out.println("Latitude "+rr[i].getAttribute("Longitude").toString());
                jsonLocationsRowInput.put("Sno",rr[i].getAttribute("Sno").toString());
                jsonLocationsRowInput.put("Category",rr[i].getAttribute("Distributorname").toString());
                jsonLocationsRowInput.put("Area",rr[i].getAttribute("Area").toString());
                jsonLocationsRowInput.put("LocationName",rr[i].getAttribute("LocationName").toString());
                jsonLocationsRowInput.put("Latitude",rr[i].getAttribute("Latitude").toString());
                jsonLocationsRowInput.put("Longitude",rr[i].getAttribute("Longitude").toString());
                locationJsonArray.put(jsonLocationsRowInput);
                
            }
            
        }
        ArrayList locations=new ArrayList();
        jsonLocationsInput.put("LocationDetails", locationJsonArray);
        CommonHelper.setEL("#{pageFlowScope.dffd}",jsonLocationsInput);
        System.out.println("jsonLocationsInput "+jsonLocationsInput);
        System.out.println("jsonStringLocations  "+jsonStringLocations);
        
        return locations;
    }


    public String searchAction() {
       
        getLocationDetails();
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
        getLocationDetails();
        return null;
    }

    public void setJsonValueBinding(RichOutputText jsonValueBinding) {
        this.jsonValueBinding = jsonValueBinding;
    }

    public RichOutputText getJsonValueBinding() {
        return jsonValueBinding;
    }

    public String closeDirectionAction() {
        // Add event code here...
        
        return null;
    }


}
