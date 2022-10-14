package union.serverforloadtesting.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import union.serverforloadtesting.utils.AppConfig;
import union.serverforloadtesting.utils.SpringResponse;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@RequiredArgsConstructor
public class ServiceGetRequest implements ServiceRequest {
    private final AppConfig appConfig;

    @Getter
    private SpringResponse springResponse;

    @SneakyThrows
    @Override
    public void sendRequest(String urlForRequest, int numberOfRequests) {
        String url = appConfig.getUrl()+urlForRequest;
        String login = appConfig.getLogin();
        String password = appConfig.getPassword();
        springResponse=new SpringResponse(null,null,null);

        ExecutorService executorService = Executors.newScheduledThreadPool( numberOfRequests );
        long startTime = System.nanoTime();
        Future<?> future = null;
        for (int i = 0; i < numberOfRequests; i++) {
            System.out.println("thread start"+i);
            future = executorService.submit(
                    new Runnable() {
                        public void run() {
                            HttpResponse<String> response=takeResponse(login,password,url);
                            System.out.println("thread");
                            springResponse.setErrorCode("fdfhtfhrththth");
                            if(response.statusCode()!=200) {
                                springResponse.setErrorCode(String.valueOf(response.statusCode()));
                                springResponse.setErrorText(response.body().substring(0,100));
                               // errorCode[0] = String.valueOf(response.statusCode());
                                //errorText[0] =response.body().substring(0,100);
                            }

                        }
                    });
            System.out.println("thread done"+i);
            if(i==numberOfRequests-1)future.get();
        }
        System.out.println("thread main");
       // future.get();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        //= new SpringResponse(errorCode, errorText, String.valueOf((duration) + " nanosec "));
        springResponse.setTimeToRequestsAndReaponses((duration) + " nanosec ");
        future.cancel(false);
        executorService.shutdown();
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

