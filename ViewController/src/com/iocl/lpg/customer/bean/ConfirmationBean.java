package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.utils.CommonHelper;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import java.io.Serializable;
public class ConfirmationBean implements Serializable{

    private static String conf_message;

    public static void setConf_message(String conf_message) {
        ConfirmationBean.conf_message = conf_message;
    }

    public static String getConf_message() {
        return conf_message;
    }
    private String confirmationMessage;
    private RichOutputText confirmationMsgBind;

    public ConfirmationBean() {
        super();
    }

    public ConfirmationBean(String msg) {
        super();
        System.out.println("msg  :" + msg);
        //        this.setConf_message(msg);
        this.setConfirmationMessage(msg);


    }

    public void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public void setConfirmationMsgBind(RichOutputText confirmationMsgBind) {
        this.confirmationMsgBind = confirmationMsgBind;
    }

    public RichOutputText getConfirmationMsgBind() {
        return confirmationMsgBind;
    }

    public String backActionRefreshCall() {
        //        String target = null;
        //        if (ADFContext.getCurrent()
        //                               .getPageFlowScope()
        //                               .get("destination") != null) {
        //            target = ADFContext.getCurrent()
        //                               .getPageFlowScope()
        //                               .get("destination")
        //                               .toString();
        //        }
        CommonHelper.refreshPage();
        return "back";
    }

    public String confirmBackAction() {
        // Add event code here...
        java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
        String backAction = null;
        if (pageflowParam.get("destination") != null) {
            backAction = ADFContext.getCurrent()
                                   .getPageFlowScope()
                                   .get("destination")
                                   .toString();
        }

        if (pageflowParam.get("navigation") != null && pageflowParam.get("navigation")
                                                                    .toString()
                                                                    .equalsIgnoreCase("external")) {
            CommonHelper.refreshPage();
            ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
            try {
                exct.redirect(backAction);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return backAction;
    }
}
