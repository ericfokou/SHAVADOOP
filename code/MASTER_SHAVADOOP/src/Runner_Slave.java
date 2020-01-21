import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**  
* Runner_Slave.java - Class for running operation (mapping, Shuffling) on slave machine.  
* @author  E. FOKOU and A. Khouiy
* @version 1.0
*/
public class Runner_Slave  extends Thread {
	
	/**
     * Ip address on which run current operation.
     */
	private String ip_address;
	/**
     * identification value of a Runner Slave.
     */
	private long id;
	/**
     * Input Split to create .
     */
	private String Sx;
	/**
     * Output stream for the current Runner slave.
     */
	ArrayList<String> output = new ArrayList<String>();
	/**
     * Mode of operation to run (modeSXUMX, modeUMXSMX).
     */
	String mode;
	/**
     * Sorted Map to create.
     */
	private String smx;
	/**
     * String which represent a set of Unsorted Map to process.
     */
	private String UMxList;
	/**
     * Current word to process in mode modeUMXSMX.
     */
	private String key;
	
	/**
     * Constructor
     * 
     * @param ip_address
     * @param id
     *
     */
	public Runner_Slave(String ip_address, int id) {
		super();
		this.ip_address = ip_address;
		this.id = id;
	}
	
	/**
     * Constructor
     * 
     * @param ip_address
     * @param id
     * @param sx
     * @param mode
     *
     */
	public Runner_Slave(String ip_address, long id, String sx, String mode) {
		super();
		this.ip_address = ip_address;
		this.id = id;
		this.mode = mode;
		Sx = sx;
		
	}
	
	/**
     * Constructor
     * 
     * @param ip_address
     * @param id
     * @param key
     * @param smx
     * @param UMxList
     * @param mode
     *
     */
	public Runner_Slave(String ip_address, long id, String key, String smx, String UMxList, String mode) {
		super();
		this.ip_address = ip_address;
		this.id = id;
		this.key = key;
		this.smx = smx;
		this.UMxList = UMxList;
		this.mode = mode;
		
	}
	
	/**
     * getter for id
     *
     * @return long value. 
     */
	public long getId() {
		return id;
	}
	
	/**
     * setter for id
     *
     * @param id
     */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
     * getter for ip_address
     *
     * @return String value. 
     */
	public String getIp_address() {
		return ip_address;
	}
	
	/**
     * setter for ip_address
     *
     * @param ip_address 
     */
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	
	/**
     * Function to run by Runner slave given a mode
     * @exception If exception during execution, we get this: "Cannot run on 'ip_address'"
     */
	public void run() {
		ProcessBuilder pb = null ;
		
		if (mode.compareToIgnoreCase("modeSXUMX") == 0)
			pb = new ProcessBuilder("ssh", ip_address, "cd", "/cal/homes/efokou/INF727;", "java", "-jar", "SLAVE_SHAVADOOP.jar",mode, Sx); 
		else if (mode.compareToIgnoreCase("modeUMXSMX") == 0){
			pb = new ProcessBuilder("ssh", ip_address, "cd", "/cal/homes/efokou/INF727;", "java", "-jar", "SLAVE_SHAVADOOP.jar", mode, key, smx, UMxList); 
		}
		
		Process process = null;
		try {
			
			process = pb.start();
	        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        
	        String ligne = "";
	        try {
	            while ((ligne = br.readLine()) != null) {
	                output.add(ligne);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        
	        ligne = "";
	        try {
	            while ((ligne = br.readLine()) != null) {
	                output.add(ligne);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        process.waitFor();
	        
	        
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot run on "+ip_address);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
    }
}
