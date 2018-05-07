package messenger;

import crdt.Operation;
import messenger.message.Message;

import java.util.Objects;
import java.util.UUID;

public class CRDTMessage implements Message {

    private UUID senderId;
    private Operation operation;

    public CRDTMessage(UUID senderId, Operation operation) {

        this.senderId = senderId;
        this.operation = operation;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CRDTMessage that = (CRDTMessage) o;
        boolean result1 = Objects.equals(getSenderId(), that.getSenderId());
        boolean result2 = Objects.equals(getOperation(), that.getOperation());
        return result1 && result2;
    }

}
