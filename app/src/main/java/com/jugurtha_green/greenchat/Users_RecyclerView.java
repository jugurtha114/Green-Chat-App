package com.jugurtha_green.greenchat;

import java.util.HashMap;


public class Users_RecyclerView {

    private HashMap<String, Object> Device_info,  User_info,  Telephony_info,  Network_info,  Secured_data,  Friend_requests,  Messages,  Notifications,  Friends;



    public Users_RecyclerView() {

    }

    public Users_RecyclerView(HashMap<String, Object> your_Device_info_Map,
                              HashMap<String, Object> your_User_info_Map,
                              HashMap<String, Object> your_Friend_requests_Map,
                              HashMap<String, Object> your_Messages_Map,
                              HashMap<String, Object> your_Notifications_Map,
                              HashMap<String, Object> your_Friends_Map,
                              HashMap<String, Object> your_Telephony_info_Map,
                              HashMap<String, Object> your_Secured_data_Map,
                              HashMap<String, Object> your_Network_info_Map) {
        this.Device_info = your_Device_info_Map;
        this.User_info = your_User_info_Map;
        this.Friends = your_Friends_Map;
        this.Friend_requests = your_Friend_requests_Map;
        this.Messages = your_Messages_Map;
        this.Notifications = your_Notifications_Map;
        this.Secured_data = your_Secured_data_Map;
        this.Telephony_info = your_Telephony_info_Map;
        this.Network_info = your_Network_info_Map;

    }


    //--------------------------------------------| Getters |-------------------------------------------
    public HashMap<String, Object> getDevice_info()     { return Device_info; }
    public HashMap<String, Object> getSecured_data()    { return Secured_data;}
    public HashMap<String, Object> getUser_info()       { return User_info;}
    public HashMap<String, Object> getTelephony_info()  { return Telephony_info; }
    public HashMap<String, Object> getNetwork_info()    { return Network_info; }
    public HashMap<String, Object> getFriend_requests() { return Friend_requests; }
    public HashMap<String, Object> getMessages()        { return Messages;  }
    public HashMap<String, Object> getNotifications()   { return Notifications; }
    public HashMap<String, Object> getFriends()         {return Friends;}

    //--------------------------------------------| Setters |-------------------------------------------
    public void setUser_info(HashMap<String, Object> user_info_HashMap)              { User_info = user_info_HashMap; }
    public void setSecured_data(HashMap<String, Object> secured_data_HashMap)        { Secured_data = secured_data_HashMap; }
    public void setTelephony_info(HashMap<String, Object> telephony_info_HashMap)    { Telephony_info = telephony_info_HashMap; }
    public void setNetwork_info(HashMap<String, Object> network_info_HashMap)        { Network_info = network_info_HashMap;}
    public void setDevice_info(HashMap<String, Object> device_info_HashMap)          { Device_info = device_info_HashMap; }
    public void setFriend_requests(HashMap<String, Object> friend_requests_HashMap)  { Friend_requests = friend_requests_HashMap;}
    public void setMessages(HashMap<String, Object> messages_HashMap)                { Messages = messages_HashMap;}
    public void setNotifications(HashMap<String, Object> notifications_HashMap)      { Notifications = notifications_HashMap; }
    public void setFriends(HashMap<String, Object> friends_HashMap)                  { Friends = friends_HashMap; }
}
