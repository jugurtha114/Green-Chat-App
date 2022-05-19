package com.jugurtha_green.greenchat;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ImageView _profile_imageView, _profile_add_remove_friend_ImageView,icon_edit_birthday,icon_edit_job,icon_edit_nationality,icon_edit_location,icon_edit_status;
    private TextView _profile_DispalyName_TextView, _profile_status_TextView, _profile_add_remove_textView, _txt_profile_birthday,_txt_profile_username, _txt_profile_job, _txt_profile_nationality,_txt_profile_email,_txt_profile_gender,_txt_profile_location,_txt_profile_gold_ammount,_txt_profile_gems_amount,_txt_profile_exp,_txt_profile_online_stat,_txt_profile_followers_amount,_txt_profile_friends_amount;
    private Button _profile_accept_friend_Button, _profile_reject_friend_Button;


    private ProgressDialog mProgressDialog;
    private  HashMap<String, String> _hashMap_temp = new HashMap<String, String>();
    private DatabaseReference _db_selected_user_Friend_Rec;


    private String mCurrent_state, display_name="Loading Name...", status="Loading status...", image="logo", _selected_username ;
    private DatabaseReference _db_current_user_Friend_Rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

         _selected_username = getIntent().getStringExtra(UserDetails.get_username_KEY());


        _db_selected_user_Friend_Rec = UserDetails.get_db_users_ref().child(_selected_username).child(UserDetails.get_friend_requests_DIR());

        _db_current_user_Friend_Rec = UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_friend_requests_DIR());

        _profile_imageView = (ImageView) findViewById(R.id.profile_image);

        _profile_add_remove_friend_ImageView = (ImageView) findViewById(R.id.profile_add_remove_friend_icon);
        _profile_accept_friend_Button = (Button) findViewById(R.id.profile_accept_btn);
        _profile_reject_friend_Button = (Button) findViewById(R.id.profile_decline_btn);

        _profile_DispalyName_TextView = (TextView) findViewById(R.id.profile_displayName);
        _profile_status_TextView = (TextView) findViewById(R.id.profile_status);
        _profile_add_remove_textView = (TextView) findViewById(R.id.profile_add_remove_text);
        _txt_profile_birthday = (TextView) findViewById(R.id.profile_birthday);
        _txt_profile_username=(TextView) findViewById(R.id.profile_username);
        _txt_profile_email=(TextView) findViewById(R.id.profile_email);
        _txt_profile_exp=(TextView) findViewById(R.id.profile_exp);
        _txt_profile_followers_amount=(TextView) findViewById(R.id.profile_followers);
        _txt_profile_gems_amount=(TextView) findViewById(R.id.profile_gems);
        _txt_profile_gold_ammount=(TextView) findViewById(R.id.profile_gold);
        _txt_profile_friends_amount=(TextView) findViewById(R.id.profile_totalFriends);
        _txt_profile_job=(TextView) findViewById(R.id.profile_job);
        _txt_profile_location=(TextView) findViewById(R.id.profile_location);
        _txt_profile_nationality=(TextView) findViewById(R.id.profile_nationality);
        _txt_profile_online_stat=(TextView) findViewById(R.id.profile_online_stat);
        _txt_profile_gender=(TextView) findViewById(R.id.profile_UserInfo_gender);


        icon_edit_birthday = (ImageView) findViewById(R.id.edit_birthday);
        icon_edit_job = (ImageView) findViewById(R.id.edit_job);
        icon_edit_location = (ImageView) findViewById(R.id.edit_location);
        icon_edit_nationality = (ImageView) findViewById(R.id.edit_nationality);
        icon_edit_status = (ImageView) findViewById(R.id.edit_status);


        deactivate_edit_mode_jugu();
