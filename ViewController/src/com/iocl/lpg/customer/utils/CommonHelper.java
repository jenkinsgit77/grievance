package com.iocl.lpg.customer.utils;

import com.iocl.customer.model.service.AppModuleImpl;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import javax.servlet.http.HttpServletRequest;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.ApplicationModule;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import org.json.JSONException;

public class CommonHelper implements Serializable {

    public CommonHelper() {
        super();
    }


    private static Logger logger = Logger.getLogger(CommonHelper.class); ;

    public static String getValueFromRsBundle(String key) {
        String value = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("com.iocl.lpg.customer.custPortalBundle");
            value = bundle.containsKey(key) ? bundle.getString(key) : key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getValueFromModelRsBundle(String key) {
        String value = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("com.iocl.customer.model.ModelBundle");
            value = bundle.containsKey(key) ? bundle.getString(key) : key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    //    public static String getMainBundleProperty(String key) {
    //        return getLocProperty("com.penfax.view.PenfaxViewControllerBundle", key);
    //    }

    public static void calltaskFlowNavigation(String navigigationName) {
        try {
            NavigationHandler nvHndlr = FacesContext.getCurrentInstance()
                                                    .getApplication()
                                                    .getNavigationHandler();
            nvHndlr.handleNavigation(FacesContext.getCurrentInstance(), null, navigigationName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getLocProperty(String resBundleName, String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale = context.getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(resBundleName, locale);

        String propValue = bundle.getString(key);

        return propValue;
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

    // invokes popup aligned with button

    public static void invokePopup(RichPopup popup, UIComponent parent) {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        //hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID, parent);
        //hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN, RichPopup.PopupHints.AlignTypes.ALIGN_AFTER_START);

        popup.show(hints);
    }

    public static void invokePopup(RichPopup popup) {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        popup.show(hints);
    }

    public static void invokePopup(String popupId) {
        invokePopup(popupId, null, null);
    }

    public static void invokePopup(String popupId, String align, String alignId) {
        if (popupId != null) {
            ExtendedRenderKitService service =
                Service.getRenderKitService(FacesContext.getCurrentInstance(), ExtendedRenderKitService.class);

            StringBuffer showPopup = new StringBuffer();
            showPopup.append("var hints = new Object();");
            //Add hints only if specified - see javadoc for AdfRichPopup js for details on valid values and behavior
            if (align != null && alignId != null) {
                showPopup.append("hints[AdfRichPopup.HINT_ALIGN] = " + align + ";");
                showPopup.append("hints[AdfRichPopup.HINT_ALIGN_ID] ='" + alignId + "';");
            }
            showPopup.append("var popupObj=AdfPage.PAGE.findComponent('" + popupId + "'); popupObj.show(hints);");
            service.addScript(FacesContext.getCurrentInstance(), showPopup.toString());
        }
    }

    public static void hidePopup(String popupId) {
        if (popupId != null) {
            ExtendedRenderKitService service =
                Service.getRenderKitService(FacesContext.getCurrentInstance(), ExtendedRenderKitService.class);

            String hidePopup = "var popupObj=AdfPage.PAGE.findComponent('" + popupId + "'); popupObj.hide();";
            service.addScript(FacesContext.getCurrentInstance(), hidePopup);
        }

    }

    /*
     *
     *  This method displays error message to end user
     */
    /*
    * This method displays error message to end user
    */
    public static void addErrorFacesMessage(String compID, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(compID, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, msg));
    }

    public static void addInfoMessage(String compID, String msg) {
        FacesContext.getCurrentInstance().addMessage(compID, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    public static void addWarnsMessage(String compID, String msg) {
        FacesContext.getCurrentInstance().addMessage(compID, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
    }
    /*
 * method will provide the values
 *  @param  ViewObject
 *   @param whereClause
 *    @param parameters for whereClause
 * */


    public static Object evaluateEL(String el) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);

        return exp.getValue(elContext);
    }


    public static Object invokeMethodExpression(String expr, Class returnType, Class[] argTypes, Object[] args) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ELContext elctx = fc.getELContext();
        ExpressionFactory elFactory = fc.getApplication().getExpressionFactory();
        MethodExpression methodExpr = elFactory.createMethodExpression(elctx, expr, Object.class, argTypes);
        return methodExpr.invoke(elctx, args);
    }

    //------------------------------
    public static Object invokeEL(String el, Class[] paramTypes, Object[] params) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        MethodExpression exp = expressionFactory.createMethodExpression(elContext, el, Object.class, paramTypes);

        return exp.invoke(elContext, params);
    }
    //-------------------------------

    public static Object invokeMethodExpression(String expr, Class returnType, Class argType, Object argument) {
        return invokeMethodExpression(expr, returnType, new Class[] { argType }, new Object[] { argument });
    }


    /*
   * This method finds the operationBinding for method opr(String).
   * If no operationBinding is found it adds Error OPER_BINDING_NOT_FOUND to PenfaxResult .
   * Why Class as input param : This method return Object, returned by method Executed. In case method execution fails, reported
   * error must provide information which class tried executing opr method.
   * If operationBinding is present, it is executed. If there are any errors in execution,
   *     erros are added to PenfaxResult and further processResult method is called to display error.
   *
   * @param opr
   * @param className
   * @return
   */


    /*
     * Sets a value into an EL object. Provides similar functionality to
     * the <af:setActionListener> tag, except the from is
     * not an EL. You can get similar behavior by using the following...
     * setEL(to, evaluateEL(from))
     *
     * @param el EL object to assign a value
     * @param val Value to assign
     */

    public static void setEL(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
        ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);

        exp.setValue(elContext, val);
    }

    public static void navigation(String navigation) {
        NavigationHandler nvHndlr = FacesContext.getCurrentInstance()
                                                .getApplication()
                                                .getNavigationHandler();
        nvHndlr.handleNavigation(FacesContext.getCurrentInstance(), null, navigation);
    }

    /**
     * Find an iterator binding in the current binding container by name.
     *
     * @param name iterator binding name
     * @return iterator binding
     */
    public static DCIteratorBinding findIterator(String name) {
        DCIteratorBinding iter = getDCBindingContainer().findIteratorBinding(name);
        if (iter == null) {
            throw new RuntimeException("Iterator '" + name + "' not found");
        }
        return iter;
    }

    /**
     * Find an operation binding in the current binding container by name.
     *
     * @param name operation binding name
     * @return operation binding
     */
    public static OperationBinding findOperation(String name) {
        OperationBinding op = getDCBindingContainer().getOperationBinding(name);
        if (op == null) {
            throw new RuntimeException("Operation '" + name + "' not found");
        }
        return op;
    }

    /**
     * Return the Binding Container as a DCBindingContainer.
     * @return current binding container as a DCBindingContainer
     */
    public static DCBindingContainer getDCBindingContainer() {
        return (DCBindingContainer) getBindingContainer();
    }

    /**
     * Return the current page's binding container.
     * @return the current page's binding container
     */
    public static BindingContainer getBindingContainer() {
        return (BindingContainer) resolveExpression("#{bindings}");
    }


    public static void refreshLayout(javax.faces.component.UIComponent vBinding) {
        AdfFacesContext.getCurrentInstance().addPartialTarget(vBinding);
    }

    public static void checkValidation(String msg, javax.faces.component.UIComponent vBinding) {
        FacesMessage fm = new FacesMessage(msg);
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(vBinding.getClientId(context), fm);
        return;
    }

    /**
     * Method for taking a reference to a JSF binding expression and returning
     * the matching object (or creating it).
     * @param expression EL expression
     * @return Managed object
     */
    public static Object resolveExpression(String expression) {
        FacesContext ctx = getFacesContext();
        Application app = ctx.getApplication();
        ValueBinding bind = app.createValueBinding(expression);
        return bind.getValue(ctx);
    }

    /**
     * Get FacesContext.
     * @return FacesContext
     */
    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static void refreshCompent(String id) {
        UIComponent uiComp = FacesContext.getCurrentInstance()
                                         .getViewRoot()
                                         .findComponent(id);
        if (uiComp != null) {
            AdfFacesContext context = AdfFacesContext.getCurrentInstance();
            context.addPartialTarget(uiComp);
        }
    }


    public static void runJavaScript(String javaScript) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExtendedRenderKitService service = Service.getRenderKitService(fc, ExtendedRenderKitService.class);
            service.addScript(fc, javaScript);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * Description: to refresh page
     */
    public static void refreshPage() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String refreshpage = facesContext.getViewRoot().getViewId();
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        UIViewRoot viewroot = viewHandler.createView(facesContext, refreshpage);
        viewroot.setViewId(refreshpage);
        facesContext.setViewRoot(viewroot);

    }

    public static void addScriptOnPartialRequest(String script) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (AdfFacesContext.getCurrentInstance().isPartialRequest(context)) {
            ExtendedRenderKitService erks = Service.getRenderKitService(context, ExtendedRenderKitService.class);
            erks.addScript(context, script);
        }
    }

    public static String getFileExtn(String filename) {
        String parts[] = filename.split("\\.(?=[^\\.]+$)");
        return parts[1].toLowerCase();
    }

    public static void kycStatusImage(String kycStatus) {
        logger.info("Inside kycStatusImage  Start");
        logger.info("vkycStatus parameter Value in kycStatusImage Method: " + kycStatus);
        java.util.Map sessionscope = ADFContext.getCurrent().getSessionScope();
        
        String stageCheckPath = "/images/KycStatus/kyc_tick.svg";
        String stageUnCheckPath = "/images/KycStatus/kyc_empty.svg";

        if (kycStatus != null && kycStatus.equalsIgnoreCase("N")) {
            sessionscope.put("kycSubmitDistributer", stageCheckPath);
            sessionscope.put("kycReadyLPGConn", stageUnCheckPath);

        } else if (kycStatus != null && kycStatus.equalsIgnoreCase("Y")) {
            sessionscope.put("kycSubmitDistributer", stageCheckPath);
            sessionscope.put("kycReadyLPGConn", stageCheckPath);

        } else {
            sessionscope.put("kycSubmitDistributer", stageUnCheckPath);
            sessionscope.put("kycReadyLPGConn", stageUnCheckPath);
        }
        logger.info("Inside kycStatusImage in CommonHelper End");
    }

    public static void getTrainImage(String currentTrainStop) {
        logger.info("Inside getTrainImage Method Start");
        java.util.Map pageflowParam = ADFContext.getCurrent().getSessionScope();
        if (currentTrainStop == null || currentTrainStop.equals("1")) {
            pageflowParam.put("kycstop1", getValueFromRsBundle("YELLOW_1"));
            pageflowParam.put("kycstop2", getValueFromRsBundle("WHITE_2"));
            pageflowParam.put("kycstop3", getValueFromRsBundle("WHITE_3"));
            pageflowParam.put("kycstop4", getValueFromRsBundle("WHITE_4"));
            /*------------------------Code to Disabled Stop--------------------*/
            pageflowParam.put("kycStopdis1", true);
            pageflowParam.put("kycStopdis2", false);
            pageflowParam.put("kycStopdis3", true);
            pageflowParam.put("kycStopdis4", true);
            /*--------------------------------End Here-------------------------*/
        } else if (currentTrainStop.equals("2")) {
            pageflowParam.put("kycstop1", getValueFromRsBundle("GREEN_1"));
            pageflowParam.put("kycstop2", getValueFromRsBundle("YELLOW_2"));
            pageflowParam.put("kycstop3", getValueFromRsBundle("WHITE_3"));
            pageflowParam.put("kycstop4", getValueFromRsBundle("WHITE_4"));
            /*------------------------Code to Disabled Stop--------------------*/
            pageflowParam.put("kycStopdis1", false);
            pageflowParam.put("kycStopdis2", true);
            pageflowParam.put("kycStopdis3", false);
            pageflowParam.put("kycStopdis4", true);
            /*--------------------------------End Here-------------------------*/
        } else if (currentTrainStop.equals("3")) {
            pageflowParam.put("kycstop1", getValueFromRsBundle("GREEN_1"));
            pageflowParam.put("kycstop2", getValueFromRsBundle("GREEN_2"));
            pageflowParam.put("kycstop3", getValueFromRsBundle("YELLOW_3"));
            pageflowParam.put("kycstop4", getValueFromRsBundle("WHITE_4"));
            /*------------------------Code to Disabled Stop--------------------*/
            pageflowParam.put("kycStopdis1", false);
            pageflowParam.put("kycStopdis2", false);
            pageflowParam.put("kycStopdis3", true);
            pageflowParam.put("kycStopdis4", false);
            /*--------------------------------End Here-------------------------*/
        } else if (currentTrainStop.equals("4")) {
            pageflowParam.put("kycstop1", getValueFromRsBundle("GREEN_1"));
            pageflowParam.put("kycstop2", getValueFromRsBundle("GREEN_2"));
            pageflowParam.put("kycstop3", getValueFromRsBundle("GREEN_3"));
            pageflowParam.put("kycstop4", getValueFromRsBundle("YELLOW_4"));
            /*------------------------Code to Disabled Stop--------------------*/
            pageflowParam.put("kycStopdis1", false);
            pageflowParam.put("kycStopdis2", false);
            pageflowParam.put("kycStopdis3", false);
            pageflowParam.put("kycStopdis4", true);
            /*--------------------------------End Here-------------------------*/
        }
        logger.info("Inside getTrainImage Method End");
    }

    /*
     * creates a unique ID
     */
    public static String createUniqueID() {
        //generate random UUIDs
        UUID idOne = UUID.randomUUID();

        return idOne.toString();
    }

    /*method to generate unique number
     * added by saumya-for displaying reference number
    * */
    public static String getCurrentDateWithTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        String uniqueNo = dateFormat.format(date);
        return uniqueNo;
    }

    

