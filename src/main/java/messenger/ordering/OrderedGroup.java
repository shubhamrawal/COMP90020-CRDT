package messenger.ordering;

import messenger.message.Callback;
import messenger.message.Group;
import messenger.message.Message;

public class OrderedGroup implements Group<Message> {

    private Group<Message> messageGroup;
    private Callback<Message> callback;
    private VectorTimestamp timestamp;

    public OrderedGroup(Group<Message> messageGroup) {
        this.messageGroup = messageGroup;
    }

    public void send(Message message) {
        //TODO increment timestamp
        //add timestamp to message
        //messageGroup.send(new TimestampedMessage(new VectorTimestamp()));
    }

    public void onReceipt(Callback<Message> callback) {
        this.callback = callback;
        this.messageGroup.onReceipt(message -> {
            //TODO increment timestamp
            //add to buffer if required
            //check buffer for messages
            //this.callback.process(message.getInnerMessage());
        });
    }

    public void join() {
        messageGroup.join();
    }

    public void leave(){
        messageGroup.leave();
    }
}
