package com.iocl.lpg.customer.bean;

//import com.iocl.lpg.utils.CommonHelper;

import com.iocl.customer.model.service.AppModuleImpl;
import com.iocl.lpg.customer.bean.profileOverview.AddressParams;
import com.iocl.lpg.customer.bean.profileOverview.EmailParams;
import com.iocl.lpg.customer.bean.profileOverview.IdentityParams;
import com.iocl.lpg.customer.bean.profileOverview.MobileNoParams;
import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.RegionBinding;
import oracle.adf.model.RegionContext;
import oracle.adf.model.RegionController;

import oracle.adf.model.binding.DCDataControl;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adf.view.rich.render.ClientEvent;

import oracle.jbo.ApplicationModule;

import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.trinidad.util.Service;
import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class TemplateRegionBean implements RegionController {
    public TemplateRegionBean() {
        super();
    }
    private static Logger logger = Logger.getLogger(TemplateRegionBean.class);

    @Override
    public boolean refreshRegion(RegionContext regionContext) {
        int refreshFlag = regionContext.getRefreshFlag();
        if (refreshFlag == RegionBinding.PREPARE_MODEL) {
//            this.initializeMethod();
        }
        regionContext.getRegionBinding().refresh(refreshFlag);
        return false;
    }


    @Override
    public boolean validateRegion(RegionContext regionContext) {
        // TODO Implement this method
        return false;
    }

    @Override
    public boolean isRegionViewable(RegionContext regionContext) {
        // TODO Implement this method
        return false;
    }

    @Override
    public String getName() {
        // TODO Implement this method
        return null;
    }

    public static ApplicationModule getApplicationModule(String appModuleName) {
        ApplicationModule am = null;
        BindingContext ctx = BindingContext.getCurrent();
        if (ctx != null) {
            DCDataControl dc = ctx.findDataControl(appModuleName + "DataControl");
            if (dc != null) {
                am = dc.getApplicationModule();
            }
        }
        return am;
    }

    public static AppModuleImpl getRootApplicationModule() {
        AppModuleImpl res = null;
        ApplicationModule am = getApplicationModule("AppModule");
        if (am != null) {
            res = (AppModuleImpl) am;
        }
        return res;
    }

    public static String maskedGenericNumber(String genericNumber) {
        // logger.info("Number to be masked: " + mobileNumber);
        StringBuilder masked = new StringBuilder();
        int maskDigits = genericNumber.length() - EPICConstant.NON_MASKED_MOBILECHAR;
        for (int i = 0; i < genericNumber.length(); i++) {
            char c = genericNumber.charAt(i);
            System.out.println("character at i: " + i + " is :" + c);
            if (i < maskDigits) {
                masked.append(EPICConstant.MASKEDCHAR);
            } else {
                masked.append(c);
            }
        }
        System.out.println("Final String is :" + masked.toString());
        return masked.toString();

    }

}
