package messenger;

import java.io.IOException;

import messenger.message.Callback;
import messenger.message.Group;
import texteditor.MulticastSender;

public class CRDTGroup implements Group<CRDTMessage> {

    private Group<CRDTMessage> messageGroup;

    public CRDTGroup(Group<CRDTMessage> messageGroup) {
        this.messageGroup = messageGroup;
    }

    public void send(CRDTMessage message) {
//    		System.out.println(message.getOperation().getAtom().toString());
//    		MulticastSender sender = new MulticastSender();
//    		try {
//    			sender.multicast(message);
//    		} catch(IOException e) {
//    			e.printStackTrace();
//    		}
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
