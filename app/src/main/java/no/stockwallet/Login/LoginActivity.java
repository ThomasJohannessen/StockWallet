package no.stockwallet.Login;

import androidx.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import no.stockwallet.MainActivity;
import no.stockwallet.R;

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

        auth = FirebaseAuth.getInstance();
    }

    private void setUpButtonListeners() {
        Intent signInIntent = googleClient.getSignInIntent();

        Button logInButton = findViewById(R.id.buttonLogIn);
        logInButton.setOnClickListener(view -> handleSignInAttempt(view));

        SignInButton logInGoogleButton = findViewById(R.id.buttonSignGoogle);
        logInGoogleButton.setSize(SignInButton.SIZE_WIDE);
        logInGoogleButton.setOnClickListener(view -> startActivityForResult(signInIntent, GOOGLE_CODE));

        Button registerButton = findViewById(R.id.buttonRegisterEmail);
        registerButton.setOnClickListener(view -> handleRegisterNewUserClick());
    }

    public void handleRegisterNewUserClick() {
        Intent toRegisterScreen = new Intent(this, RegisterUserActivity.class);
        startActivity(toRegisterScreen);
    }

    private void handleSignInAttempt(View view) {
        Thread thread = new Thread(() -> {
            EditText usernameField = findViewById(R.id.inputUsername);
            EditText passwordField = findViewById(R.id.inputPassword);

            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            try {
                auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        hideKeyboard();
                        changeActivity();
                    }
                    else {
                        Log.d("loginAttempt", "No success");
                        Snackbar.make(view, "Given credentials are wrong", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
            catch(IllegalArgumentException e) {
                Snackbar.make(view, "Input fields cannot be empty", Snackbar.LENGTH_SHORT).show();
            }
        });

        thread.start();
    }

    private void setUpGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Thread thread = new Thread(() -> {
            if (requestCode == GOOGLE_CODE) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    Log.w("TEST", "Google sign in failed", e);
                }
            }
        });

        thread.start();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TEST", "AUTH COMPLETED");
                            hideKeyboard();
                            checkIfFirstTimeSignIn(account.getGivenName(), account.getFamilyName());
                        } else {
                            Log.w("TEST", "Google sign in failed");
                        }
                    }
                });
    }

    private void checkIfFirstTimeSignIn(String firstname, String lastname) {
        FirebaseUser user = auth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("StockWallet").document(user.getUid());

        docRef.get().addOnCompleteListener(task -> {
           if(task.getResult().exists() == false) {
               Log.d("TEST", "USER CREDS SAVING");
               HashMap<String, String> userCred = new HashMap<>();
               userCred.put("firstName", firstname);
               userCred.put("lastName", lastname);
               docRef.set(userCred);
           }
            changeActivity();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}