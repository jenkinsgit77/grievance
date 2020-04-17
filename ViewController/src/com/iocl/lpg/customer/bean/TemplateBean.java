package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.share.ADFContext;

import org.apache.log4j.Logger;

public class TemplateBean implements Serializable{
    public TemplateBean() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            logger = Logger.getLogger(TemplateBean.class);

        } else {
            logger = Logger.getLogger(TemplateBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    private static Logger logger = Logger.getLogger(TemplateBean.class);
    java.util.Map sessionscope = ADFContext.getCurrent().getSessionScope();
    private String showMarqee;
    private String marqeeMessage;

    public void setShowMarqee(String showMarqee) {
        this.showMarqee = showMarqee;
    }

    public String getShowMarqee() {
        showMarqee= EPICIOCLResourceCustBundle.findKeyValue("SHOW_MARQUEE_NOFIFY")==null?"":EPICIOCLResourceCustBundle.findKeyValue("SHOW_MARQUEE_NOFIFY");
        return showMarqee;
    }

    public void setMarqeeMessage(String marqeeMessage) {
        this.marqeeMessage = marqeeMessage;
    }

    public String getMarqeeMessage() {
        marqeeMessage=EPICIOCLResourceCustBundle.findKeyValue("MARQUEE_MESSAGE")==null?"":EPICIOCLResourceCustBundle.findKeyValue("MARQUEE_MESSAGE");
        return marqeeMessage;
    }

    public void goToLPGSection(ActionEvent actionEvent) {

        logger.info("Inside goToLPGSection method in TemplateBean");
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("CUSTOMER_LPG_PAGE_PATH"));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToCustomer360Profile(ActionEvent actionEvent) {

        logger.info("Inside goToCustomer360Profile method in TemplateBean");
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            sessionscope.put("sDestinationTo", "CustomerProfilePage");
            sessionscope.put("redirectCheck", "myprofile");
         //   exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("CUSTOMER_LPG_PAGE_PATH"));           
            exct.redirect("/webcenter/portal/Customer/pages_profile360");
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToLPGPortal(ActionEvent actionEvent) {

        logger.info("Inside goToLPGPortal method in TemplateBean");

        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("LPG_DASHBOARD_PATH"));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void goToOverviewSection(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside goToOverviewSection method in TemplateBean");
        sessionscope.put("currentTabInCustomer","overview");
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("CUSTOMER_LANDING_PAGE_PATH"));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void clickSignOut(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside clickSignOut method");
        java.util.Map paramSesion = ADFContext.getCurrent().getSessionScope();
        FacesContext fctx = FacesContext.getCurrentInstance();  
        ExternalContext ectx = fctx.getExternalContext();  
        HttpSession session = (HttpSession)ectx.getSession(false);  
        session.invalidate(); 
        ExternalContext exct=FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("SIGN_OUT_URL")); 
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            logger.info("Error-Occured");
            e.printStackTrace();
        }
        paramSesion.clear();
    }

    public void goToustomernonloginHP(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside goToustomernonloginHP method in TemplateBean");
        String loggedIn = null;
        if (sessionscope.get(EPICConstant.SESSION_IS_LOGGED_IN) != null) {
            loggedIn = sessionscope.get(EPICConstant.SESSION_IS_LOGGED_IN).toString();
        }
        CommonHelper.refreshPage();
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {

            if (loggedIn != null && loggedIn.equalsIgnoreCase(EPICConstant.IS_LOGGED_IN_VAL)) {
                sessionscope.put("currentTabInCustomer", "overview");
                exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("CUSTOMER_LANDING_PAGE_PATH"));
            } else {
                exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("CUSTOMER_NON_LOGGEDIN_PATH"));
            }
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToRegister(ActionEvent actionEvent) {

        logger.info("Inside goToRegister method in TemplateBean");
    //        CommonHelper.refreshPage();
        logger.info("CUSTOMER_REGISTER_LOGIN"+EPICIOCLResourceCustBundle.findKeyValue("CUSTOMER_REGISTER_LOGIN"));
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {     
            exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("CUSTOMER_REGISTER_LOGIN"));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }
    
    
    public void navigationListener(ActionEvent actionEvent) {
        logger.info(" navigationListener start::" + actionEvent.getComponent()
                                                                    .getAttributes()
                                                                    .get("node"));
//        String actionValue = actionEvent.getComponent()
//                                        .getAttributes()
//                                        .get("node")
//                                        .toString();
//        String actionValuelink = actionEvent.getComponent()
//                                            .getAttributes()
//                                            .get("golinkurl")
//                                            .toString();
//        String prettylink = actionEvent.getComponent()
//                                            .getAttributes()
//                                            .get("prettyLink")
//                                            .toString();
        
        java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
        String goPrettyLink = String.valueOf(pageflowParam.get("golinkurl"));

        if (goPrettyLink != null) {
                logger.info("Inside direct link::"+goPrettyLink);
                try {
                    FacesContext fctx = FacesContext.getCurrentInstance();
                    ExternalContext ectx = fctx.getExternalContext();
                    String prettyUrl = ectx.getRequestContextPath()+goPrettyLink;
                    logger.info("Redirect PrettyURL from hierachy:: " + prettyUrl);
//                    logger.info("DirectLink golink prettyUrl 1 "+nodeDir.getGoLinkPrettyUrl());
                    ectx.redirect(prettyUrl);
                    fctx.responseComplete();
                    fctx.renderResponse();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
    }
    
    public void onClickChangePwd(ActionEvent actionEvent) {

            logger.info("Inside onClickChangePwd method in TemplateBean");
            logger.info("Change password link-->"+EPICIOCLResourceCustBundle.findKeyValue("CHANGE_PASSWORD_LINK"));
            ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
            try {     
                exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("CHANGE_PASSWORD_LINK"));
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    public void goToLube(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside goToLubeSection method in TemplateBean");
        sessionscope.put("currentTabInCustomer","lubes");
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("LUBE_PAGE_PATH"));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToLoyalty(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside goToLoyaltySection method in TemplateBean");
        logger.info("LOYALTY_PAGE_PATH "+EPICIOCLResourceCustBundle.findKeyValue("LOYALTY_PAGE_PATH"));
        sessionscope.put("currentTabInCustomer","loyalty");
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("LOYALTY_PAGE_PATH"));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void goToRetail(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside goToRetailSection method in TemplateBean");
        sessionscope.put("currentTabInCustomer","Retail");
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("RETAIL_PAGE_PATH"));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToRetailOnboarding(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside goToRetailOnboarding method in TemplateBean");       
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(EPICIOCLResourceCustBundle.findKeyValue("RETAIL_ONBOARD_PAGE_PATH"));
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void enrollNowAction(ActionEvent actionEvent) {
        // Add event code here...
        sessionscope.put("loyaltyFlow", "Enrollment");
        String trimURL = "/webcenter/portal/Customer/pages_loyaltyenrichment";
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
        exct.redirect(trimURL);
        FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
        e.printStackTrace();
        }
    }
}
