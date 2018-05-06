package messenger.ordering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

class MessageBuffer<M extends OrderedMessage> {
	
	private ArrayList<M> buffer;
	
	MessageBuffer() {
		buffer = new ArrayList<M>();
	}
	
	void add(M message) {
		buffer.add(message);
	}

	void applyAndRemoveIfTrue(Function<M, Boolean> condition, Function<M, Boolean> operation) {
		Iterator<M> iterator = buffer.iterator();

		while(iterator.hasNext()) {
			M message = iterator.next();
			if(condition.apply(message)) {
				operation.apply(message);
				iterator.remove();
			}
		}
	}

}
