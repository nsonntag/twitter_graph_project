package com.coursera.dsalgo.nsonntag.examples;

import com.coursera.dsalgo.nsonntag.Graph;
import com.coursera.dsalgo.nsonntag.GraphLoader;
import com.coursera.dsalgo.nsonntag.GraphUser;

import java.io.File;
import java.util.Set;
import java.util.Stack;

public class CapstoneQuestions {

    //example of using classes to answer capstone questions
    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("Please provide the following argument");
            System.out.println("  0 - Input file containing list of Twitter graph");
            System.exit(1);
        }

        String inputFile = args[0];
        Graph graph = GraphLoader.graphFromFile(new File(inputFile));

        //single level mother node
        System.out.println("Checking for single level mother nodes.  These are Twitter users that are followed by all others in the graph.");
        System.out.println("Single level mother node exists? -> " + graph.hasMotherNode());
        System.out.println();

        //traditional style mother node
        System.out.println("Checking for traditional mother nodes.  These are Twitter users that have a path to all other nodes in the graph.");

        //create stack to use in dfsScan
        Stack<GraphUser> gStack = new Stack<GraphUser>();
        for(GraphUser user : graph.getGraph().keySet()) {
            gStack.push(user);
        }

        Stack<GraphUser> finished = graph.dfsScan(gStack);
        GraphUser user = finished.pop();
        System.out.println("Traditional mother node exists? -> " + graph.isMotherNodeExt(user));
        System.out.println();

        //dominating set
        System.out.println("Finding the dominating set.  The dominating set plus their neighbors make up all the nodes in the graph.");
        Set<GraphUser> domSet = graph.dominatingSet();
        System.out.println("Dominating set contains " + domSet.size() + " vertices");
        System.out.println();
        System.out.println("These vertices/users include:");
        for(GraphUser graphUser : domSet) {
            System.out.println(graphUser.username);
        }

    }
}
