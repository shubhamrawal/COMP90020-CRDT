package texteditor;

import javafx.application.Platform;
import javafx.concurrent.Task;
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

public class AppController {
	@FXML
	private TextArea textArea;
	@FXML
	private Label fileName;
	
	private TextFile textFile;
	private AppModel model;
	
	public AppController(AppModel model) {
		this.model = model;
		model.addListener(this);
	}
	
	public synchronized void remoteInsert(int index, String text) {
		Platform.runLater(() -> {
			int caret = textArea.getCaretPosition();
			if(index < caret) caret++;
			textArea.insertText(index, text);
			textArea.positionCaret(caret);
		});
	}
	
	public synchronized void remoteDelete(int index) {
		Platform.runLater(() -> {
			int caret = textArea.getCaretPosition();
			if(index < caret) caret--;
			textArea.deleteText(index, index+1);
			textArea.positionCaret(caret);
		});
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
			}
		} else if(!code.isArrowKey() && !code.isFunctionKey() && !code.isMediaKey() 
				&& !code.isModifierKey() && !code.isNavigationKey() 
				&& !code.equals(KeyCode.CAPS)) {
			String text = (e.getCode().equals(KeyCode.ENTER)) ? "\n" : e.getText();
			model.insert(text, position);
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
