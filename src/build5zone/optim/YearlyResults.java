package build5zone.optim;

import java.io.Serializable;
import java.util.HashMap;

public class YearlyResults implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4411962727807829097L;
	
	HashMap<Integer, Double> yearlyEnergy = new HashMap<Integer, Double>();
	HashMap<Integer, Double> yearlyPrice = new HashMap<Integer, Double>();
	HashMap<Integer, Double> yearlyInconfort = new HashMap<Integer, Double>();
	HashMap<Integer, Double> yearlyColdInconfort = new HashMap<Integer, Double>();

	
	public void setresultsForZone(int zoneID, double energy,double price, double inconf,double coldinconf){
		yearlyEnergy.put(zoneID, energy);
	}
	
	
	public int getZoneCountForEnergy(){
		return yearlyEnergy.keySet().size();
	}
	
	public void setEnergy(int zoneID, double value){
		yearlyEnergy.put(zoneID,value);
	}
	
	public void setPrice(int zoneID, double value){
		yearlyPrice.put(zoneID,value);
	}
	
	public void setInconfort(int zoneID, double value){
		yearlyInconfort.put(zoneID,value);
	}
	
	public void setColdInconfort(int zoneID, double value){
		yearlyColdInconfort.put(zoneID,value);
	}
	
	public Double getEnergy(int zoneID){
		return yearlyEnergy.get(zoneID);
	}
	
	public Double getPrice(int zoneID){
		return yearlyPrice.get(zoneID);
	}
	
	public Double getInconfort(int zoneID){
		return yearlyInconfort.get(zoneID);
	}
	
	public Double getColdInconfort(int zoneID){
		return yearlyColdInconfort.get(zoneID);
	}
}
