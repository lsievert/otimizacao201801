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
			
			
			//Le os arquivos e popula com as dependencias, cria��o da primeira solu��o aleatoria
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
			while(lastImproveIteration < 5000) {
				totalIteration++;
				lastImproveIteration++;
				if(tabu.size() > 100 ) {
					tabu.remove(0);
				}
				int bestNeighborIndex = 0;
				int bestNeighborValue = 0;
				int bestTabuValue;
				boolean nextSolutionFound = false;
				neighborsValue = new ArrayList<Integer>();
				tabusValue = new ArrayList<Integer>();
				for(int tabuSolution = 0; tabuSolution < tabu.size() ; tabuSolution ++) {
					tabusValue.add(utils.evaluateSolution(tabu.get(tabuSolution), nodeList));
				}
				bestTabuValue = Collections.min(tabusValue);
				neighbors = utils.findNeighborhood(currentSolution, STATION_NUM, numOfTasks, dependencies);
				System.out.println(neighbors);
				for(int neighbor = 0 ; neighbor < neighbors.size() ; neighbor ++) {
					neighborsValue.add(utils.evaluateSolution(neighbors.get(neighbor), nodeList));
				}
				while(!nextSolutionFound) {
						
						bestNeighborValue = Collections.min(neighborsValue);
						bestNeighborIndex = neighborsValue.indexOf(new Integer(bestNeighborValue));
						System.out.println(bestFoundValue);
						System.out.println(bestNeighborIndex);
						System.out.println(neighbors.get(bestNeighborIndex));
						if(bestNeighborValue > bestTabuValue) {
							currentSolution = utils.mapCopy(tabu.get(tabu.indexOf(new Integer(bestTabuValue))));
							tabu.remove(tabu.get(tabu.indexOf(new Integer(bestTabuValue))));
						}
						else {
							if(tabu.contains(neighbors.get(bestNeighborIndex))) {
								neighbors.remove(bestNeighborIndex);
								neighborsValue.remove(bestNeighborIndex);
							}
							else {
								if(bestNeighborValue < bestFoundValue) {
									bestFoundValue = bestNeighborValue;
									bestSolution = utils.mapCopy(neighbors.get(bestNeighborIndex));
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

