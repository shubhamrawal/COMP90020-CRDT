package messenger.ordering;

import messenger.message.Callback;
import messenger.message.Group;
import messenger.message.Message;
import texteditor.App;

import java.util.logging.Logger;

public class OrderedGroup<M extends Message> implements Group<M> {

    private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName() );
    private Group<OrderedMessage<M>> messageGroup;
    private VectorTimestamp timestamp;
    private MessageBuffer<OrderedMessage<M>> messageBuffer;

    public OrderedGroup(Group<OrderedMessage<M>> messageGroup) {
        this.messageGroup = messageGroup;
        this.timestamp = new VectorTimestamp();
        this.messageBuffer = new MessageBuffer<OrderedMessage<M>>();
    }

    public synchronized void send(M message) {
        this.timestamp.increment(App.uuid);
        messageGroup.send(new OrderedMessage<M>(App.uuid, this.timestamp, message));
    }

    public void onReceipt(Callback<M> callback) {
        this.messageGroup.onReceipt(message -> receive(message, callback));
    }

    synchronized void receive(OrderedMessage<M> message, Callback<M> callback) {
        // first, compare local timestamp with timestamp of received message
        int order = timestamp.compareTo(message.getTimestamp(), message.getSenderId());
        if (order == 0) {
            // if received timestamp is expected deliver the message
            timestamp.merge(message.getTimestamp());
            if (callback != null) {
                callback.process(message.getInnerMessage());
            } else {
                LOGGER.info("No callback attached -> message ignored");
            }
        } else if (order == -1) {
            // if received timestamp lies in the future, buffer message
            messageBuffer.add(message);
        }
        // otherwise ignore message as it has already been delivered in the past

        // check the message buffer for deliverable messages and deliver them if possible
        messageBuffer.applyAndRemoveIfTrue(
                bufferedMessage -> this.timestamp.compareTo(bufferedMessage.getTimestamp(),
                        bufferedMessage.getSenderId())
                        == 0,
                bufferedMessage -> {
                    timestamp.merge(bufferedMessage.getTimestamp());
                    if (callback != null) {
                        callback.process(bufferedMessage.getInnerMessage());
                    } else {
                        LOGGER.info("No callback attached -> message ignored");
                    }
                    return true;
                });
    }

    public void join() {
        messageGroup.join();
        timestamp = new VectorTimestamp();
    }

    public void leave() {
        messageGroup.leave();
    }
}
