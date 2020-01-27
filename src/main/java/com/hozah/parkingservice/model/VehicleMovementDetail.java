package com.hozah.parkingservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleMovementDetail {

    @JsonProperty("vrm")
    private String vehicleRegistration;

    @JsonProperty("motion")
    private VehicleMovementType motion;
}
