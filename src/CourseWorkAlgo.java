import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;



public class CourseWorkAlgo {
    public static void main(String[] args) throws InterruptedException {

        // finding the time before the operation is executed
        long start = System.currentTimeMillis();
        for (int i = 0; i <5; i++) {
            Thread.sleep(60);
        }

        System.out.println("Graph:\n");
        Scanner input = null;
        try {
            input = new Scanner(new File("new.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        }

        assert input != null;
        int N = input.nextInt();
        GraphX g = new GraphX(N);

        System.out.println("++++Inputted Text File++++\n"+N);
        // add Edges
        while (input.hasNextInt()) {
            int i = input.nextInt();
            int j = input.nextInt();
            int w = input.nextInt();
            g.addEdge(i, j, w);
            System.out.println(i + " " + j +" "+w);
        }

        System.out.println();
        System.out.println("++++Adjacency List++++");
        // print Graph
       g.printGraph();

        System.out.println();
        System.out.println("++++Adjacency Matrix++++");
       g.printMe();

        System.out.println();
        // Ford-Fulkerson Max Flow Algorithm
        System.out.print("Ford-Fulkerson Max Flow: ");
        System.out.println(FordFulkerson(g, 0, N-1));


        // finding the time after the operation is executed
        long end = System.currentTimeMillis();
        //finding the time difference and converting it into seconds
        float sec = (end - start) / 1000F; System.out.println("Elapsed time = "+sec + " seconds");

    }

    public static float FordFulkerson(GraphX g, int source, int target) {
        // error proof
        if (source == target) {
            return 0;
        }
        int V = g.getvCount();

        // create residual graph
        GraphX rg = new GraphX(V);
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                rg.getAdj()[i][j] = g.getAdj()[i][j];
            }
        }

        // filled by BFS to store path
        int[] parent = new int[V];

        float max_flow = 0; // max flow value

        // while a path exists from source to dest loop
        while (bfs(rg, source, target, parent)) {
            // to store path flow
            float path_flow = Float.MAX_VALUE;

            // find maximum flow of path filled by bfs
            for (int i = target; i != source; i = parent[i]) {
                int j = parent[i];
                path_flow = Math.min(path_flow, rg.getAdj()[j][i]);
            }

            // update residual graph capacities, reverse edges along the path
            for (int i = target; i != source; i = parent[i]) {
                int j = parent[i];
                rg.getAdj()[j][i] -= path_flow;
                rg.getAdj()[i][j] += path_flow;
            }

            // Add path flow to max flow
            max_flow += path_flow;
        }

        return max_flow;
    }

    public static boolean bfs(GraphX rg, int source, int target, int[] parent) {
        // array to store visited vertices
        boolean[] seen = new boolean[rg.getvCount()];
        for (int i = 0; i < rg.getvCount(); i++)
            seen[i] = false;

        LinkedList<Integer> q = new LinkedList<>(); // queue-like

        // visit source
        q.add(source);
        seen[source] = true;
        parent[source] = -1;

        // loop through all vertices
        while (!q.isEmpty()) {
            int i = q.poll();
            // check neighbours of vertex i
            for (Integer j : rg.neighbours(i)) {
                // if not visited and positive value then visit
                if ((!seen[j]) && (rg.getAdj()[i][j] > 0)) {
                    q.add(j);
                    seen[j] = true;
                    parent[j] = i;
                }
            }
        }

        // return boolean that tells us if we ended up at the destination
        return seen[target];
    }
}

class GraphX {
    private final int vCount;
    private final float[][] adj;

    public int getvCount() {
        return vCount;
    }

    public float[][] getAdj() {
        return adj;
    }

    public GraphX(int vCount) {
        this.vCount = vCount;
        adj = new float[vCount][vCount];
        for (int i = 0; i < vCount; i++) {
            for (int j = 0; j < vCount; j++) {
                adj[i][j] = 0;
            }
        }
    }

    public void addEdge(int i, int j, int weight) {
        adj[i][j] = weight;
    }

    public boolean hasEdge(int i, int j) {
        return adj[i][j] != 0;
    }

    public List<Integer> neighbours(int vertex) {
        List<Integer> edges = new ArrayList<>();
        for (int i = 0; i < vCount; i++)
            if (hasEdge(vertex, i))
                edges.add(i);
        return edges;
    }

    //adjacencyList
    public void printGraph() {
        for (int i = 0; i < vCount; i++) {
            List<Integer> edges = neighbours(i);
            System.out.print(i + ": ");
            for (Integer edge : edges) {
                System.out.print(edge + " ");
            }
            System.out.println();
        }
    }

    //adjacencyMatrix
    void printMe() {
        for (int i = 0; i < vCount; i++) {
            for (int j = 0; j < vCount; j++)
                System.out.print(hasEdge(i,j) ? "1 " : "0 ");
            System.out.println();
        }
    }
}