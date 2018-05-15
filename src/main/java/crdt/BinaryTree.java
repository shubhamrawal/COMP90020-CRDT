package crdt;

import java.util.ArrayList;
import java.util.List;

public class BinaryTree {
	Node root = null;
	List<String> stringList;
	
	public class Result {
		public Result(Position position, int index, OperationType type) {
			this.position = position;
			this.index = index;
			this.type = type;
		}
		
		public Position position;
		public int index;
		public OperationType type;
	}
	
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
		if(root == null) return null;
		processTree(root);
		return stringList;
	}
	
	public void printString() {
		string(root);
	}
	
	public Position getPosition(int position, OperationType type) {
		return getPositionRec(root, new Result(null, position, type)).position;
	}
	
	private Result getPositionRec(Node current, Result result) {
		if(current == null) {
			return result;
		}
		if(current.getLeft() != null) {
			result = getPositionRec(current.getLeft(), result);
		}
		if(result.position == null) {
			for(MiniNode miniNode: current.getMiniNodes()) {
				if(result.type == OperationType.INSERT || (result.type == OperationType.DELETE 
						&& !miniNode.isTombstone())) {
					result.index--;
					if(result.index == 0) {
						result.position = current.getNodeId();
						return result;
					}
				}
			}
		}
		if(result.position == null && current.getRight() != null) {
			result = getPositionRec(current.getRight(), result);
		}
		
		return result;
	}
	
	public int getIndex(Position posId) {
		return getIndexRec(root, posId, 0);
	}
	
	private int getIndexRec(Node current, Position posId, int index) {
		if(current == null) {
			return index;
		}
		if(current.getLeft() != null) {
			index = getIndexRec(current.getLeft(), posId, index);
		}
		for(MiniNode miniNode: current.getMiniNodes()) {
			Position posMiniNode = miniNode.getPosId();
			if(posId.lessThan(posMiniNode)) return index;
			if(posId.equalToNodeId(posMiniNode)) {
				if(posId.getUDIS().compareTo(posMiniNode.getUDIS()) == -1) 
					return index;
			}
			if(!miniNode.isTombstone()) {
				index++;
			}
		}
		if(current.getRight() != null) {
			index = getIndexRec(current.getRight(), posId, index);
		}
		
		return index;
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
