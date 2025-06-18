package kernel360.ckt.core.common.util;

public class MaskingUtil {

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "익명";
        return email.replaceAll("^(.{2}).*(@.*)$", "$1****$2");
    }

    public static String maskPassword(String password) {
        return (password == null || password.isBlank()) ? "" : "******";
    }

    public static String maskTel(String tel) {
        if (tel == null) return "";
        return tel.replaceAll("([0-9]{2,3})-([0-9]{3,4})-([0-9]{4})", "$1-****-$3");
    }

    public static String maskJsonBody(String jsonBody) {
        if (jsonBody == null || jsonBody.isBlank()) return jsonBody;
        String masked = jsonBody;
        masked = masked.replaceAll("(\"password\"\\s*:\\s*\").+?(\")", "$1******$2");
        masked = masked.replaceAll("(\"telNumber\"\\s*:\\s*\")([0-9]{2,3})-([0-9]{3,4})-([0-9]{4})(\")", "$1$2-****-$4$5");
        return masked;
    }
}
