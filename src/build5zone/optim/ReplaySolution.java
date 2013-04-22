package build5zone.optim;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map.Entry;

import tools.WriteListToFile;
import build5zone.GenerateNightTimeOrderFile;
import build5zone.optim.YearlySimulationResults.OptimOption;

public class ReplaySolution {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		YearlySimulationResults annualzoneResult = new YearlySimulationResults("AllSimuDayly.results");
		
		System.out.println("size : "+annualzoneResult.size());
		
		Entry<SimParameters, YearlyResults> best = annualzoneResult.getBestParameters(1, OptimOption.OPT_PRICE);
		
		LinkedList<Double> solution = GenerateNightTimeOrderFile.generateWeekelyTempControl(best.getKey().params, 23d, 7, 18);
		
		//write it
		GenerateNightTimeOrderFile.writeList(solution);

	}

}
