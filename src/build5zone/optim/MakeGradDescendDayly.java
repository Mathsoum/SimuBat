package build5zone.optim;

import java.io.IOException;

public class MakeGradDescendDayly {
	
	public static String fileName = "AllSimuDayly.results";
	
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
	
		
		double [] nightOrders = new double [8];
		for(int i = 0 ; i < nightOrders.length ; i++){
			nightOrders[i] = 17;
		}
		YearlySimulationResults dataBase = new YearlySimulationResults(fileName);
		//dataBase.removeAllData();
		
		
		double dayTimeTemp = 21;
		
		
		SimParameters initial = new SimParameters(nightOrders);
		YearlyResults  initialSituationYearlyResult = new YearlySimulationResults(fileName).
				evaluateOrders(initial,dayTimeTemp);

		YearlyResults[] resultbyzone = new YearlyResults[10];
		for(int zoneID = 1 ; zoneID <= 1 ; zoneID++ ){

			YearlyResults zoneResult = dataBase.graddescend(initial,true, zoneID, 0, 15, 23,dayTimeTemp);
			resultbyzone[zoneID] = zoneResult;
		}

		for(int zoneID = 1 ; zoneID <= 1 ; zoneID++ ){
			System.out.println(" ==== ZONE "+zoneID );
			double initialYearPrice = initialSituationYearlyResult.getPrice(zoneID);
			double optimYearly = resultbyzone[zoneID].getPrice(zoneID);
			System.out.println(String.format("Price         -> %.3f %%", ((optimYearly-initialYearPrice)/initialYearPrice)*100) );

			double initialYearEnergy = initialSituationYearlyResult.getEnergy(zoneID);
			double optimYearlyEnergy = resultbyzone[zoneID].getEnergy(zoneID);
			System.out.println(String.format("Energy        -> %.3f %%", ((optimYearlyEnergy-initialYearEnergy)/initialYearEnergy)*100) );

			double initialYearConf = initialSituationYearlyResult.getInconfort(zoneID);
			double optimYearlyConf = resultbyzone[zoneID].getInconfort(zoneID);
			System.out.println(String.format("Inconfort     -> %.3f %%", ((optimYearlyConf-initialYearConf)/initialYearConf)*100) );

			double initialYearColdInconf = initialSituationYearlyResult.getColdInconfort(zoneID);
			double optimYearlyColdInconf = resultbyzone[zoneID].getColdInconfort(zoneID);
			System.out.println(String.format("ColdInconfort -> %.3f %%", ((optimYearlyColdInconf-initialYearColdInconf)/initialYearColdInconf)*100) );

		}
		

	}
}