//before
        mCurrent_state = UserDetails.get_friend_request__NOT_FRIENDS__VALUE();
        _profile_reject_friend_Button.setVisibility(View.INVISIBLE);
        _profile_accept_friend_Button.setVisibility(View.INVISIBLE);
        _profile_reject_friend_Button.setEnabled(false);
        _profile_accept_friend_Button.setEnabled(false);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();


        UserDetails.get_db_USER_INFO(_selected_username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//*********************************

                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                        _hashMap_temp.put(uniqueKeySnapshot.getKey() + "", uniqueKeySnapshot.getValue() + "");
                    }

                    // UserDetails.editShared_pref_HashMap_jugu(UserDetails.getFilename_USER_INFO(),_hashMap_temp);
                }
                UserDetails.set_UserInfo_friends_amount(_selected_username+"");

                display_name = dataSnapshot.child(UserDetails.get_UserInfo_name_KEY()).getValue()+"";
                 status = dataSnapshot.child(UserDetails.get_UserInfo_status_KEY()).getValue()+"";
                 image = dataSnapshot.child(UserDetails.get_UserInfo_profile_image_URl_KEY()).getValue()+"";
                final String _profile_gold_amount=dataSnapshot.child(UserDetails.get_UserInfo_gold_amount_KEY()).getValue()+"";
                final String _profile_gems_amount=dataSnapshot.child(UserDetails.get_UserInfo_gems_amount_KEY()).getValue()+"";
                final String _profile_friends_amount=dataSnapshot.child(UserDetails.get_UserInfo_friends_amount_KEY()).getValue()+"";
                final String _profile_location=dataSnapshot.child(UserDetails.get_UserInfo_location_KEY()).getValue()+"";
                final String _profile_followers_amount=dataSnapshot.child(UserDetails.get_UserInfo_followers_amount_KEY()).getValue()+"";
                final String _profile_online_stat=dataSnapshot.child(UserDetails.get_UserInfo_online_stat_KEY()).getValue()+"";
                final String _profile_exp=dataSnapshot.child(UserDetails.get_UserInfo_exp_KEY()).getValue()+"";
                final String _profile_nationality=dataSnapshot.child(UserDetails.get_UserInfo_nationality_KEY()).getValue()+"";
                final String _profile_job=dataSnapshot.child(UserDetails.get_UserInfo_job_KEY()).getValue()+"";
                final String _profile_birthday=dataSnapshot.child(UserDetails.get_UserInfo_birthday_KEY()).getValue()+"";
                final String _profile_gender=dataSnapshot.child(UserDetails.get_UserInfo_gender_KEY()).getValue()+"";

                _profile_DispalyName_TextView.setText(display_name);
                _profile_status_TextView.setText(status);
                _txt_profile_email.setText(_hashMap_temp.get(UserDetails.get_email_KEY()));
                _txt_profile_gold_ammount.setText(_profile_gold_amount);
                _txt_profile_gems_amount.setText(_profile_gems_amount);
                _txt_profile_friends_amount.setText(_profile_friends_amount);
                _txt_profile_location.setText(_profile_location);
                _txt_profile_followers_amount.setText(_profile_followers_amount);
                _txt_profile_online_stat.setText(_profile_online_stat.equals(UserDetails.get_online__TRUE__VALUE()+"")?"Online":UserDetails.get_time_ago_jugu(Long.parseLong(_profile_online_stat)));
                _txt_profile_exp.setText(_profile_exp);
                _txt_profile_nationality.setText(_profile_nationality);
                _txt_profile_username.setText(_selected_username);
                _txt_profile_job.setText(_profile_job);
                _txt_profile_birthday.setText(_profile_birthday);
                _txt_profile_gender.setText(_profile_gender);



                Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(_profile_imageView);

                if(UserDetails.get_username().equals(_selected_username)){
                   // if (UserDetails.isCurrent_User(_selected_username))UserDetails.set_hashMap_user_info(_hashMap_temp);
                    onCurrent_user_profile_stat();
                }


                //--------------- FRIENDS LIST / REQUEST FEATURE -----

                _db_current_user_Friend_Rec.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {//-------------------------

                        if(dataSnapshot.hasChild(_selected_username)){

                            String req_type = dataSnapshot.child(_selected_username).child(UserDetails.get_friend_request_type_KEY()).getValue()+"";

                            if(req_type.equals(UserDetails.get_friend_request__RECEIVED__VALUE()))
                            { onReceived_request_stat();}

                            else if(req_type.equals(UserDetails.get_friend_request__SENT__VALUE()))
                            {   onSent_request_stat(); }

                            mProgressDialog.dismiss();
                        } else {


                            UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_friends_DIR()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.hasChild(_selected_username)){ onAlready_friend_stat();  }
                                    mProgressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    mProgressDialog.dismiss();

                                }
                            });
                        }
                    }//-----------------------------------------------------------------------------

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });//***************************************************************************************


        _profile_add_remove_friend_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _profile_add_remove_friend_ImageView.setEnabled(false);

                // --------------- NOT FRIENDS STATE ------------
                if(mCurrent_state.equals(UserDetails.get_friend_request__NOT_FRIENDS__VALUE())){
                    doOn_Send_request_jugu(_selected_username);
                }

                //---------------- on CANCELED --------------
                if(mCurrent_state.equals(UserDetails.get_friend_request__SENT__VALUE())){
                    doOn_Cancel_request_jugu(_selected_username);
                }


                // ------------ UNFRIENDS ---------
                if(mCurrent_state.equals(UserDetails.get_friend_request__FRIENDS__VALUE())){
                    doOn_Unfriend_jugu(_selected_username);
                }


            } // end onClickListner(add/remove/cancel)
        });

        // ------------ REQ RECEIVED STATE ----------


        _profile_accept_friend_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrent_state.equals(UserDetails.get_friend_request__RECEIVED__VALUE())){
                do_On_Accept_request_jugu(_selected_username);}
            }
        });




          _profile_reject_friend_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mCurrent_state.equals(UserDetails.get_friend_request__RECEIVED__VALUE())){
                        doOn_Reject_request_jugu(_selected_username); }
                    }
            });




    }

    private void doOn_Cancel_request_jugu(String _selected_username) {
        Map requestMap = new HashMap();
        requestMap.put(UserDetails.get_username() +"/"+ UserDetails.get_friend_requests_DIR()+"/" + _selected_username , null);
        requestMap.put( _selected_username + "/" +UserDetails.get_friend_requests_DIR()+"/"+UserDetails.get_username(), null);

        UserDetails.get_db_users_ref().updateChildren(requestMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if(databaseError != null){

                    Toast.makeText(ProfileActivity.this, "There was some error in Canceling request\n"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    mCurrent_state = UserDetails.get_friend_request__SENT__VALUE();
                    Toast.makeText(ProfileActivity.this, "Friend request Canceled !", Toast.LENGTH_SHORT).show();
                    onNotFriend_stat();  }



            }
        });
    }


    //========================================| Functions |=============================================
