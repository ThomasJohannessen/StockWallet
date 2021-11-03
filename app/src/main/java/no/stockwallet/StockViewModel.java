package no.stockwallet;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.Arrays;
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

    public void fillWithDummyData() {
        Investment invest1 = new Investment("NHY.OL",200,60,"NOK",1);
        Investment invest2 = new Investment("AKH.OL",100,30,"NOK",10);
        Investment invest3= new Investment("NOD.OL",10,250,"NOK",100);
        Investment invest4 = new Investment("ITERA.OL",1000,12.20,"NOK",1.05);
        Investment invest5 = new Investment("MPCC.OL",65,20,"NOK",35);
        Investment invest6 = new Investment("KAHOT.OL",80,60,"NOK",5);
        Investment invest7 = new Investment("FLYR.OL",50,1.58,"NOK",3);
        Investment invest8 = new Investment("LCID",100,20,"USD",9);
        Investment invest9 = new Investment("MSFT",1000,250,"USD",3);
        Investment invest10 = new Investment("AKSO.OL",200,12,"NOK",50);

        HashMap<String, Investment> temp = new HashMap<>();
        temp.put(invest1.getTicker(),invest1);
        temp.put(invest2.getTicker(),invest2);
        temp.put(invest3.getTicker(),invest3);
        temp.put(invest4.getTicker(),invest4);
        temp.put(invest5.getTicker(),invest5);
        temp.put(invest6.getTicker(),invest6);
        temp.put(invest7.getTicker(),invest7);
        temp.put(invest8.getTicker(),invest8);
        temp.put(invest9.getTicker(),invest9);
        temp.put(invest10.getTicker(),invest10);

        stockMap.setValue(temp);
        toJson();
    }

    public void addInvestment(Investment investment) {

        HashMap<String, Investment> temp = stockMap.getValue();
        if (temp != null) {
            temp.put(investment.getTicker(), investment);
        }
        stockMap.setValue(temp);
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

        Log.d("JSON", new Gson().toJson(stockMap.getValue()));
    }

}
