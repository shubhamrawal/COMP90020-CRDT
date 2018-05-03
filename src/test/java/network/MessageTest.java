package network;

import crdt.Operation;
import network.message.Message;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class MessageTest {

    @Test
    public void SerializingAndDeserializingIsTransient() throws IOException {
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
        Operation op = new Operation();
        Message in = new CRDTMessage(uuid, op);

        Message out = Message.deserialize(Message.serialize(in));
        Assert.assertEquals(in, out);
    }
}
