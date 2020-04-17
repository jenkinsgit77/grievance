package com.iocl.customer.model.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Properties;

import java.io.InputStream;

import oracle.adf.share.ADFContext;

import org.apache.log4j.Logger;
public class EPICIOCLResourceModel {
    private static Logger log = Logger.getLogger(EPICIOCLResourceModel.class);
    static java.util.Map paramLoad = ADFContext.getCurrent().getSessionScope();
    public EPICIOCLResourceModel() {
        super();
    }
    
    private static Object[][] contents = readPropertyFile();

    public static void setContents(Object[][] contents) {
        EPICIOCLResourceModel.contents = contents;
    }

    public static Object[][] getContents() {
        return contents;
    }

    public static Object[][] readPropertyFile() {
        log.info("External file name"+EPICConstant.PROPERTIES_FILEPATH);
        Object[][] data = null;
        try {
    //Property file path in system
                       String nodeProFile =EPICConstant.PROPERTIES_FILEPATH;
      //      log.info("nodeProFile : " + nodeProFile);
            
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream ("CUSTOMER_BUNDLE.properties");
            Properties prop = new Properties();
            //load a properties file
            if(inputStream!=null) {
                log.info("%%%%%%%%%%%%SUCCESS-----------");
                prop.load(inputStream);
            }
            else
            {
                log.info("%%%%%%%%%%%%FAILURE-----------");
                prop.load(new FileInputStream(nodeProFile));
            }
            Enumeration propKeys = prop.keys();
        //    log.info("Property size : " + prop.size());
            data = new Object[prop.size()][2];
            int i = 0;
            while (propKeys.hasMoreElements()) {
                String key = (String)propKeys.nextElement();
                String value = prop.getProperty(key);
                data[i][0] = key;
                data[i][1] = value;
                i++;
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return data;
    }
    
    public static String findKeyValue(String key) {
        String val=null;
        if(getContents()==null){
            contents=readPropertyFile();
        }
        for(int i=0;i<contents.length;i++) {            
            if(contents[i][0].toString().equalsIgnoreCase(key)) {
                val=contents[i][1].toString();
            }
        }
        paramLoad.put("propertyLoad", "false");
        
        return val;
    }
}
