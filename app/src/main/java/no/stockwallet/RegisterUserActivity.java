package no.stockwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
            EditText passwordInput = findViewById(R.id.inputPassword);
            EditText emailInput = findViewById(R.id.emailInput);

            String password = passwordInput.getText().toString();
            String email = emailInput.getText().toString();

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getParent(), task -> {
                if(task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    appendUserCredentials(user);
                    runOnUiThread(() -> {
                        Intent toMainActivity = new Intent(getApplicationContext(), MainActivity.class);
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
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("test");
        ref.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Log.d("Heyoheyo", String.valueOf(task.getResult().getValue()));
            }
            else {
                Log.d("Heyoheyo", "Failed");
            }
        });
    }

}