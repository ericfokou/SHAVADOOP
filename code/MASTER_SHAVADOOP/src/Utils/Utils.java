package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

/**  
* Utils.java - Class containing utility functions for the program.  
* @author  E. FOKOU and A. Khouiy
* @version 1.0
*/
public class Utils {
	
	
	/**
     * This function read a file by removing empty lines and special characters and return an arrayList of String which each entry represent a line
     * 
     * @param fileName
     *            file path to read. 
     *
     * @return ArrayList of string which each entry represent a line. 
     */
	public static ArrayList<String> readLines(String fileName) throws IOException {

		List<String> lines = new ArrayList<String>();

		
		String line = null;
		
		
		String wordToSkip = "s',l',est,a,sur,sont,as,suis,es,sommes,êtes,ont,avez,avons,ai,pour,son,même,sous,dans,si,sa,se,ce,cette,ses,ces,ceux,celles,du,au,"
				+ "en,je,tu,il,elle,nous,vous,ils,elles,le,la,l',lui,les,nous,vous,leur,eux,celui,celle,celui-ci,celui-là,celle-ci,celle-là,ceci,cela,ça,mien,tien,"
				+ "sien,nôtre,vôtre,leur,mienne,tienne,sienne,miens,tiens,siens,nôtres,vôtres,leurs,miennes,tiennes,siennes,leurs,on,personne,rien,aucun,aucune,d'aucun,"
				+ "d'aucune,nul,nulle,l'un,l'une,l'autre,et,ni,pas,une,un,tout,toute,toutes,quelqu'une,quelqu'un,quelque,chose,certain,certains,certaine,certaines,plusieurs,"
				+ "d'aucunes,d'aucuns,nuls,nulles,uns,unes,autres,tous,d'autres,qui,que,quoi,dont,par,où,ou,ne,lequel,à,laquelle,duquel,auquel,lesquels,desquels,auxquels,lesquelles,d"
				+ "es,desquelles,auxquelles,de,aux";
		
		String[] wordToSkipListArray = wordToSkip.split(",");
		
		
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader1 = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader1 = new BufferedReader(fileReader1);

			while ((line = bufferedReader1.readLine()) != null) {
				if (!line.isEmpty()){
					String  result = line.replaceAll("[^\\w\\sêéèûùîç\']","");
					
					for(String s:wordToSkipListArray){
						result=result.replaceAll("\\b(?i)"+s+"\\b","");
						//System.out.println("result :"+result);
					}
					lines.add(result);
				}
			}

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		return (ArrayList<String>) lines;
	}
	
	/**
     * This function read a file which contains IP address on each line and return ArrayList of string
     * 
     * @param fileName
     *            file path to read. 
     *
     * @return ArrayList of string which each entry represent a line (or IP). 
     */
	public static ArrayList<String> readIP(String fileName) {

		List<String> lines = new ArrayList<String>();

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				if (!line.isEmpty()){
					lines.add(line);
				}else
					System.out.println("Ligne vide");
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		// conversation facultative
		return (ArrayList<String>) lines;
	}
	
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
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(fileName);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (String line : lines) {
				bufferedWriter.write(line);
				bufferedWriter.write("\n");
			}

			// Always close files.
			bufferedWriter.close();
			status = true;
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
			status = false;
		}
		return status;
	}
	
	/**
     * This function write into a file a set of lines where each line is given by an entry of arrayList
     * 
     * @param map
     *            HashMap of <string,Integer> where each entry represent a word and the corresponding count of word into file. 
     * @param fileName
     *            file path to write. 
     * 
     * @return status value which check if write is ended correctly or not. 
     */
	public static void writeLinesMap(TreeMap<String, Integer> map, String fileName) {
		
			List<String> data = new ArrayList<String>();
			for (Entry<String, Integer> entry : map.entrySet()) {
				String key = entry.getKey();
				Integer value = entry.getValue();
				data.add(key+"  "+value);
			}
			writeLines(data, fileName);
	}
	
	/**
     * This function print the most frequents words given a HashMap of <string,Integer>
     * 
     * @param maxNumber
     * 			  Maximum number of word to print
     * @param map
     *            HashMap of <string,Integer> where each entry represent a word and the corresponding count of word into file. 
     *  
     */
	public static void printMostFrequentWord(int maxNumber, TreeMap<String, Integer> map){
		if (map.size() < maxNumber)
			maxNumber = map.size();
			int count = 0;
			System.out.println("\n===	les "+maxNumber+" mots les plus frequents sont:	===\n");
			for (Entry<String, Integer> entry : map.entrySet()) {
				String key = entry.getKey();
				Integer value = entry.getValue();
				count++;
				System.out.println(key+"  "+value);
				if (count >= maxNumber)
					break;
			}
	}
	
	
}
