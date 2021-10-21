package no.stockwallet;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class StockViewModel extends ViewModel {

    private MutableLiveData<HashMap<String, Investment>> stockMap;

    public void fillWithDummyData() {
        Investment invest1 = new Investment("NHY.OL",200,60,"NOK",1);
        Investment invest2 = new Investment("AKH.OL",100,30,"NOK",10);
        Investment invest3= new Investment("NOD.OL",10,250,"NOK",100);
        Investment invest4 = new Investment("ITERA.OL",1000,8.20,"NOK",1.05);
        Investment invest5 = new Investment("MPCC.OL",65,20,"NOK",35);
        Investment invest6 = new Investment("KAHOT.OL",100,60,"NOK",5);
        Investment invest7 = new Investment("FLYR.OL",50,2.58,"NOK",3);
        Investment invest8 = new Investment("LCID",100,20,"USD",9);
        Investment invest9 = new Investment("MSFT",1000,200,"USD",3);
        Investment invest10 = new Investment("AKSO.OL",200,12,"NOK",50);

        HashMap<String, Investment> temp = new HashMap<>();
        temp.put(invest1.ticker,invest1);
        temp.put(invest2.ticker,invest2);
        temp.put(invest3.ticker,invest3);
        temp.put(invest4.ticker,invest4);
        temp.put(invest5.ticker,invest5);
        temp.put(invest6.ticker,invest6);
        temp.put(invest7.ticker,invest7);
        temp.put(invest8.ticker,invest8);
        temp.put(invest9.ticker,invest9);
        temp.put(invest10.ticker,invest10);

        stockMap.setValue(temp);
    }

    public void addInvestment(String ticker, int volume, float price, String currency, float brokerage) {
        Investment newInvestment = new Investment(ticker, volume, price, currency, brokerage);
        HashMap<String, Investment> temp = stockMap.getValue();
        if (temp != null) {
            temp.put(ticker, newInvestment);
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

    public void updateUserPortfolio() {}

    public void fetchUserPortfolio() {}

    public MutableLiveData<HashMap<String, Investment>> getStockMap() {
        if(stockMap == null)
            stockMap = new MutableLiveData<HashMap<String, Investment>>();
        return stockMap;
    }
}
