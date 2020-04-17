package com.iocl.lpg.customer.bean.custNonlogin;

import com.iocl.lpg.customer.imageSliderView.view.TestBean;
import com.iocl.lpg.customer.utils.CommonHelper;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;

import org.apache.log4j.Logger;

public class FetchIconsGenericBean {
    private static Logger logger = Logger.getLogger(FetchIconsGenericBean.class);
    public FetchIconsGenericBean() {
    }

   
    public void customerUnsecuredActionList(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside customerUnsecuredActionLis method in FetchIconsGenericBean");
        if (ADFContext.getCurrent().getRequestScope().get("ActionName") != null) {
        }
        customerNavigate();
    }
    
    public void customerNavigate() {
        CommonHelper.refreshPage();
        String destination = "/webcenter";
        String appPath = null;
        if (ADFContext.getCurrent()
                      .getRequestScope()
                      .get("ActionName") != null) {
            appPath = ADFContext.getCurrent()
                                .getRequestScope()
                                .get("ActionName")
                                .toString();
            logger.info("appPath from db is: " + appPath);
        }
        destination = destination + appPath;
        logger.info("final destination app Path is: " + destination);

        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(destination);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    
    public String unsecuredTFActions() {
        // Add event code here...
        String outcome=null;
        logger.info("Inside unsecuredTFActions method in FetchIconsGenericBean");
        if (ADFContext.getCurrent().getRequestScope().get("ActionName") != null) {
            if(ADFContext.getCurrent().getRequestScope().get("ActionName").toString().equalsIgnoreCase("toRecaptcha")){
                outcome=ADFContext.getCurrent().getRequestScope().get("ActionName").toString();
            }else{
                customerNavigate();
            }
        }
        logger.info("appPath from db is: " + outcome);
        CommonHelper.refreshPage();
        return outcome;
    }

   
}
