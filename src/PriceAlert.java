public class PriceAlert implements Runnable {

    private String coinName;

    private double targetPrice;


    public PriceAlert(String coinName, double targetPrice) {

        this.coinName = coinName;

        this.targetPrice = targetPrice;

    }


    @Override
    public void run() {

        while (true) {

            try {

                double currentPrice =
                        CryptoService.getPrice(coinName);

                if (currentPrice == -1) {

                    System.out.println("Coin not found!");

                    return;

                }

                System.out.println(
                        "[Checking] "
                                + coinName.toUpperCase()
                                + " : $"
                                + currentPrice
                );

                // Alert condition
                if (currentPrice >= targetPrice) {

                    System.out.println("\n🚨 ALERT 🚨");

                    System.out.println(
                            coinName.toUpperCase()
                                    + " crossed $"
                                    + targetPrice
                    );

                    System.out.println(
                            "Current Price: $"
                                    + currentPrice
                    );

                    return;

                }

                Thread.sleep(5000);

            }

            catch (Exception e) {

                System.out.println("Alert system error!");

            }

        }

    }

}