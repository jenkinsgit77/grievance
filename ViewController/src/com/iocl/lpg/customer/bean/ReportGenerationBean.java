package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.bean.profileOverview.ReportEJB;
import com.iocl.lpg.customer.utils.CommonHelper;

import java.io.Serializable;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.time.Month;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.faces.bi.component.chart.UIBarChart;
import oracle.adf.view.rich.component.rich.RichPoll;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;

//import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.event.PollEvent;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class ReportGenerationBean  implements Serializable {
    @SuppressWarnings("compatibility:-9018529671209993274")
    private static final long serialVersionUID = 1L;
    private static Logger log ;
    private RichPanelGroupLayout reportDVTPanelBind;
    private Boolean reportAvailableFlg;
    
    private String evalCount;
    java.util.Map reportParam = ADFContext.getCurrent().getPageFlowScope();
    java.util.Map sessionParam = ADFContext.getCurrent().getSessionScope();
    private RichPoll pollstbinding;
    private RichPanelGroupLayout pg14;
    private UIBarChart barChart;
    private RichPanelGroupLayout pgCharNotFound;

    public ReportGenerationBean() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(ReportGenerationBean.class);

        } else {
            log = Logger.getLogger(ReportGenerationBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }


    public void setPollstbinding(RichPoll pollstbinding) {
        this.pollstbinding = pollstbinding;
    }

    public RichPoll getPollstbinding() {
        return pollstbinding;
    }

    public String generateReportCall() {
        log.info("inside generateReportCall****");
        String genReport="goToReportFrg";
        
//        try {
//            DCIteratorBinding iter = CommonHelper.findIterator("ReportsChartVO2Iterator");
//            System.out.println("---generateReportCall1>"+iter);
//            ViewObject obj = iter.getViewObject();
//            System.out.println("---generateReportCall2>"+obj);
//            java.util.List<ReportEJB> lstReportList = new java.util.ArrayList<ReportEJB>();
//            System.out.println("---generateReportCall3>"+lstReportList);
//            if (obj.getRowCount() > 0) {
//                System.out.println("---generateReportCall4>"+obj.getRowCount());
//                RowSetIterator iterR = obj.createRowSetIterator(null);
//                System.out.println("---generateReportCall5>"+iterR);
//                while (iterR.hasNext()) {
//                    System.out.println("---generateReportCall6>"+iterR);
//                    ReportEJB repBn = new ReportEJB();
//                    System.out.println("---generateReportCall7>"+repBn);
//                    Row row = iterR.next();
//                    System.out.println("---generateReportCall8>"+row);
//                    repBn.setBookingCount((Number)row.getAttribute("CountVal"));
//                    System.out.println("---generateReportCall9>"+(Number)row.getAttribute("CountVal"));
//                    repBn.setBookingMonth(String.valueOf(row.getAttribute("MonthVal")));
//                    System.out.println("---generateReportCall10>"+String.valueOf(row.getAttribute("MonthVal")));
//                    if(row.getAttribute("CountVal")==null || StringUtils.isBlank(row.getAttribute("CountVal").toString()))
//                    {
//                    repBn.setBookingValue(0);
//                    System.out.println("---generateReportCall1 if>");
//                    }
//                    else {
//                        repBn.setBookingValue(Integer.parseInt(row.getAttribute("CountVal").toString()));
//                        System.out.println("---generateReportCall1 if>"+String.valueOf(row.getAttribute("CountVal")));
//                    }
//                    lstReportList.add(repBn);
//                }
//               
//            }
//            CommonHelper.setEL("#{pageFlowScope.reportListChart}", lstReportList);
//            System.out.println("lstReportList=--12>"+lstReportList);
//            
//        }catch(Exception e) {
//            log.info("consumption pattern exception----==>"+e);
//            e.printStackTrace();
//        }
       /* reportAvailableFlg=false;
        try{
            java.util.List lstInputGenRep = new java.util.ArrayList();
            JSONObject jsonInputGenRep = new JSONObject();
            java.util.List returnListREP = null;
            DateFormat formatter = new SimpleDateFormat("MM/dd/YYYY");
            
            Calendar cal = Calendar.getInstance();  //Get current date/month 
            cal.add(Calendar.MONTH, -11);   //Go to date, 12 months ago 
            cal.set(Calendar.DAY_OF_MONTH, 1); 
            Date date = cal.getTime();
            String startDt = formatter.format(date);            
            jsonInputGenRep.put("StartDate", startDt);
            
            Calendar endDtCal = Calendar.getInstance();
            date = endDtCal.getTime();
            String today = formatter.format(date);
            jsonInputGenRep.put("EndDate", today);
            
            lstInputGenRep.add(0, EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.GENERATE_REPORTCHART));
            lstInputGenRep.add(1, jsonInputGenRep);
            lstInputGenRep.add(2, startDt);
            if (sessionParam.get("GENERATE_REPORTCHART_RESPONSE") == null || StringUtils.isBlank(String.valueOf(CommonHelper.evaluateEL("#{sessionScope.GENERATE_REPORTCHART_RESPONSE}")))) {
                log.info("session Variable GENERATE_REPORTCHART_RESPONSE is NULL");
                OperationBinding opSR = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
                opSR.getParamsMap().put(EPICConstant.SERVICELIST, lstInputGenRep);
                opSR.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.GENERATE_REPORTCHART);
                opSR.execute();
                returnListREP = (java.util.List) opSR.getResult();
                sessionParam.put("GENERATE_REPORTCHART_RESPONSE", returnListREP); //Storing Serivce Response in Session Variable
            }else
            {  
                log.info("session Variable GET_SR_LIST_RESPONSE is NOT NULL");
                returnListREP = (java.util.List) sessionParam.get("GENERATE_REPORTCHART_RESPONSE");  
            }
            if ((returnListREP != null) && (returnListREP.get(0) != null) &&
                   returnListREP.get(0).toString().equalsIgnoreCase(EPICConstant.TRUE_VAl)) {
                JSONObject jsonObject =new JSONObject(returnListREP.get(1).toString());
                
                log.info("consumption pattern errorMessage"+String.valueOf(jsonObject.getString("ErrorMessage")));
                if(String.valueOf(jsonObject.getString("ErrorCode")).equalsIgnoreCase("0")) {
                    reportAvailableFlg=true;
                    reportParam.put("reportAvailableFlg", true);
                    DCIteratorBinding iter = CommonHelper.findIterator("ReportsChartVO2Iterator");
                    ViewObject obj = iter.getViewObject();
                    java.util.List<ReportEJB> lstReportList = new java.util.ArrayList<ReportEJB>();
                    if (obj.getRowCount() > 0) {
                        RowSetIterator iterR = obj.createRowSetIterator(null);
                        while (iterR.hasNext()) {
                            ReportEJB repBn = new ReportEJB();
                            Row row = iterR.next();
                            repBn.setBookingCount((Number)row.getAttribute("CountVal"));
                            repBn.setBookingMonth(String.valueOf(row.getAttribute("MonthVal")));
                            repBn.setBookingValue(Integer.parseInt(row.getAttribute("CountVal").toString()));
                            lstReportList.add(repBn);
                        }
                    }
                    reportParam.put("reportList", lstReportList);
                    
                }else{
                    reportAvailableFlg=false;
                    reportParam.put("reportAvailableFlg", false);
                }
                     
            }else{
                reportAvailableFlg=false;
                reportParam.put("reportAvailableFlg", false);

            }
        }catch(Exception ex){
            reportDVTPanelBind.setVisible(false);
            reportAvailableFlg=false;
            reportParam.put("reportAvailableFlg", false);
            ex.printStackTrace();
        }*/
        return genReport;
    }
    
    /*   public static void main(String str[]){
        DateFormat formatter = new SimpleDateFormat("MM/dd/YYYY");

        Calendar cal = Calendar.getInstance();  //Get current date/month i.e 27 Feb, 2012
        cal.add(Calendar.MONTH, -11);   //Go to date, 6 months ago 27 July, 2011
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date date = cal.getTime();
        String startDt = formatter.format(date);
        System.out.println("startDt::"+startDt);

        Calendar endDtCal = Calendar.getInstance();
        date = endDtCal.getTime();
        String today = formatter.format(date);
        System.out.println("Enddate::"+today);

        String startMonth="";
        startMonth=startDt.split("/")[0];
        Integer startMonInt=Integer.parseInt(startMonth);
        System.out.println("start month num::"+startMonth);
        HashMap<String, Integer> monthMap = new HashMap<>();
        // Initializing map for 12 months
        for(int i=0;i<12;i++){
            if(startMonInt+i<=12){
                 monthMap.put(Month.of(startMonInt+i).name(), 0);
            }else{
                 for(int k=1;k<startMonInt;k++){
                   monthMap.put(Month.of(k).name(), 0);
                 }
              break;
            }
        }
      //  Iterator<Map.Entry<String, Integer>> monthItr =(Map.Entry<String, Integer>)monthMap.entrySet().iterator();
//        while(monthItr.hasNext())
//           {
        for (Map.Entry<String, Integer> entry : monthMap.entrySet()){
             //    Map.Entry<String, Integer> monEntry =(Map.Entry) monthItr.next();
                 int cnt=Integer.parseInt(String.valueOf(entry.getValue()));

                 monthMap.put(entry.getKey().toString(), cnt+1);

           }

//        Map<String, Integer> map) {
//            for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                System.out.println(entry.getKey() + ":" + entry.getValue());
//            }
        System.out.println("MAP size:::"+monthMap.size());
    } */
    public void setReportDVTPanelBind(RichPanelGroupLayout reportDVTPanelBind) {
        this.reportDVTPanelBind = reportDVTPanelBind;
    }

    public RichPanelGroupLayout getReportDVTPanelBind() {
        return reportDVTPanelBind;
    }

    public void setReportAvailableFlg(Boolean reportAvailableFlg) {
        this.reportAvailableFlg = reportAvailableFlg;
    }

    public Boolean getReportAvailableFlg() {
        return reportAvailableFlg;
    }

    public void setEvalCount(String evalCount) {
        this.evalCount = evalCount;
    }

    public String getEvalCount() {
        evalCount=String.valueOf(CommonHelper.evaluateEL("#{row.bookingCount}"));
        return evalCount;
    }
    public void handleFragmentOnLoad(PollEvent pollEvent) {
        System.out.println("mmmmmmmmmmmmm...");
        try
        {
        if(ADFContext.getCurrent().getSessionScope().get("responseCustChart")!=null)
        {
        Future<String> t=(Future<String>)ADFContext.getCurrent().getSessionScope().get("responseCustChart");
        if(t!=null)
        {
           // if(t.get()!=null)
        System.out.println("t.get()="+t.get());
          //  System.out.println("t.get().String()="+t.get().toString());
        
        if(t.get()!=null || (!t.get().equalsIgnoreCase("")) ) {
           /* System.out.println("true...");
            AdfFacesContext.getCurrentInstance().getPageFlowScope().put("istreadDeal",(t.get().toString()));
            AdfFacesContext.getCurrentInstance().addPartialTarget(txtBindingval);*/
            
           System.out.println("t.get()==="+t.get());
           System.out.println("false..");   
            pollstbinding.clearInitialState();
             pollstbinding.setInterval(-1);
             pollstbinding.setRendered(false);
             AdfFacesContext.getCurrentInstance().addPartialTarget(pollstbinding.getParent().getParent());
            
             log.info("consumption pattern before--==>");
            getPartnerProfileDetails(t.get());
            log.info("consumption pattern after--==>");
             
           
           
            
            AdfFacesContext.getCurrentInstance().addPartialTarget(pg14);
            log.info("consumption pattern after--1==>");
            AdfFacesContext.getCurrentInstance().addPartialTarget(barChart);
            log.info("consumption pattern after--=2=>");
            AdfFacesContext.getCurrentInstance().addPartialTarget(pgCharNotFound);
            log.info("consumption pattern after--==3>");
        }
        
        }
        }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void getPartnerProfileDetails(String jsonObjectStr) {
        log.info("inside generateReportCall");
       // String genReport="goToReportFrg";
        reportAvailableFlg=false;
        System.out.println("***************="+jsonObjectStr);
        try{
           if(jsonObjectStr!=null && !(jsonObjectStr.equalsIgnoreCase("0") || jsonObjectStr.equalsIgnoreCase("-1")) )
           {
               
                
           
                JSONObject jsonObject =new JSONObject(jsonObjectStr);
                
                log.info("consumption pattern errorMessage"+String.valueOf(jsonObject.getString("ErrorMessage")));
                if(String.valueOf(jsonObject.getString("ErrorCode")).equalsIgnoreCase("0")) {
                    log.info("consumption pattern errorMessage--1==>");
                    reportAvailableFlg=true;
                    log.info("consumption pattern errorMessage--2==>");
                   // reportParam.put("reportAvailableFlg", true);
                    CommonHelper.setEL("#{pageFlowScope.reportAvailableFlg}", true);
                    log.info("consumption pattern errorMessage--=3=>");
                    
                        DCIteratorBinding iter = CommonHelper.findIterator("ReportsChartVO2Iterator");
                    log.info("consumption pattern errorMessage--=4=>"+iter);
                        ViewObject vo = iter.getViewObject();
                    log.info("consumption pattern errorMessage--=5=>"+vo);
                   
                    
                    DateFormat formatter = new SimpleDateFormat("MM/dd/YYYY");
                    Calendar cal = Calendar.getInstance(); //Get current date/month
                    cal.add(Calendar.MONTH, -11); //Go to date, 12 months ago
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    java.util.Date date = cal.getTime();
                    String startDt = formatter.format(date);
                    System.out.println("***********start date"+startDt);
                    
                  //  JSONObject jsonObjectReportLists = new JSONObject(reportLists.get(1).toString());
                    Row row1 = null;
                    log.info("return json obj is:::" + jsonObject+" with start month as::"+startDt);
                    String startMonth="";
                    startMonth=startDt.toString().split("/")[0];
                    Integer startMonInt=Integer.parseInt(startMonth);
                    log.info("start month num::"+startMonth);
                    Map<String, Integer> monthMap = new LinkedHashMap<String, Integer>(); 
                    // Initializing map for 12 months
                    for(int i=0;i<12;i++){    
                        if(startMonInt+i<=12){  
                                 monthMap.put(Month.of(startMonInt+i).name(), 0);   
                        }else{
                              for(int k=1;k<startMonInt;k++){   
                                 monthMap.put(Month.of(k).name(), 0);  
                              }
                              break;
                       }
                    }
                    System.out.println("MAP size:::"+monthMap.size());
                    
                    //Getting value from Json response    
                    JSONArray  arrObj = jsonObject.getJSONArray("OrderDetails");
                    for (int i = 1; i <= arrObj.length(); i++) {
                        int k=i-1;
                        JSONObject jsonObjectREP = arrObj.getJSONObject(k);
                        String dt[]=jsonObjectREP.getString("BookingDate").split("/");
                          for (Map.Entry<String, Integer> monEntry : monthMap.entrySet()){
                                 int cnt=Integer.parseInt(String.valueOf(monEntry.getValue()));
                                 if(Month.of(Integer.parseInt(dt[0])).name()
                                            .equalsIgnoreCase(String.valueOf(monEntry.getKey()))){
                                     monthMap.put(Month.of(Integer.parseInt(dt[0])).name(), cnt+1);    
                                 }  
                                  
                             } 

                      }
                    
                    //PUTTING COUNTS in VO
                    int i=3;  // TO REMOVE
                    vo.executeEmptyRowSet();
                    for (Map.Entry<String, Integer> monthItrPutEntry : monthMap.entrySet()){
                           row1 = vo.createRow();
                           System.out.println("Key = " + monthItrPutEntry.getKey() +  
                                                        ", Value = " + monthItrPutEntry.getValue()); 
                           row1.setAttribute("MonthVal",  String.valueOf(monthItrPutEntry.getKey()));
                           row1.setAttribute("BookingCount",String.valueOf(monthItrPutEntry.getValue()));
                           row1.setAttribute("CountVal",Integer.parseInt(String.valueOf(monthItrPutEntry.getValue())));
                    //                   row1.setAttribute("MonthVal",  String.valueOf(monthItrPutEntry.getKey()));
                    //                   row1.setAttribute("BookingCount",String.valueOf(i));
                    //                   row1.setAttribute("CountVal",i);
                    //                   i++;  // TO REMOVE
                           vo.insertRow(row1);
                       } 
                    
                    //reportParam.put("reportList", lstReportList);
                    
                    java.util.List<ReportEJB> lstReportList = new java.util.ArrayList<ReportEJB>();
                    if (vo.getRowCount() > 0) {
                        RowSetIterator iterR = vo.createRowSetIterator(null);
                        while (iterR.hasNext()) {
                            System.out.println("---generateReportCall6>"+iterR);
                                                ReportEJB repBn = new ReportEJB();
                                                System.out.println("---generateReportCall7>"+repBn);
                                                Row row = iterR.next();
                                                System.out.println("---generateReportCall8>"+row);
                                                repBn.setBookingCount((Number)row.getAttribute("CountVal"));
                                                System.out.println("---generateReportCall9>"+(Number)row.getAttribute("CountVal"));
                                                repBn.setBookingMonth(String.valueOf(row.getAttribute("MonthVal")));
                                                System.out.println("---generateReportCall10>"+String.valueOf(row.getAttribute("MonthVal")));
                                                if(row.getAttribute("CountVal")==null || StringUtils.isBlank(row.getAttribute("CountVal").toString()))
                                                {
                                                repBn.setBookingValue(0);
                                                System.out.println("---generateReportCall1 if>");
                                                }
                                                else {
                                                    repBn.setBookingValue(Integer.parseInt(row.getAttribute("CountVal").toString()));
                                                    System.out.println("---generateReportCall1 if>"+String.valueOf(row.getAttribute("CountVal")));
                                                }
                                                lstReportList.add(repBn);
                        }
                    }
                   // reportParam.put("reportList", lstReportList);
                    
                    CommonHelper.setEL("#{pageFlowScope.reportList}", lstReportList);
                    log.info("consumption pattern errorMessage--4==>"+ lstReportList);
                }else{
                    log.info("consumption pattern errorMessage-5-==>");
                    reportAvailableFlg=false;
                  //  reportParam.put("reportAvailableFlg", false);
                  CommonHelper.setEL("#{pageFlowScope.reportAvailableFlg}", false);
                    log.info("consumption pattern errorMessage--==>6");
                }
                     
            }else{
                log.info("consumption pattern errorMessage--==>7");
                reportAvailableFlg=false;
                //reportParam.put("reportAvailableFlg", false);
                CommonHelper.setEL("#{pageFlowScope.reportAvailableFlg}", false);
                log.info("consumption pattern errorMessage--==>8");

            }
        }catch(Exception ex){
           // reportDVTPanelBind.setVisible(false
        
           ex.printStackTrace();
            log.info("consumption pattern errorMessage--==>9"+ex);
            reportAvailableFlg=false;
            //reportParam.put("reportAvailableFlg", false);
            CommonHelper.setEL("#{pageFlowScope.reportAvailableFlg}", false);
            ex.printStackTrace();
        }
       

    }

    public void setPg14(RichPanelGroupLayout pg14) {
        this.pg14 = pg14;
    }

    public RichPanelGroupLayout getPg14() {
        return pg14;
    }

    public void setBarChart(UIBarChart barChart) {
        this.barChart = barChart;
    }

    public UIBarChart getBarChart() {
        return barChart;
    }

    public void setPgCharNotFound(RichPanelGroupLayout pgCharNotFound) {
        this.pgCharNotFound = pgCharNotFound;
    }

    public RichPanelGroupLayout getPgCharNotFound() {
        return pgCharNotFound;
    }
}
