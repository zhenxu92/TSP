import java.util.*;
public class TSPManager {
	
	// data field
	int[][] listOfEdges = new int[20][20];	// list of edges, which should be a square matrix
	List<Integer> optimalPath = new ArrayList<Integer>(); // best path
	List<Integer> parenta = new ArrayList<Integer>();
	List<Integer> parentb = new ArrayList<Integer>();
	int size = listOfEdges.length;	// the size of the edge matrix size
	int minColor = 20;
	
	
	public TSPManager(int[][] list) {
		this.listOfEdges = list;
		for (int i = 0; i < size; i++) {
			parenta.add(i);
			parentb.add(i);
			optimalPath.add(i);
		}
		java.util.Collections.shuffle(parenta);
		java.util.Collections.shuffle(parentb);
	}
	
	
	// crossover
	// random select some numbers and do the crossover
	// produce the two offsprings and then let the two
	// offsprings be the new parents
	public void crossover () {
		int startpos = (int) (Math.random() * parenta.size());
		int endpos = (int) (Math.random() * parenta.size());
		if (startpos > endpos) {
			int temp = startpos;
			startpos = endpos;
			endpos = temp;
		}
		// Debug parents
		System.out.println("The parent size: a - " + parenta.size() + "b - " + parentb.size() + "before crossover");
		
		// initializing offspring list
		List<Integer> osa = new ArrayList<Integer>();
		List<Integer> osb = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            osa.add(0);
            osb.add(0);
        }
        // mutation
        for (int i = 0; i <= endpos - startpos; i++) {
            osa.add(startpos + i, parenta.get(endpos - i)); // 3 inherit from 1
            osb.add(startpos + i, parentb.get(endpos - i)); // 4 inherit from 2
        }
        
        // cross osa with parentb
        // cross osab with parenta
        // delete elements
        for (int i = startpos; i <= endpos; i++) {
            parenta.remove(osb.get(i));
            parentb.remove(osa.get(i));
        }
        
        // push
		Stack<Integer> s1 = new Stack<Integer>();
		Stack<Integer> s2 = new Stack<Integer>();
        
		for (int i = 0; i < parenta.size(); i++) {
			s1.push(parenta.get(i));
			s2.push(parentb.get(i));
		}

        
        for (int i = 0; i < startpos; i++) {
            osa.set(i, s2.pop());  // cross with 2
            osb.set(i, s1.pop());  // cross with 1
        }
        
        //hardcode 10 here
        for (int i = endpos + 1; i < size && !s1.isEmpty() && !s2.isEmpty(); i++) {
            osa.set(i, s2.pop());
            osb.set(i, s1.pop());
        }
        // Debug
        // System.out.println("The size of parents and offspring" + parenta.size() + " " + parentb.size() + " " + osa.size() + " " + osb.size());
        // TODO: trim osa, osb down to 10 as size 
        if (osa.size() > size) {
        	osa = osa.subList(0, size);
        }
        if (osb.size() > 10) {
        	osb = osb.subList(0, size);

        }
        
        // TODO: update cost and reset optimal path
        Object[] arr = new Object[size];
        // List<List<Integer>> arr2 = new ArrayList<List<Integer>>();
        arr[0] = parenta;
        arr[1] = parentb;
        arr[2] = osa;
        arr[3] = osb;
        System.out.println("The offspring size" + osa.size());
        // System.out.println(parenta.size());
        int cost = this.costCalc(optimalPath);
        for (int i = 2; i < 3; i++) {
        	if(this.costCalc((List<Integer>) arr[i]) < cost) {
        		cost = this.costCalc((List<Integer>) arr[i]);
        		optimalPath = (List<Integer>) arr[i];
        	}
        }
        // Print out the current optimal cost
        System.out.println("Currently the optimal cost is: " + cost);
        
        // calc color
        for (int i = 2; i < 3; i++) {
        	if (this.calcColor((List<Integer>)arr[i]) < minColor) {
        		minColor = this.calcColor((List<Integer>)arr[i]);
        		
        	}
        }
        System.out.println("minimum coloring: " + minColor);
        // TODO: Assign os to parent
        parenta = osa;
        parentb = osb;
		
	}
	
	// compare the offsprings with the parents, if cost
	// is smaller, promote it and store as optimal path
	public void comparator () {
		
	}
	
	
	public int costCalc(List<Integer> list) {
		int cost = 0;
		// System.out.println(list.size());
		for (int i = 0; i < list.size() - 1; i++) {
			if (listOfEdges[list.get(i)][list.get(i + 1)] == 0) {
				cost += 200;	// add maximum cost if there is no edge
			} else {
				cost = cost + listOfEdges[list.get(i)][list.get(i + 1)]; 	
			}
		}
		return cost;
	}
	
	public int calcColor (List<Integer> list) {
		int num = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != 0) {
				num++;
			}
		}
		return num;
	}
	
	
	
}