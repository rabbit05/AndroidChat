package com.example.androidchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    RecyclerView chatWindow;
    Button sendMessage;
    EditText inputMessage;
    MessageController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatWindow = findViewById(R.id.chatWindow);
        sendMessage = findViewById(R.id.sendMessage);
        inputMessage = findViewById(R.id.inputMessage);
        controller = new MessageController();
        controller
                .setIncomingLayout(R.layout.message)
                .setOutgoingLayout(R.layout.message)
                .setMessageTextId(R.id.messageText)
                .setMessageTimeId(R.id.messageDate)
                .setUserNameId(R.id.userName)
                .appendTo(chatWindow, this);

        controller.addMessage(new MessageController.Message(
                "Hello, android developer",
                "Max",
                true));

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.addMessage(new MessageController.Message(inputMessage.getText().toString(),
                        "Max", true));
                inputMessage.setText("");
            }
        });
    }
}
