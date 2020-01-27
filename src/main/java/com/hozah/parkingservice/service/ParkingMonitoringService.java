package com.hozah.parkingservice.service;


import com.hozah.parkingservice.client.CameraClient;
import com.hozah.parkingservice.model.VehicleMovementDetail;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ParkingMonitoringService {
    private static final Set<String> PARKED_VEHICLES = new HashSet<>();

    private final CameraClient cameraClient;

    public boolean isVehicleParked(String vehicleRegistration) {
        return PARKED_VEHICLES.contains(vehicleRegistration);
    }

    @Scheduled(fixedRateString = "${camera.polling.interval.in-millis}", initialDelayString = "${camera.polling.initial-delay.in-millis}")
    private void updateParkingData() {
        final List<VehicleMovementDetail> vehicleMovementDetails = cameraClient.getDecodeData();

        if (vehicleMovementDetails != null) {
            vehicleMovementDetails.forEach(vehicleMovementDetail -> {
                switch (vehicleMovementDetail.getMotion()) {
                    case TOWARDS:
                        PARKED_VEHICLES.add(vehicleMovementDetail.getVehicleRegistration());
                        break;
                    case AWAY:
                        PARKED_VEHICLES.remove(vehicleMovementDetail.getVehicleRegistration());
                }
            });
        }
    }
}
