package com.example.healthgenie.base.exception;

import com.example.healthgenie.exception.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {

        final List<String> errorList = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.warn("Invalid DTO Parameter errors : {}", errorList);
        return this.makeErrorResponseEntity(errorList.toString());
    }

    private ResponseEntity<Object> makeErrorResponseEntity(final String errorDescription) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errorDescription));
    }

    @ExceptionHandler({PtReviewException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final PtReviewException exception) {
        log.warn("PtReviewException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getPtReviewErrorResult());
    }

    @ExceptionHandler({MatchingException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final MatchingException exception) {
        log.warn("MatchingException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getMatchingErrorResult());
    }

    @ExceptionHandler({CommunityPostException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final CommunityPostException exception) {
        log.warn("CommunityException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getCommunityPostErrorResult());
    }

    @ExceptionHandler({TrainerProfileException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final TrainerProfileException exception) {
        log.warn("TrainerProfileException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getTrainerProfileErrorResult());
    }
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(final Exception exception) {
        log.warn("Exception occur: ", exception);
        return this.makeErrorResponseEntity(exception.getMessage().toString());
    }

    @ExceptionHandler({CommonException.class})
    public ResponseEntity<ErrorResponse> handleException(final CommonException exception) {
        log.warn("Exception occur: ", exception);
        return this.makeErrorResponseEntity(exception.getCommonErrorResult());
    }

    @ExceptionHandler({UserEmailException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final UserEmailException exception) {
        log.warn("UserEmailException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getUserEmailErrorResult());
    }

    @ExceptionHandler({TodoException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final TodoException exception) {
        log.warn("TodoException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getTodoErrorResult());
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final TodoErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final UserEmailErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final CommonErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }
    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final PtReviewErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final CommunityPostErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final TrainerProfileErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final MatchingErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }


    @Getter
    @RequiredArgsConstructor
    static class ErrorResponse {
        private final String code;
        private final String message;
    }

}
