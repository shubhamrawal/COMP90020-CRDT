package crdt;

import messenger.CRDTCallback;
import messenger.CRDTGroup;
import messenger.CRDTMessage;
import messenger.Network;
import texteditor.App;
import texteditor.AppModel;

import java.util.List;
import java.util.UUID;

public class TreeReplicatedDocument extends ReplicatedDocument {
	public static final boolean IS_INSERT = true;
	private static final String MULTICAST_ADDRESS = "239.250.250.250";
	private static final int MULTICAST_PORT = 9999;
	
	private BinaryTree tree = new BinaryTree();
	private UUID udis = App.uuid;
	private CRDTCallback callback = new CRDTCallback();
	private AppModel model;
	private CRDTGroup group;
	
	public TreeReplicatedDocument() {
		callback.addListener(this);
		group = Network.getInstance().create(MULTICAST_ADDRESS, MULTICAST_PORT);
		group.onReceipt(callback);
		group.join();
	}

	@Override
	public synchronized void insert(int position, Atom newAtom) {
		Position posId = generatePosId(tree.getPosition(position), tree.getPosition(position+1));
		tree.add(new MiniNode(posId, newAtom));
		group.send(new CRDTMessage(new Operation(OperationType.INSERT, posId, newAtom)));
	}
	
	@Override
	public synchronized void delete(int position) {
		Position posId = tree.getPosition(position);
		tree.delete(posId);
		group.send(new CRDTMessage(new Operation(OperationType.DELETE, posId, null)));
	}

	@Override
	public synchronized void remoteInsert(Position posId, Atom newAtom) {
		int index = tree.getIndex(posId);
		tree.add(new MiniNode(posId, newAtom));
		model.remoteInsert(index, newAtom.toString());
	}

	@Override
	public synchronized void remoteDelete(Position posId) {
		int index = tree.getIndex(posId);
		tree.delete(posId);
		model.remoteDelete(index-1);
	}
	
	public String getTreeString() {
		List<String> treeList = tree.getTreeAsList();
		if(treeList == null) return "";
		StringBuilder treeString = new StringBuilder();
		for(String element : treeList) {
			treeString.append(element);
		}
		
		return treeString.toString();
	}
	
	public void printString() {
		tree.printString();
	}
	
	public void addListner(AppModel model) {
		this.model = model;
	}
	
	private Position generatePosId(Position x, Position y) {
		if(x == null && y == null) {
			return new Position(Position.EMPTY_PATH, Position.EMPTY_NODE, udis);
		}
		if(y == null) {
			if(x.isNodeEmpty()) {
				return new Position(Position.EMPTY_PATH, Position.RIGHT_NODE, udis);
			} else {
				return new Position(x.getPath() + x.getNode(), Position.RIGHT_NODE, udis);
			}
		}
		if(x == null) {
			if(y.isNodeEmpty()) {
				return new Position(Position.EMPTY_PATH, Position.LEFT_NODE, udis);
			} else {
				return new Position(y.getPath() + y.getNode(), Position.LEFT_NODE, udis);
			}
		}
		
		if(x.isAncestorOf(y)) {
			return new Position(y.getPath() + y.getNode(), Position.LEFT_NODE, udis);
		} else if(y.isAncestorOf(x)) {
			return new Position(x.getPath() + x.getNode(), Position.RIGHT_NODE, udis);
		} else if(areMiniSiblings(x, y)) {
			return new Position(x.getPath(), Position.RIGHT_NODE, udis);
		} else {
			return new Position(x.getPath() + x.getNode(), Position.RIGHT_NODE, udis);
		}
	}
	
	private boolean areMiniSiblings(Position x, Position y) {
		if((x.getPath() + x.getNode()).equals(y.getPath() + y.getNode())) {
			return true;
		}
		return false;
	}
}
