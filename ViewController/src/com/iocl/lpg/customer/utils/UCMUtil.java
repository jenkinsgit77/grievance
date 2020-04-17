package com.iocl.lpg.customer.utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.adf.share.ADFContext;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.model.TransferFile;
import oracle.stellent.ridc.protocol.ServiceResponse;

import org.apache.log4j.Logger;

public class UCMUtil implements Serializable{
    private static Logger log = Logger.getLogger(UCMUtil.class);
    private static IdcClient idcClient;
    private static IdcContext userContext;
    public UCMUtil() {
        super();
    }

    


    //Method added to get content by ID
    public static String getUCMContentById(String contentId) throws IdcClientException, IOException {

        String docID = null;

        DataResultSet resultset = searchResultAsResultSet(contentId);


        for (DataObject dataObject : resultset.getRows()) {
            docID = dataObject.get("dID");
        }

        ServiceResponse symResponse = getDocAsServiceResponseByID(docID);


        System.out.println("getApplicationPropertiesFromUCM::" + symResponse.getResponseType() + "--1--" +
                           symResponse.getHeader("Content-type") + "--2---" + symResponse.getHeader("idc-file") +
                           "---4---" + symResponse.isReleased());
        String charContent = null;
        if (symResponse.getHeader("idc-file") != null) {
            charContent = symResponse.getResponseAsString();
            System.out.println("content--" + charContent);
        }

        closeStream(symResponse);

        return charContent;
    }

    public static String getUCMContent(String content) throws IdcClientException, IOException {

        String docID = null;

        DataResultSet resultset = searchResultAsResultSet("dDocTitle <matches>`" + content + "`", "1");

        for (DataObject dataObject : resultset.getRows()) {
            docID = dataObject.get("dID");
        }

        ServiceResponse symResponse = getDocAsServiceResponseByID(docID);


        System.out.println("getApplicationPropertiesFromUCM::" + symResponse.getResponseType() + "--1--" +
                           symResponse.getHeader("Content-type") + "--2---" + symResponse.getHeader("idc-file") +
                           "---4---" + symResponse.isReleased());
        String charContent = null;
        if (symResponse.getHeader("idc-file") != null) {
            charContent = symResponse.getResponseAsString();
            System.out.println("content--" + charContent);
        }

        closeStream(symResponse);

        return charContent;
    }


    public static IdcContext getUserContext() {
        return userContext;
    }

    private static DataResultSet searchResultAsResultSet(String queryText,
                                                         String resultCount) throws IdcClientException {
        long before = System.currentTimeMillis();
        ServiceResponse symResponse =
            getIdcClientInstance()
            .sendRequest(getUserContext(), getDataBinderForGET_SEARCH_RESULTS(queryText, resultCount));
        DataResultSet resultset = symResponse.getResponseAsBinder().getResultSet("SearchResults");
        closeStream(symResponse);
        System.out.println("UCMUtil:searchResultAsResultSet" + ": MilliSec - " + (System.currentTimeMillis() - before));

        return resultset;
    }

    private static IdcClient getIdcClientInstance() {
        if (null == idcClient) {
            String idcConnectionURL = EPICIOCLResourceCustBundle.findKeyValue("IDC_URL");
            String username = EPICIOCLResourceCustBundle.findKeyValue("IDC_USERNAME");
            String password = EPICIOCLResourceCustBundle.findKeyValue("IDC_PASSWORD");
            try {
                IdcClientManager clientManager = new IdcClientManager();
                idcClient = clientManager.createClient(idcConnectionURL);
//                idcClient.getConfig().setProperty("http.library", RIDCHttpConstants.HttpLibrary.apache4.name());//line to be added for cluster;
                userContext = new IdcContext(username, password);
            } catch (IdcClientException e) {
                System.out.println("Error creating IdcClient! Application will not work properly. Please fix the issue and restart application");
            }

        }
        return idcClient;
    }

    private static DataBinder getDataBinderForGET_SEARCH_RESULTS(String queryText, String resultCount) {
        long before = System.currentTimeMillis();
        DataBinder dataBinder = getIdcClientInstance().createBinder();

        dataBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
        dataBinder.putLocal("QueryText", queryText);
        dataBinder.putLocal("ResultCount", resultCount);

        return dataBinder;
    }

