package dataStructures;

/**
 * File for the addon from curseforge
 * @author Ben Shabowski
 *
 */
public class AddonFile {
	
	String isAlternate;
	String isAvailable;
	String fileName;
	String displayName;
	String downloadUrl;
	String gameVersionFlavor;
	String fileDate;
	String id;
	Module[] modules;
	Dependency[] dependencies;
	String packageFingerprint;
	String gameVersionDateReleased;
	String fileStatus;
	String releaseType;
	String alternateFileId;
	String[] gameVersion;
	String hasInstallScript;
	String installMetadata;
	String fileLength;
	String serverPackFileId;
	
	public String toString() {
		return fileName + ":" + gameVersion[0] + ":" + downloadUrl;
	}
	
	public String getIsAlternate() {
		return isAlternate;
	}



	public void setIsAlternate(String isAlternate) {
		this.isAlternate = isAlternate;
	}



	public String getIsAvailable() {
		return isAvailable;
	}



	public void setIsAvailable(String isAvailable) {
		this.isAvailable = isAvailable;
	}



	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public String getDisplayName() {
		return displayName;
	}



	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}



	public String getDownloadUrl() {
		return downloadUrl;
	}



	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}



	public String getGameVersionFlavor() {
		return gameVersionFlavor;
	}



	public void setGameVersionFlavor(String gameVersionFlavor) {
		this.gameVersionFlavor = gameVersionFlavor;
	}



	public String getFileDate() {
		return fileDate;
	}



	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public Module[] getModules() {
		return modules;
	}



	public void setModules(Module[] modules) {
		this.modules = modules;
	}



	public Dependency[] getDependencies() {
		return dependencies;
	}



	public void setDependencies(Dependency[] dependencies) {
		this.dependencies = dependencies;
	}



	public String getPackageFingerprint() {
		return packageFingerprint;
	}



	public void setPackageFingerprint(String packageFingerprint) {
		this.packageFingerprint = packageFingerprint;
	}



	public String getGameVersionDateReleased() {
		return gameVersionDateReleased;
	}



	public void setGameVersionDateReleased(String gameVersionDateReleased) {
		this.gameVersionDateReleased = gameVersionDateReleased;
	}



	public String getFileStatus() {
		return fileStatus;
	}



	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}



	public String getReleaseType() {
		return releaseType;
	}



	public void setReleaseType(String releaseType) {
		this.releaseType = releaseType;
	}



	public String getAlternateFileId() {
		return alternateFileId;
	}



	public void setAlternateFileId(String alternateFileId) {
		this.alternateFileId = alternateFileId;
	}



	public String[] getGameVersion() {
		return gameVersion;
	}



	public void setGameVersion(String[] gameVersion) {
		this.gameVersion = gameVersion;
	}



	public String getHasInstallScript() {
		return hasInstallScript;
	}



	public void setHasInstallScript(String hasInstallScript) {
		this.hasInstallScript = hasInstallScript;
	}



	public String getInstallMetadata() {
		return installMetadata;
	}



	public void setInstallMetadata(String installMetadata) {
		this.installMetadata = installMetadata;
	}



	public String getFileLength() {
		return fileLength;
	}



	public void setFileLength(String fileLength) {
		this.fileLength = fileLength;
	}



	public String getServerPackFileId() {
		return serverPackFileId;
	}



	public void setServerPackFileId(String serverPackFileId) {
		this.serverPackFileId = serverPackFileId;
	}

}
