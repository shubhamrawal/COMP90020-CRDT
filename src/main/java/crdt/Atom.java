package crdt;

import java.io.Serializable;
import java.util.Objects;

public class Atom implements Serializable {
	
	private char character;
	
	public Atom(char character) {
		this.character = character;
	}
	
	public String toString() {
		return String.valueOf(character);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Atom atom = (Atom) o;
		return character == atom.character;
	}

	@Override
	public int hashCode() {

		return Objects.hash(character);
	}
}
