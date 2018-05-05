package messenger;

import crdt.Operation;
import messenger.message.Group;
import messenger.message.Message;

import java.util.UUID;

public class CRDTGroup {

    private Group<Message> messageGroup;

    public CRDTGroup(Group<Message> messageGroup) {
        this.messageGroup = messageGroup;
    }

    public void send(UUID memberId, Operation operation) {
        this.messageGroup.send(new CRDTMessage(memberId, operation));
    }

    public void onReceipt(CRDTCallback crdtCallback) {
        this.messageGroup.onReceipt(crdtCallback);
    }

    public void join() {
        this.messageGroup.join();
    }

    public void leave(){
        this.messageGroup.leave();
    }
}
