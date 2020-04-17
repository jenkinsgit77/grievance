package com.iocl.lpg.customer.bean.loyalty;

public class VehicleDetailsParam {
    public VehicleDetailsParam() {
        super();
    }
    String vehicleType;
    String vehicleMake;
    String registrationNo;

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }
}
