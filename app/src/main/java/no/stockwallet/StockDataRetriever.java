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

    public void getStockPrice(Map<String, BigDecimal> returVar, String ticker){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BigDecimal stockValue = null;
                    Stock stock = null;
                    stock = YahooFinance.get(ticker);

                    while (stock == null){
                        TimeUnit.MILLISECONDS.sleep(10);
                    }
                    returVar.put("Stock",stock.getQuote().getPrice());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                    /*
                    if (stock == null){
                        //stock = alphaVantageStockRetriever(ticker);
                    }*/
            }
        });
        thread.start();
    }

    public void getMultipleStockPrices(Map<String, BigDecimal> returnVar, String[] tickerArr){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Stock> stocks = YahooFinance.get(tickerArr);

                    while (stocks.isEmpty()){
                        TimeUnit.MILLISECONDS.sleep(10);
                    }

                    //gives the right format of the key-value pair
                    for (Map.Entry<String,Stock> entry : stocks.entrySet())
                        returnVar.put(entry.getKey(), entry.getValue().getQuote().getPrice());

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                    /*
                    if (stock == null){
                        //stock = alphaVantageStockRetriever(ticker);
                    }*/ //TODO:fix if not found: send to Alpha Vantage
            }
        });
        thread.start();
    }

}
