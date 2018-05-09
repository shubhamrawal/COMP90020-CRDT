package messenger.multicast;

import crdt.Operation;
import messenger.CRDTMessage;
import messenger.TestUtil;
import messenger.message.Callback;
import messenger.message.Message;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MulticastGroupTest {

    @Test
    public void SentMessageIsReceived() {
        MulticastGroup<Message> group = new MulticastGroup<Message>("224.224.224.2", 9999);
        Operation op = TestUtil.createOperation();
        Message message = new CRDTMessage(op);
        Callback<Message> callback = mock(Callback.class);

        group.onReceipt(callback);
        group.join();
        group.send(message);
        group.leave();

        verify(callback).process(message);
    }
}
