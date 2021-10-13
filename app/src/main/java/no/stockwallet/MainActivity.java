package no.stockwallet;

import androidx.appcompat.app.AppCompatActivity;
import yahoofinance.*;
import android.os.Bundle;
import android.util.Log;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.Interval;
import com.crazzyghost.alphavantage.parameters.OutputSize;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlphaVan();
    }
public void AlphaVan() {
    Config cfg = Config.builder()
            .key("04C7U8DGXKH0OY8B")
            .timeOut(10)
            .build();


}

}