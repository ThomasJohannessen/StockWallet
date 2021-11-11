package no.stockwallet;

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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.HashMap;

import no.stockwallet.Login.LoginActivity;
import no.stockwallet.Model.Investment;
import no.stockwallet.ViewModels.StockViewModel;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private StockViewModel viewModel;

    public static HashMap<String, Investment> investments = new HashMap<String, Investment>();
    public void AlphaVantageInit() {
        Config cfg = Config.builder()
                .key("04C7U8DGXKH0OY8B")
                .timeOut(5)
                .build();

        AlphaVantage.api().init(cfg);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
        if(user == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        setUpViewModel();

        if(user == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        setContentView(R.layout.activity_main);

        NavController navController = Navigation.findNavController(this, R.id.NavHost);
        NavigationView navView = findViewById(R.id.NavigationViewMain);
        NavigationUI.setupWithNavController(navView, navController);

        //viewModel = new ViewModelProvider(this).get(StockViewModel.class)
        findViewById(R.id.NavMenuButton).setOnClickListener((view) -> {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.openDrawer(Gravity.LEFT);
        });
    }

    public void setToolbarTitle(String title) {
        TextView navTitle = (TextView) findViewById(R.id.NavBar_Title);
        navTitle.setText(title);
    }

    public void setUpViewModel() {
        viewModel = new ViewModelProvider(this).get(StockViewModel.class);
        //viewModel.fetchUserData();
    }

}