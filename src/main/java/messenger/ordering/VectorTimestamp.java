package messenger.ordering;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

class VectorTimestamp implements Serializable {

	private static final long serialVersionUID = 6755210344616768108L;
	private HashMap<UUID,Integer> logicalClocks;

	VectorTimestamp() {
		this.logicalClocks = new HashMap<UUID,Integer>();
	}

	VectorTimestamp(HashMap<UUID,Integer> logicalClocks) {
		this.logicalClocks = logicalClocks;
	}

	/*
	 *  returns 1 if other timestamp is in the past
	 *  		0 if other timestamp can be delivered
	 *  		-1 if other timestamp is in the future
	 */
	public int compareTo(VectorTimestamp other, UUID senderId){

		boolean expected = true;
		boolean past = true;

		for (Entry<UUID, Integer> logicalClock: this.logicalClocks.entrySet()) {

			// we skip over any logical clock that is not contained in the other timestamp
			if (other.logicalClocks.containsKey(logicalClock.getKey())) {
				// if any logical clock of the other timestamp is greater than the corresponding local logical clock,
				// then it cannot be a past timestamp
				if(logicalClock.getValue() < other.logicalClocks.get(logicalClock.getKey())){
					past = false;
				}

				// other timestamp is not expected if either of these conditions hold
				if (logicalClock.getKey().equals(senderId)) {
					if(logicalClock.getValue() + 1 != other.logicalClocks.get(senderId)) {
						expected = false;
					}
				} else {
					if(logicalClock.getValue() < other.logicalClocks.get(logicalClock.getKey())){
						expected = false;
					}
				}
			}
		}

		// now we have to deal with logical clocks of the other timestamp that are not part of this one
		Map<UUID, Integer> unknownLClocks = Maps.difference(this.logicalClocks, other.logicalClocks).entriesOnlyOnRight();

		// if other timestamp contains a logical clock that this one does not
		for(Entry<UUID, Integer> unknownLClock : unknownLClocks.entrySet()) {
			if(unknownLClock.getKey().equals(senderId)) {
				// if the unknown logical clock is from the sender
				if(unknownLClock.getValue() != 1) {
					// and the logical clock is not 1,
					// it is a future timestamp and we need to wait for the first timestamp from that sender
					return -1;
				}
			} else {
				// if the unknown logical clock is not from the sender the timestamp is in the future
				return -1;
			}
		}

		if (expected) {
			return 0;
		} else {
			return past ? 1 : -1;
		}
	}

	void increment(UUID processId) {
		if (!logicalClocks.containsKey(processId)) {
			logicalClocks.put(processId, 0);
		}
		int value = logicalClocks.get(processId) + 1;
		logicalClocks.put(processId, value);
	}
	
	void merge(VectorTimestamp other) {
		// add all new logical clocks and update the existing ones to the greater one
		for(Entry<UUID, Integer> entry : other.logicalClocks.entrySet()) {
			this.logicalClocks.merge(entry.getKey(), entry.getValue(),
					(local, remote) -> (remote > local) ? remote : local );
		}
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VectorTimestamp that = (VectorTimestamp) o;
        return Objects.equals(logicalClocks, that.logicalClocks);
    }
}
