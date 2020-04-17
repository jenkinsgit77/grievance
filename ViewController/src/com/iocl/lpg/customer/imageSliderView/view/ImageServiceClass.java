//package view;
//
//import java.util.AbstractList;
//import java.util.ArrayList;
//import java.util.List;
//
//import oracle.stellent.ridc.IdcClientException;
//import oracle.stellent.ridc.model.DataObject;
//
//public class ImageServiceClass {
//   
//    
//    private java.util.List<ImageInfo> imageCR;
//    
//    public ImageServiceClass() {
//        this.imageCR=new ArrayList<ImageInfo>();
//      //  imageCR.add(new ImageInfo("http://laoblogger.com/images/jpg-images-of-flowers-1.jpg"));
//        //imageCR.add(new ImageInfo("http://laoblogger.com/images/images-of-flowers-and-plants-2.jpg"));
//        
//        List <DataObject> lst;
//        try {
//            lst = UCMUtil.getImageListInstance();
//        
//        for(int i=0;i<lst.size();i++) {
//            DataObject  ds=lst.get(i);
//            System.out.println("-------->"+ds.get("dDocName"));
//          imageCR.add( UCMUtil.searchResultAsResultSet2(ds.get("dDocName")));
//           
//                
//        }
//            
//        } catch (Exception e) {
//        }
//        
//    }
//
//
//    public void setImageCR(List<ImageInfo> imageCR) {
//        this.imageCR = imageCR;
//    }
//
//    public List<ImageInfo> getImageCR() {
//        return imageCR;
//    }
//}
