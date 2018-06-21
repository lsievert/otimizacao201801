import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Utils {
	
		private static final String END_OF_FILE = "-1,-1";
		private static final int TOTAL_NEIGHBOR = 30;
		private static final int MOVEMENT = 1;
		
		
	    private Map<Integer, ArrayList<Integer>> mapCopy(Map<Integer, ArrayList<Integer>> original){
	    	Map<Integer, ArrayList<Integer>> copy = new HashMap<Integer, ArrayList<Integer>>();
	    	ArrayList<Integer> copyTasks;
	    	ArrayList<Integer> originalTasks;
	    	for(int key : original.keySet()) {
	    		copyTasks = new ArrayList<Integer>();
	    		originalTasks = original.get(key);
	    		for(int task : originalTasks) {
	    			copyTasks.add(task);
	    		}
	    		copy.put(key, copyTasks);
	    	}
	    	return copy;
	    }
		
		public int evaluateSolution(Map<Integer, ArrayList<Integer>> solution, Map<Integer, Integer> taskCost) {
	    	int totalValue;
	    	int maxValue = 0;
	    	ArrayList<Integer> stationTasks;
	    	for(int station : solution.keySet()) {
	    		totalValue = 0;
	    		stationTasks = solution.get(station);
	    		for(int task : stationTasks) {
	    			totalValue =  totalValue + taskCost.get(task);
	    		}
	    		if(totalValue > maxValue) {
	    			maxValue = totalValue;
	    		}
	    	}
	    	return maxValue;
	    }
		
		
		public ArrayList<Map<Integer, ArrayList<Integer>>> findNeighborhood(Map<Integer, ArrayList<Integer>> currentSolution, int stationNumber, int numberOfTasks, Map<Integer, ArrayList<Integer>>  dependencies ){
			ArrayList<Map<Integer, ArrayList<Integer>>> neighbors = new ArrayList<Map<Integer, ArrayList<Integer>>>();
			Map<Integer, ArrayList<Integer>> currentNeighborBuild;
			Random random = new Random();
			int moviment;
			int attempts;
			int neighborsFound;
			int taskNumberCurrentStation;
			int taskNumberNextStation;
			ArrayList<Integer> currentStation;
			ArrayList<Integer> nextStation;
			for(int i = 1; i <= stationNumber - 1; i++) {
				attempts = 1;
				neighborsFound = 1;
				while(neighborsFound <= TOTAL_NEIGHBOR && attempts < 100) {
					moviment = 1;
					while(moviment <= MOVEMENT && attempts < 100) {
						currentNeighborBuild = mapCopy(currentSolution);
						if(currentNeighborBuild.get(i).size() > 1) {
							currentStation = currentNeighborBuild.get(i);
							nextStation = currentNeighborBuild.get(i+1);
							int randomTask = random.nextInt(currentStation.size());
							if(randomTask == currentStation.size()) {
								randomTask --;
							}
							taskNumberCurrentStation = currentStation.get(randomTask);
							currentStation.remove(new Integer(taskNumberCurrentStation));
							nextStation.add(taskNumberCurrentStation);
							if(!neighbors.contains(new HashMap<Integer, ArrayList<Integer>>(currentNeighborBuild))) {
								if(checkSolutionValidate(currentNeighborBuild, dependencies)) {
									neighbors.add(currentNeighborBuild);
									moviment++;
									attempts++;
									neighborsFound++;
								}
							}else {
								attempts++;
							}
						}
						else {
							moviment++;
							attempts++;
						}
					}
				}
				
				attempts = 1;
				neighborsFound = 1;
				while(neighborsFound <= TOTAL_NEIGHBOR && attempts < 100) {
					moviment = 1;
					while(moviment <= MOVEMENT && attempts < 100) {
						currentNeighborBuild = mapCopy(currentSolution);
						if(currentNeighborBuild.get(i+1).size() > 1) {
							currentStation = currentNeighborBuild.get(i);
							nextStation = currentNeighborBuild.get(i+1);
							int randomTask = random.nextInt(nextStation.size());
							if(randomTask == nextStation.size()) {
								randomTask --;
							}
							taskNumberNextStation = nextStation.get(randomTask);
							nextStation.remove(new Integer(taskNumberNextStation));
							currentStation.add(taskNumberNextStation);
							if(!neighbors.contains(new HashMap<Integer, ArrayList<Integer>>(currentNeighborBuild))) {
								if(checkSolutionValidate(currentNeighborBuild, dependencies)) {
									neighbors.add(currentNeighborBuild);
									moviment++;
									attempts++;
									neighborsFound++;
								}
							}else {
								attempts++;
							}
						}
						else {
							moviment++;
							attempts++;
						}
					}
				}
			}
			return neighbors;
		}
		
		public void readArrayTasks(ArrayList<Integer> tasks, int numberOfNodes) {
			for(int i = 1; i <= numberOfNodes; i++) {
				tasks.add(i);
			}
		}
		
		
		
		
		public void buildFirstSolution(ArrayList<Integer> arraySolution, Map<Integer, ArrayList<Integer>> solution, int stationNumber, int numOfTasks, Map<Integer, ArrayList<Integer>> dependencies) {
			int tasksPerStation = numOfTasks / stationNumber;
			Random random = new Random();
			ArrayList<Integer> stationTasks;
			for(int i = 1 ; i <= stationNumber ; i++ ) {
				int numberOfTasksInStation = random.nextInt(tasksPerStation);
				if(numberOfTasksInStation == 0) {
					numberOfTasksInStation++;
				}
				if(i == 1) {
					stationTasks = new ArrayList<Integer>();
					for(int j = 0 ; j < numberOfTasksInStation - 1; j++ ) {
						stationTasks.add(arraySolution.get(j));
					}
					solution.put(i, stationTasks);
				}
				else {
					if( i < stationNumber) {
						ArrayList<Integer> lastStation = solution.get(i-1);
						int index = arraySolution.indexOf(new Integer(lastStation.get(lastStation.size() - 1)));
						stationTasks = new ArrayList<Integer>();
						for(int j = index + 1; j < index + 1  + numberOfTasksInStation  ; j++) {
							stationTasks.add(arraySolution.get(j));
						}
						solution.put(i, stationTasks);
					}
					else {
						ArrayList<Integer> lastStation = solution.get(i-1);
						int index = arraySolution.indexOf(new Integer(lastStation.get(lastStation.size() - 1)));
						stationTasks = new ArrayList<Integer>();
						for(int j = index + 1; j < numOfTasks ; j++) {
							stationTasks.add(arraySolution.get(j));
						}
						solution.put(i, stationTasks);
					}					
				}
			}
		}
		
		//Permuta as tasks entre as estações até achar uma combinação válida para solução inicial
		private void permutingUntilValidSolution(Map<Integer, ArrayList<Integer>> solution, int stationNumber, Map<Integer, ArrayList<Integer>> dependencies) {
			Map<Integer, ArrayList<Integer>> currentSolutionPermuted;
			Random random = new Random();
			int swapNumber;
			ArrayList<Integer> currentStation;
			ArrayList<Integer> nextStation;
			int taskNumberCurrentStationSwap;
			int taskNumberNextStationSwap;
			boolean valid = false;
			while(!valid) {
				currentSolutionPermuted = new HashMap<Integer, ArrayList<Integer>>();
				for(int i = 1; i < stationNumber; i++) {
					currentStation = solution.get(i);
					nextStation = solution.get(i+1);
					swapNumber = 1;
					while(swapNumber <= MOVEMENT) {
						taskNumberCurrentStationSwap = currentStation.get(random.nextInt(currentStation.size() - 1));
						taskNumberNextStationSwap = nextStation.get(random.nextInt(nextStation.size() - 1));
						currentStation.remove(new Integer(taskNumberCurrentStationSwap));
						currentStation.add(taskNumberNextStationSwap);
						nextStation.remove(new Integer(taskNumberNextStationSwap));
						nextStation.add(taskNumberCurrentStationSwap);
						swapNumber++;
					}
					currentSolutionPermuted.put(i, currentStation);
					currentSolutionPermuted.put(i+1, nextStation);
				}
				/*
				currentStation = currentSolutionPermuted.get(stationNumber);
				nextStation = currentSolutionPermuted.get(1);
				swapNumber = 1;
				while(swapNumber <= SWAP_MAX) {
					taskNumberCurrentStationSwap = currentStation.get(random.nextInt(currentStation.size() - 1));
					taskNumberNextStationSwap = nextStation.get(random.nextInt(nextStation.size() - 1));
					currentStation.remove(new Integer(taskNumberCurrentStationSwap));
					currentStation.add(taskNumberNextStationSwap);
					nextStation.remove(new Integer(taskNumberNextStationSwap));
					nextStation.add(taskNumberCurrentStationSwap);
					swapNumber++;
				}
				currentSolutionPermuted.put(stationNumber, currentStation);
				currentSolutionPermuted.put(1, nextStation);
				System.out.println(solution);
				*/
				System.out.println(currentSolutionPermuted);
				System.exit(0);
				if(checkSolutionValidate(currentSolutionPermuted, dependencies)) {
					valid = true;
					System.out.println(currentSolutionPermuted);
					
				}
			}
			System.exit(0);
		}
		
		public int readNodeList(Map<Integer, Integer> nodeList, Map<Integer, ArrayList<Integer>> dependencies){
			try(FileReader nodeListArq = new FileReader("LUTZ3.IN2")){
				BufferedReader readNodeList = new BufferedReader(nodeListArq);
				int numberTasks = Integer.parseInt(readNodeList.readLine());
				int taskCost;
				for(int index = 1; index <= numberTasks; index++) {
					taskCost = Integer.parseInt(readNodeList.readLine());
					nodeList.put(index, taskCost);
				}
				String currentDependencie;
				String[] currentDependencieSplitted;
				ArrayList<Integer> nodeDependencie;
				currentDependencie = readNodeList.readLine();
				while(!currentDependencie.equals(END_OF_FILE)) {
					currentDependencieSplitted = currentDependencie.split(",");
					nodeDependencie = dependencies.get(Integer.parseInt(currentDependencieSplitted[1]));
					if(nodeDependencie == null) {
						nodeDependencie = new ArrayList<Integer>();
						nodeDependencie.add(Integer.parseInt(currentDependencieSplitted[0]));
						dependencies.put(Integer.parseInt(currentDependencieSplitted[1]), nodeDependencie);
					}
					else {
						nodeDependencie.add(Integer.parseInt(currentDependencieSplitted[0]));
						dependencies.put(Integer.parseInt(currentDependencieSplitted[1]), nodeDependencie);
					}
					currentDependencie = readNodeList.readLine();
				}
				return numberTasks;
			}catch(Exception e) {
				return -1;
			}
		}
		
		
		
		public boolean checkSolutionValidate(Map<Integer, ArrayList<Integer>> possibleSolution, Map<Integer, ArrayList<Integer>> dependencies ) {
			List<Integer> alreadyChecked = new ArrayList<Integer>();
			for(Integer key : possibleSolution.keySet()) {
				ArrayList<Integer> nodeSet = possibleSolution.get(key);
				if(nodeSet != null) {
					for(int currentNode : nodeSet) {
						List<Integer> currentDependencies = dependencies.get(currentNode);
						if(currentDependencies == null) {
							alreadyChecked.add(currentNode);
						}
						else {
							for(int testDependencie : currentDependencies) {
								if(alreadyChecked.contains(new Integer(testDependencie)) || nodeSet.contains(new Integer(testDependencie))) {
									alreadyChecked.add(currentNode);;
								}else {
									return false;
								}
							}
						}
					}
				}
			}
			return true;
		}
}
