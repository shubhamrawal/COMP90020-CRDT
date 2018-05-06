package messenger.ordering;

import messenger.message.Message;

import java.util.Objects;
import java.util.UUID;

public class OrderedMessage<M extends Message> implements Message {
	
	private static final long serialVersionUID = -648298223220854124L;
	private VectorTimestamp timestamp;
    private UUID senderId;
    private M innerMessage;

    OrderedMessage(UUID senderId, VectorTimestamp timestamp, M innerMessage) {
        this.senderId = senderId;
    	this.timestamp = timestamp;
    	this.innerMessage = innerMessage;
    }

    UUID getSenderId() {
    	return senderId;
    }

    VectorTimestamp getTimestamp() {
        return timestamp;
    }

    M getInnerMessage() {
		return innerMessage;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedMessage<?> that = (OrderedMessage<?>) o;
        return Objects.equals(getTimestamp(), that.getTimestamp()) &&
                Objects.equals(getSenderId(), that.getSenderId()) &&
                Objects.equals(getInnerMessage(), that.getInnerMessage());
    }

}
