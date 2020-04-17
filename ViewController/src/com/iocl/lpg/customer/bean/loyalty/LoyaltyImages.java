package com.iocl.lpg.customer.bean.loyalty;

import java.io.Serializable;

public class LoyaltyImages implements Serializable {
    @SuppressWarnings("compatibility:12469098653097226")
    private static final long serialVersionUID = 1L;
    public LoyaltyImages() {
        super();
    }
    String loyalityImg1;
    String loyalityImg2;
    String loyalityImg3;
    String loyalityImg4;

    public void setLoyalityImg1(String loyalityImg1) {
        this.loyalityImg1 = loyalityImg1;
    }

    public String getLoyalityImg1() {
        return loyalityImg1;
    }

    public void setLoyalityImg2(String loyalityImg2) {
        this.loyalityImg2 = loyalityImg2;
    }

    public String getLoyalityImg2() {
        return loyalityImg2;
    }

    public void setLoyalityImg3(String loyalityImg3) {
        this.loyalityImg3 = loyalityImg3;
    }

    public String getLoyalityImg3() {
        return loyalityImg3;
    }

    public void setLoyalityImg4(String loyalityImg4) {
        this.loyalityImg4 = loyalityImg4;
    }

    public String getLoyalityImg4() {
        return loyalityImg4;
    }
}
