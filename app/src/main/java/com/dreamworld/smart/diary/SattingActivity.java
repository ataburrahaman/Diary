package com.dreamworld.smart.diary;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class SattingActivity extends AppCompatActivity {



    private  Toolbar toolbar;
    private TextView logout;
    private TextView account;
    private CircleImageView setingProfile;
    private TextView needHelp;

    private FirebaseAuth mauth;
    private TextView fontsize;
    private SeekBar seekBar;
    int seekValue;
    private TextView seekvalue;
    private GoogleSignInClient mGoogleSignInClient;
    Shared sharePref;
    private Switch myswitch;
    private RelativeLayout accountshow;
    private LinearLayout needhelp;
    private TextView reminder;

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
        setContentView(R.layout.activity_satting);

        toolbar=(Toolbar)findViewById(R.id.toolbar_setting);
        account=findViewById(R.id.edit_account);
        setingProfile=findViewById(R.id.edit_profilepicture);
        needHelp=findViewById(R.id.edit_needhelp);
        needhelp=findViewById(R.id.needhelp);
        reminder=findViewById(R.id.edit_reminder);

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SattingActivity.this,ReminderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        accountshow=findViewById(R.id.accountShow);

        accountshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SattingActivity.this,ProfileActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");

        myswitch=findViewById(R.id.edit_switch);
        if (sharePref.lodeNightModeState()==true){
            myswitch.setChecked(true);
        }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sharePref.setNightModeState(true);
                    restartApp();
                }else {
                    sharePref.setNightModeState(false);
                    restartApp();
                }
            }

        });

        needhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.i("Send email", "");
                String[] TO = {
                        "dreammworld700@gmail.com"
                };
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Diary World Feedback v"+ BuildConfig.VERSION_NAME);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Write Here: \n\n\n" +"______________"+"\n\nPlease leave this data in " +
                        "email to help with app issues and thanks for Feedback."+"\n\n"+
                        "Device info:\n\n"+
                        "Manufacture:  "+Build.MANUFACTURER+
                        "\nBrand:  "+Build.BRAND+
                        "\nMODEL:  "+Build.MODEL+
                        "\nVersion:  "+ Build.VERSION.RELEASE+
                        "\nSDK:  "+Build.VERSION.SDK+
                        "\nFINGERPRINT: "+Build.FINGERPRINT+
                        "\nINCREMENTAL: "+Build.VERSION.INCREMENTAL+
                        "\nBootloader: "+Build.BOOTLOADER+
                        "\n Screen Dencity: "+Build.DISPLAY+
                        "\nUser Id: "+mauth.getUid()+"\n\n"+
                        "________________"

                );
                try {

                    startActivity(emailIntent);
                    //startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    // Toast.makeText(getApplicationContext(),"Finished sending email",Toast.LENGTH_SHORT).show();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

       /* needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Send email", "");
                String[] TO = {
                        "dreammworld700@gmail.com"
                };
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Diary World Feedback v"+ BuildConfig.VERSION_NAME);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Write Here: \n\n\n" +"______________"+"\n\nPlease leave this data in " +
                                "email to help with app issues and thanks for Feedback."+"\n\n"+
                        "Device info:\n\n"+
                        "Manufacture:  "+Build.MANUFACTURER+
                        "\nBrand:  "+Build.BRAND+
                        "\nMODEL:  "+Build.MODEL+
                        "\nVersion:  "+ Build.VERSION.RELEASE+
                        "\nSDK:  "+Build.VERSION.SDK+
                        "\nFINGERPRINT: "+Build.FINGERPRINT+
                        "\nINCREMENTAL: "+Build.VERSION.INCREMENTAL+
                        "\nBootloader: "+Build.BOOTLOADER+
                        "\n Screen Dencity: "+Build.DISPLAY+
                        "\nUser Id: "+mauth.getUid()+"\n\n"+
                                "________________"

                );
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    // Toast.makeText(getApplicationContext(),"Finished sending email",Toast.LENGTH_SHORT).show();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


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
            account.setText(personName);
            Glide.with(this).load(acct.getPhotoUrl()).into(setingProfile);

        }
       /* account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(SattingActivity.this,ProfileActivity.class));
               finish();
            }
        });*/



       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mauth=FirebaseAuth.getInstance();
        logout=findViewById(R.id.edit_logout);
        fontsize=findViewById(R.id.edit_fontsize);

        fontsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myDialog= new AlertDialog.Builder(SattingActivity.this);
                LayoutInflater inflater = LayoutInflater.from(SattingActivity.this);

                Toast.makeText(SattingActivity.this, "Inside FontSize", Toast.LENGTH_SHORT).show();

                View myview = inflater.inflate(R.layout.changefontsize,null);
                myDialog.setView(myview);
                final AlertDialog dialog=myDialog.create();


                seekBar=myview.findViewById(R.id.seekbar);
                seekvalue=myview.findViewById(R.id.valueSeek);


                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekValue=progress;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                        seekvalue.setText("hello");

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        fontsize.setTextSize(seekValue);
                    }
                });

                dialog.show();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
               /* mauth.signOut();

                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();*/
            }
        });


    }

    private void restartApp() {
        Intent i = new Intent(getApplicationContext(),SattingActivity.class);
        startActivity(i);
        finish();
    }

    private void signOut() {
        mauth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        startActivity(new Intent(getApplicationContext(),GoogleSignActivity.class));
                        finish();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        return true;
    }
}
