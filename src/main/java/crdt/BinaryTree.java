package crdt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BinaryTree {
	Node root = null;
	HashMap<Double, List<MiniNode>> flat = new HashMap<>();
	
	public void add(double posId, MiniNode node) {
		addToMap(posId, node);
		root = addNode(root, posId, node);
	}
	
	private Node addNode(Node current, double posId, MiniNode node) {
		if(current == null) {
			return new Node(posId, node);
		}
		
		if(posId < current.getPosId()) {
			current.setLeft(addNode(current.getLeft(), posId, node));
		} else if(posId > current.getPosId()) {
			current.setRight(addNode(current.getRight(), posId, node));
		} else {
			return current.addMiniNode(node);
		}
		
		return current;
	}
	
	public void printString() {
		string(root);
	}
	
	private void addToMap(double posId, MiniNode node) {
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
