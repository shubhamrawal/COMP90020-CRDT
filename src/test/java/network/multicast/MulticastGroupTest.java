package network.multicast;

import crdt.Operation;
import network.CRDTMessage;
import network.message.Callback;
import network.message.Message;
import org.junit.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MulticastGroupTest {

    @Test
    public void SentMessageIsReceived() {
        MulticastGroup group = new MulticastGroup("224.224.224.2", 9999);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation op = new Operation();
        Message message = new CRDTMessage(uuid, op);
        Callback<Message> callback = mock(Callback.class);

        group.onReceipt(callback);
        group.join();
        group.send(message);
        group.leave();

        verify(callback).process(message);
    }
}
