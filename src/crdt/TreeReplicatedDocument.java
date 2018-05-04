package crdt;

import java.util.LinkedList;
import java.util.UUID;

public class TreeReplicatedDocument extends ReplicatedDocument {
	public static final Byte ZERO = 0;
	public static final Byte ONE = 1;
	
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
//		if(x == null && y == null) {
//			return new Position("", "", udis);
//		}
//		if(y == null) {
//			if(x.getNode().equals("")) {
//				return new Position("", "1", udis);
//			} else {
//				return new Position(x.getPath() + x.getNode(), "1", udis);
//			}
//		}
//		if(x == null) {
//			if(y.getNode().equals("")) {
//				return new Position("", "0", udis);
//			} else {
//				return new Position(y.getPath() + y.getNode(), "0", udis);
//			}
//		}
//		
//		if(x.isAncestorOf(y)) {
//			return new Position(y.getPath() + y.getNode(), "0", udis);
//		} else if(y.isAncestorOf(x)) {
//			return new Position(x.getPath() + x.getNode(), "1", udis);
//		} else if(areMiniSiblings(x, y)) {
//			return new Position(x.getPath(), "1", udis);
//		} else {
//			return new Position(x.getPath() + x.getNode(), "1", udis);
//		}
		
		if(x == null && y == null) {
			return new Position(null, null, udis);
		}
		if(y == null) {
			if(x.getNode() == null) {
				return new Position(null, ONE, udis);
			} else {
				return new Position(getByteArray(x.getPath(), x.getNode()), ONE, udis);
			}
		}
		if(x == null) {
			if(y.getNode() == null) {
				return new Position(null, ZERO, udis);
			} else {
				return new Position(getByteArray(y.getPath(), y.getNode()), ZERO, udis);
			}
		}
		
		if(x.isAncestorOf(y)) {
			return new Position(getByteArray(y.getPath(), y.getNode()), ZERO, udis);
		} else if(y.isAncestorOf(x)) {
			return new Position(getByteArray(x.getPath(), x.getNode()), ONE, udis);
		} else if(areMiniSiblings(x, y)) {
			return new Position(x.getPath(), ONE, udis);
		} else {
			return new Position(getByteArray(x.getPath(), x.getNode()), ONE, udis);
		}
	}
	
	private boolean areMiniSiblings(Position x, Position y) {
		if(x.equalTo(y)) {
			return true;
		}
		return false;
	}
	
	private Byte[] getByteArray(Byte[] array, Byte newByte) {
		int len = array.length;
		Byte[] ret = new Byte[len+1];
		for(int i = 0; i < len; i++) {
			ret[i] = array[i];
		}
		ret[len] = newByte;
		
		return ret;
	}
}
