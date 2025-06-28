package kernel360.ckt.core.domain.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Boolean 타입 (true/false)을 MySQL CHAR(1) 타입 ('Y'/'N')으로 변환하는 JPA 컨버터.
 * 엔티티 필드에 @Convert(converter = BooleanToYNConverter.class) 어노테이션과 함께 사용됩니다.
 */
@Converter
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return "N"; //
        }
        return attribute ? "Y" : "N";
    }
    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return false;
        }
        return "Y".equalsIgnoreCase(dbData.trim());
    }
}
