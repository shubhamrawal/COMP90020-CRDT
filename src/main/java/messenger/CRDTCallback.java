package messenger;

import crdt.Operation;
import messenger.message.Callback;

import java.util.UUID;

public class CRDTCallback implements Callback<CRDTMessage> {

    public void process(CRDTMessage crdtMessage) {
        merge(crdtMessage.getSenderId(), crdtMessage.getOperation());
    }

    private void merge(UUID memberId, Operation operation) {
        //TODO overwrite and implement merge
    }
}
