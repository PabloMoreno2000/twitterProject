package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Tweet {

    //attributes
    public String body;
    public long uid; //database ID for the tweet
    public String createdAt;
    public User user;

    public Entity entity;
    public boolean hasEntitites;

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.hasEntitites = false;

        //extract all the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");

        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        JSONObject entityObject = jsonObject.getJSONObject("entities");

        if(entityObject.has("media")) {
            JSONArray mediaEndpoint = entityObject.getJSONArray("media");

            if(mediaEndpoint != null && mediaEndpoint.length() != 0) {
                tweet.entity = Entity.fromJSON(jsonObject.getJSONObject("entities"));
                tweet.hasEntitites = true;

            }
        }
        //tweet.entity = Entity.fromJSON();


        return tweet;
    }
}
