package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Server head class to launch server part of application. The server will host
 * the mods and serve them to clients. This can get launched with arguments, or
 * through forge server
 * 
 * @author Ben Shabowski
 * @version 0.2
 * @since 0.1
 *
 */
public class ServerHead {

	public ServerHead() {

		File properties = new File("modmanager.properties");
		if (!properties.exists()) {
			try {
				properties.createNewFile();
				FileWriter out = new FileWriter(properties);
				out.write("port=42069\n");
				out.write("dirs_to_sync=\n");
				out.write("ignore=");
				out.flush();
				out.close();
				System.out.println("Server properties file created. Populated fields and restart.");
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// port of server
		String port = "";

		// list of directories to sync and files to sync
		LinkedList<File> dirSync = new LinkedList<File>();
		LinkedList<File> fileSync = new LinkedList<File>();
		LinkedList<String> ignore = new LinkedList<String>();

		try {
			Scanner propertiesInput = new Scanner(properties);

			// go through the config file
			while (propertiesInput.hasNextLine()) {
				String line = propertiesInput.nextLine();
				if (line.contains("port=")) {
					port = line.replace("port=", "");
				} else if (line.contains("dirs_to_sync=")) {
					// make new files for the dirs
					for (String dir : line.replace("dirs_to_sync=", "").replace(" ", "").split(",")) {
						System.out.println("Adding " + dir + " as a directory to sync");
						dirSync.add(new File(dir));
					}
				} else if (line.contains("ignore=")) {
					for (String x : line.replace("ignore=", "").replace(" ", "").split(",")) {
						System.out.println("Ignoring file:" + x);
						ignore.add(x);
					}
				}
			}

			propertiesInput.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (dirSync.size() == 0) {
			System.out.println("We need directories to sync");
			System.exit(1);
		}

		// goes through all the dirs
		for (File x : dirSync) {
			// if the directory actually exists, add all the files to the file list
			if (x.exists()) {
				fileSync.addAll(getFiles(x, ignore));
			}
		}

		// remove files that we dont want to sync
		for (int i = 0; i < fileSync.size(); i++) {
			File x = fileSync.get(i);
			if (ignore.contains(x.getName())) {
				fileSync.remove(x);
				i--;
			} else {
				System.out.println("Registering file to be synced:" + x.getName());
			}
		}
		
		
		//get forge file
		File forge = null;
		
		for(File x : new File(System.getProperty("user.dir")).listFiles()) {
			if(x.getName().contains("forge") && x.getName().contains("installer")) {
				forge = new File(x.getName());
				break;
			}
		}
		
		if(forge == null) {
			System.out.println("Please provide forge installer");
			System.exit(1);
		}else {
			System.out.println("Registering forge version: " + forge.getName().replace("forge-", "").replace("-installer.jar", ""));
		}

		try {
			System.out.println("Starting server on port " + port);
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(42069);
			System.out.println("Waiting for connections...");
			while (true) {
				new ClientHandler(server.accept(), dirSync, fileSync, forge).start();
			}

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}

	}

	private ArrayList<File> getFiles(File dir, LinkedList<String> ignore) {
		ArrayList<File> files = new ArrayList<File>();
		for (File x : dir.listFiles()) {
			if (x.isDirectory() && !ignore.contains(x.getName())) {
				files.addAll(getFiles(x, ignore));
			} else if (!x.isDirectory()) {
				files.add(x);
			}
		}
		return files;
	}

	private class ClientHandler extends Thread {

		private Socket client;
		private LinkedList<File> dirSync;
		private LinkedList<File> fileSync;
		private File forge;

		public ClientHandler(Socket client, LinkedList<File> dirSync, LinkedList<File> fileSync, File forge) {
			this.client = client;
			this.dirSync = dirSync;
			this.fileSync = fileSync;
			this.forge = forge;

		}

		public void run() {

			System.out.println("Connection recieved from:" + client.getInetAddress());

			try {
				PrintStream output = new PrintStream(client.getOutputStream());
				BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
				//Send forge version
				output.println(forge.getName());
				
				//listen if we need to send it
				if(Boolean.parseBoolean(input.readLine())) {
					sendFile(forge);
				}
				
				output.println("Blackhole");
				output.println(9);
				
				//Send how many files to sync
				output.println(fileSync.size());

				for (File x : dirSync) {
					output.println("d," + x.getName());
				}

				for (File x : fileSync) {
					sendFile(x);
				}

				output.println("c");

				input.close();
				output.close();
				client.close();
				System.out.println("Closing connection with:" + client.getInetAddress());
			} catch (IOException e) {
				System.out.println("We lost connection with:" + client.getInetAddress());
			}

		}
		
		private void sendFile(File x) throws IOException {
			PrintStream output = new PrintStream(client.getOutputStream());
			BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			boolean isCorrect = false;
			while (!isCorrect) {
				output.println("f," + x.getPath() + "," + x.hashCode() + "," + x.length());
				String reply = input.readLine();
				if (reply.equals("true")) {

					byte[] bytes = new byte[(int) x.length()];
					BufferedInputStream fileInput = new BufferedInputStream(new FileInputStream(x));
					fileInput.read(bytes, 0, bytes.length);

					output.println(bytes.length);
					input.readLine();

					client.getOutputStream().write(bytes, 0, bytes.length);

					fileInput.close();

					input.readLine();
				}
				isCorrect = Boolean.parseBoolean(input.readLine());
			}
		}
	}
}
