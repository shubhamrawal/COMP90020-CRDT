package messenger.message;

public interface Callback<M extends Message> {

    void process(M message);
}
