package messenger;

import crdt.Atom;
import crdt.Operation;
import crdt.OperationType;
import crdt.Position;
import crdt.TreeReplicatedDocument;
import messenger.message.Callback;

import java.util.UUID;

public class CRDTCallback implements Callback<CRDTMessage> {
	private TreeReplicatedDocument doc;
	
	public void addListner(TreeReplicatedDocument doc) {
		this.doc = doc;
	}

    public void process(CRDTMessage crdtMessage) {
        merge(crdtMessage.getSenderId(), crdtMessage.getOperation());
    }

    private void merge(UUID senderId, Operation operation) {
        //TODO implement merge here
    		OperationType type = operation.getType();
    		Position posId = operation.getPosId();
    		switch(type) {
    			case INSERT:
    				Atom value = operation.getAtom();
    				doc.remoteInsert(posId, value);
    				break;
    			case DELETE:
    				break;
    		}
    }
}
