package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rendering.SystemSettings;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) {
		stage.setTitle("Jurassic Park");

		SystemSettings.setScreenHeight(900);
		SystemSettings.setScreenWidth(900);
		
		Group root = new Group();
		Scene scene = new GameScene(root, stage);
		stage.setScene(scene);
	
		stage.show();
		
	}
}