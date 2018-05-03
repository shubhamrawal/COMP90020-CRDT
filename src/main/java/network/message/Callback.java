package network.message;

public interface Callback<M extends Message> {

    void process(M message);
}
