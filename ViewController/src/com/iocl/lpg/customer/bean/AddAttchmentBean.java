package com.iocl.lpg.customer.bean;

import com.iocl.lpg.customer.utils.CommonHelper;
import com.iocl.lpg.customer.utils.EPICConstant;
import com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

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

import oracle.dss.util.BASE64Encoder;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.model.UploadedFile;

public class AddAttchmentBean implements Serializable {
    @SuppressWarnings("compatibility:2202990474569526213")
    private static final long serialVersionUID = 1L;

    public AddAttchmentBean() {
        super();
        String logFlag = EPICIOCLResourceCustBundle.findKeyValue("LOGS_PRINT_FLAG");
        if (logFlag != null && "Y".equalsIgnoreCase(logFlag)) {

            log = Logger.getLogger(AddAttchmentBean.class);

        } else {
            log = Logger.getLogger(AddAttchmentBean.class);
            Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        }
    }
    
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
    
    private static Logger log ;

    public void setImgPreviewServletPath(String imgPreviewServletPath) {
        this.imgPreviewServletPath = imgPreviewServletPath;
    }

    public String getImgPreviewServletPath() {
        return imgPreviewServletPath;
    }
    private String imgPreviewServletPath=com.iocl.lpg.customer.utils.EPICIOCLResourceCustBundle.findKeyValue("IMAGE_SERVLET_PREVIEW");
    
    public void onRemoveAttachment(ActionEvent actionEvent) {
        // Add event code here...
        java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
        param.put("pFileUploadName", null);
        param.put("IMAGE_PATH", null);
        
        java.util.Map sessionParam = ADFContext.getCurrent().getSessionScope();
        sessionParam.remove("pFileUploadName");
        sessionParam.remove("pFileUploadExt");
        sessionParam.remove("pFileBse64");
        
        this.getThumbnailAttachImage().setRendered(false);
        this.getFileuploadBinding().setValue(null);
        this.getPreviewImage().setSource(null);
        AdfFacesContext.getCurrentInstance().addPartialTarget(addAttachmentPGL);
        AdfFacesContext.getCurrentInstance().addPartialTarget(removeAttachmentPgl);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getThumbnailAttachImage());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPreviewImage());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getFileuploadBinding());
    }
    
    
    public void uploadImageFileAction(ActionEvent actionEvent) {
        java.util.Map param = ADFContext.getCurrent().getPageFlowScope();

        //Upload Currently Selected File


        param.put("pFileUploadName", param.get("pFileUploadNamePre"));
        //fileUpload


        log.info((String) param.get("pFileUploadName"));
        param.put("pFileBse64", param.get("pFileBse64Pre"));

        //log.info((String) param.get("pFileBse64"));
        
        param.put("pFileUploadExt", com.iocl.lpg.customer.utils.CommonHelper.getFileExtn(param.get("pFileUploadNamePre").toString()));
        
        java.util.Map sessionParam = ADFContext.getCurrent().getSessionScope();
        
        sessionParam.put("pFileUploadName", param.get("pFileUploadNamePre"));
        
        sessionParam.put("pFileBse64", param.get("pFileBse64Pre"));

        
        sessionParam.put("pFileUploadExt", com.iocl.lpg.customer.utils.CommonHelper.getFileExtn(param.get("pFileUploadNamePre").toString()));
        
        
        this.getThumbnailAttachImage().setRendered(true);
        this.getPreviewImage().setRendered(true);
        AdfFacesContext.getCurrentInstance().addPartialTarget(removeAttachmentPgl);
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getThumbnailAttachImage());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getFileuploadBinding());
        AdfFacesContext.getCurrentInstance().addPartialTarget(this.getPreviewImage());
        popupImage.hide();
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
    
    
    private static final int MAGICPNG[] = new int[] { 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a };
    private static final int MAGICJPG[] = new int[] { 0xff, 0xd8, 0xff};
//    private static final int MAGICDOCXLS[] = new int[] {0xd0,0xcf,0x11,0xe0,0xa1,0xb1,0x1a,0xe1};
//    private static final int MAGICDOCXXLSX[] = new int[] {0x50,0x4b,0x03,0x04,0x05,0x06,0x07,0x08};
//    private static final int MAGICGIF[] = new int[] {0x47,0x49,0x46,0x38,0x37,0x61,0x39};
    private static final int MAGICBMP[] = new int[] {0x42,0x4d};
    private static final int MAGICPDF[] = new int[] {0x25,0x50,0x44,0x46};
