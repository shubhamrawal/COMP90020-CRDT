package messenger.ordering;

import crdt.Operation;
import messenger.CRDTMessage;
import messenger.TestUtil;
import messenger.message.Callback;
import messenger.message.Group;
import messenger.message.Message;
import org.junit.Test;
import texteditor.App;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OrderedGroupTest {

    @Test
    public void sentMessageIsForwarded() {
        Group<OrderedMessage<Message>> groupMock = mock(Group.class);
        OrderedGroup<Message> orderedGroup = new OrderedGroup<Message>(groupMock);
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation op = TestUtil.createOperation();
        Message message = new CRDTMessage(uuid, op);
        Callback<Message> callback = mock(Callback.class);

        orderedGroup.onReceipt(callback);
        orderedGroup.join();
        orderedGroup.send(message);
        orderedGroup.leave();

        HashMap<UUID, Integer> expectedLClocks = new HashMap<>();
        expectedLClocks.put(App.uuid, 1);
        verify(groupMock).send(new OrderedMessage<>(App.uuid, new VectorTimestamp(expectedLClocks), message));
    }

    @Test
    public void messagesAreDeliveredInOrder() {
        //TODO add test
        fail();
    }

    @Test
    public void pastMessageIsIgnored() {
        //TODO add test
        fail();
    }

    @Test
    public void expectedMessageIsDeliveredImmediately() {
        //TODO add test
        fail();
    }

    @Test
    public void futureMessageIsBuffered() {
        //TODO add test
        fail();
    }
}
