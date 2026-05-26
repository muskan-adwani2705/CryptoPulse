import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.ArrayList;
import java.util.Scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

    static ArrayList<String> favoriteCoins = new ArrayList<>();

    static java.util.HashMap<String, Double> portfolio =
        new java.util.HashMap<>();
    
    static final String FILE_PATH =
            "../data/favorites.txt";


    // Load favorites from file
    public static void loadFavorites() {

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(FILE_PATH)
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                favoriteCoins.add(line);

            }

            reader.close();

        }

        catch (IOException e) {

            System.out.println("No previous favorites found.");

        }

    }


    // Save favorites to file
    public static void saveFavorites() {

        try {

            FileWriter writer =
                    new FileWriter(FILE_PATH);

            for (String coin : favoriteCoins) {

                writer.write(coin + "\n");

            }

            writer.close();

        }

        catch (IOException e) {

            System.out.println("Error saving favorites!");

        }

    }


    // Fetch crypto price
    public static void getCryptoPrice(String coinName) {

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
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            String jsonResponse = response.body();

            JsonObject jsonObject =
                    JsonParser.parseString(jsonResponse).getAsJsonObject();

            if (!jsonObject.has(coinName)) {

                System.out.println("Coin not found!");

                return;

            }

            JsonObject coinObject =
                    jsonObject.getAsJsonObject(coinName);

            double price =
                    coinObject.get("usd").getAsDouble();

            System.out.println("\n======================");
            System.out.println("Coin: " + coinName.toUpperCase());
            System.out.println("Price: $" + price);
            System.out.println("======================");

        }

        catch (Exception e) {

            System.out.println("Error fetching price!");

        }

    }


    // Add favorite coin
    public static void addFavoriteCoin(String coinName) {

        favoriteCoins.add(coinName);

        saveFavorites();

        System.out.println(coinName + " added successfully!");

    }


    // View favorite coins
    public static void viewFavoriteCoins() {

        System.out.println("\n===== Favorite Coins =====");

        if (favoriteCoins.isEmpty()) {

            System.out.println("No favorite coins added.");

            return;

        }

        for (String coin : favoriteCoins) {

            System.out.println("- " + coin);

        }

    }
    // Add coin to portfolio
    public static void addToPortfolio(String coinName, double quantity) {

        portfolio.put(coinName, quantity);

        System.out.println(quantity + " " + coinName
            + " added to portfolio!");

    }

    // View portfolio
public static void viewPortfolio() {

    System.out.println("\n===== Portfolio =====");

    if (portfolio.isEmpty()) {

        System.out.println("Portfolio is empty!");

        return;

    }

    double totalValue = 0;

    for (String coin : portfolio.keySet()) {

        try {

            String apiUrl =
                    "https://api.coingecko.com/api/v3/simple/price?ids="
                            + coin +
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

            JsonObject coinObject =
                    jsonObject.getAsJsonObject(coin);

            double price =
                    coinObject.get("usd").getAsDouble();

            double quantity =
                    portfolio.get(coin);

            double value =
                    price * quantity;

            totalValue += value;

            System.out.println(
                    coin.toUpperCase()
                            + " | Quantity: "
                            + quantity
                            + " | Value: $"
                            + value
            );

        }

        catch (Exception e) {

            System.out.println("Error loading portfolio!");

        }

    }

    System.out.println("\nTotal Portfolio Value: $"
            + totalValue);

    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        boolean running = true;

        // Load saved favorites
        loadFavorites();


        while (running) {

            System.out.println("\n===== CryptoPulse =====");

            System.out.println("1. Check Crypto Price");
            System.out.println("2. View Favorite Coins");
            System.out.println("3. Add Favorite Coin");
            System.out.println("4. Start Live Tracker");
            System.out.println("5. Add To Portfolio");
            System.out.println("6. View Portfolio");
            System.out.println("7. Start Price Alert");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            sc.nextLine();


            switch (choice) {

                case 1:

                    System.out.print("Enter coin name: ");

                    String coinName =
                            sc.nextLine().toLowerCase();

                    getCryptoPrice(coinName);

                    break;


                case 2:

                    viewFavoriteCoins();

                    break;


                case 3:

                    System.out.print("Enter coin name: ");

                    String favoriteCoin =
                            sc.nextLine().toLowerCase();

                    addFavoriteCoin(favoriteCoin);

                    break;


                case 4:

                     System.out.print("Enter coin name: ");

                String liveCoin =
                        sc.nextLine().toLowerCase();

                PriceTracker tracker =
                        new PriceTracker(liveCoin);

                Thread thread =
                        new Thread(tracker);

                thread.start();

                break;

                case 5:

                    System.out.print("Enter coin name: ");

                    String portfolioCoin =
                        sc.nextLine().toLowerCase();

                    System.out.print("Enter quantity: ");

                    double quantity =
                        sc.nextDouble();

                    sc.nextLine();

                    addToPortfolio(portfolioCoin, quantity);

                    break;

                case 6:

                    viewPortfolio();

                    break;
                
                case 7:
                    System.out.print("Enter coin name: ");

                    String alertCoin =
                        sc.nextLine().toLowerCase();

                    System.out.print("Enter target price: ");

                    double targetPrice =
                        sc.nextDouble();

                    sc.nextLine();

                    PriceAlert alert =
                        new PriceAlert(alertCoin, targetPrice);

                    Thread alertThread =
                        new Thread(alert);

                    alertThread.start();

                    break;
                case 8:
                    running = false;

                    System.out.println("Exiting CryptoPulse...");

                    break;

                default:

                    System.out.println("Invalid choice!");

            }

        }

        sc.close();

    }
}