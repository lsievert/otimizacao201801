package otimim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
		private static final int MOVEMENT = 3;
		
		/*
		 * Gera valores randomicos que s�o utilizadas para permutar as tarefas na fun��o de encontra novos vizinhos
		 */
	   
		private ArrayList<Integer> randValues(int quantityOfNumbers, ArrayList<Integer> station){
	    	Random random = new Random();
	    	int randomNumber;
	    	ArrayList<Integer> numbers = new ArrayList<Integer>();
	    	while(numbers.size() != quantityOfNumbers) {
	    		randomNumber = random.nextInt(station.size());
	    		if(!numbers.contains(new Integer(randomNumber))) {
	    			numbers.add(randomNumber);
	    		}
	    	}
	    	return numbers;
	    }
		
		/* 
		 * Fun��ozinha barbada para copiar uma estrutra de MAP
		 */
	    
	    public Map<Integer, ArrayList<Integer>> mapCopy(Map<Integer, ArrayList<Integer>> original){
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
		
		/*
		 * Simplesmente avalia uma solu��o e devolve o maior valor de esta��o existente daquela solu��o recebida
		 * Utilizada depois para compara��o entre os vizinhos para descobrir qual � o melhor
		 */
		
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
		
		
		
		
		
		/* A ideia dessa funcao � tomar as esta�oes par a par e fazer permutacoes entre as tarefas, que sao escolhidas de forma aleatoria
		 * as permuta��es funcionam de maneira simples. A vari�vel MOVEMENT guarda o numero m�ximo de tarefas que podem ser alocadas a outra esta��o
		 * exemplo: a estacao 1 aloca uma de suas tarefas para a esta��o dois, o que d� origem a um vizinho
		 * a  esta��o dois por sua vez tb aloca uma de suas tarefas a esta��o 1, o que da origem a outro vizinho, e assim sucessivamente, para duas, tres, n tarefas, 
		 * dependendo da vari�vel MOVEMENT.
		 */
		
		public ArrayList<Map<Integer, ArrayList<Integer>>> findNeighborhood(Map<Integer, ArrayList<Integer>> currentSolution, int stationNumber, int numberOfTasks, Map<Integer, ArrayList<Integer>>  dependencies ){
			ArrayList<Map<Integer, ArrayList<Integer>>> neighbors = new ArrayList<Map<Integer, ArrayList<Integer>>>();
			Map<Integer, ArrayList<Integer>> currentNeighborBuild;
			int attempts;
			int taskNumberCurrentStation;
			int taskNumberNextStation;
			ArrayList<Integer> currentStation;
			ArrayList<Integer> nextStation;
			
			for(int i = 1; i <= stationNumber - 1; i++) {
				for(int j = 1 ; j<= MOVEMENT ; j++) {
					
					/*
					 * Tomadas Par a Par as esta��es, essa seria a esta��o da esquerda, que manda tarefas suas para a esta��o da direita
					 */
					attempts = 1;
					while(attempts != 100) {
						currentNeighborBuild = mapCopy(currentSolution);
						if(currentNeighborBuild.get(i).size() <= j) {
							attempts = 100;
						}
						else {
							currentStation = currentNeighborBuild.get(i);
							nextStation = currentNeighborBuild.get(i+1);
							ArrayList<Integer> randomTasks = randValues(j, currentStation);
							Collections.sort(randomTasks);
							for(int randomTask = randomTasks.size() ; randomTask > 0 ; randomTask--) {
								taskNumberCurrentStation = currentStation.get(randomTasks.get(randomTask - 1));
								currentStation.remove(new Integer(taskNumberCurrentStation));
								nextStation.add(taskNumberCurrentStation);
								Collections.sort(nextStation);
								Collections.sort(currentStation);
							}
							if(!neighbors.contains(currentNeighborBuild)) {
								if(checkSolutionValidate(currentNeighborBuild, dependencies)) {
									neighbors.add(mapCopy(currentNeighborBuild));
									attempts++;
								}
								else {
									attempts++;
								}
							}
							else {
								attempts++;
							}
						}
					}
					
					/*
					 * Por sua vez, essa manda as tarefas da esta��o da direita para a esta��o mais a esquerda
					 */

					attempts = 1;
					while(attempts != 100) {
						currentNeighborBuild = mapCopy(currentSolution);
						if(currentNeighborBuild.get(i+1).size() <= j) {
							attempts = 100;
						}
						else {
							currentStation = currentNeighborBuild.get(i);
							nextStation = currentNeighborBuild.get(i+1);
							ArrayList<Integer> randomTasks = randValues(j, nextStation);
							Collections.sort(randomTasks);
							for(int randomTask = randomTasks.size() ; randomTask > 0 ; randomTask--) {
								taskNumberNextStation = nextStation.get(randomTasks.get(randomTask - 1));
								nextStation.remove(new Integer(taskNumberNextStation));
								currentStation.add(taskNumberNextStation);
								Collections.sort(currentStation);
								Collections.sort(nextStation);
							}
							if(!neighbors.contains(currentNeighborBuild)) {
								if(checkSolutionValidate(currentNeighborBuild, dependencies)) {
									neighbors.add(mapCopy(currentNeighborBuild));
									attempts++;
								}
								else {
									attempts++;
								}
							}
							else {
								attempts++;
							}
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
		
		
		/*
		 * Cria uma primeira solu��o valida, simplesmente balanceando o numero de n�s entre as esta��es
		 */
		
		public void buildFirstSolution(ArrayList<Integer> arraySolution, Map<Integer, ArrayList<Integer>> solution, int stationNumber, int numOfTasks, Map<Integer, ArrayList<Integer>> dependencies) {
			int tasksPerStation = numOfTasks / stationNumber;
			ArrayList<Integer> stationTasks;
			for(int i = 1 ; i <= stationNumber ; i++ ) {
				if(i == 1) {
					stationTasks = new ArrayList<Integer>();
					for(int j = 0 ; j < tasksPerStation ; j++ ) {
						stationTasks.add(arraySolution.get(j));
					}
					solution.put(i, stationTasks);
				}
				else {
					if( i < stationNumber) {
						ArrayList<Integer> lastStation = solution.get(i-1);
						int index = arraySolution.indexOf(new Integer(lastStation.get(lastStation.size() - 1)));
						stationTasks = new ArrayList<Integer>();
						for(int j = index + 1; j < index + 1  + tasksPerStation  ; j++) {
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
		
		
		
		
		
		
		public int readNodeList(Map<Integer, Integer> nodeList, Map<Integer, ArrayList<Integer>> dependencies, String fileName){
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
		
		
		/*
		 * Verifica se uma dada solu��o respeita a precedencia das tarefas que est�o guardadas no array de dependecias
		 */
		
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
								if(!alreadyChecked.contains(new Integer(testDependencie)) && !nodeSet.contains(new Integer(testDependencie))) {
									return false;
								}
							}
							alreadyChecked.add(currentNode);
						}
					}
				}
			}
			return true;
		}
	}
