package texteditor;

import crdt.Atom;
import crdt.TreeReplicatedDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class AppModel {
	
	private TreeReplicatedDocument doc = new TreeReplicatedDocument();
	private AppController controller;
	
	public AppModel() {
		doc.addListner(this);
	}
	
	public void save(TextFile file) {
		try {
			Files.write(file.getFilePath(), file.getContent(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public IOResult<TextFile> open(Path filePath) {
		try {
			List<String> lines = Files.readAllLines(filePath);
			return new IOResult<TextFile>(IOResult.IO_SUCCESS, new TextFile(filePath, lines), null);
		} catch (IOException e) {
			e.printStackTrace();
			return new IOResult<TextFile>(IOResult.IO_FAILURE, null, e);
		}
	}

	public void exit() {
		System.exit(0);
	}
	
	public void insert(String ch, int position) {
		Atom a = new Atom(ch.charAt(0));
		doc.insert(position, a);
	}
	
	public void delete(int position) {
//		System.out.println(position);
		doc.delete(position);
	}
	
	public void remoteInsert(int index, String text) {
		controller.remoteInsert(index, text);
	}
	
	public void test() {
		System.out.println(doc.getTreeString());
		doc.test();
	}
	
	public void addListner(AppController controller) {
		this.controller = controller;
	}
	
	public void notifyOperation() {
		
	}
}
