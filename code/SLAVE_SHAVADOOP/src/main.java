import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import Utils.Utils;

/**  
* main.java - main  program.  
* @author  E. FOKOU and A. Khouiy
* @version 1.0
*/


public class main {
	

	
	/**
     * This parameter give the path where temporary file will be save p.
     */
	static String dataNode = "/cal/homes/efokou/";
	
	
	/**
     * This function is entry point of main program. Given mode we can have: 
     * 
     * ---- java -jar SLAVE_SHAVADOOP_JAR modeSXUMX Sx --- OR
     * 
     * ---- java -jar SLAVE_SHAVADOOP_JAR modeUMXSMX Car SM1 /users/johnDoe/Desktop/UM1 /users/johnDoe/Desktop/UM2 ... /users/johnDoe/Desktop/UMx ----
     * 
     * @param args
     *            Array of argument to program. 
     *
     */
	public static void main(String[] args) throws InterruptedException, UnknownHostException {
		
		
		if (args.length > 0){
			String mode = args[0];
			if (mode.compareToIgnoreCase("modeSXUMX") == 0){
				String Sx = args[1];
				int index = Integer.parseInt(new File(Sx).getName().substring(1));
				List<String> sxData = Utils.readLines(Sx);
				List<String> keys = new ArrayList<String>();
				List<String> sxDataWord = new ArrayList<String>();
				for (int i=0;i<sxData.size();i++){
					String[] sxDataSplit = sxData.get(i).split(" ");
					for (String word:sxDataSplit){
						if (!keys.contains(word) && !word.isEmpty() && !(word.compareToIgnoreCase(" ")==0) ){
							System.out.println(word);
							keys.add(word);
						}
						if (!word.isEmpty() && !(word.compareToIgnoreCase(" ")==0) )
							sxDataWord.add(word+" 1");
					}
				}
				Utils.writeLines(sxDataWord, (new File(Sx).getParent())+"/UM"+String.valueOf(index));
			}else if (mode.compareToIgnoreCase("modeUMXSMX") == 0){ // mode == modeUMXSMX
				String key= args[1];
				String SMx = args[2];
				String RMx = "RM"+SMx.substring(2);
				int nombreUMx = args.length - 3;
				List<String> SMxData = new ArrayList<String>();
				List<String> RMxData = new ArrayList<String>();
				int compteur = 0;
				for (int j=0; j<nombreUMx; j++){
					String UMx = args[j+3];
					List<String> UMxData = Utils.readLines(UMx);
					for (String UMxline: UMxData){
						if (UMxline.split(" ")[0].compareToIgnoreCase(key) == 0){
							SMxData.add(key+" 1");
						}
					}
				}
				Utils.writeLines(SMxData, (new File(args[3]).getParent())+"/"+SMx); // args[3] represent first  UMx with absolute path
				
				long beginReducing = System.currentTimeMillis();
				for (int j=0; j<nombreUMx; j++){
					String UMx = args[j+3];
					List<String> UMxData = Utils.readLines(UMx);
					for (String UMxline: UMxData){
						if (UMxline.split(" ")[0].compareToIgnoreCase(key) == 0){
							compteur++;
						}
					}
				}
				RMxData.add(key+" "+compteur);
				System.out.println(key+" "+compteur);
				Utils.writeLines(RMxData, (new File(args[3]).getParent())+"/"+RMx); // args[3] represent first  UMx with absolute path
				long endReducing = System.currentTimeMillis();
				System.out.println(endReducing - beginReducing);
				
			}
			else{
				System.out.println("mode inconnu");
				help();
			}
			
		}else{
			help();
		}
	}

	private static void help() {
		System.out.println("Utiliser cette syntaxe pour executer le prgramme:\n");
		System.out.println("\tmode modeSXUMX: java -jar SLAVE_SHAVADOOP_JAR modeSXUMX Sx");
		System.out.println("\n\tou\n");
		System.out.println("\tmode modeUMXSMX: java -jar SLAVE_SHAVADOOP_JAR modeUMXSMX Car SM1 /users/johnDoe/Desktop/UM1 /users/johnDoe/Desktop/UM2 ... /users/johnDoe/Desktop/UMx");
		
	}

}