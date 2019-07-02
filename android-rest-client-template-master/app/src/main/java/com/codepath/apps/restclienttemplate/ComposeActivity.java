package com.codepath.apps.restclienttemplate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {

    private Button bSend;
    private EditText etTweet;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        bSend = (Button) findViewById(R.id.btnSend);
        etTweet = (EditText) findViewById(R.id.etTweetInput);

        client = TwitterApp.getRestClient(this);

        setListeners();
    }

    private void setListeners() {
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageForUser;

                //get the message
                String message = etTweet.getText().toString();

                //if it is not empty
                if(! message.equals("")) {
                    Toast.makeText(ComposeActivity.this, "Tweet posted!", Toast.LENGTH_LONG).show();
                }

                //if it is empty
                else {
                    Toast.makeText(ComposeActivity.this, "Please write a tweet", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
