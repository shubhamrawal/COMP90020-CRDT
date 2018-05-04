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
		Position x = new Position("", "1", udis);
		Position y = new Position("1", "1", udis);
		Position z = generatePosId(x, y, udis);
		System.out.println(z.getFullPath());
	}
	
	private static Position generatePosId(Position x, Position y, UUID udis) {
		if(x == null && y == null) {
			return new Position("", "", udis);
		}
		if(y == null) {
			if(x.getNode().equals("")) {
				return new Position("", "1", udis);
			} else {
				return new Position(x.getPath() + x.getNode(), "1", udis);
			}
		}
		if(x == null) {
			if(y.getNode().equals("")) {
				return new Position("", "0", udis);
			} else {
				return new Position(y.getPath() + y.getNode(), "0", udis);
			}
		}
		
		if(x.isAncestorOf(y)) {
			return new Position(y.getPath() + y.getNode(), "0", udis);
		} else if(y.isAncestorOf(x)) {
			return new Position(x.getPath() + x.getNode(), "1", udis);
		} else if(areMiniSiblings(x, y)) {
			return new Position(x.getPath(), "1", udis);
		} else {
			return new Position(x.getPath() + x.getNode(), "1", udis);
		}
	}
	
	private static boolean areMiniSiblings(Position x, Position y) {
		if((x.getPath() + x.getNode()).equals(y.getPath() + y.getNode())) {
			return true;
		}
		return false;
	}

}
