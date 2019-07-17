package com.dreamworld.smart.diary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


   private TextView register;
   private EditText email;
   private EditText password;
   private Button login;

    private FirebaseAuth logAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logAuth = FirebaseAuth.getInstance();

        if(logAuth.getCurrentUser()!= null) {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }
        progressDialog = new ProgressDialog(LoginActivity.this);

        email=(EditText)findViewById(R.id.email_login);
        password=(EditText)findViewById(R.id.password_login);
        login=(Button)findViewById(R.id.login_btn);

        register = (TextView)findViewById(R.id.singup_txt);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String memail = email.getText().toString().trim();
                String mpassword=password.getText().toString().trim();

                if(TextUtils.isEmpty(memail)){
                    email.setError("Email Required*");
                    Toast.makeText(getApplicationContext(),"Enter Email Address",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(mpassword)){
                    password.setError("Password Required*");
                    Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_LONG).show();
                    return;
                }

                progressDialog.setMessage("Processing..");
                progressDialog.show();

                logAuth.signInWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Shared shared=new Shared(LoginActivity.this);
                            shared.setEmail(memail);
                            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            return;
                        }else {
                            Toast.makeText(getApplicationContext(),"Problem",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }

                    }
                });

            }
        });

    }
}
