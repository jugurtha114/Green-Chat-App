package com.jugurtha_green.greenchat;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView mConvList;
    private TextView txt_status;
    private DatabaseReference _db_Chats_ref;
    private DatabaseReference mMessageDatabase;

    private View mMainView;


    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_chats, container, false);

        mConvList = (RecyclerView) mMainView.findViewById(R.id.conv_list);

        _db_Chats_ref = UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_Chats_DIR());

        _db_Chats_ref.keepSynced(true);
        mMessageDatabase = UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_messages_DIR());
        mMessageDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);


        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();

        Query conversationQuery = _db_Chats_ref.orderByChild(UserDetails.get_chat_TimeStamp_KEY());

/*        FirebaseRecyclerAdapter<Conv, ConvViewHolder> firebaseConvAdapter = new FirebaseRecyclerAdapter<Conv, ConvViewHolder>(
                Conv.class,
                R.layout.users_single_layout,
                ConvViewHolder.class,
                conversationQuery
        ) {
            @Override
            protected void populateViewHolder(final ConvViewHolder convViewHolder, final Conv conv, int i) {



                final String list_user_id = getRef(i).getKey();

                Query lastMessageQuery = mMessageDatabase.child(list_user_id).limitToLast(1);

                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String data = dataSnapshot.child("message").getValue().toString();
                        convViewHolder.setMessage(data, conv.isSeen());

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


                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                        if(dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            convViewHolder.setUserOnline(userOnline);

                        }

                        convViewHolder.setDevice_Info_Map(userName);
                        convViewHolder.setUser_info_thumb_profile_image(userThumb, getContext());

                        convViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra("user_id", list_user_id);
                                chatIntent.putExtra("user_name", userName);
                                startActivity(chatIntent);

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };*/

        FirebaseRecyclerOptions<Conv> _conv_options =new FirebaseRecyclerOptions.Builder<Conv>()
                .setQuery(conversationQuery,Conv.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<Conv, ConvViewHolder> firebaseConvAdapter = new FirebaseRecyclerAdapter<Conv, ConvViewHolder>(_conv_options) {
            @NonNull
            @Override
            public ConvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return  new ConvViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_single_layout, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull final ConvViewHolder convViewHolder, int i, @NonNull final Conv model) {



                final String _selected_user = getRef(i).getKey();

                Query lastMessageQuery = mMessageDatabase.child(_selected_user).limitToLast(1);

                lastMessageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        //Toast.makeText(UserDetails.getAppCONTEXT(), "message last: "+dataSnapshot , Toast.LENGTH_LONG).show();

                        convViewHolder.setMessage(dataSnapshot, model.isSeen());

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


                UserDetails.get_db_users_ref().child(_selected_user).child(UserDetails.get_user_info_DIR()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String _display_name = dataSnapshot.child(UserDetails.get_UserInfo_name_KEY()).getValue()+"";
                        String userThumb = dataSnapshot.child(UserDetails.get_profile_image_thumb_URL_KEY()).getValue()+"";

                        if(dataSnapshot.hasChild(UserDetails.get_UserInfo_online_stat_KEY())) {

                            String userOnline = dataSnapshot.child(UserDetails.get_UserInfo_online_stat_KEY()).getValue()+"";
                            convViewHolder.setUserOnline(userOnline);

                        }

                        convViewHolder.setName(_display_name);
                        convViewHolder.setUserImage(userThumb, getContext());

                        convViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                chatIntent.putExtra(UserDetails.get_username_KEY(), _selected_user);
                                chatIntent.putExtra(UserDetails.get_UserInfo_name_KEY(), _display_name);
                                startActivity(chatIntent);

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        };

        mConvList.setAdapter(firebaseConvAdapter);

    }

    public class ConvViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ConvViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setMessage(DataSnapshot dataSnapshot, boolean isSeen){
            String message = dataSnapshot.child(UserDetails.get_message_MESSAGE_KEY()).getValue()+"";
           // Toast.makeText(UserDetails.getAppCONTEXT(), "message last: "+message , Toast.LENGTH_LONG).show();
            String last_time = UserDetails.get_time_ago_jugu(Long.parseLong(dataSnapshot.child(UserDetails.get_message_TimeStamp_KEY()).getValue()+""));
             txt_status = mView.findViewById(R.id.user_single_status);
            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
             if (dataSnapshot.child(UserDetails.get_message_TYPE_KEY()).getValue().equals(UserDetails.get_message__TYPE_Image___VALUE())){
                    message="You have received a picture !"; }


            SpannableStringBuilder builder = new SpannableStringBuilder();

            SpannableString spanner_last_time= new SpannableString(last_time);
            spanner_last_time.setSpan(new RelativeSizeSpan(0.8f), 0, spanner_last_time.length(), 0);
            spanner_last_time.setSpan(new ForegroundColorSpan(Color.parseColor("#99ffff00")), 0, spanner_last_time.length(), 0);

           // SpannableString spanner_close_the_parentheses= new SpannableString(" ◀️\n\n");

            if(isSeen){

                SpannableString spanner_last_message= new SpannableString("✔️ ️ Last message  ▶ ");
                spanner_last_message.setSpan(new RelativeSizeSpan(0.8f), 0, spanner_last_message.length(), 0);
                spanner_last_message.setSpan(new ForegroundColorSpan(Color.parseColor("#dd77aadd")), 0, spanner_last_message.length(), 0);
                builder.append(spanner_last_message);



               // spanner_last_time.setSpan(new ImageSpan(getContext(),R.drawable.time_gray_32), 0, spanner_last_time.length(), 0);
                builder.append(spanner_last_time);


                SpannableString spanner_close_the_parentheses= new SpannableString(" ◀️\n");
                spanner_close_the_parentheses.setSpan(new RelativeSizeSpan(0.8f), 0, spanner_close_the_parentheses.length(), 0);
                spanner_close_the_parentheses.setSpan(new ForegroundColorSpan(Color.parseColor("#dd77aadd")), 0, spanner_close_the_parentheses.length(), 0);
                builder.append(spanner_close_the_parentheses);

                builder.append(message);

                userStatusView.setText( builder, TextView.BufferType.SPANNABLE);


            } else {

                txt_status.setBackground(getResources().getDrawable(R.drawable._gradient_bg_blue__border_only));
                txt_status.setAlpha(1f);
                SpannableString spanner_new_message= new SpannableString("⚠️️ New message✨  ▶ ");

                spanner_new_message.setSpan(new RelativeSizeSpan(0.8f), 0, spanner_new_message.length(), 0);
                spanner_new_message.setSpan(new ForegroundColorSpan(Color.parseColor("#dd00ffff")), 0, spanner_new_message.length(), 0);
                builder.append(spanner_new_message);

                builder.append(spanner_last_time);

                SpannableString spanner_close_the_parentheses= new SpannableString(" ◀️\n");
                spanner_close_the_parentheses.setSpan(new RelativeSizeSpan(0.8f), 0, spanner_close_the_parentheses.length(), 0);
                spanner_close_the_parentheses.setSpan(new ForegroundColorSpan(Color.parseColor("#dd00ffff")), 0, spanner_close_the_parentheses.length(), 0);
                builder.append(spanner_close_the_parentheses);

                SpannableString spanner_message= new SpannableString(message);
                spanner_message.setSpan(new ForegroundColorSpan(Color.parseColor("#dd00aaff")), 0, spanner_message.length(), 0);

                builder.append(spanner_message);
                userStatusView.setText( builder, TextView.BufferType.SPANNABLE);


            }



        }

        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.get(/*ctx*/).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

        }

        public void setUserOnline(String online_status) {
            Activity activity = getActivity();
            if (isAdded() && activity != null) {
           TextView txt_online = (TextView) mView.findViewById(R.id.single_user_exp);
                ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_medal);
                txt_online.setText("Offline");
                txt_online.setTextColor(Color.parseColor("#aaaaaa"));
                userOnlineView.setImageDrawable(getResources().getDrawable(R.drawable.time_gray_32));

                if(online_status.equals(UserDetails.get_online__TRUE__VALUE()+"")){
                    txt_online.setText("Online");
                    txt_online.setTextColor(Color.parseColor("#00bbff"));
                    //txt_status.setAlpha(1.0f);
                    userOnlineView.setImageDrawable(getResources().getDrawable(R.drawable.online_32));
                    // userOnlineView.setVisibility(View.VISIBLE);

                } else {
                    txt_online.setText(UserDetails.get_time_ago_jugu(Long.parseLong(online_status)));

                }
            }//else activity is null


        }


    }



}
