package messenger;

import crdt.Atom;
import crdt.Operation;
import crdt.OperationType;
import crdt.Position;

import java.util.UUID;

public class TestUtil {

    public static Operation createOperation() {
        UUID uuid = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dffff");
        return new Operation(OperationType.INSERT, new Position("test", "test", uuid), new Atom('t'));
    }
}
