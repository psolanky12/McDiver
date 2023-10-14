package diver;

import datastructures.SlowPQueue;
import game.*;
import graph.ShortestPaths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.DoubleUnaryOperator;




public class McDiver implements SewerDiver {


    /**
     * This is a helper method for seek. It recursively preforms a depth first search and tries to get to the
     * exit in the least number of steps possible. It sorts the nodes of in a Slow Priority Queue with
     * the NodeStatus and distance to the ring and McDiver moves to the location with the smallest distance.
     * @param state The current state of McDiver with the current move.
     * @param visited An ArrayList of type Long that stores the ID of the node that McDiver has visited.
     */
    private int dfs(SeekState state, ArrayList<Long> visited) {
        if (state.distanceToRing() == 0) {
            return 1;
        }
        SlowPQueue<NodeStatus> OptList = new SlowPQueue<NodeStatus>();
        for (NodeStatus neighbor : state.neighbors()) {
            if (!visited.contains(neighbor.getId()))
                OptList.add(neighbor, neighbor.getDistanceToRing());
        }

        long location = state.currentLocation();
        visited.add(state.currentLocation());
        int finished=0;
        while (OptList.size() > 0 && finished == 0) {
            state.moveTo(OptList.extractMin().getId());
            finished = dfs(state, visited);
            if (finished == 0) {
                state.moveTo(location);
            }

        }
        if (state.distanceToRing() == 0) {
            return 1;
        }
        return 0;

    }


    /**
     * See {@code SewerDriver} for specification.
     */
    @Override
    public void seek(SeekState state) {
        // TODO : Look for the ring and return.
        // DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
        // Instead, write your method (it may be recursive) elsewhere, with a
        // good specification, and call it from this one.
        //
        // Working this way provides you with flexibility. For example, write
        // one basic method, which always works. Then, make a method that is a
        // copy of the first one and try to optimize in that second one.
        // If you don't succeed, you can always use the first one.
        //
        // Use this same process on the second method, scram.
        ArrayList<Long> visited = new ArrayList<>();
        dfs(state, visited);
    }

    /**
     * This is a helper method for scram. It creates a maze with all the nodes in the current state
     * and finds the shortest distance to the path. It uses a SlowPQueue to find coins and the
     * distance to coin and sorts through the list to collect the nearest coin if there are enough
     * steps left. McDiver always exits the maze within the specified number of steps.
     * @param state The current state of McDiver
     */
    public void scramHelper(ScramState state){
        Maze m = new Maze((Set<Node>) state.allNodes());
        double distToCoin;
        ShortestPaths<Node, Edge> path = new ShortestPaths<>(m);
        path.singleSourceDistances(state.currentNode());
        Node bestCoinNode;

        while (path.getDistance(state.exit())<state.stepsToGo()) {
            path.singleSourceDistances(state.currentNode());
            SlowPQueue<Node> OptList = new SlowPQueue<>();
            for (Node n : state.allNodes()) {
                if (n.getTile().coins() != 0) {
                    OptList.add(n,  path.getDistance(n)/n.getTile().coins());
                }
            }
            ArrayList<Edge> coinPath = new ArrayList<>(path.bestPath(OptList.peek()));

            bestCoinNode= OptList.extractMin();
            distToCoin=path.getDistance(bestCoinNode);
            path.singleSourceDistances(bestCoinNode);
            if (path.getDistance(state.exit())+distToCoin>state.stepsToGo()){
                break;
            }
            for (Edge edge : coinPath) {
                state.moveTo(edge.destination());

            }
        }
        Node exit = state.exit();
        path.singleSourceDistances(state.currentNode());
        ArrayList<Edge> exitPath = new ArrayList<>(path.bestPath(exit));
        for (Edge edge : exitPath) {
            state.moveTo(edge.destination());
        }
    }

    /** This is the place for your implementation of the {@code SewerDiver}.
     */
    public void scram(ScramState state) {
        scramHelper(state);
    }


}