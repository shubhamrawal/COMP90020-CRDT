package network.message;

public interface Group<M extends Message> {

    void send(M message);

    void onReceipt(Callback<M> callback);

    void join();

    void leave();
}
