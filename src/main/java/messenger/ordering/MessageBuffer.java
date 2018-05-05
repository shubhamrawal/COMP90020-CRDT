package messenger.ordering;

import java.util.ArrayList;
import java.util.UUID;

public class MessageBuffer {
	
	private ArrayList<TimestampedMessage> holdBackQueue;
	private VectorTimestamp vectorTs;
	
	public MessageBuffer(VectorTimestamp vectorTs) {
		holdBackQueue = new ArrayList<TimestampedMessage>();
		this.vectorTs = vectorTs;
	}
	
	public void enqueue(TimestampedMessage msg) {
		holdBackQueue.add(msg);
	}
	
	public void dequeue(TimestampedMessage msg) {
		if (holdBackQueue.contains(msg)) {
			holdBackQueue.remove(msg);
		}
	}
	
	public TimestampedMessage checkHoldbackQueue() {
		VectorTimestamp other;
		UUID sender;
		int msgIndex;
		if (!holdBackQueue.isEmpty()) {
			for (int i = 0; i < holdBackQueue.size(); i++) {
				other = holdBackQueue.get(i).getTimestamp();
				sender = holdBackQueue.get(i).getProcessId();
				if (vectorTs.isDeliverable(other, sender)){
					msgIndex = holdBackQueue.indexOf(holdBackQueue.get(i));
					return holdBackQueue.remove(msgIndex);
				}
			}
		}
		return null;
	}

}
