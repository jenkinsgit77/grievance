package com.iocl.customer.model.viewobject;

import java.math.BigDecimal;

import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Aug 16 16:18:52 IST 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class LPGAreaOfficeMasterVORowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    protected enum AttributesEnum {
        AreaOfficeAddress,
        Zipcode,
        Longitude,
        Latitude,
        StateOfficeCode,
        StateOfficeName,
        AreaOfficeCode,
        Sno,
        AreaOfficeName;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        protected int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        protected static final int firstIndex() {
            return firstIndex;
        }

        protected static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        protected static final AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }


    public static final int AREAOFFICEADDRESS = AttributesEnum.AreaOfficeAddress.index();
    public static final int ZIPCODE = AttributesEnum.Zipcode.index();
    public static final int LONGITUDE = AttributesEnum.Longitude.index();
    public static final int LATITUDE = AttributesEnum.Latitude.index();
    public static final int STATEOFFICECODE = AttributesEnum.StateOfficeCode.index();
    public static final int STATEOFFICENAME = AttributesEnum.StateOfficeName.index();
    public static final int AREAOFFICECODE = AttributesEnum.AreaOfficeCode.index();
    public static final int SNO = AttributesEnum.Sno.index();
    public static final int AREAOFFICENAME = AttributesEnum.AreaOfficeName.index();

    /**
     * This is the default constructor (do not remove).
     */
    public LPGAreaOfficeMasterVORowImpl() {
    }
}

