package com.iocl.customer.model.service.common;

import java.util.HashMap;
import java.util.List;

import oracle.jbo.ApplicationModule;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri Mar 16 17:51:41 IST 2018
// ---------------------------------------------------------------------
public interface AppModule extends ApplicationModule {
    void fetchApplist(String flowCode);

    List serviceCustomerCall(List inputList, String method);

    void fetchApplistpartner(String flowCode);

    List updatePaymentDetailsInCRM(List inputList);
    
    void fetchErrorMessage(String errorCode);

    void initLpgDistVo(String ftlFlag, String zipcodeFilter);

    void fetchAppListLpgLogIn();

    void fetchAppListCustomerNonLogin(String pflowCode);


    List initiatePaymentDetails(List inputList);

    List callChartAsync(List inputList);

    void insertIntoRoLocator(List jsonArrayList);

    void insertVehicleTypeRecord(String count);

    void insertProfileEnrichInfo(String FirstName, String LastName, String EmailId, String MobileNo);

    void insertRetailEnrichInfo(String FirstName, String LastName, String EmailId, String MobileNo);

    void insertIntoAoLocator(List jsonArrayList);

    void insertIntoPartnerDistLocator(List jsonArrayList);

    void insertIntoPartnerStockLocator(List jsonArrayList);
}
