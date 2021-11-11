package no.stockwallet.Handlers;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import no.stockwallet.Model.Investment;
import no.stockwallet.ViewModels.StockViewModel;
import yahoofinance.Stock;

public class API_InvestmentDataHandler {

    StockViewModel svm;

    public API_InvestmentDataHandler(StockViewModel viewModel) {
        svm = viewModel;
    }

    public void addFullStockNamesToInvestments(){
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

    public void addFullStockNameToAInvestment(Investment investment){
        Thread thread = new Thread(() -> {
            HashMap<String, Stock> temp = new HashMap<>();
            StockDataRetriever.getInstance().getStockObject(temp,investment.getTicker());

            while (temp.size() < 1){
                try { TimeUnit.MILLISECONDS.sleep(10);}
                catch (InterruptedException e) {e.printStackTrace();}
            }

            Investment updateInvestment = svm.getInvestment(investment.getTicker());
            updateInvestment.setFullName(temp.get("Stock").getName());
            temp.clear();
        });
        thread.start();
    }

    public void addTotalEarningsNOKOnStockToInvestments(){
        Thread thread = new Thread(() -> {

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
                    updateInvestment.setEarningsNOK(temp.get(stock.getKey()));
                }
                temp.clear();
            }
        });
        thread.start();
    }

    public void addTotalEarningsPercentOnStockToInvestment(){
        Thread thread = new Thread(() -> {

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
                    updateInvestment.setEarningsPercent(temp.get(stock.getKey()));
                }
                temp.clear();
            }
        });
        thread.start();
    }

    public void addTotalMarkedValueNOKOnStockToInvestment(){
        Thread thread = new Thread(() -> {

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
                    updateInvestment.setMarkedValue(temp.get(stock.getKey()));
                }
                temp.clear();
            }
        });
        thread.start();
    }
    public void addTotalMarkedValueNOKOnSingleStockToInvestment(Investment investment){
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
            updateInvestment.setEarningsPercent(marketValue);

    }

    public void addTotalEarningsPercentOnSingleStockToInvestment(Investment investment){
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
            updateInvestment.setEarningsPercent(earnings);
    }

    public void addTotalEarningsNOKOnSingleStockToInvestment(Investment investment){
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
            updateInvestment.setEarningsNOK(earnings);
    }
}