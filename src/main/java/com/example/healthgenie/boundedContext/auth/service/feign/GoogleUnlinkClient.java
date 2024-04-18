package com.example.healthgenie.boundedContext.auth.service.feign;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-unlink", url = "https://oauth2.googleapis.com/revoke")
public interface GoogleUnlinkClient {

    @PostMapping
    @Headers("Content-type:application/x-www-form-urlencoded")
    void unlink(@RequestParam("token") String accessToken);
}
