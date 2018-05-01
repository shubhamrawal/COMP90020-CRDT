package crdt;

import java.util.UUID;

public class MiniNode {
	private UUID udis;
	private Atom value;
	
	public MiniNode(UUID udis, Atom value) {
		this.udis = udis;
		this.value = value;
	}
	
	public String toString() {
		return value.toString();
	}
}
