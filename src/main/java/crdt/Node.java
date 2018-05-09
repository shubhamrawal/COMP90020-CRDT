package crdt;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private List<MiniNode> nodes;
	private Node left;
	private Node right;
	
	public Node(MiniNode node) {
		nodes = new ArrayList<MiniNode>();
		nodes.add(node);
	}
	
	public Node addMiniNode(MiniNode node) {
		// TODO change to account for udis
		nodes.add(node);
		return this;
	}
	
	public void deleteMiniNode(Position posId) {
		for(MiniNode n : nodes) {
			if(posId.equalTo(n.getPosId())) {
				n.delete();
				break;
			}
		}
	}
	
	public List<MiniNode> getMiniNodes() {
		return nodes;
	}
	
	public Position getNodeId() {
		// TODO only used for path and node. udis not to be used
		return nodes.get(0).getPosId();
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
		StringBuilder strNodes = new StringBuilder();
		for(MiniNode n : nodes) {
			if(!n.isTombstone()) {
				strNodes.append(n.toString());
			}
		}
		return strNodes.toString();
	}
}
