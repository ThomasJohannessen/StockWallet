package no.stockwallet;

import androidx.appcompat.app.AppCompatActivity;
import yahoofinance.*;
import android.os.Bundle;
import android.util.Log;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;


import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AlphaVantageInit();
        Thread thread = new Thread(() -> Yahoo());

        thread.start();

    }

    public void AlphaVantageInit() {
        Config cfg = Config.builder()
                .key("04C7U8DGXKH0OY8B")
                .timeOut(10)
                .build();

        AlphaVantage.api().init(cfg);
    }

    public void Yahoo() {


        try {
            Stock aksje = YahooFinance.get("NHyY.OL");
            Log.d("Yahoo", aksje.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}