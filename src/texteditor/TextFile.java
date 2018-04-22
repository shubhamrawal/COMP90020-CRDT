package texteditor;

import java.nio.file.Path;
import java.util.List;

public class TextFile {
	private final Path filePath;
	private final List<String> content;
	
	public TextFile(Path filePath, List<String> content) {
		this.filePath = filePath;
		this.content = content;
	}
	
	public Path getFilePath() {
		return filePath;
	}
	
	public List<String> getContent() {
		return content;
	}

}
