package no.stockwallet.Handlers;

import android.util.Pair;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import no.stockwallet.Model.Investment;
import no.stockwallet.Support.ValueSetterSupport;
import no.stockwallet.ViewModels.StockViewModel;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class API_InvestmentDataHandler {

    StockViewModel svm;
    private Map<String, Double> currencyCache = new HashMap<>();
    private ArrayList<Pair<String, Double>> topThreeGainerStocks = new ArrayList<Pair<String, Double>>();
    private ArrayList<Pair<String, Double>> bottomThreeLoserStocks = new ArrayList<Pair<String, Double>>();


    private void AlphaVantageInit() {
        Config cfg = Config.builder()
                .key("04C7U8DGXKH0OY8B")
                .timeOut(5)
                .build();

        AlphaVantage.api().init(cfg);
    }

    public API_InvestmentDataHandler(StockViewModel viewModel) {
        svm = viewModel;
        AlphaVantageInit();
    }

    private double currencyConverter(Investment foreignCurrencyInvestment) {

        ValueSetterSupport valueSetterSupport = new ValueSetterSupport();

        Thread thread = new Thread(() -> {

            String inputCurr = foreignCurrencyInvestment.getCurrency();

            if (currencyCache.containsKey(inputCurr)) {
                valueSetterSupport.setReturnValue(currencyCache.get(foreignCurrencyInvestment.getCurrency()));
            } else {
                try {
                    String FxSymbol = inputCurr.toUpperCase(Locale.ROOT) + "NOK=X";
                    BigDecimal conversionRate = YahooFinance.getFx(FxSymbol).getPrice();
                    valueSetterSupport.setReturnValue(conversionRate.doubleValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    
    //all investments
    public void addFullStockNamesAndCurrencyToInvestments(){
        HashMap<String, Investment> investments = svm.getStockMap().getValue();

        if(investments.size() > 0) {
            for (HashMap.Entry<String, Investment> stock : investments.entrySet()) {

                HashMap<String, Stock> temp = new HashMap<>();
                StockDataRetriever.getInstance().getStockObject(temp, stock.getKey());

                while (temp.size() < 1) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Investment updateInvestment = svm.getInvestment(stock.getKey());
                updateInvestment.setCurrency(temp.get("Stock").getCurrency());
                updateInvestment.setFullName(temp.get("Stock").getName());

                temp.clear();
            }
        }
    }
    
    public void addTotalEarningsNOKToInvestments() {
        HashMap<String, Investment> investments = svm.getStockMap().getValue();
        if (investments.size() > 0) {

            Investment USD_currency_get_invest = new Investment("AAPL",0,0,"USD",1);
            currencyCache.put(USD_currency_get_invest.getCurrency(),currencyConverter(USD_currency_get_invest));

            HashMap<String, BigDecimal> temp = new HashMap<>();
            StockDataRetriever.getInstance().getMultipleStockPrices(temp,svm.getInvestmentTickers());    //TODO:Mulig dårlig ytelse

            while (temp.size() != investments.size()) {
                try { TimeUnit.MILLISECONDS.sleep(10);}
                catch (InterruptedException e) {e.printStackTrace();}
            }

            double buyingCost, markedValue;

            for (HashMap.Entry<String, Investment> stock : investments.entrySet()) {
                if (!(stock.getValue().getCurrency().equals("NOK"))){
                    double exchangeCourse = currencyConverter(stock.getValue());
                    markedValue = (temp.get(stock.getValue().getTicker()).doubleValue() * exchangeCourse * stock.getValue().getVolume());
                    buyingCost = (stock.getValue().getAvgBuyPrice() * exchangeCourse * stock.getValue().getVolume());
                }
                else{
                    markedValue = (temp.get(stock.getValue().getTicker()).doubleValue() * stock.getValue().getVolume());
                    buyingCost = stock.getValue().getAvgBuyPrice() * stock.getValue().getVolume();
                }
                Investment updateInvestment = svm.getInvestment(stock.getValue().getTicker());
                updateInvestment.setEarningsNOK(markedValue - buyingCost);
            }
            temp.clear();
            currencyCache.clear();
        }
    }

    public void addTotalEarningsPercentToInvestments(){
        HashMap<String, Investment> investments = svm.getStockMap().getValue();
        if (investments.size() > 0) {

            Investment USD_currency_get_invest = new Investment("AAPL",0,0,"USD",1);
            currencyCache.put(USD_currency_get_invest.getCurrency(),currencyConverter(USD_currency_get_invest));

            HashMap<String, BigDecimal> temp = new HashMap<>();
            StockDataRetriever.getInstance().getMultipleStockPrices(temp,svm.getInvestmentTickers());    //TODO:Mulig dårlig ytelse

            while (temp.size() != investments.size()) {
                try { TimeUnit.MILLISECONDS.sleep(10);}
                catch (InterruptedException e) {e.printStackTrace();}
            }

            double buyingCost, markedValue;

            for (HashMap.Entry<String, Investment> stock : investments.entrySet()) {
                if (!(stock.getValue().getCurrency().equals("NOK"))){
                    double exchangeCourse = currencyConverter(stock.getValue());
                    markedValue = (temp.get(stock.getValue().getTicker()).doubleValue() * exchangeCourse * stock.getValue().getVolume());
                    buyingCost = (stock.getValue().getAvgBuyPrice() * exchangeCourse * stock.getValue().getVolume());
                }
                else{
                    markedValue = (temp.get(stock.getValue().getTicker()).doubleValue() * stock.getValue().getVolume());
                    buyingCost = stock.getValue().getAvgBuyPrice() * stock.getValue().getVolume();
                }
                Investment updateInvestment = svm.getInvestment(stock.getValue().getTicker());
                updateInvestment.setEarningsPercent((markedValue / buyingCost) * 100 - 100);
            }
            temp.clear();
            currencyCache.clear();
        }
    }

    public void addTotalMarkedValueNOKToInvestments(){
        HashMap<String, Investment> investments = svm.getStockMap().getValue();
        if(investments.size() != 0) {

            Investment USD_currency_get_invest = new Investment("AAPL",0,0,"USD",1);
            currencyCache.put(USD_currency_get_invest.getCurrency(),currencyConverter(USD_currency_get_invest));

            HashMap<String, BigDecimal> temp = new HashMap<>();
            StockDataRetriever.getInstance().getMultipleStockPrices(temp,svm.getInvestmentTickers());    //TODO:Mulig dårlig ytelse

            while (temp.size() != investments.size()) {
                try { TimeUnit.MILLISECONDS.sleep(10);}
                catch (InterruptedException e) {e.printStackTrace();}
            }

            double markedValue;
            for (HashMap.Entry<String, Investment> stock : investments.entrySet()) {
                if (!(stock.getValue().getCurrency().equals("NOK"))) {
                    double exchangeCourse = currencyConverter(stock.getValue());
                    markedValue = (temp.get(stock.getValue().getTicker()).doubleValue() * exchangeCourse * stock.getValue().getVolume());
                } else {
                    markedValue = (temp.get(stock.getValue().getTicker()).doubleValue() * stock.getValue().getVolume());
                }
                Investment updateInvestment = svm.getInvestment(stock.getValue().getTicker());
                updateInvestment.setMarkedValueNOK(markedValue);
            }
            temp.clear();
            currencyCache.clear();
        }
    }

    public void addIntradayChangePercentToInvestments(){
        HashMap<String, Investment> investments = svm.getStockMap().getValue();
        if(investments.size() != 0) {

            HashMap<String, BigDecimal> temp = new HashMap<>();
            StockDataRetriever.getInstance().getIntradayChangePercent(temp, svm.getInvestmentTickers());

            while (temp.size() != investments.size()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (HashMap.Entry<String, Investment> stock : investments.entrySet()) {
                Investment updateInvestment = svm.getInvestment(stock.getKey());
                updateInvestment.setIntradayChange(temp.get(stock.getKey()).doubleValue());
            }
            temp.clear();
        }
    }

    public void addCurrentStockPriceToInvestments(){
        HashMap<String, Investment> investments = svm.getStockMap().getValue();
        if(investments.size() != 0) {

            HashMap<String, BigDecimal> temp = new HashMap<>();
            StockDataRetriever.getInstance().getMultipleStockPrices(temp, svm.getInvestmentTickers());

            while (temp.size() != investments.size()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (HashMap.Entry<String, Investment> stock : investments.entrySet()) {
                Investment updateInvestment = svm.getInvestment(stock.getKey());
                updateInvestment.setCurrentStockPrice(temp.get(stock.getKey()).doubleValue());
            }
            temp.clear();
        }
    }


    //a single investment
    public void addFullStockNameAndCurrencyToSingleInvestment(Investment investment){

        HashMap<String, Stock> temp = new HashMap<>();
        StockDataRetriever.getInstance().getStockObject(temp,investment.getTicker());

        while (temp.size() < 1){
            try { TimeUnit.MILLISECONDS.sleep(10);}
            catch (InterruptedException e) {e.printStackTrace();}
        }

        investment.setCurrency(temp.get("Stock").getCurrency());

        Investment updateInvestment = svm.getInvestment(investment.getTicker());
        updateInvestment.setFullName(temp.get("Stock").getName());
        updateInvestment.setCurrency(temp.get("Stock").getCurrency());
        temp.clear();
    }

    public void addTotalMarkedValueNOKToSingleInvestment(Investment investment){
        String ticker = investment.getTicker();
        double markedValue;

        HashMap<String, BigDecimal> temp = new HashMap<>();
        StockDataRetriever.getInstance().getStockPrice(temp,investment.getTicker());

        while (temp.size() < 1){
            try { TimeUnit.MILLISECONDS.sleep(10);}
            catch (InterruptedException e) {e.printStackTrace();}
        }

        if (!(investment.getCurrency().equals("NOK"))){
            double exchangeCourse = currencyConverter(investment);
            markedValue = (temp.get("Stock").doubleValue() * exchangeCourse * investment.getVolume());
        }
        else{
            markedValue = (temp.get("Stock").doubleValue() * investment.getVolume());
        }
        Investment updateInvestment = svm.getInvestment(ticker);
        updateInvestment.setMarkedValueNOK(markedValue);
    }

    public void addTotalEarningsPercentToSingleInvestment(Investment investment){
        String ticker = investment.getTicker();

        HashMap<String, BigDecimal> temp = new HashMap<>();
        StockDataRetriever.getInstance().getStockPrice(temp,investment.getTicker());

        while (temp.size() < 1){
            try { TimeUnit.MILLISECONDS.sleep(10);}
            catch (InterruptedException e) {e.printStackTrace();}
        }

        double buyingCost, markedValue;

        if (!(investment.getCurrency().equals("NOK"))){
            double exchangeCourse = currencyConverter(investment);
            markedValue = (temp.get("Stock").doubleValue() * exchangeCourse * investment.getVolume());
            buyingCost = (investment.getAvgBuyPrice() * exchangeCourse * investment.getVolume());
        }
        else{
            markedValue = (temp.get("Stock").doubleValue() * investment.getVolume());
            buyingCost = investment.getAvgBuyPrice() * investment.getVolume();
        }
        Investment updateInvestment = svm.getInvestment(ticker);
        updateInvestment.setEarningsPercent((markedValue / buyingCost) * 100 - 100);
    }

    public void addTotalEarningsNOKToSingleInvestment(Investment investment){
        String ticker = investment.getTicker();

        HashMap<String, BigDecimal> temp = new HashMap<>();
        StockDataRetriever.getInstance().getStockPrice(temp,investment.getTicker());

        while (temp.size() < 1){
            try { TimeUnit.MILLISECONDS.sleep(10);}
            catch (InterruptedException e) {e.printStackTrace();}
        }

        double buyingCost, markedValue;

        if (!(investment.getCurrency().equals("NOK"))){
            double exchangeCourse = currencyConverter(investment);
            markedValue = (temp.get("Stock").doubleValue() * exchangeCourse * investment.getVolume());
            buyingCost = (investment.getAvgBuyPrice() * exchangeCourse * investment.getVolume());
        }
        else{
            markedValue = (temp.get("Stock").doubleValue() * investment.getVolume());
            buyingCost = investment.getAvgBuyPrice() * investment.getVolume();
        }
        Investment updateInvestment = svm.getInvestment(ticker);
        updateInvestment.setEarningsNOK(markedValue - buyingCost);
    }

    public void addIntradayChangePercentToSingleInvestment(Investment investment){
        HashMap<String, BigDecimal> temp = new HashMap<>();
        String[] ticker =  {investment.getTicker()};


        StockDataRetriever.getInstance().getIntradayChangePercent(temp,ticker);

        while (temp.size() < 1){
            try { TimeUnit.MILLISECONDS.sleep(10);}
            catch (InterruptedException e) {e.printStackTrace();}
        }

        Investment updateInvestment = svm.getInvestment(investment.getTicker());

        updateInvestment.setIntradayChange(temp.get(investment.getTicker()).doubleValue());
        temp.clear();
    }

    public void addCurrentStockPriceToSingleInvestment(Investment investment){
        HashMap<String, BigDecimal> temp = new HashMap<>();
        StockDataRetriever.getInstance().getStockPrice(temp,investment.getTicker());

        while (temp.size() < 1){
            try { TimeUnit.MILLISECONDS.sleep(10);}
            catch (InterruptedException e) {e.printStackTrace();}
        }

        Investment updateInvestment = svm.getInvestment(investment.getTicker());
        updateInvestment.setCurrentStockPrice(temp.get("Stock").doubleValue());
        temp.clear();
    }

    //calculate topp3 and bottom3
    public void findBiggestGainerAndLoserInInvestedStocks() {
        //finds top 3 looser this day and top 3 gainers. Puts them in array with separet getters.

        HashMap<String, BigDecimal> investedStockChangePercent = new HashMap<>();
        StockDataRetriever.getInstance().getIntradayChangePercent(investedStockChangePercent, svm.getInvestmentTickers()); //TODO:Mulig dårlig ytelse

        while (investedStockChangePercent.size() < svm.getInvestmentTickers().length){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        List<Map.Entry<String, BigDecimal>> list =
                new LinkedList<Map.Entry<String, BigDecimal>>(investedStockChangePercent.entrySet());

        list.sort(Map.Entry.comparingByValue());
        int listSize = list.size();


        if (listSize >= 6) {
            for (int i = 0; i < 3; i++) {
                Pair<String, Double> pair = new Pair<>(list.get(i).getKey(), list.get(i).getValue().doubleValue());
                bottomThreeLoserStocks.add(pair);
            }

            for (int i = listSize - 1; i > (listSize - 4); i--) {
                Pair<String, Double> pair = new Pair<>(list.get(i).getKey(), list.get(i).getValue().doubleValue());
                topThreeGainerStocks.add(pair);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                Pair<String, Double> pair = new Pair<>(null, null);
                topThreeGainerStocks.add(pair);
                bottomThreeLoserStocks.add(pair);
            }
        }
    }

    public ArrayList<Pair<String, Double>> getTopThreeGainerStocks() {
        return topThreeGainerStocks;
    }

    public ArrayList<Pair<String, Double>> getBottomThreeLoserStocks() {
        return bottomThreeLoserStocks;
    }

    //find total percentEarnings and totalInvested
    public int getTotalMarkedValue() {
        //calculated the total of all invedsted stocks NOK. returns the number
        HashMap<String, Investment> investments = svm.getStockMap().getValue();

        if(investments.size() > 0) {

            double totSumMarkedValue = 0;

            for (Investment x : investments.values()) {
                totSumMarkedValue += x.getMarkedValueNOK();
            }

            return (int) totSumMarkedValue;
        }
        else{return 0;}
    }

    public double getTotalPercentEarnings() {
        //gets the total change for in inputed stocks this day in percent. Returns double with percent
        HashMap<String, Investment> investments = svm.getStockMap().getValue();

        if(investments.size() > 0) {
            double totalChange = 0;
            double totalEarned = 0;
            double markedValue = 0;

            //get the earnings for all stocks total
            for (Investment x : investments.values()) {
                totalEarned += x.getEarningsNOK();
            }

            //get the total marked value
            for (Investment x : investments.values()) {
                markedValue += x.getMarkedValueNOK();
            }


            totalChange = ((totalEarned / markedValue) * 100);

            DecimalFormat df = new DecimalFormat("#.##");
            totalChange = Double.parseDouble(df.format(totalChange));


            return Double.parseDouble(df.format(totalChange));
        }
        else{return 0;}
    }
}