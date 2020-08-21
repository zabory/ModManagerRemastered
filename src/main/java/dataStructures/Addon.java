package dataStructures;

/**
 * Curse forge mod addon
 * @author Ben Shabowski
 *
 */
public class Addon {

	private String id;
	private String name;
	private String URL;
	
	
	public Addon(String id, String name, String URL) {
		this.id = id;
		this.name = name;
		this.URL = URL;
	}


	public String getURL() {
		return URL;
	}


	public void setURL(String uRL) {
		URL = uRL;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
}
