package com.coursera.dsalgo.nsonntag;

import java.util.*;

public class Graph {

    private Map<GraphUser, Set<GraphUser>> graph;

    public Graph(){
        this.graph = new HashMap<GraphUser, Set<GraphUser>>();
    }

    public Graph(Map<GraphUser, Set<GraphUser>> graph){
        this.graph = graph;
    }

    public Map<GraphUser, Set<GraphUser>> getGraph() {
        return graph;
    }

    public void removeNode(GraphUser userToRemove) {
        for(GraphUser user : graph.keySet()) {
            Set<GraphUser> followers = graph.get(user);
            removeEdge(user, userToRemove);
        }
        graph.remove(userToRemove);
    }

    public void removeNode(String username) {
        GraphUser userToRemove = new GraphUser(username, 0);
        removeNode(userToRemove);
    }

    public void addNode(GraphUser user) {
        if(!graph.containsKey(user))
            graph.put(user, new HashSet<GraphUser>());
    }

    public boolean addEdge(GraphUser user, GraphUser follower) {
        if(user == null || follower == null)
            return false;

        if(graph.containsKey(user) && !graph.get(user).contains(follower)) {
            graph.get(user).add(follower);
        }
        return true;
    }

    public boolean removeEdge(GraphUser user, GraphUser follower) {
        if(user == null || follower == null)
            return false;

        if(graph.containsKey(user) && graph.get(user).contains(follower)) {
            graph.get(user).remove(follower);
            return true;
        }
        else
            return false;
    }

    //find which nodes are followed by all others.
    public List<GraphUser> getMotherNodes() {
        List<GraphUser> listOut = new ArrayList();
        int numUsers = graph.size();

        for(GraphUser user : graph.keySet()) {
            if(graph.get(user).size() == numUsers - 1) {
                listOut.add(user);
            }
        }
        return listOut;
    }

    public boolean hasMotherNode() {
        if(getMotherNodes().size() > 0)
            return true;
        else
            return false;
    }

    //depth first search from specified node
    public Set<GraphUser> dfs(GraphUser startUser) {
        Set <GraphUser> visited = new HashSet<GraphUser>();
        Stack <GraphUser> stack = new Stack<GraphUser>();

        stack.add(startUser);
        visited.add(startUser);

        while(!stack.isEmpty()) {
            GraphUser curr = stack.pop();
            Set<GraphUser> neighbors = graph.get(curr);
            for(GraphUser neighbor : neighbors) {
                if(!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    stack.add(neighbor);
                }
            }
        }
        return visited;
    }

    //dfsScan(), dfsVisit(), and isMotherNodeExt() used to find "traditional" mother nodes
    public Stack<GraphUser> dfsScan(Stack<GraphUser> vertices) {
        Set<GraphUser> visited = new HashSet<GraphUser>();
        Stack<GraphUser> finished = new Stack<GraphUser>();

        while (!vertices.isEmpty()) {
            GraphUser v = vertices.pop();
            if(!visited.contains(v)) {
                dfsVisit(v, visited, finished);
            }
        }
        return finished;
    }

    public void dfsVisit(GraphUser v, Set<GraphUser> visited, Stack<GraphUser> finished) {
        visited.add(v);
        Set<GraphUser> neighbors = graph.get(v);
        for(GraphUser neighbor : neighbors) {
            if(!visited.contains(neighbor)) {
                dfsVisit(neighbor, visited, finished);
            }
        }
        finished.add(v);
    }

    public boolean isMotherNodeExt(GraphUser user) {
        Set<GraphUser> dfsVisited = dfs(user);
        if(dfsVisited.size() == graph.size())
            return true;
        else
            return false;
    }

    //find dominating set
    public Set<GraphUser> dominatingSet() {
        Set<GraphUser> domSet = new HashSet<GraphUser>();
        Set<GraphUser> covered = new HashSet<GraphUser>();

        while(covered.size() != getGraph().size()) {
            //each iteration, add node that touches most "uncovered" nodes
            int max_uncovered = 0;
            GraphUser max_user = null;

            for(GraphUser node : getGraph().keySet()) {
                if(covered.contains(node))
                    continue;

                int uncovered = 1;
                for(GraphUser neighbor : graph.get(node)) {
                    if(!covered.contains(neighbor))
                        uncovered++;
                }
                if(uncovered > max_uncovered) {
                    max_uncovered = uncovered;
                    max_user = node;
                }
            }

            covered.add(max_user);
            for(GraphUser neighbor : graph.get(max_user)) {
                covered.add(neighbor);
            }
            domSet.add(max_user);
        }

        return domSet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(GraphUser user : graph.keySet()) {
            sb.append("User: ");
            sb.append(user);
            sb.append("\n");
            sb.append("  Followers:\n");

            for(GraphUser follower : graph.get(user)) {
                sb.append("    ");
                sb.append(follower);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
