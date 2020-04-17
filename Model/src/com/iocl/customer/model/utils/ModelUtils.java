package com.iocl.customer.model.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import org.json.JSONException;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class ModelUtils {
    public ModelUtils() {
        super();
    }
    private static Logger log = Logger.getLogger(ModelUtils.class);
    static final Object[][] contents = readPropertyFile();

    public Object[][] getContents() {
        return contents;
    }

    public static Object[][] readPropertyFile() {
        Object[][] data = null;
        try {
    //Property file path in system
                       String nodeProFile =EPICConstant.PROPERTIES_FILEPATH;
      //      log.info("nodeProFile : " + nodeProFile);
            
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream ("LPG_BUNDLE.properties");
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
        
        for(int i=0;i<contents.length;i++) {
            
            if(contents[i][0].toString().equalsIgnoreCase(key)) {
                val=contents[i][1].toString();
                
            }
        }
        
        return val;
    }

    /**
     *Converts a String to oracle.jbo.domain.Date
     * @param String
     * @return oracle.jbo.domain.Date
     */
    public static oracle.jbo.domain.Date castToJBODate(String aDate) {
        DateFormat formatter;
        java.util.Date date;

        if (aDate != null) {

            try {

                formatter = new SimpleDateFormat("dd/MM/yyyy");
                date = formatter.parse(aDate);
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                oracle.jbo.domain.Date jboDate = new oracle.jbo.domain.Date(sqlDate);

                return jboDate;
            } catch (ParseException e) {

                e.printStackTrace();
            }

        }

        return null;
    }
    
    public static String createUniqueID() {
        //generate random UUIDs
        UUID idOne = UUID.randomUUID();

        return idOne.toString();
    }
    public static Object evaluateEL(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);

        return exp.getValue(elContext);
    }
    
    public static void setEL(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);

        exp.setValue(elContext, val);
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            log.info("isJSONValid Exception------>" + ex);
            return false;

        }
        return true;
    }
    
    public static String changeDateParse(String dateToBeChanged){
        String dateReturn="NA";   
        try{
            List<String> strings = new ArrayList<String>();
            int index = 0;
            while (index < dateToBeChanged.length()) {
                strings.add(dateToBeChanged.substring(index, Math.min(index + 8,dateToBeChanged.length())));
                index += 8;
            }
            String dateSplitted=strings.get(0);            
            StringBuilder str = new StringBuilder(dateSplitted);           
            str.insert(4, '/');           
            str.insert(7, '/');            
            String strB=str.toString();
            String[] splitter=strB.split("/");
            dateReturn=splitter[2]+"/"+splitter[1]+"/"+splitter[0];           
        }
        catch(Exception ex){
            log.info("exception in date parser, setting def date as 01/01/2018::"+ex);
            dateReturn="01/01/2018";   // default date in error
        }
        
        return dateReturn;        
    }
    
    /**
     *Converts a String to oracle.jbo.domain.Date
     * @param String
     * @return oracle.jbo.domain.Date
     */
    public static oracle.jbo.domain.Date dateCompToJBODate(String aDate, String formatType) {
        DateFormat formatter;
        java.util.Date date;
        if (aDate != null) {
            try {
                if (formatType != null && formatType.equalsIgnoreCase("1")) {
                    formatter = new SimpleDateFormat("MM/dd/yyyy");
                }else if (formatType != null && formatType.equalsIgnoreCase("2")) {
                    formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                } else if (formatType != null && formatType.equalsIgnoreCase("3")) {
                    formatter = new SimpleDateFormat("dd/MM/yyyy");
                }else if (formatType != null && formatType.equalsIgnoreCase("4")) {
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                }else {
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                }

                date = formatter.parse(aDate);
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                oracle.jbo.domain.Date jboDate = new oracle.jbo.domain.Date(sqlDate);
                return jboDate;
            } catch (ParseException e) {

                e.printStackTrace();
            }

        }

        return null;
    }
}
