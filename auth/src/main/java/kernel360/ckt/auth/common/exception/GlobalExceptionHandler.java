package kernel360.ckt.auth.common.exception;

import kernel360.ckt.core.common.error.ErrorCode;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomeException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.ok(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return  ResponseEntity.ok(ErrorResponse.from(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
}
