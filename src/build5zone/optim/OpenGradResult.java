package build5zone.optim;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.JFrame;

import panel.MultipleXYTrace;

import build5zone.Main;
import build5zone.optim.YearlySimulationResults.OptimOption;

public class OpenGradResult {

	public static void main(String[] args) {
		double [] nightOrders = new double [8];
		for(int i = 0 ; i < 53 ; i++){
			nightOrders[i] = 17;
		}
		//SimParameters initial = new SimParameters(nightOrders);
		//YearlyResults  initialSituationYearlyResult =ysr.evaluateOrders(initial,23d);


		YearlySimulationResults annualzoneResult = new YearlySimulationResults("AllSimuDayly.results");

		MultipleXYTrace price_optimal = new MultipleXYTrace("Price Optimal", "week", "nightTemp");
		JFrame fpriceO = new JFrame();
		fpriceO.add(price_optimal);
		fpriceO.pack();
		fpriceO.setVisible(true);
		
		MultipleXYTrace energy_optimal = new MultipleXYTrace("Ennergy Optimal", "week", "nightTemp");
		JFrame fenerO = new JFrame();
		fenerO.add(energy_optimal);
		fenerO.pack();
		fenerO.setVisible(true);
		
		
		MultipleXYTrace crossPrice = new MultipleXYTrace("weektemp vs nightTemp", "week Temp", "nightTemp");
		JFrame fcrosPrice = new JFrame();
		fcrosPrice.add(crossPrice);
		fcrosPrice.pack();
		fcrosPrice.setVisible(true);
		
		TreeMap<Double, Double> weeklyTemp = Main.collectWeeklyOutsideTemp("C:\\Trnsys17\\MyProjects\\5ZonesModel_20130301\\");
		System.out.println("weeklyTempsize"+weeklyTemp.size());
		
		for(int zoneID = 1 ; zoneID <= 5 ; zoneID++ ){
			Entry<SimParameters,YearlyResults>  bestPriceForZone = annualzoneResult.getBestParameters(zoneID, OptimOption.OPT_PRICE);
			Entry<SimParameters,YearlyResults>  bestEnergyForZone = annualzoneResult.getBestParameters(zoneID, OptimOption.OPT_ENERGY);
			
			SimParameters params = bestPriceForZone.getKey();
			for (int i = 0; i < params.params.length; i++) {
				double temp = params.params[i];
				Double weekTemp = weeklyTemp.get(new Double(i+1));
				price_optimal.addValue(new Double(i), temp, "zone"+zoneID, 0);
				
				if(weekTemp!=null){
					crossPrice.addValue(weekTemp, temp, "zone"+zoneID, 0);
				}
				
			}
			
			params = bestEnergyForZone.getKey();
			for (int i = 0; i < params.params.length; i++) {
				double temp = params.params[i];
				
				energy_optimal.addValue(new Double(i), temp, "zone"+zoneID, 0);
			}
		}
	}

}
