package kernel360.ckt.core.common.exception;

import kernel360.ckt.core.common.error.ErrorCode;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 애플리케이션 내에서 발생하는 비즈니스 로직 관련 예외를 처리하기 위한 커스텀 예외 클래스입니다.
 *
 * 이 예외는 오류 식별을 위해 {@link ErrorCode}를 기반으로 포맷팅된 오류 메시지를 생성합니다.
 */
@Getter
public class CustomException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(CustomException.class);
    private final ErrorCode errorCode;

    /**
     * ErrorCode 와 메시지 포맷에 주입할 인자들을 받는 생성자.
     * ErrorCode 의 메시지 포맷(%s, %d 등)에 따라 messageArgs 가 주입되어 최종 예외 메시지가 생성됩니다.
     *
     * @param errorCode   예외를 나타내는 ErrorCode (메시지 포맷 포함)
     * @param messageArgs 메시지 포맷에 주입될 가변 인자들 (예: ID, 이름 등)
     */
    public CustomException(ErrorCode errorCode, Object... messageArgs) {
        super(String.format(errorCode.getMessage(), messageArgs));
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode 만 받는 생성자.
     * 메시지에 주입할 인자가 없는 경우 이 생성자를 사용합니다.
     *
     * @param errorCode 예외를 나타내는 ErrorCode
     */
    public CustomException(ErrorCode errorCode) {
        this(errorCode, (Object[]) null);
    }
}
