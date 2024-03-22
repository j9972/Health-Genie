package com.example.healthgenie.base.exception;

import com.example.healthgenie.base.exception.Chat.ChatErrorResult;
import com.example.healthgenie.base.exception.Chat.ChatException;
import com.example.healthgenie.base.exception.Comment.CommunityCommentErrorResult;
import com.example.healthgenie.base.exception.Comment.CommunityCommentException;
import com.example.healthgenie.base.exception.Common.CommonException;
import com.example.healthgenie.base.exception.Jwt.JwtErrorResult;
import com.example.healthgenie.base.exception.Jwt.JwtException;
import com.example.healthgenie.base.exception.Like.LikeErrorResult;
import com.example.healthgenie.base.exception.Like.LikeException;
import com.example.healthgenie.base.exception.Matching.MatchingErrorResult;
import com.example.healthgenie.base.exception.Matching.MatchingException;
import com.example.healthgenie.base.exception.Post.CommunityPostErrorResult;
import com.example.healthgenie.base.exception.Post.CommunityPostException;
import com.example.healthgenie.base.exception.PtProcess.PtProcessException;
import com.example.healthgenie.base.exception.PtReview.PtReviewException;
import com.example.healthgenie.base.exception.Routine.RoutineException;
import com.example.healthgenie.base.exception.Todo.TodoException;
import com.example.healthgenie.base.exception.TrainerProfile.TrainerProfileException;
import com.example.healthgenie.base.exception.User.UserErrorResult;
import com.example.healthgenie.base.exception.User.UserException;
import com.example.healthgenie.base.exception.UserEmail.UserEmailException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final Exception exception) {
        log.warn("Exception occur: ", exception);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("UNEXPECTED_ERROR", "some errors occurred : " + exception.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
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
        return this.makeErrorResponseEntity(exception);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final PtReviewException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getHttpStatus().name(), exception.getMessage()));
    }

    @ExceptionHandler({PtProcessException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final PtProcessException exception) {
        log.warn("Exception occur: ", exception);
        return this.makeErrorResponseEntity(exception);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final PtProcessException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getHttpStatus().name(), exception.getMessage()));
    }

    @ExceptionHandler({RoutineException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final RoutineException exception) {
        log.warn("PtReviewException occur: ", exception);
        return this.makeErrorResponseEntity(exception);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final RoutineException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getHttpStatus().name(), exception.getMessage()));
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

    @ExceptionHandler({LikeException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final LikeException exception) {
        log.warn("LikeException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getLikeErrorResult());
    }

    @ExceptionHandler({UserException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final UserException exception) {
        log.warn("UserException occur: ", exception);
        return this.makeErrorResponseEntity(exception.getUserErrorResult());
    }

    @ExceptionHandler({TrainerProfileException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final TrainerProfileException exception) {
        log.warn("TrainerProfileException occur: ", exception);
        return this.makeErrorResponseEntity(exception);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final TrainerProfileException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getHttpStatus().name(), exception.getMessage()));
    }

    @ExceptionHandler({CommonException.class})
    public ResponseEntity<ErrorResponse> handleException(final CommonException exception) {
        log.warn("Exception occur: ", exception);
        return this.makeErrorResponseEntity(exception);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final CommonException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getHttpStatus().name(), exception.getMessage()));
    }

    @ExceptionHandler({UserEmailException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final UserEmailException exception) {
        log.warn("UserEmailException occur: ", exception);
        return this.makeErrorResponseEntity(exception);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final UserEmailException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getHttpStatus().name(), exception.getMessage()));
    }

    @ExceptionHandler({TodoException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final TodoException exception) {
        log.warn("TodoException occur: ", exception);
        return this.makeErrorResponseEntity(exception);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final TodoException exception) {
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getHttpStatus().name(), exception.getMessage()));
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

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final MatchingErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final LikeErrorResult errorResult) {
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
