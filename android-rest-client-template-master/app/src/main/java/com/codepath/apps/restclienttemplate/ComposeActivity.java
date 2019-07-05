package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private Button bSend;
    private EditText etTweet;

    private TwitterClient client;

    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        bSend = (Button) findViewById(R.id.btnSend);
        ivProfile = (ImageView) findViewById(R.id.ivUser);
        etTweet = (EditText) findViewById(R.id.etTweetInput);
        client = TwitterApp.getRestClient(this);

        setListeners();


        Glide.with(this)
                .load(getResources().getIdentifier("profile", "drawable", this.getPackageName()))
                //.bitmapTransform(new RoundedCornersTransformation(this, 25, 0))
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfile);

    }


    private void populateUserData() {
        client.getCurrentUserObject("PabloMT2000", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("TwitterClient", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("TwitterClient", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
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
