package com.jugurtha_green.greenchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;
    private ProgressDialog _progressDialog;
    private String _Current_UserID, _name = "", _email = "", _password = "", _username = "";
    private static HashMap<String, String> _hashMap_devce_info, _hashMap_telephony_info, _hashMap_network_info, _hashMap_user_info, _hashMap_secured_data;
    private Snackbar _snackbar;
    private Button _register_Btn;
    private EditText _input_display_name, _input_Username, _input_Email, _input_Password;
    private boolean done = false;
    @Override
    protected void onStart() {
        super.onStart();
if (!isConnected()){
    Toast.makeText(RegisterActivity.this, "You're not connected to a Network !", Toast.LENGTH_LONG).show();
    snackBar_jugu("Please, Check you Internet connectivity !");
}else {snackBar_jugu("Internet connectivity is available !");
    Toast.makeText(RegisterActivity.this, "You're Connected !", Toast.LENGTH_SHORT).show();
}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        UserDetails.setAppCONTEXT(getApplicationContext());
        //Toolbar Set
        Toolbar _toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        _progressDialog = new ProgressDialog(this);
        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        // Android Fields
        _input_display_name = (EditText) findViewById(R.id.register_display_name);
        _input_Username = (EditText) findViewById(R.id.register_username);
        _input_Email = (EditText) findViewById(R.id.register_email);
        _input_Password = (EditText) findViewById(R.id.reg_password);
        _register_Btn = (Button) findViewById(R.id.reg_create_btn);
        TextView _login_Link = (TextView) findViewById(R.id.link_login);


//******************************|  Button Click  |********************************_
        _register_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().translationY(20);
                getInput_data_jugu();
                if (validate_jugu()){getInfo_jugu();register_jugu();}
                else {snackBar_jugu("Please , Fix the form Errors & try again !");}


            }
        });


