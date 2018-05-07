package messenger.ordering;

import messenger.message.Callback;
import messenger.message.Group;
import messenger.message.Message;
import texteditor.App;

import java.util.logging.Logger;

public class OrderedGroup<M extends Message> implements Group<M> {

    private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName() );
    private Group<OrderedMessage<M>> messageGroup;
    private VectorTimestamp timestamp;
    private MessageBuffer<OrderedMessage<M>> messageBuffer;

    public OrderedGroup(Group<OrderedMessage<M>> messageGroup) {
        this.messageGroup = messageGroup;
        this.timestamp = new VectorTimestamp();
        this.messageBuffer = new MessageBuffer<OrderedMessage<M>>();
    }

    public void send(M message) {
        this.timestamp.increment(App.uuid);
        messageGroup.send(new OrderedMessage<M>(App.uuid, this.timestamp, message));
    }
    
    public void onReceipt(Callback<M> callback) {
		this.messageGroup.onReceipt(message -> {
			// filter the message generated in the past in case it got re-delivered
			if (!timestamp.isPast(message.getTimestamp())){	
				if(timestamp.isDeliverable(message.getTimestamp(), message.getSenderId())) {
					timestamp.merge(message.getTimestamp());
					callback.process(message.getInnerMessage());
				}
				else {
					messageBuffer.add(message);
				}
				if (messageBuffer.checkbuffer()!= null) {
					timestamp.merge(messageBuffer.checkbuffer().getTimestamp());
					callback.process(messageBuffer.checkbuffer().getInnerMessage());
				}
			}
		}
		);
	}

    public void join() {
        messageGroup.join();
        timestamp = new VectorTimestamp();
    }

    public void leave() {
        messageGroup.leave();
    }
}
