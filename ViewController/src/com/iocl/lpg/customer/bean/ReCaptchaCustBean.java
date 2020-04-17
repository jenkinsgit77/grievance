package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.utils.CommonHelper;

import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.IOException;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

public class ReCaptchaCustBean implements Serializable{
    
    public ReCaptchaCustBean() {
    }
    
    private RichOutputText outputTextCaptchaBind;
        
    private String errorCapcha="";
    
    private RichPanelGroupLayout recaptchaBind;
    
    public void setErrorCapcha(String errorCapcha) {
        this.errorCapcha = errorCapcha;
    }

    public String getErrorCapcha() {
        return errorCapcha;
    }
    
    public String getSiteKey() {
        return EPICIOCLResourceCustBundle.findKeyValue("G_CAPACHA_SITE_KEY");
    }
    
    
    public String reCaptchaSubmitAction() {
        // Add event code here...
            String submitAction=null;
            boolean isError = false;
            String returnBNDLString = null;
            String gRecaptchaResponse = FacesContext.getCurrentInstance()
                                                    .getExternalContext()
                                                    .getRequestParameterMap()
                                                    .get("g-recaptcha-response");
            System.out.println("gRecaptchaResponse  "+gRecaptchaResponse);

        try {
        isError = checkValidation(isError, gRecaptchaResponse);
        if (!isError){
            ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
            
                exct.redirect("/webcenter/portal/Customer/pages_downloadformats");
            
        }
        }catch (IOException e) {
                e.printStackTrace();
        }catch (Exception e) {
        // TODO: Add catch code
        e.printStackTrace();
        }    
        return submitAction;
    }
            
    private boolean checkValidation(boolean isError, String gRecaptchaResponse) {
        System.out.println("gRecaptchaResponse  "+gRecaptchaResponse);
        cleanValidationErrors();
        System.out.println("gRecaptchaResponse  "+gRecaptchaResponse);
        if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
                   errorCapcha = CommonHelper.getValueFromRsBundle("ENTER_RECAPTCHA");
                   outputTextCaptchaBind.setValue(errorCapcha);
                   isError = true;
               }
    //        if (StringUtils.isBlank(gRecaptchaResponse)) {
    //            errorCapcha = CommonHelper.getValueFromRsBundle("PLZ_SEL_CAPA");
    //
    //            recaptchaBind.setStyleClass(EPICConstant.ERROR_CLASS);
    //            isError = true;
    //        }
        return isError;
    }
            
    public void cleanValidationErrors(){
        errorCapcha = "";
        outputTextCaptchaBind.setValue("");
    }



    
    public void setRecaptchaBind(RichPanelGroupLayout recaptchaBind) {
        this.recaptchaBind = recaptchaBind;
    }

    public RichPanelGroupLayout getRecaptchaBind() {
        return recaptchaBind;
    }

    public void setOutputTextCaptchaBind(RichOutputText outputTextCaptchaBind) {
        this.outputTextCaptchaBind = outputTextCaptchaBind;
    }

    public RichOutputText getOutputTextCaptchaBind() {
        return outputTextCaptchaBind;
    }
}
