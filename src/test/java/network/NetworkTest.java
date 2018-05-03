package network;

import crdt.Operation;
import network.message.Message;
import org.junit.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NetworkTest {

    @Test
    public void ASendsAndBReceivesSameMessage() {
        CRDTGroup processA = Network.getInstance().create("224.224.224.2", 9998);
        CRDTGroup processB = Network.getInstance().create("224.224.224.2", 9998);
        CRDTCallback callback = mock(CRDTCallback.class);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation operation = new Operation();

        processB.onReceipt(callback);
        processB.join();
        processA.send(uuid, operation);
        processA.leave();
        processB.leave();

        verify(callback).process((Message) new CRDTMessage(uuid, operation));
    }

    @Test
    public void ASendsAndReceivesOwnMessage() {
        CRDTGroup processA = Network.getInstance().create("224.224.224.2", 9998);
        CRDTCallback crdtCallback = mock(CRDTCallback.class);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation operation = new Operation();

        processA.onReceipt(crdtCallback);
        processA.join();
        processA.send(uuid, operation);
        processA.leave();

        verify(crdtCallback).process((Message) new CRDTMessage(uuid, operation));
    }

}
