package network.message;

import java.io.*;

public interface Message extends Serializable {

    static byte[] serialize(Message message) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(message);
            out.flush();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return bos.toByteArray();
    }

    static Message deserialize(byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        Message message = null;
        try {
            in = new ObjectInputStream(bis);
            message = (Message) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Data could not be deserialized into a message object", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return message;
    }

}
