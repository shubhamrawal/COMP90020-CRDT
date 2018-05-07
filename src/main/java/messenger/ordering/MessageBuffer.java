package messenger.ordering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

class MessageBuffer<M extends OrderedMessage> {
	
	private ArrayList<M> buffer;
	private VectorTimestamp vectorClocks;
	
	MessageBuffer() {
		buffer = new ArrayList<M>();
	}
	
	void add(M message) {
		buffer.add(message);
	}

	int getSize() {
		return buffer.size();
	}
	
	public M checkbuffer() {
		 		VectorTimestamp other;
		 		UUID sender;
		 		int msgIndex;
		 		if (!buffer.isEmpty()) {
		 			for (int i = 0; i < buffer.size(); i++) {
		 				other = buffer.get(i).getTimestamp();
		 				sender = buffer.get(i).getSenderId();
		 				if (vectorClocks.isDeliverable(other, sender)){
		 					msgIndex = buffer.indexOf(buffer.get(i));
		 					return buffer.remove(msgIndex);
		 				}
		 			}
		 		}
		 		return null;
		  	}
}
