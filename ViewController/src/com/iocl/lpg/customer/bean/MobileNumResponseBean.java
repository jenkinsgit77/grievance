package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class MobileNumResponseBean {
    private static Logger log ;
    java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
    private String url;
    public MobileNumResponseBean() {
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(MobileNumResponseBean.class);

        } else {
            log = Logger.getLogger(MobileNumResponseBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }

    public void fetchMobileNum() {
        // Add event code here...
        String  mobileNum=null;
        
        
        String mobNum = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("MN");
        
        if(mobNum!=null){
               mobileNum=mobNum.trim();
               log.info("mobileNum value"+mobileNum);
               param.put("pmobileNum", mobileNum);
           
           }
        url = EPICIOCLResourceCustBundle.findKeyValue("MOBNUM_RESPONSE_URL") + mobileNum;
        
      
       
            String openURL = "window.location.replace(\"" + url + "\");";
            CommonHelper.runJavaScript(openURL);
            
      
    
//        FacesContext fctx = FacesContext.getCurrentInstance();
//        ExtendedRenderKitService erks =
//        Service.getRenderKitService(fctx,
//        ExtendedRenderKitService.class);
//            
//           StringBuilder script = new StringBuilder();  
//        
//               script.append("window.replace(\""+url+"\");");  
//                     erks.addScript(FacesContext.getCurrentInstance(),   
//                                    script.toString()); 
//                              

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
