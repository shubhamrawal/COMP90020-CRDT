package crdt;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private Position posId;
	private List<MiniNode> nodes;
	private Node left;
	private Node right;
	
	public Node(Position posId, MiniNode node) {
		this.posId = posId;
		nodes = new ArrayList<MiniNode>();
		nodes.add(node);
	}
	
	public Node addMiniNode(MiniNode node) {
		nodes.add(node);
		return this;
	}
	
	public Position getPosId() {
		return posId;
	}
	
	public Node getLeft() {
		return left;
	}
	
	public Node getRight() {
		return right;
	}
	
	public void setLeft(Node node) {
		left = node;
	}
	
	public void setRight(Node node) {
		right = node;
	}
	
	@Override
	public String toString() {
		return nodes.toString();
	}
}
