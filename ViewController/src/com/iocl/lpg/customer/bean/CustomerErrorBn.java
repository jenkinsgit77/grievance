package com.iocl.lpg.customer.bean;


import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.Serializable;

import java.util.Map;
import java.util.Properties;

import javax.faces.event.ActionEvent;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import org.apache.log4j.Logger;

public class CustomerErrorBn implements Serializable {
    @SuppressWarnings("compatibility:7149979844210686625")
    private static final long serialVersionUID = 1L;
    private static Logger log;
    private RichPopup errorPopupBn;
    private RichInputText txtErrorBody;
    private String showLink;
    private Boolean showMessage;

    public void setShowLink(String showLink) {
        this.showLink = showLink;
    }

    public String getShowLink() {
        java.util.Map paramSession = ADFContext.getCurrent().getSessionScope();
        if(paramSession.get("SESSION_PORTAP_IOCL_CONFIG")!=null)
        {
        java.util.Map<String,String > mapProp =(Map<String, String>) paramSession.get("SESSION_PORTAP_IOCL_CONFIG");
        if(mapProp!=null && mapProp.get(com.iocl.lpg.customer.utils.EPICConstant.IS_SHOW_EMAIL_LINK)!=null  ) {
            return mapProp.get(EPICConstant.IS_SHOW_EMAIL_LINK).toString();
        }
        
        }
        return showLink;
    }

    public CustomerErrorBn() {
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(CustomerErrorBn.class);

        } else {
            log = Logger.getLogger(CustomerErrorBn.class);
            Logger.getRootLogger().setLevel(org.apache
                                               .log4j
                                               .Level
                                               .OFF);
        }
    }

    public void mailSend(ActionEvent actionEvent) {
        try {
            boolean debug = false;
            String smtpHost = EPICIOCLResourceCustBundle.findKeyValue("MAIL_SMTP_SERVER");
            String smtpPort = EPICIOCLResourceCustBundle.findKeyValue("MAIL_SMTP_SERVER_PORT");
            String from = EPICIOCLResourceCustBundle.findKeyValue("MAIL_FROM");
            String recipients = EPICIOCLResourceCustBundle.findKeyValue("MAIL_TO");
            String subject = EPICIOCLResourceCustBundle.findKeyValue("MAIL_SUBJECT");
            String message = EPICIOCLResourceCustBundle.findKeyValue("MAIL_BODY");

            log.info("custumer_mail=smtpHost=" + smtpHost);
            log.info("custumer_mail=smtpPort=" + smtpPort);
            log.info("custumer_mail=from=" + from);
            log.info("custumer_mail=recipients=" + recipients);
            log.info("custumer_mail=subject=" + subject);
            log.info("custumer_mail=message=" + message);

            String mailBodyfromuser = "";
            java.util.Map paramSession = ADFContext.getCurrent().getSessionScope();
                        String tracking_number = paramSession.get("TRACKING_NUMBER_MAIL_PORTAL") == null ? "" : paramSession.get("TRACKING_NUMBER_MAIL_PORTAL").toString();
                        
                        
                        subject=subject+tracking_number;

            if (txtErrorBody != null && txtErrorBody.getValue() != null && txtErrorBody.getValue().toString() != null) {
                mailBodyfromuser = txtErrorBody.getValue().toString();
            }

            message = message + mailBodyfromuser;

            log.info("CUSTOMER_Final mail=subject=" + subject);
            log.info("CUSTOMER_FInal mail=message=" + message);


            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);

            // create some properties and get the default Session
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(debug);

            // create a message
            Message msg = new MimeMessage(session);

            // set the from and to address
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);

            /*InternetAddress[] addressTo = new InternetAddress[recipients.length];
              for (int i = 0; i < recipients.length; i++) {
                  addressTo[i] = new InternetAddress(recipients[i]);
              }
              msg.setRecipients(Message.RecipientType.TO, addressTo);*/
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipients));


            // Setting the Subject and Content Type
            msg.setSubject(subject);
            msg.setContent(message, "text/html");
            Transport.send(msg);
            log.info("Mail sent successfully");
            errorPopupBn.hide();
            setShowMessage(true);
        } catch (Exception e) {
            log.info("Errror in mail send.." + e.getMessage());
            e.printStackTrace();

            log.info("Errror in mial send.." + e);
            errorPopupBn.hide();

        }
    }

    public void setErrorPopupBn(RichPopup errorPopupBn) {
        this.errorPopupBn = errorPopupBn;
    }

    public RichPopup getErrorPopupBn() {
        return errorPopupBn;
    }

    public void setTxtErrorBody(RichInputText txtErrorBody) {
        this.txtErrorBody = txtErrorBody;
    }

    public RichInputText getTxtErrorBody() {
        return txtErrorBody;
    }

    public void setShowMessage(Boolean showMessage) {
        this.showMessage = showMessage;
    }

    public Boolean getShowMessage() {
        return showMessage;
    }
}
