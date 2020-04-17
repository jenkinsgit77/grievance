package com.iocl.lpg.customer.bean.profileOverview;
import java.io.Serializable;

public class EmailParams implements Serializable{
    @SuppressWarnings("compatibility:-4138663130871965509")
    private static final long serialVersionUID = 1L;

    public EmailParams() {
        super();
    }
    
    private String emailId;
    private String emailRowId;
    private String emailPrimary;
    private String emailVerified;

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailRowId(String emailRowId) {
        this.emailRowId = emailRowId;
    }

    public String getEmailRowId() {
        return emailRowId;
    }

    public void setEmailPrimary(String emailPrimary) {
        this.emailPrimary = emailPrimary;
    }

    public String getEmailPrimary() {
        return emailPrimary;
    }
}
