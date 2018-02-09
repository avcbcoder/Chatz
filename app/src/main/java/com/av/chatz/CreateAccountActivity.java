package com.av.chatz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {


    private TextInputLayout tuser, tmail, tpass;
    private Button btnCreate;
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    private ProgressDialog mprogress;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        dbref = FirebaseDatabase.getInstance().getReference().child("Users");
        mprogress = new ProgressDialog(this);
        mtoolbar = findViewById(R.id.caa_toolbar);
        mAuth = FirebaseAuth.getInstance();
        tuser = findViewById(R.id.caa_username);
        tmail = findViewById(R.id.caa_mail);
        tpass = findViewById(R.id.caa_pass);
        btnCreate = findViewById(R.id.caa_create);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = tuser.getEditText().getText().toString();
                String mail = tmail.getEditText().getText().toString();
                String pass = tpass.getEditText().getText().toString();
                if (username.length() >= 5 && mail.length() >= 3 && pass.length() >= 5) {
                    mprogress.setTitle("Registering");
                    mprogress.setMessage("wait processing request");
                    mprogress.setCanceledOnTouchOutside(false);
                    mprogress.show();
                    registerUser(username, mail, pass);
                }
            }
        });
    }

    private void registerUser(final String username, final String mail, String pass) {
        Toast.makeText(this, username + " " + mail + " " + pass, Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser curr=FirebaseAuth.getInstance().getCurrentUser();
                            String uid=curr.getUid();
//                            String id = dbref.push().getKey();
                            User user = new User(uid + "", mail + "", username, "Hey There", "link");
                            dbref.child(uid).setValue(user);
                            Intent i = new Intent(CreateAccountActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                            mprogress.dismiss();
                        } else {
                            mprogress.hide();
                            Log.v("--------------", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
