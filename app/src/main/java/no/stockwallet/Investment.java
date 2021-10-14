package no.stockwallet;

import java.util.ArrayList;

public class Investment {

    public ArrayList<Investment> investments = new ArrayList<Investment>();

    public Investment(String ticker, int volum, double price, String currency, double brokerage) {
        Investment investment = new Investment(ticker,volum,price,currency,brokerage);
        investment.investments.add(investment);
    }

    public ArrayList<Investment> getInvestments() {
        return investments;
    }
}

