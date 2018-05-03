package texteditor;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class AppController {
	@FXML
	private TextArea textArea;
	@FXML
	private Label fileName;
	@FXML
	private Label wordCount;
	
	private TextFile textFile;
	private AppModel model;
	
	public AppController(AppModel model) {
		this.model = model;
	}
	
	@FXML
	private void onOpen() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("./"));
		File file = fileChooser.showOpenDialog(null); 
		if(file != null) {
			IOResult<TextFile> openFile = model.open(file.toPath());
			if(openFile.isOk() && openFile.hasData()) {
				 textFile = openFile.getData();
				 textArea.clear();
				 textFile.getContent().forEach(line -> textArea.appendText(line + "\n"));
			} else {
				// Show dialog box
				System.out.println(openFile.getError());
			}
		}
	}
	
	@FXML
	private void onSave() {
		if(textFile != null) {
			saveFile(textFile.getFilePath());
		} else {
			onSaveAs();
		}
	}
	
	@FXML
	private void onSaveAs() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("./"));
		File file = fileChooser.showSaveDialog(null);
		if(file != null) {
			saveFile(file.toPath());
		}
	}
	
	@FXML
	private void onExit() {
		model.exit();
	}
	
	@FXML
	private void onDelete() {
		
	}
	
	@FXML
	private void onAbout() {
		Alert about = new Alert(AlertType.INFORMATION);
		about.setHeaderText("COMP90020: Distributed Algorithms");
		about.setTitle("About");
		about.setContentText("Text Editor implementing Conflict Free Replicated Data Types\nVersion: 1.0.0");
		about.show();
	}
	
	@FXML
	private void onKeyPressed(KeyEvent e) {
		if(e.getCode().isDigitKey() || e.getCode().isLetterKey() || e.getCode().isWhitespaceKey()) {
//			System.out.println(e.getCode());
			int position = textArea.caretPositionProperty().intValue();
//			System.out.println(position);
			model.insert(e.getText(), position);
		}
//		if(e.getCode().equals(KeyCode.SPACE) || e.getCode().equals(KeyCode.ENTER)) {
//			List<String> words = Arrays.asList(textArea.getText().split("\\s")); 
//			System.out.println(words);
//			wordCount.setText("Word Count: " + words.size());
//		}
	}
	
	@FXML
	private void onPrint() {
		model.print();
	}
	
	private void saveFile(Path filePath) {
		TextFile newTextFile = new TextFile(filePath, Arrays.asList(textArea.getText().split("\n")));
		model.save(newTextFile);
		textFile = newTextFile;
	}
}
