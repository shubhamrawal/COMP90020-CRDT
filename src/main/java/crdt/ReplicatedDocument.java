package crdt;

public abstract class ReplicatedDocument {
	
	public abstract void insert(int position, int deletes, Atom newAtom);
	public abstract void delete(int position);
	public abstract void remoteInsert(Position posId, Atom newAtom);
	public abstract void remoteDelete(Position posId);

}
