package union.serverforloadtesting.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    private final ConfigVariables configVariables;

    @SneakyThrows
    @PostMapping
    public SpringResponse doRequests(@RequestParam("url")String urlForRequest, @RequestParam("numberRequests") int numberOfRequests){
        String url = configVariables.getUrl()+urlForRequest;
        String login = configVariables.getLogin();
        String password = configVariables.getPassword();
        final String[] errorCode = {null};
        final String[] errorText = {null};

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 50 );
        long startTime = System.nanoTime();
        for (int i = 0; i < numberOfRequests; i++) {
            ScheduledFuture<?> sf = scheduler.scheduleAtFixedRate(
                    new Runnable() {
                        public void run() {
                            HttpResponse<String> response=takeResponse(login,password,url);
                            if(response.statusCode()!=200) {
                               errorCode[0] = String.valueOf(response.statusCode());
                               errorText[0] =response.body().substring(0,100);
                            }

                        }
                    },
                    1,  60,TimeUnit.SECONDS );
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        return new SpringResponse(errorCode[0], errorText[0], String.valueOf((duration)+" nanosec "));
    }
    @SneakyThrows
    private HttpResponse<String> takeResponse(String login, String password,String url){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .authenticator(takeAuthenticator(login,password)).build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    private Authenticator takeAuthenticator(String login, String password){
        Authenticator authenticator=new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        login,
                        password.toCharArray());
            }
        };
    return authenticator;
    }
}
