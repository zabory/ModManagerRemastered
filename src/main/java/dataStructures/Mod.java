package dataStructures;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Mod {
	
	//file of the mod
	private File modFile;
	
	//current version of the mod
	private String currentVersion;
	
	//name of the mod
	private String modName;
	
	//Corresponding addon (mod) from curseforge
	private Addon curseForgeMod;
	
	
	public Mod(File modFile) {
		this.modFile = modFile;
		
		try {
			//extract the mcmod.info
			new ProcessBuilder("cmd", "/c", "jar xf " + modFile.getName() + " mcmod.info").directory(modFile.getParentFile()).start().waitFor();	
			
			//find and open the mod info file
			File modInfo = new File(modFile.getParentFile().getPath() + "\\mcmod.info");
			Scanner infoInput = new Scanner(modInfo);
			
			//go through the file and extract the data
			while(infoInput.hasNextLine()) {
				String input = infoInput.nextLine();
				//gets the name of the mod
				if(input.contains("\"name\":")) {
					modName = input.replace("\"name\": \"", "").replace("\",", "");
				}
				//gets the version of the mod
				if(input.contains("\"version\": \"")) {
					currentVersion = input.replace("\"version\": \"", "").replace("\",", "");
				}
			}
			//close the input stream and delete the file
			infoInput.close();
			modInfo.delete();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}
}
