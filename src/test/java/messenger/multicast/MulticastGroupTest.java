package messenger.multicast;

import crdt.Operation;
import messenger.CRDTMessage;
import messenger.TestUtil;
import messenger.message.Callback;
import messenger.message.Message;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MulticastGroupTest {

    @Test
    public void SentMessageIsReceived() {
        MulticastGroup<Message> group = new MulticastGroup<Message>("224.0.0.15", 9999);
        Operation op = TestUtil.createOperation();
        Message message = new CRDTMessage(op);
        Callback<Message> callback = mock(Callback.class);

        group.onReceipt(callback);
        group.join();
        group.send(message);
        group.leave();

        verify(callback).process(message);
    }

    @Test
    public void info() throws Exception{
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
            if(netint.supportsMulticast()) {
                displayInterfaceInformation(netint);
            }
    }

    private void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        System.out.printf("Display name: %s\n", netint.getDisplayName());
        System.out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.printf("InetAddress: %s\n", inetAddress);
        }
        System.out.printf("\n");
    }
}
