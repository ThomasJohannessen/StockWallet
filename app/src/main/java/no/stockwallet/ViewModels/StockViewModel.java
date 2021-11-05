package no.stockwallet.ViewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import no.stockwallet.Handlers.API_InvestmentDataHandler;
import no.stockwallet.Model.Investment;
import no.stockwallet.Support.FireBaseJsonSupport;

public class StockViewModel extends ViewModel {

    private MutableLiveData<HashMap<String, Investment>> stockMap = new MutableLiveData<>();

    public void fetchUserData() {
        FireBaseJsonSupport.readDB(this);
    }

    public void addAPIvaluesToInvestmentObjects(){
        API_InvestmentDataHandler APIhandler = new API_InvestmentDataHandler(this);

        APIhandler.addTotalMarkedValueNOKOnStockToInvestment();
        APIhandler.addFullStockNamesToInvestments();
        APIhandler.addTotalEarningsPercentOnStockToInvestment();
        APIhandler.addTotalEarningsNOKOnStockToInvestments();
    }

    public void addInvestment(Investment newInvestment) {
        // TODO: Hvis eksisterer
        HashMap<String, Investment> temp = stockMap.getValue();
        if(temp.get(newInvestment.getTicker()) == null) {
            temp.put(newInvestment.getTicker(), newInvestment);
            API_InvestmentDataHandler api = new API_InvestmentDataHandler(this);
            api.addTotalEarningsNOKOnSingleStockToInvestment(newInvestment);
            api.addTotalMarkedValueNOKOnSingleStockToInvestment(newInvestment);
            api.addTotalEarningsPercentOnSingleStockToInvestment(newInvestment);
            api.addFullStockNamesToInvestments();
            stockMap.setValue(temp);
        }
        else {
            Investment existingInvestment = temp.get(newInvestment.getTicker());
            double existPrice = existingInvestment.getPrice() * existingInvestment.getVolum();
            Log.d("MATTE", "ExistPrice = " + " " + String.valueOf(existPrice));
            double newPrice = newInvestment.getPrice() * newInvestment.getVolum();
            Log.d("MATTE", "NewPrice = " + " " + String.valueOf(newPrice));
            existingInvestment.setVolum(existingInvestment.getVolum() + newInvestment.getVolum());
            Log.d("MATTE", "VOLUME = " + " " + String.valueOf(existingInvestment.getVolum()));
            double meanPrice = (existPrice + newPrice) / (existingInvestment.getVolum());
            Log.d("MATTE", "MeanPrice = " + " " + String.valueOf(meanPrice));
            existingInvestment.setPrice(meanPrice);
        }
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
}

