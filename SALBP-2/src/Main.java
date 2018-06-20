import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class Main {

	public static final int MAX_ITERATION = 200;
	public static final int STATION_NUM = 3;
	
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
		
		int currentIteration = 0;
		int bestFoundValue;
		Map<Integer, ArrayList<Integer>> bestSolution;
		int found;
		ArrayList<Map<Integer, ArrayList<Integer>>> tabu = new ArrayList<Map<Integer, ArrayList<Integer>>>();
		ArrayList<Integer> solutionTabuValue;
		ArrayList<Map<Integer, ArrayList<Integer>>> neighbors;
		bestFoundValue = utils.evaluateSolution(currentSolution, nodeList);
		tabu.add(currentSolution);
		while(currentIteration < MAX_ITERATION) {
			neighbors = utils.findNeighborhood(currentSolution, STATION_NUM, numOfTasks, dependencies);
		}
	}
}
