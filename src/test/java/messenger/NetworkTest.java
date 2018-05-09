package messenger;

import crdt.Operation;
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
        Operation operation = TestUtil.createOperation();
        CRDTMessage crdtMessage = new CRDTMessage(uuid, operation);

        processB.onReceipt(callback);
        processB.join();
        processA.send(crdtMessage);
        processA.leave();
        processB.leave();

        verify(callback).process(new CRDTMessage(uuid, operation));
    }

    @Test
    public void ASendsAndReceivesOwnMessage() {
        CRDTGroup processA = Network.getInstance().create("224.224.224.2", 9997);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation operation = TestUtil.createOperation();
        CRDTMessage crdtMessage = new CRDTMessage(uuid, operation);
        CRDTCallback crdtCallback = mock(CRDTCallback.class);

        processA.onReceipt(crdtCallback);
        processA.join();
        processA.send(crdtMessage);
        processA.leave();

        verify(crdtCallback).process(new CRDTMessage(uuid, operation));
    }

}
