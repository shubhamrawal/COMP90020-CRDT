package messenger;

import crdt.Operation;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class NetworkTest {

    @Test
    public void ASendsAndBReceivesSameMessage() {
        CRDTGroup processA = Network.getInstance().create("224.224.224.3", 9998);
        CRDTGroup processB = Network.getInstance().create("224.224.224.3", 9998);
        Operation operation = TestUtil.createOperation();
        CRDTMessage crdtMessage = new CRDTMessage(operation);
        CRDTCallback callback = mock(CRDTCallback.class);

        //note that the processes share the same uuid through App.uuid

        processB.onReceipt(callback);
        processB.join();
        processA.send(crdtMessage);
        processA.leave();
        processB.leave();

        // TODO flaky test: fails when debugging it!!!
        verify(callback).process(crdtMessage);
    }

    @Test
    public void ownMessageIsIgnored() {
        CRDTGroup processA = Network.getInstance().create("224.224.224.2", 9997);
        Operation operation = TestUtil.createOperation();
        CRDTMessage crdtMessage = new CRDTMessage(operation);
        CRDTCallback crdtCallback = mock(CRDTCallback.class);

        processA.onReceipt(crdtCallback);
        processA.join();
        processA.send(crdtMessage);
        processA.leave();

        verify(crdtCallback, never()).process(new CRDTMessage(operation));
    }

}
