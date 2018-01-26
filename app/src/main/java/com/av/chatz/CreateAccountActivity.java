package com.av.chatz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {


    private TextInputLayout tuser, tmail, tpass;
    private Button btnCreate;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
        tuser = findViewById(R.id.caa_username);
        tmail = findViewById(R.id.caa_mail);
        tpass = findViewById(R.id.caa_pass);
        btnCreate = findViewById(R.id.caa_create);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = tuser.getEditText().getText().toString();
                String mail = tmail.getEditText().getText().toString();
                String pass = tpass.getEditText().getText().toString();
                registerUser(username, mail, pass);
            }
        });
    }

    private void registerUser(String username, String mail, String pass) {
        Toast.makeText(this, username+" "+mail+" "+pass, Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            Intent i = new Intent(CreateAccountActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.v("--------------", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

}
