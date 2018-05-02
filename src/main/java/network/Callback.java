package network;

import crdt.Operation;

import java.util.UUID;

public interface Callback {

    void merge(UUID memberId, Operation operation);
}
