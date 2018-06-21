import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class teste {
	
	
	Utils nodeFile = new Utils();
	Map<Integer, ArrayList<String>> solution;
	Map<String, ArrayList<String>> dependencies;
	Map<String, Integer> nodes;
	
	@Before
	public void setUp() throws Exception {
		solution = new HashMap<Integer, ArrayList<String>>();
		dependencies = new HashMap<String, ArrayList<String>>();
		ArrayList<String> nodeSet1 = new ArrayList<String>() {
			{
			add("A");
			add("C");
			}
		};
		ArrayList<String> nodeSet2 = new ArrayList<String>() {
			{
			add("B");
			add("D");
			add("E");
			}
		};
		ArrayList<String> nodeSet3 = new ArrayList<String>() {
			{
			add("F");
			add("G");
			}
		};
		ArrayList<String> nodeSet4 = new ArrayList<String>() {
			{
			add("H");
			add("I");
			}
		};
		ArrayList<String> nodeSet5 = new ArrayList<String>() {
			{
			add("J");
			add("K");
			}
		};
		
		solution.put(1, nodeSet1);
		solution.put(2, nodeSet2);
		solution.put(3, nodeSet3);
		solution.put(4, nodeSet4);
		solution.put(5, nodeSet5);
		
		nodes = nodeFile.readNodeList();
	    nodeFile.readArchList(dependencies);
		
	}

	@Test
	public void test() {
		assertEquals(true, nodeFile.checkSolutionValidate(solution, dependencies));
	}

}
