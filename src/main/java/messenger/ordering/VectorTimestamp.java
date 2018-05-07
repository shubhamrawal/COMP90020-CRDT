package messenger.ordering;

//import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.Vector;

class VectorTimestamp implements Serializable {

	private static final long serialVersionUID = 6755210344616768108L;
	private HashMap<UUID,Integer> logicalClocks;

	VectorTimestamp() {
		this.logicalClocks = new HashMap<UUID,Integer>();
	}

	VectorTimestamp(HashMap<UUID,Integer> logicalClocks) {
		this.logicalClocks = logicalClocks;
	}
	
	public int getTimestamp(UUID processID) {
		return logicalClocks.get(processID);
	}
	/*
	 * *
	 * look for the past timestamp only
	 * doesn't care whether concurrent, greater or greater equal case
	 * because concurrent case was handled by isDelivable method
	 */
	public boolean isPast(VectorTimestamp other){
		//generate union key set between local and other logicalClock
		Vector<UUID> keys = new Vector<UUID>();
		keys.addAll(logicalClocks.keySet());
		for (UUID u: other.getVector().keySet()) {
			if(!keys.contains(u)) {
				keys.add(u);
			}
		}
		// compare the value associated with each processID by iterating the union key set.
		boolean first = true;
		int less = -2;
		int equal = 0;
		int less_eq = -1;
		int concurrent = 3;
		int greaterOrgreater_eq = 2;
		int result = concurrent;
		for (UUID id: keys) {
			int v1 = logicalClocks.get(id);
			int v2 = other.getVector().get(id);
			
			if (v1 < v2) {
				if (first || result == less){
					result = less;
				}
				else{
					if (result == less_eq || result == equal) {
						result = less_eq;
					}
					else{
						result = concurrent;
						break;
					}
				}
			}
			else if (v1 == v2) {
				if (first || result == equal) {
					result =equal;
				}
				else {
					if(result == less_eq || result == less) {
						result = less_eq;
					} else {
						result = greaterOrgreater_eq;
						break;
					}
				}
			}
			else{
				result = greaterOrgreater_eq;
				break;
			}
			first = false;
		}
		if (result == less ||result == less_eq) {
			return true;
		}
		return false;
	}
	
	public boolean isDeliverable(VectorTimestamp other, UUID sender) {
		int greater = 0;
		int less = 0;
		int sent = other.getTimestamp(sender);
		int seen = logicalClocks.get(sender);
		UUID processID;
		if (sent == (seen + 1)) {
			for (Entry<UUID, Integer> entry: logicalClocks.entrySet()) {
				processID = entry.getKey();
				if (!processID.equals(sender)) {
					if (other.getTimestamp(processID) > logicalClocks.get(processID))
						greater ++;
					else if (other.getTimestamp(processID) < logicalClocks.get(processID))
						less ++;
				}
			}
			// concurrent case
			if (greater >= 1 && less >= 1) {
				return true;
			}
			// satisfied condition to deliver
			else if (greater == 0) {
				return true;
			}
		}
		return false;	
	}
	
	public HashMap<UUID, Integer> getVector() {
		return logicalClocks;
	}

	void increment(UUID processId) {
		if (!logicalClocks.containsKey(processId)) {
			logicalClocks.put(processId, 0);
		}
		int value = logicalClocks.get(processId) + 1;
		logicalClocks.put(processId, value);
	}
	
	public void set(UUID processID, int value) {
		logicalClocks.put(processID, value);
	}
	
	public void merge(VectorTimestamp other) {
		HashMap<UUID, Integer> union = new HashMap<UUID, Integer>();
		union.putAll(this.logicalClocks);
		for(Entry<UUID, Integer> entry : other.logicalClocks.entrySet()) {
			if(!union.containsKey(entry.getKey())) {
				union.put(entry.getKey(), entry.getValue());
			} else if (union.get(entry.getKey()) < entry.getValue()) {
				union.put(entry.getKey(), entry.getValue());
			}
		}
		this.logicalClocks = union;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VectorTimestamp that = (VectorTimestamp) o;
        return Objects.equals(logicalClocks, that.logicalClocks);
    }
}
