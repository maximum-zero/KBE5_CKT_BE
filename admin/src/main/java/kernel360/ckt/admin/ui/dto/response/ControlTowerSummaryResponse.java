package kernel360.ckt.admin.ui.dto.response;

public record ControlTowerSummaryResponse(
    int total,
    int running,
    int stopped
) {
    public static ControlTowerSummaryResponse of(int total, int running, int stopped) {
        return new ControlTowerSummaryResponse(total, running, stopped);
    }
}
