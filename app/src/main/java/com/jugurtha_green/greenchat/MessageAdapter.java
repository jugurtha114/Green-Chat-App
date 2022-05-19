package com.jugurtha_green.greenchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.ref.PhantomReference;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

private boolean m=false;
    private List<Messages> mMessageList;

    private DatabaseReference mUserDatabase;
  //  public static boolean isCurrent_User;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(m) LayoutInflater.from(parent.getContext()).inflate(R.layout.message_data_separator ,parent, false);
        if (viewType==1) return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout_current_user ,parent, false));
       // if (viewType==2) return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_data_separator ,parent, false));
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout ,parent, false));
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
         public TextView _txt_date_separator=null;
        public final TextView _txt_time_spent,_txt_last_time;
        final  public TextView _txt_message;
        final public CircleImageView _img_circle_profile_image;
        final public TextView _txt_display_name;
        final public ImageView _img_message_image;

        public MessageViewHolder(View view) {
            super(view);

            _txt_message = (TextView) view.findViewById(R.id.message_text_layout);
            _img_circle_profile_image = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            _txt_display_name = (TextView) view.findViewById(R.id.name_text_layout);
            _txt_time_spent = (TextView) view.findViewById(R.id.txt_time_spent);
            _txt_last_time = (TextView) view.findViewById(R.id.txt_last_time);
            _img_message_image = (ImageView) view.findViewById(R.id.message_image_layout);
            if (m){
            _txt_date_separator = (TextView) view.findViewById(R.id.txt_date_separator);
            _txt_date_separator.setVisibility(View.GONE);}

        }
    }


/*    public class Date_Separator_ViewHolder extends RecyclerView.ViewHolder {
        final public TextView _txt_date_separator;
        public Date_Separator_ViewHolder(View view) {
            super(view);
            _txt_date_separator = (TextView) view.findViewById(R.id.txt_date_separator);
        }
    }*/




    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
if (/*viewHolder.getItemViewType()*/ m){
    final Messages c = mMessageList.get(i);
    final String d = "--------------| "+new Date(c.getTimestamp()* 1000L).toString().substring(0,11)+" |--------------";
    viewHolder._txt_date_separator.setVisibility(View.VISIBLE);
    viewHolder._txt_date_separator.setText(d);m=false;
}
        final Messages c = mMessageList.get(i);
       // Toast.makeText(UserDetails.getAppCONTEXT(), UserDetails.get_time_ago_jugu(c.getTimestamp()), Toast.LENGTH_SHORT).show();
        final String _last_time = UserDetails.get_time_ago_jugu(c.getTimestamp());
        final String _selected_user_sender = c.getFrom();
        final String message_type = c.getType();
        if (UserDetails.get_username().equals(_selected_user_sender+"")){
                // i want to change the other xml "R.layout.message_single_layout_current_user" here (v2)

        }

        mUserDatabase = UserDetails.get_db_users_ref().child(_selected_user_sender).child(UserDetails.get_user_info_DIR());
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  Toast.makeText(UserDetails.getAppCONTEXT(), dataSnapshot+"", Toast.LENGTH_SHORT).show();
               final String name = dataSnapshot.child(UserDetails.get_UserInfo_name_KEY()).getValue()+"";

               final String image = dataSnapshot.child(UserDetails.get_profile_image_thumb_URL_KEY()).getValue()+"";


                viewHolder._txt_display_name.setText(name);
               /* long time=UserDetails.getCurrent_Time();

                if(!_last_time.equals("true") && !_last_time.equals("null")){ viewHolder._txt_last_time.setText("Loading...");}else time=Long.parseLong(_last_time);


                    viewHolder._txt_last_time.setText(UserDetails.get_time_ago_jugu(time));
*/
               final String dd="at "+new Date(c.getTimestamp()* 1000L).toString().substring(10,19);
             //  String exact_date=dd.getDay()+"/"+dd.getMonth()+"/"+dd.getYear()+" at "+dd.getHours()+":"+dd.getMinutes()+":("+dd.getSeconds()+")";
                viewHolder._txt_time_spent.setText(dd);
                viewHolder._txt_last_time.setText(_last_time);
                Picasso.get(/*viewHolder._img_circle_profile_image.getContext()*/).load(image).placeholder(R.drawable.default_avatar).into(viewHolder._img_circle_profile_image);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals(UserDetails.get_message__TYPE_Text___VALUE())) {

            viewHolder._txt_message.setText(c.getMessage());
            viewHolder._img_message_image.setVisibility(View.INVISIBLE);


        } else {

            viewHolder._txt_message.setVisibility(View.INVISIBLE);
            Picasso.get(/*viewHolder._img_circle_profile_image.getContext()*/).load(c.getMessage())
                    .placeholder(R.drawable.default_avatar).into(viewHolder._img_message_image);

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }



    @Override
    public int getItemViewType(int position) {



        if (mMessageList.get(position).getFrom().equals(UserDetails.get_username())) {
            if (position>=1 && !getOnlyDate_jugu(position).equals(getOnlyDate_jugu(position-1))){ m=false;}
            return 1; }

        else   {if (position>=1 && !getOnlyDate_jugu(position).equals(getOnlyDate_jugu(position-1))){ m=false;}
            return 0;}}

private String getOnlyDate_jugu(int pos){return new Date(mMessageList.get(pos).getTimestamp()* 1000L).toString().substring(0,11);}
}
