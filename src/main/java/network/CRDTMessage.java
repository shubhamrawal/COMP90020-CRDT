package network;

import crdt.Operation;
import network.message.Message;

import java.util.Objects;
import java.util.UUID;

public class CRDTMessage implements Message {

    private UUID memberId;
    private Operation operation;

    public CRDTMessage(UUID memberId, Operation operation) {

        this.memberId = memberId;
        this.operation = operation;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
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
        boolean result1 = Objects.equals(getMemberId(), that.getMemberId());
        boolean result2 = Objects.equals(getOperation(), that.getOperation());
        return result1 && result2;
    }

}
