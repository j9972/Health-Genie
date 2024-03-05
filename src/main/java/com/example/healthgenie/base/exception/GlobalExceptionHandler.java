package com.example.healthgenie.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final Exception exception) {
        log.warn("Exception occur: ", exception);
        return ResponseEntity.badRequest().body(new ErrorResponse("UNEXPECTED_ERROR", "some errors occurred : " + exception.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse("NOT_VALID_PARAMETERS", errors));
    }

    @ExceptionHandler({PtReviewException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final PtReviewException exception) {
        log.warn("PtReviewException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getPtReviewErrorResult());
    }

    @ExceptionHandler({PtProcessException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final PtProcessException exception) {
        log.warn("Exception occur: ", exception);
        return this.makeErrorResponseEntity(exception.getPtProcessErrorResult());
    }

    @ExceptionHandler({RoutineException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final RoutineException exception) {
        log.warn("PtReviewException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getRoutineErrorResult());
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

    @ExceptionHandler({CommunityCommentException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final CommunityCommentException exception) {
        log.warn("CommunityException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getCommunityCommentErrorResult());
    }

    @ExceptionHandler({ChatException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final ChatException exception) {
        log.warn("ChatException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getChatErrorResult());
    }

    @ExceptionHandler({JwtException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final JwtException exception) {
        log.warn("JwtException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getJwtErrorResult());
    }

    @ExceptionHandler({UserException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final UserException exception) {
        log.warn("UserException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getUserErrorResult());
    }

    @ExceptionHandler({TrainerProfileException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final TrainerProfileException exception) {
        log.warn("TrainerProfileException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getTrainerProfileErrorResult());
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

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final RoutineErrorResult errorResult) {
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

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final PtProcessErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final CommunityPostErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final CommunityCommentErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final ChatErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final JwtErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final UserErrorResult errorResult) {
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
        private final Object message;
    }
}
