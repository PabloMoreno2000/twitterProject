package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;


public class Tweet {

    //attributes
    public String body;
    public long uid; //database ID for the tweet
    public String createdAt;
    public User user;

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //extract all the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");

        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }
}
