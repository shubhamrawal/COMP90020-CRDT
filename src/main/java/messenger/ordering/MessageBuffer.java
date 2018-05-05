package messenger.ordering;

import java.util.LinkedList;
import java.util.Queue;

public class MessageBuffer {
	Queue<TimestampedMessage> queue;
	
	public MessageBuffer(){
		queue = new LinkedList<TimestampedMessage>();
	}
	
	public TimestampedMessage dequeue(TimestampedMessage msg) {
		return queue.remove();
	}
	
	public void enqueue(TimestampedMessage msg) {
		queue.add(msg);
	}

}
