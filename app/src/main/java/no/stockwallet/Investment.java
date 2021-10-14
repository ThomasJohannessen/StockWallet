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

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolum() {
        return volum;
    }

    public void setVolum(int volum) {
        this.volum = volum;
    }
}
