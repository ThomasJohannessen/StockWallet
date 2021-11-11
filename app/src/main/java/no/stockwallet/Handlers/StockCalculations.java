package no.stockwallet.Handlers;

import android.util.Pair;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import no.stockwallet.Model.Investment;
import no.stockwallet.Support.ValueSetterSupport;
import yahoofinance.YahooFinance;

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

                String inputCurr = foreignCurrencyInvestment.getCurrency();

                if (currencyCache.containsKey(inputCurr)){
                    valueSetterSupport.setReturnValue(currencyCache.get(foreignCurrencyInvestment.getCurrency()));
                }else {
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




    public int getTotalMarkedValue(HashMap<String, Investment> investments){
        //calculated the total of all invedsted stocks and converts to NOK. returns the number
        //fake invest to cache currency-conversion-rate for USD to not use time for this later

        Investment USD_currency_get_invest = new Investment("AAPL",0,0,"USD",1);
        currencyCache.put(USD_currency_get_invest.getCurrency(),currencyConverter(USD_currency_get_invest));
        
        ArrayList<Integer> totSumMarkedValueReturnArr = new ArrayList<>();

        Thread thread = new Thread(() -> {

            //fake invest to cache currency-conversion-rate for USD to not use time for this later
            Investment USD_currency_get_invest1 = new Investment("AAPL", 0, 0, "USD", 1);
            currencyCache.put(USD_currency_get_invest1.getCurrency(), currencyConverter(USD_currency_get_invest1));

            double totSumMarkedValue = 0;

            Map<String, BigDecimal> investedStockPrices = new HashMap<>();
            String[] investedStocksTickers = new String[investments.size()];
            int i = 0;

            for (Map.Entry<String, Investment> x : investments.entrySet()) {
                investedStocksTickers[i] = x.getValue().getTicker();
                i++;
            }

            StockDataRetriever.getInstance().getMultipleStockPrices(investedStockPrices, investedStocksTickers);

            while (investedStockPrices.size() < investedStocksTickers.length) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (Investment x : investments.values()) {
                if (!x.getCurrency().equals("NOK")) {
                    double exchangeCourse = currencyConverter(x);
                    currencyCache.put(x.getCurrency(), exchangeCourse);

                    BigDecimal currentPriceStock = investedStockPrices.get(x.getTicker());
                    totSumMarkedValue += (currentPriceStock.doubleValue() * exchangeCourse * x.getVolume());
                } else {
                    BigDecimal currentPriceStock = investedStockPrices.get(x.getTicker());
                    totSumMarkedValue += currentPriceStock.doubleValue() * x.getVolume();
                }
            }

            currencyCache.clear();

            totSumMarkedValueReturnArr.add((int) totSumMarkedValue);
        });
        thread.start();

        while(totSumMarkedValueReturnArr.size() == 0){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) {e.printStackTrace();}
        }

        return totSumMarkedValueReturnArr.get(0);
    }

    public double getTotalPercentEarnings(HashMap<String, Investment> investments){
        //gets the total change for in inputed stocks this day in percent. Returns double with percent

        ArrayList<Double> totalChangeReturnArr = new ArrayList<>();
        Thread thread = new Thread(() -> {

            double totalChange = 0;
            double totalEarned = 0;
            HashMap<String, Double> investedStockEarnedNOK = new HashMap<>();
            investedStockEarnedNOK = getEarningsNOKMultipleStocks(investments);

            for (HashMap.Entry<String, Double> x : investedStockEarnedNOK.entrySet()) {
                totalEarned += x.getValue();
            }
            int markedValue = getTotalMarkedValue(investments);

            totalChange = ((totalEarned / markedValue) * 100);

            DecimalFormat df = new DecimalFormat("#.##");
            totalChange = Double.parseDouble(df.format(totalChange));

            totalChangeReturnArr.add(Double.parseDouble(df.format(totalChange)));
        });
        thread.start();

            while(totalChangeReturnArr.size() == 0){
                try { TimeUnit.MILLISECONDS.sleep(10); }
                catch (InterruptedException e) {e.printStackTrace();}
            }

            return totalChangeReturnArr.get(0);
    }

    public HashMap<String, BigDecimal> getIntradayChangesInStocksPercent(HashMap<String, Investment> investments){
        //gets the change for in inputed stocks this day in percent. Returns hashmap with ticker-change percent

        ArrayList<HashMap<String, BigDecimal>> investedStockChangePercentReturnArr = new ArrayList<>();

        Thread thread = new Thread(() -> {
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
            investedStockChangePercentReturnArr.add(investedStockChangePercent);
        });
        thread.start();

            while(investedStockChangePercentReturnArr.size() == 0){
                try { TimeUnit.MILLISECONDS.sleep(10); }
                catch (InterruptedException e) {e.printStackTrace();}
            }

            return investedStockChangePercentReturnArr.get(0);
    }

    public void findBiggestGainerAndLoserInInvestedStocks(HashMap<String, BigDecimal> hashmapChangesInInvestedStocksPercent) {
        //finds top 3 looser this day and top 3 gainers. Puts them in array with separet getters.

        List<Map.Entry<String, BigDecimal> > list =
                new LinkedList<Map.Entry<String, BigDecimal> >(hashmapChangesInInvestedStocksPercent.entrySet());

        list.sort(Map.Entry.comparingByValue());
        int listSize = list.size();
        

        if(listSize >= 6) {
            for (int i = 0; i < 3; i++) {
                Pair<String, BigDecimal> pair = new Pair<>(list.get(i).getKey(), list.get(i).getValue());
                bottomThreeLoserStocks.add(pair);
            }

            for (int i = listSize - 1; i > (listSize - 4); i--) {
                Pair<String, BigDecimal> pair = new Pair<>(list.get(i).getKey(), list.get(i).getValue());
                topThreeGainerStocks.add(pair);
            }
        }
        else{
            for (int i = 0; i < 3; i++) {
                Pair<String, BigDecimal> pair = new Pair<>(null, null);
                topThreeGainerStocks.add(pair);
                bottomThreeLoserStocks.add(pair);
            }
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

        ArrayList<HashMap<String, Double>> markedValueNOKReturnArr = new ArrayList<>();
        Thread thread = new Thread(() -> {

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
                    markedValueNOK.put(x.getTicker(),(currentPriceStock.doubleValue() * exchangeCourse * x.getVolume()));
                }
                else {
                    BigDecimal currentPriceStock = investedStockPrices.get(x.getTicker());
                    markedValueNOK.put(x.getTicker(),(currentPriceStock.doubleValue() * x.getVolume()));
                }
            }

            currencyCache.clear();
            markedValueNOKReturnArr.add(markedValueNOK);
        });
        thread.start();

            while(markedValueNOKReturnArr.size() == 0){
                try { TimeUnit.MILLISECONDS.sleep(10); }
                catch (InterruptedException e) {e.printStackTrace();}
            }

            return markedValueNOKReturnArr.get(0);
    }

    public double getMarkedValueNOKSingleStock(HashMap<String, Investment> investment, String investedStocksTicker){

        ArrayList<Double> markedValueNOKReturnArr = new ArrayList<>();
        Thread thread = new Thread(() -> {
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
                markedValueNOK = (currentPriceStock.doubleValue() * exchangeCourse * investment.get(investedStocksTicker).getVolume());
            }
            else {
                BigDecimal currentPriceStock = investedStockPrice.get("Stock");
                markedValueNOK = currentPriceStock.doubleValue() * investment.get(investedStocksTicker).getVolume();
            }
            markedValueNOKReturnArr.add(markedValueNOK);
        });
        thread.start();

        while(markedValueNOKReturnArr.size() == 0){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) {e.printStackTrace();}
        }
        return markedValueNOKReturnArr.get(0);
    }

    public double getEarningsNOKSingleStock(HashMap<String, Investment> investment,String investedStocksTicker){
        //Calculates ernings in NOK on the investment inputed. Markedvalue-buyvalue. Currency is converted. Returned double with ernings

        ArrayList<Double> earningsNOKReturnArr = new ArrayList<>();
        Thread thread = new Thread(() -> {

            double markedValue = StockCalculations.getInstance().getMarkedValueNOKSingleStock(investment,investedStocksTicker);
            double buyingCost;

            if (!(investment.get(investedStocksTicker).getCurrency().equals("NOK"))){
                double exchangeCourse = currencyConverter(investment.get(investedStocksTicker));
                buyingCost = (investment.get(investedStocksTicker).getAvgBuyPrice() * exchangeCourse * investment.get(investedStocksTicker).getVolume());
            }
            else{
                 buyingCost = investment.get(investedStocksTicker).getVolume() * investment.get(investedStocksTicker).getAvgBuyPrice();
            }
            earningsNOKReturnArr.add((markedValue - buyingCost));
        });
        thread.start();

        while(earningsNOKReturnArr.size() == 0){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) {e.printStackTrace();}
        }
        return earningsNOKReturnArr.get(0);
    }

    public double getEarningsPercentSingleStock(HashMap<String, Investment> investment,String investedStocksTicker){
    //Calculates percent ernings on the investment inputed. Currency is converted. Returned double with percentErnings

        ArrayList<Double> earningsPercentReturnArr = new ArrayList<>();
        Thread thread = new Thread(() -> {

            double markedValue = StockCalculations.getInstance().getMarkedValueNOKSingleStock(investment,investedStocksTicker);
            double buyingCost;

            if (!(investment.get(investedStocksTicker).getCurrency().equals("NOK"))){
                double exchangeCourse = currencyConverter(investment.get(investedStocksTicker));
                buyingCost = (investment.get(investedStocksTicker).getAvgBuyPrice() * exchangeCourse * investment.get(investedStocksTicker).getVolume());
            }
            else{
                buyingCost = investment.get(investedStocksTicker).getVolume() * investment.get(investedStocksTicker).getAvgBuyPrice();
            }
            earningsPercentReturnArr.add(((markedValue / buyingCost) * 100 - 100));
        });
        thread.start();

        while(earningsPercentReturnArr.size() == 0){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) {e.printStackTrace();}
        }

        return earningsPercentReturnArr.get(0);
    }

    public HashMap<String, Double> getEarningsNOKMultipleStocks(HashMap<String, Investment> investments){
    //calculates ernings in NOK on the stocks inputed. Takes care of currencyconvertion and volume. Returns in hashmap ticker - eranings in NOK

        ArrayList<HashMap<String, Double>> earningsNOKReturnArr = new ArrayList<>();
        Thread thread = new Thread(() -> {

            double markedValue, buyingCost;;
            HashMap<String, Double> earningsNOK = new HashMap<>();

            HashMap<String, Double> currentPriceStocks = StockCalculations.getInstance().getMarkedValueNOKMultipleStocks(investments);

            for (Investment x: investments.values()){
                if (!x.getCurrency().equals("NOK")){
                    double exchangeCourse = currencyConverter(x);
                    currencyCache.put(x.getCurrency(),exchangeCourse);
                    buyingCost = x.getVolume() * x.getAvgBuyPrice() * exchangeCourse;
                    markedValue = (currentPriceStocks.get(x.getTicker()));
                    earningsNOK.put(x.getTicker(), (markedValue - buyingCost));
                }
                else {
                    buyingCost = x.getVolume() * x.getAvgBuyPrice();
                    markedValue = (currentPriceStocks.get(x.getTicker()));
                    earningsNOK.put(x.getTicker(), (markedValue - buyingCost));
                }
            }
            currencyCache.clear();

            earningsNOKReturnArr.add(earningsNOK);
        });
        thread.start();

        while(earningsNOKReturnArr.size() == 0){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) {e.printStackTrace();}
        }

        return earningsNOKReturnArr.get(0);
    }

    public HashMap<String, Double> getEarningsPercentMultipleStocks(HashMap<String, Investment> investments) {
    //calculates ernings in percent on the stocks inputed. Takes care of currencyconvertion and volume. Returns in hashmap ticker - eranings in percent


        ArrayList<HashMap<String, Double>> earningsNOKReturnArr = new ArrayList<>();
        Thread thread = new Thread(() -> {
            double markedValue, buyingCost;

            HashMap<String, Double> earningsNOK = new HashMap<>();

            HashMap<String, Double> currentPriceStocks = StockCalculations.getInstance().getMarkedValueNOKMultipleStocks(investments);

            for (Investment x : investments.values()) {
                if (!x.getCurrency().equals("NOK")) {
                    double exchangeCourse = currencyConverter(x);
                    currencyCache.put(x.getCurrency(), exchangeCourse);
                    buyingCost = x.getVolume() * x.getAvgBuyPrice() * exchangeCourse;
                    markedValue = (currentPriceStocks.get(x.getTicker()));
                    earningsNOK.put(x.getTicker(), (markedValue / buyingCost) * 100 - 100);
                } else {
                    buyingCost = x.getVolume() * x.getAvgBuyPrice();
                    markedValue = (currentPriceStocks.get(x.getTicker()));
                    earningsNOK.put(x.getTicker(), (markedValue / buyingCost) * 100 - 100);
                }
            }
            currencyCache.clear();

            earningsNOKReturnArr.add(earningsNOK);
        });
        thread.start();

        while(earningsNOKReturnArr.size() == 0){
            try { TimeUnit.MILLISECONDS.sleep(10); }
            catch (InterruptedException e) {e.printStackTrace();}
        }

        return earningsNOKReturnArr.get(0);
    }
}
