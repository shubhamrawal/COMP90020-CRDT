package texteditor;

import java.io.File;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class AppController {
	@FXML
	private TextArea textArea;
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
				 textFile.getContent().forEach(textArea::appendText);
			} else {
				// Show dialog box
				System.out.println(openFile.getError());
			}
		}
	}
	
	@FXML
	private void onSave() {
		TextFile newTextFile = new TextFile(textFile.getFilePath(), Arrays.asList(textArea.getText().split("\n")));
		model.save(newTextFile); 
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
		Alert about = new Alert(Alert.AlertType.INFORMATION);
		about.setHeaderText("Ambiguous");
		about.setTitle("About");
		about.setContentText("COMP90020: Text Editor implementing Conflict Free Replicated Data Types\nVersion: 1.0.0");
		about.show();
	}
}
