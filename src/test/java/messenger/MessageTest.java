package messenger;

import crdt.Operation;
import messenger.message.Message;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class MessageTest {

    @Test
    public void SerializingAndDeserializingIsTransient() throws IOException {
        Operation op = TestUtil.createOperation();
        Message in = new CRDTMessage(op);

        Message out = Message.deserialize(Message.serialize(in));
        Assert.assertEquals(in, out);
    }
}
