package loadbalancer;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RequestQueue {
	private static ConcurrentLinkedQueue<Request> queue = new ConcurrentLinkedQueue<Request>();
	
	public RequestQueue() {}
	
	public void addRequest(Request request) {
		queue.offer(request);
	}
	
	public int getSize() {
		return queue.size();
	}
	
	public Request delRequest() {
		return queue.poll();
	}
}
