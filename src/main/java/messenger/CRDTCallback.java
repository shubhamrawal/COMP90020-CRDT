package messenger;

import crdt.Operation;
import messenger.message.Callback;
import messenger.message.Message;

import java.util.UUID;

public abstract class CRDTCallback implements Callback<Message> {

    void process(CRDTMessage crdtMessage) {
        merge(crdtMessage.getMemberId(), crdtMessage.getOperation());
    }

    //TODO overwrite and implement merge
    public abstract void merge(UUID memberId, Operation operation);
}
