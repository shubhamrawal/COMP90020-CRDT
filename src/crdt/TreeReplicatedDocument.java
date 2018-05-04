package crdt;

import java.util.LinkedList;
import java.util.UUID;

public class TreeReplicatedDocument extends ReplicatedDocument {
	private BinaryTree tree = new BinaryTree();
	private LinkedList<Position> list = new LinkedList<Position>();
	private UUID udis = UUID.randomUUID();

	@Override
	public void insert(int position, Atom newAtom) {
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
		tree.add(posId, new MiniNode(udis, newAtom));
	}

	@Override
	public void delete(int position) {
		
	}
	
	public void printString() {
		tree.printString();
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
