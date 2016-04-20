import java.util.HashMap;
import java.util.Map;

public class Router implements Runnable {

	private String sourceId;
	String destinationId;
	int cost;
	Map<String, Integer> linkStatePacket = new HashMap<String, Integer>();
	public Router(String sourceId) {
		this.sourceId = sourceId;
	}
	
	public Router connectsTo(String destinationId) {
		this.destinationId = destinationId;
		return this;
	}
	
	public Router withHopLength(int cost) {
		this.cost = cost;
		return this;
	}
	
	public String sourceId() {
		return sourceId;
	}
	
	public Router add() {
		linkStatePacket.put(destinationId, cost);
		return this;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
