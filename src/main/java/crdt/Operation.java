package crdt;

import java.io.Serializable;

public class Operation implements Serializable {
	private OperationType type;
	private Position posId;
	private Atom value;
	
	public Operation(OperationType type, Position posId, Atom value) {
		this.type = type;
		this.posId = posId;
		this.value = value;
	}
	
	public Position getPosId() {
		return posId;
	}
	
	public Atom getAtom() {
		return value;
	}
	
	public OperationType getType() {
		return type;
	}

    @Override
    public boolean equals(Object obj) {
        // TODO overwrite when field are declared
        return true;
    }
}
