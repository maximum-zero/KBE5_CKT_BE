package kernel360.ckt.admin.application.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class VehicleKeywordCommand {
    private final String keyword;

    public static VehicleKeywordCommand create(String keyword) {
        return new VehicleKeywordCommand(keyword);
    }
}
