package uk.co.sunbyte.model;

import java.util.prefs.Preferences;

/**
 * @author Petrica Taras
 * @version 1.0
 *
 */
public class AppPreferences {
	private Preferences ips;
	
	public AppPreferences() {
		this.setDefaults(); 
	}
	
	public void setDefaults() {
		// set ip addresses
		ips.put("localhost IP", "169.254.131.160");
		ips.put("Leonardo IP", "169.254.131.159");
		ips.put("EtherMega IP", "169.254.131.158");
		ips.put("Raspberry Pi IP", "169.254.131.157");
	}
	
	public String getLocalhostIP() {
		return ips.get("localhost IP", "169.254.131.160");
	}
	
	public String getLeonardoIP() {
		return ips.get("Leonardo IP", "169.254.131.159");
	}
	
	public String getEtherMegaIP() {
		return ips.get("EtherMega IP", "169.254.131.158");
	}
	
    public String getRpiIP() {
		return ips.get("Raspberry Pi IP", "169.254.131.157");
	}	
}
