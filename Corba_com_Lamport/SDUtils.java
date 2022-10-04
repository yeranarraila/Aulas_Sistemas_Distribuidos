package ServerUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class SDUtils {
	private Map<Integer, Integer> repeticoes = new HashMap<Integer, Integer>(); //decisao,vezes que repete
	private Set<Integer> uniqueSet;
	private ServerDATA data;

    public SDUtils(ServerUtils.ServerDATA data) {
    	this.data = data;
    	this.uniqueSet= new HashSet<Integer>(data.getDecisoes());
    }

    public int findMaxValue(){

		for (int key : uniqueSet) {
			repeticoes.put(key ,Collections.frequency(data.getDecisoes(), key));
		}
		int maxValueInMap=(Collections.max(repeticoes.values()));  // This will return max value in the Hashmap

       	for (Entry<Integer, Integer> entry : repeticoes.entrySet()) {  // Itrate through hashmap
       	//----------------------DEBUG
       		System.out.println("\n\nDEBUG>Chave:"+entry.getKey()+"\nRepetiu-se: "+entry.getValue()+" vez(es)\n\n");
        //----------------------FIM DEBUG
            if (entry.getValue()==maxValueInMap) {
       //----------------------DEBUG
            	System.out.println("\n\nDEBUG>Enviando minha decisao final como: "+entry.getKey()+"\n\n");
       //----------------------FIM DEBUG
               return entry.getKey();
            }
        }
        return -1;
    }
}