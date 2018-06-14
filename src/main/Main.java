package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import filehandling.FileHandler;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import landscapes.Original;
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
	
			//drawMap(grid, canvas);

		stage.show();
		
	}
}