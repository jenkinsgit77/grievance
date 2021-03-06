package com.iocl.customer.model.entityobject;

import oracle.jbo.AttributeList;
import oracle.jbo.Key;
import oracle.jbo.server.EntityDefImpl;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.TransactionEvent;
import oracle.jbo.server.SequenceImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Wed Jan 29 10:19:24 IST 2020
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class LevelADataEoImpl extends EntityImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    protected enum AttributesEnum {
        Id,
        Col2,
        Col3,
        Col4,
        Col5,
        Col6,
        Flowcode;
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
    public static final int ID = AttributesEnum.Id.index();
    public static final int COL2 = AttributesEnum.Col2.index();
    public static final int COL3 = AttributesEnum.Col3.index();
    public static final int COL4 = AttributesEnum.Col4.index();
    public static final int COL5 = AttributesEnum.Col5.index();
    public static final int COL6 = AttributesEnum.Col6.index();
    public static final int FLOWCODE = AttributesEnum.Flowcode.index();

    /**
     * This is the default constructor (do not remove).
     */
    public LevelADataEoImpl() {
    }

    /**
     * Gets the attribute value for Id, using the alias name Id.
     * @return the value of Id
     */
    public String getId() {
        return (String) getAttributeInternal(ID);
    }

    /**
     * Sets <code>value</code> as the attribute value for Id.
     * @param value value to set the Id
     */
    public void setId(String value) {
        setAttributeInternal(ID, value);
    }

    /**
     * Gets the attribute value for Col2, using the alias name Col2.
     * @return the value of Col2
     */
    public String getCol2() {
        return (String) getAttributeInternal(COL2);
    }

    /**
     * Sets <code>value</code> as the attribute value for Col2.
     * @param value value to set the Col2
     */
    public void setCol2(String value) {
        setAttributeInternal(COL2, value);
    }

    /**
     * Gets the attribute value for Col3, using the alias name Col3.
     * @return the value of Col3
     */
    public String getCol3() {
        return (String) getAttributeInternal(COL3);
    }

    /**
     * Sets <code>value</code> as the attribute value for Col3.
     * @param value value to set the Col3
     */
    public void setCol3(String value) {
        setAttributeInternal(COL3, value);
    }

    /**
     * Gets the attribute value for Col4, using the alias name Col4.
     * @return the value of Col4
     */
    public String getCol4() {
        return (String) getAttributeInternal(COL4);
    }

    /**
     * Sets <code>value</code> as the attribute value for Col4.
     * @param value value to set the Col4
     */
    public void setCol4(String value) {
        setAttributeInternal(COL4, value);
    }

    /**
     * Gets the attribute value for Col5, using the alias name Col5.
     * @return the value of Col5
     */
    public String getCol5() {
        return (String) getAttributeInternal(COL5);
    }

    /**
     * Sets <code>value</code> as the attribute value for Col5.
     * @param value value to set the Col5
     */
    public void setCol5(String value) {
        setAttributeInternal(COL5, value);
    }

    /**
     * Gets the attribute value for Col6, using the alias name Col6.
     * @return the value of Col6
     */
    public String getCol6() {
        return (String) getAttributeInternal(COL6);
    }

    /**
     * Sets <code>value</code> as the attribute value for Col6.
     * @param value value to set the Col6
     */
    public void setCol6(String value) {
        setAttributeInternal(COL6, value);
    }

    /**
     * Gets the attribute value for Flowcode, using the alias name Flowcode.
     * @return the value of Flowcode
     */
    public String getFlowcode() {
        return (String) getAttributeInternal(FLOWCODE);
    }

    /**
     * Sets <code>value</code> as the attribute value for Flowcode.
     * @param value value to set the Flowcode
     */
    public void setFlowcode(String value) {
        setAttributeInternal(FLOWCODE, value);
    }

    /**
     * @param id key constituent

     * @return a Key object based on given key constituents.
     */
    public static Key createPrimaryKey(String id) {
        return new Key(new Object[] { id });
    }

    /**
     * @return the definition object for this instance class.
     */
    public static synchronized EntityDefImpl getDefinitionObject() {
        return EntityDefImpl.findDefObject("com.iocl.customer.model.entityobject.LevelADataEo");
    }

    /**
     * Add attribute defaulting logic in this method.
     * @param attributeList list of attribute names/values to initialize the row
     */
    protected void create(AttributeList attributeList) {
        super.create(attributeList);
        SequenceImpl seq=new SequenceImpl("KYC_CONNECTION_SEQ",this.getDBTransaction());
        this.setId(String.valueOf(seq.getSequenceNumber().getBigDecimalValue()));
    }

    /**
     * Add entity remove logic in this method.
     */
    public void remove() {
        super.remove();
    }

    /**
     * Add locking logic here.
     */
    public void lock() {
        super.lock();
    }

    /**
     * Custom DML update/insert/delete logic here.
     * @param operation the operation type
     * @param e the transaction event
     */
    protected void doDML(int operation, TransactionEvent e) {
        super.doDML(operation, e);
    }
}

