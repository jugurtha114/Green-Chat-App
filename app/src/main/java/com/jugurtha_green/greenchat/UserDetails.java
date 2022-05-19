package com.jugurtha_green.greenchat;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


final public class UserDetails {


    static {
        //initialise evrything here


    }


    private static String temp_string;
    private static long temp_int=0;
    private static boolean done = false;


    private static Context appCONTEXT;

    private static final String filename_USER_INFO = "Jugurtha-Green_Preference_USER_INFO";
    private static final String filename_NETWORK_INFO = "Jugurtha-Green_Preference_NETWORK_DATA";
    private static final String filename_TELEPHONY_INFO = "Jugurtha-Green_Preference_TELEPHONY_DATA";
    private static final String filename_DEVICE_INFO = "Jugurtha-Green_Preference_DEVICE_DATA";
    private static final String filename_MESSAGES = "Jugurtha-Green_Preference_MESSAGE_DATA";
    private static final String filename_FRIENDS = "Jugurtha-Green_Preference_FRIENDS_DATA";
    private static final String filename_FRIEND_REQUESTS = "Jugurtha-Green_Preference_FRIEND_REQUESTS_DATA";
    private static final String filename_SECURED_DATA = filename_USER_INFO;
    private static final String _username_pref = UserDetails.get_username_KEY();

    private static final DatabaseReference _db_ref = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference db_UID_ref = _db_ref.child(UserDetails.get_users_IDs_DIR());
    private static final DatabaseReference db_users_Emails_ref = _db_ref.child(UserDetails.get_users_EMAILs_DIR());
    private static final DatabaseReference _db_users_ref = _db_ref.child(UserDetails.get_users_DIR());
    private static final StorageReference _db_Storage_ref = FirebaseStorage.getInstance().getReference();
    private static final StorageReference _db_Storage_image_ref = _db_Storage_ref.child(UserDetails.get_profile_images_DIR());

    private static Encryption _encryption_jugu;
    private final static byte NUMBER_OF_TRY = 5;

    private final static String _db_ref_JUGU = "https://jugurtha-green-social.firebaseio.com/";
    private final static String _users_DIR = "users";
    private final static String _notifications_DIR = "Notifications";
    private final static String _messages_DIR = "Messages";
    private final static String _user_info_DIR = "User_info";
    private final static String _device_info_DIR = "Device_info";
    private final static String _telephony_info_DIR = "Telephony_info";
    private final static String _network_info_DIR = "Network_info";
    private final static String _friend_requests_DIR = "Friend_requests";
    private final static String _friends_DIR = "Friends";
    private final static String _users_IDs_DIR = "Users_IDs";
    private final static String _secured_data_DIR = _user_info_DIR;
    private final static String _profile_images_DIR = "profile_images";
    private final static String _profile_thumb_images_DIR = "profile_thumb_images";
    private final static String _users_EMAILs_DIR = "Users_Emails";
    private final static String _Chats_DIR = "Chats";
    private final static String _message_IMAGE_DIR = "message_images";

    private final static String _password_KEY = "password";
    private final static String _email_KEY = "email";
    private final static String _username_KEY = "username";

    private final static String _Battery_percentage_KEY = "Battery_percentage";
    private final static String _Brand_KEY = "Brand";
    private final static String _UserInfo_online_stat_KEY = "online";
    private final static String _friend_request_type_KEY = "type";
    private final static String _notification_type_KEY = "type";
    private final static String _notification_from_KEY = "from";
    private final static String _friends_DATE_KEY = "date";
    private final static String _chat_seen_KEY = "seen";
    private final static String _chat_TimeStamp_KEY = "timestamp";
    private final static String _message_SEEN_KEY = "seen";
    private final static String _message_MESSAGE_KEY = "message";
    private final static String _message_TYPE_KEY = "type";
    private final static String _message_TimeStamp_KEY = "timestamp";
    private final static String _message_FROM_KEY = "from";
    private final static String _UserInfo_job_KEY="job";
    private final static String _UserInfo_gender_KEY="gender";
    private final static String _UserInfo_location_KEY="location";
    private final static String _UserInfo_gold_amount_KEY="gold";
    private final static String _UserInfo_gems_amount_KEY="gems";
    private final static String _UserInfo_friends_amount_KEY="friends";
    private final static String _UserInfo_followers_amount_KEY="followers";
    private final static String _UserInfo_nationality_KEY="nationality";
    private final static String _UserInfo_exp_KEY="exp";
    private final static String _UserInfo_birthday_KEY="birthday";
    private final static String _UserInfo_status_KEY = "status";
    private final static String _UserInfo_name_KEY = "name";
    private final static String _UserInfo_profile_image_URl_KEY = "profile_image_URl";
    private final static String _UserInfo_device_token_KEY = "device_token";
    private final static String _profile_image_thumb_URL_KEY = "profile_image_thumb_URL";

