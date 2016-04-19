import java.util.HashMap;
import java.util.Map;

public class Router implements Runnable {

	String threadId;
	String connecterId;
	int hopLength;
	Map<String, Integer> connecters = new HashMap<String, Integer>();
	public Router(String threadId) {
		this.threadId = threadId;
	}
	
	public Router connectsTo(String connecterId) {
		this.connecterId = connecterId;
		return this;
	}
	
	public Router withHopLength(int hopLength) {
		this.hopLength = hopLength;
		return this;
	}
	
	public String nodeId() {
		return threadId;
	}
	
	public Router add() {
		connecters.put(connecterId, hopLength);
		return this;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
