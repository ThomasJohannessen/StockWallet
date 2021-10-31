package no.stockwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    // TODO Recycler view i søk - oppdatert dynamisk fra api
    // TODO ViewModel for hele main activity som skal ha ansvar for å kommunisere med api og oppdatere data "realtime"
    public static HashMap<String, Investment> investments = new HashMap<String, Investment>();
    private StockViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void setToolbarTitle(String title) {
        TextView navTitle = (TextView) findViewById(R.id.NavBar_Title);
        navTitle.setText(title);
    }
}
