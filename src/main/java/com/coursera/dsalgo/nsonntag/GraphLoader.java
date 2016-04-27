package com.coursera.dsalgo.nsonntag;

import com.coursera.dsalgo.nsonntag.api.Limits;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.User;

import java.io.*;
import java.util.*;

public class GraphLoader {

    //create list of GraphUsers from list of Twitter usernames in local file
    public static List<GraphUser> loadUsersFromFile(Twitter twitter, File file, boolean pacing) throws Exception {
        List<GraphUser> userList = new ArrayList<GraphUser>();
        Set<String> usernameSet = new HashSet<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));

        try {
            String line;
            while ((line = br.readLine()) != null) {
                String username = line.toLowerCase();

                if(usernameSet.contains(username))
                    continue;

                //get user followers
                System.out.println("Getting info for " + username + "...");
                User user = twitter.users().showUser(username);
                int userFollowers = user.getFollowersCount();

                //add GraphUser object to output list
                GraphUser graphUser = new GraphUser(username, userFollowers);
                userList.add(graphUser);

                //pacing
                if(pacing == true)
                    Limits.paceFor180();
            }
        }
        finally {
            if(br != null)
                br.close();
        }
        return userList;
    }

    //make Twitter API calls to determine relationships between all users in List of GraphUsers
    public static Graph graphFromListAPI(Twitter twitter, List<GraphUser> users, boolean pacing) throws Exception {
        Limits limits = new Limits(twitter);
        Map<GraphUser, Set<GraphUser>> userGraph = new HashMap<GraphUser, Set<GraphUser>>();
        Map<GraphUser, Set<GraphUser>> checked = new HashMap<GraphUser, Set<GraphUser>>();

        for(GraphUser userOuter : users){

            for(GraphUser userInner : users) {
                //skip if outer user same as inner user
                if(userOuter.equals(userInner))
                    continue;

                //create vertices for graph
                if(!userGraph.containsKey(userOuter))
                    userGraph.put(userOuter, new HashSet<GraphUser>());

                if(!userGraph.containsKey(userInner))
                    userGraph.put(userInner, new HashSet<GraphUser>());

                //create vertices for checked
                if(!checked.containsKey(userOuter))
                    checked.put(userOuter, new HashSet<GraphUser>());

                if(!checked.containsKey(userInner))
                    checked.put(userInner, new HashSet<GraphUser>());

                //skip if relationship already checked
                if(checked.get(userOuter).contains(userInner)) {
                    System.out.println("Skipping...Previously checked relationship between " + userOuter.username + " and " + userInner.username);
                    continue;
                }

                //check relationship
                System.out.println("Checking relationship betweeen " + userOuter.username +  " and " + userInner.username  + "...");
                Relationship rel = twitter.friendsFollowers().showFriendship(userOuter.username, userInner.username);

                //add relationships to graph
                if(rel.isSourceFollowedByTarget() == true)
                    userGraph.get(userOuter).add(userInner);

                if(rel.isSourceFollowingTarget() == true)
                    userGraph.get(userInner).add(userOuter);

                //add to checked
                checked.get(userOuter).add(userInner);
                checked.get(userInner).add(userOuter);

                //pacing
                if(pacing == true)
                    Limits.paceFor180();
                }
            }
        return new Graph(userGraph);
    }

    //write graph to local file
    public static void graphToFile(Graph graphObj, File file) throws Exception {
        Map<GraphUser, Set<GraphUser>> graph = graphObj.getGraph();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        try{
           for(GraphUser userOuter : graph.keySet()) {
               StringBuilder sb = new StringBuilder();
               sb.append(userOuter);
               sb.append("\t");

               Set<GraphUser> followerList = graph.get(userOuter);
               int followerListSize = followerList.size();

               if(followerListSize > 0) {
                   int i = 1;
                   for(GraphUser userInner : followerList){
                       sb.append(userInner);
                       if(i < followerListSize)
                           sb.append(" ");
                       i++;
                   }
               }
               sb.append("\n");
               bw.write(sb.toString());
           }
        }
        finally {
            if(bw != null)
                bw.close();
        }
    }

    //create graph from local file
    public static Graph graphFromFile(File file) throws Exception {
        Map<GraphUser, Set<GraphUser>> graph = new HashMap<GraphUser, Set<GraphUser>>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        JSONParser parser = new JSONParser();

        try {
            String line;
            while((line = br.readLine()) != null) {
                //create node
                String[] ne = line.split("\t");
                JSONObject nodeJson = (JSONObject)parser.parse(ne[0]);
                GraphUser node = new GraphUser(nodeJson.get("username").toString(), Integer.parseInt(nodeJson.get("followers").toString()));

                //create edges
                String[] edges = null;
                if (!graph.containsKey(node))
                    graph.put(node, new HashSet<GraphUser>());

                if(ne.length == 2) {
                    edges = ne[1].split(" ");
                    for (int i = 0; i < edges.length; i++) {

                        JSONObject edgeJson = (JSONObject)parser.parse(edges[i]);
                        GraphUser edge = new GraphUser(edgeJson.get("username").toString(), Integer.parseInt(edgeJson.get("followers").toString()));

                        graph.get(node).add(edge);
                    }
                }

            }
        }
        finally {
            if(br != null)
                br.close();
        }

        return new Graph(graph);
    }
}
