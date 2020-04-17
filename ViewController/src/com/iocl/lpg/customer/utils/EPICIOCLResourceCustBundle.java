package com.iocl.lpg.customer.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Properties;

import java.io.InputStream;

import java.io.Serializable;

import oracle.adf.share.ADFContext;

import org.apache.log4j.Logger;

public class EPICIOCLResourceCustBundle implements Serializable{
    private static Logger log = Logger.getLogger(EPICIOCLResourceCustBundle.class);
    static java.util.Map paramLoad = ADFContext.getCurrent().getSessionScope();

    public EPICIOCLResourceCustBundle() {
        super();
    }

    private static Object[][] contents = readPropertyFile();

    public static void setContents(Object[][] contents) {
        EPICIOCLResourceCustBundle.contents = contents;
    }

    public static Object[][] getContents() {
        return contents;
    }

    public static Object[][] readPropertyFile() {
        log.info("Inside readPropertyFile in EPICIOCLResourceBundle");

        Object[][] data = null;
        try {
            //Property file path in system
            String nodeProFile = EPICConstant.PROPERTIES_FILEPATH;
            //      log.info("nodeProFile : " + nodeProFile);

            InputStream inputStream = Thread.currentThread()
                                            .getContextClassLoader()
                                            .getResourceAsStream("CUSTOMER_BUNDLE.properties");
            Properties prop = new Properties();
            //load a properties file
            if (inputStream != null) {
                log.info("%%%%%%%%%%%%SUCCESS-----------");
                prop.load(inputStream);
            } else {
                log.info("%%%%%%%%%%%%FAILURE-----------");
                prop.load(new FileInputStream(nodeProFile));
            }
            Enumeration propKeys = prop.keys();
            //    log.info("Property size : " + prop.size());
            data = new Object[prop.size()][2];
            int i = 0;
            while (propKeys.hasMoreElements()) {
                String key = (String) propKeys.nextElement();
                String value = prop.getProperty(key);
                data[i][0] = key;
                data[i][1] = value;
                i++;
            }
        } catch (FileNotFoundException fnfe) {
            log.info("External file name" + EPICConstant.PROPERTIES_FILEPATH);
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return data;
    }

    public static String findKeyValue(String key) {
        String val = null;
        
        if (getContents() == null) {
            log.info("Refreshing customer property load with load param as::" + paramLoad.get("custPropertyLoad"));
            contents = readPropertyFile();
        }
        for (int i = 0; i < contents.length; i++) {
            if (contents[i][0].toString().equalsIgnoreCase(key)) {
                val = contents[i][1].toString();
            }
        }
        //System.out.println("key--"+key);
        if(!key.equalsIgnoreCase("SIGN_OUT_URL")){
        paramLoad.put("custPropertyLoad", "false");
        }

        return val;
    }
}