    private final static String _DEFAULT_profile_img_URL_VALUE = "DEFAULT";
    private final static String _message__TYPE_Image___VALUE = "image";
    private final static String _message__TYPE_Text___VALUE = "text";
    private final static String _friend_request__NOT_FRIENDS__VALUE = "0";
    private final static String _friend_request__SENT__VALUE = "1";
    private final static String _friend_request__RECEIVED__VALUE = "2";
    private final static String _friend_request__FRIENDS__VALUE = "3";
    private final static String _notification_type__REQUEST__VALUE = "friend_request";
    private final static boolean _online__TRUE__VALUE = true;
    private final static boolean _seen__TRUE__VALUE = true;
    private final static boolean _seen__FALSE__VALUE = false;

    private final static String _Jugurtha_Green = "Jugurtha-Green";

    private static String _username;
    private static String _password;
    private static String _email;
    private static String _Current_UID;


    private static HashMap<String, String> _hashMap_devce_info, _hashMap_telephony_info, _hashMap_network_info, _hashMap_user_info;

    private static String _UserInfo_display_name;
    private static String _UserInfo_online_stat;
    private static String _UserInfo_profile_image_URl;
    private static String _UserInfo_profile_image_thumb_URL;
    private static String _UserInfo_status;
    private static String _UserInfo_job;
    private static String _UserInfo_gender;
    private static String _UserInfo_location;
    private static String _UserInfo_gold_amount;
    private static String _UserInfo_gems_amount;
    private static String _UserInfo_friends_amount;
    private static String _UserInfo_followers_amount;
    private static String _UserInfo_nationality;
    private static String _UserInfo_exp;
    private static String _UserInfo_birthday;


/*
    private static final String _msg_user_info_NAME_updated = "Your Name has been successfully Updated !";
    private static final String _msg_user_info_NAME_updated_error = "An Error occurred while updating your Name!\n";
    private static final String _msg_user_info_profile_image_URl_updated = "Your Profile image has been successfully Updated !";
    private static final String _msg_user_info_profile_image_URl_updated_error = "An Error occurred while updating your Profile image !\n";
    private static final String _msg_user_info_status_updated = "Your Status has been successfully Updated !";
    private static final String _msg_user_info_status_updated_error = "An Error occurred while updating your Status!\n";
*/


    public UserDetails() {
        _encryption_jugu = Encryption.getDefault("Jugurtha-Green", UserDetails.get_username(), new byte[16]);

    } //***********************************| Constructor |******************************************









    private static String get_UserInfo_data_from_MEMORY(final String your_KEY){return _hashMap_user_info.get(your_KEY);}
    public static String get_UserInfo_data_from_STORAGE(final String your_KEY){return getShared_Prefereces_jugu(filename_USER_INFO,your_KEY);}


    public static String get_UserInfo_value_jugu(final String your_KEY)
    {
        if(_hashMap_user_info!=null && _hashMap_user_info.get(your_KEY)!=null)return get_UserInfo_data_from_MEMORY(your_KEY);
        else{
            if (get_UserInfo_data_from_STORAGE(your_KEY) != null) {
                _hashMap_user_info.put(your_KEY, get_UserInfo_data_from_STORAGE(your_KEY));
            }else resync_hashMap_user_info();
        }
        return _hashMap_user_info!=null? _hashMap_user_info.get(your_KEY):null;
    }


    public   static void replaceSensors(View oldSensor, View newSensor) {
        ViewGroup parent = (ViewGroup) oldSensor.getParent();

        if (parent == null) {
            return;
        }

        int indexOldSensor = parent.indexOfChild(oldSensor);
        int indexNewSensor = parent.indexOfChild(newSensor);
        parent.removeView(oldSensor);
        parent.addView(oldSensor, indexNewSensor);
        parent.removeView(newSensor);
        parent.addView(newSensor, indexOldSensor);
    }

