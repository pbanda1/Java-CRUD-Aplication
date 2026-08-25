package hr.algebra.humanitarnaorganizacija.util;

import hr.algebra.humanitarnaorganizacija.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public final class HttpUtility {

    private static final Logger log = LoggerFactory.getLogger(HttpUtility.class);

    private HttpUtility() {
    }

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(Duration.ofSeconds(20)).build();

    public static String fetchJson(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url)) //objectify to JSON object
                    .timeout(Duration.ofSeconds(20))
                    .header("Accept", "application/JSON")
                    .GET()
                    .build();

            HttpResponse<String> response = CLIENT.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );
            //cause
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String msg = "API request failed " + response.statusCode() + " Response: " + response.body();
                log.error(msg);
                throw new ApiException(msg);
            }

            return response.body();
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            String msg = "Error while fetching JSON";
            log.error(msg, e);
            throw new ApiException(msg, e);
        }
    }
}
