package com.dreamworld.smart.diary;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView uname;
    private TextView uemail;
    private TextView uid;
    private Button logout;
   // private ImageView profileImage;
    private FirebaseAuth mauth;
    private GoogleSignInClient mGoogleSignInClient;
    private Toolbar toolbar;
    private CircleImageView profileImage;
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
        setContentView(R.layout.activity_profile);


        toolbar=(Toolbar)findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uname=findViewById(R.id.edit_uname);
        uemail=findViewById(R.id.edit_uemail);
        uid=findViewById(R.id.edit_uid);
        logout=findViewById(R.id.edit_ulogout);
        profileImage=findViewById(R.id.profiilephotoimage);
        mauth=FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            uname.setText(personName);
            uemail.setText(personEmail);
            uid.setText("Id: "+personId);

            Glide.with(this).load(acct.getPhotoUrl()).into(profileImage);

        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        mauth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        startActivity(new Intent(ProfileActivity.this,GoogleSignActivity.class));
                        finish();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(),SattingActivity.class));
        return true;
    }
}
