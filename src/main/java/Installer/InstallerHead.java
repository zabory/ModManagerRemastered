package Installer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import client.client.ClientHead;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;


/**
 * Head to run the installer program.
 * This will install the minecraft profile, active the forge installer, and run the client side mod manager to sync the mods with the server
 * @author Ben Shabowski
 * @version 0.2
 * @since 0.1
 */
public class InstallerHead{
	
	public InstallerHead() {
		
		Stage arg0 = new Stage();
		
		GridPane mainPane = new GridPane();
		
		mainPane.getColumnConstraints().add(0, new ColumnConstraints(125));
		for(int i = 0; i < 4; i++)
			mainPane.getRowConstraints().add(new RowConstraints(30));
		
		mainPane.add(new Label("Server ip:"), 0, 1, 1, 1);
		mainPane.add(new Label("Server port:"), 0, 2, 1, 1);
		
		TextField ip = new TextField("zgamelogic.com");
		TextField port = new TextField("42069");
		
		mainPane.add(ip, 1, 1);
		mainPane.add(port, 1, 2);
		
		Button submit = new Button("Install");
		
		submit.setOnAction(e -> {
			if (!ip.getText().equals("") && !port.getText().equals("")) {
				arg0.hide();
				
				new ClientHead(ip.getText(), port.getText());

				
			}
		});
		
		mainPane.add(submit, 0, 5, 2, 1);
		GridPane.setHalignment(submit, HPos.CENTER);
		
		Scene mainScene = new Scene(mainPane, 300, 200);
		mainScene.getStylesheets().add("/assets/resources/modmanager.css");
		arg0.setTitle("Modpack installer");
		arg0.getIcons().add(new Image(getClass().getResource("/assets/resources/Installer.png").toString()));
		arg0.setScene(mainScene);
		arg0.show();
	}
	
	public InstallerHead(String modpackName, String ip, String port, int ram) {
		try {
			installPack(modpackName, ip, port, ram);
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Installs the modpack. Creates the profile, launches the forge installer and waits for it to finish,
	 * and then syncs with whatever IP and Port provided all the files needed to the client
	 * @param modpackName Name of the modpack
	 * @param ip IP address of the server to sync to 
	 * @param port Port of the server to sync to
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void installPack(String modpackName, String ip, String port, int ram) throws FileNotFoundException, IOException, ParseException {
				
		new ClientHead(ip, port);
		
	}
}
