package no.stockwallet;

public class Investment {

    private String ticker,currency;
    private double price;
    private int volum;

    private String fullName;
    private double totalStockMarkedValue;
    private double totalEarningsStockNOK,totalEarningsStockPercent;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getTotalStockMarkedValue() {
        return totalStockMarkedValue;
    }

    public void setTotalStockMarkedValue(double totalStockMarkedValue) {
        this.totalStockMarkedValue = totalStockMarkedValue;
    }

    public double getTotalEarningsStockNOK() {
        return totalEarningsStockNOK;
    }

    public void setTotalEarningsStockNOK(double totalEarningsStockNOK) {
        this.totalEarningsStockNOK = totalEarningsStockNOK;
    }

    public double getTotalEarningsStockPercent() {
        return totalEarningsStockPercent;
    }

    public void setTotalEarningsStockPercent(double totalEarningsStockPercent) {
        this.totalEarningsStockPercent = totalEarningsStockPercent;
    }
}
