package crdt;

import java.util.ArrayList;
import java.util.List;

public class BinaryTree {
	Node root = null;
	List<String> stringList;
	
	public void add(MiniNode node) {
		root = addNode(root, node);
	}
	
	public synchronized void delete(Position posId) {
		Node node = getNodeWithPosId(root, posId);
		node.deleteMiniNode(posId);
	}
	
	public boolean isEmpty() {
		return root == null;
	}
	
	public List<String> getTreeAsList() {
		stringList = new ArrayList<String>();
		processTree(root);
		return stringList;
	}
	
	public void printString() {
		string(root);
	}
	
	private Node getNodeWithPosId(Node current, Position posId) {
		if(current == null) {
			return null;
		}
		Position nodeId = current.getNodeId();
		if(posId.equalToNodeId(nodeId)) {
			return current;
		} else if(posId.lessThan(nodeId)) {
			return getNodeWithPosId(current.getLeft(), posId);
		} else {
			return getNodeWithPosId(current.getRight(), posId);
		}
	}
	
	private Node addNode(Node current, MiniNode node) {
		if(current == null) {
			return new Node(node);
		}
		if(node.getPosId().lessThan(current.getNodeId())) {
			current.setLeft(addNode(current.getLeft(), node));
		} else if(node.getPosId().equalToNodeId(current.getNodeId())) {
			return current.addMiniNode(node);
		} else {
			current.setRight(addNode(current.getRight(), node));
		}
		
		return current;
	}
	
	private void processTree(Node current) {
		Node left = current.getLeft();
		Node right = current.getRight();
		if(left != null) {
			processTree(left);
		}
		stringList.add(current.toString());
		if(right != null) {
			processTree(right);
		}
	}
	
	private void string(Node current) {
		Node left = current.getLeft();
		Node right = current.getRight();
		if(left != null) {
			string(left);
		}
		System.out.println(current.toString() + " - " + current.getNodeId().getFullPath());
		if(right != null) {
			string(right);
		}
	}
}
