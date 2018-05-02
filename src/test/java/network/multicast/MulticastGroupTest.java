package network.multicast;

import crdt.Operation;
import network.Callback;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MulticastGroupTest {

    @Test
    public void SentMessageIsReceived() throws IOException {
        MulticastGroup group = new MulticastGroup("224.224.224.2", 9999);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation operation = new Operation();
        Callback callback = mock(Callback.class);

        group.onReceipt(callback);
        group.join();
        group.send(uuid, operation);
        group.leave();

        verify(callback).merge(uuid, operation);
    }
}
