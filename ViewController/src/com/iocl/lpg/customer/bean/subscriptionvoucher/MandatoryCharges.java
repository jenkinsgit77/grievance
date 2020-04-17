package com.iocl.lpg.customer.bean.subscriptionvoucher;

import java.io.Serializable;

public class MandatoryCharges implements Serializable {
    public MandatoryCharges() {
        super();
    }
    
    private String itemCode;
    private String itemName;
    private String itemPrice;

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemPrice() {
        return itemPrice;
    }
}
