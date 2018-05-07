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
		model.addListner(this);
	}
	
	public void updateView() {
		
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
		KeyCode code = e.getCode();
		if(code.equals(KeyCode.BACK_SPACE)) {
			int position = textArea.caretPositionProperty().intValue();
			model.delete(position);
		} else if(!code.isArrowKey() && !code.isFunctionKey() && !code.isMediaKey() 
				&& !code.isModifierKey() && !code.isNavigationKey() 
				&& !code.equals(KeyCode.CAPS)) {
			int position = textArea.caretPositionProperty().intValue();
			model.insert(e.getText(), position);
		}
	}
	
	@FXML
	private void onPrint() {
		model.print();
//		model.test();
	}
	
	private void saveFile(Path filePath) {
		TextFile newTextFile = new TextFile(filePath, Arrays.asList(textArea.getText().split("\n")));
		model.save(newTextFile);
		textFile = newTextFile;
	}
}
