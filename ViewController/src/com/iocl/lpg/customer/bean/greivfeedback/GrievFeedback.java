package com.iocl.lpg.customer.bean.greivfeedback;

import com.iocl.lpg.customer.bean.attachment.CustomerAddAttachment;

import java.io.Serializable;
import com.iocl.lpg.customer.bean.selectAccount.ShowRelationPopup;
import com.iocl.lpg.customer.utils.CommonHelper;

import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

public class GrievFeedback implements Serializable{
    @SuppressWarnings("compatibility:3929139922440535786")
    private static final long serialVersionUID = 1L;

    public GrievFeedback() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(GrievFeedback.class);

        } else {
            log = Logger.getLogger(GrievFeedback.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    private static Logger log ;

    public String moveToGrievance() {
        log.info("Inside moveToGrievance Start");
        String trimURL = EPICIOCLResourceCustBundle.findKeyValue("GRIEV_PORTAL_FEEDBACK");
//        String openURL = "window.location.replace(\"" + trimURL + "\");";
//        CommonHelper.runJavaScript(openURL);
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(trimURL);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "externalNav";
    }
}
