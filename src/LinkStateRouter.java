import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeSet;

public class LinkStateRouter {

	private static Graph.Edge[] GRAPH;
	public static List<Router> uniqueNodes = new ArrayList<Router>();

	public static void parseString(String parsedString) {

		// Remove the whitespace from the argument and split each chunk of input
		parsedString = parsedString.replaceAll("\\s", "");
		String[] nodes = parsedString.split(";");

		GRAPH = new Graph.Edge[nodes.length];

		// Scan through all of the input and check for errors
		for (int i = 0; i < nodes.length; i++) {
			Scanner scanner = new Scanner(nodes[i]);
			try {
				scanner.useDelimiter(",");
				String nodeId_A = scanner.next().toUpperCase();
				String nodeId_B = scanner.next().toUpperCase();
				int hopLength = scanner.nextInt();
				if (scanner.hasNext()) {
					System.err.println("The string is improperly formatted.");
					break;
				}

				// If no errors exist with the input then check if either of the
				// nodes exist in the input chunk
				boolean nodeAExists = false;
				boolean nodeBExists = false;
				for (Router router : uniqueNodes) {
					if (nodeId_A.equals(router.nodeId())) {
						router.connectsTo(nodeId_B).withHopLength(hopLength).add();
						nodeAExists = true;
					}
					if (nodeId_B.equals(router.nodeId())) {
						router.connectsTo(nodeId_A).withHopLength(hopLength).add();
						nodeBExists = true;
					}
				}

				if (!nodeAExists) {
					uniqueNodes.add(new Router(nodeId_A).connectsTo(nodeId_B).withHopLength(hopLength).add());
				}

				if (!nodeBExists) {
					uniqueNodes.add(new Router(nodeId_B).connectsTo(nodeId_A).withHopLength(hopLength).add());
				}

				GRAPH[i] = new Graph.Edge(nodeId_A, nodeId_B, hopLength);

			} catch (InputMismatchException ime) {
				System.err.print(scanner.next());
				System.out.println(" was not a valid hop length.");
			} catch (NoSuchElementException nsee) {
				System.err.println("The string is improperly fromatted! \nThe string is not long enough");
			} finally {
				scanner.close();
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("Please enter the routers, recievers, and the number of hops needed from the neighbor.");
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		parseString(str);
		Graph g = new Graph(GRAPH);
		//g.dijkstra(START);
		//g.printPath(END);
		//g.printAllPaths();
		
		for(Router router: uniqueNodes) {
			String startName = router.nodeId();
			g.dijkstra(startName);
			for(Router r : uniqueNodes) {
				String endName = r.nodeId();
				g.printPath(endName);
			}
		}

		scanner.close();
	}
}

class Graph {
	private final Map<String, Vertex> graph;

	/** One edge of the graph (only used by Graph constructor) */
	public static class Edge {
		public final String v1, v2;
		public final int dist;

		public Edge(String v1, String v2, int dist) {
			this.v1 = v1;
			this.v2 = v2;
			this.dist = dist;
		}
	}

	/**
	 * One vertex of the graph, complete with mappings to neighbouring vertices
	 */
	public static class Vertex implements Comparable<Vertex> {
		public final String name;
		public int dist = Integer.MAX_VALUE;
		public Vertex previous = null;
		public final Map<Vertex, Integer> neighbours = new HashMap<>();

		public Vertex(String name) {
			this.name = name;
		}

		public String nextHop;
		public int cost;
		
		private void printPath() {
			if (this == this.previous) {
				System.out.printf("%s", this.name);
			} else if (this.previous == null) {
				System.out.printf("%s(unreached)", this.name);
			} else {
				this.previous.printPath();
				System.out.printf(" -> %s(%d)", this.name, this.dist);
				
				if(this.name == null) {
					nextHop = this.name;
				}
				cost = this.dist;
			}
		}

		public int compareTo(Vertex other) {
			return Integer.compare(dist, other.dist);
		}
	}

	/** Builds a graph from a set of edges */
	public Graph(Edge[] edges) {
		graph = new HashMap<>(edges.length);

		// one pass to find all vertices
		for (Edge e : edges) {
			if (!graph.containsKey(e.v1))
				graph.put(e.v1, new Vertex(e.v1));
			if (!graph.containsKey(e.v2))
				graph.put(e.v2, new Vertex(e.v2));
		}

		// another pass to set neighbouring vertices
		for (Edge e : edges) {
			graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
			// graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); //
			// also do this for an undirected graph
		}
	}

	/** Runs dijkstra using a specified source vertex */
	public void dijkstra(String startName) {
		if (!graph.containsKey(startName)) {
			System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
			return;
		}
		final Vertex source = graph.get(startName);
		NavigableSet<Vertex> q = new TreeSet<>();

		// set-up vertices
		for (Vertex v : graph.values()) {
			v.previous = v == source ? source : null;
			v.dist = v == source ? 0 : Integer.MAX_VALUE;
			q.add(v);
		}

		dijkstra(q);
	}

	/** Implementation of dijkstra's algorithm using a binary heap. */
	private void dijkstra(final NavigableSet<Vertex> q) {
		Vertex u, v;
		while (!q.isEmpty()) {

			u = q.pollFirst(); // vertex with shortest distance (first
								// iteration will return source)
			if (u.dist == Integer.MAX_VALUE)
				break; // we can ignore u (and any other remaining vertices)
						// since they are unreachable

			// look at distances to each neighbour
			for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
				v = a.getKey(); // the neighbour in this iteration

				final int alternateDist = u.dist + a.getValue();
				if (alternateDist < v.dist) { // shorter path to neighbour
												// found
					q.remove(v);
					v.dist = alternateDist;
					v.previous = u;
					q.add(v);
				}
			}
		}
	}

	/** Prints a path from the source to the specified vertex */
	public void printPath(String endName) {
		if (!graph.containsKey(endName)) {
			System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
			return;
		}

		graph.get(endName).printPath();
		System.out.println();
	}

	/**
	 * Prints the path from the source to every vertex (output order is not
	 * guaranteed)
	 */
	public void printAllPaths() {
		for (Vertex v : graph.values()) {
			v.printPath();
			System.out.println();
		}
	}
}
