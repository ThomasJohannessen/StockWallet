package no.stockwallet;

import android.util.Log;
import android.util.Pair;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StockCalculations {

    private static StockCalculations StockCalculationInstance = null;

    private Map<String, Double> currencyCache = new HashMap<>();
    private ArrayList<Pair<String, BigDecimal>> topThreeGainerStocks = new ArrayList<>();
    private ArrayList<Pair<String, BigDecimal>> bottomThreeLoserStocks = new ArrayList<>();

    public static StockCalculations getInstance(){
        if (StockCalculationInstance == null)
            StockCalculationInstance = new StockCalculations();

        return StockCalculationInstance;
    }

    private void AlphaVantageInit() {
        Config cfg = Config.builder()
                .key("04C7U8DGXKH0OY8B")
                .timeOut(5)
                .build();

        AlphaVantage.api().init(cfg);
    }

    private StockCalculations(){
        AlphaVantageInit();
    }

    private double currencyConverter(Investment foreignCurrencyInvestment) {

        ValueSetterSupport valueSetterSupport = new ValueSetterSupport();

            Thread thread = new Thread(() -> {
                if (currencyCache.containsKey(foreignCurrencyInvestment.getCurrency())){
                    valueSetterSupport.setReturnValue(currencyCache.get(foreignCurrencyInvestment.getCurrency()));
                }
                else {
                AlphaVantage.api()
                        .exchangeRate()
                        .fromCurrency(foreignCurrencyInvestment.getCurrency())
                        .toCurrency("NOK")
                        .onSuccess(e -> {
                            valueSetterSupport.setReturnValue(e.getExchangeRate());
                        })
                        .onFailure(Throwable::printStackTrace)
                        .fetch();
                }
            });

            thread.start();

            try {
                while (valueSetterSupport.getCheckValue() == 0) {
                    TimeUnit.MILLISECONDS.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        return valueSetterSupport.getReturnValue();

    }

    public int getTotalMarkedValue(HashMap<String, Investment> investments){
        //calculated the total of all invedsted stocks and converts to NOK. returns the number

        //fake invest to cache currency-conversion-rate for USD to not use time for this later
        Investment USD_currency_get_invest = new Investment("AAPL",0,0,"USD",1);
        currencyCache.put(USD_currency_get_invest.getCurrency(),currencyConverter(USD_currency_get_invest));

        double totSumMarkedValue = 0;

        Map<String, BigDecimal> investedStockPrices = new HashMap<>();
        String[] investedStocksTickers = new String[investments.size()];
        int i = 0;

        for (Map.Entry<String, Investment> x : investments.entrySet()) {
            investedStocksTickers[i] = x.getValue().getTicker();
            i++;
        }

        StockDataRetriever.getInstance().getMultipleStockPrices(investedStockPrices, investedStocksTickers);

        while (investedStockPrices.size() < investedStocksTickers.length){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        for (Investment x: investments.values()){
            if (!x.getCurrency().equals("NOK")){
                double exchangeCourse = currencyConverter(x);
                currencyCache.put(x.getCurrency(),exchangeCourse);

                BigDecimal currentPriceStock = investedStockPrices.get(x.getTicker());
                totSumMarkedValue += (currentPriceStock.doubleValue() * exchangeCourse * x.getVolum());
            }
            else {
                BigDecimal currentPriceStock = investedStockPrices.get(x.getTicker());
                totSumMarkedValue += currentPriceStock.doubleValue() * x.getVolum();
            }
        }

        currencyCache.clear();

        return (int) totSumMarkedValue;
    }

    public HashMap<String, BigDecimal> getIntradayChangesInStocksPercent(HashMap<String, Investment> investments){
        //gets the change for in inputed stocks this day in percent. Returns hashmap with ticker-change percent

        HashMap<String, BigDecimal> investedStockChangePercent = new HashMap<>();
        String[] investedStocksTickers = new String[investments.size()];
        int i = 0;

        for (Map.Entry<String, Investment> x : investments.entrySet()) {
            investedStocksTickers[i] = x.getValue().getTicker();
            i++;
        }

        StockDataRetriever.getInstance().getIntradayChangePercent(investedStockChangePercent, investedStocksTickers);

        while (investedStockChangePercent.size() < investedStocksTickers.length){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        findBiggestGainerAndLoserInInvestedStocks(investedStockChangePercent);

        return investedStockChangePercent;
    }

    /*public int getIntradayChangesTotalPercent(HashMap<String, Investment> investments){
        //gets the total change for in inputed stocks this day in percent. Returns int with percent

        int totalChange = 0;
        HashMap<String, BigDecimal> investedStockChangePercent = new HashMap<>();

        investedStockChangePercent = StockCalculations.getInstance().getIntradayChangesInStocksPercent(investments);



        return investedStockChangePercent;
    }*/

    public void findBiggestGainerAndLoserInInvestedStocks(HashMap<String, BigDecimal> hashmapChangesInInvestedStocksPercent) {
        //finds top 3 looser this day and top 3 gainers. Puts them in array with separet getters.

        List<Map.Entry<String, BigDecimal> > list =
                new LinkedList<Map.Entry<String, BigDecimal> >(hashmapChangesInInvestedStocksPercent.entrySet());

        list.sort(Map.Entry.comparingByValue());

        for (int i = 0; i < 3; i++) {
            Pair<String, BigDecimal> pair = new Pair<>(list.get(i).getKey(), list.get(i).getValue());
            bottomThreeLoserStocks.add(pair);
        }

        int listSize = list.size();
        for (int i = listSize-1; i > (listSize - 4); i--) {
            Pair<String, BigDecimal> pair = new Pair<>(list.get(i).getKey(), list.get(i).getValue());
            topThreeGainerStocks.add(pair);
        }
    }

    public ArrayList<Pair<String, BigDecimal>> getTopThreeGainerStocks() {
        return topThreeGainerStocks;
    }

    public ArrayList<Pair<String, BigDecimal>> getBottomThreeLoserStocks() {
        return bottomThreeLoserStocks;
    }

    public HashMap<String, Double> getMarkedValueNOKMultipleStocks(HashMap<String, Investment> investments){
        //calculates the marked value for the inputed stocks. Takes in account of volume and currency,
        //and returns a hashmap with ticker-markedValueinNok(with volume and currency taken care of)

        HashMap<String, Double> markedValueNOK = new HashMap<>();
        Map<String, BigDecimal> investedStockPrices = new HashMap<>();
        String[] investedStocksTickers = new String[investments.size()];
        int i = 0;

        for (Map.Entry<String, Investment> x : investments.entrySet()) {
            investedStocksTickers[i] = x.getValue().getTicker();
            i++;
        }

        StockDataRetriever.getInstance().getMultipleStockPrices(investedStockPrices, investedStocksTickers);

        while (investedStockPrices.size() < investedStocksTickers.length){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        for (Investment x: investments.values()){
            if (!x.getCurrency().equals("NOK")){
                double exchangeCourse = currencyConverter(x);
                currencyCache.put(x.getCurrency(),exchangeCourse);

                BigDecimal currentPriceStock = investedStockPrices.get(x.getTicker());
                markedValueNOK.put(x.getTicker(),(currentPriceStock.doubleValue() * exchangeCourse * x.getVolum()));
            }
            else {
                BigDecimal currentPriceStock = investedStockPrices.get(x.getTicker());
                markedValueNOK.put(x.getTicker(),(currentPriceStock.doubleValue() * x.getVolum()));
            }
        }

        currencyCache.clear();

        return markedValueNOK;
    }

    public double getMarkedValueNOKSingleStock(HashMap<String, Investment> investment, String investedStocksTicker){

        double markedValueNOK = 0;
        Map<String, BigDecimal> investedStockPrice = new HashMap<>();

        StockDataRetriever.getInstance().getStockPrice(investedStockPrice,investedStocksTicker);

        while (investedStockPrice.size() == 0){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        if (!investment.get(investedStocksTicker).getCurrency().equals("NOK")){
            double exchangeCourse = currencyConverter(investment.get(investedStocksTicker));

            BigDecimal currentPriceStock = investedStockPrice.get("Stock");
            markedValueNOK = (currentPriceStock.doubleValue() * exchangeCourse * investment.get(investedStocksTicker).getVolum());
        }
        else {
            BigDecimal currentPriceStock = investedStockPrice.get("Stock");
            markedValueNOK = currentPriceStock.doubleValue() * investment.get(investedStocksTicker).getVolum();
        }

        return markedValueNOK;
    }

    public double getEarningsNOKSingleStock(HashMap<String, Investment> investment,String investedStocksTicker){
        //Calculates ernings in NOK on the investment inputed. Markedvalue-buyvalue. Currency is converted. Returned double with ernings

        double markedValue = StockCalculations.getInstance().getMarkedValueNOKSingleStock(investment,investedStocksTicker);
        double buyingCost;

        if (!(investment.get(investedStocksTicker).getCurrency().equals("NOK"))){
            double exchangeCourse = currencyConverter(investment.get(investedStocksTicker));
            buyingCost = (investment.get(investedStocksTicker).getPrice() * exchangeCourse * investment.get(investedStocksTicker).getVolum());
        }
        else{
             buyingCost = investment.get(investedStocksTicker).getVolum() * investment.get(investedStocksTicker).getPrice();
        }
        return (markedValue - buyingCost);
    }

    public double getEarningsPercentSingleStock(HashMap<String, Investment> investment,String investedStocksTicker){
    //Calculates percent ernings on the investment inputed. Currency is converted. Returned double with percentErnings

        double markedValue = StockCalculations.getInstance().getMarkedValueNOKSingleStock(investment,investedStocksTicker);
        double buyingCost;

        if (!(investment.get(investedStocksTicker).getCurrency().equals("NOK"))){
            double exchangeCourse = currencyConverter(investment.get(investedStocksTicker));
            buyingCost = (investment.get(investedStocksTicker).getPrice() * exchangeCourse * investment.get(investedStocksTicker).getVolum());
        }
        else{
            buyingCost = investment.get(investedStocksTicker).getVolum() * investment.get(investedStocksTicker).getPrice();
        }

        return ((markedValue / buyingCost) * 100 - 100);
    }

    public HashMap<String, Double> getEarningsNOKMultipleStocks(HashMap<String, Investment> investments){
    //calculates ernings in NOK on the stocks inputed. Takes care of currencyconvertion and volume. Returns in hashmap ticker - eranings in NOK

        double markedValue, buyingCost;;
        HashMap<String, Double> earningsNOK = new HashMap<>();

        HashMap<String, Double> currentPriceStocks = StockCalculations.getInstance().getMarkedValueNOKMultipleStocks(investments);

        for (Investment x: investments.values()){
            if (!x.getCurrency().equals("NOK")){
                double exchangeCourse = currencyConverter(x);
                currencyCache.put(x.getCurrency(),exchangeCourse);
                buyingCost = x.getVolum() * x.getPrice() * exchangeCourse;
                markedValue = (currentPriceStocks.get(x.getTicker()));
                earningsNOK.put(x.getTicker(), (markedValue - buyingCost));
            }
            else {
                buyingCost = x.getVolum() * x.getPrice();
                markedValue = (currentPriceStocks.get(x.getTicker()));
                earningsNOK.put(x.getTicker(), (markedValue - buyingCost));
            }
        }
        currencyCache.clear();
        return earningsNOK;
    }

    public HashMap<String, Double> getEarningsPercentMultipleStocks(HashMap<String, Investment> investments) {
    //calculates ernings in percent on the stocks inputed. Takes care of currencyconvertion and volume. Returns in hashmap ticker - eranings in percent

        double markedValue, buyingCost;
        ;
        HashMap<String, Double> earningsNOK = new HashMap<>();

        HashMap<String, Double> currentPriceStocks = StockCalculations.getInstance().getMarkedValueNOKMultipleStocks(investments);

        for (Investment x : investments.values()) {
            if (!x.getCurrency().equals("NOK")) {
                double exchangeCourse = currencyConverter(x);
                currencyCache.put(x.getCurrency(), exchangeCourse);
                buyingCost = x.getVolum() * x.getPrice() * exchangeCourse;
                markedValue = (currentPriceStocks.get(x.getTicker()));
                earningsNOK.put(x.getTicker(), (markedValue / buyingCost) * 100 - 100);
            } else {
                buyingCost = x.getVolum() * x.getPrice();
                markedValue = (currentPriceStocks.get(x.getTicker()));
                earningsNOK.put(x.getTicker(), (markedValue / buyingCost) * 100 - 100);
            }
        }
        currencyCache.clear();
        return earningsNOK;
    }
}
