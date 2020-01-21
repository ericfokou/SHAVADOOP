package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**  
* Utils.java - Class containing utility functions for the program.  
* @author  E. FOKOU and A. Khouiy
* @version 1.0
*/
public class Utils {
	
	/**
     * This function write into a file a set of lines where each line is given by an entry of arrayList
     * 
     * @param lines
     * 			  ArrayList of string which each entry represent a line
     * @param fileName
     *            file path to write. 
     * 
     * @return status value which check if write is ended correctly or not. 
     */
	public static boolean writeLines(List<String> lines, String fileName) {
		boolean status;
		try {
            FileWriter fileWriter =
                new FileWriter(fileName);

            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);
            
            for (String line:lines){
            	bufferedWriter.write(line);
            	bufferedWriter.write("\n");
            }

            bufferedWriter.close();
            status =  true;
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            status = false;
        }
		return status;
	}
	
	/**
     * This function read a file by removing empty lines and special characters and return an arrayList of String which each entry represent a line
     * 
     * @param fileName
     *            file path to read. 
     *
     * @return ArrayList of string which each entry represent a line. 
     */
	public static ArrayList<String> readLines(String fileName) {
		
		List<String> lines = new ArrayList<String>();

        String line = null;

        try {
            FileReader fileReader = 
                new FileReader(fileName);

            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	lines.add(line);
            }   
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");      
        }
		return (ArrayList<String>) lines;
	}

}
