package no.stockwallet;

import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import yahoofinance.Stock;

public class API_InvestmentDataHandler {

    StockViewModel svm = new StockViewModel();

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

    public void addTotalEarningsNOKOnStockToInvestments(){
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

    public void addTotalEarningsPercentOnStockToInvestment(){
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

    public void addTotalMarkedValueNOKOnStockToInvestment(){
        HashMap<String, Investment> investments = svm.getStockMap().getValue();

        Log.d("TotaltInvestert-dummeylength-API", String.valueOf(investments.size()));
        Log.d("TotaltInvestert-data-API", String.valueOf(investments.get("MPCC.OL").getFullName()));
        if(investments.size() > 0) {

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
                updateInvestment.setTotalStockMarkedValue(Integer.valueOf(String.valueOf(temp.get(stock.getKey()))));
            }
            temp.clear();
        }
    }
}