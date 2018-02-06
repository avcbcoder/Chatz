package com.av.chatz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tuser, tpass;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mprogress = new ProgressDialog(this);
        mtoolbar = findViewById(R.id.log_toolbar);
        mAuth = FirebaseAuth.getInstance();
        tuser = findViewById(R.id.log_username);
        tpass = findViewById(R.id.log_pass);
        btnLogin = findViewById(R.id.log_Login);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("LOGIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mprogress = new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = tuser.getEditText().getText().toString();
                String pass = tpass.getEditText().getText().toString();
                if (user.length() >= 5 && pass.length() >= 5) {
                    mprogress.setTitle("Logging in");
                    mprogress.setMessage("Please wait");
                    mprogress.setCanceledOnTouchOutside(false);
                    mprogress.show();
                    signIn(user,pass);
                } else {
                    Toast.makeText(LoginActivity.this, "Check Lengths", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn(String user, String pass) {
    mAuth.signInWithEmailAndPassword(user,pass)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mprogress.dismiss();
                        Intent i=new Intent(LoginActivity.this,MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }else{
                        mprogress.hide();
                        Toast.makeText(LoginActivity.this, "Error Logging In", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
