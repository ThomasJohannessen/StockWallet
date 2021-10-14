package no.stockwallet;

public class Investment {

    String ticker,currency;
    double price;
    int volum;

    public Investment(String ticker, int volum, double price, String currency, double brokerage) {
        this.ticker = ticker;
        this.volum = volum;
        this.currency = currency;

        double brokeragePrStock = brokerage / this.volum;

        this.price = price + brokeragePrStock;
    }
}
