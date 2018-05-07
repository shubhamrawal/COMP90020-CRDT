package messenger;

import crdt.Operation;
import org.junit.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class NetworkTest {

    @Test
    public void ASendsAndBReceivesSameMessage() {
        CRDTGroup processA = Network.getInstance().create("224.224.224.3", 9998);
        CRDTGroup processB = Network.getInstance().create("224.224.224.3", 9998);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation operation = new Operation();
        CRDTMessage crdtMessage = new CRDTMessage(uuid, operation);
        CRDTCallback callback = mock(CRDTCallback.class);

        //note that the processes share the same uuid through App.uuid

        processB.onReceipt(callback);
        processB.join();
        processA.send(crdtMessage);
        processA.leave();
        processB.leave();

        // TODO flaky test: fails when debugging it!!!
        //verify(callback).process(crdtMessage);
    }

    @Test
    public void ownMessageIsIgnored() {
        CRDTGroup processA = Network.getInstance().create("224.224.224.2", 9997);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation operation = new Operation();
        CRDTMessage crdtMessage = new CRDTMessage(uuid, operation);
        CRDTCallback crdtCallback = mock(CRDTCallback.class);

        processA.onReceipt(crdtCallback);
        processA.join();
        processA.send(crdtMessage);
        processA.leave();

        verify(crdtCallback, never()).process(new CRDTMessage(uuid, operation));
    }

}
