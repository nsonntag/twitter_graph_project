package com.coursera.dsalgo.nsonntag;

import org.junit.Test;
import org.junit.Assert;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class GraphTest {

    String sampleGraphFilename1;
    Graph graphObj1;
    Map<GraphUser, Set<GraphUser>> graph1;

    String sampleGraphFilename2;
    Graph graphObj2;
    Map<GraphUser, Set<GraphUser>> graph2;

    public GraphTest() throws Exception {
        this.sampleGraphFilename1 = this.getClass().getClassLoader().getResource("graphs/sample_users_graph_1.txt").getFile();
        this.graphObj1 = GraphLoader.graphFromFile(new File(sampleGraphFilename1));
        this.graph1 = graphObj1.getGraph();

        this.sampleGraphFilename2 = this.getClass().getClassLoader().getResource("graphs/sample_users_graph_2.txt").getFile();
        this.graphObj2 = GraphLoader.graphFromFile(new File(sampleGraphFilename2));
        this.graph2 = graphObj2.getGraph();
    }

    @Test
    public void addNodeTest() throws Exception {
        Graph graphObj = new Graph(graph1);
        Map<GraphUser, Set<GraphUser>> graph = graphObj.getGraph();

        //get numbmer of original verticies
        int origVerticies = graph.size();

        //add user
        graphObj.addNode(new GraphUser("testUser", 100));

        //test add user to graph
        Assert.assertEquals(origVerticies + 1, graph.size());
        Assert.assertTrue(graph.containsKey(new GraphUser("testUser")));

        //test new user follower count
        GraphUser testUser = null;
        for(GraphUser user : graph.keySet()) {
            if(user.username.equals("testUser"))
                testUser = user;
        }
        Assert.assertEquals(100, testUser.followers);
    }

    @Test
    public void removeNodeTest() throws Exception {
        Graph graphObj = new Graph(graph1);
        Map<GraphUser, Set<GraphUser>> graph = graphObj.getGraph();

        GraphUser user1 = new GraphUser("user1");
        Assert.assertTrue(graph.containsKey(user1));

        graphObj.removeNode(user1);
        Assert.assertTrue(!graph.containsKey(user1));

        Set<GraphUser> user5Neighbors = graph.get(new GraphUser("user5"));
        Assert.assertTrue(!user5Neighbors.contains(user1));
    }

    @Test
    public void addEdgeTest() {
        Graph graphObj = new Graph(graph1);
        Map<GraphUser, Set<GraphUser>> graph = graphObj.getGraph();

        GraphUser user = new GraphUser("user1");
        GraphUser follower =  new GraphUser("user4");

        graphObj.addEdge(user, follower);
        Assert.assertTrue(graph.get(user).contains(follower));
    }

    @Test
    public void removeEdgeTest() {
        Graph graphObj = new Graph(graph1);
        Map<GraphUser, Set<GraphUser>> graph = graphObj.getGraph();

        GraphUser user = new GraphUser("user1");
        GraphUser follower =  new GraphUser("user2");

        graphObj.removeEdge(user, follower);
        Assert.assertTrue(!graph.get(user).contains(follower));
    }

    @Test
    public void motherNodeTest() {
        Graph graphObj1 = new Graph(graph1);
        Graph graphObj2 = new Graph(graph2);

        Assert.assertFalse(graphObj1.hasMotherNode());
        Assert.assertTrue(graphObj2.hasMotherNode());
        Assert.assertEquals(graphObj2.getMotherNodes().size(), 2);
    }

    @Test
    public void motherNodeExtTest() {
        Graph graphObj = new Graph(graph1);

        Assert.assertFalse(graphObj.isMotherNodeExt(new GraphUser("user1")));
        Assert.assertFalse(graphObj.isMotherNodeExt(new GraphUser("user2")));
        Assert.assertFalse(graphObj.isMotherNodeExt(new GraphUser("user3")));
        Assert.assertFalse(graphObj.isMotherNodeExt(new GraphUser("user4")));
        Assert.assertTrue(graphObj.isMotherNodeExt(new GraphUser("user5")));
    }

    @Test
    public void motherNodeScanTest(){
        Graph graphObj = new Graph(graph2);

        Stack<GraphUser> gStack = new Stack<GraphUser>();
        for(GraphUser user : graphObj.getGraph().keySet()) {
            gStack.push(user);
        }
        Stack<GraphUser> finished = graphObj.dfsScan(gStack);

        Assert.assertTrue(graphObj.isMotherNodeExt(finished.pop()));
        Assert.assertTrue(graphObj.isMotherNodeExt(finished.pop()));
        Assert.assertFalse(graphObj.isMotherNodeExt(finished.pop()));
    }

    @Test
    public void domSetTest() {
        Graph graphObj1 = new Graph(graph1);
        Graph graphObj2 = new Graph(graph2);

        Set<GraphUser> set1 = graphObj1.dominatingSet();
        Assert.assertEquals(set1.size(), 3);
        Assert.assertTrue(set1.contains(new GraphUser("user1")));
        Assert.assertTrue(set1.contains(new GraphUser("user4")));
        Assert.assertTrue(set1.contains(new GraphUser("user5")));

        Set<GraphUser> set2 = graphObj2.dominatingSet();
        Assert.assertEquals(set2.size(), 1);
        Assert.assertTrue(set2.contains(new GraphUser("user6")));
    }

    @Test
    public void dfsTest() {
        Graph graphObj = new Graph(graph2);
        Set<GraphUser> set = graphObj.dfs(new GraphUser("user5"));
        Assert.assertEquals(set.size(), 5);
        Assert.assertTrue(set.contains(new GraphUser("user1")));
        Assert.assertTrue(set.contains(new GraphUser("user2")));
        Assert.assertTrue(set.contains(new GraphUser("user3")));
        Assert.assertTrue(set.contains(new GraphUser("user4")));
        Assert.assertTrue(set.contains(new GraphUser("user5")));
    }

}
