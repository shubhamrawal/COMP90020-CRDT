package messenger;

import crdt.Operation;
import messenger.message.Message;

import java.util.Objects;

public class CRDTMessage implements Message {

    private Operation operation;

    public CRDTMessage(Operation operation) {
        this.operation = operation;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CRDTMessage that = (CRDTMessage) o;
        return Objects.equals(getOperation(), that.getOperation());
    }

}
