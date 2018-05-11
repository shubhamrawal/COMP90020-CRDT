package crdt;

import messenger.CRDTCallback;
import messenger.CRDTGroup;
import messenger.CRDTMessage;
import messenger.Network;
import texteditor.AppModel;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TreeReplicatedDocument extends ReplicatedDocument {
	public static final boolean IS_INSERT = true;
	private static final String MULTICAST_ADDRESS = "224.224.224.2";
	private static final int MULTICAST_PORT = 9999;
	
	private BinaryTree tree = new BinaryTree();
	// make the lists synchronized
	private List<Position> insertList = new LinkedList<Position>();
	private List<Position> deleteList = new LinkedList<Position>();
	private UUID udis = UUID.randomUUID();
	private CRDTCallback callback = new CRDTCallback();
	private AppModel model;
	private CRDTGroup group;
	
	public TreeReplicatedDocument() {
		callback.addListener(this);
		group = Network.getInstance().create(MULTICAST_ADDRESS, MULTICAST_PORT);
	}

	@Override
	public synchronized void insert(int position, Atom newAtom) {
		int insertPosition = findPosition(IS_INSERT, position);
		int size = insertList.size();
		Position x = null, y = null;
		if(insertPosition == 0) {
			if(size != 0) {
				y = insertList.get(0);
			}
		} else if(insertPosition == size) {
			x = insertList.get(size-1);
		} else {
			x = insertList.get(insertPosition-1);
			y = insertList.get(insertPosition);
		}

		Position posId = generatePosId(x, y);
		insertList.add(insertPosition, posId);
		tree.add(new MiniNode(posId, newAtom));
		group.send(new CRDTMessage(new Operation(OperationType.INSERT, posId, newAtom)));
	}
	
	@Override
	public synchronized void delete(int position) {
		int deletePosition = findPosition(!IS_INSERT, position);
		Position posId = insertList.get(deletePosition);
		deleteList.add(posId);
		tree.delete(posId);
		group.send(new CRDTMessage(new Operation(OperationType.DELETE, posId, null)));
	}

	@Override
	public synchronized void remoteInsert(Position posId, Atom newAtom) {
		boolean added = false;
		int index = 0;
		String text = newAtom.toString();
		for(int i = 0; i < insertList.size(); i++) {
			Position next = insertList.get(i);
			if(posId.lessThan(next)) {
				insertList.add(i, posId);
				added = true;
				break;
			}
			if(!deleteList.contains(next)) {
				index++;
			}
		}
		if(!added) {
			insertList.add(posId);
			index++;
		}
		tree.add(new MiniNode(posId, newAtom));
		model.remoteInsert(index, text);
	}

	@Override
	public synchronized void remoteDelete(Position posId) {
		int index = 0;
		for(int i = 0; i < insertList.size(); i++) {
			Position next = insertList.get(i);
			if(posId.equalTo(next)) {
				deleteList.add(posId);
				break;
			}
			if(!deleteList.contains(next)) {
				index++;
			}
		}
		tree.delete(posId);
		model.remoteDelete(index);
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
	
	// TODO remove
	public void remoteInsertTest() {
		String test = "hello world\n";
		for(int i = 0; i < test.length(); i++) {
			Position posId = getTestPositionId(i+2);
			remoteInsert(posId, new Atom(test.charAt(i)));
		}
	}
	
	// TODO remove
	public void remoteDeleteTest() {
		for(int i = 10; i > 8; i--) {
			int deletePosition = findPosition(!IS_INSERT, i);
			System.out.println("position: " + deletePosition);
			Position posId = insertList.get(deletePosition);
			remoteDelete(posId);
		}
	}
	
	// TODO remove
	public Position getTestPositionId(int position) {
		int insertPosition = findPosition(IS_INSERT, position);
		Position x = null, y = null;
		if(insertPosition == 0) {
			if(insertList.size() != 0) {
				y = insertList.get(0);
			}
		} else if(insertPosition == insertList.size()) {
			x = insertList.get(insertList.size()-1);
		} else {
			x = insertList.get(insertPosition-1);
			y = insertList.get(insertPosition);
		}
		
		return generatePosId(x, y);
	}
	
	public void addListner(AppModel model) {
		this.model = model;
	}
	
	private int findPosition(boolean isInsert, int position) {
		int count = 0;
		for(int i = 0; i < insertList.size(); i++) {
			if(!deleteList.contains(insertList.get(i))) {
				count++;
			}
			if(count == ((isInsert) ? position+1 : position)) {
				return i;
			}
		}
		return insertList.size();
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
