import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Utils.Utils;

/**  
* main.java - main  program.  
* @author  E. FOKOU and A. Khouiy
* @version 1.0
*/
public class main {
	
	/**
     * constant value for modeSXUMX mode.
     */
	static String modeSXUMX = "modeSXUMX";
	/**
     * constant value for modeUMXSMX mode.
     */
	static String modeUMXSMX = "modeUMXSMX";
	/**
     * file which contains succeed IP (these IP will be used for running parallel operation).
     */
	static String Ip_Succeed = "Ip_Succeed.txt";
	/**
     * file which contains list of IP on which communication test will be realized. If test is good, the current IP will be write on Ip_Succeed file
     */
	static String IP_Address = "IP_Address.txt";
	/**
     * This parameter give the maximum number of lines for Data blocks (UMx) who will be create by Master.
     */
	static double nbLinesinFile = 50.;
	
	/**
     * This parameter give the path where temporary file will be save p.
     */
	static String dataNode = "/cal/homes/efokou/";
	
	/**
     * This function is entry point of main program
     * 
     * @param args
     *            Array of argument to program. 
     *
     */
	public static void main(String[] args) throws IOException, InterruptedException {

		if (args.length == 4) {
			
			dataNode = args[1];
			nbLinesinFile = Integer.parseInt(args[2]);
			IP_Address  = args[3];
			
			
			HashMap<String, String> UMx_machines = new HashMap<String, String>();
			HashMap<String, ArrayList<String>> cles_UMx = new HashMap<String, ArrayList<String>>();
			List<String> ipSucceed = new ArrayList<String>();
			
			// reading of IP Address file
			List<String> machines = Utils.readIP(IP_Address);
			
			/*
			 * 
			 * start Initializing step
			 * 
			 */
			long beginInitializing = System.currentTimeMillis();
			
			System.out.println("\n===	Test des connexions sur les machines	===\n");
			
			for (String line : machines) {
				ProcessBuilder pb = new ProcessBuilder("ssh", line, "calc", "2+3");
				Process process = pb.start();
				int errCode = process.waitFor();
				if (errCode == 0) {
					System.out.println("Test connection on " + line + ": Succeed");
					ipSucceed.add(line);
					;
				} else {
					System.out.println("Test connection on " + line + " Failed");
				}
			}
			// write list of Ip for which communication is succeed into Ip_succeed file
			System.out.println("\n===	Sauvegarde des machines connectées	===\n");
			Utils.writeLines(ipSucceed, Ip_Succeed);
			
			// reading of IP succeed Address file
			machines = Utils.readIP(Ip_Succeed); 
	
			if (machines.size() == 0) {
				System.out.println("\n=== Aucune machine n'est disponible pour l'instant; veuillez connecter vos slaves!!!===\n");
				return;
			}
	
			long endInitializing = System.currentTimeMillis();
			/*
			 * 
			 * end Initializing step
			 * 
			 */
		
		
		
			
			/*
			 * 
			 * start Splitting step
			 * 
			 */
			long beginSplitting = System.currentTimeMillis();
			
			System.out.println("\n===	Lecture du fichier en entrée	===\n");
			List<String> input = Utils.readLines(args[0]);
			
			
			System.out.println("\n===	Debut de la phase de Splitting	===\n");
			int chunkSize;
			chunkSize = (int) Math.ceil(input.size() / nbLinesinFile) ;
			ArrayList<ArrayList<String>> chunkData =  new ArrayList<ArrayList<String>>();
			for (int i=0;i<chunkSize;i++){
				chunkData.add(new ArrayList<String>());
			}
			int index = 0;
			for (String line : input){
				chunkData.get(index%chunkSize).add(line);
				index++;
			}
			for (int i=0;i<chunkSize;i++){
				Utils.writeLines(chunkData.get(i),dataNode+"S" + String.valueOf(i));
			}
			System.out.println("\n===	Fin de la phase de Splitting	===\n");
			long endSplitting = System.currentTimeMillis();
			/*
			 * 
			 * end Splitting step
			 * 
			 */
			
			
			
			List<Long> listInitializingTime = new ArrayList<Long>();
			List<Long> listSplittingTime = new ArrayList<Long>();
			List<Long> listMappingTime = new ArrayList<Long>();
			List<Long> listShufflingTime = new ArrayList<Long>();
			List<Long> listReducingTime = new ArrayList<Long>();
			List<Long> listAssemblingTime = new ArrayList<Long>();
			
			List<String> list = new ArrayList<String>(machines);
			
			/*
			 * 
			 * this iteration on 'k' allow us to evaluate processing time regarding the numbers of available slave // we comment now 
			 * 
			 */
			
			//for (int k=2; k<list.size();k=k+2){
			
				
				
				
				//machines = list.subList(0, k);
				
				//System.out.println("\n===	Nombre de slaves "+machines.size()+"/"+list.size()+"	===\n");
				
				/*
				 * 
				 * start Mapping step
				 * 
				 */
				System.out.println("\n===	Debut de la phase de Mapping	===\n");
				
				long beginMapping = System.currentTimeMillis(); 
				System.out.println("\n===	Debut de la phase de Mapping	===\n");
				int size_list = input.size();
				Runner_Slave[] runner_Slave = new Runner_Slave[chunkSize];
				for (int i = 0; i < chunkSize; i++) {
					runner_Slave[i] = new Runner_Slave(
							machines.get(i % (machines.size())), i, dataNode+"S"
									+ String.valueOf(i), modeSXUMX);
					runner_Slave[i].start();
					System.out.println("UM" + String.valueOf(i) + " - "
							+ machines.get(i % (machines.size())));
					UMx_machines.put("UM" + String.valueOf(i),
							machines.get(i % (machines.size())));
				}
	
				System.out.println("\n");
				for (int i = 0; i < chunkSize; i++)
					runner_Slave[i].join();
				System.out.println("\n===	Fin de la phase de Mapping	===\n");
				long endMapping = System.currentTimeMillis();
				/*
				 * 
				 * end Mapping step
				 * 
				 */
				
				/*
				 * 
				 * start Shuffling step
				 * 
				 */
				long beginShuffling = System.currentTimeMillis();
				System.out.println("\n===	Debut de la phase de Shuffling	(Reducing inclus) ===\n");
				// cles_UMx
				for (int i = 0; i < chunkSize; i++) {
					for (int j = 0; j < runner_Slave[i].output.size(); j++) {
						// System.out.println(runner_Slave[i].output.get(j));
						String key = runner_Slave[i].output.get(j);
						if (!cles_UMx.containsKey(key))
							cles_UMx.put(key, new ArrayList<String>());
						cles_UMx.get(key).add("UM" + String.valueOf(i));
					}
				}
				
				// printing cles_UMx
				int indexSlaveUMxSMx = 0;
				int cles_UMxSize = cles_UMx.size();
				runner_Slave = new Runner_Slave[cles_UMxSize];
	
				for (Map.Entry<String, ArrayList<String>> entry : cles_UMx
						.entrySet()) {
					String key = entry.getKey();
					ArrayList<String> value = entry.getValue();
					String UMxList = "";
					System.out.print("clé " + key + " - liste d’UMs: <");
					for (int j = 0; j < value.size(); j++) {
						if (j == 0)
							UMxList = UMxList + dataNode + value.get(j);
						else
							UMxList = UMxList + " "+dataNode
									+ value.get(j);
						if (j == 0)
							System.out.print(value.get(j));
						else
							System.out.print(", " + value.get(j));
					}
					System.out.println(">");
					runner_Slave[indexSlaveUMxSMx] = new Runner_Slave(
							machines.get(indexSlaveUMxSMx % (machines.size())),
							indexSlaveUMxSMx, key, "SM"
									+ String.valueOf(indexSlaveUMxSMx), UMxList,
							modeUMXSMX);
					runner_Slave[indexSlaveUMxSMx].start();
					indexSlaveUMxSMx++;
				}
	
				List<String> RMxDataFinal = new ArrayList<String>();
				System.out.println("\n");
				// wait for all threads and get cle_UMx
				for (int i = 0; i < cles_UMxSize; i++)
					runner_Slave[i].join();
				System.out.println("\n===	Fin de la phase de Shuffling (Reducing inclus)	===\n");
				long endShuffling = System.currentTimeMillis();
				/*
				 * 
				 * end Shuffling step
				 * 
				 */
				
				/*
				 * 
				 * start Assembling step (we get here the time for Reducing step, so we could compute the effective time for the reducing step)
				 * 
				 */
				long beginAssembling = System.currentTimeMillis();
				System.out.println("\n===	Debut de la phase d'Assembling	===\n");
				// cles_UMx
				long [] timeReducing = new long[machines.size()];
				for (int i = 0; i < cles_UMxSize; i++) {
					for (int j = 0; j < runner_Slave[i].output.size(); j++) {
						if (runner_Slave[i].output.get(j).split(" ").length > 1){
							RMxDataFinal.add(runner_Slave[i].output.get(j));
							System.out.println(runner_Slave[i].output.get(j));
						}else{
							int indiceMachine = i % machines.size();
							timeReducing[indiceMachine] += Long.valueOf(runner_Slave[i].output.get(j));
						}
						
					}
				}
		
				System.out.println("\n===	Fin de la phase d'Assembling	===\n");
				long endAssembling = System.currentTimeMillis();
				/*
				 * 
				 * end Assembling step
				 * 
				 */
				
				/*
				 * 
				 * extraction of  Reducing step time (maximum value between list of value for reducing step)
				 * 
				 */
				long maxtimeReducing = timeReducing[0]; 
				for (int i = 0; i < timeReducing.length; i++) {
					if (timeReducing[i] > maxtimeReducing){
						//System.out.println("timeReducing "+i+" = "+timeReducing[i]);
						maxtimeReducing = timeReducing[i];
					}
				}
				/*
				 * 
				 * start Assembling step
				 * 
				 */
				 
				
				/*
				 * 
				 * printing most frequent word contained into input file
				 * 
				 */
				HashMap<String, Integer> RMxDataFinalMap = new HashMap<String, Integer>();
				for (String RMx : RMxDataFinal) {
					RMxDataFinalMap.put(RMx.split(" ")[0], Integer.valueOf(RMx.split(" ")[1]));
				}
				ValueComparator bvc = new ValueComparator(RMxDataFinalMap);
				TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
				sorted_map.putAll(RMxDataFinalMap);
				Utils.printMostFrequentWord(50, sorted_map);
				Utils.writeLinesMap(sorted_map, "RMxDataFinal");
				
				/*
				 * 
				 * Time processing and evaluation  for each step
				 * 
				 */
				System.out.println("\n===	Temps d'execution pour chacune des phases	===\n");
				long initializingTime =  endInitializing - beginInitializing; 
				long splittingTime =  endSplitting - beginSplitting; 
				long mappingTime = endMapping - beginMapping; 
				long shufflingTime = (endShuffling - beginShuffling) - maxtimeReducing; 
				long reducingTime = maxtimeReducing; 
				long assemblingTime = endAssembling - beginAssembling;
				System.out.println("\ninitializingTime : "+initializingTime+" ms");
				System.out.println("splittingTime : "+splittingTime+" ms");
				System.out.println("mappingTime : "+mappingTime+" ms");
				System.out.println("shufflingTime : "+shufflingTime+" ms");
				System.out.println("reducingTime : "+reducingTime+" ms");
				System.out.println("assemblingTime : "+assemblingTime+" ms");
				
//				listInitializingTime.add(initializingTime);
//				listSplittingTime.add(splittingTime);
//				listMappingTime.add(mappingTime);
//				listShufflingTime.add(shufflingTime);
//				listReducingTime.add(reducingTime);
//				listAssemblingTime.add(assemblingTime);
			
			//}
			
//			String entete = "";
//			String initializingTimeString = "initializingTimeString";
//			String splittingTimeString = "splittingTimeString";
//			String mappingTimeString = "mappingTimeString";
//			String shufflingTimeString = "shufflingTimeString";
//			String reducingTimeTimeString = "reducingTimeTimeString";
//			String assemblingTimeTimeString = "assemblingTimeTimeString";
			
			index = 0;
			//for (int k=2; k<machines.size();k=k+2){
//					entete = entete+","+String.valueOf(k);
//					initializingTimeString = initializingTimeString+","+String.valueOf(listInitializingTime.get(index));
//					splittingTimeString = splittingTimeString+","+String.valueOf(listSplittingTime.get(index));
//					mappingTimeString = mappingTimeString+","+String.valueOf(listMappingTime.get(index));
//					shufflingTimeString = shufflingTimeString+","+String.valueOf(listShufflingTime.get(index));
//					reducingTimeTimeString = reducingTimeTimeString+","+String.valueOf(listReducingTime.get(index));
//					assemblingTimeTimeString = assemblingTimeTimeString+","+String.valueOf(listAssemblingTime.get(index));
			//}
			
//			ArrayList<String> allResult = new ArrayList<>();
//			allResult.add(entete);
//			allResult.add(initializingTimeString);
//			allResult.add(splittingTimeString);
//			allResult.add(mappingTimeString);
//			allResult.add(shufflingTimeString);
//			allResult.add(reducingTimeTimeString);
//			allResult.add(assemblingTimeTimeString);

			//Utils.writeLines(allResult, dataNode+"time_Result_"+k+"_"+new Double(nbLinesinFile).intValue()+".txt");
			
		} else {
			
			help();
		}
		System.out.println("\nMain process: Tout est fini");
	}

	private static void help() {
		System.out.println("Utiliser cette syntaxe pour executer le prgramme:\n");
		System.out.println("\tjava -jar MASTER_SHAVADOOP_JAR /users/johnDoe/Desktop/input.txt /users/johnDoe/Desktop/temp 50 /users/johnDoe/Desktop/IP_Address.txt\n");
		System.out.println("\tou '/users/johnDoe/Desktop/input.txt': Fichier à traiter\n");
		System.out.println("\t   '/users/johnDoe/Desktop/temp': Dossier pour stocker les fichiers temporaires des traitements\n");
		System.out.println("\t   '50': Nombre de ligne dans les fichiers après splitting sur le fichier initial\n");
		System.out.println("\t   '/users/johnDoe/Desktop/IP_Address.txt': liste des possibles machines (une par ligne) à utiliser pour les traitements\n");
	}

}