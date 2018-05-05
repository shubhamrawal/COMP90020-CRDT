package messenger.ordering;

import messenger.message.Callback;
import messenger.message.Group;
import messenger.message.Message;
import texteditor.App;

public class OrderedGroup implements Group<Message> {

	private Group<TimestampedMessage> messageGroup;
	private VectorTimestamp timestamp;
	private MessageBuffer msgBuf;

	public OrderedGroup(Group<Message> messageGroup) {
		this.messageGroup = messageGroup;
		this.timestamp = new VectorTimestamp();
		this.msgBuf = new MessageBuffer(timestamp);
	}

	public void send(Message message) {
		this.timestamp.increment(App.uuid);
		messageGroup.send(new TimestampedMessage(App.uuid, this.timestamp, message));
	}

	public void onReceipt(Callback<Message> callback) {
		this.messageGroup.onReceipt(message -> {
			
//			int order = this.timestamp.compareTo(message.getTimestamp(), message.getProcessId());
//			if (order == 0) {
//				timestamp.merge(message.getTimestamp());
//				callback.process(message.getInnerMessage());
//			}
//			else if(order == -1) {
//				msgBuf.enqueue(message);
//			}
			if(timestamp.isDeliverable(message.getTimestamp(), message.getProcessId())) {
				timestamp.merge(message.getTimestamp());
				callback.process(message.getInnerMessage());
			}
			else {
				msgBuf.enqueue(message);
			}
			
			if (msgBuf.checkHoldbackQueue() != null) {
				timestamp.merge(msgBuf.checkHoldbackQueue().getTimestamp());
				callback.process(msgBuf.checkHoldbackQueue().getInnerMessage());
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
