package kernel360.ckt.admin.application.service.command;

public record RentalRetrieveCommand(
    Long id,
    Long companyId
) {
    public static RentalRetrieveCommand create(
        Long id,
        Long companyId
    ) {
        return new RentalRetrieveCommand(id, companyId);
    }
}
