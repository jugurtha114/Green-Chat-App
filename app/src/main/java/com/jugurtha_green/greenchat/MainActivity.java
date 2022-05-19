package com.jugurtha_green.greenchat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

   // private FirebaseAuth mAuth;
    private Toolbar mToolbar;
   // private String _username;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private DatabaseReference _user_info_ref;

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserDetails.setAppCONTEXT(getApplicationContext());

       if (!UserDetails.isAuthenticated_jugu() || UserDetails.get_username() == null || UserDetails.get_username().isEmpty()) {
            UserDetails.disconnect_jugu();
            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(startIntent);finish();

        }
        setContentView(R.layout.activity_main);
        //UserDetails.resync_hashMap_user_info();



        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);

        // getSupportActionBar().setTitle(UserDetails.get_Current_UID());


        //Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mViewPager.setOffscreenPageLimit(3);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
mTabLayout.setBackground(getDrawable(R.drawable._gradient_bg_black_transparent_90_from_top_border));

            mTabLayout.getTabAt(1).setIcon(R.drawable.add_user_gray_32);
            mTabLayout.getTabAt(0).setIcon(R.drawable.chat_48);
            mTabLayout.getTabAt(2).setIcon(R.drawable.friends_gray);



            mTabLayout.setInlineLabel(true);
            mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00ffff"));
           // mTabLayout.getTabAt(0).setIcon(R.drawable.chat_48);
            mTabLayout.setTabTextColors(Color.parseColor("#a8a8a8"),Color.parseColor("#00ff00"));

            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    if (tab.getPosition()==1){mTabLayout.getTabAt(1).setIcon(R.drawable.add_user_48);}
                    else {mTabLayout.getTabAt(1).setIcon(R.drawable.add_user_gray_32);}

                    if (tab.getPosition()==0){mTabLayout.getTabAt(0).setIcon(R.drawable.chat_48);}
                    else {mTabLayout.getTabAt(0).setIcon(R.drawable.chat_gray_32);}

                    if (tab.getPosition()==2){mTabLayout.getTabAt(2).setIcon(R.drawable.friends_lime);}
                    else {mTabLayout.getTabAt(2).setIcon(R.drawable.friends_gray);}

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });



    }




    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        UserDetails.get_db_users_ref().child(UserDetails.get_username()). child(UserDetails.get_user_info_DIR()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(UserDetails.get_UserInfo_device_token_KEY()))
                {UserDetails.get_db_users_ref().
                        child(UserDetails.get_username()).
                        child(UserDetails.get_user_info_DIR()).
                        child(UserDetails.get_UserInfo_device_token_KEY()).
                        setValue(UserDetails.get_Token_jugu());}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


            _user_info_ref = UserDetails.get_db_users_ref().child(UserDetails.get_username()).child(UserDetails.get_user_info_DIR());

            getSupportActionBar().setTitle(UserDetails.get_username());
            _user_info_ref.child(UserDetails.get_UserInfo_online_stat_KEY()).setValue("true");
           // _user_info_ref.keepSynced(true);
        }




    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();


        if (UserDetails.isAuthenticated_jugu()) {

            _user_info_ref.child(UserDetails.get_UserInfo_online_stat_KEY()).setValue(ServerValue.TIMESTAMP);

        }

    }

    private void sendToStart() {


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_logout_btn) {

            _user_info_ref.child(UserDetails.get_UserInfo_online_stat_KEY()).setValue(ServerValue.TIMESTAMP);

            UserDetails.disconnect_jugu();finish();

        }

        if (item.getItemId() == R.id.main_settings_btn) {

            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);

        }

        if (item.getItemId() == R.id.main_all_btn) {

            Intent settingsIntent = new Intent(MainActivity.this, UsersActivity.class);
            startActivity(settingsIntent);


        }

        return true;
    }



}
