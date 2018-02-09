package com.av.chatz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingActivity extends AppCompatActivity {
    private Button change_status, change_image;
    private CircleImageView profile_image;
    private RelativeLayout prof_bkg;
    private DatabaseReference dbrefUser;
    private FirebaseUser currUser;
    TextView tvUser, tvStatus;
    private StorageReference mstorage;
    private ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currUser.getUid();
        dbrefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mstorage = FirebaseStorage.getInstance().getReference();


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
                if(u.image_link.length()>5)
                Picasso.with(AccountSettingActivity.this).load(u.image_link).into(profile_image);
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

        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send intent to pick image
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent, "Select image"), reqCodeForImageChoosing);
                /*
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AccountSettingActivity.this);*/

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //image picking successfull then start the intent to cropimage Activity
        if (requestCode == reqCodeForImageChoosing && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        //if image crop is done successfully then upload to firebase
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mprogress = new ProgressDialog(AccountSettingActivity.this);
                mprogress.setTitle("Uploading Image");
                mprogress.setMessage("Please wait till the upload is finish");
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.show();

                Uri resultUri = result.getUri();
                StorageReference filePath = mstorage.child("profile_images").child(currUser.getUid() + ".jpg");
                //upload file to that database refrance
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            String download_url = task.getResult().getDownloadUrl().toString();
                            Log.i("8****************",download_url);
                            //set image link of current user
                            dbrefUser.child("image_link").setValue(download_url + "");
//                            dbrefUser.child("image_link").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(AccountSettingActivity.this, "uploaded to firebase", Toast.LENGTH_SHORT).show();
//                                        mprogress.dismiss();
//                                    } else {
//                                        Toast.makeText(AccountSettingActivity.this, "faild to upload to firebase", Toast.LENGTH_SHORT).show();
//                                        mprogress.dismiss();
//                                    }
//                                }
//                            });
                            Toast.makeText(AccountSettingActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            mprogress.dismiss();
                        } else {
                            Toast.makeText(AccountSettingActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            mprogress.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static final int reqCodeForImageChoosing = 40;

}
