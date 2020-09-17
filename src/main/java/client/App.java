package client;

import java.io.File;
import java.util.List;

import Installer.InstallerHead;
//import client.admin.AdminHead;
import client.client.ClientHead;
import javafx.application.Application;
import javafx.stage.Stage;
import server.ServerHead;

/**
 * Application head. Launches appropriate program since there are technically 3
 * built in.
 * 
 * @author Ben Shabowski
 * @version 0.1
 * @since 0.1
 */
public class App extends Application {

	@Override
	public void start(Stage arg0) throws Exception {
		List<String> args = this.getParameters().getRaw();

		// Launch specific applications
		for (String x : args) {
			switch (x) {
			case "-admin":
				//new AdminHead();
				break;
			case "-server":
				new ServerHead();
				break;
			default:
				new ClientHead();
				break;
			}
		}

		// if we get here that means theres no arguments and was launched by clicking
		// the jar file
		// test for is mods and configs exist, this will determine which thing to launch
		File modsFolder = new File("mods");
		if (modsFolder.exists()) {
			new ClientHead();
		} else {
			new InstallerHead();
		}
	}
}
