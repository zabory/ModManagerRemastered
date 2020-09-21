package client.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	private boolean install;

	public Sync(String ip, String port, ProgressBar currentProg, ProgressBar totalProg, VBox logOutput, boolean install) {
		this.ip = ip;
		this.port = port;
		this.currentProg = currentProg;
		this.totalProg = totalProg;
		this.logOutput = logOutput;
		this.install = install;
	}

	@SuppressWarnings("unchecked")
	public void run() {

		try {
			output("Connecting to server");
			Socket server = new Socket(ip, Integer.parseInt(port));

			output("Connected to server");

			BufferedReader serverInput = new BufferedReader(new InputStreamReader(server.getInputStream()));
			PrintStream serverOuput = new PrintStream(server.getOutputStream());

			String input = "";

			input = serverInput.readLine();
			
			if(install) {
				//find forge and see if it needs installed
				String forgeFileName = input.split("-")[1] + "-" + input.split("-")[0] + "-" + input.split("-")[2];
				File forge = new File("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\.minecraft\\versions\\" + forgeFileName);
				
				if(forge.exists()) {
					output("No need to download forge, its already installed");
					serverOuput.println(false);
				}else {
					output("Lets get minecraft forge going for ya");
					serverOuput.println(true);
					input = serverInput.readLine();
					File cFile = new File(input.split(",")[1]);
					recieveFile(server, input, cFile);
					new ProcessBuilder("cmd", "/c", "java -jar " + cFile.getName()).start();
				}
				
				String modpackName = serverInput.readLine();
				String ram = serverInput.readLine();
				
				//create the profile
				//load json data from minecraft launcher profiles
				JSONParser parser = new JSONParser();
				Object minecraftJSONFile = parser.parse(new FileReader("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\.minecraft\\launcher_profiles.json"));
				JSONObject minecraftJSON =  (JSONObject) minecraftJSONFile;
				
				
				//create the new profile for the new modpack
				JSONObject newProfile = new JSONObject();
				
				newProfile.put("gameDir", new File(".").getAbsolutePath().replace("\\.", ""));
				newProfile.put("icon", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAIAAABMXPacAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAEVZSURBVHhexd35v19Vdf/xC5ExDEkIcyCBBAhDGEIIECACBmRGga/VinYerG0dAAGlgME4VL/Vr0Nr57nWtk5VK+o/932e8/pk8+ESILEC64f92Gefvdde6/1ea+197s2FlTdajjnmmLVr165bt+6MM87YtGnTBRdccMkll1xxxRXXXHPN7t27b7rppr17995222133HHH3Xff/cADDzzyyCP33HPPjTfeeNFFF1l1wgknvO1tb1vo+mXIUUcdxSRqTznllFNPPfX0008/++yzzz//fNttn+XqWYx7m6xfv37DLKeddppxcuaZZ3JHe9ZZZ51zzjn8OvfccynRIZs3bza+2O+tFd4ee+yxJ510EuvZxMRt27ZdfvnlO3fuvOGGG2655ZYB/X333ffggw8+9NBD/2dJ3vnOd8LCwuOOO27NmjULpb+oHH300aA/8cQTYcoeCEKNiImLL744q8REAcE2UJpDMDQkiJex3rJlC6e2zqJz4YUXaulE8GLjt1CgJtYEsqjhKlsF2mWXXXbVVVddf/31b3/720H8rne9C9bvfe973//+93/gAx949NFHf/VXf/V973ufkV/5lV95z3ve4y1QeEUVRheqj0RALw5Az5JCIezoBL2ov/LKK6+99lppd+edd94/C6s8MthMbWIJlEHMC52ymRLkcarM1l566aU63i62fwul+sNnscN6drO14rNnzx4EvOMd7xD+Ah/6v/M7v/PHf/zHH/rQh3R+7dd+DRMGl8kAyq5du44osoKeDaJ+48aNZWGWBJOol2Tsufnmm2+//XY18IMf/KBN7S4avAI0TAmqih6EWWi5wR07dhihhFP6WNQKL48mnHzyyQs73hIRrUoHIwo6bvMhKwWXoA594YYA+HIY9Aj4oz/6o4985CM6v/3bvw0OWCRwkRDm8w2si21eRUBv944f+ceA8847T1SCz3JmVHDEAehJxthCEPze7/2e3T/84Q/bTsTIDwJ0ghL4Wqstb2DNHRVVX3vdLDQbdzwsrHlLpPoj9PgvkWWukMkH9UeR3bdvn/NW6Xfw8vzXf/3Xf+u3fgvoaPj93//9P/zD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");
				newProfile.put("lastVersionId", forgeFileName);
				newProfile.put("name", modpackName);
				newProfile.put("type", "custom");
				newProfile.put("javaArgs", "-Xmx" + ram + "G -Xmn1G");
				
				if(((JSONObject) minecraftJSON.get("profiles")).containsKey(modpackName)) {
					//change existing profile
					((JSONObject) minecraftJSON.get("profiles")).remove(modpackName);
					((JSONObject) minecraftJSON.get("profiles")).put(modpackName, newProfile);
				}else {
					//add the profile to json object
					((JSONObject) minecraftJSON.get("profiles")).put(modpackName, newProfile);
				}
				
				//save the file
				FileWriter output = new FileWriter("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Roaming\\.minecraft\\launcher_profiles.json");
				output.write(minecraftJSON.toString());
				output.flush();
				output.close();
				
				output("I installed the modpack profile on your minecraft launcher");
				
				
			}else {
				//we dont need forge, we arent installing
				serverOuput.println(false);
				serverInput.readLine();
				serverInput.readLine();
			}
			
			
			
			
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
					
					if(recieveFile(server, input, cFile)) {
						checks++;
						updateBig(checks / (double) totalChecks);
					}
					
					
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
			serverOuput.close();

		} catch (NumberFormatException e) {
			output("Something went wrong");
			output(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			output("Failed to connect or maintain connection");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private boolean recieveFile(Socket server, String input, File cFile) throws IOException {
		BufferedReader serverInput = new BufferedReader(new InputStreamReader(server.getInputStream()));
		PrintStream serverOuput = new PrintStream(server.getOutputStream());
		
		if (fileCheck(input.split(",")[1], Integer.parseInt(input.split(",")[2]),
				Long.parseLong(input.split(",")[3]))) {
			serverOuput.println(false);
			serverOuput.println(true);
			output(cFile.getName() + " is already up to date");
			return true;
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
				if(cFile.getParent() != null) {
					cFile.getParentFile().mkdirs();
				}
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
				return true;
			} else {
				serverOuput.println(false);
				return false;
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
