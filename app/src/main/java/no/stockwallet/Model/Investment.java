package no.stockwallet.Model;

public class Investment {

    private String ticker,currency;
    private double avgBuyPrice;
    private int volume;
    private String fullName;
    private double markedValue;
    private double earningsNOK, earningsPercent;
    private double intradayChange;
    private double stockPriceNow;

    public Investment(){}

    public Investment(String ticker, int volume, double buyPrice, String currency, double brokerage) {
        this.ticker = ticker;
        this.volume = volume;
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

    public double getMarkedValue() {
        return markedValue;
    }

    public void setMarkedValue(double markedValue) {
        this.markedValue = markedValue;
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

    public double getStockPriceNow() {
        return stockPriceNow;
    }

    public void setStockPriceNow(double stockPriceNow) {
        this.stockPriceNow = stockPriceNow;
    }
}