//    private static final int MAGICTXT[] = new int[] {0x46,0x4f,0x52,0x4d};
    public void changeFileLstnr(ValueChangeEvent valueChangeEvent) throws IOException {
        UploadedFile imageFile = (UploadedFile) valueChangeEvent.getNewValue();
       
        List validFileExtn = new ArrayList();
        validFileExtn.add(EPICConstant.XLS);
        validFileExtn.add(EPICConstant.XLSX);
        validFileExtn.add(EPICConstant.PNG);
        validFileExtn.add(EPICConstant.JPG);
        validFileExtn.add(EPICConstant.GIF);
       // validFileExtn.add(EPICConstant.SVG); after security issues
        validFileExtn.add(EPICConstant.BMP);
        validFileExtn.add(EPICConstant.TXT);
        validFileExtn.add(EPICConstant.DOC);
        validFileExtn.add(EPICConstant.DOCX);
        validFileExtn.add(EPICConstant.JPEG);
        validFileExtn.add(EPICConstant.PDF);
        double file_size = imageFile.getLength() / (1024 * 1024);
//        Path source = Paths.get("c:/temp/0multipage.tif");
//       String type = imageFile.getContentType();
        InputStream isStr = null;
        try {
           // ins = new FileInputStream("C:\\Testing\\Images\\interCompanyPortabilityCopy.png");
            isStr = imageFile.getInputStream();
        } catch (FileNotFoundException e) {
        }
        catch (IOException e) {
                }
        
        int dotCount=0;
        
        if(imageFile.getFilename()!=null)
         dotCount = imageFile.getFilename().length()-imageFile.getFilename().replaceAll("\\.","").length();
        
        
       
        
        if(imageFile.getFilename()!=null &&  (imageFile.getFilename().startsWith(".") || !imageFile.getFilename().contains(".") || dotCount>1 || imageFile.getFilename().endsWith("."))) {
            fileUplaodError = CommonHelper.getValueFromRsBundle("NOT_A_VALID_FILE");
            return; 
        }
        
        if (file_size > EPICConstant.FILE_SIZE) {
            fileUplaodError = CommonHelper.getValueFromRsBundle("FILE_SIZE_EXCEEDS");
            return;
        }

        if (!validFileExtn.contains(CommonHelper.getFileExtn(imageFile.getFilename()))) {
            fileUplaodError = CommonHelper.getValueFromRsBundle("NOT_A_VALID_FILE");
            return;
        }

        if (CommonHelper.getFileExtn(imageFile.getFilename()).equalsIgnoreCase(EPICConstant.JPG) ||
            CommonHelper.getFileExtn(imageFile.getFilename()).equalsIgnoreCase(EPICConstant.JPEG)) {
            try {
                for (int i = 0; i < MAGICJPG.length; ++i) {


                    if (isStr.read() != MAGICJPG[i]) {
                        log.info("Not a valid JPG/JPEG");
                        fileUplaodError = "Not a valid JPG/JPEG";
                        return;
                    }
                }
            } finally {
                isStr.close();
            }

        }
        if (CommonHelper.getFileExtn(imageFile.getFilename()).equalsIgnoreCase(EPICConstant.PNG)) {
            try {
                for (int i = 0; i < MAGICPNG.length; ++i) {


                    if (isStr.read() != MAGICPNG[i]) {
                        log.info("Not a valid PNG");
                        fileUplaodError = "Not a valid PNG";
                        return;
                    }
                }
            } finally {
                isStr.close();
            }


        }


        if (CommonHelper.getFileExtn(imageFile.getFilename()).equalsIgnoreCase(EPICConstant.BMP)) {
            try {
                for (int i = 0; i < MAGICBMP.length; ++i) {

                    if (isStr.read() != MAGICBMP[i]) {
                        log.info("Not a valid BMP");
                        fileUplaodError = "Not a valid BMP";
                        return;
                    }
                }
            } finally {
                isStr.close();
            }


        }

        if (CommonHelper.getFileExtn(imageFile.getFilename()).equalsIgnoreCase(EPICConstant.PDF)) {
            try {

                for (int i = 0; i < MAGICPDF.length; ++i) {

                    if (isStr.read() != MAGICPDF[i]) {
                        log.info("Not a valid PDF");
                        fileUplaodError = "Not a valid PDF";
                        return;
                    }
                }
            } finally {
                isStr.close();
            }


        }

        java.util.Map param = ADFContext.getCurrent().getPageFlowScope();


        try {

            //log.info(imageFile);

            InputStream is = imageFile.getInputStream();
            
            param.put("pFileUploadNamePre", imageFile.getFilename());
            //fileUpload

            String base64 = new BASE64Encoder().encode(IOUtils.toByteArray(is));
           // log.info(base64);
            param.put("pFileBse64Pre", base64);


            //String flag = uploadImage(imageFile);
            FileOutputStream newFile = null;
            log.info(">> Attachment: " + imageFile.getFilename());
            String strWriteFilePath = EPICIOCLResourceCustBundle.findKeyValue("PROPERTIES_UPLAOD_FILEPATH") + imageFile.getFilename();
            newFile = new FileOutputStream(strWriteFilePath);
            IOUtils.copy(imageFile.getInputStream(), newFile);

            param.put("IMAGE_PATH", strWriteFilePath);
            param.put("pUplaodButton",true);
            
            
            param.put("pShowImage", "show");


            param.put("pShowImage", "false");
            param.put("pShowBrowser", "false");
            param.put("pShowupdate", "true");
            
            log.info(CommonHelper.getFileExtn(imageFile.getFilename()));

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
        java.util.Map param = ADFContext.getCurrent().getPageFlowScope();
        param.put("pFileUploadName", null);
        param.put("IMAGE_PATH", null);
        
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        popupImage.show(hints);
    }

    public void onClickPreviewImage(ActionEvent actionEvent) {
        // Add event code here...
        
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        imagePreviewPopup.show(hints);
    }
}
