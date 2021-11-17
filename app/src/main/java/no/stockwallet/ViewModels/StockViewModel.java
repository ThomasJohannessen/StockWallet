package no.stockwallet.ViewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import no.stockwallet.Handlers.API_InvestmentDataHandler;
import no.stockwallet.Model.Investment;
import no.stockwallet.Support.FireBaseJsonSupport;

public class StockViewModel extends ViewModel {

    private MutableLiveData<HashMap<String, Investment>> stockMap = new MutableLiveData<>();
    private ArrayList<Long> historyArrayInvestmentTotalValueForGraph = new ArrayList<>();

    public String[] getInvestmentTickers() {
        String[] investedStocksTickers = new String[getStockMap().getValue().size()];
        int i = 0;

        for (HashMap.Entry<String, Investment> x : getStockMap().getValue().entrySet()) {
            investedStocksTickers[i] = x.getValue().getTicker();
            i++;
        }
        return investedStocksTickers;
    }

    //used by service and underway to update the values on the fly
    public void updateModel(){
        API_InvestmentDataHandler APIhandler = new API_InvestmentDataHandler(this);

        APIhandler.addFullStockNamesAndCurrencyToInvestments();
        APIhandler.addTotalEarningsNOKToInvestments();
        APIhandler.addTotalEarningsPercentToInvestments();
        APIhandler.addTotalMarkedValueNOKToInvestments();
        APIhandler.addIntradayChangePercentToInvestments();
        APIhandler.addCurrentStockPriceToInvestments();
        APIhandler.findBiggestGainerAndLoserInInvestedStocks();
        addDailyDataToHistoryArray();

        //writes new values to db
        FireBaseJsonSupport.writeDB(stockMap.getValue());
    }

    //used when a new investment is added
    public void addValuesToNewInvestment(Investment newInvestment){
        API_InvestmentDataHandler APIhandler = new API_InvestmentDataHandler(this);

        APIhandler.addFullStockNameAndCurrencyToSingleInvestment(newInvestment);
        APIhandler.addTotalMarkedValueNOKToSingleInvestment(newInvestment);
        APIhandler.addTotalEarningsPercentToSingleInvestment(newInvestment);
        APIhandler.addTotalEarningsNOKToSingleInvestment(newInvestment);
        APIhandler.addIntradayChangePercentToSingleInvestment(newInvestment);
        APIhandler.addCurrentStockPriceToSingleInvestment(newInvestment);
        APIhandler.findBiggestGainerAndLoserInInvestedStocks();
    }

    public void addInvestment(Investment newInvestment) {

        HashMap<String, Investment> temp = stockMap.getValue();
        if(temp.get(newInvestment.getTicker()) == null) {
            temp.put(newInvestment.getTicker(), newInvestment);
            stockMap.setValue(temp);
        }
        else {
            Investment existingInvestment = temp.get(newInvestment.getTicker());
            double existPrice = existingInvestment.getAvgBuyPrice() * existingInvestment.getVolume();
            double newPrice = newInvestment.getAvgBuyPrice() * newInvestment.getVolume();
            existingInvestment.setVolume(existingInvestment.getVolume() + newInvestment.getVolume());
            double meanPrice = (existPrice + newPrice) / (existingInvestment.getVolume());
            existingInvestment.setAvgBuyPrice(meanPrice);
        }
        addValuesToNewInvestment(newInvestment);
        FireBaseJsonSupport.writeDB(stockMap.getValue());
    }

    public Investment getInvestment(String ticker){
        return stockMap.getValue().get(ticker);
    }

    public MutableLiveData<HashMap<String, Investment>> getStockMap() {
        if(stockMap == null)
            stockMap = new MutableLiveData<HashMap<String, Investment>>();
        return stockMap;
    }

    public void setStockMap(HashMap<String, Investment> stockMap) {
        this.stockMap.setValue(stockMap);
    }

    public ArrayList<Long> getHistoryArrayInvestmentTotalValueForGraph() {

        return historyArrayInvestmentTotalValueForGraph;
    }

    public void addDailyDataToHistoryArray() {
        API_InvestmentDataHandler API = new API_InvestmentDataHandler(this);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if (dtf.format(now).equals("23:50:00")) {
            int todaysMarkedValue = API.getTotalMarkedValue();

            historyArrayInvestmentTotalValueForGraph.add((long)todaysMarkedValue);
            FireBaseJsonSupport.writeHistoryArrayToDB(historyArrayInvestmentTotalValueForGraph);
        }
    }

    public void setHistoryArrayInvestmentTotalValueForGraph(ArrayList<Long> historyArrayInvestmentTotalValueForGraph) {
        this.historyArrayInvestmentTotalValueForGraph = historyArrayInvestmentTotalValueForGraph;
    }
}

