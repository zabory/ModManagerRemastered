package client.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

/**
 * This is to connect to a server and update all the mods based off what the
 * server wants updated
 * 
 * @author Ben Shabowski
 * @version 0.2
 * @since 0.2
 */
public class Sync extends Thread {

	private ProgressBar currentProg;
	private ProgressBar totalProg;
	private VBox logOutput;
	private String ip;
	private String port;

	public Sync(String ip, String port, ProgressBar currentProg, ProgressBar totalProg, VBox logOutput) {
		this.ip = ip;
		this.port = port;
		this.currentProg = currentProg;
		this.totalProg = totalProg;
		this.logOutput = logOutput;
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

					if (fileCheck(input.split(",")[1], Integer.parseInt(input.split(",")[2]),
							Long.parseLong(input.split(",")[3]))) {
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
						if (cFile.length() == Long.parseLong(input.split(",")[3])) {
							serverOuput.println(true);
						} else {
							serverOuput.println(false);
						}
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
	private boolean fileCheck(String name, int hashCode, long size) {

		File file = new File(name);

		if (!file.exists() || file.hashCode() != hashCode || file.length() != size) {
			return false;
		}
		return true;
	}

	public void output(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				logOutput.getChildren().add(new Label(message));
			}
		});
	}

	public void updateSmall(double update) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				currentProg.setProgress(update);
			}
		});
	}

	public void updateBig(double update) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				totalProg.setProgress(update);
			}
		});
	}
}
