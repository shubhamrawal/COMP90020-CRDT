package crdt;

import java.util.UUID;

public class Position {
//	private String path;
//	private String node;
	private Byte[] path;
	private Byte node;
	private UUID udis;
	
	public Position(Byte[] path, Byte node, UUID udis) {
		this.path = path;
		this.node = node;
		this.udis = udis;
	}
	
	public boolean lessThan(Position x) {
		if(x.isRoot()) {
			if(node == 1) {
				return true;
			} else {
				return false;
			}
		}
		Byte[] xPath = x.getPath();
		int minLength = Math.min(path.length, xPath.length);
		for(int i = 0; i < minLength; i++) {
			if(path[i] < xPath[i]) {
				return true;
			} else if(path[i] > xPath[i]) {
				return false;
			} else {
				continue;
			}
		}
		
//		return this.node < x.getNode();
		if(x.getNode() == 1) {
			return true;
		} 
		return false;
	}
	
//	public boolean lessThan(Position x) {
//		if(this.path.startsWith(x.getPath()) && x.getNode().equals("1")) {
//			return true;
//		}
//		if(x.getPath().startsWith(this.path) && this.node.equals("0")) {
//			return true;
//		}
//		if(checkCommonPrefix(this.path + this.node, x.getPath() + x.getNode())) {
//			return true;
//		}
//		
//		return false;
//	}
	
	public boolean equalTo(Position x) {
//		if(this.path.equals(x.getPath()) && this.node.equals(x.getNode())) {
//			return true;
//		}
//		return false;
		
		if(this.path.length != x.getPath().length) {
			return false;
		}
		for(int i = 0; i < this.path.length; i++) {
			if(path[i] != x.getPath()[i]) {
				return false;
			}
		}
		
		return this.node == x.getNode();
	}
	
	public boolean greaterThan(Position x) {
		return (!lessThan(x) && !equalTo(x));
	}
	
	public Byte[] getPath() {
		return path;
	}
	
	public Byte getNode() {
		return node;
	}
	
	public UUID getUDIS() {
		return udis;
	}
	
	public String getFullPath() {
//		return "[" + path + "(" + node + " : " + udis.toString() + ")]";
		return "[" + path + "(" + node + " : d)]";
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
	
//	private boolean isRoot() {
//		return (path + node).equals("");
//	}
//	
//	private boolean isParentOf(Position x) {
//		return (this.path + this.node).equals(x.getPath());
//	}
//	
//	private Position getParent(Position x) {
//		String xPath = x.getPath();
//		int len = x.getPath().length();
//		return new Position(xPath.substring(0, len-2), xPath.substring(len-2, len-1), x.getUDIS());
//	}
	
	private boolean isRoot() {
		return (path == null && node == null);
	}
	
	private boolean isParentOf(Position x) {
		Byte[] xPath = x.getPath();
		if(path.length+1 != xPath.length) {
			return false;
		}
		for(int i = 0; i < path.length; i++) {
			if(path[i] != xPath[i]) {
				return false;
			}
		}
		if(node != xPath[xPath.length-1]) {
			return false;
		}
		return true;
	}
	
	private Position getParent(Position x) {
		Byte[] xPath = x.getPath();
		Byte[] parentPath = new Byte[xPath.length-1];
		for(int i = 0; i < parentPath.length; i++) {
			parentPath[i] = xPath[i];
		}
		return new Position(parentPath, xPath[xPath.length-1], x.getUDIS());
	}
	
	private boolean checkCommonPrefix(String a, String b) {
		int minLength = Math.min(a.length(), b.length());
		int count = -1;
		for(int i = 0; i < minLength; i++) {
			if(a.charAt(i) != b.charAt(i)) {
				count = i+1;
				break;
			}
		}
		if(count > 0 && count < minLength) {
			return a.charAt(count) < b.charAt(count);
		}
		
		return false;
	}
}
