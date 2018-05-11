package messenger.ordering;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.UUID;

public class VectorTimestampTest {

	private UUID uuid1;
    private UUID uuid2;
    private UUID uuid3;
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
