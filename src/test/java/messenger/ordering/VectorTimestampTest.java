package messenger.ordering;

import org.junit.Test;
import org.junit.Assert;

import java.util.HashMap;
import java.util.UUID;

public class VectorTimestampTest {

    @Test
    public void test() {
    	//vectorTimestamp of process1
    	UUID uuid1 = UUID.fromString("235569AB-F4F5-47D7-92C1-9B14505CFFA1");
    	HashMap<UUID,Integer> logicalClock1 = new HashMap<UUID,Integer>();
    	logicalClock1.put(uuid1,0);
        VectorTimestamp vTimestamp1 = new VectorTimestamp(logicalClock1);
        
        //vectorTimestamp of process2 and an event happened at process 2
        UUID uuid2 = UUID.fromString("9CEBC96F-51BE-4A7B-8376-F75BFCD0F24E");
        VectorTimestamp vTimestamp2 = new VectorTimestamp();
        vTimestamp2.increment(uuid2);
        
        //vectorTimestamp of process3
        UUID uuid3 = UUID.fromString("62F26D29-7A74-467E-9CA4-0D8A20F176EA");
        HashMap<UUID,Integer> logicalClock3 = new HashMap<UUID,Integer>();
        logicalClock3.put(uuid3, 0);
        VectorTimestamp vTimestamp3 = new VectorTimestamp();
        
        //1. normal multicast case
        //expected to be deliverable
        Assert.assertEquals(0, vTimestamp1.compareTo(vTimestamp2, uuid2));
        Assert.assertEquals(0, vTimestamp3.compareTo(vTimestamp2, uuid2));
        
        //then processes 1 and 3 are merged to (0,1,0)
        vTimestamp1.merge(vTimestamp2);
        vTimestamp3.merge(vTimestamp2);
        
        // all processes are expected to have same vector clock
        Assert.assertEquals(vTimestamp1, vTimestamp3);
        Assert.assertEquals(vTimestamp3, vTimestamp2);
        
        // 2. buffered case!!
        //event happened at process 1
        vTimestamp1.increment(uuid1);
        
        //should be deliverable at process 2
        Assert.assertEquals(0, vTimestamp2.compareTo(vTimestamp1, uuid1));
        //merge process2 to (1,1,0)
        vTimestamp2.merge(vTimestamp1);
        
        //and event happened again at process 2 and send it to process 3
        //before process 3 received the message from process1
        vTimestamp2.increment(uuid2);
        
        //expected to be future message to process 3 then
        Assert.assertEquals(-1, vTimestamp3.compareTo(vTimestamp2, uuid2));
        
        //wait for message from process 1 before merge with process 3
        vTimestamp3.merge(vTimestamp1);
        vTimestamp3.merge(vTimestamp2);
        
        //expected to be deliverable for process 1 for the message from process 2
        Assert.assertEquals(0, vTimestamp1.compareTo(vTimestamp2, uuid2));
        vTimestamp1.merge(vTimestamp2);
        
        //expected to have all the same vectorclock as (1,2,0)
        Assert.assertEquals(vTimestamp1, vTimestamp2);
        Assert.assertEquals(vTimestamp2, vTimestamp3);
        
        //3. past case!!
        //when process 3 receives the past message from both process 1 and 2
        // expected past message to process 3
        vTimestamp3.increment(uuid3);
        Assert.assertEquals(1, vTimestamp3.compareTo(vTimestamp1, uuid1));
        Assert.assertEquals(1, vTimestamp3.compareTo(vTimestamp2, uuid2));
    }

}
