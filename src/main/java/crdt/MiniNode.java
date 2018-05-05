package crdt;

public class MiniNode {
	private Atom value;
	
	public MiniNode(Atom value) {
		this.value = value;
	}
	
	public String toString() {
		return value.toString();
	}
}
