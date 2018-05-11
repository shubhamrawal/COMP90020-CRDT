package texteditor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import messenger.CRDTMessage;
import messenger.message.Message;

public class MulticastSender {
	private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    
    public void multicast(CRDTMessage multicastMessage) throws IOException {
	        socket = new DatagramSocket();
	        group = InetAddress.getByName("239.250.250.250");
	        buf = Message.serialize(multicastMessage);
	 
	        DatagramPacket packet 
	          = new DatagramPacket(buf, buf.length, group, 4446);
	        socket.send(packet);
	        socket.close();
    }
}
