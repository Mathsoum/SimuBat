package build5zone.optim;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jfree.util.ArrayUtilities;
import org.mockito.internal.util.ArrayUtils;

import build5zone.GenerateNightTimeOrderFile;
import build5zone.Main;
import build5zone.SetOfMap;

import tools.ObjectWriteAndRead;
import tools.RandomTool;
import tools.RunTrnSys;
import tools.WriteListToFile;

public class YearlySimulationResults {

	public static String basePathwithending = "C:\\Trnsys17\\MyProjects\\5ZonesModel_20130301\\";
	public String fileName = "fileNotNAmed.file";
	
	HashMap< SimParameters, YearlyResults> allResults = null;

	public void removeAllData(){
		allResults.clear();
	}

	public YearlySimulationResults (String serializedfileName ){
		
		fileName = serializedfileName;
		try {
			allResults = (HashMap< SimParameters, YearlyResults>) ObjectWriteAndRead.loadFromFile(fileName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		if(allResults == null){
			System.out.println("error loading file");
			allResults = new HashMap<SimParameters, YearlyResults>();
		}else{
			System.out.println("dataFileOK");
		}
	}

	public int size(){
		return allResults.size();
	}

	public enum OptimOption {
		OPT_PRICE,
		OPT_ENERGY
	} ;

	public Entry<SimParameters,YearlyResults> getBestParameters(int zoneID,OptimOption optimOption){
		Entry<SimParameters,YearlyResults> lowestFound = null;
		for (Entry<SimParameters,YearlyResults> anEntry : allResults.entrySet()) {
			if(lowestFound == null){
				lowestFound = anEntry;
			}else{
				switch (optimOption) {
				case OPT_ENERGY:

					if(anEntry.getValue().getEnergy(zoneID) < lowestFound.getValue().getEnergy(zoneID)){
						lowestFound = anEntry;
					}
					break;
				case OPT_PRICE:
					if(anEntry.getValue().getPrice(zoneID) < lowestFound.getValue().getPrice(zoneID) )  {
						lowestFound = anEntry;
					}
					break;

				default:
					break;
				}
			}
		}

		return lowestFound;
	}
	public YearlyResults evaluateOrders (SimParameters params,double dayTimeTemp) {

		if(!allResults.containsKey(params)){

			//generate the tempFile
			LinkedList<Double> tempControl = GenerateNightTimeOrderFile.generateWeekelyTempControl(params.params, dayTimeTemp, 7, 18);

			//write it
			try {
				WriteListToFile.writeToFile(basePathwithending+"Profils\\TempConsigne.txt","nightTempOrder", tempControl);
			} catch (IOException e) {
				e.printStackTrace();
			}

			//run trnsys
			String dckfilefullpath = basePathwithending+"5ZonesModel.dck";
			RunTrnSys.runTrnSys(dckfilefullpath,false);

			//openAnnualResult
			YearlyResults yearlyresults = Main.collectYearlyResults(basePathwithending);
			allResults.put(params, yearlyresults);


			try {
				ObjectWriteAndRead.saveToFile(fileName, allResults);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("I saved a test ! ");
		}

		return allResults.get(params);
	}



	public YearlyResults graddescend(SimParameters initial,
			boolean optimOnPrice,
			int zoneID,
			int startingPoint,
			double minNight,double maxNight,
			double dayTImeTemp
			){
		System.out.println("graddescend at point "+startingPoint +" for zone "+ zoneID);

		SimParameters current = initial.clone();

		SetOfMap tryedHere = new SetOfMap();

		for(double d = minNight ; d <= maxNight ; d+=0.5){

			SimParameters next = current.clone();
			next.getParams()[startingPoint] = d;

			YearlyResults nextResult = evaluateOrders(next,dayTImeTemp);

			if(optimOnPrice){
				tryedHere.addData("ll", d, nextResult.getPrice(zoneID));
				System.out.println("nightTime : "+d + " -> "+nextResult.getPrice(zoneID));
			}else{
				tryedHere.addData("ll", d, nextResult.getEnergy(zoneID));
				System.out.println("nightTime : "+d + " -> "+nextResult.getEnergy(zoneID));
			}

		}

		TreeMap<Double, Double> minimums = tryedHere.getMinimums("ll");
		
		/*********
		 * BIAS
		 */
		//double nightTimeoptimal = minimums.firstKey();
		double nightTimeoptimal = minimums.lastKey();

		current.getParams()[startingPoint] = nightTimeoptimal;

		YearlyResults optimalHere = evaluateOrders(current,dayTImeTemp);

		System.out.println("current optimal yearly data :");
		System.out.println("energy"+optimalHere.getEnergy(zoneID));
		System.out.println("price"+optimalHere.getPrice(zoneID));

		if(startingPoint< initial.getParams().length-1){
			return graddescend(current,optimOnPrice,zoneID, startingPoint+1, minNight, maxNight,dayTImeTemp);
		}else{
			return optimalHere;
		}

	}
}
