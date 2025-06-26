package kernel360.ckt.admin.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import kernel360.ckt.core.common.exception.CustomException;
import kernel360.ckt.core.common.error.ErrorCode;
import kernel360.ckt.core.common.response.ErrorResponse;
import kernel360.ckt.core.common.util.MaskingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String params = getRequestParams(request);
        log.error("⛔️ - URI: {} {}\n- Params: {}\nCustomException: {}",
            method, uri, params, ex.getErrorCode().getMessage());
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.ok(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String params = getRequestParams(request);

        String body = null;
        if (request instanceof ContentCachingRequestWrapper wrapper) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                body = new String(buf, StandardCharsets.UTF_8);
                body = MaskingUtil.maskJsonBody(body);
            }
        }

        log.error("⛔️ - URI: {} {}\n- Params: {}\n- Body: {}\nGeneralException 발생",
            method, uri, params, body, ex);

        return ResponseEntity.ok(
            ErrorResponse.from(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage())
        );
    }

    private String getRequestParams(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
            .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
            .collect(Collectors.joining(", "));
    }
}
