package com.jugurtha_green.greenchat;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;


    private View mMainView;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_list);

        mFriendsDatabase = UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_friends_DIR());
        mFriendsDatabase.keepSynced(true);
        UserDetails.get_db_users_ref().keepSynced(true);


        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();

/*        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(

                Friends.class,
                R.layout.users_single_layout,
                FriendsViewHolder.class,
                mFriendsDatabase


        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder friendsViewHolder, Friends friends, int i) {

                friendsViewHolder.setDate(friends.getDate());

                final String list_user_id = getRef(i).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                        if(dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            friendsViewHolder.setUserOnline(userOnline);

                        }

                        friendsViewHolder.setDevice_Info_Map(userName);
                        friendsViewHolder.setUser_info_thumb_profile_image(userThumb, getContext());

                        friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //Click Event for each item.
                                        if(i == 0){

                                            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                            profileIntent.putExtra("user_id", list_user_id);
                                            startActivity(profileIntent);

                                        }

                                        if(i == 1){

                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                            chatIntent.putExtra("user_id", list_user_id);
                                            chatIntent.putExtra("user_name", userName);
                                            startActivity(chatIntent);

                                        }

                                    }
                                });

                                builder.show();

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };*/

        FirebaseRecyclerOptions<Users_RecyclerView> _adapter_options = new FirebaseRecyclerOptions.Builder<Users_RecyclerView>()
                .setQuery(mFriendsDatabase, Users_RecyclerView.class)
                .setLifecycleOwner(this)
                .build();
        FirebaseRecyclerAdapter<Users_RecyclerView, FriendsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users_RecyclerView, FriendsViewHolder>(_adapter_options) {


            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new FriendsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_single_layout, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int i, @NonNull Users_RecyclerView users_recyclerView_Class) {


                final String _seleceted_username = getRef(i).getKey();
                //       if (list_of_friends.length()+""!=UserDetails.get_UserInfo_value_jugu(UserDetails.get_UserInfo_friends_amount_KEY()+""))UserDetails.set_UserInfo_friends_amount(list_of_friends.length()+"");
                // holder.setDate("888");

                // Toast.makeText(getContext(), i+"_Key: "+list_of_friends, Toast.LENGTH_SHORT).show();
                UserDetails.get_db_users_ref().child(_seleceted_username).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                            final String userName = dataSnapshot.child(UserDetails.get_user_info_DIR()).child(UserDetails.get_UserInfo_name_KEY()).getValue() + "";
                            String userThumb = dataSnapshot.child(UserDetails.get_user_info_DIR()).child(UserDetails.get_profile_image_thumb_URL_KEY()).getValue() + "";
                            holder.set_UserInfo_STATUS(dataSnapshot.child(UserDetails.get_user_info_DIR()).child(UserDetails.get_UserInfo_status_KEY()).getValue() + "");

                            String userOnline = dataSnapshot.child(UserDetails.get_user_info_DIR()).child(UserDetails.get_UserInfo_online_stat_KEY()).getValue() + "";
                            holder.setUserOnline(userOnline);


                            holder.setName(userName);
                            holder.setUserImage(userThumb, getContext());

                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};

                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                    builder.setTitle("Select Options");
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            //Click Event for each item.
                                            if (i == 0) {

                                                Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                                profileIntent.putExtra(UserDetails.get_username_KEY(), _seleceted_username);
                                                startActivity(profileIntent);

                                            }

                                            if (i == 1) {

                                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                chatIntent.putExtra(UserDetails.get_username_KEY(), _seleceted_username);
                                                chatIntent.putExtra(UserDetails.get_UserInfo_name_KEY(), userName);
                                                startActivity(chatIntent);

                                            }

                                        }
                                    });

                                    builder.show();

                                }
                            });

                        }





                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };


        mFriendsList.setAdapter(firebaseRecyclerAdapter);


    }


    public class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDate(String date) {

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(date);

        }

        public void setName(String name) {

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx) {

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

                if (online_status.equals(UserDetails.get_online__TRUE__VALUE()+"")) {
                    txt_online.setText("Online");
                    txt_online.setTextColor(Color.parseColor("#0099ff"));
                    userOnlineView.setImageDrawable(getResources().getDrawable(R.drawable.online_32));
                    // userOnlineView.setVisibility(View.VISIBLE);

                } else {
                    txt_online.setText(UserDetails.get_time_ago_jugu(Long.parseLong(online_status)));

                }
            }//else activity is null


        }

        public void set_UserInfo_STATUS(String your_IMEI) {

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(your_IMEI);
        }


    }


}
