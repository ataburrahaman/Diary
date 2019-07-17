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

import com.google.android.gms.flags.Flag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity {

   private TextView login;
   private EditText email;
   private EditText password;
   private Button register;
   private FirebaseAuth mAuth;
   private ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        mdialog = new ProgressDialog(this);

        email=(EditText)findViewById(R.id.email_reg);
        password=(EditText)findViewById(R.id.password_reg);
        register=(Button)findViewById(R.id.reg_btn);

        login = (TextView)findViewById(R.id.login_txt);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String memail=email.getText().toString().trim();
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
                mdialog.setMessage("Processing..");
                mdialog.show();

                mAuth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Register Successful",Toast.LENGTH_SHORT).show();
                            mdialog.dismiss();
                            Shared shared=new Shared(RegisterActivity.this);
                            shared.setEmail(memail);
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();

                            return;
                        }
                        else {
                            mdialog.dismiss();
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(),"You are already Register",Toast.LENGTH_SHORT).show();
                               return;
                            }
                            else {
                                Toast.makeText(getApplicationContext(), " Please Try again....", Toast.LENGTH_SHORT).show();
                               return;
                            }
                        }
                    }
                });

            }
        });

    }
}
