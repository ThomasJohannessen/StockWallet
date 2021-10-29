package no.stockwallet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient googleClient;
    private FirebaseAuth auth;
    private final int GOOGLE_CODE = 555;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        setUpGoogleClient();

        Intent signInIntent = googleClient.getSignInIntent();


        Button logInButton = findViewById(R.id.buttonLogIn);
        logInButton.setOnClickListener(view -> handleSignInAttempt(view));

        SignInButton logInGoogleButton = findViewById(R.id.buttonSignGoogle);
        logInGoogleButton.setSize(SignInButton.SIZE_WIDE);
        logInGoogleButton.setOnClickListener(view -> startActivityForResult(signInIntent, GOOGLE_CODE));
    }

    private void handleSignInAttempt(View view) {
        EditText usernameField = findViewById(R.id.inputUsername);
        EditText passwordField = findViewById(R.id.inputPassword);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                hideKeyboard();

                Intent toMainActivity = new Intent(this, MainActivity.class);
                startActivity(toMainActivity);
            }
            else {
                Log.d("loginAttempt", "No success");
                Snackbar.make(view, "Given credentials are wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);
    }

    private void handleGoogleSignIn(String idToken) {
        Log.d("googlesignin", "Test");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                hideKeyboard();

                Intent toMainActivity = new Intent(this, MainActivity.class);
                startActivity(toMainActivity);
            }
            else {
                Log.d("loginAttempt", "No success");
                Snackbar.make(getWindow().getDecorView().getRootView(), "Given credentials are wrong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TEST", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TEST", "AUTH COMPLETED");
                            hideKeyboard();
                            changeActivity();
                        } else {
                            Log.w("TEST", "Google sign in failed");
                        }
                    }
                });
    }
    private void changeActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}






































