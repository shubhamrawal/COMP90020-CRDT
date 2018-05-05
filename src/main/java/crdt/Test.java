package crdt;

public class Test {
	
	public static void main(String[] args) {
		
		TreeReplicatedDocument doc = new TreeReplicatedDocument();
		doc.insert(1, new Atom("Hello"));
		doc.insert(1, new Atom("Hi"));
		doc.insert(2, new Atom("World"));
		doc.insert(5, new Atom("this"));
		doc.insert(5, new Atom("our"));
		doc.insert(4, new Atom("to"));
		doc.insert(7, new Atom("world"));
		doc.insert(6, new Atom("new"));
		doc.insert(3, new Atom("welcome"));
		doc.insert(1, new Atom("Hola"));
		
		doc.printString();
	}
}
