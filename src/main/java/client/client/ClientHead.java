package client.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Client head. This is what most users will be interacting with. 
 * 
 * @author Ben Shabowski
 * @version 0.2
 * @since 0.1
 */
public class ClientHead {

	/**
	 * This is for GUI version
	 */
	public ClientHead() {

		Stage head = new Stage();
		
		head.setOnCloseRequest(e -> {
			System.exit(1);
		});

		head.setTitle("Client to server Sync information");
		head.getIcons().add(new Image(getClass().getResource("/assets/resources/ModManagerIco.png").toString()));
		Pane mainPane = null;
		
		try {
			mainPane = FXMLLoader.load(getClass().getResource("/assets/resources/HeadedSync.fxml"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scene clientLogScene = new Scene(mainPane);
		clientLogScene.getStylesheets().add("/assets/resources/modmanager.css");
		
		head.setScene(clientLogScene);
		head.setResizable(false);
		head.show();
		

		
	}

	/**
	 * This still has a gui, but purely for displaying progress
	 * 
	 * @param ip
	 * @param port
	 */
	public ClientHead(String ip, String port) {

		Stage headless = new Stage();
		headless.setTitle("Client to server Sync information");
		headless.getIcons().add(new Image(getClass().getResource("/assets/resources/ModManagerIco.png").toString()));
		headless.setOnCloseRequest(e -> {
			System.exit(1);
		});
		
		FXMLLoader fl = new FXMLLoader();
		
		try {
			Pane headlessPane = fl.load(getClass().getResource("/assets/resources/HeadlessSync.fxml").openStream());
			Scene headlessScene = new Scene(headlessPane);
			ClientHeadlessController chc = fl.getController();
			
			headless.setScene(headlessScene);
			
			headlessScene.getStylesheets().add("/assets/resources/modmanager.css");
			
			chc.sync(ip, port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		headless.setResizable(false);
		headless.show();
		
		

	}

}
