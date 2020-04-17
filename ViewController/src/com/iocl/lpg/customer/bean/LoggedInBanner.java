package com.iocl.lpg.customer.bean;

import java.util.ArrayList;
import java.util.List;

import oracle.adf.share.ADFContext;

import oracle.stellent.ridc.IdcClientException;

import com.iocl.lpg.customer.imageSliderView.view.Bannner;
import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;
import com.iocl.lpg.customer.utils.UCMUtil;

import java.io.Serializable;

import java.util.Arrays;

import oracle.adf.view.rich.component.rich.RichPopup;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.event.PollEvent;

public class LoggedInBanner implements Serializable {
    private RichPopup onloadPopUpBinding;

    public LoggedInBanner() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(LoggedInBanner.class);

        } else {
            log = Logger.getLogger(LoggedInBanner.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    private static Logger log ;
    private java.util.List<Bannner> lstBanner = new java.util.ArrayList<Bannner>();
    
  //  java.util.Map param = ADFContext.getCurrent().getPageFlowScope();

    public void setLstBanner(List<Bannner> lstBanner) {
        this.lstBanner = lstBanner;
    }

    public List<Bannner> getLstBanner() {
        return lstBanner;
    }

    public String loggedInBannerImage() {
        //        String retString = "goToBanner";
        String retString = EPICIOCLResourceCustBundle.findKeyValue("ExternalPath");
        log.info("retString " + retString);
        log.info("Inside loggedInBannerImage Start");

        try {
            String bannerName = null;
            List<String> varLst = null;
            log.info("BannerName " + CommonHelper.evaluateEL("#{pageFlowScope.BannerName}"));
            if (CommonHelper.evaluateEL("#{pageFlowScope.BannerName}") != null) {
                log.info("BannerName length " +
                         String.valueOf(CommonHelper.evaluateEL("#{pageFlowScope.BannerName}")).length());
                if (!StringUtils.isBlank(String.valueOf(CommonHelper.evaluateEL("#{pageFlowScope.BannerName}")))) {
                    bannerName = CommonHelper.evaluateEL("#{pageFlowScope.BannerName}").toString();
                } else {
                    bannerName = "PUBLIC_BANNER_IMAGE_LIST";
                    CommonHelper.setEL("#{pageFlowScope.BannerPath}", "HomeBanner");
                }
            } else {
                bannerName = "PUBLIC_BANNER_IMAGE_LIST";
                CommonHelper.setEL("#{pageFlowScope.BannerPath}", "HomeBanner");

            }
            log.info("bannerName is " + bannerName);
            String imgList = EPICIOCLResourceCustBundle.findKeyValue(bannerName);
            log.info("imgList " + imgList);
            String[] imgListArray = imgList.split(",");
            varLst = Arrays.asList(imgListArray);
            if (varLst != null) {
                for (int i = 0; i < varLst.size(); i++) {
                    Bannner obj = new Bannner();
                    if (varLst.get(i)
                        .equalsIgnoreCase(EPICIOCLResourceCustBundle.findKeyValue("KM_TRAINING_URL_CONTENTID"))) {

                        obj.setTrainingUrl(EPICIOCLResourceCustBundle.findKeyValue("LEARNING_URL_KM"));
                    }
                    if (varLst.get(i)
                        .equalsIgnoreCase(EPICIOCLResourceCustBundle.findKeyValue("KM_HOMEURL_CONTENTID"))) {

                        obj.setTrainingUrl(EPICIOCLResourceCustBundle.findKeyValue("KM_HOME"));
                    }
                    if (varLst.get(i)
                        .equalsIgnoreCase(EPICIOCLResourceCustBundle.findKeyValue("KM_FAC_URL_CONTENTID"))) {

                        obj.setTrainingUrl(EPICIOCLResourceCustBundle.findKeyValue("KM_FAC"));
                    }
                    if (varLst.get(i)
                        .equalsIgnoreCase(EPICIOCLResourceCustBundle.findKeyValue("KM_UJJWALA_CONTENTID"))) {

                        obj.setTrainingUrl(EPICIOCLResourceCustBundle.findKeyValue("KM_UJJWALA_URL"));
                    }
                    log.info("Image-" + varLst.get(i));
                    obj.setUrl(varLst.get(i));
                    lstBanner.add(obj);
                }
                CommonHelper.setEL("#{pageFlowScope.lstBannerPF}", lstBanner);
                // param.put("lstBannerPF", lstBanner);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception in loggedInBannerImage Method");
            retString = EPICConstant.ERROR;
            log.info(e.getMessage());
        }
        log.info("Inside loggedInBannerImage End");
        log.info("retString " + retString);
        return retString;
    }

    public void bannerOnLoad(PollEvent pollEvent) {
        // Add event code here...
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        onloadPopUpBinding.show(hints);
    }

    public void setOnloadPopUpBinding(RichPopup onloadPopUpBinding) {
        this.onloadPopUpBinding = onloadPopUpBinding;
    }

    public RichPopup getOnloadPopUpBinding() {
        return onloadPopUpBinding;
    }
}
