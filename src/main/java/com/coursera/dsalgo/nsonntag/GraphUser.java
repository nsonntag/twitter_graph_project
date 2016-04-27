package com.coursera.dsalgo.nsonntag;

import org.json.simple.JSONObject;

public class GraphUser {

    public final String username;
    public final int followers;

    public GraphUser(String username, int followers) {
        this.username  = username;
        this.followers = followers;
    }

    public GraphUser(String username) {
        this.username = username;
        this.followers = 0;
    }


    //don't include "follower" field in equals() or hashcode()
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(!(obj instanceof GraphUser))
            return false;

        GraphUser objUser = (GraphUser)obj;
        if(!this.username.equals(objUser.username))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + username.hashCode();
        return result;
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("username", username);
        obj.put("followers", followers);
        return obj.toJSONString();
    }

}
