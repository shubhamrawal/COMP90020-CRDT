package crdt;

import java.io.Serializable;
import java.util.Objects;

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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Operation operation = (Operation) o;
		return getType() == operation.getType() &&
				Objects.equals(getPosId(), operation.getPosId()) &&
				Objects.equals(value, operation.value);
	}

	@Override
	public int hashCode() {

		return Objects.hash(getType(), getPosId(), value);
	}
}
