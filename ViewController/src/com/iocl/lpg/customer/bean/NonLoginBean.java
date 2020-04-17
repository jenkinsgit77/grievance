
package com.iocl.lpg.customer.bean;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.render.ClientEvent;

import org.apache.commons.lang3.StringUtils;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import com.iocl.lpg.customer.utils.EPICConstant;
import org.apache.myfaces.trinidad.util.Service;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.IOException;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;

public class NonLoginBean implements Serializable{
    java.util.Map param = ADFContext.getCurrent().getPageFlowScope();

    private RichInputText searchInputtxt;
    
    private String kmurl;

    public void setKmurl(String kmurl) {
        this.kmurl = kmurl;
    }

    public String getKmurl() {
        
     
     
        return kmurl;
      
    }
    
  

    public NonLoginBean() {
    }


    public void setSearchInputtxt(RichInputText searchInputtxt) {
        this.searchInputtxt = searchInputtxt;
    }

    public RichInputText getSearchInputtxt() {
        return searchInputtxt;
    }


  
    public void kmChangeLstr(ValueChangeEvent valueChangeEvent) {
        ExternalContext exct=FacesContext.getCurrentInstance().getExternalContext();
        if(!StringUtils.isBlank((CharSequence)searchInputtxt.getValue())){
        String searchInputVal= searchInputtxt.getValue().toString();
        
        System.out.println("val===="+searchInputVal);
        
        kmurl = EPICIOCLResourceCustBundle.findKeyValue("KM_URL") + searchInputVal;
        param.put("kmval", kmurl);
        }
        this.setKmurl(kmurl);
    }

    public void callKM() {
        // Add event code here...
        ExternalContext exct=FacesContext.getCurrentInstance().getExternalContext();
        if(!StringUtils.isBlank((CharSequence)searchInputtxt.getValue())){
        String searchInputVal= searchInputtxt.getValue().toString();
        
        System.out.println("val===="+searchInputVal);
                FacesContext fctx = FacesContext.getCurrentInstance();
          
        kmurl = EPICIOCLResourceCustBundle.findKeyValue("KM_URL") + searchInputVal;
                ExtendedRenderKitService erks =
                 Service.getRenderKitService(fctx,
                 ExtendedRenderKitService.class);
                        
                       StringBuilder script = new StringBuilder();  
               
                           script.append("window.open(\""+ kmurl+"\");");  
                                 erks.addScript(FacesContext.getCurrentInstance(),   
                                                script.toString());   
               
               //param.put("kmval", kmurl);
            }
    }

    public void submitOnEnter(ClientEvent clientEvent) {
        // Add event code here...
        ExternalContext exct=FacesContext.getCurrentInstance().getExternalContext();
        if(!StringUtils.isBlank((CharSequence)searchInputtxt.getValue())){
        String searchInputVal= searchInputtxt.getValue().toString();
        
        System.out.println("val===="+searchInputVal);
                FacesContext fctx = FacesContext.getCurrentInstance();
          
        kmurl = EPICIOCLResourceCustBundle.findKeyValue("KM_URL") + searchInputVal;
                    ExtendedRenderKitService erks =
                 Service.getRenderKitService(fctx,
                 ExtendedRenderKitService.class);
                        
                       StringBuilder script = new StringBuilder();  
               
                           script.append("window.open(\""+ kmurl+"\");");  
                                 erks.addScript(FacesContext.getCurrentInstance(),   
                                                script.toString()); 
    }
    }
}
