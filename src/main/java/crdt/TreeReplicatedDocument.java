package crdt;

import java.util.UUID;

public class TreeReplicatedDocument extends ReplicatedDocument {
	private BinaryTree tree = new BinaryTree();
	private UUID udis = UUID.randomUUID();

	@Override
	public void insert(double posId, Atom newAtom) {
		tree.add(posId, new MiniNode(udis, newAtom));
	}

	@Override
	public void delete(double posId) {
		
	}
	
	public void printString() {
		tree.printString();
	}
}
