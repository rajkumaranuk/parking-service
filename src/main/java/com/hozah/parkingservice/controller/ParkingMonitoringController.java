package com.hozah.parkingservice.controller;

import com.hozah.parkingservice.model.BooleanValue;
import com.hozah.parkingservice.service.ParkingMonitoringService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/parking")
@AllArgsConstructor
public class ParkingMonitoringController {

    private final ParkingMonitoringService parkingMonitoringService;

    @GetMapping(value = "/is-vehicle-in-parking/{vehicleRegistration}", produces = "application/json")
    public BooleanValue isVehicleInParking(@PathVariable String vehicleRegistration) {
        final boolean vehicleParked = parkingMonitoringService.isVehicleParked(vehicleRegistration);
        return new BooleanValue(vehicleParked);
    }

}
