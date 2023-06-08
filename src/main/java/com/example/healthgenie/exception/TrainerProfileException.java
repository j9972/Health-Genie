package com.example.healthgenie.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TrainerProfileException extends RuntimeException {

    final private TrainerProfileErrorResult trainerProfileErrorResult;

}
