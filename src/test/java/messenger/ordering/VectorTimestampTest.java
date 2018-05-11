package messenger.ordering;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.UUID;

public class VectorTimestampTest {
	//vectorTimestamp of process1
	UUID uuid1;
    VectorTimestamp vTimestamp1 = new VectorTimestamp();
    
    //vectorTimestamp of process2
    UUID uuid2;
    VectorTimestamp vTimestamp2 = new VectorTimestamp();
    
    //vectorTimestamp of process3
    UUID uuid3;
    VectorTimestamp vTimestamp3 = new VectorTimestamp();

    private VectorTimestamp localTimestamp;

    @Before
    public void setLocal() {
        uuid1 = UUID.fromString("235569AB-F4F5-47D7-92C1-9B14505CFFA1");
        uuid2 = UUID.fromString("9CEBC96F-51BE-4A7B-8376-F75BFCD0F24E");
        uuid3 = UUID.fromString("62F26D29-7A74-467E-9CA4-0D8A20F176EA");

        //set local timestamp to (2, 3)
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(uuid1, 2);
        logicalClocks.put(uuid2, 3);
        localTimestamp = new VectorTimestamp(logicalClocks);
    }
	
    @Test
    public void testNormalCase() {
    	
        //1. normal multicast case
    	//event happened at process 2
    	vTimestamp2.increment(uuid2);
    	
        //expected to be deliverable
        Assert.assertEquals(0, vTimestamp1.compareTo(vTimestamp2, uuid2));
        Assert.assertEquals(0, vTimestamp3.compareTo(vTimestamp2, uuid2));
        
        //then processes 1 and 3 are merged to (0,1,0)
        vTimestamp1.merge(vTimestamp2);
        vTimestamp3.merge(vTimestamp2);
        
        // all processes are expected to have same vector clock as (0,1,0)
        Assert.assertEquals(vTimestamp1, vTimestamp3);
        Assert.assertEquals(vTimestamp3, vTimestamp2);
        
        
        
        //3. past case!!
        //when process 3 receives the past message from both process 1 and 2
        // expected past message to process 3
        vTimestamp3.increment(uuid3);
        Assert.assertEquals(1, vTimestamp3.compareTo(vTimestamp1, uuid1));
        Assert.assertEquals(1, vTimestamp3.compareTo(vTimestamp2, uuid2));
    }
    
    // proceed with the same vector clock (0,1,0) for all three processes
    @Test
    public void testBufferedCase() {
        //event happened at process 1
        vTimestamp1.increment(uuid1);
        
        //should be deliverable at process 2
        Assert.assertEquals(0, vTimestamp2.compareTo(vTimestamp1, uuid1));
        //merge process2 to (1,1,0)
        vTimestamp2.merge(vTimestamp1);
        
        //and event happened again at process 2 
        vTimestamp2.increment(uuid2);
        //process 3 received the message sent by process 2 first
        //before it received the message sent by process1
        //expected to be future message at process 3
        Assert.assertEquals(-1, vTimestamp3.compareTo(vTimestamp2, uuid2));
        
        //wait for the message from process 1 before merge with process 3
        vTimestamp3.merge(vTimestamp1);
        // expected to be deliverable after being merged with message sent by process 1
        Assert.assertEquals(0, vTimestamp3.compareTo(vTimestamp2, uuid2));
        vTimestamp3.merge(vTimestamp2);
        
        //expected to be deliverable at process 1 for the message sent by process 2
        Assert.assertEquals(0, vTimestamp1.compareTo(vTimestamp2, uuid2));
        vTimestamp1.merge(vTimestamp2);
        
        //expected to have all the same vector clock as (1,2,0)
        Assert.assertEquals(vTimestamp1, vTimestamp2);
        Assert.assertEquals(vTimestamp2, vTimestamp3);
    }
    
    // proceed with the same vector clock as (1,2,0) for all three processes
    @Test
    public void testPastCase() {
    	//event happened at process 3
    	vTimestamp3.increment(uuid3);
    	//process 3 was received past message sent by process1 and 2
        // expected to be past message to process 3
        Assert.assertEquals(1, vTimestamp3.compareTo(vTimestamp1, uuid1));
        Assert.assertEquals(1, vTimestamp3.compareTo(vTimestamp2, uuid2));
    }

    @Test
    //compare local with other past timestamp (2, 3)
    public void pastMessage1(){
        Assert.assertEquals(1, localTimestamp.compareTo(localTimestamp, uuid1));
    }

    @Test
    //compare local with other past timestamp (2, 2)
    public void pastMessage2() {
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(uuid1, 2);
        logicalClocks.put(uuid2, 2);
        VectorTimestamp otherTimestamp = new VectorTimestamp(logicalClocks);

        Assert.assertEquals(1, localTimestamp.compareTo(otherTimestamp, uuid2));
    }

    @Test
    // compare local with other future timestamp (2, 3, 1)
    public void firstMessageOfNewProcess() {
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(uuid1, 2);
        logicalClocks.put(uuid2, 3);
        logicalClocks.put(uuid3, 1);
        VectorTimestamp otherTimestamp = new VectorTimestamp(logicalClocks);

        Assert.assertEquals(0, localTimestamp.compareTo(otherTimestamp, uuid3));
    }

    @Test
    // compare local with other future timestamp (2, 4)
    public void expectedMessage1() {
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(uuid1, 2);
        logicalClocks.put(uuid2, 4);
        VectorTimestamp otherTimestamp = new VectorTimestamp(logicalClocks);

        Assert.assertEquals(0, localTimestamp.compareTo(otherTimestamp, uuid2));
    }

    @Test
    // compare local with other future timestamp (3)
    public void expectedMessage2() {
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(uuid1, 3);
        VectorTimestamp otherTimestamp = new VectorTimestamp(logicalClocks);

        Assert.assertEquals(0, localTimestamp.compareTo(otherTimestamp, uuid1));
    }

    @Test
    // compare local with other future timestamp (2, 5)
    public void futureMessage1() {
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(uuid1, 2);
        logicalClocks.put(uuid2, 5);
        VectorTimestamp otherTimestamp = new VectorTimestamp(logicalClocks);

        Assert.assertEquals(-1, localTimestamp.compareTo(otherTimestamp, uuid2));
    }

    @Test
    // compare local with other future timestamp (1, 5)
    public void futureMessage2() {
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(uuid1, 1);
        logicalClocks.put(uuid2, 5);
        VectorTimestamp otherTimestamp = new VectorTimestamp(logicalClocks);

        Assert.assertEquals(-1, localTimestamp.compareTo(otherTimestamp, uuid2));
    }

    @Test
    // compare local with other future timestamp (2, 3, 2)
    public void futureMessage3() {
        HashMap<UUID, Integer> logicalClocks = new HashMap<>();
        logicalClocks.put(uuid1, 2);
        logicalClocks.put(uuid2, 3);
        logicalClocks.put(uuid3, 2);
        VectorTimestamp otherTimestamp = new VectorTimestamp(logicalClocks);

        Assert.assertEquals(-1, localTimestamp.compareTo(otherTimestamp, uuid2));
    }

}
