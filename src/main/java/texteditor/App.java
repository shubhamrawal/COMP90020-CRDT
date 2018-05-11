package texteditor;

import com.google.common.io.Resources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.UUID;

public class App extends Application {
	
	public static final UUID uuid = UUID.randomUUID();

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Resources.getResource("ui.fxml"));
		loader.setControllerFactory(t -> new AppController(new AppModel()));
		
		primaryStage.setScene(new Scene(loader.load()));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}