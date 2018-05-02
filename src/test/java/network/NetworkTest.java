package network;

import crdt.Operation;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NetworkTest {

    @Test
    public void ASendsAndBReceivesSameMessage() throws IOException {
        Group processA = Network.getInstance().create("224.224.224.2", 9998);
        Group processB = Network.getInstance().create("224.224.224.2", 9998);
        Callback callback = mock(Callback.class);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation operation = new Operation();

        processB.onReceipt(callback);
        processB.join();
        processA.send(uuid, operation);
        processA.leave();
        processB.leave();

        verify(callback).merge(uuid, operation);
    }

    @Test
    public void ASendsAndReceivesOwnMessage() throws IOException {
        Group processA = Network.getInstance().create("224.224.224.2", 9998);
        Callback callback = mock(Callback.class);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation operation = new Operation();

        processA.onReceipt(callback);
        processA.join();
        processA.send(uuid, operation);
        processA.leave();

        verify(callback).merge(uuid, operation);
    }

}
