package com.av.chatz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    private Button start_reg_btn,start_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        start_reg_btn = findViewById(R.id.start_reg_btn);
        start_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, CreateAccountActivity.class);
                startActivity(i);
            }
        });
        start_log=findViewById(R.id.start_log_btn);
        start_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
