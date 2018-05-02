package network;

import network.multicast.MulticastGroup;

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

    public Group create(String multicastAddress, int multicastPort) {
        Group group = new MulticastGroup(multicastAddress, multicastPort);
        return group;
    }
}
