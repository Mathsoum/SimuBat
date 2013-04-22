package build5zone.optim;

import java.io.IOException;

public class MakeWeeklyGradDescent {
	
	public static String fileName = "gradResultsAllSimu.hm_simparam_double";
			
			
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		double [] nightOrders = new double [53];
		for(int i = 0 ; i < 53 ; i++){
			nightOrders[i] = 17;
		}

		SimParameters initial = new SimParameters(nightOrders);
		YearlyResults  initialSituationYearlyResult = new YearlySimulationResults(fileName).evaluateOrders(initial);

		YearlyResults[] resultbyzone = new YearlyResults[10];
		for(int zoneID = 1 ; zoneID <= 3 ; zoneID++ ){

			YearlyResults zoneResult = new YearlySimulationResults(fileName).graddescend(initial,true, zoneID, 0, 15, 23);
			resultbyzone[zoneID] = zoneResult;
		}

		for(int zoneID = 1 ; zoneID <= 3 ; zoneID++ ){
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
