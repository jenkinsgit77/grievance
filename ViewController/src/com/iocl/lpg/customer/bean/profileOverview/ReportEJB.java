package com.iocl.lpg.customer.bean.profileOverview;

import java.io.Serializable;

public class ReportEJB implements Serializable {
    @SuppressWarnings("compatibility:-2018529671209993274")
    private static final long serialVersionUID = 1L;
    public ReportEJB() {
        super();
    }
    
    private String bookingMonth;
    private Number bookingCount;
    private Integer bookingValue;

    public void setBookingMonth(String bookingMonth) {
        this.bookingMonth = bookingMonth;
    }

    public String getBookingMonth() {
        return bookingMonth;
    }

    public void setBookingCount(Number bookingCount) {
        this.bookingCount = bookingCount;
    }

    public Number getBookingCount() {
        return bookingCount;
    }

    public void setBookingValue(Integer bookingValue) {
        this.bookingValue = bookingValue;
    }

    public Integer getBookingValue() {
        return bookingValue;
    }

}
