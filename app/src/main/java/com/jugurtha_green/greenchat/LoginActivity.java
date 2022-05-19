package com.jugurtha_green.greenchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEmail, mLoginPassword;

    private Button mLogin_btn;

    private ProgressDialog mLoginProgress;

    private String _email, _password;
    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserDetails.setAppCONTEXT(getApplicationContext());

        UserDetails.disconnect_jugu();


        mLoginProgress = new ProgressDialog(this);

        mUserDatabase = UserDetails.get_db_users_ref();


        mLoginEmail = (EditText) findViewById(R.id.login_email);
        mLoginPassword = (EditText) findViewById(R.id.login_password);
        mLogin_btn = (Button) findViewById(R.id.login_btn);

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UserDetails.deleteRecursive(new File("/data/data/"+getApplicationContext().getPackageName()+"/shared_prefs"));
                _email = mLoginEmail.getText().toString();
                _password = mLoginPassword.getText().toString();


                if (!TextUtils.isEmpty(_email) || !TextUtils.isEmpty(_password) && validate_jugu()) {

                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    loginUser(_email, _password);

                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean validate_jugu() {
        boolean valid = true;

//----------------------------------------------------------------
        if (_email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            mLoginEmail.setError("Enter a valid Email address");
            valid = false;
        } else {
            mLoginEmail.setError(null);
        }
//----------------------------------------------------------------
        if (_password.isEmpty() || _password.length() < 4 || _password.length() > 10) {
            mLoginPassword.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mLoginPassword.setError(null);
        }
//----------------------------------------------------------------

        return valid;
    }

    private void loginUser(final String email, String password) {


        UserDetails.getmAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    save_data_offline();

                    UserDetails.getDb_users_Emails_ref().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(_email.replace('.', ','))) {
                                UserDetails.set_username(dataSnapshot.child(_email.replace('.', ',')).getValue() + "");

                                //UserDetails.re_Authenticate_jugu(2);
                                UserDetails.get_db_USER_INFO().addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                            HashMap<String, String> _hashMap_temp = new HashMap<String, String>();
                                            for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                                                _hashMap_temp.put(uniqueKeySnapshot.getKey() + "", uniqueKeySnapshot.getValue() + "");
                                            }
                                            UserDetails.set_hashMap_user_info(_hashMap_temp);

                                            UserDetails.get_db_USER_INFO().child(UserDetails.get_UserInfo_device_token_KEY()).
                                                    setValue(UserDetails.get_Token_jugu()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mLoginProgress.dismiss();

                                                    //UserDetails.resync_hashMap_user_info();
                                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);

                                                    finish();


                                                }
                                            });

                                        } // error data snapshot not found
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                //
                                //******* add here resync
                            } else {
                                mLoginProgress.dismiss();
                                Toast.makeText(LoginActivity.this, "Email probably not found !", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {

                    mLoginProgress.hide();


                    Toast.makeText(LoginActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }

            }
        });


    }

    private void save_data_offline() {

        UserDetails.set_email(_email);
        UserDetails.set_password(_password, _email);
        // UserDetails.resync_hashMap_user_info();


    }

}
