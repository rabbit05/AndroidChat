package com.example.androidchat;

import android.util.Log;
import android.util.Pair;

import androidx.core.util.Consumer;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private WebSocketClient client;
    private Map<Long, String> names = new ConcurrentHashMap<>();

    private Consumer<Pair<String, String>> onMessageReceived;

    public Server(Consumer<Pair<String, String>> onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }

    public void connect(){
        //35.214.1.221:
        URI address = null;
        try {
            address = new URI("ws://35.214.1.221:8881");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        client = new WebSocketClient(address) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("SERVER", "Connected to server");
                sendName("Меня зовут МАКС");
            }

            @Override
            public void onMessage(String json) {
                Log.i("SERVER", "Got json from server: " + json);
                int type = Protocol.getType(json);
                if(type == Protocol.MESSAGE){
                   displayIncoming(Protocol.unpackMessage(json));
                }

                if(type == Protocol.USER_STATUS){
                    updateStatus(Protocol.unpackStatus(json));
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("SERVER", "Connection closed");
            }

            @Override
            public void onError(Exception ex) {
                Log.i("SERVER", "ERROR: " + ex.getMessage());
            }
        };
        client.connect();
    }

    public void sendName(String name){
        Protocol.UserName userName = new Protocol.UserName(name);
        if(client != null && client.isOpen()) {
            client.send(Protocol.packName(userName));
        }
    }

    private void updateStatus(Protocol.UserStatus status){
        if(status.isConnected()){
            names.put(status.getUser().getId(), status.getUser().getName());
        } else{
            names.remove(status.getUser().getId());
        }
    }

    private void displayIncoming(Protocol.Message message){
        String name = names.get(message.getSender());
        if(name == null){
            name = "Unnamed";
        }
        onMessageReceived.accept(new Pair<>(name, message.getEncodedText()));
    }

    public void sendMessage(String text){
        Protocol.Message mess = new Protocol.Message(text);
        client.send(Protocol.packMessage(mess));

    }
}
