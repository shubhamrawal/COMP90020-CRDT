package network;

import crdt.Operation;

import java.util.UUID;

public interface Group {

    void send(UUID memberId, Operation operation);

    void onReceipt(Callback callback);

    void join();

    void leave();
}
