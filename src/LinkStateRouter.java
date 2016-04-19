import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class LinkStateRouter {

	public static void parseString(String parsedString) {
		List<Router> uniqueNodes = new ArrayList<Router>();
		
		// Remove the whitespace from the argument and split each chunk of input
		parsedString = parsedString.replaceAll("\\s", "");
		String[] nodes = parsedString.split(";");
		
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
				
				// If no errors exist with the input then check if either of the nodes exist in the input chunk
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
				
				if(!nodeAExists) {
					uniqueNodes.add(new Router(nodeId_A).connectsTo(nodeId_B).withHopLength(hopLength).add());
				}
				
				if(!nodeBExists) {
					uniqueNodes.add(new Router(nodeId_B).connectsTo(nodeId_A).withHopLength(hopLength).add());
				}
				
				

			} catch (InputMismatchException ime) {
				System.err.print(scanner.next());
				System.out.println(" was not a valid hop length.");
			} catch (NoSuchElementException nsee) {
				System.err.println("The string is improperly fromatted! \nThe string is not long enough");
			} finally {
				scanner.close();
			}
		}
		for(Router router: uniqueNodes) {
			System.out.println("Router: " + router.threadId);
			for(String str: router.connecters.keySet()) {
				System.out.println(str + "   " + router.connecters.get(str));
			}
			System.out.println();
		}
		
	}

	public static void main(String[] args) {
		System.out.println("Please enter the routers, recievers, and the number of hops needed from the neighbor.");
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		parseString(str);
		scanner.close();
	}

}
