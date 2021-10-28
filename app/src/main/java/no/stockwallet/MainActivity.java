package no.stockwallet;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.crazzyghost.alphavantage.*;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth auth;
    private StockViewModel viewModel;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );

    public static HashMap<String, Investment> investments = new HashMap<String, Investment>();
    public void AlphaVantageInit() {
        Config cfg = Config.builder()
                .key("04C7U8DGXKH0OY8B")
                .timeOut(5)
                .build();

        AlphaVantage.api().init(cfg);

    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        user = auth.getCurrentUser();
    }

    private void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();

        signInLauncher.launch(signInIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(user == null)
            createSignInIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        NavController navController = Navigation.findNavController(this, R.id.NavHost);
        NavigationView navView = findViewById(R.id.NavigationViewMain);
        NavigationUI.setupWithNavController(navView, navController);

        viewModel = new ViewModelProvider(this).get(StockViewModel.class);
        viewModel.fillWithDummyData();

        findViewById(R.id.NavMenuButton).setOnClickListener((view) -> {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.openDrawer(Gravity.LEFT);
        });

    }

    public void setToolbarTitle(String title){
        TextView navTitle = (TextView) findViewById (R.id.NavBar_Title);
        navTitle.setText(title);
    }

    public void checkIfAlreadyInvestedIn(String key){

        if (investments.containsKey(key)){
            investments.get(key);
        }
    }


    public double currencyConverter(Investment foreignCurrencyInvestment) {
        ValueSetterSupport valueSetterSupport = new ValueSetterSupport();

        Thread thread = new Thread(() -> {

            AlphaVantage.api()
                    .exchangeRate()
                    .fromCurrency(foreignCurrencyInvestment.currency)
                    .toCurrency("NOK")
                    .onSuccess(e -> {
                        valueSetterSupport.setReturnValue((foreignCurrencyInvestment.getPrice() * e.getExchangeRate()));
                    })
                    .onFailure(Throwable::printStackTrace)
                    .fetch();
        });

        thread.start();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return valueSetterSupport.getReturnValue();

    }

    public double getTotalInvestments(){
        double totSumInvested = 0;
        for (Investment x: investments.values()){
            if (!x.currency.equals("NOK")){
                totSumInvested += (currencyConverter(x) * x.volum);
            }
            else {
                totSumInvested += x.price * x.volum;            //TODO: må endres tilå hente nå pris fremfor kjøpt-pris
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