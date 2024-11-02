package eu.entarc.deejava;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class RestApiExample {

    private static final String keycloakUrl = "https://iam.entarc.eu/realms/dataentarceu/protocol/openid-connect/token";
    private static final String clientId = "dataentarceu";
    private static final String clientSecret = "CciEH31F6xfI2dMhigu0wjqWnrI3bPvz";
    private static final String username = "?";
    private static final String password = "?";
    private static final String connectionId = "test";
    private static final String country = "AT";
    private static final String codingSchema = "NAT";
    private static final String accountingPointId = "AT0020000000000000000000100383465";
    private static final String tariffCode = "AWATTAR";
    private static final String aiidaPermissionId = "a3bb84ca-e0e1-4441-a95b-0c4c8c08f3d9";
    private static final String fromDt =
            ZonedDateTime.of(2024, 11, 1, 0, 0, 0, 0, ZoneId.of("UTC")).toString().replace(
            "[UTC]", "");
    private static final String toDt = ZonedDateTime.of(2024, 11, 2, 0, 0, 0, 0, ZoneId.of("UTC")).toString().replace(
            "[UTC]", "");

    public static void main(String[] args) throws IOException, InterruptedException {
        // Step 1: Obtain the access token from Keycloak
        String accessToken = getAccessToken();

        if (accessToken != null) {
            // Step 2: Use the access token to make a request to the protected API
            requestTimeseriesData(accessToken);
            requestAiidaData(accessToken);
        } else {
            System.out.println("Failed to obtain access token.");
        }
    }

    private static String getAccessToken() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Build request body with form data
        String requestBody = "grant_type=password" +
                "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                "&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(keycloakUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Parse JSON response to get the access token
            JSONObject jsonResponse = new JSONObject(response.body());
            String accessToken = jsonResponse.getString("access_token");
            System.out.println("Access Token: " + accessToken);
            return accessToken;
        } else {
            System.out.println("Failed to obtain access token. Status Code: " + response.statusCode());
            System.out.println("Response: " + response.body());
            return null;
        }
    }

    private static void requestTimeseriesData(String accessToken) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Build the URL with path parameters
        //String url = String.format("https://data-api.entarc.eu/timeseriesWithPrice/%s/%s/%s/%s/%s/%s/AWATTAR",
        //        connectionId, country, codingSchema, accountingPointId, fromDt, toDt);
        String url = String.format("https://data-api.entarc.eu/timeseries/%s/%s/%s/%s/%s/%s",
                connectionId, country, codingSchema, accountingPointId, fromDt, toDt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Protected Data Response:\n" + new JSONObject(response.body()).toString(2));
        } else {
            System.out.println("Failed to access protected resource. Status Code: " + response.statusCode());
            System.out.println("Response: " + response.body());
        }
    }


    private static void requestAiidaData(String accessToken) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Build the URL with path parameters
        String url = String.format("https://data-api.entarc.eu/latest_values/%s", aiidaPermissionId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Protected Data Response:\n" + new JSONArray(response.body()).toString(2));
        } else {
            System.out.println("Failed to access protected resource. Status Code: " + response.statusCode());
            System.out.println("Response: " + response.body());
        }
    }
}