package com.coursera.dsalgo.nsonntag.examples;

import com.coursera.dsalgo.nsonntag.Graph;
import com.coursera.dsalgo.nsonntag.GraphLoader;

import java.io.File;

public class GraphFromFile {
    //example of loading graph from local file
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Please provide the following argument");
            System.out.println("  0 - Input file containing list of Twitter graph");
            System.exit(1);
        }

        String inputFile = args[0];
        Graph graph = GraphLoader.graphFromFile(new File(inputFile));
    }
}
