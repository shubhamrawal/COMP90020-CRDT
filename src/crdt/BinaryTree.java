package crdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BinaryTree {
	Node root = null;
	HashMap<Position, List<MiniNode>> flat = new HashMap<>();
	
	public void add(Position posId, MiniNode node) {
		addToMap(posId, node);
		root = addNode(root, posId, node);
	}
	
	private Node addNode(Node current, Position posId, MiniNode node) {
		if(current == null) {
			return new Node(posId, node);
		}
		
		if(posId.lessThan(current.getPosId())) {
			current.setLeft(addNode(current.getLeft(), posId, node));
		} else if(posId.equalTo(current.getPosId())) {
			return current.addMiniNode(node);
		} else {
			current.setRight(addNode(current.getRight(), posId, node));
		}
		
		return current;
	}
	
	public void printString() {
		string(root);
	}
	
	private void addToMap(Position posId, MiniNode node) {
		if(flat.containsKey(posId)) {
			flat.get(posId).add(node);
		} else {
			ArrayList<MiniNode> nodes = new ArrayList<MiniNode>();
			nodes.add(node);
			flat.put(posId, nodes);
		}
	}
	
	private void string(Node current) {
		Node left = current.getLeft();
		Node right = current.getRight();
		if(left != null) {
			string(left);
		}
		System.out.println(current.toString());
		if(right != null) {
			string(right);
		}
	}
}
