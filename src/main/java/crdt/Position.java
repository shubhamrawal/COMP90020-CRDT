package crdt;

import java.util.UUID;

public class Position {
	public static final String LEFT_NODE = "0";
	public static final String RIGHT_NODE = "1";
	public static final String EMPTY_PATH = "";
	public static final String EMPTY_NODE = "";
	
	private String path;
	private String node;
	private UUID udis;
	
	public Position(String path, String node, UUID udis) {
		this.path = path;
		this.node = node;
		this.udis = udis;
	}
	
	public boolean lessThan(Position x) {
		String a = this.path + this.node;
		String b = x.getPath() + x.getNode();
		if(a.length() < b.length() && b.startsWith(a) && 
				String.valueOf(b.charAt(a.length())) == RIGHT_NODE) {
			return true;
		}
		if(b.length() < a.length() && a.startsWith(b) && 
				String.valueOf(a.charAt(b.length())) == LEFT_NODE) {
			return true;
		}
		int index = commonPrefix(a, b);
		if(index > 0 && index < a.length() && index < b.length()) {
			return a.charAt(index) < b.charAt(index);
		}
		
		return false;
	}
	
	public boolean equalToNodeId(Position x) {
		if(this.path.equals(x.getPath()) && this.node.equals(x.getNode())) {
			return true;
		}
		return false;
	}
	
	public boolean equalTo(Position x) {
		if(equalToNodeId(x) && this.udis.equals(x.getUDIS())) {
			return true;
		}
		return false;
	}
	
	public boolean greaterThan(Position x) {
		return (!lessThan(x) && !equalTo(x));
	}
	
	public boolean isPathEmpty() {
		return path.equals(EMPTY_PATH);
	}
	
	public String getPath() {
		return path;
	}
	
	public boolean isNodeEmpty() {
		return node.equals(EMPTY_NODE);
	}
	
	public String getNode() {
		return node;
	}
	
	public UUID getUDIS() {
		return udis;
	}
	
	public String getFullPath() {
		return "[" + path + "(" + node + " : " + udis.toString() + ")]";
	}
	
	public boolean isAncestorOf(Position x) {
		if(x.isRoot()) {
			return false;
		}
		if(isParentOf(x)) {
			return true;
		} else {
			return isAncestorOf(getParent(x));
		}
	}
	
	private boolean isRoot() {
		return (path + node).equals(EMPTY_PATH);
	}
	
	private boolean isParentOf(Position x) {
		return (this.path + this.node).equals(x.getPath());
	}
	
	private Position getParent(Position x) {
		String xPath = x.getPath();
		int len = x.getPath().length();
		if(len>1) {
			return new Position(xPath.substring(0, len-1), xPath.substring(len-1, len), x.getUDIS());
		} else if(len == 1) {
			return new Position(EMPTY_PATH, xPath.substring(0, 1), x.getUDIS());
		}
		return new Position(EMPTY_PATH, EMPTY_NODE, x.getUDIS());
	}
	
	private int commonPrefix(String a, String b) {
		int minLength = Math.min(a.length(), b.length());
		int count = -1;
		for(int i = 0; i < minLength; i++) {
			if(a.charAt(i) != b.charAt(i)) {
				count = i+1;
			}
		}
		
		return count;
	}
}
