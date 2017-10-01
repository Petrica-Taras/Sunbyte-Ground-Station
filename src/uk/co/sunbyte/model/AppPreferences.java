package uk.co.sunbyte.model;

import java.util.prefs.Preferences;

/**
 * @author Petrica Taras
 * @version 1.0
 *
 */
public class AppPreferences {
	private Preferences prefs;	
	
	public AppPreferences() {
		prefs = Preferences.userNodeForPackage(this.getClass());
		
		this.setIPDefaults(); 
		this.setServerPortsDefaults();
		this.setCommunicationCommandsDefaults();
	}
	
	private void setIPDefaults() {	
		prefs.put("Ground Station IP", "172.16.18.130");
		prefs.put("Main Controller IP", "172.16.18.131");
		prefs.put("Sensor Controller IP", "172.16.18.132");
		prefs.put("Rapberry Pi IP", "172.16.18.133");
		prefs.put("PC IP", "172.16.18.134");
	}
	
	private void setCommunicationCommandsDefaults() {
		prefs.put("Enquire Main Controller Status", "a");
		prefs.put("Enquire Sensor Controller Status", "b");
		prefs.put("Enquire Main Controller Status", "c");
	}
	
	private void setServerPortsDefaults() {
		prefs.putInt("Ground Station Port", 9999);
	}
	
	public String getGroundStationIP() {
		return prefs.get("Ground Station IP", "172.16.18.130");
	}
	
	public String getGroundStationName() {
		return "Ground Station IP";
	}
	
	public String getMainControllerIP() {
		return prefs.get("Main Controller IP", "172.16.18.131");
	}
	
	public String getSensorControllerIP() {
		return prefs.get("Sensor Controller IP", "172.16.18.132");
	}
	
    public String getRPiIP() {
		return prefs.get("Rapberry Pi IP", "172.16.18.133");
	}	
    
    public String getPCIP() {
		return prefs.get("PC IP", "172.16.18.134");
	}	
    
    public int getGroundStationServerPort() {
		return prefs.getInt("Ground Station Port", 9999);
	}	
}
