package com.iocl.customer.model.viewobject;

import com.iocl.customer.model.utils.EPICConstant;

import oracle.jbo.Row;
import oracle.jbo.server.ViewObjectImpl;

import ioclcommonproj.com.iocl.utils.JSONArray;
import ioclcommonproj.com.iocl.utils.JSONObject;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sat Apr 28 16:22:15 IST 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class GlassTopLPG_StoveVoImpl extends ViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public GlassTopLPG_StoveVoImpl() {
    }
    
    public void populateGlassTopStove(String jsonInpString) {
        JSONObject jsonResponse = new JSONObject(jsonInpString);
        JSONArray arrObj = jsonResponse.getJSONArray("GlassTopLPGStove");
        this.getViewObject().executeEmptyRowSet();

        for (int i = 0; i < arrObj.length(); i++) {
            JSONObject objOD = arrObj.getJSONObject(i);
            Row row = this.createRow();
            row.setAttribute("Code",
                             objOD.isNull(EPICConstant.CODE) ? EPICConstant.NOT_APPLICABLE :
                             objOD.getString(EPICConstant.CODE).toString());
            row.setAttribute("Type",
                             objOD.isNull(EPICConstant.TYPE) ? EPICConstant.NOT_APPLICABLE :
                             objOD.getString(EPICConstant.TYPE).toString());
            row.setAttribute("Mrp",
                             objOD.isNull(EPICConstant.MRP) ? EPICConstant.NOT_APPLICABLE :
                             objOD.getString(EPICConstant.MRP).toString());
            row.setAttribute("Make",
                             objOD.isNull(EPICConstant.MAKE) ? EPICConstant.NOT_APPLICABLE :
                             objOD.getString(EPICConstant.MAKE).toString());
            row.setAttribute("Brand",
                             objOD.isNull(EPICConstant.BRAND) ? EPICConstant.NOT_APPLICABLE :
                             objOD.getString(EPICConstant.BRAND).toString());
            row.setAttribute("Manufacturing",
                             objOD.isNull(EPICConstant.MANUFACTURING) ? EPICConstant.NOT_APPLICABLE :
                             objOD.getString(EPICConstant.MANUFACTURING).toString());
            row.setAttribute("Description",
                             objOD.isNull(EPICConstant.DESCRIPTION) ? EPICConstant.NOT_APPLICABLE :
                             objOD.getString(EPICConstant.DESCRIPTION).toString());
            this.insertRow(row);
        }

    }
}

