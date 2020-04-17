package com.iocl.lpg.customer.utils;

//import com.iocl.lpg.utils.image.ViewImageServlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class PGInterceptServlet extends HttpServlet {
    public PGInterceptServlet() {
        super();
    }
    
    private static Logger log = Logger.getLogger(PGInterceptServlet.class);
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    
    public void doPost(HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) throws ServletException, IOException {
        
        String encResp= httpServletRequest.getParameter("encResp");
        String encData= httpServletRequest.getParameter("encData");
        if(encResp != null)
        httpServletResponse.sendRedirect("http://localhost:7101/EPICIOCL/faces/LoginTFpage/PGResponsePage.jspx?encResp="+encResp);
        if(encData != null){
            String encDataRefined = encData.replaceAll("\\+", "%2B");
        httpServletResponse.sendRedirect("http://localhost:7101/EPICIOCL/faces/LoginTFpage/PGResponsePage.jspx?encData="+encDataRefined);
        }
        
    }

    
    public void doGet(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) throws ServletException, IOException {
    
        String encResp= httpServletRequest.getParameter("encResp");
        System.out.println("encResp--"+encResp);
    }
}
