package messenger.ordering;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MessageBufferTest {

    @Test
    public void correctMessageIsOperatedAndRemoved() {
        MessageBuffer<OrderedMessage> buffer = new MessageBuffer<OrderedMessage>();
        OrderedMessage msg1 = mock(OrderedMessage.class);
        OrderedMessage msg2 = mock(OrderedMessage.class);

        buffer.add(msg1);
        buffer.add(msg2);

        buffer.applyAndRemoveIfTrue(message -> message == msg1, message -> {message.getTimestamp(); return true;});

        verify(msg1).getTimestamp();
        verifyZeroInteractions(msg2);
        Assert.assertTrue(buffer.getSize() == 1);
    }

}
