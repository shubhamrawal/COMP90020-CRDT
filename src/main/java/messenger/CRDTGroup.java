package messenger;

import messenger.message.Callback;
import messenger.message.Group;

public class CRDTGroup implements Group<CRDTMessage> {

    private Group<CRDTMessage> messageGroup;

    public CRDTGroup(Group<CRDTMessage> messageGroup) {
        this.messageGroup = messageGroup;
    }

    public void send(CRDTMessage message) {
        this.messageGroup.send(message);
    }

    public void onReceipt(Callback<CRDTMessage> crdtCallback) {
        this.messageGroup.onReceipt(crdtCallback);
    }

    public void join() {
        this.messageGroup.join();
    }

    public void leave(){
        this.messageGroup.leave();
    }
}
