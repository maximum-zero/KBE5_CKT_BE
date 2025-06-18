package kernel360.ckt.auth.common.exception;

import kernel360.ckt.core.common.error.ErrorCode;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error("[CustomException] code={}, message={}", errorCode.getCode(), errorCode.getMessage(), ex);
        return ResponseEntity.ok(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("[ValidationException] {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.from(400, "요청 파라미터 유효성 검사에 실패했습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("[UnhandledException] {}", ex.getMessage(), ex);
        return  ResponseEntity.ok(ErrorResponse.from(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

}
