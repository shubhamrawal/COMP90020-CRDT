package messenger.ordering;

import crdt.Operation;
import crdt.OperationType;
import messenger.CRDTMessage;
import messenger.TestUtil;
import messenger.message.Callback;
import messenger.message.Group;
import org.junit.Test;
import texteditor.App;

import java.util.HashMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static texteditor.App.uuid;

public class OrderedGroupTest {

    @Test
    public void sentMessageIsForwarded() {
        Group<OrderedMessage<CRDTMessage>> groupMock = mock(Group.class);
        OrderedGroup<CRDTMessage> orderedGroup = new OrderedGroup<CRDTMessage>(groupMock);
        Operation op = TestUtil.createOperation();
        CRDTMessage message = new CRDTMessage(op);
        Callback<CRDTMessage> callback = mock(Callback.class);

        orderedGroup.onReceipt(callback);
        orderedGroup.join();
        orderedGroup.send(message);
        orderedGroup.leave();

        HashMap<UUID, Integer> expectedLClocks = new HashMap<>();
        expectedLClocks.put(uuid, 1);
        verify(groupMock).send(new OrderedMessage<>(uuid, new VectorTimestamp(expectedLClocks), message));
    }

    @Test
    public void pastMessageIsIgnored() {
        Group<OrderedMessage<CRDTMessage>> groupMock = mock(Group.class);
        OrderedGroup<CRDTMessage> orderedGroup = new OrderedGroup<CRDTMessage>(groupMock);
        CRDTMessage sentMessage = mock(CRDTMessage.class);
        Callback<CRDTMessage> callback = mock(Callback.class);

        //received stuff
        CRDTMessage innerMessage = mock(CRDTMessage.class);
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(App.uuid, 1);
        OrderedMessage receivedMessage = new OrderedMessage(App.uuid, new VectorTimestamp(logicalClocks), innerMessage);

        // set up the scene
        orderedGroup.onReceipt(callback);
        orderedGroup.join();
        orderedGroup.send(sentMessage);
        orderedGroup.send(sentMessage);

        //local timestamp is now (2, 0, 0, ...)
        // received timestamp is (1, 0, 0, ...)
        orderedGroup.receive(receivedMessage, callback);

        verify(callback, never()).process(any());
    }

    @Test
    public void expectedMessageIsDeliveredImmediately() {
        Group<OrderedMessage<CRDTMessage>> groupMock = mock(Group.class);
        OrderedGroup<CRDTMessage> orderedGroup = new OrderedGroup<CRDTMessage>(groupMock);
        CRDTMessage sentMessage = mock(CRDTMessage.class);
        Callback<CRDTMessage> callback = mock(Callback.class);

        //received stuff
        CRDTMessage innerMessage = mock(CRDTMessage.class);
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(App.uuid, 3);
        OrderedMessage receivedMessage = new OrderedMessage(App.uuid, new VectorTimestamp(logicalClocks), innerMessage);

        // set up the scene
        orderedGroup.onReceipt(callback);
        orderedGroup.join();
        orderedGroup.send(sentMessage);
        orderedGroup.send(sentMessage);

        //local timestamp is now (2, 0, 0, ...)
        // received timestamp is (3, 0, 0, ...)
        orderedGroup.receive(receivedMessage, callback);

        verify(callback).process(innerMessage);
    }

    @Test
    public void firstMessageFromUnknownProcessIsDeliveredImmediately() {
        Group<OrderedMessage<CRDTMessage>> groupMock = mock(Group.class);
        OrderedGroup<CRDTMessage> orderedGroup = new OrderedGroup<CRDTMessage>(groupMock);
        CRDTMessage sentMessage = mock(CRDTMessage.class);
        Callback<CRDTMessage> callback = mock(Callback.class);

        //received stuff
        CRDTMessage innerMessage = mock(CRDTMessage.class);
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(App.uuid, 1);
        UUID senderId = UUID.randomUUID();
        logicalClocks.put(senderId, 1);
        OrderedMessage receivedMessage = new OrderedMessage(senderId, new VectorTimestamp(logicalClocks), innerMessage);

        // set up the scene
        orderedGroup.onReceipt(callback);
        orderedGroup.join();
        orderedGroup.send(sentMessage);
        orderedGroup.send(sentMessage);

        //local timestamp is now (2, 0, 0, ...)
        // received timestamp is (1, 0, 1, ...)
        orderedGroup.receive(receivedMessage, callback);

        verify(callback).process(innerMessage);
    }

    @Test
    public void futureMessageIsBufferedAndDeliveredInOrder() {
        Group<OrderedMessage<CRDTMessage>> groupMock = mock(Group.class);
        OrderedGroup<CRDTMessage> orderedGroup = new OrderedGroup<CRDTMessage>(groupMock);
        CRDTMessage sentMessage = mock(CRDTMessage.class);
        Callback<CRDTMessage> callback = mock(Callback.class);

        // received stuff
        CRDTMessage innerMessage = mock(CRDTMessage.class, RETURNS_DEEP_STUBS);
        // only deletions are buffered
        when(innerMessage.getOperation().getType()).thenReturn(OperationType.DELETE);
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(App.uuid, 4);
        OrderedMessage receivedMessage = new OrderedMessage(App.uuid, new VectorTimestamp(logicalClocks), innerMessage);

        // set up the scene
        orderedGroup.onReceipt(callback);
        orderedGroup.join();
        orderedGroup.send(sentMessage);
        orderedGroup.send(sentMessage);

        // local timestamp is now (2, 0, 0, ...)
        // received timestamp is (4, 0, 0, ...)
        orderedGroup.receive(receivedMessage, callback);

        verify(callback, never()).process(any());

        HashMap<UUID, Integer> logicalClocks2 = new HashMap<>();
        logicalClocks.put(App.uuid, 3);
        OrderedMessage receivedMessage2 = new OrderedMessage(App.uuid, new VectorTimestamp(logicalClocks2), innerMessage);

        //local timestamp is still (2, 0, 0, ...)
        //received timestamp is (4, 0, 0, ...)
        orderedGroup.receive(receivedMessage2, callback);

        verify(callback, times(2)).process(innerMessage);
    }
}
