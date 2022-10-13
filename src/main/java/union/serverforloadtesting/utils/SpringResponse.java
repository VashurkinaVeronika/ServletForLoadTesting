package union.serverforloadtesting.utils;

import lombok.Getter;

@Getter
public class SpringResponse {

    private final String errorCode;
    private final String errorText;
    private final String timeToRequestsAndReaponses;

    public SpringResponse(String errorCode, String errorText, String timeToRequestsAndReaponses) {
        this.errorText = errorText;
        this.timeToRequestsAndReaponses = timeToRequestsAndReaponses;
        this.errorCode = errorCode;
    }
}