package crdt;

public abstract class ReplicatedDocument {
	
	public abstract void insert(double posId, Atom newAtom);
	public abstract void delete(double posId);

}
