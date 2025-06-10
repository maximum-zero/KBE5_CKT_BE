package kernel360.ckt.admin.application.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerKeywordCommand {
    private final String keyword;

    public static CustomerKeywordCommand create(String keyword) {
        return new CustomerKeywordCommand(keyword);
    }
}
