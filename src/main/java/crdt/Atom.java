package crdt;

public class Atom {
	
	private char character;
	
	public Atom(char character) {
		this.character = character;
	}
	
	public String toString() {
		return String.valueOf(character);
	}

}
