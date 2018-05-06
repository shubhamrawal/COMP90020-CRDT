package messenger.ordering;

import messenger.message.Callback;
import messenger.message.Group;
import messenger.message.Message;
import texteditor.App;

public class OrderedGroup<M extends Message> implements Group<M> {

	private Group<TimestampedMessage<M>> messageGroup;
	private VectorTimestamp timestamp;
	private MessageBuffer<TimestampedMessage<M>> messageBuffer;

	public OrderedGroup(Group<TimestampedMessage<M>> messageGroup) {
		this.messageGroup = messageGroup;
		this.timestamp = new VectorTimestamp();
		this.messageBuffer = new MessageBuffer<TimestampedMessage<M>>();
	}

	public void send(M message) {
		this.timestamp.increment(App.uuid);
		messageGroup.send(new TimestampedMessage<M>(App.uuid, this.timestamp, message));
	}

	public void onReceipt(Callback<M> callback) {
		this.messageGroup.onReceipt(message -> {
			
//			int order = this.timestamp.compareTo(message.getTimestamp(), message.getSenderId());
//			if (order == 0) {
//				timestamp.merge(message.getTimestamp());
//				callback.process(message.getInnerMessage());
//			}
//			else if(order == -1) {
//				messageBuffer.enqueue(message);
//			}
			if(timestamp.isDeliverable(message.getTimestamp(), message.getSenderId())) {
				timestamp.merge(message.getTimestamp());
				callback.process(message.getInnerMessage());
			} else {
				messageBuffer.enqueue(message);
			}
			
			while(messageBuffer.checkHoldbackQueue(OrderedGroup.this.timestamp) != null) {
				timestamp.merge(messageBuffer.checkHoldbackQueue().getTimestamp());
				callback.process(messageBuffer.checkHoldbackQueue().getInnerMessage());
			}
			
		}
		);

	}

	public void join() {
		messageGroup.join();
		timestamp = new VectorTimestamp();
	}

	public void leave(){
		messageGroup.leave();
		timestamp = null;
	}
}
