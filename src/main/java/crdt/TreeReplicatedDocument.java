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
		if(position-1 >= 0) {
			x = list.get(position-1);
		}
		if(position+1 < list.size()) {
			y = list.get(position+1);
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
		if(x == null) {
			if(y == null) {
				return new Position("", "", udis);
			} else {
				return new Position(y.getPath(), "0", udis);
			}
		} else {
			if(y == null) {
				return new Position(x.getPath(), "1", udis);
			} else {
				return null;
			}
		}
	}
}
