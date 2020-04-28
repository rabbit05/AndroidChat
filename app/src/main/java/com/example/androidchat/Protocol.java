package com.example.androidchat;

import com.google.gson.Gson;

public class Protocol {
    //USER_STATUS - online/offline
    //MESSAGE - input/output
    //USER_NAME - name for server

    public final static int USER_STATUS = 1;
    public final static int MESSAGE = 2;
    public final static int USER_NAME = 3;

    // USER_NAME:    { name: "Max" }
    // USER_STATUS:  { connected: false, user: { name: "Stas", id: 1488 } }
    // MESSAGE:      { encodedText: "Salam popolam", sender: 1337 }


    static class UserStatus{
        private boolean connected;
        private User user;

        public UserStatus() {
        }

        public void setConnected(boolean connected) {
            this.connected = connected;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public boolean isConnected() {
            return connected;
        }

        public User getUser() {
            return user;
        }
    }

    static class User{
        private String name;
        private long id;

        public User() {
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public long getId() {
            return id;
        }
    }

    static class UserName {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public UserName(String name) {
            this.name = name;
        }
    }

    static class Message {
        final static int GROUP_CHAT = 1;
        private long sender;
        private String encodedText;
        private long receiver = GROUP_CHAT;

        public Message(String encodedText) {
            this.encodedText = encodedText;
        }

        public long getSender() {
            return sender;
        }

        public String getEncodedText() {
            return encodedText;
        }

        public long getReceiver() {
            return receiver;
        }

        public void setSender(long sender) {
            this.sender = sender;
        }

        public void setEncodedText(String encodedText) {
            this.encodedText = encodedText;
        }

        public void setReceiver(long receiver) {
            this.receiver = receiver;
        }
    }

    public static String packName(UserName name){
        Gson gson = new Gson();
        return USER_NAME + gson.toJson(name);
    }

    public static int getType(String json){
        if(json == null || json.length() == 0){
            return -1;
        }
        return Integer.parseInt(json.substring(0, 1));
    }

    public static String packMessage(Message mess){
        Gson gson = new Gson();
        return MESSAGE + gson.toJson(mess);
    }

    public static Message unpackMessage(String json){
        Gson g = new Gson();
        return g.fromJson(json.substring(1), Message.class);
    }

    public static UserStatus unpackStatus (String json){
        Gson g = new Gson();
        return g.fromJson(json.substring(1), UserStatus.class);
    }
}
