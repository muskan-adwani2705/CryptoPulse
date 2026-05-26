public class PriceTracker implements Runnable {

    private String coinName;

    public PriceTracker(String coinName) {

        this.coinName = coinName;

    }

    @Override
    public void run() {

        while (true) {

            try {

                double price =
                        CryptoService.getPrice(coinName);

                if (price == -1) {

                    System.out.println("Coin not found!");

                    return;

                }

                System.out.println(
                        "\n[LIVE] "
                                + coinName.toUpperCase()
                                + " : $"
                                + price
                );

                Thread.sleep(5000);

            }

            catch (Exception e) {

                System.out.println("Tracker error!");

            }

        }

    }

}