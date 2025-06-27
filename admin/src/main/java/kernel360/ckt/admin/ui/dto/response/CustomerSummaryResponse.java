package kernel360.ckt.admin.ui.dto.response;

public record CustomerSummaryResponse(
    long total,
    long individual,
    long corporate,
    long renting
) {
    public static CustomerSummaryResponse of(long total, long individual, long corporate, long renting) {
        return new CustomerSummaryResponse(total, individual, corporate, renting);
    }
}
