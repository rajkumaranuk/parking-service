package com.hozah.parkingservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VehicleMovementType {
    TOWARDS("towards"),
    AWAY("away");

    private final String movement;
}
