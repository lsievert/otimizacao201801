package otimim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

	//public static final int MAX_ITERATION = 200;
		public static final int STATION_NUM = 20;
		
		public static void main(String[] args) {
			
			Utils utils = new Utils();
			Map<Integer, Integer> nodeList = new HashMap<Integer, Integer>();
			Map<Integer, ArrayList<Integer>> dependencies = new HashMap<Integer, ArrayList<Integer>>();
			Map<Integer, ArrayList<Integer>> currentSolution = new HashMap<Integer, ArrayList<Integer>>();
			ArrayList<Integer> arrayTasks = new ArrayList<Integer>();
			
			
			//Le os arquivos e popula com as dependencias, criação da primeira solução aleatoria
			int numOfTasks = utils.readNodeList(nodeList, dependencies);
			utils.readArrayTasks(arrayTasks, numOfTasks);
			utils.buildFirstSolution(arrayTasks, currentSolution, STATION_NUM, numOfTasks, dependencies);
			
			int totalIteration = 0;
			int bestFoundValue;
			int lastImproveIteration = 0;
			Map<Integer, ArrayList<Integer>> bestSolution;
			ArrayList<Map<Integer, ArrayList<Integer>>> tabu = new ArrayList<Map<Integer, ArrayList<Integer>>>();
			ArrayList<Map<Integer, ArrayList<Integer>>> neighbors;
			
			bestFoundValue = utils.evaluateSolution(currentSolution, nodeList);
			tabu.add(utils.mapCopy(currentSolution));
			ArrayList<Integer> neighborsValue;
			ArrayList<Integer> tabusValue;
			while(lastImproveIteration < 200) {
				totalIteration++;
				lastImproveIteration++;
				if(tabu.size() > 11 ) {
					tabu.remove(0);
				}
				int bestNeighborIndex = 0;
				int bestNeighborValue = 0;
				int bestTabuValue;
				int bestTabuIndex;
				boolean nextSolutionFound = false;
				neighborsValue = new ArrayList<Integer>();
				tabusValue = new ArrayList<Integer>();
				//Descobre valores de solucao dentro da lista tabu, pra comparar depois se é menor que todos os vizinhos encontrados
				for(int tabuSolution = 0; tabuSolution < tabu.size() ; tabuSolution ++) {
					tabusValue.add(utils.evaluateSolution(tabu.get(tabuSolution), nodeList));
				}
				bestTabuValue = Collections.min(tabusValue);
				bestTabuIndex = tabusValue.indexOf(new Integer(bestTabuValue));
				neighbors = utils.findNeighborhood(currentSolution, STATION_NUM, numOfTasks, dependencies);
				for(int neighbor = 0 ; neighbor < neighbors.size() ; neighbor ++) {
					neighborsValue.add(utils.evaluateSolution(neighbors.get(neighbor), nodeList));
				}
				while(!nextSolutionFound) {
						System.out.println(neighborsValue);
						bestNeighborValue = Collections.min(neighborsValue);
						bestNeighborIndex = neighborsValue.indexOf(new Integer(bestNeighborValue));
						System.out.println(bestNeighborIndex);
						System.out.println(neighbors.get(bestNeighborIndex));
						if(bestNeighborValue > bestTabuValue) {
							currentSolution = utils.mapCopy(tabu.get(bestTabuIndex));
							tabu.remove(tabu.get(bestTabuIndex));
						}
						else {
							//Tabu já contem a solução que seria utilizada, solucao descartada, procura a proxima no loop
							if(tabu.contains(neighbors.get(bestNeighborIndex))) {
								neighbors.remove(bestNeighborIndex);
								neighborsValue.remove(bestNeighborIndex);
							}
							else {
								if(bestNeighborValue < bestFoundValue) {
									bestFoundValue = bestNeighborValue;
									bestSolution = utils.mapCopy(neighbors.get(bestNeighborIndex));
									currentSolution = utils.mapCopy(bestSolution);
									lastImproveIteration = 0;
								}
							currentSolution = utils.mapCopy(neighbors.get(bestNeighborIndex));
							tabu.add(utils.mapCopy(neighbors.get(bestNeighborIndex)));
							nextSolutionFound = true;
						}
					}
				}
				System.out.println(currentSolution);
			}
			System.out.println(bestFoundValue);
		}
	}

