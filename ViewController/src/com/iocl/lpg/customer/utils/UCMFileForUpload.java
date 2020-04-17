package com.iocl.lpg.customer.utils;

import java.io.Serializable;

public class UCMFileForUpload implements Serializable {
    @SuppressWarnings("compatibility:6947317544177898037")
    private static final long serialVersionUID = 1L;

    public UCMFileForUpload() {
        super();
    }
    
    private String fileName;           //FileName   
    private String contentType;     //File content type   
    private byte[] file;                   //File content (Input Stream)   
    private String urlFileName;      //Url fileName   
    private String fileDescription;  //File description

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public void setUrlFileName(String urlFileName) {
        this.urlFileName = urlFileName;
    }

    public String getUrlFileName() {
        return urlFileName;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public String getFileDescription() {
        return fileDescription;
    }
}
