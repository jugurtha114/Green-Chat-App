package com.jugurtha_green.greenchat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mUsersList;


    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar = (Toolbar) findViewById(R.id.users_appBar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("All Users_RecyclerView");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mLayoutManager = new LinearLayoutManager(this);

        mUsersList = (RecyclerView) findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(mLayoutManager);
        mUsersList.setItemViewCacheSize(25);
        mUsersList.setDrawingCacheEnabled(true);
        mUsersList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }

    @Override
    protected void onStart() {
        super.onStart();
/*
        FirebaseRecyclerAdapter<Users_RecyclerView, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users_RecyclerView, UsersViewHolder>(

                Users_RecyclerView.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mUsersDatabase

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, Users_RecyclerView users, int position) {

                usersViewHolder.setUser_info_name(users.getDevice_info());
                usersViewHolder.setTelephony_info_IMEI(users.getTelephony_info());
                usersViewHolder.setUser_info_thumb_profile_image(users.getNetwork_info(), getApplicationContext());

                final String user_id = getRef(position).getKey();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });

            }
        };


        mUsersList.setAdapter(firebaseRecyclerAdapter);*/

        FirebaseRecyclerOptions<Users_RecyclerView> options= new FirebaseRecyclerOptions.Builder<Users_RecyclerView>()
                        .setQuery(UserDetails.get_db_users_ref(), Users_RecyclerView.class)
                        .setLifecycleOwner(this)
                        .build();


        FirebaseRecyclerAdapter<Users_RecyclerView, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users_RecyclerView, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder users_ViewHolder, int position, @NonNull final Users_RecyclerView Users_RecyclerView_Class) {
/*                    if (Users_RecyclerView_Class.getUser_info()==null ||
                        Users_RecyclerView_Class.getTelephony_info()==null ||
                        Users_RecyclerView_Class.getNetwork_info()==null ||
                        Users_RecyclerView_Class.getSecured_data()==null ||
                        Users_RecyclerView_Class.getDevice_info()==null)
                    { Toast.makeText(UsersActivity.this, "Please check your Internet connection and try again", Toast.LENGTH_SHORT).show();finish();}*/

                users_ViewHolder.setUser_info_name(Users_RecyclerView_Class.getUser_info().get(UserDetails.get_UserInfo_name_KEY())+"");
                users_ViewHolder.set_UserInfo_STATUS(Users_RecyclerView_Class.getUser_info().get(UserDetails.get_UserInfo_status_KEY())+"");
                users_ViewHolder.setUser_Info_EXP(Users_RecyclerView_Class.getUser_info().get(UserDetails.get_UserInfo_exp_KEY())+"");
 //               users_ViewHolder.setUser_info_thumb_profile_image(Users_RecyclerView_Class.getNetwork_info().get(UserDetails.get_profile_image_thumb_URL_KEY())+"",getApplicationContext());



                final String _selected_username=getRef(position).getKey();
                users_ViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent9=new Intent(UsersActivity.this,ProfileActivity.class);
                        intent9.putExtra(UserDetails.get_username_KEY(),_selected_username);
                        startActivity(intent9);
                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new UsersViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.users_single_layout, viewGroup, false));
            }
        };


        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setUser_info_name(String your_Device_Brand){

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(your_Device_Brand);

        }

        public void set_UserInfo_STATUS(String your_IMEI){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(your_IMEI);
        }

        public void setUser_Info_EXP(String your_EXP){

            TextView userStatusView = (TextView) mView.findViewById(R.id.single_user_exp);
            userStatusView.setText(your_EXP);
        }

        public void setUser_info_thumb_profile_image(String thumb_image, Context ctx){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);

            Picasso.get().load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

        }


    }

}
