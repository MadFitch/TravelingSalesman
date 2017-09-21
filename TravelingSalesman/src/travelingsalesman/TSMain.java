package travelingsalesman;

/**
 *
 * @author Madison Fichtner
 * Traveling Salesmen Program that uses an Edge Weighted Graph to find a solution to the classic
 * Computer Science problem.
 */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class TSMain {
    double currentWeight = 0;
    private static Node[] cities;
    private static int[] edges;
    public static double distance;
    public static EdgeWeightedGraph G;
    
    public static void main(String[] args)
    {
        Node[] cities;
        boolean valid = false;
        boolean validE = false;
        if(args.length == 0)
        {
            //Reading in initial size of graph, and building graph of vertex n and the minimum required edges to connect all vertices in the graph
            System.out.println("Please input the number of vertices you wish to be in the graph: ");
            int n = StdIn.readInt();
            int nEdges = ((n-1)*(n-2))/(2);
            //EdgeWeightedGraph G;
            //StdOut.println(G);
            
            //Creating new node array of N number of cities
            cities = new Node[n];
            
            long startTime = System.currentTimeMillis();
            G = validate(n, nEdges, valid, cities);
            System.out.println(G);
            
            for(int i = 0; i < n; i++)
            {
                cities[i].printEdges(); // prints the edges around the city
            }
            //Prints out the vector that is the shortest distance away from vector i
            for(int i = 0; i < n; i++)
            {
                System.out.println("The vertex with the shortest edge distance away from " + i + " is " + cities[i].getAdjVector() + " with a weight of: " + (cities[i].getShortestEdgeWeight() * 10)); 
            }
            
            greedy(cities, n, distance); // calls the greedy function
            long endTime = System.currentTimeMillis();
            System.out.println("Total Time: " + (endTime - startTime) + " ms"); // records the time to figure out the algorithmn
        }
    }
    
    public static EdgeWeightedGraph validate(int n, int nEdges, boolean valid, Node[] cities) //method to validate the graph
    {
        while(valid != true)
            {
                G = new EdgeWeightedGraph(n, nEdges);
                //Filling the cities[] with n number of new nodes, and adding the edges of each vertex to another array found in the node class
                for(int i = 0; i < n; i++)
                {
                    cities[i] = new Node(i, G);
                    cities[i].addEdges(G);
                }
                for(int j = 0; j < n; j++)
                {
                    if(cities[j].size == 0)
                    {
                        cities[j].valid = false;
                        break;
                    }
                    else if(cities[j].getAdjVector() == j)
                    {
                        cities[j].valid = false;
                        break;
                    }
                    else
                        cities[j].valid = true;
                }
                for(int k = 0; k < n; k++)
                {
                    if(cities[k].valid == false)
                    {
                        valid = false;
                        break;
                    }
                    else
                        valid = true;
                }
            }
        
        return G;
    }
    
	//Greedy solution to the problem, meaning it always chooses the closest node/city
    public static void greedy(Node[] cities, int n, double distance)
    {
        for(int i = 0; i < n; i++)
        {
            distance += cities[i].getShortestEdgeWeight();
        }
        System.out.println("\nTotal Cost: " + distance * 10); //prints the total distance the salesman has to travel
    }
    
    //node class
    public static class Node{
        public boolean valid;
        public Edge[] edges;
        double[] edgeWeights;
        public int size;
        public boolean visited;
        Edge shortestEdge;
        double shortestEdgeWeight;
        int vector;
        int v;
        int count = 0;
        boolean VV = false;
        
        // Constructor
        Node(int vector, EdgeWeightedGraph G)
        {
            this.vector = vector;
            size = G.degree(vector);
            visited = false;
        }
        
        public int getOther()
        {
            return shortestEdge.either();
        }
        
        public double getVector() //method that returns vector
        {
            return vector;
        }
        
        public Edge getShortestEdge() // method that returns the shortest edge
        {
            if(VV == true)
            {
                return shortestEdge;
            }
            shortestEdge = edges[0];
            for(int i = 0; i < edges.length; i++)
            {
                if(edges[i].weight() < shortestEdge.weight())
                {
                    if(shortestEdge.other(vector) != vector)
                        shortestEdge = edges[count];
                }
            }
            
            return shortestEdge;
        }
        
        public void setShortestEdge() // sets the shortest edge with a boolean 
        {
            count++;
            shortestEdge = edges[count];
            shortestEdgeWeight = edges[count].weight();
            VV = true;
        }
        
        public double getShortestEdgeWeight()
        {
            //sortWeights();
            return edgeWeights[count]; // returns the value of weight from the shortestedge
        }
        
        public int getAdjVector() // method to retrieve the adjacent vector connected to the shortest edge
        {
            shortestEdge = getShortestEdge();
            v = shortestEdge.other(vector);
            return v;
        }
        
        public void addEdges(EdgeWeightedGraph G) //method to add edges to vertices
        {
            edges = new Edge[size];
            int i = 0;
            for(Edge e: G.adj(vector))
            {
                edges[i] = e;
                i++;
            }
        }
        
        public void printEdges() //prints the edges to the vertices
        {
            if(size == 0)
               return;
            System.out.println("---------------Printing Edges for " + vector + "---------------");
            int k = 0;
            edgeWeights = new double[size];
            for(Edge e: edges)
            {
                
                edgeWeights[k] = e.weight();
                System.out.println(e);
                k++;
            }
            
            System.out.println("");
            
            sortWeights();
        }
        public void printWeights() // prints the weights in order from least to greatest
        {
            if(size == 0)
               return;
            sortWeights();
            System.out.println("---------------Printing Weights for " + vector + "---------------");
            for(int j = 0; j <= size; j++)
            {
                System.out.println(edgeWeights[j]);
            }
            System.out.println("");
        }
        
        public void sortWeights() // sorts the weights of the cities from least to greatest
        {
            double temp;
            if(vector != v)
            {
                for (int i = 0; i < size; i++) 
                {
                    for (int j = i + 1; j < size; j++) 
                    {
                        if (edgeWeights[i] > edgeWeights[j]) 
                        {
                            temp = edgeWeights[i];
                            edgeWeights[i] = edgeWeights[j];
                            edgeWeights[j] = temp;
                        }
                    }
                }
            }
        }
    }
}