    /**
     * @param data
     * @return
     */
    public static Object resolvEl(String data) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = fc.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, data, Object.class);
        Object Message = valueExp.getValue(elContext);
        return Message;
    }

    public static AppModuleImpl getRootApplicationModule() {
        AppModuleImpl res = null;
        ApplicationModule am = getApplicationModule("AppModule");
        if (am != null) {
            res = (AppModuleImpl) am;
        }
        return res;
    }

    public static String findAfterNullChk(Object val) {

        if (val == null)
            return null;
        else {
            if (StringUtils.isBlank(val.toString())) {
                return null;

            } else {
                val.toString();
            }
        }

        return val.toString();
    }

    public static String dateToString(oracle.jbo.domain.Date var) {
        logger.info("Date Format is oracle.jbo.domain.Date");
        String dateformat = getValueFromRsBundle("LPG_DATE_FORMAT_CODE");
        DateFormat formatter = new SimpleDateFormat(dateformat);
        String dobInString = formatter.format(var.dateValue());
        return dobInString;

    }

    public static String dateToString(java.util.Date var) {
        logger.info("Date Format is java.util.Date");
        String dateformat = getValueFromRsBundle("LPG_DATE_FORMAT_CODE");
        DateFormat formatter = new SimpleDateFormat(dateformat);
        String dobInString = formatter.format(var);
        return dobInString;

    }

    public static Date stringToDate(String var) {
        Date customDate = null;
        try {
            DateFormat formatter = new SimpleDateFormat(getValueFromRsBundle("LPG_DATE_FORMAT_CODE"));
            customDate = formatter.parse(var);
        } catch (ParseException pe) {
            // TODO: Add catch code
            pe.printStackTrace();
        }
        return customDate;
    }

    //        public static String maskedMobileNumber(String mobileNumber){
    //             logger.info("Number to be masked: " + mobileNumber);
    //             StringBuilder masked = new StringBuilder();
    //             int maskDigits=mobileNumber.length()-EPICConstant.NON_MASKED_MOBILECHAR;
    //             for (int i = 0; i < mobileNumber.length(); i++) {
    //                 char c = mobileNumber.charAt(i);
    //                 logger.info("character at i: "+i+" is :" + c);
    //                 if(i<maskDigits){
    //                     masked.append(EPICConstant.MASKEDCHAR);
    //                 }else{
    //                     masked.append(c);
    //                 }
    //             }
    //             logger.info("Final String is :" + masked.toString());
    //             return masked.toString();
    //
    //        }

    public static String getClientIpAddress() {
        String clientIpAddress = ((HttpServletRequest) FacesContext.getCurrentInstance()
                                                                   .getExternalContext()
                                                                   .getRequest()).getRemoteAddr();
        return clientIpAddress;
    }

    public static String getPort() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fctx.getExternalContext().getRequest();

        String serverPort = request.getServerPort() + "";
        return serverPort;
    }

    public static String getClientProtocol() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fctx.getExternalContext().getRequest();

        String protocol = request.getProtocol();
        return protocol;
    }


    public static String getClientIpContext() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fctx.getExternalContext().getRequest();

        String context = request.getContextPath();
        return context;
    }

    public static void uploadReset(List listAttr) {

        for (int counter = 0; counter < listAttr.size(); counter++) {
            ADFContext.getCurrent()
                      .getSessionScope()
                      .remove(listAttr.get(counter).toString());
        }

    }
    
    
    public static String convertDateFormat(String inputDate) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            String convertedDate = "";
            if (inputDate != null)
                try {
                    if (!inputDate.isEmpty()) {
                        Date date = format.parse(inputDate);
                        convertedDate = format1.format(date);
                    }
                } catch (Exception e) {
                    logger.error("In convertDateFormat method : " +
                              e.getMessage());
                }
            return convertedDate;
        }
    public static String maskedMobileNumber(String mobileNumber) {
//        logger.info("Number to be masked: " + mobileNumber);
        StringBuilder masked = new StringBuilder();
        int maskDigits = mobileNumber.length() - EPICConstant.NON_MASKED_MOBILECHAR;
        for (int i = 0; i < mobileNumber.length(); i++) {
            char c = mobileNumber.charAt(i);
           
            if (i < maskDigits) {
                masked.append(EPICConstant.MASKEDCHAR);
            } else {
                masked.append(c);
            }
        }
//        logger.info("Final String is :" + masked.toString());
        return masked.toString();

    }
    
    public static void sendOTP(String mobileNumber){
            logger.info("**inside send otp**");
            try{
                OperationBinding ob = CommonHelper.findOperation("serviceCustomerCall");
                java.util.List<Object> lstInput = new java.util.ArrayList<Object>();
                lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue("SEND_OTP"));
                JSONObject jsonInput = new JSONObject();
                jsonInput.put(EPICConstant.MOBILE_NUMBER, mobileNumber);
                lstInput.add(1, jsonInput);
    
                ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
                ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.SENDOTP);
                ob.execute();
                if (!ob.getErrors().isEmpty()) {
                    throw new Exception("Error while Sending OTP...");
    
                }
                List result = (List) ob.getResult();
                if ( result.size() > 0 && String.valueOf(result.get(0)).equalsIgnoreCase(EPICConstant.FALSE_VAl)){
                     throw new Exception("Error while Sending OTP...");
                    }
    
            }catch(Exception otpEx){
                logger.info("**ERROR in send otp**");
                otpEx.printStackTrace();
            }
    }
    
    public static String maskedGenericNumber(String genericNumber) {
        // logger.info("Number to be masked: " + mobileNumber);
        StringBuilder masked = new StringBuilder();
        int maskDigits = genericNumber.length() - EPICConstant.NON_MASKED_MOBILECHAR;
        for (int i = 0; i < genericNumber.length(); i++) {
            char c = genericNumber.charAt(i);
            if (i < maskDigits) {
                masked.append(EPICConstant.MASKEDCHAR);
            } else {
                masked.append(c);
            }
        }
        System.out.println("Final String is :" + masked.toString());
        return masked.toString();

    }
 
     public static void moveToExternalContext(String path) {
        logger.info("Inside moveToExternalContext and path is"+path);
        CommonHelper.refreshPage();
        ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
        try {
            exct.redirect(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String sendEmailOTP(String emailID){
        logger.info("**INSIDE SEND OTP for EMAIL=**"+"serviceCustomerCall");
        String status=null;
        try{        
            OperationBinding ob = CommonHelper.findOperation("serviceCustomerCall");
            java.util.List lstInput = new java.util.ArrayList();
            lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.GENERATE_EMAILOTP));
            JSONObject jsonInput = new JSONObject();
            jsonInput.put(EPICConstant.SOURCE, EPICConstant.SOURCE_PORTAL);
            jsonInput.put(EPICConstant.TRACKING_ID, CommonHelper.createUniqueID());
            jsonInput.put(EPICConstant.EmailAddress, emailID);
            lstInput.add(1, jsonInput);
    
            ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
            ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.GENERATE_EMAILOTP);
            ob.execute();
            if (!ob.getErrors().isEmpty()) {
                status=EPICConstant.ERROR;
            } else {
                status=EPICConstant.SUCCESS;
            }
            
        }catch(Exception otpEx){
            logger.info("**ERROR in send otp for email**",otpEx);
            status= EPICConstant.ERROR;
        }
        return status;
    }
    
    
    
    
    public static String validateEmailOTP(String emailOTP,String emailID){
        logger.info("**INSIDE SEND OTP for EMAIL=**"+"serviceCustomerCall");
        String status=null;
        try{        
            OperationBinding ob = CommonHelper.findOperation("serviceCustomerCall");
            java.util.List lstInput = new java.util.ArrayList();
            lstInput.add(0, EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.VALIDATE_EMAILOTP));
            JSONObject jsonInput = new JSONObject();
            jsonInput.put(EPICConstant.SOURCE, EPICConstant.SOURCE_PORTAL);
            jsonInput.put(EPICConstant.TRACKING_ID, CommonHelper.createUniqueID());
            jsonInput.put(EPICConstant.EmailAddress, emailID);
            jsonInput.put(EPICConstant.EMAIL_OTP, emailOTP);
            lstInput.add(1, jsonInput);
    
            ob.getParamsMap().put(EPICConstant.SERVICELIST, lstInput);
            ob.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.VALIDATE_EMAILOTP);
            ob.execute();
            if (!ob.getErrors().isEmpty()) {
                status=EPICConstant.ERROR;
            } else {
                status=EPICConstant.SUCCESS;
            }
            
        }catch(Exception otpEx){
            logger.info("**ERROR in send otp for email**",otpEx);
            status= EPICConstant.ERROR;
        }
        return status;
    }
    
    public static String errorMessageCall(String errorCode){
        logger.info("**inside errorMessageCall for Error Handling**");
        String status=null;
        try{     
            OperationBinding opErr = CommonHelper.findOperation(EPICConstant.FETCHERRORMESSAGE);
            opErr.getParamsMap().put(EPICConstant.ERRORCODE, String.valueOf(errorCode).equalsIgnoreCase(EPICConstant.ERROR_NULL)?
                                    EPICConstant.OTH1:String.valueOf(errorCode));
            opErr.execute();
            status=EPICConstant.SUCCESS;            
            return status;
        }catch(Exception otpEx){
            logger.info("**ERROR in errorMessageCall**",otpEx);
            OperationBinding opErr = CommonHelper.findOperation(EPICConstant.FETCHERRORMESSAGE);
            opErr.getParamsMap().put(EPICConstant.ERRORCODE, EPICConstant.OTH2);
            opErr.execute();
            return EPICConstant.ERROR;
        }
    }
    
    /**
     * distance B/w 
     * Latt & Long
     * maps
     * */
    public static double distanceBwLatLon(String srcLat1, String destLat2, String srcLon1,
            String destLon2) {
        logger.info("srcLat1 "+srcLat1);
        logger.info("destLat2 "+destLat2);
        logger.info("srcLon1 "+srcLon1);
        logger.info("destLon2 "+destLon2);
        final int R = 6371; // Radius of the earth
        double distance = 1;
        try{
            double lat1=Double.parseDouble(srcLat1);
            double lat2=Double.parseDouble(destLat2);
            double lon1=Double.parseDouble(srcLon1);
            double lon2=Double.parseDouble(destLon2);
            double latDistance = Math.toRadians(lat2 - lat1);
            double lonDistance = Math.toRadians(lon2 - lon1);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            distance = R * c * 1000; // convert to meters
        }catch(Exception diEx){
           logger.info("error in calculating retail outlets distance",diEx);
        }
        return distance;
    }
    
    /**
     * Sorting JsonArray
     * maps
     * */
    public static JSONArray sortedJsonArray(JSONArray jsonArr){
        logger.info("Inside sortedJsonArray method start");
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonValues.add(jsonArr.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            // sort by Distance
            private static final String KEY_NAME = "Distance";

            @Override
            public int compare(JSONObject a, JSONObject b) {
//                String valA = new String();
//                String valB = new String();
//               =new Double();
//                =new Double();
                Double valA =null;
                Double valB =null;
                try {
                     valA = (Double) a.get(KEY_NAME);
                     valB = (Double) b.get(KEY_NAME);
                } catch (JSONException e) {
                    //do something
                }

                return valA.compareTo(valB);
                //return -valA.compareTo(valB);
            }
        });

        for (int i = 0; i < jsonArr.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
            logger.info("After Order "+i+" "+jsonValues.get(i));
        }
        
        JSONArray newSortedJsonArray = new JSONArray();
        for (int j = 0; j < sortedJsonArray.length(); j++) {
        JSONObject invenLocObj = sortedJsonArray.getJSONObject(j);
        invenLocObj.put("Sno",String.valueOf(j));
        newSortedJsonArray.put(invenLocObj);
        }
        
        logger.info("Inside sortedJsonArray method end");
        return newSortedJsonArray;
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

                if (formatType != null && formatType.equalsIgnoreCase("2")) {
                    formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                } else if (formatType != null && formatType.equalsIgnoreCase("3")) {
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
    
    public static int diffInYear(oracle.jbo.domain.Date custDate) {

        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(custDate.getValue().getTime());
        Calendar sysDate = Calendar.getInstance();
        int age = sysDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if ((dob.get(Calendar.MONTH) > sysDate.get(Calendar.MONTH)) ||
            (dob.get(Calendar.MONTH) == sysDate.get(Calendar.MONTH) &&
             dob.get(Calendar.DAY_OF_MONTH) > sysDate.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }
    public static boolean invalidCharacter(Object input) {
          if (input == null)
              return false;
          else {
              if (StringUtils.isBlank(String.valueOf(input)))
                  return false;

          }

          return evaluateRegex(String.valueOf(input), EPICConstant.SPECIAL_CHAR_EXP);

      }
    public static boolean evaluateRegex(String inputval, String regex) {
        boolean val = true;
        Pattern reges = Pattern.compile(regex);
        Matcher m = reges.matcher(inputval);
        val = m.matches();
        return val;
    }
}

