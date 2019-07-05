package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.restclienttemplate.utils.TweetAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetAdapter tweetAdapter;
    private ArrayList<Tweet> tweets;
    private RecyclerView rvTweets;
    private ImageView ivImage;

    private Toolbar mToolbar;
    private SwipeRefreshLayout swipeContainer;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        ivImage = (ImageView) findViewById(R.id.ivUser);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        //init the arraylist
        tweets = new ArrayList<>();

        //construct the adapter from this arraylist
        tweetAdapter = new TweetAdapter(tweets);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        //Recyclerview setup(layout manager, use adapter)
        rvTweets.setLayoutManager(linearLayoutManager);

        //set the adapter
        rvTweets.setAdapter(tweetAdapter);



        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });

        //Configure refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //Triggered only when new data needs to be appended to the list
                //Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };

        //add the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);








    }

    public void loadNextDataFromApi(int page) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Checking the request code and result code
        if(resultCode == RESULT_OK && requestCode == 123) {
            Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("newTweet"));

            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
    }

    private void composeMessage() {
        Intent composeTweet = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(composeTweet, 123);
    }

    public void fetchTimelineAsync(int page) {

        showProgressBar();
        //Send request to fetch data
        //client here is an instance of Android Async HTTP
        //getHomeTimeline is an example endpoint
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //clean the adapter
                tweetAdapter.clear();

                //put the new items
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

                        Log.i("Refresh", "Added element " + i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                //Now we signal that the refresh has finished
                swipeContainer.setRefreshing(false);

                hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            public void onSuccess(JSONArray json) {

            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error :" + e.toString());
            }
        });
    }

/**
    private void getCurrentUserInfo() {
        client.getCurrentUserObject(123, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }*/

    private void populateTimeline() {

        showProgressBar();

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
                hideProgressBar();

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
                hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
                hideProgressBar();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
                hideProgressBar();
            }
        });
        //we should NOT put the hideProgressBar here, because the async programming stuff
    }

    // Instance of the progress action-view
    MenuItem miActionProgressItem;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        populateTimeline();
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }
}
