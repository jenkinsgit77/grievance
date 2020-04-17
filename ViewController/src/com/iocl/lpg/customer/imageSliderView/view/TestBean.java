package com.iocl.lpg.customer.imageSliderView.view;

import com.iocl.lpg.customer.bean.TemplateBean;
import com.iocl.lpg.customer.bean.subscriptionvoucher.SVProductCharges;
import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;
import com.iocl.lpg.customer.utils.UCMUtil;

import java.io.IOException;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.share.ADFContext;

import oracle.stellent.ridc.IdcClientException;

import oracle.stellent.ridc.model.DataResultSet;

import org.apache.log4j.Logger;

public class TestBean implements Serializable {
    @SuppressWarnings("compatibility:9041156156295281520")
    private static final long serialVersionUID = 1L;

    public TestBean() {


    }
    private java.util.List<Bannner> lstBanner = new java.util.ArrayList<Bannner>();
    private java.util.List<Bannner> lstnews = new java.util.ArrayList<Bannner>();
    private java.util.List<News> newsList = new java.util.ArrayList<News>();
    java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
    private static Logger logger = Logger.getLogger(TestBean.class);
    public void testp(ActionEvent actionEvent) {

        this.lstBanner.add(new Bannner("http://laoblogger.com/images/jpg-images-of-flowers-1.jpg"));

        this.lstBanner.add(new Bannner("http://laoblogger.com/images/images-of-flowers-and-plants-2.jpg"));


        this.lstBanner.add(new Bannner("http://laoblogger.com/images/jpg-images-of-flowers-1.jpg"));

        this.lstBanner.add(new Bannner("http://laoblogger.com/images/images-of-flowers-and-plants-2.jpg"));

    }


    public void setLstBanner(List<Bannner> lstBanner) {
        this.lstBanner = lstBanner;
    }

    public List<Bannner> getLstBanner() {

        param.get("imagesList");
        return lstBanner;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    String val = null;

    public void callUcmImage() throws IOException {
        System.out.println("------------Inside callUcmImage 1-----------");
        String news = "";
       
            
            List<String> varLst = UCMUtil.fetchContentIdList(EPICConstant.BANNERIMGPATH);
          
            if (lstBanner != null) {
                for (int i = 0; i < varLst.size(); i++) {
                    Bannner obj = new Bannner();
                    if(varLst.get(i).equalsIgnoreCase(EPICIOCLResourceCustBundle.findKeyValue("KM_TRAINING_URL_CONTENTID"))){
                          
                            obj.setTrainingUrl(EPICIOCLResourceCustBundle.findKeyValue("LEARNING_URL_KM"));
                        }
                    if(varLst.get(i).equalsIgnoreCase(EPICIOCLResourceCustBundle.findKeyValue("KM_HOMEURL_CONTENTID"))){
                        
                            obj.setTrainingUrl(EPICIOCLResourceCustBundle.findKeyValue("KM_HOME"));
                        }
                    if(varLst.get(i).equalsIgnoreCase(EPICIOCLResourceCustBundle.findKeyValue("KM_FLIGHT_URL_CONTENTID"))){
                        
                            obj.setTrainingUrl(EPICIOCLResourceCustBundle.findKeyValue("KM_FLIGHT"));
                        }
                    if(varLst.get(i).equalsIgnoreCase(EPICIOCLResourceCustBundle.findKeyValue("KM_UJJWALA_CONTENTID"))){
                        
                            obj.setTrainingUrl(EPICIOCLResourceCustBundle.findKeyValue("KM_UJJWALA_URL"));
                        }
                    obj.setUrl(varLst.get(i));
                    lstBanner.add(obj);
                }
                param.put("dynamicBanner", lstBanner);
                param.put("imagesList", lstBanner);

            }
            
            List<String> varLstt = UCMUtil.fetchContentIdList(EPICConstant.NEWSIMGPATH);
            if (lstnews != null) {
                for (int i = 0; i < varLstt.size(); i++) {
                    System.out.println("------------Inside callUcmImage 2-----------");
                    if(varLstt.get(i)!=null){
                    news = varLstt.get(i);
                    }

                try {
                    val = UCMUtil.getUCMContentById(news);
                } catch (IdcClientException e) {
                }
                newsList.add(new News(val));
                    
                        param.put("news", newsList);
                }
             
            }
       


    }

    public void setLstnews(List<Bannner> lstnews) {
        this.lstnews = lstnews;
    }

    public List<Bannner> getLstnews() {

        param.get("newsList");
        return lstnews;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public List<News> getNewsList() {

        return newsList;
    }

    public void customerUnsecuredActionLis(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside customerUnsecuredActionLis method in TestBean");
        if (ADFContext.getCurrent().getRequestScope().get("ActionName") != null) {
        }
        customerNavigate();
    }

    public void customerUnsecuredTFActionLis(ActionEvent actionEvent) {
        // Add event code here...
        logger.info("Inside customerUnsecuredTFActionLis method in TestBean");
        customerNavigate();
    }

    public void customerNavigate() {
        CommonHelper.refreshPage();
        String destination = "/webcenter";
        String appPath = null;
        if (ADFContext.getCurrent()
                      .getRequestScope()
                      .get("ActionName") != null) {
            appPath = ADFContext.getCurrent()
                                .getRequestScope()
                                .get("ActionName")
                                .toString();
            logger.info("appPath from db is: " + appPath);
        }
        destination = destination + appPath;
        logger.info("final destination app Path is: " + destination);

        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(destination);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public String unsecuredTFActions() {
        // Add event code here...
        String outcome=null;
        logger.info("Inside customerUnsecuredActionLis method in TestBean");
        if (ADFContext.getCurrent().getRequestScope().get("ActionName") != null) {
            if(ADFContext.getCurrent().getRequestScope().get("ActionName").toString().equalsIgnoreCase("toRecaptcha")){
                outcome=ADFContext.getCurrent().getRequestScope().get("ActionName").toString();
            }else{
                customerNavigate();
            }
        }
        logger.info("appPath from db is: " + outcome);
        CommonHelper.refreshPage();
        return outcome;
    }
    
//    public String unsecuredLinkTFActions() {
//        // Add event code here...
//        String outcome=null;
//        logger.info("Inside customerUnsecuredActionLis method in TestBean");
//        if (ADFContext.getCurrent().getRequestScope().get("ActionName") != null) {
//            if(ADFContext.getCurrent().getRequestScope().get("ActionName").toString().equalsIgnoreCase("toRecaptcha")){
//                outcome=ADFContext.getCurrent().getRequestScope().get("ActionName").toString();
//            }else{
//                customerNavigate();
//            }
//        }
//        CommonHelper.refreshPage();
//        logger.info("appPath from db is: " + outcome);
//        return outcome;
//    }
}
