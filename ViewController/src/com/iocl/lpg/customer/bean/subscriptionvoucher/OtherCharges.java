package com.iocl.lpg.customer.bean.subscriptionvoucher;

import java.io.Serializable;

public class OtherCharges implements Serializable {
    @SuppressWarnings("compatibility:-7136382879805907628")
    private static final long serialVersionUID = 1L;

    public OtherCharges() {
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
