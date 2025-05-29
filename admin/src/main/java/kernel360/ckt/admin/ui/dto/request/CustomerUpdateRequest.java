package kernel360.ckt.admin.ui.dto.request;

public record CustomerUpdateRequest (
    String customerName,
    String customerType,
    String phoneNumber,
    String licenseNumber,
    String zipCode,
    String address,
    String detailedAddress,
    String birthday,
    String memo,
    String status
) {

}
