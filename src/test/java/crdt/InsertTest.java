package crdt;

import org.junit.Assert;
import org.junit.Test;

public class InsertTest {
	TreeReplicatedDocument doc = new TreeReplicatedDocument();

	@Test
	public void InsertsAreCorrectlyInputInTree() {
		String first = "Hello world\n";
		String second = "This is a new string\n";
		String third = "This is another string";
		insertStringIntoTree(third, 0);
		insertStringIntoTree(first, 0);
		insertStringIntoTree(second, first.length());
		
		String expected = first + second + third;
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

}
