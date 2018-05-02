package network.multicast;

import crdt.Operation;
import network.Callback;
import network.Group;
import network.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class MulticastGroup implements Group {

    private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName() );
    private String multicastHost;
    private int multicastPort;
    private InetAddress receiveInetAddress;
    private MulticastSocket receiveMulticastSocket;
    private Callback callback;
    private ExecutorService service = Executors.newFixedThreadPool(1);
    private Future future;

    //multicast host must be between 224.0.0.0 to 239.255.255.255
    public MulticastGroup(String multicastHost, int multicastPort) {
        this.multicastHost = multicastHost;
        this.multicastPort = multicastPort;
    }

    public void send(UUID memberId, Operation operation) {
        // TODO increment vector timestamp
        // TODO add timestamp to message
        try {
            sendMulticast(Message.serialize(new Message(memberId, operation)));
        } catch (IOException e) {
            LOGGER.severe("Message could not be serialized and therefore not sent");
        }
    }

    public void onReceipt(Callback callback) {
        this.callback = callback;
    }

    public void join() {
        try {
            receiveMulticast();
        } catch (IOException e) {
            LOGGER.severe("Could not join group");
        }
    }

    @Override
    public void leave() {
        if (future != null){
            future.cancel(true);
            future = null;
            leave();
        }
        if (receiveMulticastSocket != null && receiveInetAddress != null) {
            try {
                receiveMulticastSocket.leaveGroup(receiveInetAddress);
            } catch (IOException e) {
                LOGGER.warning("Could not leave group");
            }
        }
    }

    private void sendMulticast(byte[] multicastData) throws IOException {
        InetAddress group = InetAddress.getByName(this.multicastHost);
        try(MulticastSocket socket = new MulticastSocket()){
            socket.send(new DatagramPacket(multicastData, multicastData.length, group, this.multicastPort));
        }
    }

    private void receive(byte[] data) {
        try {
            Message message = Message.deserialize(data);
            if (callback != null) {
                callback.merge(message.getMemberId(), message.getOperation());
            }
        } catch (IOException e) {
            LOGGER.warning("Received data could not be turned into valid message object");
            //if data could not be turned into a valid message, ignore it
        }
    }

    private void receiveMulticast() throws IOException {
        if (future != null){
            future.cancel(true);
            future = null;
            leave();
        }
        receiveInetAddress = InetAddress.getByName(multicastHost);
        receiveMulticastSocket = new MulticastSocket(multicastPort);
        receiveMulticastSocket.joinGroup(receiveInetAddress);
        future = service.submit(() -> {
            byte[] buf = new byte[2048];
            while (true) {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                receiveMulticastSocket.receive(msgPacket);
                receive(msgPacket.getData());
            }
        });
    }
}
