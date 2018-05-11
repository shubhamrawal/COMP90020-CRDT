package texteditor;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	
	public static final UUID uuid = UUID.randomUUID();
	private final ExecutorService pool = Executors.newCachedThreadPool();  

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui.fxml"));
		loader.setControllerFactory(t -> new AppController(new AppModel()));
		
		primaryStage.setScene(new Scene(loader.load()));
		primaryStage.show();
		
		pool.execute(new MulticastReceiver());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}