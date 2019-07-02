package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.TweetAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetAdapter tweetAdapter;
    private ArrayList<Tweet> tweets;
    private RecyclerView rvTweets;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        //init the arraylist
        tweets = new ArrayList<>();

        //construct the adapter from this arraylist
        tweetAdapter = new TweetAdapter(tweets);

        //Recyclerview setup(layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        //set the adapter
        rvTweets.setAdapter(tweetAdapter);



        populateTimeline();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();

        mMenuInflater.inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.newTweet:
                composeMessage();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void composeMessage() {
        Intent composeTweet = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(composeTweet, 123);
    }


    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());

                //iterate through the JSON array
                for(int i = 0; i < response.length(); i++) {
                    //for each entry, deserialize the JSON object
                    //Convert each object to a tweet model
                    //add that Tweet model to our data source(array list)
                    //notify the adapter we have added an item

                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
