package no.stockwallet;

import java.util.ArrayList;

public class Investment {

    public ArrayList<Investment> investments = new ArrayList<Investment>();

    public Investment(String ticker, int volum, float kurs) {
        Investment investment = new Investment(ticker, volum, kurs);
        investment.investments.add(investment);
    }

    public void addTrade(String ticker, int volum, float kurs) {
        Investment trade = new Investment(ticker, volum, kurs);
        investments.add(trade);
    }

    public ArrayList<Investment> getInvestments() {
        return investments;
    }
}
