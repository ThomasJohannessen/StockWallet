package no.stockwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.crazzyghost.alphavantage.*;
import com.crazzyghost.alphavantage.exchangerate.ExchangeRateResponse;
import com.google.android.material.navigation.NavigationView;


import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static HashMap<String, Investment> investments = new HashMap<String, Investment>();

    public void AlphaVantageInit() {
        Config cfg = Config.builder()
                .key("04C7U8DGXKH0OY8B")
                .timeOut(5)
                .build();

        AlphaVantage.api().init(cfg);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlphaVantageInit();

        NavController navController = Navigation.findNavController(this, R.id.NavHost);
        NavigationView navView = findViewById(R.id.NavigationViewMain);
        NavigationUI.setupWithNavController(navView, navController);



        addDummyInvestments(investments);
        //getTotalInvestments();
        Log.d("KALL:",currencyConverter(investments.get("LCID")));



    }


    public void addDummyInvestments(HashMap<String, Investment> list){
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

        list.put(invest1.ticker,invest1);
        list.put(invest2.ticker,invest2);
        list.put(invest3.ticker,invest3);
        list.put(invest4.ticker,invest4);
        list.put(invest5.ticker,invest5);
        list.put(invest6.ticker,invest6);
        list.put(invest7.ticker,invest7);
        list.put(invest8.ticker,invest8);
        list.put(invest9.ticker,invest9);
        list.put(invest10.ticker,invest10);
    }   //adds dummy investments for testing for now

    public void checkIfAlreadyInvestedIn(String key){

        if (investments.containsKey(key)){
            investments.get(key);
        }
    }

    public void ApiResponsMethode(ExchangeRateResponse response){
        Log.d("NOK", String.valueOf(response.getExchangeRate()));
        Log.d("NOK", String.valueOf(response.getBidPrice()));
        Log.d("NOK", String.valueOf(response.getAskPrice()));
        Log.d("NOK",response.getFromCurrencyCode());
        Log.d("NOK",response.getFromCurrencyName());
        Log.d("NOK",response.getToCurrencyCode());
        Log.d("NOK",response.getToCurrencyName());
        Log.d("NOK",response.getLastRefreshed());
        Log.d("NOK",response.getTimeZone());
    }

    public String currencyConverter(Investment foreignCurrencyInvestment) {
        StringBuilder sb = new StringBuilder();
        String result = sb.toString();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("RESULTAT-VAluta",foreignCurrencyInvestment.currency);

                AlphaVantage.api()
                        .exchangeRate()
                        .fromCurrency(foreignCurrencyInvestment.currency)
                        .toCurrency("NOK")
                        .onSuccess(e -> sb.append(e.getExchangeRate()))
                        .onFailure(e -> Log.d("RESULTAT-feil",e.getMessage()))
                        .fetch();

                Log.d("RESULTAT",result);

            }
        });
        thread.start();

        return result;
    }



    public double getTotalInvestments(){
        double totSumInvested = 0;
        for (Investment x: investments.values()){
            if (!x.currency.equals("NOK")){
                //totSumInvested += currencyConverter(x);
                currencyConverter(x);
            }
            else {
                totSumInvested += x.price;
            }
        }
        return totSumInvested;
    }





/*

    public void Yahoo() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Stock aksje = YahooFinance.get("NHY.OL");
                    //Log.d("Yahoo", aksje.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        }*/
}