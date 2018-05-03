package network.ordering;

import network.message.Message;

public class TimestampedMessage implements Message {

    private VectorTimestamp timestamp;

    public TimestampedMessage(VectorTimestamp timestamp) {
        this.timestamp = timestamp;
    }

    public VectorTimestamp getTimestamp() {
        return timestamp;
    }
}
