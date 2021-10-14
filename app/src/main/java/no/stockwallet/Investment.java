package no.stockwallet;

import java.util.ArrayList;

public class Investment {

    String ticker,currency;
    double price, brokerage;
    int volum;

    public Investment(String ticker, int volum, double price, String currency, double brokerage) {
        this.ticker = ticker;
        this.volum = volum;
        this.price = price;
        this.currency = currency;
        this.brokerage = brokerage;
    }
}
