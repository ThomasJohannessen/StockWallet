package no.stockwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        Button logInButton = findViewById(R.id.buttonLogIn);
        logInButton.setOnClickListener(view -> handleSignInAttempt());
    }

    private void handleSignInAttempt() {
        EditText usernameField = findViewById(R.id.inputUsername);
        EditText passwordField = findViewById(R.id.inputPassword);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Intent toMainActivity = new Intent(this, MainActivity.class);
                startActivity(toMainActivity);
            }
            else {
                Log.d("loginAttempt", "No success");
            }
        });


    }
}