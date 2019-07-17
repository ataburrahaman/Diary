package com.dreamworld.smart.diary;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignActivity extends AppCompatActivity {

    private SignInButton signIn;
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=1;
    private String TAG ="GoogleSignActivity";
    private FirebaseAuth mAuth;
    Shared sharePref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharePref=new Shared(this);

        if(sharePref.lodeNightModeState()==true)
        {
            setTheme(R.style.darktheme);
        }else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign);

         signIn = findViewById(R.id.sign_in_button);
         //signIn.setSize(SignInButton.SIZE_STANDARD);


        mAuth = FirebaseAuth.getInstance();


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    signIn();
            }
        });



    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           Toast.makeText(getApplicationContext(),"signInWithCredential:success",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(GoogleSignActivity.this,HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                           Toast.makeText(getApplicationContext(),"signInWithCredential:failure",Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),"Authentication Failed.",Toast.LENGTH_SHORT).show();

                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
       /* GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!= null){
            startActivity(new Intent(GoogleSignActivity.this,HomeActivity.class));
        }
        mAuth = FirebaseAuth.getInstance();
        */

        if(mAuth.getCurrentUser()!= null) {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }


        super.onStart();
    }
}
