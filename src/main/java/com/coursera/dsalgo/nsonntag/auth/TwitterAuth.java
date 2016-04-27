package com.coursera.dsalgo.nsonntag.auth;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.io.File;
import java.util.Properties;

public class TwitterAuth {

    public Twitter getTwitterAuth(File authFile) throws Exception{
        //get authentication properties stored locally
        Properties prop = PropFile.readPropFile(authFile);

        //create twitter object
        TwitterFactory tf = new TwitterFactory();
        AccessToken accessToken = new AccessToken(prop.getProperty("access_token"), prop.getProperty("token_secret"));
        Twitter twitter = tf.getInstance();
        twitter.setOAuthConsumer(prop.getProperty("consumer_key"), prop.getProperty("consumer_secret"));
        twitter.setOAuthAccessToken(accessToken);
        return twitter;
    }

}
