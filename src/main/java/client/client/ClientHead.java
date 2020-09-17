package client.client;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;

/**
 * Client head. This is what most users will be interacting with. This is to
 * connect to a server and update all the mods based off what the server wants
 * updated
 * 
 * @author Ben Shabowski
 * @version 0.1
 * @since 0.1
 */
public class ClientHead {
	

	ProgressBar total = new ProgressBar();
	ProgressBar currentFile = new ProgressBar();
	ScrollPane scrollPane = new ScrollPane();

	VBox textPane = new VBox();

	/**
	 * This is for GUI version
	 */
	public ClientHead() {

		Stage head = new Stage();

		head.setTitle("Client to server Sync information");

		GridPane mainPane = new GridPane();

		total.setMinWidth(500);
		currentFile.setMinWidth(500);

		mainPane.add(new Label("IP:"), 0, 1);
		TextField ipTF = new TextField("ZGamelogic.com");
		ipTF.setFocusTraversable(false);
		mainPane.add(ipTF, 1, 1);

		mainPane.add(new Label("Port:"), 0, 3);
		TextField portTF = new TextField("42069");
		portTF.setFocusTraversable(false);
		mainPane.add(portTF, 1, 3);

		Button startSync = new Button("Sync");

		startSync.setOnAction(e -> {
			new Sync(ipTF.getText(), portTF.getText()).start();
			startSync.setDisable(true);
		});

		mainPane.add(startSync, 1, 5);

		mainPane.add(new Label("Total progress"), 2, 0);
		mainPane.add(total, 2, 1);
		mainPane.add(new Label("Current file progress"), 2, 2);
		mainPane.add(currentFile, 2, 3);

		mainPane.add(new Label("Log"), 2, 4);
		mainPane.add(scrollPane, 2, 5);

		scrollPane.setMinHeight(160);
		scrollPane.setMaxHeight(160);
		textPane.heightProperty().addListener(observable -> {
			scrollPane.setVvalue(1.0);
		});

		mainPane.setFocusTraversable(false);

		textPane.setFocusTraversable(false);
		scrollPane.setContent(textPane);
		scrollPane.setFocusTraversable(false);

		Scene clientLogScene = new Scene(mainPane, 700, 300);
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

		VBox mainPane = new VBox();

		total.setMinWidth(500);
		currentFile.setMinWidth(500);

		mainPane.getChildren().add(new Label("Total progress"));
		mainPane.getChildren().add(total);
		mainPane.getChildren().add(new Label("Current file progress"));
		mainPane.getChildren().add(currentFile);

		mainPane.getChildren().add(new Label("Log"));
		mainPane.getChildren().add(scrollPane);
		scrollPane.setMinHeight(160);

		mainPane.setFocusTraversable(false);

		textPane.setFocusTraversable(false);
		scrollPane.setContent(textPane);
		scrollPane.setFocusTraversable(false);
		
		textPane.heightProperty().addListener(observable -> {
			scrollPane.setVvalue(1.0);
		});

		Scene clientLogScene = new Scene(mainPane, 500, 250);
		headless.setScene(clientLogScene);
		headless.setResizable(false);
		headless.show();

		new Sync(ip, port).start();

	}

	public void output(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textPane.getChildren().add(new Label(message));
			}
		});
	}

	public void updateSmall(double update) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				currentFile.setProgress(update);
			}
		});
	}

	public void updateBig(double update) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				total.setProgress(update);
			}
		});
	}

	private class Sync extends Thread {

		private String ip;
		private String port;

		public Sync(String ip, String port) {
			this.ip = ip;
			this.port = port;
		}

		public void run() {

			try {
				output("Connecting to server");
				Socket server = new Socket(ip, Integer.parseInt(port));

				output("Connected to server");

				BufferedReader serverInput = new BufferedReader(new InputStreamReader(server.getInputStream()));
				PrintStream serverOuput = new PrintStream(server.getOutputStream());

				String input = "";

				int totalChecks = Integer.parseInt(serverInput.readLine());
				int checks = 0;

				LinkedList<String> fileNames = new LinkedList<String>();
				LinkedList<String> dirNames = new LinkedList<String>();

				while (!input.equals("c")) {
					input = serverInput.readLine();
					/*
					 * lets talk message commands d <dir> checks if directory exists : no return
					 * needed f <file> <hash code> checks if file exists : expects a true or false,
					 * true if the file doesnt need to sync, false if it does b <byte> byte for
					 * current file : no return needed c closes connection
					 */

					if (input.charAt(0) == 'f') {

						File cFile = new File(input.split(",")[1]);
						fileNames.add(cFile.getName());

						if (fileCheck(input.split(",")[1], Integer.parseInt(input.split(",")[2]))) {
							serverOuput.println(false);
							output(cFile.getName() + " is already up to date");
						} else {
							output(cFile.getName() + " needs updating");
							// sends message to server that we need to sync file
							serverOuput.println(true);

							int total = Integer.parseInt(serverInput.readLine());
							serverOuput.println("Recieved file size");

							/*
							 * heres what order the server should do this <size of file> b <byte of file
							 * until its done> b close
							 */

							if (!cFile.exists()) {
								cFile.getParentFile().mkdirs();
								cFile.createNewFile();
							}

							OutputStream out = new FileOutputStream(cFile.getPath());

							byte[] buffer = new byte[1024];
							int bytesRead;
							int totalBytesRead = 0;

							while (totalBytesRead < total && (bytesRead = server.getInputStream().read(buffer)) != -1) {

								out.write(buffer, 0, bytesRead);
								totalBytesRead += bytesRead;
								updateSmall(totalBytesRead / (double) total);
							}

							out.close();
							serverOuput.println("Recieved file");

						}
						checks++;
						updateBig(checks / (double) totalChecks);
					} else if (input.charAt(0) == 'd') {
						dirNames.add(input.split(",")[1]);
						dirCheck(input.split(",")[1]);
					}
				}

				server.close();

				/*
				 * clean directories
				 */
				for (String currentDir : dirNames) {
					for (File x : new File(currentDir).listFiles()) {
						if (!fileNames.contains(x.getName())) {
							x.delete();

							output("Deleted file:" + x.getName());
						}
					}
				}

				output("Seems like we are done here");

			} catch (NumberFormatException e) {
				output("Something went wrong");
				output(e.toString());
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				output("Failed to connect or maintain connection");
			}

		}
	}

	/**
	 * Checks if a directory exists, and if it does not, makes it
	 * 
	 * @param directory
	 */
	private void dirCheck(String directory) {
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	/**
	 * Checks if a file exists, and that it matches the hash code passed in.
	 * 
	 * @param name     Name of the file
	 * @param hashCode Hash code of the file
	 * @return false if the file does not exist or match the hash code
	 */
	private boolean fileCheck(String name, int hashCode) {

		File file = new File(name);

		if (!file.exists() || file.hashCode() != hashCode) {
			return false;
		}
		return true;
	}

}
