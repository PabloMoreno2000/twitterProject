package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

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
                    sendTweet();
                }

                //if it is empty
                else {
                    Toast.makeText(ComposeActivity.this, "Please write a tweet", Toast.LENGTH_LONG).show();

                }
            }

        });
    }

    private void sendTweet() {
        client.sendTweet(etTweet.getText().toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        //parsing response
                        JSONObject response = new JSONObject(new String(responseBody));
                        Tweet resultTweet = Tweet.fromJSON(response);

                        //return result to calling activity
                        Intent resultData = new Intent();

                        resultData.putExtra("newTweet", Parcels.wrap(resultTweet));

                        startActivity(resultData);
                    } catch(JSONException e) {
                        Log.e("ComposeActivity", "Error parsing response", e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