private void doOn_Reject_request_jugu(final String _selected_username) {

    _profile_reject_friend_Button.setEnabled(false);
    _profile_accept_friend_Button.setEnabled(false);
    Map requestMap = new HashMap();
    requestMap.put(UserDetails.get_username() +"/"+ UserDetails.get_friend_requests_DIR()+"/" + _selected_username ,null);
    requestMap.put( _selected_username + "/" +UserDetails.get_friend_requests_DIR()+"/"+UserDetails.get_username() , null);

    UserDetails.get_db_users_ref().updateChildren(requestMap, new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

            if(databaseError != null){
                Toast.makeText(ProfileActivity.this, "Error, can't Rejecting the request !\n"+databaseError.getMessage(), Toast.LENGTH_LONG).show();

            } else {
                mCurrent_state = UserDetails.get_friend_request__NOT_FRIENDS__VALUE();
                _profile_accept_friend_Button.setVisibility(View.GONE);
                _profile_reject_friend_Button.setVisibility(View.GONE);
                onNotFriend_stat();
                Toast.makeText(ProfileActivity.this, "Friend request Rejected !", Toast.LENGTH_SHORT).show(); }

        }
    });

}


    private void doOn_Send_request_jugu(String _selected_username) {
        UserDetails.get_db_users_ref().child(_selected_username).child(UserDetails.get_notifications_DIR()).push();
        String newNotificationId = UserDetails.get_username()+"__"+_selected_username+"__"+UserDetails.get_notification_type__REQUEST__VALUE()+"__"+UserDetails.getCurrent_Time();

        HashMap<String, String> notificationData = new HashMap<>();
        notificationData.put(UserDetails.get_notification_from_KEY(),UserDetails.get_username());
        notificationData.put(UserDetails.get_notification_type_KEY(), UserDetails.get_notification_type__REQUEST__VALUE());

        Map requestMap = new HashMap();
        requestMap.put(UserDetails.get_username() +"/"+ UserDetails.get_friend_requests_DIR()+"/" + _selected_username + "/"+UserDetails.get_friend_request_type_KEY(), UserDetails.get_friend_request__SENT__VALUE());
        requestMap.put( _selected_username + "/" +UserDetails.get_friend_requests_DIR()+"/"+UserDetails.get_username() + "/"+UserDetails.get_friend_request_type_KEY(), UserDetails.get_friend_request__RECEIVED__VALUE());
        requestMap.put( _selected_username +"/"+UserDetails.get_notifications_DIR()+ "/" + newNotificationId, notificationData);

        UserDetails.get_db_users_ref().updateChildren(requestMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if(databaseError != null){

                    Toast.makeText(ProfileActivity.this, "There was some error in sending request\n"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ProfileActivity.this, "Friend request sent !", Toast.LENGTH_SHORT).show();
                     onSent_request_stat();  }


            }
        });
    }



    private void doOn_Unfriend_jugu(String _selected_username) {
        _profile_reject_friend_Button.setEnabled(false);
        _profile_accept_friend_Button.setEnabled(false);
        Map unfriendMap = new HashMap();
        unfriendMap.put(UserDetails.get_username()+"/" + UserDetails.get_friends_DIR() + "/" + _selected_username, null);
        unfriendMap.put(_selected_username+"/" + UserDetails.get_friends_DIR() + "/" + UserDetails.get_username(), null);

        UserDetails.get_db_users_ref().updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if(databaseError == null){
                    onNotFriend_stat();
                    Toast.makeText(ProfileActivity.this, "User removed from your friend list !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void do_On_Accept_request_jugu(final String _selected_username) {
        final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

        Map friendsMap = new HashMap();
        friendsMap.put( UserDetails.get_username()+"/"+ UserDetails.get_friends_DIR()+"/"+ _selected_username +       "/"+UserDetails.get_friends_DATE_KEY(), currentDate);
        friendsMap.put( _selected_username        +"/"+ UserDetails.get_friends_DIR()+"/"+ UserDetails.get_username()+"/"+UserDetails.get_friends_DATE_KEY(), currentDate);


        friendsMap.put(UserDetails.get_username()+"/"+UserDetails.get_friend_requests_DIR()+"/"+ _selected_username, null);
        friendsMap.put(_selected_username+"/"+UserDetails.get_friend_requests_DIR()+"/"+ UserDetails.get_username(), null);

        UserDetails.get_db_users_ref().updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                if(databaseError == null){
                    onAlready_friend_stat();
                    _profile_accept_friend_Button.setVisibility(View.GONE);
                    _profile_reject_friend_Button.setVisibility(View.GONE);
                    UserDetails.set_UserInfo_gold_amount((Integer.parseInt(UserDetails.get_UserInfo_data_from_STORAGE(UserDetails.get_UserInfo_gold_amount_KEY()))+10)+"",UserDetails.get_username());
                    UserDetails.set_UserInfo_gems_amount((Integer.parseInt(UserDetails.get_UserInfo_data_from_STORAGE(UserDetails.get_UserInfo_gems_amount_KEY()))+1)+"",UserDetails.get_username());
                    UserDetails.set_UserInfo_exp((Integer.parseInt(UserDetails.get_UserInfo_data_from_STORAGE(UserDetails.get_UserInfo_exp_KEY()))+5)+"",UserDetails.get_username());
                  //  UserDetails.set_UserInfo_friends_amount(UserDetails.get_username()+"");

                    UserDetails.set_UserInfo_gold_amount((Integer.parseInt(_hashMap_temp.get(UserDetails.get_UserInfo_gold_amount_KEY()))+10)+"",_selected_username);
                    UserDetails.set_UserInfo_gems_amount((Integer.parseInt(_hashMap_temp.get(UserDetails.get_UserInfo_gems_amount_KEY()))+1)+"",_selected_username);
                    UserDetails.set_UserInfo_exp((Integer.parseInt(_hashMap_temp.get(UserDetails.get_UserInfo_exp_KEY()))+5)+"",_selected_username);
                    UserDetails.set_UserInfo_friends_amount(_selected_username+"");
                    Toast.makeText(ProfileActivity.this, "Friend request Accepted !", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void onNotFriend_stat() {
        mCurrent_state = UserDetails.get_friend_request__NOT_FRIENDS__VALUE();
        final Drawable add_friend_drawable = getResources().getDrawable(R.drawable.add_user_48);
        mCurrent_state = UserDetails.get_friend_request__NOT_FRIENDS__VALUE();
        _profile_add_remove_friend_ImageView.setImageDrawable(add_friend_drawable);
        _profile_add_remove_textView.setText("Add");
        _profile_add_remove_textView.setTextColor(getResources().getColor(R.color.colorText));
        _profile_add_remove_friend_ImageView.setEnabled(true);
    }

    private void onAlready_friend_stat() {
        mCurrent_state = UserDetails.get_friend_request__FRIENDS__VALUE();
        final Drawable remove_friend_drawable = getResources().getDrawable(R.drawable.unfriend_user_32);
        _profile_add_remove_friend_ImageView.setImageDrawable(remove_friend_drawable);
        _profile_add_remove_textView.setText("Unfriend");
        _profile_add_remove_textView.setTextColor(getResources().getColor(R.color.color_red));
        _profile_add_remove_friend_ImageView.setEnabled(true);

    }

    private void onSent_request_stat() {
        mCurrent_state = UserDetails.get_friend_request__SENT__VALUE();
        final Drawable cancel_friend_request_drawable = getResources().getDrawable(R.drawable.cancel_request_32);
        _profile_add_remove_friend_ImageView.setImageDrawable(cancel_friend_request_drawable);
        _profile_add_remove_textView.setText("Cancel");
        _profile_add_remove_textView.setTextColor(getResources().getColor(R.color.color_orange));
        _profile_add_remove_friend_ImageView.setEnabled(true);
    }

    private void onReceived_request_stat() {
        mCurrent_state = UserDetails.get_friend_request__RECEIVED__VALUE();
        _profile_accept_friend_Button.setVisibility(View.VISIBLE);
        _profile_reject_friend_Button.setVisibility(View.VISIBLE);
        _profile_reject_friend_Button.setEnabled(true);
        _profile_accept_friend_Button.setEnabled(true);
        _profile_add_remove_friend_ImageView.setEnabled(false);
        _profile_add_remove_textView.setText("Received");
        _profile_add_remove_textView.setTextColor(getResources().getColor(R.color.colorText));

    }

    private void onCurrent_user_profile_stat() {
        final Drawable currentUser_add_remove_friend_drawable = getResources().getDrawable(R.drawable.current_user_32);
        activate_edit_mode_jugu();
        _profile_add_remove_friend_ImageView.setEnabled(false);
        _profile_add_remove_friend_ImageView.setImageDrawable(currentUser_add_remove_friend_drawable);
        _profile_add_remove_textView.setText("Me");
        _profile_add_remove_textView.setTextColor(getResources().getColor(R.color.colorText));
    }

    private void activate_edit_mode_jugu() {
        icon_edit_birthday.setVisibility(View.VISIBLE);
        icon_edit_job.setVisibility(View.VISIBLE);
        icon_edit_location.setVisibility(View.VISIBLE);
        icon_edit_nationality.setVisibility(View.VISIBLE);
        icon_edit_status.setVisibility(View.VISIBLE);
    }

    private void deactivate_edit_mode_jugu() {
        icon_edit_birthday.setVisibility(View.GONE);
        icon_edit_job.setVisibility(View.GONE);
        icon_edit_location.setVisibility(View.GONE);
        icon_edit_nationality.setVisibility(View.GONE);
        icon_edit_status.setVisibility(View.GONE);
    }


}
