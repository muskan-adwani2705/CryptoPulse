import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PriceTracker implements Runnable {

    private String coinName;

    public PriceTracker(String coinName) {

        this.coinName = coinName;

    }

    @Override
    public void run() {

        while (true) {

            try {

                String apiUrl =
                        "https://api.coingecko.com/api/v3/simple/price?ids="
                                + coinName +
                                "&vs_currencies=usd";

                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .GET()
                        .build();

                HttpResponse<String> response =
                        client.send(request,
                                HttpResponse.BodyHandlers.ofString());

                String jsonResponse = response.body();

                JsonObject jsonObject =
                        JsonParser.parseString(jsonResponse)
                                .getAsJsonObject();

                if (!jsonObject.has(coinName)) {

                    System.out.println("Coin not found!");

                    return;

                }

                JsonObject coinObject =
                        jsonObject.getAsJsonObject(coinName);

                double price =
                        coinObject.get("usd").getAsDouble();

                System.out.println(
                        "\n[LIVE] "
                                + coinName.toUpperCase()
                                + " : $"
                                + price
                );

                // Wait 5 seconds
                Thread.sleep(5000);

            }

            catch (Exception e) {

                System.out.println("Tracker error!");

            }

        }

    }

}