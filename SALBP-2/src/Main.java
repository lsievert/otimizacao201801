import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class Main {

	public static final int MAX_ITERATION = 200;
	public static final int STATION_NUM = 5;
	
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
		tabu.add(currentSolution);
		ArrayList<Integer> neighborsValue;
		while(lastImproveIteration < 500) {
			totalIteration++;
			lastImproveIteration++;
			if(totalIteration % 10 == 0) {
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
					System.out.println("indice melhor vizinho" + bestNeighborIndex );
					System.exit(0);
					if(tabu.contains(new HashMap<Integer, ArrayList<Integer>>(neighbors.get(bestNeighborIndex)))) {
						neighbors.remove(new HashMap<Integer, ArrayList<Integer>>(neighbors.get(bestNeighborIndex)));
					}
					else {
						if(bestNeighborValue < bestFoundValue) {
							bestFoundValue = bestNeighborValue;
							bestSolution = neighbors.get(bestNeighborIndex);
							lastImproveIteration = 0;
							tabu.add(bestSolution);
						}
					currentSolution = neighbors.get(bestNeighborIndex);
					tabu.add(currentSolution);
					nextSolutionFound = true;
				}
			}
			System.out.println(currentSolution);
		}
		System.out.println(bestFoundValue);
	}
}
