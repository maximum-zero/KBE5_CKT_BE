package kernel360.ckt.admin.ui.dto.request;

import kernel360.ckt.core.domain.entity.CustomerStatus;
import lombok.Data;

@Data
public class CustomerRequest {
    private String customerType;
    private String customerName;
    private String phoneNumber;
    private String licenseNumber;
    private String zipCode;
    private String address;
    private String detailedAddress;
    private String birthday;
    private CustomerStatus status;
    private String memo;
}
