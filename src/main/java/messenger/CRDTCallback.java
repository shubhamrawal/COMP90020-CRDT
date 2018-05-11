package messenger;

import crdt.*;
import messenger.message.Callback;

public class CRDTCallback implements Callback<CRDTMessage> {
	private TreeReplicatedDocument doc;
	
	public void addListener(TreeReplicatedDocument doc) {
		this.doc = doc;
	}

    public void process(CRDTMessage crdtMessage) {
    		System.out.println("received");
        merge(crdtMessage.getOperation());
    }

    private void merge(Operation operation) {
    		OperationType type = operation.getType();
    		Position posId = operation.getPosId();
    		switch(type) {
    			case INSERT:
    				Atom value = operation.getAtom();
    				if(value != null) {
    					doc.remoteInsert(posId, value);
    				}
    				break;
    			case DELETE:
    				doc.remoteDelete(posId);
    				break;
    		}
    }
}
