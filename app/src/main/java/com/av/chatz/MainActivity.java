package com.av.chatz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //firebase authentication Instance
        mAuth = FirebaseAuth.getInstance();
        //Toolbar
        toolbar = findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CHATZ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.ma_menu, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_logout) {
        FirebaseAuth.getInstance().signOut();
        updateUI();
        }
        return true;
    }

    public void updateUI(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intentStartActivity = new Intent(this, StartActivity.class);
            startActivity(intentStartActivity);
        } else {
            Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
