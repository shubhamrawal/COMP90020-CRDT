package crdt;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TreeReplicatedDocument extends ReplicatedDocument {
	private BinaryTree tree = new BinaryTree();
	private LinkedList<Position> list = new LinkedList<Position>();
	private UUID udis = UUID.randomUUID();

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
		if(list.size() > 0 && position < list.size()) {
			Position posId = list.get(position);
			tree.delete(posId);
		}
	}
	
	@Override
	public synchronized void remoteInsert(MiniNode node) {
		Position posId = node.getPosId();
		Atom value = node.getAtom();
		boolean added = false;
		for(int i = 0; i < list.size(); i++) {
			Position next = list.get(i);
			if(next.lessThan(posId)) {
				list.add(i, posId);
				tree.add(node);
				added = true;
			}
		}
		if(!added) {
			list.add(posId);
			tree.add(node);
		}
		
		// update view
		// model.updateView(i, value);
	}
	
	@Override
	public synchronized void remoteDelete(MiniNode node) {
		
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
	
	public void test() {
		for(int i = 0; i < list.size()-1; i++) {
			if(list.get(i+1).lessThan(list.get(i))) {
				System.out.println("Less Than");
			}
		}
		System.out.println("Finished");
	}
	
	private Position generatePosId(Position x, Position y) {
		if(x == null && y == null) {
			return new Position("", "", udis);
		}
		if(y == null) {
			if(x.getNode().equals("")) {
				return new Position("", "1", udis);
			} else {
				return new Position(x.getPath() + x.getNode(), "1", udis);
			}
		}
		if(x == null) {
			if(y.getNode().equals("")) {
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
