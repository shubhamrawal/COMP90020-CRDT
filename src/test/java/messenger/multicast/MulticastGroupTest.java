package messenger.multicast;

import crdt.Operation;
import messenger.CRDTMessage;
import messenger.message.Callback;
import messenger.message.Message;
import org.junit.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MulticastGroupTest {

    @Test
    public void SentMessageIsReceived() {
        MulticastGroup group = new MulticastGroup<Message>("224.224.224.2", 9999);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation op = new Operation();
        Message message = new CRDTMessage(uuid, op);
        Callback callback = mock(Callback.class);

        group.onReceipt(callback);
        group.join();
        group.send(message);
        group.leave();

        verify(callback).process(message);
    }
}
