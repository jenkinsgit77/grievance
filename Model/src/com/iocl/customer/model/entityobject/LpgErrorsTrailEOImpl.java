package com.iocl.customer.model.entityobject;

import java.sql.Timestamp;

import oracle.jbo.AttributeList;
import oracle.jbo.Key;
import oracle.jbo.domain.BlobDomain;
import oracle.jbo.server.EntityDefImpl;
import oracle.jbo.server.EntityImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Nov 18 16:27:12 IST 2019
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class LpgErrorsTrailEOImpl extends EntityImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    protected enum AttributesEnum {
        TrackingId,
        ServiceInput,
        ServiceOutput,
        LpgErrorCode,
        LpgErrorDesc,
        Time,
        ServiceUrl,
        UserSessionTrackingId,
        UserId,
        HeaderAttribute,
        SvcRespTime;
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
    public static final int TRACKINGID = AttributesEnum.TrackingId.index();
    public static final int SERVICEINPUT = AttributesEnum.ServiceInput.index();
    public static final int SERVICEOUTPUT = AttributesEnum.ServiceOutput.index();
    public static final int LPGERRORCODE = AttributesEnum.LpgErrorCode.index();
    public static final int LPGERRORDESC = AttributesEnum.LpgErrorDesc.index();
    public static final int TIME = AttributesEnum.Time.index();
    public static final int SERVICEURL = AttributesEnum.ServiceUrl.index();
    public static final int USERSESSIONTRACKINGID = AttributesEnum.UserSessionTrackingId.index();
    public static final int USERID = AttributesEnum.UserId.index();
    public static final int HEADERATTRIBUTE = AttributesEnum.HeaderAttribute.index();
    public static final int SVCRESPTIME = AttributesEnum.SvcRespTime.index();

    /**
     * This is the default constructor (do not remove).
     */
    public LpgErrorsTrailEOImpl() {
    }

    /**
     * Gets the attribute value for TrackingId, using the alias name TrackingId.
     * @return the value of TrackingId
     */
    public String getTrackingId() {
        return (String) getAttributeInternal(TRACKINGID);
    }

    /**
     * Sets <code>value</code> as the attribute value for TrackingId.
     * @param value value to set the TrackingId
     */
    public void setTrackingId(String value) {
        setAttributeInternal(TRACKINGID, value);
    }

    /**
     * Gets the attribute value for ServiceInput, using the alias name ServiceInput.
     * @return the value of ServiceInput
     */
    public BlobDomain getServiceInput() {
        return (BlobDomain) getAttributeInternal(SERVICEINPUT);
    }

    /**
     * Sets <code>value</code> as the attribute value for ServiceInput.
     * @param value value to set the ServiceInput
     */
    public void setServiceInput(BlobDomain value) {
        setAttributeInternal(SERVICEINPUT, value);
    }

    /**
     * Gets the attribute value for ServiceOutput, using the alias name ServiceOutput.
     * @return the value of ServiceOutput
     */
    public BlobDomain getServiceOutput() {
        return (BlobDomain) getAttributeInternal(SERVICEOUTPUT);
    }

    /**
     * Sets <code>value</code> as the attribute value for ServiceOutput.
     * @param value value to set the ServiceOutput
     */
    public void setServiceOutput(BlobDomain value) {
        setAttributeInternal(SERVICEOUTPUT, value);
    }

    /**
     * Gets the attribute value for LpgErrorCode, using the alias name LpgErrorCode.
     * @return the value of LpgErrorCode
     */
    public String getLpgErrorCode() {
        return (String) getAttributeInternal(LPGERRORCODE);
    }

    /**
     * Sets <code>value</code> as the attribute value for LpgErrorCode.
     * @param value value to set the LpgErrorCode
     */
    public void setLpgErrorCode(String value) {
        setAttributeInternal(LPGERRORCODE, value);
    }

    /**
     * Gets the attribute value for LpgErrorDesc, using the alias name LpgErrorDesc.
     * @return the value of LpgErrorDesc
     */
    public String getLpgErrorDesc() {
        return (String) getAttributeInternal(LPGERRORDESC);
    }

    /**
     * Sets <code>value</code> as the attribute value for LpgErrorDesc.
     * @param value value to set the LpgErrorDesc
     */
    public void setLpgErrorDesc(String value) {
        setAttributeInternal(LPGERRORDESC, value);
    }

    /**
     * Gets the attribute value for Time, using the alias name Time.
     * @return the value of Time
     */
    public Timestamp getTime() {
        return (Timestamp) getAttributeInternal(TIME);
    }

    /**
     * Sets <code>value</code> as the attribute value for Time.
     * @param value value to set the Time
     */
    public void setTime(Timestamp value) {
        setAttributeInternal(TIME, value);
    }

    /**
     * Gets the attribute value for ServiceUrl, using the alias name ServiceUrl.
     * @return the value of ServiceUrl
     */
    public String getServiceUrl() {
        return (String) getAttributeInternal(SERVICEURL);
    }

    /**
     * Sets <code>value</code> as the attribute value for ServiceUrl.
     * @param value value to set the ServiceUrl
     */
    public void setServiceUrl(String value) {
        setAttributeInternal(SERVICEURL, value);
    }

    /**
     * Gets the attribute value for UserSessionTrackingId, using the alias name UserSessionTrackingId.
     * @return the value of UserSessionTrackingId
     */
    public String getUserSessionTrackingId() {
        return (String) getAttributeInternal(USERSESSIONTRACKINGID);
    }

    /**
     * Sets <code>value</code> as the attribute value for UserSessionTrackingId.
     * @param value value to set the UserSessionTrackingId
     */
    public void setUserSessionTrackingId(String value) {
        setAttributeInternal(USERSESSIONTRACKINGID, value);
    }

    /**
     * Gets the attribute value for UserId, using the alias name UserId.
     * @return the value of UserId
     */
    public String getUserId() {
        return (String) getAttributeInternal(USERID);
    }

    /**
     * Sets <code>value</code> as the attribute value for UserId.
     * @param value value to set the UserId
     */
    public void setUserId(String value) {
        setAttributeInternal(USERID, value);
    }

    /**
     * Gets the attribute value for HeaderAttribute, using the alias name HeaderAttribute.
     * @return the value of HeaderAttribute
     */
    public String getHeaderAttribute() {
        return (String) getAttributeInternal(HEADERATTRIBUTE);
    }

    /**
     * Sets <code>value</code> as the attribute value for HeaderAttribute.
     * @param value value to set the HeaderAttribute
     */
    public void setHeaderAttribute(String value) {
        setAttributeInternal(HEADERATTRIBUTE, value);
    }

    /**
     * Gets the attribute value for SvcRespTime, using the alias name SvcRespTime.
     * @return the value of SvcRespTime
     */
    public String getSvcRespTime() {
        return (String) getAttributeInternal(SVCRESPTIME);
    }

    /**
     * Sets <code>value</code> as the attribute value for SvcRespTime.
     * @param value value to set the SvcRespTime
     */
    public void setSvcRespTime(String value) {
        setAttributeInternal(SVCRESPTIME, value);
    }

    /**
     * @param trackingId key constituent

     * @return a Key object based on given key constituents.
     */
    public static Key createPrimaryKey(String trackingId) {
        return new Key(new Object[] { trackingId });
    }

    /**
     * @return the definition object for this instance class.
     */
    public static synchronized EntityDefImpl getDefinitionObject() {
        return EntityDefImpl.findDefObject("com.iocl.customer.model.entityobject.LpgErrorsTrailEO");
    }

    /**
     * Add attribute defaulting logic in this method.
     * @param attributeList list of attribute names/values to initialize the row
     */
    protected void create(AttributeList attributeList) {
        super.create(attributeList);
    }

    /**
     * Add entity remove logic in this method.
     */
    public void remove() {
        super.remove();
    }
}
