package messenger.ordering;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

public class VectorTimestamp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6755210344616768108L;
	private HashMap<UUID,Integer> vectorTs;

	public VectorTimestamp() {
		this.vectorTs = new HashMap<UUID,Integer>();
	}
	
	public int getTimestamp(UUID processID) {
		return vectorTs.get(processID);
	}

	/*
	 *  returns 1 if other timestamp is in the past
	 *  		0 if other timestamp can be delivered
	 *  		-1 if other timestamp is in the future
	 */
	public int compareTo(VectorTimestamp other, UUID sender){

		boolean deliverable = true;
		boolean past = true;

		for (Entry<UUID, Integer> entry: vectorTs.entrySet()) {
			if(entry.getValue() < other.getTimestamp(entry.getKey())){
				past = false;
			}
			if (entry.getKey().equals(sender)) {
				if(entry.getValue() + 1 != other.getTimestamp(sender)) {
					deliverable = false;
				}
			} else {
				if(entry.getValue() < other.getTimestamp(entry.getKey())){
					deliverable = false;
				}
			}
		}
		
		if (deliverable) {
			return 0;
		} else {
			return past ? 1 : -1;
		}
	}
	
	public HashMap<UUID, Integer> getVector() {
		return vectorTs;
	}

	public void increment(UUID process) {
		if (!vectorTs.containsKey(process)) {
			vectorTs.put(process, 0);
		}
		int value = vectorTs.get(process) + 1;
		vectorTs.put(process, value);
	}
	
	public void merge(VectorTimestamp other) {
		// create union set of all process ids
		HashMap<UUID, Integer> union = new HashMap<UUID, Integer>();
		union.putAll(this.vectorTs);
		for(Entry<UUID, Integer> entry : other.vectorTs.entrySet()) {
			if(!union.containsKey(entry.getKey())) {
				union.put(entry.getKey(), entry.getValue());
			} else if (union.get(entry.getKey()) < entry.getValue()) {
				union.put(entry.getKey(), entry.getValue());
			}
		}
		
		this.vectorTs = union;
	}

}
