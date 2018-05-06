package messenger.ordering;

import messenger.message.Message;

import java.util.UUID;

public class OrderedMessage<M extends Message> implements Message {
	
	private static final long serialVersionUID = -648298223220854124L;
	private VectorTimestamp timestamp;
    private UUID senderId;
    private M innerMessage;

    public OrderedMessage(UUID senderId, VectorTimestamp timestamp, M innerMessage) {
        this.senderId = senderId;
    	this.timestamp = timestamp;
    	this.innerMessage = innerMessage;
    }

    public UUID getSenderId() {
    	return senderId;
    }

    public VectorTimestamp getTimestamp() {
        return timestamp;
    }

	public M getInnerMessage() {
		return innerMessage;
	}
    
}
