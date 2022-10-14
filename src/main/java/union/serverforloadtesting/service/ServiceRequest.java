package union.serverforloadtesting.service;

import org.springframework.stereotype.Service;
import union.serverforloadtesting.utils.SpringResponse;

@Service
public interface ServiceRequest  {
    SpringResponse getSpringResponse();
    void sendRequest(String urlForRequest, int numberOfRequests);
}
