package no.stockwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;


import com.crazzyghost.alphavantage.*;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import no.stockwallet.Handlers.API_InvestmentDataHandler;
import no.stockwallet.Login.LoginActivity;
import no.stockwallet.Model.Investment;
import no.stockwallet.Service.UpdateInvestmentsWorker;
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
    protected void onResume() {
        super.onResume();


    }

    private void setUpNetworkCallback() {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(Network network) {
                createDialog();
            }
        };
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Test", "Inside onstart");
        Log.d("Test", "Onstartvalue " + isNetworkAvailable());
        if(isNetworkAvailable() != true) {
            createDialog();
        }
        else {
            setUpNetworkCallback();

            AlphaVantageInit();
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            if (user == null) {
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test", "Inside oncreate");
        if(isNetworkAvailable() != true) {
            createDialog();
        }
        else {
            setContentView(R.layout.activity_main);
            PeriodicWorkRequest updateRequest =
                    new PeriodicWorkRequest.Builder(UpdateInvestmentsWorker.class, 15, TimeUnit.MINUTES).build();
            WorkManager.getInstance().enqueue(updateRequest);

            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            setUpViewModel();

            if (user == null) {
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
            }

            NavController navController = Navigation.findNavController(this, R.id.NavHost);
            NavigationView navView = findViewById(R.id.NavigationViewMain);
            NavigationUI.setupWithNavController(navView, navController);

            findViewById(R.id.NavMenuButton).setOnClickListener((view) -> {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.LEFT);
            });
        }

    }

    public void setToolbarTitle(String title) {
        TextView navTitle = (TextView) findViewById(R.id.NavBar_Title);
        navTitle.setText(title);
    }

    public void setUpViewModel() {
        viewModel = new ViewModelProvider(this).get(StockViewModel.class);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }


    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(false);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                System.exit(0);
            }
        })
            .setTitle("INTERNET CONNECTION REQUIRED")
            .setMessage("Internet connection is required for StockWallet to function properly" +
                    "the application will now shut down.");
        builder.show();
    }

}