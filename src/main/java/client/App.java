package client;

import client.admin.AdminHead;
import client.client.ClientHead;
import server.ServerHead;

/**
 * Application head. Launches appropriate program since there are technically 3 built in.
 * @author Ben Shabowski
 * @version 0.1
 * @since 0.1
 */
public class App {

	public static void main(String[] args) {
		
		//Launch specific applications
		for(String x : args) {
			switch(x) {
			case "-admin":
				new AdminHead();
				break;
			case "-server":
				new ServerHead();
				break;
			default:
				new ClientHead();
				break;
			}
		}
	}

}
