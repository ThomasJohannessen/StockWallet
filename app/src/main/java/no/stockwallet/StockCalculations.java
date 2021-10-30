package no.stockwallet;

import android.util.Log;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import no.stockwallet.Investment;
import no.stockwallet.ValueSetterSupport;

public class StockCalculations {

    private static StockCalculations StockCalculationInstance = null;

    Map<String, Double> currencyCache = new HashMap<>();

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
        //fake invest to cache currency-conversion-rate for USD to not use time for this later
        Investment USD_currency_get_invest = new Investment("AAPL",0,0,"USD",1);
        currencyCache.put(USD_currency_get_invest.getCurrency(),currencyConverter(USD_currency_get_invest));

long start = System.currentTimeMillis();
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

        long end = System.currentTimeMillis();
        long elapsedTime = (end - start);
        Log.d("TotaltInvestert-tid", String.valueOf(elapsedTime));
        return (int) totSumMarkedValue;
    }























}
