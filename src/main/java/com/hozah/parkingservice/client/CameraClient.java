package com.hozah.parkingservice.client;

import com.hozah.parkingservice.model.VehicleMovementDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "camera-client", url = "${camera.server.base-url}")
public interface CameraClient {

    @RequestMapping(
            method = GET,
            value = "${camera.server.endpoint.context-path}",
            headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE
    )
    List<VehicleMovementDetail> getDecodeData();
}
