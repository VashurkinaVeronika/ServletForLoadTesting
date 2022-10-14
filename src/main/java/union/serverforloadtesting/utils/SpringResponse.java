package union.serverforloadtesting.utils;

import lombok.Data;
import lombok.Getter;

@Data
public class SpringResponse {

    private String errorCode;
    private String errorText;
    private String timeToRequestsAndReaponses;

    public SpringResponse(String errorCode, String errorText, String timeToRequestsAndReaponses) {
        this.errorText = errorText;
        this.timeToRequestsAndReaponses = timeToRequestsAndReaponses;
        this.errorCode = errorCode;
    }
}