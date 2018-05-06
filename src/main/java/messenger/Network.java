package messenger;

import messenger.multicast.MulticastGroup;
import messenger.ordering.OrderedGroup;
import messenger.ordering.OrderedMessage;

public class Network {

    private static Network network;

    private Network() {
    }

    public static Network getInstance() {
        if (Network.network == null) {
            Network.network = new Network();
        }
        return Network.network;
    }

    public CRDTGroup create(String multicastAddress, int multicastPort) {
        return new CRDTGroup(new OrderedGroup<CRDTMessage>(new MulticastGroup<OrderedMessage<CRDTMessage>>(multicastAddress, multicastPort)));
    }
}
