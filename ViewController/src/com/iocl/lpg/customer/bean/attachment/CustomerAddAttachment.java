package com.iocl.lpg.customer.bean.attachment;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;
import com.iocl.lpg.customer.utils.UCMFileForUpload;
import com.iocl.lpg.customer.utils.UCMUtil;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.nav.RichLink;
import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adf.view.rich.event.PopupCanceledEvent;

import oracle.binding.OperationBinding;

import oracle.dss.util.BASE64Encoder;

import oracle.stellent.ridc.IdcClientException;

import oracle.stellent.ridc.model.TransferFile;

import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import ioclcommonproj.com.iocl.utils.JSONArray;
import org.json.JSONException;
import ioclcommonproj.com.iocl.utils.JSONObject;

public class CustomerAddAttachment implements Serializable {
    @SuppressWarnings("compatibility:2202990474569526213")
    private static final long serialVersionUID = 1L;

    public CustomerAddAttachment() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(CustomerAddAttachment.class);

        } else {
            log = Logger.getLogger(CustomerAddAttachment.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    
    private static Logger log ;
    private RichLink attachmentLink;
    private RichImage previewImage;
    private RichImage thumbnailAttachImage;
    private RichPanelGroupLayout removeAttachmentPgl;
    private RichOutputText imageFileLabel;
    private RichPanelGroupLayout addAttachmentPGL;
    private RichPopup imagePreviewPopup;
    private RichPopup popupImage;
    String fileUplaodError;
    private UploadedFile imageFile;
    private RichInputFile fileuploadBinding;
    
    private String confirmationPageMsg;
    private String confirmDestinationLink=CommonHelper.getValueFromRsBundle("BACK_TO_PROFILE");
    private String confirmDestinationLinkDirect="backFromConfirm";
    private String confirmReferenceNum;


    public void onRemoveAttachment(ActionEvent actionEvent) throws IdcClientException, IOException {
        
        // Add event code here...
        java.util.Map sessionparam = ADFContext.getCurrent().getSessionScope();
        java.util.Map pageflowparam = ADFContext.getCurrent().getPageFlowScope();
        log.info("file param to be removed::"+pageflowparam.get("pFileInfo").toString());
        Map<String, String> fileInfo = (Map<String, String>) sessionparam.get(pageflowparam.get("pFileInfo").toString());
        String contentId = fileInfo.get("contentId");
        String dId = fileInfo.get("dId");
        UCMUtil.deleteAttatchment(contentId, dId);
        
        sessionparam.remove(ADFContext.getCurrent()
                                    .getPageFlowScope()
                                    .get("pFileInfo")
                                    .toString());
        
        
        ADFContext.getCurrent()
                  .getSessionScope()
                  .remove(ADFContext.getCurrent()
                                    .getPageFlowScope()
                                    .get("pFileUploadName")
                                    .toString());
        ADFContext.getCurrent()
                  .getSessionScope()
                  .remove(ADFContext.getCurrent()
                                    .getPageFlowScope()
                                    .get("IMAGE_PATH")
                                    .toString());
        ADFContext.getCurrent()
                  .getSessionScope()
                  .remove(ADFContext.getCurrent()
                                    .getPageFlowScope()
                                    .get("pFileUploadNamePre")
                                    .toString());

        java.util.Map sessionParam = ADFContext.getCurrent().getSessionScope();
        sessionParam.remove(ADFContext.getCurrent()
                                      .getPageFlowScope()
                                      .get("pNameFile"));
        sessionParam.remove(ADFContext.getCurrent()
                                      .getPageFlowScope()
                                      .get("pNameExt"));
        sessionParam.remove(ADFContext.getCurrent()
                                      .getPageFlowScope()
                                      .get("pBase64Code"));
        ;

        this.getThumbnailAttachImage().setRendered(false);
        if(this.getFileuploadBinding()!=null)
        this.getFileuploadBinding().setValue(null);
        
        if(this.getPreviewImage()!=null)
        this.getPreviewImage().setSource(null);
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(addAttachmentPGL);
        AdfFacesContext.getCurrentInstance().addPartialTarget(removeAttachmentPgl);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getThumbnailAttachImage());
        if(this.getPreviewImage()!=null)
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPreviewImage());
        
        if(this.getFileuploadBinding()!=null)
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getFileuploadBinding());
    }


    public String uploadImageFileAction() throws IdcClientException, IOException {
        java.util.Map sessionparam = ADFContext.getCurrent().getSessionScope();
        java.util.Map pageflowparam = ADFContext.getCurrent().getPageFlowScope();
        UCMFileForUpload file = (UCMFileForUpload) (pageflowparam.get("uploadedFile"));
        TransferFile primaryFile = new TransferFile(new ByteArrayInputStream(file.getFile()), file.getFileName(), file.getFile().length);            
        primaryFile.setFileName(file.getFileName());            
        primaryFile.setContentType(file.getContentType());            
        primaryFile.setContentLength(file.getFile().length);
        
        String attachmentType = String.valueOf(pageflowparam.get("docType"));
//        String attachmentDocType = String.valueOf(sessionparam.get("selectedAttachmentDocType"));
//        String attachmentDocId = String.valueOf(sessionparam.get("selectedAttachmentDocId"));
        
        String attachmentDocType = String.valueOf(pageflowparam.get("pDocumentType"));
        String attachmentDocId = String.valueOf(pageflowparam.get("pDocumentID"));
        
        System.out.println("attachmentType--"+attachmentType);
        System.out.println("attachmentDocType--"+attachmentDocType);
        System.out.println("attachmentDocId--"+attachmentDocId);
        Map<String, String> fileInfo =  UCMUtil.checkInAttatchment(primaryFile, file.getFileName(), attachmentDocType, attachmentType, attachmentDocId);
        //UCMUtil.uplaodImage(file);
        
       sessionparam.put(pageflowparam.get("pFileInfo").toString(), fileInfo);
        
//       sessionparam.remove("selectedAttachmentDocType");
//       sessionparam.remove("selectedAttachmentDocId");
       
        sessionparam.put(pageflowparam.get("pFileUploadName").toString(),
                         sessionparam.get(pageflowparam.get("pFileUploadNamePre").toString()));
        //fileUpload


        sessionparam.put("pFileBse64", sessionparam.get("pFileBse64Pre"));

        //System.out.println((String) param.get("pFileBse64"));

        sessionparam.put("pFileUploadExt",
                         CommonHelper.getFileExtn(sessionparam.get(pageflowparam.get("pFileUploadNamePre").toString())
                                                  .toString()));

        java.util.Map sessionParam = ADFContext.getCurrent().getSessionScope();

        System.out.println("--?" + sessionparam.get(pageflowparam.get("pFileUploadNamePre").toString()).toString());

        sessionParam.put(ADFContext.getCurrent()
                                   .getPageFlowScope()
                                   .get("pNameFile")
                                   .toString(),
                         String.valueOf(sessionParam.get(EPICConstant.FLOW_NAME))+sessionparam.get(pageflowparam.get("pFileUploadNamePre").toString()).toString().replaceAll("\\s", "").toLowerCase());

        sessionParam.put(ADFContext.getCurrent()
                                   .getPageFlowScope()
                                   .get("pBase64Code")
                                   .toString(), sessionparam.get("pFileBse64Pre").toString());


        sessionParam.put(ADFContext.getCurrent()
                                   .getPageFlowScope()
                                   .get("pNameExt")
                                   .toString(),
                         CommonHelper.getFileExtn(sessionparam.get(pageflowparam.get("pFileUploadNamePre").toString())
                                                  .toString()));


        this.getThumbnailAttachImage().setRendered(true);
        this.getPreviewImage().setRendered(true);
        AdfFacesContext.getCurrentInstance().addPartialTarget(removeAttachmentPgl);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getThumbnailAttachImage());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getFileuploadBinding());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPreviewImage());
        popupImage.hide();

        /*---------------Code to execute WebService in case of Profile Photo Update-------*/
        if (pageflowparam.get("flowInUse") != null && pageflowparam.get("flowInUse")
                                                                             .toString()
                                                                             .equalsIgnoreCase("ProfilePhotoChange")) {
            return updateProfilePhoto();
        }else{
            return "";
        }
       
        /*------------------------------------------End Here-------------------------------*/
    }


    public String updateProfilePhoto() {
        log.info(" inside updateProfilePhoto");
        java.util.Map sessionparam = ADFContext.getCurrent().getSessionScope();
        String errorMessage=null;                
        String retString=null;
        try {
            JSONObject jsonInput = new JSONObject();

            jsonInput.put(EPICConstant.PHOTO_TYPE, "Profile");
            jsonInput.put("TrackingId", CommonHelper.createUniqueID());

            /* /*-----------Code for Photo Upload-------
            JSONArray imgProfile = new JSONArray();
            java.util.Map pageflowparam = ADFContext.getCurrent().getPageFlowScope();
            JSONObject jsonInputimgProfile = new JSONObject();
            jsonInputimgProfile.put(EPICConstant.PROFILE_FILE_NAME,
                                    sessionparam.get(pageflowparam.get("pFileUploadNamePre").toString()).toString());

            jsonInputimgProfile.put(EPICConstant.PROFILE_FILE_ATTACHMENT, sessionparam.get("pFileBse64Pre").toString());

            jsonInputimgProfile.put(EPICConstant.PROFILE_FILEEXT,
                                    CommonHelper.getFileExtn(sessionparam.get(pageflowparam.get("pFileUploadNamePre")
                                                                              .toString()).toString()));
            imgProfile.put(jsonInputimgProfile); */

            jsonInput.put(EPICConstant.PHOTO_DETAILS, getProfilePhotoAttachmentJson());
            /*--------------------End Here------------------*/
            java.util.List lst = new java.util.ArrayList();
            lst.add(0, EPICIOCLResourceCustBundle.findKeyValue(EPICConstant.PROFILE_PHOTO_UPDATE));
            // 1-jsonInput
            lst.add(1, jsonInput);
            
            log.info("jsonInput::"+jsonInput.toString());

            OperationBinding op = CommonHelper.findOperation(EPICConstant.SERVICE_CUSTOMER_NAME);
            op.getParamsMap().put(EPICConstant.SERVICELIST, lst);
            op.getParamsMap().put(EPICConstant.SERVICEMETHOD, EPICConstant.PROFILE_PHOTO_UPDATE);
            op.execute();

            java.util.List ReturnList = (java.util.List) op.getResult();

            if ((ReturnList != null) && (ReturnList.get(0) != null) && (ReturnList.get(0)
                                                                                  .toString()
                                                                                  .equalsIgnoreCase("true"))) {
                log.info("ReturnList Value at 0 index is " + ReturnList.get(0));
                sessionparam.put("ProfilePhotoImagePath", getPreviewPath());
                log.info("ProfilePhotoImagePath" + sessionparam.get("ProfilePhotoImagePath"));
                JSONObject jsonObject =new JSONObject( ReturnList.get(1).toString());
                errorMessage= jsonObject.getString("ErrorMessage").toString();
                log.info("errorMessage in update social pro::"+errorMessage);
                if(errorMessage.equalsIgnoreCase("Success")) {
                    retString="ToConfirmation";
                    confirmationPageMsg=CommonHelper.getValueFromRsBundle("PROFILE_SERVICEREQ_CONFIRM1")+" photo "
                        +CommonHelper.getValueFromRsBundle("PROFILE_SERVICEREQ_CONFIRM2");
                    confirmReferenceNum=" "+String.valueOf(jsonObject.getString("ServiceRequest"));
                }               
            } else {
                JSONObject errMsgObj=(JSONObject)ReturnList.get(1);                    
                CommonHelper.errorMessageCall(String.valueOf(errMsgObj.get(EPICConstant.ERROR_CODE)));
                retString = EPICConstant.ERROR;
                try{
                    ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
                    exct.redirect("/webcenter/portal/LPG/pages_errorpage");
                }catch(IOException ioex){
                    ioex.printStackTrace();
                }
                log.info("Error from WebService ReturnList.get(0) is not true");
            }
            /*----------------------End Here-------------*/

        } catch (JSONException jsone) {
            // TODO: Add catch code
            jsone.printStackTrace();
            log.info("Error in submitPhotoAction method");
            CommonHelper.errorMessageCall(EPICConstant.OTH2);
            try{
                ExternalContext exct = FacesContext.getCurrentInstance().getExternalContext();
                exct.redirect("/webcenter/portal/LPG/pages_errorpage");
            }catch(IOException ioex){
                ioex.printStackTrace();
            }
            retString = EPICConstant.ERROR;
            //                vretString = "ERROR";
        }
        return retString;
    }
    
    public JSONObject getProfilePhotoAttachmentJson(){
        java.util.Map sessionParam = ADFContext.getCurrent().getSessionScope();
        java.util.Map pageflowParam = ADFContext.getCurrent().getPageFlowScope();
       
        JSONObject jsonInputPhotoUpload = new JSONObject();                      
        jsonInputPhotoUpload.put(EPICConstant.ATTACHMENT_FILENAME, sessionParam.get(pageflowParam.get("pNameFile").toString()).toString());
        jsonInputPhotoUpload.put(EPICConstant.ATTACHMENT_EXTENSION, sessionParam.get(pageflowParam.get("pNameExt").toString()).toString());
    //        jsonInputPanUpload.put(EPICConstant.DBC_FILEATTACHMENT_POI, sessionParam.get("dbcPanAtchmntBase64Code"));
        
        jsonInputPhotoUpload.put(EPICConstant.ATTACHMENT_TYPE, EPICConstant.PROFILEIMAGE);
        jsonInputPhotoUpload.put(EPICConstant.ATTACHMENT_DOC_TYPE, EPICConstant.PROFILEIMAGE);
        log.info("Profile update for CId::"+String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerId}")));
        
        jsonInputPhotoUpload.put(EPICConstant.ATTACHMENT_NUMBER, String.valueOf(CommonHelper.evaluateEL("#{sessionScope.userDetails.consumerId}")));
       
        Map<String, String> photoFileInfo =
            (Map<String, String>) (sessionParam.get("PROFILEPHOTO_FILE") == null ? null :
                                   sessionParam.get("PROFILEPHOTO_FILE"));

        String photoContentId = null;
        String photoDocId = null;        
        if(photoFileInfo != null){
            photoContentId = photoFileInfo.get("contentId");
            photoDocId = photoFileInfo.get("dId");
        }
        jsonInputPhotoUpload.put(EPICConstant.ATTACHMENT_WCC_CONTENTID,photoContentId);
        jsonInputPhotoUpload.put(EPICConstant.ATTACHMENT_WCC_DOCID,photoDocId);
        log.info("jsonInputPhotoUpload "+jsonInputPhotoUpload);
        return jsonInputPhotoUpload;
    }

    public void setPopupImage(RichPopup popupImage) {
        this.popupImage = popupImage;
    }

    public RichPopup getPopupImage() {
        return popupImage;
    }

    public void setFileUplaodError(String fileUplaodError) {
        this.fileUplaodError = fileUplaodError;
    }

    public String getFileUplaodError() {
        return fileUplaodError;
    }

    public void setImageFile(UploadedFile imageFile) {
        this.imageFile = imageFile;
    }

    public UploadedFile getImageFile() {
        return imageFile;
    }

    public void setFileuploadBinding(RichInputFile fileuploadBinding) {
        this.fileuploadBinding = fileuploadBinding;
    }

    public RichInputFile getFileuploadBinding() {
        return fileuploadBinding;
    }

    public void onCloseImagePreview(ActionEvent actionEvent) {
        // Add event code here...
        this.getImagePreviewPopup().hide();
    }


    public void changeFileLstnr(ValueChangeEvent valueChangeEvent) {        
        UploadedFile imageFile = (UploadedFile) valueChangeEvent.getNewValue();
        List validFileExtn = new ArrayList();
        //validFileExtn.add(EPICConstant.XLS);
        //validFileExtn.add(EPICConstant.XLSX);
        validFileExtn.add(EPICConstant.PNG);
        validFileExtn.add(EPICConstant.JPG);
        validFileExtn.add(EPICConstant.GIF);
        //validFileExtn.add(EPICConstant.SVG);
        validFileExtn.add(EPICConstant.BMP);
        //validFileExtn.add(EPICConstant.TXT);
        //validFileExtn.add(EPICConstant.DOC);
        //validFileExtn.add(EPICConstant.DOCX);
        validFileExtn.add(EPICConstant.JPEG);
        validFileExtn.add(EPICConstant.PDF);
        double file_size = imageFile.getLength() / (1024 * 1024);
        int dotCount = 0;
        if (imageFile.getFilename() != null)
            dotCount = imageFile.getFilename().length() - imageFile.getFilename()
                                                                   .replaceAll("\\.", "")
                                                                   .length();


        if (imageFile.getFilename() != null &&
            (imageFile.getFilename().startsWith(".") || !imageFile.getFilename().contains(".") || dotCount > 1 ||
             imageFile.getFilename().endsWith("."))) {
            fileUplaodError = CommonHelper.getValueFromRsBundle("NOT_A_VALID_FILE");
            return;
        }


        if (!validFileExtn.contains(CommonHelper.getFileExtn(imageFile.getFilename()))) {
            fileUplaodError = CommonHelper.getValueFromRsBundle("NOT_A_VALID_FILE");
            return;
        }

        if (file_size > EPICConstant.FILE_SIZE) {
            fileUplaodError = CommonHelper.getValueFromRsBundle("FILE_SIZE_EXCEEDS");
            return;
        }
        java.util.Map param = ADFContext.getCurrent().getSessionScope();


        try {

            //log.info(imageFile);

            InputStream is = imageFile.getInputStream();
            param.put(ADFContext.getCurrent()
                                .getPageFlowScope()
                                .get("pFileUploadNamePre")
                                .toString(), imageFile.getFilename());
            //fileUpload

            String base64 = new BASE64Encoder().encode(IOUtils.toByteArray(is));
            // log.info(base64);
            param.put("pFileBse64Pre", base64);


            //String flag = uploadImage(imageFile);
            FileOutputStream newFile = null;
            log.info(">> Attachment: " + imageFile.getFilename());
            String strWriteFilePath =
                EPICIOCLResourceCustBundle.findKeyValue("PROPERTIES_UPLAOD_FILEPATH") + String.valueOf(param.get(EPICConstant.FLOW_NAME))+imageFile.getFilename().replaceAll("\\s", "").toLowerCase();
            newFile = new FileOutputStream(strWriteFilePath);
            IOUtils.copy(imageFile.getInputStream(), newFile);

            param.put(ADFContext.getCurrent()
                                .getPageFlowScope()
                                .get("IMAGE_PATH")
                                .toString(), strWriteFilePath);
            param.put("pUplaodButton", true);


            /* if ("NO".equalsIgnoreCase(flag)) {
            FacesMessage msg =
                new FacesMessage("This is not an Image file, Please upload supported file type (.jpg,.png etc)");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }*/
            param.put("pShowImage", "show");


            param.put("pShowImage", "false");
            param.put("pShowBrowser", "false");
            param.put("pShowupdate", "true");

            log.info(CommonHelper.getFileExtn(imageFile.getFilename()));
            
            UCMFileForUpload primaryFile = new UCMFileForUpload();
            primaryFile.setFileName(imageFile.getFilename());
            primaryFile.setFile(IOUtils.toByteArray(imageFile.getInputStream()));
            primaryFile.setContentType(imageFile.getContentType());
            ADFContext.getCurrent().getPageFlowScope().put("uploadedFile", primaryFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }


    public void setAttachmentLink(RichLink attachmentLink) {
        this.attachmentLink = attachmentLink;
    }

    public RichLink getAttachmentLink() {
        return attachmentLink;
    }

    public void setPreviewImage(RichImage previewImage) {
        this.previewImage = previewImage;
    }

    public RichImage getPreviewImage() {
        return previewImage;
    }

    public void setThumbnailAttachImage(RichImage thumbnailAttachImage) {
        this.thumbnailAttachImage = thumbnailAttachImage;
    }

    public RichImage getThumbnailAttachImage() {
        return thumbnailAttachImage;
    }

    public void setRemoveAttachmentPgl(RichPanelGroupLayout removeAttachmentPgl) {
        this.removeAttachmentPgl = removeAttachmentPgl;
    }

    public RichPanelGroupLayout getRemoveAttachmentPgl() {
        return removeAttachmentPgl;
    }

    public void setImageFileLabel(RichOutputText imageFileLabel) {
        this.imageFileLabel = imageFileLabel;
    }

    public RichOutputText getImageFileLabel() {
        return imageFileLabel;
    }

    public void setAddAttachmentPGL(RichPanelGroupLayout addAttachmentPGL) {
        this.addAttachmentPGL = addAttachmentPGL;
    }

    public RichPanelGroupLayout getAddAttachmentPGL() {
        return addAttachmentPGL;
    }

    public void setImagePreviewPopup(RichPopup imagePreviewPopup) {
        this.imagePreviewPopup = imagePreviewPopup;
    }

    public RichPopup getImagePreviewPopup() {
        return imagePreviewPopup;
    }


    public void onClickAddAttachment(ActionEvent actionEvent) {
        // Add event code here...
        java.util.Map<String, Object> param = ADFContext.getCurrent().getSessionScope();
        param.put(ADFContext.getCurrent()
                            .getPageFlowScope()
                            .get("pFileUploadName")
                            .toString(), null);
        param.put(ADFContext.getCurrent()
                            .getPageFlowScope()
                            .get("IMAGE_PATH")
                            .toString(), null);

        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        popupImage.show(hints);
    }

    public void onClickPreviewImage(ActionEvent actionEvent) {
        // Add event code here...

        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        imagePreviewPopup.show(hints);
    }

    public String getPreviewPath() {
        java.util.Map param = ADFContext.getCurrent().getSessionScope();
        StringBuilder str = new StringBuilder();
        String protocol = CommonHelper.getClientProtocol();
        str.append(protocol.substring(0, protocol.indexOf("/")));
        str.append(EPICConstant.HTTPORHTTPCOLONS);
        str.append(EPICConstant.HTTPORHTTPSUPPORT);
        str.append(CommonHelper.getClientIpAddress());
        str.append(EPICConstant.HTTPORHTTPCOLONS);
        str.append(CommonHelper.getPort());
        str.append(CommonHelper.getClientIpContext());
        str.append(EPICConstant.IMAGE_SERVLET_PATH);
        str.append(CommonHelper.evaluateEL("#{pageFlowScope.IMAGE_PATH}"));
        log.info("pathwithstrinbuilder=" + str.toString());
        log.info("pathwithstrin=" + protocol.substring(0, protocol.indexOf("/")) + EPICConstant.HTTPORHTTPCOLONS +
                 EPICConstant.HTTPORHTTPSUPPORT + EPICConstant.HTTPORHTTPCOLONS + CommonHelper.getPort() +
                 CommonHelper.getClientIpContext() + EPICConstant.IMAGE_SERVLET_PATH +
                 CommonHelper.evaluateEL("#{pageFlowScope.IMAGE_PATH}"));


        log.info("actual=" + EPICIOCLResourceCustBundle.findKeyValue("IMAGE_SERVLET_PREVIEW") +
                 param.get(CommonHelper.evaluateEL("#{pageFlowScope.IMAGE_PATH}")));
        //return str.toString();

        return EPICIOCLResourceCustBundle.findKeyValue("IMAGE_SERVLET_PREVIEW") +
               param.get(CommonHelper.evaluateEL("#{pageFlowScope.IMAGE_PATH}"));

    }

    public String getImagePath() {
        if (ADFContext.getCurrent()
                      .getSessionScope()
                      .get(ADFContext.getCurrent()
                                     .getPageFlowScope()
                                     .get("IMAGE_PATH")) != null)
            return ADFContext.getCurrent()
                             .getSessionScope()
                             .get(ADFContext.getCurrent()
                                            .getPageFlowScope()
                                            .get("IMAGE_PATH")
                                            .toString())
                             .toString();
        else
            return null;
    }

    public String getUploadName() {
        if (ADFContext.getCurrent()
                      .getSessionScope()
                      .get(ADFContext.getCurrent()
                                     .getPageFlowScope()
                                     .get("pFileUploadName")) != null)
            return ADFContext.getCurrent()
                             .getSessionScope()
                             .get(ADFContext.getCurrent()
                                            .getPageFlowScope()
                                            .get("pFileUploadName")
                                            .toString())
                             .toString();
        else
            return null;
    }

    public String getImagePathPre() {
        java.util.Map param = ADFContext.getCurrent().getSessionScope();
        if (ADFContext.getCurrent()
                      .getSessionScope()
                      .get(ADFContext.getCurrent()
                                     .getPageFlowScope()
                                     .get("pFileUploadNamePre")) != null)
            return String.valueOf(param.get(EPICConstant.FLOW_NAME))+ADFContext.getCurrent()
                             .getSessionScope()
                             .get(ADFContext.getCurrent()
                                            .getPageFlowScope()
                                            .get("pFileUploadNamePre")
                                            .toString())
                             .toString().replaceAll("\\s", "").toLowerCase();
        else
            return null;
    }

    public boolean isImgVisible() {
        return ADFContext.getCurrent()
                         .getSessionScope()
                         .get(ADFContext.getCurrent()
                                        .getPageFlowScope()
                                        .get("IMAGE_PATH")) != null ? true : false;
    }

    public boolean isUplaodNameVisible() {
        return ADFContext.getCurrent()
                         .getSessionScope()
                         .get(ADFContext.getCurrent()
                                        .getPageFlowScope()
                                        .get("pFileUploadName")) != null ? true : false;
        
    }

    public void setConfirmationPageMsg(String confirmationPageMsg) {
        this.confirmationPageMsg = confirmationPageMsg;
    }

    public String getConfirmationPageMsg() {
        return confirmationPageMsg;
    }

    public void setConfirmDestinationLink(String confirmDestinationLink) {
        this.confirmDestinationLink = confirmDestinationLink;
    }

    public String getConfirmDestinationLink() {
        return confirmDestinationLink;
    }

    public void setConfirmDestinationLinkDirect(String confirmDestinationLinkDirect) {
        this.confirmDestinationLinkDirect = confirmDestinationLinkDirect;
    }

    public String getConfirmDestinationLinkDirect() {
        return confirmDestinationLinkDirect;
    }

    public void setConfirmReferenceNum(String confirmReferenceNum) {
        this.confirmReferenceNum = confirmReferenceNum;
    }

    public String getConfirmReferenceNum() {
        return confirmReferenceNum;
    }

    public void attachmentCancelLis(PopupCanceledEvent popupCanceledEvent) {
        // Add event code here...
        
        /**-----------Below Code is written to clear image preview on PopUp when  PopUp is close without saving it----------**/
//        CommonHelper.setEL("#{pageFlowScope.IMAGE_PATH}",CommonHelper.createUniqueID());
        ADFContext.getCurrent()
                  .getSessionScope()
                  .remove(ADFContext.getCurrent()
                                    .getPageFlowScope()
                                    .get("IMAGE_PATH")
                                    .toString());

    }
}
