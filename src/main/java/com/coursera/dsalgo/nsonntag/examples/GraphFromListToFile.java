package com.coursera.dsalgo.nsonntag.examples;

import com.coursera.dsalgo.nsonntag.Graph;
import com.coursera.dsalgo.nsonntag.GraphLoader;
import com.coursera.dsalgo.nsonntag.GraphUser;
import com.coursera.dsalgo.nsonntag.api.Limits;
import com.coursera.dsalgo.nsonntag.auth.TwitterAuth;
import twitter4j.Twitter;

import java.io.File;
import java.util.List;

public class GraphFromListToFile {

    //example of using list of Twitter usernames to generate graph by making API calls
    //example of writing graph to file
    public static void main(String[] args) throws Exception {
        if(args.length < 3) {
            System.out.println("Please provide these three arguments");
            System.out.println("  0 - Input file containing list of Twitter users");
            System.out.println("  1 - Output file to store graph");
            System.out.println("  2 - Authorization file containing needed Twitter keys");
            System.exit(1);
        }

        String graphInputFile = args[0];
        String graphOutputFile = args[1];
        String twitterAuthFile = args[2];

        //twitter object
        TwitterAuth ta = new TwitterAuth();
        Twitter twitter = ta.getTwitterAuth(new File(twitterAuthFile));

        //limit object
        Limits limits = new Limits(twitter);

        //load list of Twitter users from file
        File inputFile = new File(graphInputFile);
        List<GraphUser> s = GraphLoader.loadUsersFromFile(twitter, inputFile, true);

        //create graph making Twitter API calls
        Graph graph = GraphLoader.graphFromListAPI(twitter, s, true);

        //write graph to file
        GraphLoader.graphToFile(graph, new File(graphOutputFile));

    }
}
