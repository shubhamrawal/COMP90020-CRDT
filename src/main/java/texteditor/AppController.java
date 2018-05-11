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
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class AppController {
	@FXML
	private TextArea textArea;
	@FXML
	private Label fileName;
	@FXML
	private Label charCount;
	
	private TextFile textFile;
	private AppModel model;
	private int count = 0;
	
	public AppController(AppModel model) {
		this.model = model;
		model.addListener(this);
	}
	
	public void remoteInsert(int index, String text) {
		textArea.insertText(index, text);
	}
	
	public void remoteDelete(int index) {
		textArea.deleteText(index, index+1);
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
	private synchronized void onKeyPressed(KeyEvent e) {
		KeyCode code = e.getCode();
		int position = textArea.caretPositionProperty().intValue();
		if(code.equals(KeyCode.BACK_SPACE)) {
			if(position != 0) {
				model.delete(position);
				count--;
				charCount.setText("Character Count: " + count);
			}
		} else if(!code.isArrowKey() && !code.isFunctionKey() && !code.isMediaKey() 
				&& !code.isModifierKey() && !code.isNavigationKey() 
				&& !code.equals(KeyCode.CAPS)) {
			model.insert(e.getText(), position);
			count++;
			charCount.setText("Character Count: " + count);
		}
	}
	
	@FXML
	private void onTest() {
		model.test();
	}
	
	private void saveFile(Path filePath) {
		TextFile newTextFile = new TextFile(filePath, Arrays.asList(textArea.getText().split("\n")));
		model.save(newTextFile);
		textFile = newTextFile;
	}
}
