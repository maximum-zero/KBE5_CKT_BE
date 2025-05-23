package kernel360.ckt.admin.ui.dto.request;

public record CompanyUpdateRequest(
    String name,
    String ceoName,
    String telNumber
) {
}
