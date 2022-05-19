package com.jugurtha_green.greenchat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
//  import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String _selectd_user;
    private Toolbar mChatToolbar;


    private TextView mTitleView;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;

    private ImageView mChatAddBtn;
    private ImageView mChatSendBtn;
    private EditText _edit_text_message_View;

    private RecyclerView _recycleView_message_list;
    // private SwipeRefreshLayout mRefreshLayout;

    private final List<Messages> messagesArrayList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;

    private static final int TOTAL_ITEMS_TO_LOAD = 30;
    private int mCurrentPage = 1;

    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;


    //New Solution
    private int itemPos = 0;

    private String mLastKey = "";
    private String mPrevKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatToolbar = (Toolbar) findViewById(R.id.chat_app_bar);

        mChatToolbar.setContentInsetsAbsolute(0, 0);
        mChatToolbar.getContentInsetEnd();
        mChatToolbar.setPadding(0, 0, 0, 0);

        setSupportActionBar(mChatToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        _selectd_user = getIntent().getStringExtra(UserDetails.get_username_KEY());
        final String userName = getIntent().getStringExtra(UserDetails.get_UserInfo_name_KEY());

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chat_custom_bar, null);

        actionBar.setCustomView(action_bar_view);

        // ---- Custom Action bar Items ----

        mTitleView = (TextView) findViewById(R.id.custom_bar_title);
        mLastSeenView = (TextView) findViewById(R.id.custom_bar_seen);
        mProfileImage = (CircleImageView) findViewById(R.id.custom_bar_image);

        mChatAddBtn = (ImageView) findViewById(R.id.chat_add_btn);
        mChatSendBtn = (ImageView) findViewById(R.id.chat_send_btn);
        _edit_text_message_View = (EditText) findViewById(R.id.chat_message_view);

        mAdapter = new MessageAdapter(messagesArrayList);

        _recycleView_message_list = (RecyclerView) findViewById(R.id.messages_list);
        //  mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);
        mLinearLayout = new LinearLayoutManager(this);

        _recycleView_message_list.setHasFixedSize(true);
        _recycleView_message_list.setLayoutManager(mLinearLayout);

        _recycleView_message_list.setAdapter(mAdapter);

        UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_Chats_DIR()).child(_selectd_user).child(UserDetails.get_chat_seen_KEY()).setValue(UserDetails.is_seen__TRUE__VALUE());

        loadMessages();


        mTitleView.setText(userName);

        UserDetails.get_db_users_ref().child(_selectd_user).child(UserDetails.get_user_info_DIR()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String online = dataSnapshot.child(UserDetails.get_UserInfo_online_stat_KEY()).getValue() + "";
                String image = dataSnapshot.child(UserDetails.get_UserInfo_profile_image_URl_KEY()).getValue() + "";

                if (online.equals(UserDetails.get_online__TRUE__VALUE()+"")) {

                    mLastSeenView.setText("Online");

                } else {

                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    if (online.equals(UserDetails.get_online__TRUE__VALUE() + "")) {
                        mLastSeenView.setText("Online");
                    } else {
                        long lastTime = Long.parseLong(online);

                        String lastSeenTime = UserDetails.get_time_ago_jugu(lastTime);

                        mLastSeenView.setText(lastSeenTime);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_Chats_DIR()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(_selectd_user)) {

                    Map chatAddMap = new HashMap();
                    chatAddMap.put(UserDetails.get_chat_seen_KEY(), false);
                    chatAddMap.put(UserDetails.get_chat_TimeStamp_KEY(), ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put(UserDetails.get_username() + "/" + UserDetails.get_Chats_DIR() + "/" + _selectd_user, chatAddMap);
                    chatUserMap.put(_selectd_user + "/" + UserDetails.get_Chats_DIR() + "/" + UserDetails.get_username(), chatAddMap);

                    UserDetails.get_db_users_ref().updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError != null) {

                                Log.d("CHAT_LOG", databaseError.getMessage().toString());

                            }

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   MessageAdapter.isCurrent_User=true;
                sendMessage();


            }
        });


        mChatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });



       /* mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCurrentPage++;

                itemPos = 0;

                loadMoreMessages();


            }
        });*/


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            final String _db_curren_user_msg_ref = UserDetails.get_username() + UserDetails.get_messages_DIR() + "/" + _selectd_user;
            final String _db_selected_user_msg_ref = _selectd_user + UserDetails.get_messages_DIR() + "/" + UserDetails.get_username();

            //   DatabaseReference user_message_push =UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_messages_DIR()).child(_selectd_user).push();

            final String push_id = UserDetails.get_username() + "_" + _selectd_user + "_" + UserDetails.getCurrent_Time();


            StorageReference filepath = mImageStorage.child(UserDetails.get_username()).child(UserDetails.get_message_IMAGE_DIR()).child(push_id + ".jpg");

            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        //String download_url = task.getResult().getDownloadUrl().toString();
                        String download_url = "https://scontent-mad1-1.xx.fbcdn.net/v/t1.0-0/cp0/e15/q65/p320x320/37770200_681882985479238_6448686725412683776_n.jpg?_nc_cat=0&efg=eyJpIjoiYiJ9&oh=a088402ab8eb0beba7a793a9bbcb945c&oe=5C02CE27";

                        Map messageMap = new HashMap();
                        messageMap.put(UserDetails.get_message_MESSAGE_KEY(), download_url);
                        messageMap.put(UserDetails.get_message_SEEN_KEY(), false);
                        messageMap.put(UserDetails.get_message_TYPE_KEY(), UserDetails.get_message__TYPE_Image___VALUE());
                        messageMap.put(UserDetails.get_message_TimeStamp_KEY(), ServerValue.TIMESTAMP);
                        messageMap.put(UserDetails.get_message_FROM_KEY(), UserDetails.get_username());

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(_db_curren_user_msg_ref + "/" + push_id, messageMap);
                        messageUserMap.put(_db_selected_user_msg_ref + "/" + push_id, messageMap);

                        _edit_text_message_View.setText("");

                        UserDetails.get_db_users_ref().updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if (databaseError != null) {

                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                }

                            }
                        });


                    }

                }
            });

        }

    }

    private void loadMoreMessages() {

        DatabaseReference messageRef = UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_messages_DIR()).child(_selectd_user);

        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Messages message = (Messages) dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();

                if (!mPrevKey.equals(messageKey)) {

                    messagesArrayList.add(itemPos++, message);

                } else {

                    mPrevKey = mLastKey;

                }


                if (itemPos == 1) {

                    mLastKey = messageKey;

                }


                Log.d("TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey);

                mAdapter.notifyDataSetChanged();

                //   mRefreshLayout.setRefreshing(false);

                mLinearLayout.scrollToPositionWithOffset(10, 0);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadMessages() {

        DatabaseReference messageRef = UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_messages_DIR()).child(_selectd_user);

        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD).orderByChild(UserDetails.get_message_TimeStamp_KEY());


        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = (Messages) dataSnapshot.getValue(Messages.class);
                //         Users_RecyclerView message = dataSnapshot.getValue();

                itemPos++;

                if (itemPos == 1) {

                    String messageKey = dataSnapshot.getKey();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;

                }

                messagesArrayList.add(message);
                mAdapter.notifyDataSetChanged();

                _recycleView_message_list.scrollToPosition(messagesArrayList.size() - 1);

                //    mRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage() {


        String message = _edit_text_message_View.getText().toString();

        if (!TextUtils.isEmpty(message)) {
            final String _db_curren_user_msg_ref = UserDetails.get_username() + "/" + UserDetails.get_messages_DIR() + "/" + _selectd_user;
            final String _db_selected_user_msg_ref = _selectd_user + "/" + UserDetails.get_messages_DIR() + "/" + UserDetails.get_username();

//            DatabaseReference user_message_push = mRootRef.child("messages")
//                    .child(mCurrentUserId).child(_selectd_user).push();

            String push_id = UserDetails.get_username() + "_" + _selectd_user + "_" + UserDetails.getCurrent_Time();

            Map messageMap = new HashMap();
            messageMap.put(UserDetails.get_message_MESSAGE_KEY(), message);
            messageMap.put(UserDetails.get_message_SEEN_KEY(), false);
            messageMap.put(UserDetails.get_message_TYPE_KEY(), UserDetails.get_message__TYPE_Text___VALUE());
            messageMap.put(UserDetails.get_message_TimeStamp_KEY(), ServerValue.TIMESTAMP);
            messageMap.put(UserDetails.get_message_FROM_KEY(), UserDetails.get_username());
//            messageMap.put("message", message);
//            messageMap.put("seen", false);
//            messageMap.put("type", "text");
//            messageMap.put("time", ServerValue.TIMESTAMP);
//            messageMap.put("from", mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(_db_curren_user_msg_ref + "/" + push_id, messageMap);
            messageUserMap.put(_db_selected_user_msg_ref + "/" + push_id, messageMap);

            _edit_text_message_View.setText("");

            UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_Chats_DIR()).child(_selectd_user).child(UserDetails.get_chat_seen_KEY()).setValue(UserDetails.is_seen__TRUE__VALUE());
            UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_Chats_DIR()).child(_selectd_user).child(UserDetails.get_chat_TimeStamp_KEY()).setValue(UserDetails.getCurrent_Time());

            UserDetails.get_db_users_ref().child(_selectd_user).child(UserDetails.get_Chats_DIR()).child(UserDetails.get_username()).child(UserDetails.get_chat_seen_KEY()).setValue(UserDetails.is_seen__FALSE__VALUE());
            UserDetails.get_db_users_ref().child(_selectd_user).child(UserDetails.get_Chats_DIR()).child(UserDetails.get_username()).child(UserDetails.get_chat_TimeStamp_KEY()).setValue(UserDetails.getCurrent_Time());

            UserDetails.get_db_users_ref().updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if (databaseError != null) {

                        Log.d("CHAT_LOG", databaseError.getMessage()+"");

                    }

                }
            });

        }
    }

}
