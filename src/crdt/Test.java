package crdt;

import java.util.UUID;

public class Test {
	
	public static void main(String[] args) {
		
//		TreeReplicatedDocument doc = new TreeReplicatedDocument();
//		doc.insert(1, new Atom("Hello"));
//		doc.insert(1, new Atom("Hi"));
//		doc.insert(2, new Atom("World"));
//		doc.insert(5, new Atom("this"));
//		doc.insert(5, new Atom("our"));
//		doc.insert(4, new Atom("to"));
//		doc.insert(7, new Atom("world"));
//		doc.insert(6, new Atom("new"));
//		doc.insert(3, new Atom("welcome"));
//		doc.insert(1, new Atom("Hola"));
//		
//		doc.printString();
		UUID udis = UUID.randomUUID();
		byte zero = 0;
		byte one = 1;
		Position x = new Position(new byte[] {1}, one, udis);
		Position y = new Position(new byte[] {1, 1}, one, udis);
//		Position z = generatePosId(x, y, udis);
//		System.out.println(z.getFullPath());
		System.out.println(x.lessThan(y));
	}

}
