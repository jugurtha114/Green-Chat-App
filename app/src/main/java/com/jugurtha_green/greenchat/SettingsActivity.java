package com.jugurtha_green.greenchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference _db_user_info_ref;
    private FirebaseUser mCurrentUser;


    //Android Layout

    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;

    private Button mStatusBtn;
    private Button mImageBtn;


    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private final StorageReference mImageStorage =UserDetails.get_db_Storage_ref();

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDisplayImage = (CircleImageView) findViewById(R.id.settings_image);
        mName = (TextView) findViewById(R.id.settings_name);
        mStatus = (TextView) findViewById(R.id.settings_status);
        mStatusBtn = (Button) findViewById(R.id.settings_status_btn);
        mImageBtn = (Button) findViewById(R.id.settings_image_btn);






        _db_user_info_ref = UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_user_info_DIR());
        UserDetails.get_db_users_ref().child(UserDetails.get_username()).keepSynced(true);

        _db_user_info_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String _Display_name = dataSnapshot.child(UserDetails.get_UserInfo_name_KEY()).getValue()+"";
                final String _profile_img_URL = dataSnapshot.child(UserDetails.get_UserInfo_profile_image_URl_KEY()).getValue()+"";
                String _user_status = dataSnapshot.child(UserDetails.get_UserInfo_status_KEY()).getValue()+"";
                String _profile_img_thumb_URL = dataSnapshot.child(UserDetails.get_profile_image_thumb_URL_KEY()).getValue().toString();

                mName.setText(_Display_name);
                mStatus.setText(_user_status);

                if(!_profile_img_URL.equals(UserDetails.get_DEFAULT_profile_img_URL_VALUE())) {

                    //Picasso.with(SettingsActivity.this).load(User_info).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                    Picasso.get(/*SettingsActivity.this*/).load(_profile_img_URL).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_avatar).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                            Picasso.get(/*SettingsActivity.this*/).load(_profile_img_URL).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status_value = mStatus.getText().toString();

                Intent status_intent = new Intent(SettingsActivity.this, StatusActivity.class);
                status_intent.putExtra("status_value", status_value);
                startActivity(status_intent);

            }
        });


        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);


                /*
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
                        */

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

           // Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_LONG).show();

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                mProgressDialog = new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setIcon(R.drawable.green_earth_jugu);
                mProgressDialog.getProgress();
                mProgressDialog.setMessage("Please wait while we upload and process the User_info.");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

               final String _username = UserDetails.get_username();


                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                final byte[] thumb_byte = baos.toByteArray();


                final StorageReference filepath = mImageStorage.child(UserDetails.get_username()).child(UserDetails.get_profile_images_DIR()). child(UserDetails.get_Jugurtha_Green()+"_"+_username +"_"+ UserDetails.getCurrent_Time() +".jpg");
                final StorageReference thumb_filepath = mImageStorage.child(UserDetails.get_username()).child(UserDetails.get_profile_images_DIR()).child(UserDetails.get_profile_thumb_images_DIR()). child(UserDetails.get_Jugurtha_Green()+"_"+_username +"_"+ UserDetails.getCurrent_Time() +".jpg");


                //*************************************************************
                filepath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();


                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri download_url = task.getResult();
/*                            Map map=new HashMap();
                            map.put("image",downloadUri.toString());*/

                            Map<String, Object> update_hashMap = new HashMap<String, Object>();
                            update_hashMap.put(UserDetails.get_UserInfo_profile_image_URl_KEY(), download_url+"");
                           // update_hashMap.put(UserDetails.get_profile_image_thumb_URL_KEY(), thumb_downloadUrl);
                            _db_user_info_ref.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        mProgressDialog.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Success Uploading.", Toast.LENGTH_LONG).show();

                                    }

                                }
                            });


                        } else {

                            Toast.makeText(SettingsActivity.this, "Error in uploading Image.\n "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();

                        }


                    }
                });
                //***********************************************************


                thumb_filepath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();


                        }
                        return thumb_filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri download_url_thumb = task.getResult();
/*                            Map map=new HashMap();
                            map.put("image",downloadUri.toString());*/

                            Map<String, Object> update_hashMap = new HashMap<String, Object>();
                            update_hashMap.put(UserDetails.get_UserInfo_profile_image_URl_KEY(), download_url_thumb+"");
                            // update_hashMap.put(UserDetails.get_profile_image_thumb_URL_KEY(), thumb_downloadUrl);
                            _db_user_info_ref.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                      //  mProgressDialog.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Success Uploading thumb.", Toast.LENGTH_LONG).show();

                                    }

                                }
                            });


                        } else {

                            Toast.makeText(SettingsActivity.this, "Error in uploading Thumb  Image.\n "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                           // mProgressDialog.dismiss();

                        }


                    }
                });


/*                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                          //  final String download_url = task.getResult().getDownloadUrl().toString();
                            final  String download_url="https://scontent-mad1-1.xx.fbcdn.net/v/t1.0-1/cp0/e15/q65/p74x74/37770200_681882985479238_6448686725412683776_n.jpg?_nc_cat=0&efg=eyJpIjoiYiJ9&oh=ade410663053754181d038d1c61a1d85&oe=5C0739DD";

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                 //   String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                                    String thumb_downloadUrl="https://scontent-mad1-1.xx.fbcdn.net/v/t1.0-1/cp0/e15/q65/p74x74/37770200_681882985479238_6448686725412683776_n.jpg?_nc_cat=0&efg=eyJpIjoiYiJ9&oh=ade410663053754181d038d1c61a1d85&oe=5C0739DD";
                                    if(thumb_task.isSuccessful()){

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put(UserDetails.get_UserInfo_profile_image_URl_KEY(), download_url);
                                        update_hashMap.put(UserDetails.get_profile_image_thumb_URL_KEY(), thumb_downloadUrl);

                                        _db_user_info_ref.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(SettingsActivity.this, "Success Uploading.", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });


                                    } else {

                                        Toast.makeText(SettingsActivity.this, "Error in uploading thumbnail.\n "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();

                                    }


                                }
                            });



                        } else {

                            Toast.makeText(SettingsActivity.this, "Error in uploading."+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();

                        }

                    }
                });*/
/*

                thumb_filepath.putBytes(thumb_byte).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw  task.getException();
                        }
                      //  String thumb_downloadUrl=thumb_filepath.getDownloadUrl()+"";
                        return thumb_filepath.getDownloadUrl();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, Object> update_hashMap = new HashMap<String, Object>();
                      //  update_hashMap.put(UserDetails.get_UserInfo_profile_image_URl_KEY(), download_url);
                        update_hashMap.put(UserDetails.get_profile_image_thumb_URL_KEY(), uri);

                        _db_user_info_ref.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){

                                  //  mProgressDialog.dismiss();
                                    Toast.makeText(SettingsActivity.this, "Success Uploading Thumbnail.", Toast.LENGTH_LONG).show();

                                }

                            }
                        });


                    }
                });




*/




            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }


    }


    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


}
