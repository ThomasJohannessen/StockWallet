package no.stockwallet.Handlers;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;

import java.io.IOException;
import java.math.BigDecimal;
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

    public void getStockPrice(Map<String, BigDecimal> returVar, String ticker){
        Thread thread = new Thread(() -> {
            try {
                BigDecimal stockValue = null;
                Stock tempStock = new Stock("");
                Stock stock = tempStock;
                stock = YahooFinance.get(ticker);

                while (stock == tempStock){
                    TimeUnit.MILLISECONDS.sleep(10);
                }

                if (stock == null){
                    returVar.put("Stock",null);
                }
                returVar.put("Stock",stock.getQuote().getPrice());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void getMultipleStockPrices(Map<String, BigDecimal> returnVar, String[] tickerArr){
        Thread thread = new Thread(() -> {
            try {
                Map<String, Stock> stocks = YahooFinance.get(tickerArr);

                while (stocks.isEmpty()){
                    TimeUnit.MILLISECONDS.sleep(10);
                }

                //gives the right format of the key-value pair
                for (Map.Entry<String,Stock> entry : stocks.entrySet())
                    returnVar.put(entry.getKey(), entry.getValue().getQuote().getPrice());

                while (returnVar.size() < stocks.size()){
                    TimeUnit.MILLISECONDS.sleep(10);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void getIntradayChangePercent(Map<String, BigDecimal> returnVar, String[] tickerArr){
        Thread thread = new Thread(() -> {
            try {
                for (String x : tickerArr) {
                    BigDecimal stockChangePercent = null;
                    stockChangePercent = YahooFinance.get(x).getQuote().getChangeInPercent();

                    while (stockChangePercent == null) {
                        TimeUnit.MILLISECONDS.sleep(10);
                    }

                    returnVar.put(x,stockChangePercent);
                }

                while (returnVar.size() < tickerArr.length){
                    TimeUnit.MILLISECONDS.sleep(10);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();

    }

    public void getStockObject(Map<String, Stock> returVar, String ticker){
        Thread thread = new Thread(() -> {
            try {

                Stock stock = YahooFinance.get(ticker);

                while (stock == null) {
                    TimeUnit.MILLISECONDS.sleep(10);
                }

                returVar.put("Stock",stock);

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
