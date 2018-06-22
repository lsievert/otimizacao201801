import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;



public class Main {

	//public static final int MAX_ITERATION = 200;
	public static final int STATION_NUM = 8;
	
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
		while(lastImproveIteration < 1000) {
			totalIteration++;
			lastImproveIteration++;
			if(tabu.size() > 10 ) {
				tabu.remove(0);
			}
			int bestNeighborIndex = 0;
			int bestNeighborValue = 0;
			boolean nextSolutionFound = false;
			neighborsValue = new ArrayList<Integer>();
			neighbors = utils.findNeighborhood(currentSolution, STATION_NUM, numOfTasks, dependencies);
			for(int neighbor = 0 ; neighbor < neighbors.size() ; neighbor ++) {
				neighborsValue.add(utils.evaluateSolution(neighbors.get(neighbor), nodeList));
			}
			while(!nextSolutionFound) {
					bestNeighborValue = Collections.min(neighborsValue);
					bestNeighborIndex = neighborsValue.indexOf(new Integer(bestNeighborValue));
					System.out.println(neighborsValue);
					System.out.println(tabu.size());
					if(tabu.contains(neighbors.get(bestNeighborIndex))) {
						neighbors.remove(neighbors.get(bestNeighborIndex));
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
			System.out.println(currentSolution);
		}
		System.out.println(bestFoundValue);
	}
}
