package no.stockwallet.Model;

public class Investment {

    private String ticker,currency;
    private double avgBuyPrice;
    private int volume;
    private String fullName;
    private double markedValueNOK;
    private double earningsNOK, earningsPercent;
    private double intradayChange;
    private double currentStockPrice;

    public Investment(){}

    public Investment(String ticker, int volume, double buyPrice, double brokerage) {
        this.ticker = ticker;
        this.volume = volume;

        double brokeragePrStock = brokerage / this.volume;
        this.avgBuyPrice = buyPrice + brokeragePrStock;
    }

    public Investment(String ticker, int volum, double buyPrice, String currency, double brokerage) {
        this.ticker = ticker;
        this.volume = volum;
        this.currency = currency;

        double brokeragePrStock = brokerage / this.volume;
        this.avgBuyPrice = buyPrice + brokeragePrStock;
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

    public double getAvgBuyPrice() {
        return avgBuyPrice;
    }

    public void setAvgBuyPrice(double avgBuyPrice) {
        this.avgBuyPrice = avgBuyPrice;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getMarkedValueNOK() {
        return markedValueNOK;
    }

    public void setMarkedValueNOK(double markedValueNOK) {
        this.markedValueNOK = markedValueNOK;
    }

    public double getEarningsNOK() {
        return earningsNOK;
    }

    public void setEarningsNOK(double earningsNOK) {
        this.earningsNOK = earningsNOK;
    }

    public double getEarningsPercent() {
        return earningsPercent;
    }

    public void setEarningsPercent(double earningsPercent) {
        this.earningsPercent = earningsPercent;
    }

    public double getIntradayChange() {
        return intradayChange;
    }

    public void setIntradayChange(double intradayChange) {
        this.intradayChange = intradayChange;
    }

    public double getCurrentStockPrice() {
        return currentStockPrice;
    }

    public void setCurrentStockPrice(double currentStockPrice) {
        this.currentStockPrice = currentStockPrice;
    }
}
