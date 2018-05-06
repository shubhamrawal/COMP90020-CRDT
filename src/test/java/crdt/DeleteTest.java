package crdt;

import org.junit.Assert;
import org.junit.Test;

public class DeleteTest {
	TreeReplicatedDocument doc = new TreeReplicatedDocument();

	@Test
	public void DeletesAreConsistent() {
		String first = "Hello World.\n";
		String second = "This is a line.";
		String delete = "Delete String";
		insertStringIntoTree(first, 0);
		insertStringIntoTree(delete, first.length());
		insertStringIntoTree(second, first.length() + delete.length());
		deleteStringFromTree(delete, first.length());
		
		String expected = first + second;
		String actual = doc.getTreeString();
		Assert.assertArrayEquals(expected.toCharArray(), actual.toCharArray());
	}
	
	@Test
	public void DeleteNonExistingString() {
		String first = "hello world";
		deleteStringFromTree(first, 0);
		insertStringIntoTree(first, 0);
		
		String expected = first;
		String actual = doc.getTreeString();
		Assert.assertArrayEquals(expected.toCharArray(), actual.toCharArray());
	}
	
	@Test
	public void ConsecutiveInsertsAndDeletes() {
		String first = "this is a line";
		String second = "hello world";
		for(int i = 0; i < 100; i++) {
			insertStringIntoTree(first, 0);
			deleteStringFromTree(first, 0);
		}
		insertStringIntoTree(second, 0);
		
		String expected = second;
		String actual = doc.getTreeString();
		Assert.assertArrayEquals(expected.toCharArray(), actual.toCharArray());
	}
	
	private void insertStringIntoTree(String str, int offset) {
		int i = 0 + offset;
		for(char c : str.toCharArray()) {
			doc.insert(i, new Atom(c));
			i++;
		}
	}
	
	private void deleteStringFromTree(String str, int offset) {
		int position = str.length() -1 + offset;
		for(int i = 0; i < str.length(); i++) {
			doc.delete(position);
			position--;
		}
	}

}
