package no.stockwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import yahoofinance.*;
import android.os.Bundle;
import android.util.Log;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.google.android.material.navigation.NavigationView;


import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavController navController = Navigation.findNavController(this, R.id.NavHost);
        NavigationView navView = findViewById(R.id.NavigationViewMain);
        NavigationUI.setupWithNavController(navView, navController);
    }



/*
    public void AlphaVantageInit() {
        Config cfg = Config.builder()
                .key("04C7U8DGXKH0OY8B")
                .timeOut(10)
                .build();

        AlphaVantage.api().init(cfg);
    }

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