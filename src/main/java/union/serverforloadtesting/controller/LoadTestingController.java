package union.serverforloadtesting.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import union.serverforloadtesting.service.ServiceGetRequest;
import union.serverforloadtesting.service.ServiceRequest;
import union.serverforloadtesting.utils.AppConfig;
import union.serverforloadtesting.utils.ConfigVariables;
import union.serverforloadtesting.utils.SpringResponse;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;
import static java.time.temporal.ChronoUnit.SECONDS;

@RestController
@RequiredArgsConstructor
public class LoadTestingController {
    private final ServiceRequest serviseRequest;

    @SneakyThrows
    @PostMapping
    public SpringResponse doRequests(@RequestParam("url") String urlForRequest, @RequestParam("numberRequests") int numberOfRequests) {
        serviseRequest.sendRequest(urlForRequest, numberOfRequests);
        return serviseRequest.getSpringResponse();
    }
}
