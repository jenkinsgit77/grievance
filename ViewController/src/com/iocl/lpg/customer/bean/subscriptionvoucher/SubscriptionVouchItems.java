package com.iocl.lpg.customer.bean.subscriptionvoucher;

import com.iocl.lpg.customer.utils.CommonHelper;

import java.io.Serializable;

import java.util.List;

import javax.faces.model.SelectItem;

public class SubscriptionVouchItems implements Serializable{
    public SubscriptionVouchItems() {
        super();
    }
    
    private String itemName;
    private String itemDesc;
    private String itemMrp;
    private String itemLength;
    private String itemLengthError;
    private String itemCode;
    private boolean itemSelect;
    List<SelectItem> itemLst;

    public void setItemLengthError(String itemLengthError) {
        this.itemLengthError = itemLengthError;
    }

    public String getItemLengthError() {
        return itemLengthError;
    }

    public void setItemLst(List<SelectItem> itemLst) {
        this.itemLst = itemLst;
    }

    public List<SelectItem> getItemLst() {
        return itemLst;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemSelect(boolean itemSelect) {
        this.itemSelect = itemSelect;
    }

    public boolean isItemSelect() {
        return itemSelect;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemMrp(String itemMrp) {
        this.itemMrp = itemMrp;
    }

    public String getItemMrp() {
        return itemMrp;
    }

    public void setItemLength(String itemLength) {
        this.itemLength = itemLength;
    }

    public String getItemLength() {
        return itemLength;
    }
}
