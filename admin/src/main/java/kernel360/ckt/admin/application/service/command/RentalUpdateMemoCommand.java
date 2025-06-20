package kernel360.ckt.admin.application.service.command;

public record RentalUpdateMemoCommand(
    Long id,
    Long companyId,
    String memo
) {
    public static RentalUpdateMemoCommand create(Long id, Long companyId, String memo) {
        return new RentalUpdateMemoCommand(id, companyId, memo);
    }
}
