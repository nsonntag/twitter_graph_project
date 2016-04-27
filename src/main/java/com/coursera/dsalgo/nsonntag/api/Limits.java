package com.coursera.dsalgo.nsonntag.api;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import java.util.Map;

public class Limits {

    private Twitter twitter;

    public Limits(Twitter twitter) throws Exception {
        this.twitter = twitter;
    }

    public Map<String , RateLimitStatus> getRateLimits() throws Exception {
        return twitter.getRateLimitStatus();
    }

    //get number of remaining requests for certain API endpoint
    public int getRemaining(String endpoint) throws Exception {
        RateLimitStatus rls = twitter.getRateLimitStatus().get(endpoint);
        return rls.getRemaining();
    }

    //pause execution for 5 seconds (180 requests every 15 minutes)
    public static void paceFor180() throws Exception{
        Thread.sleep(5001);
    }

    //pause execution until new API request quota has been issued
    public void pauseExecUntilReset(String endpoint) throws Exception {
        RateLimitStatus rls = twitter.getRateLimitStatus().get(endpoint);
        int limitRemaining = rls.getRemaining();
        System.out.println(endpoint + " - " + limitRemaining);
        int sleepTime = rls.getSecondsUntilReset() + 5; //add 5 extra seconds just in case
        if(limitRemaining == 0) {
            System.out.println("No more API " + endpoint + " requests.  Pausing execution " + sleepTime + " seconds...");
            Thread.sleep(sleepTime * 1000);
        }
        return;
    }
}
