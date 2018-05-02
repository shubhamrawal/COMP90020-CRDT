package network;

import crdt.Operation;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

public class Message implements Serializable {

    private UUID memberId;
    private Operation operation;

    public Message(UUID memberId, Operation operation) {

        this.memberId = memberId;
        this.operation = operation;
    }

    public static byte[] serialize(Message message) throws IOException {
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

    public static Message deserialize(byte[] data) throws IOException {
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

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(getMemberId(), message.getMemberId()) &&
                Objects.equals(getOperation(), message.getOperation());
    }

}
