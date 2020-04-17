package com.iocl.customer.model.viewobject;

import java.math.BigDecimal;

import java.sql.Timestamp;

import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Jul 12 17:26:38 IST 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class AutolpgDistMasterVoRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    protected enum AttributesEnum {
        DistName,
        UcmUid,
        SapCode,
        SoldToPartyCode,
        MainPhNum,
        MainFaxPhNum,
        MainEmailAddr,
        CustSinceDt,
        Addr,
        State,
        StateName,
        Province,
        Zipcode,
        Longitude,
        Latitude,
        SalesAreaCode,
        SalesAreaName,
        AoCode,
        AoName,
        Sno,
        Distance;
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


    public static final int DISTNAME = AttributesEnum.DistName.index();
    public static final int UCMUID = AttributesEnum.UcmUid.index();
    public static final int SAPCODE = AttributesEnum.SapCode.index();
    public static final int SOLDTOPARTYCODE = AttributesEnum.SoldToPartyCode.index();
    public static final int MAINPHNUM = AttributesEnum.MainPhNum.index();
    public static final int MAINFAXPHNUM = AttributesEnum.MainFaxPhNum.index();
    public static final int MAINEMAILADDR = AttributesEnum.MainEmailAddr.index();
    public static final int CUSTSINCEDT = AttributesEnum.CustSinceDt.index();
    public static final int ADDR = AttributesEnum.Addr.index();
    public static final int STATE = AttributesEnum.State.index();
    public static final int STATENAME = AttributesEnum.StateName.index();
    public static final int PROVINCE = AttributesEnum.Province.index();
    public static final int ZIPCODE = AttributesEnum.Zipcode.index();
    public static final int LONGITUDE = AttributesEnum.Longitude.index();
    public static final int LATITUDE = AttributesEnum.Latitude.index();
    public static final int SALESAREACODE = AttributesEnum.SalesAreaCode.index();
    public static final int SALESAREANAME = AttributesEnum.SalesAreaName.index();
    public static final int AOCODE = AttributesEnum.AoCode.index();
    public static final int AONAME = AttributesEnum.AoName.index();
    public static final int SNO = AttributesEnum.Sno.index();
    public static final int DISTANCE = AttributesEnum.Distance.index();

    /**
     * This is the default constructor (do not remove).
     */
    public AutolpgDistMasterVoRowImpl() {
    }

    /**
     * Gets the attribute value for the calculated attribute DistName.
     * @return the DistName
     */
    public String getDistName() {
        return (String) getAttributeInternal(DISTNAME);
    }

    /**
     * Gets the attribute value for the calculated attribute UcmUid.
     * @return the UcmUid
     */
    public String getUcmUid() {
        return (String) getAttributeInternal(UCMUID);
    }

    /**
     * Gets the attribute value for the calculated attribute SapCode.
     * @return the SapCode
     */
    public String getSapCode() {
        return (String) getAttributeInternal(SAPCODE);
    }

    /**
     * Gets the attribute value for the calculated attribute SoldToPartyCode.
     * @return the SoldToPartyCode
     */
    public String getSoldToPartyCode() {
        return (String) getAttributeInternal(SOLDTOPARTYCODE);
    }

    /**
     * Gets the attribute value for the calculated attribute MainPhNum.
     * @return the MainPhNum
     */
    public String getMainPhNum() {
        return (String) getAttributeInternal(MAINPHNUM);
    }

    /**
     * Gets the attribute value for the calculated attribute MainFaxPhNum.
     * @return the MainFaxPhNum
     */
    public String getMainFaxPhNum() {
        return (String) getAttributeInternal(MAINFAXPHNUM);
    }

    /**
     * Gets the attribute value for the calculated attribute MainEmailAddr.
     * @return the MainEmailAddr
     */
    public String getMainEmailAddr() {
        return (String) getAttributeInternal(MAINEMAILADDR);
    }

    /**
     * Gets the attribute value for the calculated attribute CustSinceDt.
     * @return the CustSinceDt
     */
    public Timestamp getCustSinceDt() {
        return (Timestamp) getAttributeInternal(CUSTSINCEDT);
    }

    /**
     * Gets the attribute value for the calculated attribute Addr.
     * @return the Addr
     */
    public String getAddr() {
        return (String) getAttributeInternal(ADDR);
    }


    /**
     * Gets the attribute value for the calculated attribute State.
     * @return the State
     */
    public String getState() {
        return (String) getAttributeInternal(STATE);
    }

    /**
     * Gets the attribute value for the calculated attribute StateName.
     * @return the StateName
     */
    public String getStateName() {
        return (String) getAttributeInternal(STATENAME);
    }

    /**
     * Gets the attribute value for the calculated attribute Province.
     * @return the Province
     */
    public String getProvince() {
        return (String) getAttributeInternal(PROVINCE);
    }

    /**
     * Gets the attribute value for the calculated attribute Zipcode.
     * @return the Zipcode
     */
    public String getZipcode() {
        return (String) getAttributeInternal(ZIPCODE);
    }

    /**
     * Gets the attribute value for the calculated attribute Longitude.
     * @return the Longitude
     */
    public BigDecimal getLongitude() {
        return (BigDecimal) getAttributeInternal(LONGITUDE);
    }

    /**
     * Gets the attribute value for the calculated attribute Latitude.
     * @return the Latitude
     */
    public BigDecimal getLatitude() {
        return (BigDecimal) getAttributeInternal(LATITUDE);
    }

    /**
     * Gets the attribute value for the calculated attribute SalesAreaCode.
     * @return the SalesAreaCode
     */
    public String getSalesAreaCode() {
        return (String) getAttributeInternal(SALESAREACODE);
    }

    /**
     * Gets the attribute value for the calculated attribute SalesAreaName.
     * @return the SalesAreaName
     */
    public String getSalesAreaName() {
        return (String) getAttributeInternal(SALESAREANAME);
    }

    /**
     * Gets the attribute value for the calculated attribute AoCode.
     * @return the AoCode
     */
    public String getAoCode() {
        return (String) getAttributeInternal(AOCODE);
    }

    /**
     * Gets the attribute value for the calculated attribute AoName.
     * @return the AoName
     */
    public String getAoName() {
        return (String) getAttributeInternal(AONAME);
    }

    /**
     * Gets the attribute value for the calculated attribute Sno.
     * @return the Sno
     */
    public BigDecimal getSno() {
        return (BigDecimal) getAttributeInternal(SNO);
    }

    /**
     * Gets the attribute value for the calculated attribute Distance.
     * @return the Distance
     */
    public BigDecimal getDistance() {
        return (BigDecimal) getAttributeInternal(DISTANCE);
    }
}
