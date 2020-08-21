package dataStructures;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AddonInterfacer {
	
	/**
	 * Gets the latest file version of the addon based off the the minecraft version
	 * @param version Version of minecraft the mod is for
	 */
	public static AddonFile getLastestAddonFile(String addonId, String version) {
		
		Client client = ClientBuilder.newClient();
		Response response = client.target("https://addons-ecs.forgesvc.net/api/v2/addon/" + addonId + "/files")
		  .request(MediaType.APPLICATION_JSON_TYPE)
		  .get();
		
		ObjectMapper mapper = new ObjectMapper();
		String body = response.readEntity(String.class);
		
		try {
			AddonFile[] files = mapper.readValue(body, AddonFile[].class);
			
			AddonFile currentRelease = files[0];
			
			for(AddonFile f : files) {
				if(f.getGameVersion()[0].equals(version)) {
					if(f.getReleaseType().equals("1")) {
						long timeCode = Long.parseLong(f.getFileDate().replaceAll("[-T:.Z]", ""));
						long currentTimeCode = Long.parseLong(currentRelease.getFileDate().replaceAll("[-T:.Z]", ""));
						
						if(timeCode > currentTimeCode) {
							currentRelease = f;
						}
					}
				}
			}
			
			return currentRelease;
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Returns the Addon Ids of the search from curseforge
	 * @param name
	 * @param version
	 */
	public static Addon searchAddon(String name, String version) {
		
		int categoryID = 0;
		int gameId = 432;
		String gameVersion = version;
		int index = 0;
		int pageSize = 25;
		String searchFilter = name;
		int sectionId = 6;
		int sort = 0;
		
		Client client = ClientBuilder.newClient();
		Response response = client.target("https://addons-ecs.forgesvc.net/api/v2/addon/search?" +
				"categoryId=" + categoryID + 
				"&gameId=" + gameId + 
				"&gameVersion=" + gameVersion + 
				"&index=" + index + 
				"&pageSize=" + pageSize + 
				"&searchFilter=" + searchFilter + 
				"&sectionId=" + sectionId + 
				"&sort=" + sort)
		  .request(MediaType.TEXT_PLAIN_TYPE)
		  .get();
		
		
		String body = response.readEntity(String.class);
		
		JSONObject test = new JSONObject(body.substring(1, body.length() - 2));
		
		return new Addon(test.get("id") + "", test.get("name") + "", test.get("websiteUrl") + "");
		
	}

}
