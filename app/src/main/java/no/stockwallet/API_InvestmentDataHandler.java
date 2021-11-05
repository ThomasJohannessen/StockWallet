package no.stockwallet;

import android.util.Log;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import yahoofinance.Stock;

public class API_InvestmentDataHandler {

    StockViewModel svm;
    HashMap<String, BigDecimal> stockPricesTopp3Bottom3 = new HashMap<>();

    public API_InvestmentDataHandler(StockViewModel viewModel) {
        svm = viewModel;
    }

    public void addFullStockNamesToInvestments(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

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
                    updateInvestment.setFullName(temp.get("Stock").getName());

                    temp.clear();
                }
            }
            }
        });
        thread.start();
    }

    public void addFullStockNameToAInvestment(Investment investment){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Stock> temp = new HashMap<>();
                StockDataRetriever.getInstance().getStockObject(temp,investment.getTicker());

                while (temp.size() < 1){
                    try { TimeUnit.MILLISECONDS.sleep(10);}
                    catch (InterruptedException e) {e.printStackTrace();}
                }

                Investment updateInvestment = svm.getInvestment(investment.getTicker());
                updateInvestment.setFullName(temp.get("Stock").getName());
                temp.clear();
            }
        });
        thread.start();
    }

    public void addTotalEarningsNOKOnStockToInvestments(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, Investment> investments = svm.getStockMap().getValue();

                if(investments.size() > 0) {
                    HashMap<String, Double> temp = StockCalculations.getInstance().getEarningsNOKMultipleStocks(investments);

                    while (temp.size() != investments.size()) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    for (HashMap.Entry<String, Investment> stock : investments.entrySet()) {
                        Investment updateInvestment = svm.getInvestment(stock.getKey());
                        updateInvestment.setTotalEarningsStockNOK(temp.get(stock.getKey()));
                    }
                    temp.clear();
                }
            }
        });
        thread.start();
    }

    public void addTotalEarningsNOKOnSingeStockToInvestment(Investment investment){
        Thread thread = new Thread(() -> {

            String ticker = investment.getTicker();

            HashMap<String, Investment> stock = new HashMap<>();
            stock.put(investment.getTicker(),investment);

            double earnings = 0.00000000000001;
            earnings = StockCalculations.getInstance().getEarningsNOKSingleStock(stock,ticker);

            while (earnings == 0.00000000000001) {
                try {TimeUnit.MILLISECONDS.sleep(10);}
                catch (InterruptedException e) {e.printStackTrace();}
            }

            Investment updateInvestment = svm.getInvestment(ticker);
            updateInvestment.setTotalEarningsStockNOK(earnings);

        });
        thread.start();
    }

    public void addTotalEarningsPercentOnStockToInvestment(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, Investment> investments = svm.getStockMap().getValue();

                if(investments.size() > 0) {
                    HashMap<String, Double> temp = StockCalculations.getInstance().getEarningsPercentMultipleStocks(investments);

                    while (temp.size() != investments.size()) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    for (HashMap.Entry<String, Investment> stock : investments.entrySet()) {
                        Investment updateInvestment = svm.getInvestment(stock.getKey());
                        updateInvestment.setTotalEarningsStockPercent(temp.get(stock.getKey()));
                    }
                    temp.clear();
                }
            }
        });
        thread.start();
    }
    public void addTotalEarningsPercentOnSingeStockToInvestment(Investment investment){
        Thread thread = new Thread(() -> {

            String ticker = investment.getTicker();

            HashMap<String, Investment> stock = new HashMap<>();
            stock.put(investment.getTicker(),investment);

            double earnings = 0.00000000000001;
            earnings = StockCalculations.getInstance().getEarningsPercentSingleStock(stock,ticker);

            while (earnings == 0.00000000000001) {
                try {TimeUnit.MILLISECONDS.sleep(10);}
                catch (InterruptedException e) {e.printStackTrace();}
            }

            Investment updateInvestment = svm.getInvestment(ticker);
            updateInvestment.setTotalEarningsStockPercent(earnings);

        });
        thread.start();
    }


    public void addTotalMarkedValueNOKOnStockToInvestment(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, Investment> investments = svm.getStockMap().getValue();
                if(investments.size() != 0) {

                    HashMap<String, Double> temp = StockCalculations.getInstance().getMarkedValueNOKMultipleStocks(investments);

                    while (temp.size() != investments.size()) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    for (HashMap.Entry<String, Investment> stock : investments.entrySet()) {
                        Investment updateInvestment = svm.getInvestment(stock.getKey());
                        updateInvestment.setTotalStockMarkedValue(temp.get(stock.getKey()));
                    }
                    temp.clear();
                }
            }
        });
        thread.start();
    }
    public void addTotalMarkedValueNOKOnSingeStockToInvestment(Investment investment){
        Thread thread = new Thread(() -> {

            String ticker = investment.getTicker();

            HashMap<String, Investment> stock = new HashMap<>();
            stock.put(investment.getTicker(),investment);

            double marketValue = 0.00000000000001;
            marketValue = StockCalculations.getInstance().getMarkedValueNOKSingleStock(stock,ticker);

            while (marketValue == 0.00000000000001) {
                try {TimeUnit.MILLISECONDS.sleep(10);}
                catch (InterruptedException e) {e.printStackTrace();}
            }

            Investment updateInvestment = svm.getInvestment(ticker);
            updateInvestment.setTotalEarningsStockPercent(marketValue);

        });
        thread.start();
    }

    public void findTopp3AndBootom3() {
        //goes thorough all the investments and finds the biggest loss and winner and puts them in array getTop.. and getBottom..
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, Investment> investments = svm.getStockMap().getValue();
                if(investments.size() != 0) {
                    StockCalculations.getInstance().getIntradayChangesInStocksPercent(investments);
                }
            }
        });
        thread.start();
    }

    public void fetchStockPricesForToppAndBottom3(String[] tickers) {
        //fetches the stockprices for topp3 and bottom3, and saves them in hashmap for later getmetode
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                StockDataRetriever.getInstance().getMultipleStockPrices(stockPricesTopp3Bottom3, tickers);
            }
        });
        thread.start();

    }

    public HashMap<String, BigDecimal> getStockPricesTopp3Bottom3Map() {
        return stockPricesTopp3Bottom3;
    }
}