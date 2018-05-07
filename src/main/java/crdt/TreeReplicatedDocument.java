package crdt;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import messenger.CRDTCallback;
import texteditor.AppModel;

public class TreeReplicatedDocument extends ReplicatedDocument {
	private BinaryTree tree = new BinaryTree();
	private LinkedList<Position> list = new LinkedList<Position>();
	private UUID udis = UUID.randomUUID();
	private CRDTCallback callback = new CRDTCallback();
	private AppModel model;
	
	public TreeReplicatedDocument() {
		callback.addListner(this);
	}

	@Override
	public synchronized void insert(int position, Atom newAtom) {
		Position x = null, y = null;
		if(position == 0) {
			if(list.size() != 0) {
				y = list.get(0);
			}
		} else if(position == list.size()) {
			x = list.get(list.size()-1);
		} else {
			x = list.get(position-1);
			y = list.get(position);
		}

		Position posId = generatePosId(x, y);
		list.add(position, posId);
		tree.add(new MiniNode(posId, newAtom));
	}

	@Override
	public synchronized void delete(int position) {
		boolean del = false;
		if(position > 0 && position <= list.size()) {
			Position posId = list.get(position-1);
			tree.delete(posId);
			del = true;
		}
		if(!del) {
			System.out.println("delete not performed at position: " + position);
//			System.out.println(list.size());
		}
	}
	
	@Override
	public synchronized void remoteInsert(Position posId, Atom newAtom) {
		boolean added = false;
		int index = -1;
		String text = newAtom.toString();
		for(int i = 0; i < list.size(); i++) {
			Position next = list.get(i);
			if(posId.lessThan(next)) {
				list.add(i, posId);
				index = i;
				added = true;
				break;
			}
		}
		if(!added) {
			list.add(posId);
			index = list.size()-1;
		}
		tree.add(new MiniNode(posId, newAtom));
		model.remoteInsert(index, text);
	}
	
	@Override
	public synchronized void remoteDelete(Position posId) {
		
	}
	
	public String getTreeString() {
		List<String> treeList = tree.getTreeAsList();
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
	public void test() {
		String test = "Hello world\n";
		for(int i = 0; i < test.length(); i++) {
			Position posId = getTestPositionId(i);
			remoteInsert(posId, new Atom(test.charAt(i)));
		}
	}
	
	// TODO remove
	public Position getTestPositionId(int position) {
		Position x = null, y = null;
		if(position == 0) {
			if(list.size() != 0) {
				y = list.get(0);
			}
		} else if(position == list.size()) {
			x = list.get(list.size()-1);
		} else {
			x = list.get(position-1);
			y = list.get(position);
		}

		return generatePosId(x, y);
	}
	
	public void addListner(AppModel model) {
		this.model = model;
	}
	
	private Position generatePosId(Position x, Position y) {
		if(x == null && y == null) {
			return new Position("", "", udis);
		}
		if(y == null) {
			if(x.isNodeEmpty()) {
				return new Position("", "1", udis);
			} else {
				return new Position(x.getPath() + x.getNode(), "1", udis);
			}
		}
		if(x == null) {
			if(y.isNodeEmpty()) {
				return new Position("", "0", udis);
			} else {
				return new Position(y.getPath() + y.getNode(), "0", udis);
			}
		}
		
		if(x.isAncestorOf(y)) {
			return new Position(y.getPath() + y.getNode(), "0", udis);
		} else if(y.isAncestorOf(x)) {
			return new Position(x.getPath() + x.getNode(), "1", udis);
		} else if(areMiniSiblings(x, y)) {
			return new Position(x.getPath(), "1", udis);
		} else {
			return new Position(x.getPath() + x.getNode(), "1", udis);
		}
	}
	
	private boolean areMiniSiblings(Position x, Position y) {
		if((x.getPath() + x.getNode()).equals(y.getPath() + y.getNode())) {
			return true;
		}
		return false;
	}
}
