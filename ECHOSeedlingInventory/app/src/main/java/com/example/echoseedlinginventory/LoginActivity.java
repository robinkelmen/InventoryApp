package com.example.echoseedlinginventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {


    public static GoogleSignInClient mGoogleSignInClient;
    SignInButton mSignButt;
    private LayoutInflater layoutInflater;
    private View failedLogin;
    private TextView failedMsg;
    public static GoogleSignInAccount signedIn;

    int RC_SIGN_IN = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mSignButt = findViewById(R.id.sign_in_button);

        mSignButt.setSize(SignInButton.SIZE_STANDARD);

        mSignButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });

        layoutInflater = this.getLayoutInflater();
        failedLogin = layoutInflater.inflate(R.layout.failed_login, null);
        failedMsg = failedLogin.findViewById(R.id.Login_failed_text);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(myIntent);
        }

    }

    // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.

    //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    //updateUI(account);
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String userEmail = account.getEmail();
            String exOne = "^[A-Z0-9+_.-]+@echonet.org$";
            String exTwo = "^[A-Z0-9+_.-]+@echoccommunity.org$";

            Pattern one = Pattern.compile(exOne, Pattern.CASE_INSENSITIVE);
            Pattern two = Pattern.compile(exTwo, Pattern.CASE_INSENSITIVE);
            Matcher mOne = one.matcher(userEmail);
            Matcher mTwo = two.matcher(userEmail);

            if(mOne.matches() || mTwo.matches()){
                signedIn = account;
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(myIntent);
                Toast.makeText(this, "Welcome " + account.getGivenName()+ "!", Toast.LENGTH_LONG).show();
            }else{

                failedMsg.setText("Email Not Part of ECHO Organization");
                showDialog(failedLogin).show();
                signOut();
                revokeAccess();

            }

            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Login Failed: ", "signInResult:failed code=" + e.getStatusCode());
            failedMsg.setText("Log in Failed: Please Try Again");
            showDialog(failedLogin).show();
            //updateUI(null);

        }
    }

    public Dialog showDialog(View toInflate){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        //LayoutInflater inflater = ListFragment.myInflater;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        if(toInflate.getParent() != null){
            ((ViewGroup)toInflate.getParent()).removeView(toInflate);
        }
        builder.setView(toInflate)

                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
        return builder.create();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent myIntent = getIntent();
                        startActivity(myIntent);
                    }
                });
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

}


