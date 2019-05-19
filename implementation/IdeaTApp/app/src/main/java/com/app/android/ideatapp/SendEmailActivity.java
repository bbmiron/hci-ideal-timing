package com.app.android.ideatapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class SendEmailActivity extends AppCompatActivity {

    private EditText from;
    private EditText to;
    private EditText subject;
    private EditText message;
    private Button sendButton;
    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_email_activity);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        sendButton = findViewById(R.id.send_button);
        email = getIntent().getStringExtra(MainActivity.EMAIL_TAG);
        from.setText(email);

    }
}
