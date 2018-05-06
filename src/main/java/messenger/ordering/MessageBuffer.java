package messenger.ordering;

import java.util.ArrayList;
import java.util.UUID;

class MessageBuffer<M extends TimestampedMessage> {
	
	private ArrayList<M> holdBackQueue;
	
	MessageBuffer() {
		holdBackQueue = new ArrayList<M>();
	}
	
	void enqueue(M message) {
		holdBackQueue.add(message);
	}
	
	M checkHoldbackQueue(VectorTimestamp current) {
		VectorTimestamp other;
		UUID sender;
		int msgIndex;
		if (!holdBackQueue.isEmpty()) {
			for (int i = 0; i < holdBackQueue.size(); i++) {
				other = holdBackQueue.get(i).getTimestamp();
				sender = holdBackQueue.get(i).getSenderId();
				if (current.isDeliverable(other, sender)){
					msgIndex = holdBackQueue.indexOf(holdBackQueue.get(i));
					return holdBackQueue.remove(msgIndex);
				}
			}
		}
		return null;
	}

}
