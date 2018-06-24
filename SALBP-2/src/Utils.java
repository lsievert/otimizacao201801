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
		 * Gera valores randomicos que são utilizadas para permutar as tarefas na função de encontra novos vizinhos
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
		 * Funçãozinha barbada para copiar uma estrutra de MAP
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
		 * Simplesmente avalia uma solução e devolve o maior valor de estação existente daquela solução recebida
		 * Utilizada depois para comparação entre os vizinhos para descobrir qual é o melhor
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
		
		
		
		
		
		/* A ideia dessa funcao é tomar as estaçoes par a par e fazer permutacoes entre as tarefas, que sao escolhidas de forma aleatoria
		 * as permutações funcionam de maneira simples. A variável MOVEMENT guarda o numero máximo de tarefas que podem ser alocadas a outra estação
		 * exemplo: a estacao 1 aloca uma de suas tarefas para a estação dois, o que dá origem a um vizinho
		 * a  estação dois por sua vez tb aloca uma de suas tarefas a estação 1, o que da origem a outro vizinho, e assim sucessivamente, para duas, tres, n tarefas, 
		 * dependendo da variável MOVEMENT.
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
					 * Tomadas Par a Par as estações, essa seria a estação da esquerda, que manda tarefas suas para a estação da direita
					 */
					attempts = 1;
					while(attempts != 200) {
						currentNeighborBuild = mapCopy(currentSolution);
						if(currentNeighborBuild.get(i).size() <= j) {
							attempts = 200;
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
					 * Por sua vez, essa manda as tarefas da estação da direita para a estação mais a esquerda
					 */

					attempts = 1;
					while(attempts != 200) {
						currentNeighborBuild = mapCopy(currentSolution);
						if(currentNeighborBuild.get(i+1).size() <= j) {
							attempts = 200;
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
		 * Cria uma primeira solução valida, simplesmente balanceando o numero de nós entre as estações
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
		
		
		
		
		
		
		public int readNodeList(Map<Integer, Integer> nodeList, Map<Integer, ArrayList<Integer>> dependencies){
			try(FileReader nodeListArq = new FileReader("WEE-MAG.IN2")){
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
		 * Verifica se uma dada solução respeita a precedencia das tarefas que estão guardadas no array de dependecias
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
