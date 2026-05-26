import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CryptoService {

    public static double getPrice(String coinName) {

        try {

            String apiUrl =
                    "https://api.coingecko.com/api/v3/simple/price?ids="
                            + coinName
                            + "&vs_currencies=usd";

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(apiUrl))
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            String jsonResponse =
                    response.body();

            JsonObject jsonObject =
                    JsonParser.parseString(jsonResponse)
                            .getAsJsonObject();

            if (!jsonObject.has(coinName)) {

                return -1;

            }

            JsonObject coinObject =
                    jsonObject.getAsJsonObject(coinName);

            return coinObject
                    .get("usd")
                    .getAsDouble();

        }

        catch (Exception e) {

            return -1;

        }

    }

}