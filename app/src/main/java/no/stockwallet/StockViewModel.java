package no.stockwallet;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class StockViewModel extends ViewModel {

    private MutableLiveData<HashMap<String, Investment>> stockMap = new MutableLiveData<>();

    public String[] getInvestmentTickers(){
        String[] investedStocksTickers = new String[getStockMap().getValue().size()];
        int i = 0;

        for (Map.Entry<String, Investment> x : getStockMap().getValue().entrySet()) {
            investedStocksTickers[i] = x.getValue().getTicker();
            i++;
        }
        return investedStocksTickers;
    }

    public void fetchUserData() {
        FireBaseJsonSupport.readDB(this);
    }

    public void setStockMap(HashMap<String, Investment> stockMap) {
        this.stockMap.setValue(stockMap);
    }

    public void addInvestment(Investment investment) {

        HashMap<String, Investment> temp = stockMap.getValue();
        if (temp != null) {
            temp.put(investment.getTicker(), investment);
            FireBaseJsonSupport.writeDB(stockMap.getValue());
            stockMap.setValue(temp);
        }
    }

    public void editInvestment() {}

    public void sellInvestment(String ticker, int volume) {} //TEMP

    public void removeInvestment(String ticker) {
        HashMap<String, Investment> temp = stockMap.getValue();
        if (temp != null) {
            temp.remove(ticker);
        }
        stockMap.setValue(temp);
    }

    public void checkIfAlreadyInvestedIn(){


    }

    public void updateUserPortfolio() {}

    public void fetchUserPortfolio() {}

    public MutableLiveData<HashMap<String, Investment>> getStockMap() {
        if(stockMap == null)
            stockMap = new MutableLiveData<HashMap<String, Investment>>();
        return stockMap;
    }

    private void toJson() {
        String data = FireBaseJsonSupport.convertToJson(stockMap.getValue());
        Log.d("JSONTWO", new Gson().fromJson(data, new TypeToken<HashMap<String, Investment>>(){}.getType()).toString());
        Log.d("JSON", FireBaseJsonSupport.convertToJson(stockMap.getValue()));
        FireBaseJsonSupport.test(stockMap.getValue());
    }

}
