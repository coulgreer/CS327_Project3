import java.util.Scanner;

public class LinkStateRouter {

	public static void parseString(String str) {
		str = str.replaceAll("\\s", "");
		Scanner scanner = new Scanner(str);
		scanner.useDelimiter(",");
		while (scanner.hasNext()) {
			try {
				String nodeId_A = scanner.next().toUpperCase();
				String nodeId_B = scanner.next().toUpperCase();
				int hopLength = Integer.parseInt(scanner.next());
				System.out.println("Node A: " + nodeId_A //
						+ "\nNode B: " + nodeId_B //
						+ "\nHop Length: " + hopLength);
			} catch (NumberFormatException nfe) {
				System.out.println("Not a valid hop length");
			}
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		parseString(str);

	}

}
