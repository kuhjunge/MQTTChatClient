package de.quhfan.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	/**
	 * Startet die Anwendung.
	 *
	 * @param args
	 *            Die Argumente
	 */
	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final String res = "/de/quhfan/chat/";
		final Controller software = new Controller(Controller.setUpChat());
		final FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(res + "ChatWindow.fxml"));
		loader.setController(software);
		final Parent root = loader.load();
		final Scene scene = new Scene(root);
		primaryStage.setTitle("MQTT Chat");
		primaryStage.setScene(scene);
		// primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(res +
		// "db.png")));
		primaryStage.show();
	}
}
