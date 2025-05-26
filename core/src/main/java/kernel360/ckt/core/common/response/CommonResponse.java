package kernel360.ckt.core.common.response;

import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private final String code;
    private final String message;
    private final T data;

    private CommonResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>("000", "Success", data);
    }

}
