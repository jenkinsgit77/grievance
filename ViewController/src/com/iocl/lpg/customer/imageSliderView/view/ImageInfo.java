package com.iocl.lpg.customer.imageSliderView.view;

import java.io.Serializable;

public class ImageInfo implements Serializable {
    @SuppressWarnings("compatibility:-1496855268072900695")
    private static final long serialVersionUID = 1L;

    public ImageInfo() {
        super();
    }

    public ImageInfo(String fileURL) {
        this.fileURL = fileURL;
    }


    String fileURL;

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getFileURL() {
        return fileURL;
    }
}
