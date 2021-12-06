package no.stockwallet.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import no.stockwallet.MainActivity;
import no.stockwallet.R;

public class RegisterUserActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        auth = FirebaseAuth.getInstance();
        setUpButtonListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isNetworkAvailable() != true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    moveTaskToBack(true);
                    finish();
                    System.exit(0);
                }
            })
            .setTitle("INTERNET CONNECTION REQUIRED")
            .setMessage("Internet connection is required for StockWallet to function properly" +
                    "the application will now shut down.");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void setUpButtonListeners() {
        Button registerButton = findViewById(R.id.registerUserButton);
        registerButton.setOnClickListener(view -> registerUser(view));
    }

    private void registerUser(View view) {
        Thread thread = new Thread(() -> {
            EditText passwordInput = findViewById(R.id.passwordInput);
            EditText emailInput = findViewById(R.id.emailInput);

            String password = passwordInput.getText().toString();
            String email = emailInput.getText().toString();


            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    appendUserCredentials(user);
                    runOnUiThread(() -> {
                        Intent toMainActivity = new Intent(this, MainActivity.class);
                        startActivity(toMainActivity);
                    });
                }
                else {
                    Snackbar snack = Snackbar.make(view, "Something went wrong, check your input fields or internet connection", Snackbar.LENGTH_SHORT);
                    snack.show();
                }
            });
        });
        thread.start();
    }

    private void appendUserCredentials(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText firstNameField = findViewById(R.id.firstNameInput);
        EditText lastNameField = findViewById(R.id.lastNameINput);

        HashMap<String, String> credMap = new HashMap<>();
        credMap.put("firstName", firstNameField.getText().toString());
        credMap.put("lastName", lastNameField.getText().toString());

        db.collection("StockWallet").document(user.getUid()).set(credMap);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}