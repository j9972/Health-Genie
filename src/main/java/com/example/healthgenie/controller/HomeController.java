package com.example.healthgenie.controller;

import com.example.healthgenie.domain.trainer.dto.TrainerSimpleInfo;
import com.example.healthgenie.domain.user.entity.User;
import com.example.healthgenie.service.TrainerProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class HomeController {

    private final TrainerProfileServiceImpl trainerProfileServie;

    @GetMapping("/")
    public ResponseEntity c(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "popular") String sort,
                  @RequestParam String mode,@RequestParam(defaultValue = "0") int page,
                  @RequestParam(defaultValue = "20") int size){
        Page<TrainerSimpleInfo> result = trainerProfileServie.getProfileByPage(page,size,sort);
        return new ResponseEntity(result,HttpStatus.OK);
    }
}
