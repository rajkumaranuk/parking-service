package com.hozah.parkingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hozah.parkingservice.model.BooleanValue;
import com.hozah.parkingservice.model.VehicleMovementDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.hozah.parkingservice.model.VehicleMovementType.AWAY;
import static com.hozah.parkingservice.model.VehicleMovementType.TOWARDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWireMock(port = 8888)
class ParkingServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Value("${camera.polling.initial-delay.in-millis}")
    private long initialDelay;

    @Value("${camera.polling.interval.in-millis}")
    private long pollingInterval;

    @Test
    void isVehicleParked() throws Exception {
        ResponseEntity<BooleanValue> entity = restTemplate.getForEntity("http://localhost:" + port + "/parking/is-vehicle-in-parking/AB12XYZ", BooleanValue.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().isVehicleParked()).isEqualTo(false);

        givenCameraReturnsDecodes(getDecodesForVehicleEntry());
        Thread.sleep(initialDelay);

        entity = restTemplate.getForEntity("http://localhost:" + port + "/parking/is-vehicle-in-parking/AB12XYZ", BooleanValue.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().isVehicleParked()).isEqualTo(true);

        givenCameraReturnsDecodes(getDecodesForVehicleEntryAndExit());
        Thread.sleep(pollingInterval);

        entity = restTemplate.getForEntity("http://localhost:" + port + "/parking/is-vehicle-in-parking/AB12XYZ", BooleanValue.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().isVehicleParked()).isEqualTo(false);
    }

    private List<VehicleMovementDetail> getDecodesForVehicleEntry() {
        return Collections.singletonList(
                VehicleMovementDetail.builder().vehicleRegistration("AB12XYZ").motion(TOWARDS).build()
        );
    }

    private List<VehicleMovementDetail> getDecodesForVehicleEntryAndExit() {
        return Arrays.asList(
                VehicleMovementDetail.builder().vehicleRegistration("AB12XYZ").motion(TOWARDS).build(),
                VehicleMovementDetail.builder().vehicleRegistration("AB12XYZ").motion(AWAY).build()
        );
    }

    public void givenCameraReturnsDecodes(final List<VehicleMovementDetail> details) throws JsonProcessingException {
        stubFor(get(urlPathEqualTo("/ANPRMon"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.OK.value()).withBody(new ObjectMapper().writeValueAsString(details))));
    }
}
