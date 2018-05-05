package messenger.ordering;

import java.util.UUID;

import messenger.message.Message;

public class TimestampedMessage implements Message {
	
	private static final long serialVersionUID = -648298223220854124L;
	private VectorTimestamp timestamp;
    private UUID processId;
    private Message innerMessage;

    public TimestampedMessage(UUID processId, VectorTimestamp timestamp, Message innerMessage) {
        this.processId = processId;
    	this.timestamp = timestamp;
    	this.innerMessage = innerMessage;
    }
    public void setProcessId(UUID pid) {
    	this.processId = pid;
    }
    public UUID getProcessId() {
    	return processId;
    }
    
    public void setTimestamp(VectorTimestamp ts) {
    	this.timestamp = ts;
    }

    public VectorTimestamp getTimestamp() {
        return timestamp;
    }
	public Message getInnerMessage() {
		return innerMessage;
	}
    
}
