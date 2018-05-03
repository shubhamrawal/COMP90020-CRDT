package crdt;

import java.util.UUID;

public class Position {
	private String path;
	private String node;
	private UUID udis;
	
	public Position(String path, String node, UUID udis) {
		this.path = path;
		this.node = node;
		this.udis = udis;
	}
	
	public boolean lessThan(Position x) {
		if(this.path.startsWith(x.getPath()) && x.getNode().equals("1")) {
			return true;
		}
		if(x.getPath().startsWith(this.path) && this.node.equals("0")) {
			return true;
		}
		if(checkCommonPrefix(this.path + this.node, x.getPath() + x.getNode())) {
			return true;
		}
		
		return false;
	}
	
	public boolean equalTo(Position x) {
		if(this.path.equals(x.getPath()) && this.node.equals(x.getNode())) {
			return true;
		}
		return false;
	}
	
	public boolean greaterThan(Position x) {
		return (!lessThan(x) && !equalTo(x));
	}
	
	public String getPath() {
		return path;
	}
	
	public String getNode() {
		return node;
	}
	
	public String getUDIS() {
		return udis.toString();
	}
	
	public String getFullPath() {
		return "[" + path + "(" + node + " : " + udis.toString() + ")]";
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
