package com.av.chatz;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingActivity extends AppCompatActivity {
    private Button change_status, change_image;
    private CircleImageView profile_image;
    private RelativeLayout prof_bkg;
    private DatabaseReference dbrefUser;
    private FirebaseUser currUser;
    TextView tvUser, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currUser.getUid();
        dbrefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        tvStatus = findViewById(R.id.prof_username);
        tvUser = findViewById(R.id.prof_status);
        change_image = findViewById(R.id.prof_btn_image);
        change_status = findViewById(R.id.prof_btn_status);
        profile_image = findViewById(R.id.prof_image);
        prof_bkg = findViewById(R.id.prof_bkg);

        dbrefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                tvUser.setText(u.username);
                tvStatus.setText(u.status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.temp_image_zoom);
                ImageView img = findViewById(R.id.temp_image);
                img.setImageResource(R.drawable.profiledef);
                Animation zoom = AnimationUtils.loadAnimation(AccountSettingActivity.this, R.anim.zoom);
                img.startAnimation(zoom);
            }
        });

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(AccountSettingActivity.this);
        final View mdialogview = getLayoutInflater().inflate(R.layout.input_dialog, null);
        mbuilder.setView(mdialogview);
        final AlertDialog dialog = mbuilder.create();
        Button mdone = mdialogview.findViewById(R.id.btnInputDone);

        mdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText text = mdialogview.findViewById(R.id.input_text);
                Toast.makeText(AccountSettingActivity.this, text.getText(), Toast.LENGTH_SHORT).show();
                if (text.length() != 0) {
                    dbrefUser.child("status").setValue(text.getText() + "");
                }
            }
        });

        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AccountSettingActivity.this, "Not completed yet", Toast.LENGTH_SHORT).show();
            }
        });

        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


    }


}