//**********************************|  Link click  |*********************************
        _login_Link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().translationY(20);
                Intent intent_home = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent_home);
            }
        });


    }//--------------------------------------| onCreate() |-----------------------------------------


    private void register_jugu() {

        _progressDialog.setTitle("Registering User");
        _progressDialog.setMessage("Please wait while we create your account !");
        _progressDialog.setIcon(R.drawable.green_earth_jugu);
        _progressDialog.setCanceledOnTouchOutside(false);
        _progressDialog.show();

        mAuth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful() && UserDetails.isAuthenticated_jugu()) {

                    save_data_offline();
                    onAuthSuccess_jugu();
                    send_data_to_firebase_jugu();

                } else {
                    snackBar_jugu(""+task.getException().getMessage());
                    onAuthFailed_jugu();
                }

            }
        });
    }

    //**************************************************************************************************
    public void snackBar_jugu(String msg) {
        _snackbar = Snackbar.make(findViewById(R.id.signup_constraint_layout), msg, Snackbar.LENGTH_SHORT);
        _snackbar.show();
    }

    //**************************************************************************************************
    public void onRegistrationSuccess() {
        _register_Btn.setEnabled(true);
        Log.d(TAG, "createUserWithEmail:success");
        Toast.makeText(RegisterActivity.this, "Your registration was successful", Toast.LENGTH_LONG).show();
        Intent intent_home = new Intent(RegisterActivity.this, MainActivity.class);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent_home);
        finish();
        //_progressDialog.dismiss();
    }

    //**************************************************************************************************
    public void onRegistrationFailed() {
        Toast.makeText(RegisterActivity.this, "Registration failed, Please, try again", Toast.LENGTH_LONG).show();
    }

    //**************************************************************************************************
    public void onAuthFailed_jugu() {
        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
        _progressDialog.hide();
        Toast.makeText(RegisterActivity.this, "Authentication failed, Please, try again", Toast.LENGTH_LONG).show();
        _register_Btn.setEnabled(true);
    }

    //**************************************************************************************************
    public void onAuthSuccess_jugu() {
        Toast.makeText(RegisterActivity.this, "Authenticated", Toast.LENGTH_SHORT).show();
    }

    //**************************************************************************************************
    public boolean validate_jugu() {
        boolean valid = true;
//----------------------------------------------------------------
        if (_name.isEmpty() || _name.length() < 3) {
            _input_display_name.setError("at least 3 characters");
            valid = false;
        } else {
            _input_display_name.setError(null);
        }
//----------------------------------------------------------------
        if (!_username.matches("[A-Za-z0-9]+")) {
            _input_Username.setError("Only alphabet or number allowed");
            valid = false;
        } else {
            _input_Username.setError(null);
        }
//----------------------------------------------------------------
        if (_email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            _input_Email.setError("Enter a valid Email address");
            valid = false;
        } else {
            _input_Email.setError(null);
        }
//----------------------------------------------------------------
        if (_password.isEmpty() || _password.length() < 7 || _password.length() > 21) {
            _input_Password.setError("Between 8 and 20 alphanumeric characters");
            valid = false;
        } else {
            _input_Password.setError(null);
        }
//----------------------------------------------------------------


        if ( _name.length() > 25) {
            _input_display_name.setError("at Max 24 characters");
            valid = false;
        } else {
            _input_display_name.setError(null);
        }
//----------------------------------------------------------------
        if (_username.length()>21) {
            _input_Username.setError("at Max 20 characters");
            valid = false;
        } else {
            _input_Username.setError(null);
        }
//----------------------------------------------------------------
        if (_email.length() > 41) {
            _input_Email.setError("at Max 40 characters");
            valid = false;
        } else {
            _input_Email.setError(null);
        }

//----------------------------------------------------------------

        return valid;
    }

    //**************************************************************************************************
    public void getInfo_jugu() {
        TelephonyManager _tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        final String getPhoneNumber = _tMgr.getLine1Number();
        final String getMmsUserAgent = _tMgr.getMmsUserAgent();
        final String getNetworkCountryIso = _tMgr.getNetworkCountryIso();
        final String getNetworkOperatorName = _tMgr.getNetworkOperatorName();
        final String getSubscriberId = _tMgr.getSubscriberId();
        final String getDeviceId = _tMgr.getDeviceId();
        final String getDeviceSoftwareVersion = _tMgr.getDeviceSoftwareVersion();
        final String getVoiceMailNumber = _tMgr.getVoiceMailNumber();
        final String getCallState = _tMgr.getCallState() + "";
        final String getCellLocation = _tMgr.getCellLocation() + "";
        final String getPhoneType = _tMgr.getPhoneType() + "";
        final String getDataActivity = _tMgr.getDataActivity() + "";
        final String getDataState = _tMgr.getDataState() + "";

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo _ActiveNetworkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo _NetworkInfo = connectivityManager.getNetworkInfo(1);
        String _AllNetworkInfo = connectivityManager.getAllNetworkInfo().toString();

        _hashMap_telephony_info = new HashMap<>();
        _hashMap_telephony_info.put("MmsUserAgent", getMmsUserAgent);
        _hashMap_telephony_info.put("NetworkCountryIso", getNetworkCountryIso);
        _hashMap_telephony_info.put("NetworkOperatorName", getNetworkOperatorName);
        _hashMap_telephony_info.put("SubscriberId", getSubscriberId);
        _hashMap_telephony_info.put("IMEI", getDeviceId);
        _hashMap_telephony_info.put("DeviceSoftwareVersion", getDeviceSoftwareVersion);
        _hashMap_telephony_info.put("VoiceMailNumber", getVoiceMailNumber);
        _hashMap_telephony_info.put("CallState", getCallState);
        _hashMap_telephony_info.put("CellLocation", getCellLocation);
        _hashMap_telephony_info.put("PhoneType", getPhoneType);
        _hashMap_telephony_info.put("DataActivity", getDataActivity);
        _hashMap_telephony_info.put("DataState", getDataState);
        _hashMap_telephony_info.put("PhoneNumber", getPhoneNumber);

        _hashMap_devce_info = new HashMap<>();
        _hashMap_devce_info.put("Serial", Build.SERIAL);
        _hashMap_devce_info.put("MODEL", Build.MODEL);
        _hashMap_devce_info.put("ID", Build.MANUFACTURER);
        _hashMap_devce_info.put("Manufacture", Build.SERIAL);
        _hashMap_devce_info.put(UserDetails.get_Brand_KEY(), Build.BRAND);
        _hashMap_devce_info.put("Type", Build.TYPE);
        _hashMap_devce_info.put("User", Build.USER);
        _hashMap_devce_info.put("BASE", Build.VERSION_CODES.BASE + "");
        _hashMap_devce_info.put("SERIAL", Build.SERIAL);
        _hashMap_devce_info.put("SDK", Build.VERSION.SDK);
        _hashMap_devce_info.put("BOARD", Build.BOARD);
        _hashMap_devce_info.put("HOST", Build.HOST);
        _hashMap_devce_info.put("FINGERPRINT", Build.FINGERPRINT);
        _hashMap_devce_info.put("Version Code", Build.VERSION.RELEASE);
        _hashMap_devce_info.put("Time", Build.TIME + "");
        _hashMap_devce_info.put(UserDetails.get_Battery_percentage_KEY(), Functions.getBatteryPercentage_jugu(getApplicationContext()) + "");

        _hashMap_network_info = new HashMap<>();
        _hashMap_network_info.put("_ActiveNetworkInfo", _ActiveNetworkInfo + "");
        _hashMap_network_info.put("_NetworkInfo", _NetworkInfo + "");
        _hashMap_network_info.put("_AllNetworkInfo", _AllNetworkInfo);

        final String device_token = FirebaseInstanceId.getInstance().getToken();
        _hashMap_user_info = new HashMap<>();
        _hashMap_user_info.put(UserDetails.get_UserInfo_name_KEY(), _name);
        _hashMap_user_info.put(UserDetails.get_UserInfo_location_KEY(), "Unknown");
        _hashMap_user_info.put(UserDetails.get_UserInfo_followers_amount_KEY(), "0");
        _hashMap_user_info.put(UserDetails.get_UserInfo_exp_KEY(), "0");
        _hashMap_user_info.put(UserDetails.get_UserInfo_gender_KEY(), "Unknown");
        _hashMap_user_info.put(UserDetails.get_UserInfo_job_KEY(), "No Job");
        _hashMap_user_info.put(UserDetails.get_UserInfo_nationality_KEY(), "Algeria");
        _hashMap_user_info.put(UserDetails.get_UserInfo_gold_amount_KEY(), "250");
        _hashMap_user_info.put(UserDetails.get_UserInfo_gems_amount_KEY(), "0");
        _hashMap_user_info.put(UserDetails.get_UserInfo_friends_amount_KEY(), "0");
        _hashMap_user_info.put(UserDetails.get_UserInfo_online_stat_KEY(), UserDetails.get_online__TRUE__VALUE()+"");
        _hashMap_user_info.put(UserDetails.get_UserInfo_birthday_KEY(), "**/**/****");
        _hashMap_user_info.put(UserDetails.get_UserInfo_status_KEY(), "Hi , i'm " + _name + " and I love using GreenChat app !");
        _hashMap_user_info.put(UserDetails.get_UserInfo_profile_image_URl_KEY(), "default");
        _hashMap_user_info.put(UserDetails.get_UserInfo_device_token_KEY(), device_token);
        _hashMap_user_info.put(UserDetails.get_profile_image_thumb_URL_KEY(), UserDetails.get_DEFAULT_profile_img_URL_VALUE());
        _hashMap_user_info.put(UserDetails.get_username_KEY(), _username + "");
        _hashMap_user_info.put(UserDetails.get_email_KEY(), _email + "");
        _hashMap_user_info.put(UserDetails.get_password_KEY(), UserDetails.crypt_jugu(_password,_username ));

        _hashMap_secured_data = new HashMap<>();
        _hashMap_secured_data.put(UserDetails.get_username_KEY(), _username + "");
        _hashMap_secured_data.put(UserDetails.get_email_KEY(), _email + "");
        _hashMap_secured_data.put(UserDetails.get_password_KEY(), UserDetails.crypt_jugu(_password,_username ));
    }

    //**************************************************************************************************
    private void send_data_to_firebase_jugu() {

        _progressDialog.dismiss();
        final DatabaseReference _db_username_ref = UserDetails.get_db_users_ref().child(_username);
        send_sensitive_data_to_firebase_jugu();
        _db_username_ref.child(UserDetails.get_user_info_DIR()).setValue(_hashMap_user_info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                snackBar_jugu("Please wait... 10% (Collecting User data...)");
                if (task.isSuccessful()) {
                    _db_username_ref.child(UserDetails.get_device_info_DIR()).setValue(_hashMap_devce_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            snackBar_jugu("Please wait... 40% (Collecting Device data...)");
                            if (task.isSuccessful()) {
                                _db_username_ref.child(UserDetails.get_telephony_info_DIR()).setValue(_hashMap_telephony_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        snackBar_jugu("Please wait... 75% (Collecting Telephony data...)");
                                        if (task.isSuccessful()) {
                                            _db_username_ref.child(UserDetails.get_network_info_DIR()).setValue(_hashMap_network_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    snackBar_jugu("Please wait... 95% (Collecting Networking data...)");
                                                    if (task.isSuccessful()) {
                                                            save_data_offline();
                                                            onRegistrationSuccess();

                                                    } else {
                                                        snackBar_jugu("ERROR : "+task.getException().getMessage());
                                                        onRegistrationFailed();
                                                    }
                                                }
                                            });
                                        } else {
                                            snackBar_jugu("Failed Collecting network information !\n"+task.getException().getMessage());
                                        }
                                    }
                                });
                            } else {
                                snackBar_jugu("Failed Collecting telephony information !\n"+task.getException().getMessage());
                            }
                        }
                    });

                } else {
                    snackBar_jugu("Failed Collecting user information!\n"+task.getException().getMessage());
                }

            }
        });


        //  finish();
    }

    private void save_data_offline() {
        UserDetails.set_username(_username);
        UserDetails.set_email(_email);
        UserDetails.set_password(_password,UserDetails.get_Current_UID());
        UserDetails.get_db_users_ref().child(_username).keepSynced(true);
        UserDetails.set_hashMap_user_info(_hashMap_user_info);
        UserDetails.set_hashMap_devce_info(_hashMap_devce_info);
        UserDetails.set_hashMap_telephony_info(_hashMap_telephony_info);
        UserDetails.set_hashMap_network_info(_hashMap_network_info);
        //UserDetails.set_hashMap_secured_data(_hashMap_secured_data);

/*        UserDetails.set_device_info_Array(UserDetails.map2Array_jugu(_hashMap_devce_info));
        UserDetails.set_network_info_Array(UserDetails.map2Array_jugu(_hashMap_network_info));
        UserDetails.set_telephony_info_Array(UserDetails.map2Array_jugu(_hashMap_telephony_info));
        UserDetails.set_user_info_Array(UserDetails.map2Array_jugu(_hashMap_user_info));*/

    }


    //**************************************************************************************************
    public void getInput_data_jugu() {
        _name = _input_display_name.getText() + "";
        _username = _input_Username.getText() + "";
        _email = _input_Email.getText() + "";
        _password = _input_Password.getText() + "";


    }

    public void send_sensitive_data_to_firebase_jugu() {
        UserDetails.get_db_users_ref().child(_username).child(UserDetails.get_secured_data_DIR()).setValue(_hashMap_secured_data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful() && UserDetails.isAuthenticated_jugu()) {
                    UserDetails.get_db_UID_ref().child(mAuth.getCurrentUser().getUid()).setValue(_username).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            UserDetails.getDb_users_Emails_ref().child( _email.replace('.',',')).setValue(_username);
                            snackBar_jugu("Secured Data has been sent successfully !");
                        }
                    });
                } else if (UserDetails.re_Authenticate_jugu(3)){

                    UserDetails.get_db_UID_ref().child(mAuth.getCurrentUser().getUid()).setValue(_username).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            snackBar_jugu("Secured Data has been sent AFTER re-Authentication !");
                        }
                    });

                }
            }
        });
    }


//**************************************************************************************************

    private boolean isConnected() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}