    private static void closeStream(ServiceResponse symResponse) {
        if (null != symResponse) {
            InputStream inputStream = symResponse.getResponseStream();
            if (null != inputStream)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Unable to close UCM InputStream!!!!");
                }
        }
    }

    private static ServiceResponse getDocAsServiceResponseByID(String docID) throws IdcClientException {
        long before = System.currentTimeMillis();
        ServiceResponse symResponse =
            getIdcClientInstance().sendRequest(getUserContext(), getDataBinderForGET_FILEByDocID(docID));
        System.out.println("UCMUtil:getDocAsServiceResponseBgetDataBinderForGET_SEARCH_RESULTSyID" + ": MilliSec - " +
                           (System.currentTimeMillis() - before));

        return symResponse;
    }

    private static DataBinder getDataBinderForGET_FILEByDocID(String docID) {
        long before = System.currentTimeMillis();
        DataBinder dataBinder = getIdcClientInstance().createBinder();

        dataBinder.putLocal("IdcService", "GET_FILE");
        dataBinder.putLocal("dID", docID);
        System.out.println("UCMUtil:getDataBinderForGET_FILEByDocID" + ": MilliSec - " +
                           (System.currentTimeMillis() - before));

        return dataBinder;
    }

    public static InputStream getFile(String documentId) {
        ServiceResponse severiceResponse = null;
        String username = EPICIOCLResourceCustBundle.findKeyValue("IDC_USERNAME");
        String password = EPICIOCLResourceCustBundle.findKeyValue("IDC_PASSWORD");

        try {
            IdcClient client = getIdcClientInstance();
            DataBinder dataBinderReq = client.createBinder();
            dataBinderReq.putLocal("IdcService", "GET_FILE");
            dataBinderReq.putLocal("dID", documentId);

            severiceResponse = client.sendRequest(new IdcContext(username,password), dataBinderReq);
            InputStream is = severiceResponse.getResponseStream();
            System.out.println("GET_FILE size: " + is.available());
            return is;

        } catch (Exception ex) {
            System.out.println("Error GetFile(): " + ex.getMessage());
        }
        return null;
    }

    private static DataBinder getDataBinderForGET_SEARCH_URL(String docName) {
        DataBinder dataBinder = getIdcClientInstance().createBinder();

        dataBinder.putLocal("IdcService", "DOC_INFO_BY_NAME");
        dataBinder.putLocal("dDocName", docName);

        return dataBinder;
    }

    public static String findURL(String dDocName) throws IdcClientException {
        String url = null;
        try {
            long before = System.currentTimeMillis();
            ServiceResponse symResponse =
                getIdcClientInstance().sendRequest(getUserContext(), getDataBinderForGET_SEARCH_URL(dDocName));
            DataBinder serverBinder = symResponse.getResponseAsBinder();
            Map localData = serverBinder.getLocalData();
            System.out.println("---->" + localData.get("DocUrl").toString());
            url = localData.get("DocUrl").toString();
            closeStream(symResponse);
            System.out.println("UCMUtil:searchResultAsResultSet" + ": MilliSec - " +
                               (System.currentTimeMillis() - before));
        } catch (IdcClientException ice) {
            // TODO: Add catch code
            ice.printStackTrace();
        }
        return url;
    }

    private static DataResultSet searchResultAsResultSet(String dDocName) throws IdcClientException {
        long before = System.currentTimeMillis();
        log.info("inside searchResultAsResultSet of contentid::"+dDocName);
        ServiceResponse symResponse =
            getIdcClientInstance().sendRequest(getUserContext(), getDataBinderForGET_SEARCH_RESULTS(dDocName));
        /*  DataBinder serverBinder = symResponse.getResponseAsBinder();
           Map localData = serverBinder.getLocalData();
           for( Object key : localData.keySet()) {
           System.out.println(" Key "+key +" Value "+localData.get(key));
           }*/

        DataResultSet resultset = symResponse.getResponseAsBinder().getResultSet("DOC_INFO");
        closeStream(symResponse);
        System.out.println("UCMUtil:searchResultAsResultSet" + ": MilliSec - " + (System.currentTimeMillis() - before));

        return resultset;
    }

    private static DataBinder getDataBinderForGET_SEARCH_RESULTS(String docName) {
        DataBinder dataBinder = getIdcClientInstance().createBinder();

        dataBinder.putLocal("IdcService", "DOC_INFO_BY_NAME");
        dataBinder.putLocal("dDocName", docName);

        return dataBinder;
    }

    public static List<DataObject> listItems(String path) throws IdcClientException {
        List<DataObject> imageURL = new java.util.ArrayList<DataObject>();

        DataBinder dataBinder = getIdcClientInstance().createBinder();
        dataBinder.putLocal("IdcService", "FLD_BROWSE");

        //   dataBinder.putLocal("path", "/LPG/Home/Test");
        dataBinder.putLocal("path", path);


        ServiceResponse response = getIdcClientInstance().sendRequest(getUserContext(), dataBinder);

        DataBinder responseBinder = response.getResponseAsBinder();
        DataResultSet drs = responseBinder.getResultSet("ChildFiles");

        System.out.println("*************** FETCH FROM server **************");


        System.out.println("drs.getRows().size() ::" + drs.getRows().size());
        if ((drs.getRows().size() > 0)) {
            imageURL = drs.getRows();
        }
        return imageURL;
    }
    
    public static List<String> fetchList(String path) throws IdcClientException {
        java.util.List<String> lstImage =new java.util.ArrayList<String>();
        
        List <DataObject> lst=listItems(path);
        for(int i=0;i<lst.size();i++) {
           DataObject  ds=lst.get(i);
            lstImage.add(  findURL(ds.get("dDocName")));
        }
        return lstImage;
    }
   
    public static DataResultSet fetchUCMMetadata(String contentId) throws IdcClientException {
        DataResultSet dataSet=null;
        String docID=null;
        try {
            dataSet = searchResultAsResultSet(contentId);
            System.out.println("DataResultSet size in fetchUCMMetadata::"+dataSet);
            for (DataObject dataObject : dataSet.getRows()) {
                docID = dataObject.get("dID");
             }
        }catch(IdcClientException ice) {
            // TODO: Add catch code
            log.info("Custom Log in Exception: Unable to find latest revision for contentId: "+contentId);
//            ice.printStackTrace(); //Getting a lot of logs repeatedly
        }
        return dataSet;
    }
    
    public static String returnDocId(String contentId) throws IdcClientException {
        String docID="WCC006618";   // For Profile photo dummy user
        DataResultSet dataSetDocId=null;
        try{
        dataSetDocId = searchResultAsResultSet(contentId);
        for (DataObject dataObject : dataSetDocId.getRows()) {
            docID = dataObject.get("dID");
        }        
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return docID;
    }
    
    public static String returnDocExtension(String contentId) throws IdcClientException {
        log.info("Extension to search of contentid from Customer::"+contentId);
        String docIDExtension="jpg";
        DataResultSet dataSetDocId=null;
        try{
            dataSetDocId = searchResultAsResultSet(contentId);
//            for (DataObject dataObject : dataSetDocId.getRows()) {
//                docIDExtension = dataObject.get("dExtension");
//            }
            DataObject dataObject = dataSetDocId.getRows().get(dataSetDocId.getRows().size() - 1);           
            docIDExtension = dataObject.get("dExtension"); 
        }catch(Exception ex){
            log.info("No content or exception occured in returnDocExtension, setting default jpg in Customer, exception is::"+ex);
           
        }
        return docIDExtension;
    }
    
    public static DataResultSet fetchUCMxProductData(String[] xProductCat) throws IdcClientException {
        DataResultSet dataSet=null;
        String docID=null;
        dataSet = searchResultMetadataAsResultSet(xProductCat);

        for (DataObject dataObject : dataSet.getRows()) {
            docID = dataObject.get("dID");
        }

        return dataSet;
    }
    
    private static DataResultSet searchResultMetadataAsResultSet(String[] xProductCategory) throws IdcClientException {
        long before = System.currentTimeMillis();
        ServiceResponse symResponse =
            getIdcClientInstance().sendRequest(getUserContext(), getDataBinderForGET_SEARCH_RESULTSNFRCATEGORY(xProductCategory));
        /*  DataBinder serverBinder = symResponse.getResponseAsBinder();
           Map localData = serverBinder.getLocalData();
           for( Object key : localData.keySet()) {
           System.out.println(" Key "+key +" Value "+localData.get(key));
           }*/

        DataResultSet resultset = symResponse.getResponseAsBinder().getResultSet("DOC_INFO");
        closeStream(symResponse);
        System.out.println("UCMUtil:searchResultAsResultSet" + ": MilliSec - " + (System.currentTimeMillis() - before));

        return resultset;
    }
    
    private static DataBinder getDataBinderForGET_SEARCH_RESULTSNFRCATEGORY(String[] xProductCategory) {
        DataBinder dataBinder = getIdcClientInstance().createBinder();

        dataBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
        String dataQuery="xCategory <matches> `"+xProductCategory[0]+"` <AND> xProduct <matches> `"+xProductCategory[1]+"/'/";
        dataBinder.putLocal ("QueryText", dataQuery);
       

        return dataBinder;
    }


    public static Map<String, String> checkInAttatchment(TransferFile file, String docTitle, String documentSubType,
                                                         String documentType,
                                                         String documentId) throws IdcClientException, IOException {
        log.info("Inside checkInAttatchment. Preparing to check in attatchment to WCC");
        Object ucmId =CommonHelper.evaluateEL("#{sessionScope.userDetails.ucmID}");
        String folderId = EPICIOCLResourceCustBundle.findKeyValue("FOLDER_GUID");
        log.info("folderGuId----"+folderId);
        idcClient = getIdcClientInstance();
        DataBinder dataBinder = idcClient.createBinder();
        dataBinder.putLocal("IdcService", "CHECKIN_NEW");
        //dataBinder.putLocal("fParentGUID", folderId);
        dataBinder.putLocal("dDocTitle", String.valueOf(ADFContext.getCurrent().getSessionScope().get(EPICConstant.FLOW_NAME))+docTitle.replaceAll("\\s", "").toLowerCase());
        // dataBinder.putLocal("dDocName", docName);
        dataBinder.putLocal("dDocAuthor", "wccadmin");
        dataBinder.putLocal("dDocType", "SiebelAttachment");
        // dataBinder.putLocal("dRevLabel", "1");
        dataBinder.putLocal("dSecurityGroup", "SiebelDocuments");
        dataBinder.putLocal("xsiebelDocumentSubType", documentSubType);
        dataBinder.putLocal("xsiebelDocumentType", documentType);
        dataBinder.putLocal("xsiebelDocumentId", documentId);
        dataBinder.putLocal("xSource", "Portal");
        dataBinder.putLocal("xcustomerId",String.valueOf(ucmId));
        dataBinder.putLocal("xIdcProfile", "SiebelDocProfile");
        dataBinder.addFile("primaryFile", file);
       

        ServiceResponse response = idcClient.sendRequest(userContext, dataBinder);
        DataBinder responseBinder = response.getResponseAsBinder();
        //Collection<String> did = responseBinder.getResultSetNames();

        String ucmDId = responseBinder.getLocalData().get("dID");
        String ucmContentId = responseBinder.getLocalData().get("dDocName");

        Map<String, String> fileInfo = new HashMap<String, String>();
        fileInfo.put("dId", ucmDId);
        fileInfo.put("contentId", ucmContentId);
        log.info(" File checked in and document ID= :" + responseBinder.getLocalData().get("dID"));
        if (response != null) {
            response.close();
        }
        //dPrimaryFile.delete();
        return fileInfo;
    }

    public static void deleteAttatchment(String contentId, String dId) throws IdcClientException, IOException {
        ServiceResponse response = null;
        try {
            DataResultSet resultset = searchResultAsResultSet(contentId);

            if(resultset!=null)
            {
            idcClient = getIdcClientInstance();
            DataBinder dataBinder = idcClient.createBinder();
            dataBinder.putLocal("IdcService", "DELETE_DOC");
            dataBinder.putLocal("dID", dId);
            dataBinder.putLocal("dDocName", contentId);
            response = idcClient.sendRequest(userContext, dataBinder);
            DataBinder responseBinder = response.getResponseAsBinder();
            log.info("File deleted successfully");
            }
        } catch (IdcClientException idcce) {
            log.info("IDC Client Exception occurred. Unable to delete file. Message: " + idcce.getMessage() +
                               ", Stack trace: ");
            idcce.printStackTrace();
        } catch (Exception e) {
            log.info("Exception occurred. Unable to delete file. Message: " + e.getMessage() +
                               ", Stack trace: ");
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
    
    public static List<String> fetchContentIdList(String path)  {
               log.info("-------inside fetchContentIdList in UCMUtils-------");
               java.util.List<String> lstContent = new java.util.ArrayList<String>();
               try
               {
               log.info("Start fetchContentIdList");
               log.info("path: " + path);

               List<DataObject> lst = listItems(path);
               for (int i = 0; i < lst.size(); i++) {
                   DataObject ds = lst.get(i);
                   log.info("dDocName" + ds.get("dDocName"));
                   lstContent.add(ds.get("dDocName"));
               }
               log.info("lstContent" + lstContent);
               for (String abc : lstContent) {
                   log.info(abc);
               }
               log.info("End fetchContentIdList");
               }catch(Exception e) {
                   e.printStackTrace();
               }
               return lstContent;
           }
    
    

}

