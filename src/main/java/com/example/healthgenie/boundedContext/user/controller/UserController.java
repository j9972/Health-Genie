package com.example.healthgenie.boundedContext.user.controller;

import com.example.healthgenie.base.exception.UserException;
import com.example.healthgenie.base.response.Result;
import com.example.healthgenie.boundedContext.user.dto.UserRequest;
import com.example.healthgenie.boundedContext.user.dto.UserResponse;
import com.example.healthgenie.boundedContext.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.healthgenie.base.exception.UserErrorResult.NOT_VALID_FIELD;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PatchMapping("/user/{userId}")
    public ResponseEntity<Result> update(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "") String field,
                                         @RequestBody UserRequest request
    ) {
        UserResponse response = null;
        if(field.equalsIgnoreCase("ROLE")) {
            response = userService.updateRole(userId, request.getRole());
        } else if(field.equalsIgnoreCase("NICKNAME")) {
            response = userService.updateNickname(userId, request.getNickname());
        } else {
            throw new UserException(NOT_VALID_FIELD);
        }

        return ResponseEntity.ok(Result.of(response));
    }
}