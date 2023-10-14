package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ShortestPathsTest {
    /** The graph example from Prof. Myers's notes. There are 7 vertices labeled a-g, as
     *  described by vertices1. 
     *  Edges are specified by edges1 as triples of the form {src, dest, weight}
     *  where src and dest are the indices of the source and destination
     *  vertices in vertices1. For example, there is an edge from a to d with
     *  weight 15.
     */
    static final String[] vertices1 = { "a", "b", "c", "d", "e", "f", "g" };
    static final int[][] edges1 = {
        {0, 1, 9}, {0, 2, 14}, {0, 3, 15},
        {1, 4, 23},
        {2, 4, 17}, {2, 3, 5}, {2, 5, 30},
        {3, 5, 20}, {3, 6, 37},
        {4, 5, 3}, {4, 6, 20},
        {5, 6, 16}
    };
    static class TestGraph implements WeightedDigraph<String, int[]> {
        int[][] edges;
        String[] vertices;
        Map<String, Set<int[]>> outgoing;

        TestGraph(String[] vertices, int[][] edges) {
            this.vertices = vertices;
            this.edges = edges;
            this.outgoing = new HashMap<>();
            for (String v : vertices) {
                outgoing.put(v, new HashSet<>());
            }
            for (int[] edge : edges) {
                outgoing.get(vertices[edge[0]]).add(edge);
            }
        }
        public Iterable<int[]> outgoingEdges(String vertex) { return outgoing.get(vertex); }
        public String source(int[] edge) { return vertices[edge[0]]; }
        public String dest(int[] edge) { return vertices[edge[1]]; }
        public double weight(int[] edge) { return edge[2]; }
    }
    static TestGraph testGraph1() {
        return new TestGraph(vertices1, edges1);
    }

    @Test
    void lectureNotesTest() {
        TestGraph graph = testGraph1();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("a");
        assertEquals(50, ssp.getDistance("g"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" g");
        assertEquals("best path: a c e f g", sb.toString());
    }

    @Test
    void lectureNotesTest2() {
        String[] vertices_2= { "a", "b", "c", "d", "e", "f", "g" };
        int [][] edges_2= {
                {0, 1, 5}, {0, 2, 9}, {0, 4, 18},
                {1, 4, 15},{1,5,24},{1,0,9},{1,2,6},
                {2, 3, 10},{2,4,19},
                {3,4,8}, {3, 5, 11}, {3,2,19},
                {4, 1, 27},
                {5, 6, 3},{5,0,40}
        };
        TestGraph graph_2 = new TestGraph(vertices_2,edges_2);
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph_2);
        ssp.singleSourceDistances("a");
       assertEquals(32, ssp.getDistance("g"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" g");
        assertEquals("best path: a b f g", sb.toString());
    }

    @Test
    void lectureNotesTest3() {
        String[] vertices_3= { "a", "b", "c", "d", "e", "f", "g","h","i"};
        int [][] edges_3= {
                {0, 1, 10}, {0, 2, 15}, {0, 3, 9},{0,4,2},
                {1, 3, 12},{1,2,19},
                {2, 6, 11},
                {3, 2, 2}, {3, 1, 21},
                {4, 3, 28}, {4,5, 4},
                {5,6,5},
                {6,8,7}, {6,7,30},
                {7,0,20},

        };
        TestGraph graph_3 = new TestGraph(vertices_3,edges_3);
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph_3);
        ssp.singleSourceDistances("a");
        assertEquals(18, ssp.getDistance("i"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("i")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" i");
        assertEquals("best path: a e f g i", sb.toString());

    }
    // TODO: Add 2 more tests
}
