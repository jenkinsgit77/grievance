package com.iocl.customer.model.viewobject;

import java.math.BigDecimal;

import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Feb 13 16:32:53 IST 2020
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class AOLocatorVORowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    protected enum AttributesEnum {
        Dummy,
        Latitude,
        Longitude,
        PhoneNo,
        Address,
        Email,
        OfficeName;
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


    public static final int DUMMY = AttributesEnum.Dummy.index();
    public static final int LATITUDE = AttributesEnum.Latitude.index();
    public static final int LONGITUDE = AttributesEnum.Longitude.index();
    public static final int PHONENO = AttributesEnum.PhoneNo.index();
    public static final int ADDRESS = AttributesEnum.Address.index();
    public static final int EMAIL = AttributesEnum.Email.index();
    public static final int OFFICENAME = AttributesEnum.OfficeName.index();

    /**
     * This is the default constructor (do not remove).
     */
    public AOLocatorVORowImpl() {
    }
}

