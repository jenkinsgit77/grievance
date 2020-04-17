package com.iocl.lpg.customer.bean;

import com.iocl.customer.model.utils.EPICIOCLResourceModel;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.Serializable;

import oracle.adf.share.ADFContext;

import org.apache.log4j.Logger;

public class PropertYLoadBean implements Serializable{
    private static Logger log ; 
    static java.util.Map paramLoad = ADFContext.getCurrent().getSessionScope();
    
    public PropertYLoadBean() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(PropertYLoadBean.class);

        } else {
            log = Logger.getLogger(PropertYLoadBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }

    public String contentPropReset() {
        log.info("Calling for load property in customer");
        EPICIOCLResourceModel.setContents(null);
        EPICIOCLResourceCustBundle.setContents(null);
        return null;
    }
}
