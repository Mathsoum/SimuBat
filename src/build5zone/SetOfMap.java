package build5zone;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class SetOfMap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -29153085420052442L;
	Map<String,TreeMap<Double, Double>> setOfMap = new HashMap<String,TreeMap<Double, Double>>();

	public SetOfMap() {
		super();
	}

	public void addData(String mapID,double nightTimeTemp,double conso){
		if(!setOfMap.containsKey(mapID)){
			setOfMap.put(mapID, new TreeMap<Double,Double>());
		}
		setOfMap.get(mapID).put(nightTimeTemp, conso);
	}

	public TreeMap<Double, Double> getMap(String mapID){
		return setOfMap.get(mapID);
	}

	public Double getMinimumValue(String mapID){
		TreeMap<Double, Double> origin = this.getMap(mapID);
		Double minimumValue = null;

		for(Entry<Double,Double> e : origin.entrySet()){
			if(minimumValue==null || e.getValue()< minimumValue){
				minimumValue = e.getValue();
			}
		}

		return minimumValue;
	}

	public TreeMap<Double, Double> getMinimums(String mapID){
		TreeMap<Double, Double> minimums = new TreeMap<Double, Double>();
		TreeMap<Double, Double> origin = this.getMap(mapID);
		Double minimumValue = getMinimumValue(mapID);
		
		for(Entry<Double,Double> e : origin.entrySet()){
			if(e.getValue()<= minimumValue ){
				minimums.put(e.getKey(), e.getValue());
			}
		}
		return minimums;
	}
	
	public Set<String> getKeys(){
		return setOfMap.keySet();
	}

	public static SetOfMap loadFromFile(String fileName) throws IOException, ClassNotFoundException{
		FileInputStream fos = new FileInputStream(fileName);
		ObjectInputStream oos = new ObjectInputStream(fos);

		SetOfMap setofMap = (SetOfMap) oos.readObject();
		
		return setofMap;
	}
	
	public void saveToFile (String fileName) throws IOException{
		FileOutputStream fos = new FileOutputStream("dataBYtempOrder.dat");
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(this);
		oos.flush();
		oos.close();
	}

}
