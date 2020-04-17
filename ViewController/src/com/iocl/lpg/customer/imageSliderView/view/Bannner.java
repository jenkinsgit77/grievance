package com.iocl.lpg.customer.imageSliderView.view;

import java.io.Serializable;

public class Bannner implements Serializable {
    @SuppressWarnings("compatibility:-2680481712453165904")
    private static final long serialVersionUID = 1L;

    public Bannner() {
        super();
    }
    private String url;
    private String trainingUrl;
    private String kmUrl;

  
    private String customClass;

    public Bannner(String url,String asb) {
        this.url = url;
        this.customClass = asb;
    }
    
    public void setCustomClass(String customClass) {
        this.customClass = customClass;
    }

    public String getCustomClass() {
        return customClass;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Bannner(String url) {
        this.url = url;
    }

    public void setTrainingUrl(String trainingUrl) {
        this.trainingUrl = trainingUrl;
    }

    public String getTrainingUrl() {
        return trainingUrl;
    }

    public void setKmUrl(String kmUrl) {
        this.kmUrl = kmUrl;
    }

    public String getKmUrl() {
        return kmUrl;
    }
}
