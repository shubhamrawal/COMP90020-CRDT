package texteditor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import crdt.Atom;
import crdt.TreeReplicatedDocument;

public class AppModel {
	
	private TreeReplicatedDocument tree = new TreeReplicatedDocument();
	private LinkedList<Atom> list = new LinkedList<Atom>();
	
	public void save(TextFile file) {
		try {
			Files.write(file.getFilePath(), file.getContent(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public IOResult<TextFile> open(Path filePath) {
		try {
			List<String> lines = Files.readAllLines(filePath);
			return new IOResult<TextFile>(IOResult.IO_SUCCESS, new TextFile(filePath, lines), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new IOResult<TextFile>(IOResult.IO_FAILURE, null, e);
		}
	}

	public void exit() {
		System.exit(0);
	}
	
	public void insert(String ch, int position) {
		Atom a = new Atom(ch);
		tree.insert(position, a);
		list.add(position, a);
		if(position-1 >= 0) {
			System.out.println(list.get(position-1).toString());
		}
		if(position+1 < list.size()) {
			System.out.println(list.get(position+1).toString());
		}
	}
	
	public void print() {
		for(Atom a : list) {
			System.out.println(a.toString());
		}
	}
}
