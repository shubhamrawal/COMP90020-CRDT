package texteditor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import messenger.CRDTMessage;
import messenger.message.Message;

public class MulticastReceiver implements Runnable {
	protected MulticastSocket socket = null;
    protected byte[] buf = new byte[1024];
 
    public void run() {
    		try {
    			socket = new MulticastSocket(4446);
    			InetAddress group = InetAddress.getByName("239.250.250.250");
    			socket.joinGroup(group);
    	        while (true) {
    	            DatagramPacket packet = new DatagramPacket(buf, buf.length);
    	            socket.receive(packet);
    	            CRDTMessage message = (CRDTMessage)Message.deserialize(packet.getData());
    	            System.out.println(message.getOperation().getAtom().toString());
    	            if(message.getOperation().getAtom().toString().equals("end")) {
    	            		break;
    	            }
    	        }
    	        socket.leaveGroup(group);
    	        socket.close();
    		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
	
}
