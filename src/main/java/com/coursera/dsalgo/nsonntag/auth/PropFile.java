package com.coursera.dsalgo.nsonntag.auth;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.*;
import java.util.Properties;

public class PropFile {

    //write local file containing Twitter authentication info
    public static void writePropFile(File propFile, String consumerKey, String consumerSecret, AccessToken accessToken) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(propFile));
        try {
            bw.write("#properties for Coursera Data Structures and Algorithms Capstone");
            bw.newLine();
            bw.write("consumer_key=" + consumerKey);
            bw.newLine();
            bw.write("consumer_secret=" + consumerSecret);
            bw.newLine();
            bw.write("access_token=" + accessToken.getToken());
            bw.newLine();
            bw.write("token_secret=" + accessToken.getTokenSecret());
        }
        finally {
            if(bw != null)
                bw.close();
        }
    }

    //read local file containing Twitter authentication info
    public static Properties readPropFile(File propFile) throws Exception {
        InputStream is = new FileInputStream(propFile);
        try {
            Properties prop = new Properties();
            prop.load(is);
            return prop;
        }
        finally {
            if (is != null)
                is.close();
        }
    }

    //get access token (interactive, follow instructions)
    public static AccessToken getAccessToken(File propFile) throws Exception {
        AccessToken accessToken = null;
        InputStream is = new FileInputStream(propFile);

        try {
            Properties prop = new Properties();
            prop.load(is);

            Twitter twitter = TwitterFactory.getSingleton();
            twitter.setOAuthConsumer(prop.getProperty("consumer_key"), prop.getProperty("consumer_secret"));
            RequestToken requestToken = twitter.getOAuthRequestToken();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
                System.out.println("Open the following URL and grant access to your account:");
                System.out.println(requestToken.getAuthorizationURL());
                System.out.print("Enter the PIN(if available) or just hit enter.[PIN]:");
                String pin = br.readLine();
                try{
                    if(pin.length() > 0){
                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    }else{
                        accessToken = twitter.getOAuthAccessToken();
                    }
                } catch (TwitterException te) {
                    if(401 == te.getStatusCode()){
                        System.out.println("Unable to get the access token.");
                    }else{
                        te.printStackTrace();
                    }
                }
            }
        }
        finally {
            if(is != null)
                is.close();
        }
        return accessToken;
    }

}
