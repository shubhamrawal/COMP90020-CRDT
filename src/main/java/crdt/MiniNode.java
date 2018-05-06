package crdt;

public class MiniNode {
	private Position posId;
	private Atom value;
	private boolean isDeleted;
	
	public MiniNode(Position posId, Atom value) {
		this.posId = posId;
		this.value = value;
	}
	
	public Position getPosId() {
		return posId;
	}
	
	public void delete() {
		isDeleted = true;
	}
	
	public boolean isTombstone() {
		return isDeleted;
	}
	
	public String toString() {
		return value.toString();
	}
}