    public static void get_UserInfo_TxetView_by_Key(final String your_KEY,final int your_Widget_ID)
    {
        final TextView v= new TextView(appCONTEXT).findViewById(your_Widget_ID);
    if(_hashMap_user_info!=null && get_UserInfo_value_jugu(your_KEY)!=null)v.setText(get_UserInfo_value_jugu(your_KEY));
    else { get_db_USER_INFO().addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                _hashMap_user_info.put(uniqueKeySnapshot.getKey() + "", uniqueKeySnapshot.getValue() + "");
            }
            v.setText(_hashMap_user_info.get(your_KEY));
 //          editShared_pref_HashMap_jugu(filename_USER_INFO,_hashMap_user_info);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    });
    }
    }

public static boolean isCurrent_User(final String your_username){
    return your_username.equals(UserDetails.get_username());
}

    public static boolean re_Authenticate_jugu(int number_of_try) {
        byte i = 1;
        if (isAuthenticated_jugu()) {
            return true;
        } else {
            while (!isAuthenticated_jugu() && i <= number_of_try) {
                /**
                 *
                 * TODO change the SystemClock.sleep(700)  with thread
                 */
                SystemClock.sleep(700);
                getmAuth().signInWithEmailAndPassword(UserDetails.get_email(), UserDetails.get_password(UserDetails.get_email()));
                i++;
            }
        }
        return UserDetails.isAuthenticated_jugu();
    }

    public static boolean isAuthenticated_jugu() {
        return getmAuth().getCurrentUser() != null;
    }

    public static String get_time_ago_jugu(long time_in_milis)
    { GetTimeAgo getTimeAgo = new GetTimeAgo();
return   getTimeAgo.getTimeAgo(time_in_milis, getAppCONTEXT());
    }

    private static String getShared_Prefereces_jugu(final String filename,final String your_pref_KEY) {
        SharedPreferences sharedPref = getAppCONTEXT().getSharedPreferences(filename, MODE_PRIVATE);
        return sharedPref.getString(your_pref_KEY, null);
    }

    public static void editShared_preference_jugu(final String filename,final String your_pref_KEY, final String your_VALUE) {

        SharedPreferences sharedPref = getAppCONTEXT().getSharedPreferences(filename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putString(your_pref_KEY, UserDetails.crypt_jugu(your_VALUE,UserDetails.get_Current_UID()));
        editor.putString(your_pref_KEY, your_VALUE);
        editor.apply();
    }

    public static void editShared_pref_HashMap_jugu(final String filename,final HashMap<String, String> your_HashMap) {
        SharedPreferences keyValues = getAppCONTEXT().getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor keyValuesEditor = keyValues.edit();

        for (String s : your_HashMap.keySet()) {
            keyValuesEditor.putString(s, your_HashMap.get(s));
        }

        keyValuesEditor.apply();
    }

    private static void removeShared_preference_jugu(final String filename,final String your_pref_KEY) {
        SharedPreferences sharedPref = getAppCONTEXT().getSharedPreferences(filename, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    public static String getUsername_by_EMAIL(final String your_EMAIL) {

        UserDetails.getDb_users_Emails_ref().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String new_email = your_EMAIL.replace('.', ',');
                if (dataSnapshot.hasChild(new_email)) {

                    UserDetails._username = dataSnapshot.child(new_email).getValue() + "";
                  //  UserDetails.editShared_preference_jugu(filename_USER_INFO,UserDetails.get_username_pref(), UserDetails._username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return UserDetails._username;
    }

    public static String get_Token_jugu() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    public static void disconnect_jugu() {
        getmAuth().signOut();
        UserDetails._username = null;
        UserDetails._password = null;
        UserDetails._email = null;
        UserDetails._Current_UID = null;
//deleteRecursive(new File("/data/data/"+appCONTEXT.getPackageName()+"/shared_prefs"));

    }

    protected static void  deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public static long getCurrent_Time() {
        return System.currentTimeMillis();
    }

    public static String[] map2Array_jugu(final Map your_Map) {
        return (String[]) your_Map.keySet().toArray(new String[your_Map.size()]);
    }

    public static String crypt_jugu(final String s, final String your_UID) {
        _encryption_jugu = Encryption.getDefault("Jugurtha-Green", your_UID, new byte[16]);
        return _encryption_jugu.encryptOrNull(s);
    }

    public static String decrypt_jugu(final String your_crypted_String, final String your_UID) {
        _encryption_jugu = Encryption.getDefault("Jugurtha-Green", your_UID, new byte[16]);
        return _encryption_jugu.decryptOrNull(your_crypted_String);
    }
//-------------------------------------------| Getters |--------------------------------------------
    public static DatabaseReference get_db_username(){return UserDetails.get_db_users_ref().child(UserDetails.get_username());}
    public static DatabaseReference get_db_USER_INFO(){return get_db_username().child(UserDetails.get_user_info_DIR());}
    public static DatabaseReference get_db_NETWORK_INFO(){return get_db_username().child(UserDetails.get_network_info_DIR());}
    public static DatabaseReference get_db_TELEPHONY_INFO(){return get_db_username().child(UserDetails._telephony_info_DIR);}
    public static DatabaseReference get_db_DEVICE_INFO(){return get_db_username().child(UserDetails.get_device_info_DIR());}
    public static DatabaseReference get_db_MESSAGES(){return get_db_username().child(UserDetails.get_messages_DIR());}
    public static DatabaseReference get_db_FRIENDS(){return  get_db_username().child(UserDetails.get_friends_DIR());}
    public static DatabaseReference get_db_FRIEND_REQUEST(){return get_db_username().child(UserDetails.get_friend_requests_DIR());}
    public static DatabaseReference get_db_NOTIFICATIONS(){return get_db_username().child(UserDetails.get_notifications_DIR());}
   // public static DatabaseReference get_db_SECURED_DATA(){return get_db_username().child(UserDetails.get_secured_data_DIR());}

    public static DatabaseReference get_db_username(final String your_username){return UserDetails.get_db_users_ref().child(your_username);}
    public static DatabaseReference get_db_USER_INFO(final String your_username){return get_db_username(your_username).child(UserDetails.get_user_info_DIR());}
    public static DatabaseReference get_db_NETWORK_INFO(final String your_username){return get_db_username(your_username).child(UserDetails.get_network_info_DIR());}
    public static DatabaseReference get_db_TELEPHONY_INFO(final String your_username){return get_db_username(your_username).child(UserDetails.get_telephony_info_DIR());}
    public static DatabaseReference get_db_DEVICE_INFO(final String your_username){return get_db_username(your_username).child(UserDetails.get_device_info_DIR());}
    public static DatabaseReference get_db_MESSAGES(final String your_username){return get_db_username(your_username).child(UserDetails.get_messages_DIR());}
    public static DatabaseReference get_db_FRIENDS(final String your_username){return  get_db_username(your_username).child(UserDetails.get_friends_DIR());}
    public static DatabaseReference get_db_FRIEND_REQUEST(final String your_username){return get_db_username(your_username).child(UserDetails.get_friend_requests_DIR());}
    public static DatabaseReference get_db_NOTIFICATIONS(final String your_username){return get_db_username(your_username).child(UserDetails.get_notifications_DIR());}
   // public static DatabaseReference get_db_SECURED_DATA(final String your_username){return get_db_username(your_username).child(UserDetails.get_secured_data_DIR());}


    public static HashMap<String, String> get_hashMap_user_info(){return _hashMap_user_info;}
    public static HashMap<String, String> get_hashMap_devce_info() {  return _hashMap_devce_info;}
    public static HashMap<String, String> get_hashMap_telephony_info() { return _hashMap_telephony_info; }
    public static HashMap<String, String> get_hashMap_network_info() { return _hashMap_network_info;}
  //  public static HashMap<String, String> get_hashMap_secured_data() { return _hashMap_secured_data;}

    public static String getFilename_USER_INFO() {
        return filename_USER_INFO;
    }

    public static String getFilename_SECURED_DATA() {
        return filename_USER_INFO;
    }

    public static String get_UserInfo_job_KEY() {
        return _UserInfo_job_KEY;
    }

    public static String get_UserInfo_gender_KEY() {
        return _UserInfo_gender_KEY;
    }

    public static String get_UserInfo_location_KEY() {
        return _UserInfo_location_KEY;
    }

    public static String get_UserInfo_gold_amount_KEY() {
        return _UserInfo_gold_amount_KEY;
    }

    public static String get_UserInfo_gems_amount_KEY() {
        return _UserInfo_gems_amount_KEY;
    }

    public static String get_UserInfo_friends_amount_KEY() {
        return _UserInfo_friends_amount_KEY;
    }

    public static String get_UserInfo_followers_amount_KEY() {
        return _UserInfo_followers_amount_KEY;
    }

    public static String get_UserInfo_nationality_KEY() {
        return _UserInfo_nationality_KEY;
    }

    public static String get_UserInfo_exp_KEY() {
        return _UserInfo_exp_KEY;
    }

    public static String get_UserInfo_birthday_KEY() {
        return _UserInfo_birthday_KEY;
    }
//================================| DEVICE_Info HashMap method |====================================
    private static HashMap<String, String> resync_hashMap_devce_info() {
        return _hashMap_devce_info;
    }//---------------------------------------------------------------------------------------------


//================================| TELEPHONY_Info HashMap method |=================================
    private static HashMap<String, String> resync_hashMap_telephony_info() {
        return _hashMap_telephony_info;
    }//---------------------------------------------------------------------------------------------


//================================| NETWORK_Info HashMap method |===================================
    private static HashMap<String, String> resync_hashMap_network_info() {
        return _hashMap_network_info;
    }//---------------------------------------------------------------------------------------------




//================================| USER_Info HashMap method |======================================
    public static void resync_hashMap_user_info() {
        UserDetails.get_db_USER_INFO().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                    _hashMap_user_info.put(uniqueKeySnapshot.getKey() + "", uniqueKeySnapshot.getValue() + "");
                }editShared_pref_HashMap_jugu(filename_USER_INFO,_hashMap_user_info); // it should be isCurrent_User()

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }





    public static String get_message__TYPE_Text___VALUE() {
        return _message__TYPE_Text___VALUE;
    }

    public static String get_message__TYPE_Image___VALUE() {
        return _message__TYPE_Image___VALUE;
    }

    public static boolean is_seen__TRUE__VALUE() {
        return _seen__TRUE__VALUE;
    }

    public static boolean is_seen__FALSE__VALUE() {
        return _seen__FALSE__VALUE;
    }

    public static String get_message_IMAGE_DIR() {
        return _message_IMAGE_DIR;
    }

    public static boolean get_online__TRUE__VALUE() {
        return _online__TRUE__VALUE;
    }



    public static String get_Chats_DIR() {
        return _Chats_DIR;
    }

    public static String get_chat_seen_KEY() {
        return _chat_seen_KEY;
    }

    public static String get_chat_TimeStamp_KEY() {
        return _chat_TimeStamp_KEY;
    }

    public static String get_message_SEEN_KEY() {
        return _message_SEEN_KEY;
    }

    public static String get_message_MESSAGE_KEY() {
        return _message_MESSAGE_KEY;
    }

    public static String get_message_TYPE_KEY() {
        return _message_TYPE_KEY;
    }

    public static String get_message_TimeStamp_KEY() {
        return _message_TimeStamp_KEY;
    }

    public static String get_message_FROM_KEY() {
        return _message_FROM_KEY;
    }

    public static StorageReference get_db_Storage_image_ref() {
        return _db_Storage_image_ref;
    }

    public static Context getAppCONTEXT() {
        return appCONTEXT;
    }

    public static DatabaseReference getDb_users_Emails_ref() {
        return db_users_Emails_ref;
    }

    public static String get_users_EMAILs_DIR() {
        return _users_EMAILs_DIR;
    }

    public static String get_friends_DATE_KEY() {
        return _friends_DATE_KEY;
    }

    public static String get_notification_type_KEY() {
        return _notification_type_KEY;
    }

    public static String get_notification_from_KEY() {
        return _notification_from_KEY;
    }

    public static String get_notification_type__REQUEST__VALUE() {
        return _notification_type__REQUEST__VALUE;
    }

    public static String get_friend_request_type_KEY() {
        return _friend_request_type_KEY;
    }

    public static String get_friend_request__NOT_FRIENDS__VALUE() {
        return _friend_request__NOT_FRIENDS__VALUE;
    }

    public static String get_friend_request__SENT__VALUE() {
        return _friend_request__SENT__VALUE;
    }

    public static String get_friend_request__RECEIVED__VALUE() {
        return _friend_request__RECEIVED__VALUE;
    }

    public static String get_friend_request__FRIENDS__VALUE() {
        return _friend_request__FRIENDS__VALUE;
    }

    public static String get_notifications_DIR() {
        return _notifications_DIR;
    }

    public static String get_messages_DIR() {
        return _messages_DIR;
    }



    public static String get_username_pref() {
        return _username_pref;
    }

    public static String get_profile_thumb_images_DIR() {
        return _profile_thumb_images_DIR;
    }

    public static String get_Jugurtha_Green() {
        return _Jugurtha_Green;
    }

    public static String get_profile_images_DIR() {
        return _profile_images_DIR;
    }

    public static String get_DEFAULT_profile_img_URL_VALUE() {
        return _DEFAULT_profile_img_URL_VALUE;
    }

    public static StorageReference get_db_Storage_ref() {
        return _db_Storage_ref;
    }

    public static String get_UserInfo_name_KEY() {
        return _UserInfo_name_KEY;
    }

    public static String get_UserInfo_online_stat_KEY() {
        return _UserInfo_online_stat_KEY;
    }

    public static String get_UserInfo_status_KEY() {
        return _UserInfo_status_KEY;
    }

    public static String get_UserInfo_profile_image_URl_KEY() {
        return _UserInfo_profile_image_URl_KEY;
    }

    public static String get_UserInfo_device_token_KEY() {
        return _UserInfo_device_token_KEY;
    }

    public static String get_profile_image_thumb_URL_KEY() {
        return _profile_image_thumb_URL_KEY;
    }

    public static String get_Battery_percentage_KEY() {
        return _Battery_percentage_KEY;
    }

    public static String get_Brand_KEY() {
        return _Brand_KEY;
    }

    public static DatabaseReference get_db_UID_ref() {
        return db_UID_ref;
    }

    public static DatabaseReference get_db_ref() {
        return _db_ref;
    }

    public static DatabaseReference get_db_users_ref() {
        return _db_users_ref;
    }


    public static String getUsername_By_UID(final String your_UID) {

        if (UserDetails.isAuthenticated_jugu()) {
            if (UserDetails.get_Current_UID() == your_UID) {

            }

            UserDetails.get_db_UID_ref().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(your_UID)) {

                       if (your_UID.equals(get_Current_UID())){UserDetails._username = dataSnapshot.child(your_UID).getValue() + "";
                        UserDetails.editShared_preference_jugu(filename_USER_INFO,UserDetails.get_username_pref(), UserDetails._username);}
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
        return UserDetails._username;
    }


 /*   public static String get_Current_UID(FirebaseAuth your_mAuth) {
        if (UserDetails._Current_UID != null) {
            return UserDetails._Current_UID;
        } else if (UserDetails.isAuthenticated_jugu()) {

            UserDetails.set_Current_UID(your_mAuth.getCurrentUser().getUid());
            return UserDetails._Current_UID;
        } else return null;

    }*/


    public static String get_Current_UID() {
        if (UserDetails._Current_UID != null) {
            return UserDetails._Current_UID;
        } else if (UserDetails.isAuthenticated_jugu()) {

            UserDetails.set_Current_UID(UserDetails.getmAuth().getCurrentUser().getUid());
            return UserDetails._Current_UID;
        } else return null;

    }


    public static String get_password(final String your_email) {return decrypt_jugu(getShared_Prefereces_jugu(getFilename_USER_INFO(),get_password_KEY()),get_Current_UID()) ;}

    public static String get_secured_data_DIR() {
        return _secured_data_DIR;
    }

    public static String get_email() {
        if (UserDetails._email != null) { return UserDetails._email;}
return getShared_Prefereces_jugu(getFilename_USER_INFO(),get_email_KEY());
    }

    public static FirebaseAuth getmAuth() {
        return FirebaseAuth.getInstance();
    }

    public static String get_users_DIR() {
        return _users_DIR;
    }

    public static String get_db_ref_JUGU() {
        return _db_ref_JUGU;
    }

    public static String get_user_info_DIR() {
        return _user_info_DIR;
    }

    public static String get_device_info_DIR() {
        return _device_info_DIR;
    }

    public static String get_telephony_info_DIR() {
        return _telephony_info_DIR;
    }

    public static String get_network_info_DIR() {
        return _network_info_DIR;
    }

    public static String get_friend_requests_DIR() {
        return _friend_requests_DIR;
    }

    public static String get_friends_DIR() {
        return _friends_DIR;
    }


    public static String get_users_IDs_DIR() {
        return _users_IDs_DIR;
    }

    public static String get_username() {
        if (UserDetails._username != null) {return UserDetails._username;}
final String usr=getShared_Prefereces_jugu(getFilename_SECURED_DATA(),UserDetails.get_username_pref());
        if (usr != null) {UserDetails._username=usr;return usr;
        } else {
            UserDetails.set_username(getUsername_By_UID(get_Current_UID()));
            UserDetails.editShared_preference_jugu(getFilename_SECURED_DATA(),UserDetails.get_username_pref(), UserDetails._username);
        }
        return UserDetails._username;
    }


    public static String get_password_KEY() {

        return _password_KEY;
    }

    public static String get_email_KEY() {
        return _email_KEY;
    }

    public static String get_username_KEY() {
        return _username_KEY;
    }

//-------------------------------------------| Setters |--------------------------------------------
public static void setAppCONTEXT(Context appCONTEXT) {
    UserDetails.appCONTEXT = appCONTEXT;
}

    public static void set_Current_UID(final String _Current_UID) {
        UserDetails._Current_UID = _Current_UID;
    }

    public static void set_email(final String _email) {
        UserDetails._email = _email;
    }

    public static void set_username(final String _username) {
        UserDetails._username = _username;
        UserDetails.editShared_preference_jugu(getFilename_SECURED_DATA(),UserDetails.get_username_pref(), UserDetails._username);

    }

    public static void set_password(final String your_password, final String your_email) {
        UserDetails._password = UserDetails.crypt_jugu(your_password, your_email);
    }




    public static void set_UserInfo_display_name(final String your_new_NAME, final String your_username) {
       if (isCurrent_User(your_username))editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_name_KEY(),  your_new_NAME+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_name_KEY()).setValue(your_new_NAME).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_display_name = your_new_NAME;
                }
            }
        });
    }



    public static void set_UserInfo_online(final String your_online_stat, final String your_username) {

        if (isCurrent_User(your_username))editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_online_stat_KEY(),  your_online_stat+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_online_stat_KEY()).setValue(your_online_stat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_display_name = your_online_stat;
                }
            }
        });
    }



    public static void set_UserInfo_profile_image_URl(final String your_profile_image_URl, final String your_username) {
        if (isCurrent_User(your_username))editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_profile_image_URl_KEY(),  your_profile_image_URl+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_profile_image_URl_KEY()).setValue(your_profile_image_URl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_display_name = your_profile_image_URl;
                }
            }
        });
    }



    public static void set_UserInfo_profile_image_thumb_URL(final String your_profile_image_thumb_URL, final String your_username) {
        if (isCurrent_User(your_username))editShared_preference_jugu(filename_USER_INFO,  get_profile_thumb_images_DIR(),  your_profile_image_thumb_URL+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_profile_image_thumb_URL_KEY()).setValue(your_profile_image_thumb_URL).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_display_name = your_profile_image_thumb_URL;
                }
            }
        });
    }






    public static void set_UserInfo_online_stat(final String your_Online_stat, final String your_username) {
        if (isCurrent_User(your_username))editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_online_stat_KEY(),  your_Online_stat+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_profile_image_thumb_URL_KEY()).setValue(your_Online_stat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_online_stat = your_Online_stat+"";
                }
            }
        });
    }



    public static void set_UserInfo_job(final String your_UserInfo_job, final String your_username) {

        if (isCurrent_User(your_username))editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_job_KEY(),  your_UserInfo_job+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_job_KEY()).setValue(your_UserInfo_job).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_display_name = your_UserInfo_job+"";
                }
            }
        });
    }



    public static void set_UserInfo_gender(final String your_UserInfo_gender, final String your_username) {
        if (isCurrent_User(your_username))editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_gender_KEY(),  your_UserInfo_gender+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_gender_KEY()).setValue(your_UserInfo_gender).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_gender = your_UserInfo_gender+"";
                }
            }
        });
    }



    public static void set_UserInfo_location(final String your_UserInfo_location, final String your_username) {
        if (isCurrent_User(your_username)) editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_location_KEY(),  your_UserInfo_location+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_location_KEY()).setValue(your_UserInfo_location).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_location = your_UserInfo_location;
                }
            }
        });
    }




    public static void set_UserInfo_gold_amount(final String your_UserInfo_gold_amount, final String your_username) {
        if (isCurrent_User(your_username)) editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_gold_amount_KEY(),  your_UserInfo_gold_amount+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_gold_amount_KEY()).setValue(your_UserInfo_gold_amount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_gold_amount = your_UserInfo_gold_amount;
                }
            }
        });
    }



    public static void set_UserInfo_gems_amount(final String your_UserInfo_gems_amount, final String your_username) {
        if (isCurrent_User(your_username)) editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_gems_amount_KEY(),  your_UserInfo_gems_amount+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_gems_amount_KEY()).setValue(your_UserInfo_gems_amount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_gems_amount = your_UserInfo_gems_amount;
                }
            }
        });
    }



        public static void set_UserInfo_friends_amount(/*final String your_UserInfo_friends_amount,*/ final String your_username) {

            get_db_FRIENDS(your_username).addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    temp_int=dataSnapshot.getChildrenCount();
                    get_db_USER_INFO().child(UserDetails.get_UserInfo_friends_amount_KEY()).setValue(temp_int+"").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (isCurrent_User(your_username)) editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_friends_amount_KEY(),  temp_int+""+"");
                        }
                    });
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    temp_int=dataSnapshot.getChildrenCount();
                    get_db_USER_INFO().child(UserDetails.get_UserInfo_friends_amount_KEY()).setValue(temp_int+"").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (isCurrent_User(your_username)) editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_friends_amount_KEY(),  temp_int+""+"");
                        }
                    });
                }
                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    temp_int=dataSnapshot.getChildrenCount();
                    get_db_USER_INFO().child(UserDetails.get_UserInfo_friends_amount_KEY()).setValue(temp_int+"").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (isCurrent_User(your_username)) editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_friends_amount_KEY(),  temp_int+""+"");
                        }
                    });
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    temp_int=dataSnapshot.getChildrenCount();
                    get_db_USER_INFO().child(UserDetails.get_UserInfo_friends_amount_KEY()).setValue(temp_int+"").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (isCurrent_User(your_username)) editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_friends_amount_KEY(),  temp_int+""+"");
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(appCONTEXT, "Friend request Canceled !\nError : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        }

    public static void set_UserInfo_followers_amount(final String your_UserInfo_followers_amount, final String your_username) {
        if (isCurrent_User(your_username)) editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_followers_amount_KEY(),  your_UserInfo_followers_amount+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_followers_amount_KEY()).setValue(your_UserInfo_followers_amount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_followers_amount = your_UserInfo_followers_amount;
                }
            }
        });
    }



    public static void set_UserInfo_nationality(final String your_UserInfo_nationality, final String your_username) {
        if (isCurrent_User(your_username))  editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_nationality_KEY(),  your_UserInfo_nationality+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_nationality_KEY()).setValue(your_UserInfo_nationality).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_nationality = your_UserInfo_nationality;
                }
            }
        });
    }



    public static void set_UserInfo_exp(final String your_UserInfo_exp, final String your_username) {
        if (isCurrent_User(your_username)) editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_exp_KEY(),  your_UserInfo_exp+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_exp_KEY()).setValue(your_UserInfo_exp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_exp = your_UserInfo_exp;
                }
            }
        });
    }



    public static void set_UserInfo_birthday(final String your_UserInfo_birthday, final String your_username) {
        if (isCurrent_User(your_username)) editShared_preference_jugu(filename_USER_INFO,  get_UserInfo_birthday_KEY(),  your_UserInfo_birthday+"");
        get_db_USER_INFO(your_username).child(UserDetails.get_UserInfo_birthday_KEY()).setValue(your_UserInfo_birthday).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserDetails._UserInfo_birthday = your_UserInfo_birthday;
                }
            }
        });

    }



    public static void set_hashMap_devce_info(HashMap<String, String> _hashMap_devce_info) {
        editShared_pref_HashMap_jugu(UserDetails.filename_DEVICE_INFO,_hashMap_devce_info);
        UserDetails._hashMap_devce_info = _hashMap_devce_info;
    }

    public static void set_hashMap_telephony_info(HashMap<String, String> _hashMap_telephony_info) {
        editShared_pref_HashMap_jugu(UserDetails.filename_TELEPHONY_INFO,_hashMap_telephony_info);
        UserDetails._hashMap_telephony_info = _hashMap_telephony_info;
    }

    public static void set_hashMap_network_info(HashMap<String, String> _hashMap_network_info) {
        editShared_pref_HashMap_jugu(UserDetails.filename_NETWORK_INFO,_hashMap_network_info);
        UserDetails._hashMap_network_info = _hashMap_network_info;
    }

    public static void set_hashMap_user_info(HashMap<String, String> _hashMap_user_info) {
        editShared_pref_HashMap_jugu(UserDetails.filename_USER_INFO,_hashMap_user_info);
        UserDetails._hashMap_user_info = _hashMap_user_info;
    }
//
//    public static void set_hashMap_secured_data(HashMap<String, String> _hashMap_secured_data) {
//        editShared_pref_HashMap_jugu(UserDetails._secured_data_DIR,_hashMap_secured_data);
//        UserDetails._hashMap_secured_data = _hashMap_secured_data;
//    }



    }

