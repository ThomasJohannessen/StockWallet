package no.stockwallet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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

public class RegisterUserActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        auth = FirebaseAuth.getInstance();

        Button registerButton = findViewById(R.id.registerUserButton);
        registerButton.setOnClickListener(view -> registerUser(view));
    }

    private void registerUser(View view) {
        Thread thread = new Thread(() -> {
            EditText passwordInput = findViewById(R.id.passwordInput);
            EditText emailInput = findViewById(R.id.emailInput);

            Log.d("Heyoheyo", "Inside registerUser");

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
        EditText lastNameField = findViewById(R.id.firstNameInput);

        HashMap<String, String> credMap = new HashMap<>();
        credMap.put("firstName", );

        db.collection("StockWallet").document(user.getUid()).set();
    }
}