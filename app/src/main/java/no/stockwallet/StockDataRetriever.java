package no.stockwallet;

import android.util.Log;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class StockDataRetriever {

    private static StockDataRetriever StockRetrieverInstance = null;

    private void AlphaVantageInit() {
        Config cfg = Config.builder()
                .key("04C7U8DGXKH0OY8B")
                .timeOut(5)
                .build();

        AlphaVantage.api().init(cfg);
    }

    private StockDataRetriever(){
        AlphaVantageInit();
    }

    public static StockDataRetriever getInstance(){
        if (StockRetrieverInstance == null)
            StockRetrieverInstance = new StockDataRetriever();

        return StockRetrieverInstance;
    }

    private BigDecimal alphaVantageStockRetriever(String ticker){
        return null;
        //TODO: TBD
    }

    public BigDecimal getStockPrice(String ticker){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                BigDecimal stockValue = null;

                    try {
                        Stock stock = YahooFinance.get(ticker);
                        TimeUnit.SECONDS.sleep(3);
                        stockValue = stock.getQuote().getPrice();
                        Log.d("Stock-class", String.valueOf(stockValue));
                        StockValueSetterSupport.setReturnValue(stockValue);

                    } catch (NullPointerException e) {
                        StockValueSetterSupport.setReturnValue(null);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*
                    if (stock == null){
                        //stock = alphaVantageStockRetriever(ticker);
                    }*/
            }});
        thread.start();
        return StockValueSetterSupport.getReturnValue();
    }

    public Map<String, BigDecimal> getMultipleStockPrice(String[] tickerArr){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Map<String, Stock> stocks = YahooFinance.get(tickerArr);
                    TimeUnit.SECONDS.sleep(3);

                    Map<String, BigDecimal> stocksValue = new HashMap<>();

                    for (Map.Entry<String,Stock> entry : stocks.entrySet())
                        stocksValue.put(entry.getKey(), entry.getValue().getQuote().getPrice());



                    //Log.d("Stock-class", String.valueOf(stockValue));
                    StockValueSetterSupport.setReturnValueMultiple(stocksValue);

                } catch (NullPointerException e) {
                    StockValueSetterSupport.setReturnValueMultiple(null);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                    /*
                    if (stock == null){
                        //stock = alphaVantageStockRetriever(ticker);
                    }*/
            }});
        thread.start();
        return StockValueSetterSupport.getReturnValueMultiple();
    }


}
