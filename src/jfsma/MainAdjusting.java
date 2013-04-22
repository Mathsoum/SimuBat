package jfsma;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


import org.jfree.util.ArrayUtilities;
import org.mockito.internal.util.ArrayUtils;

import tools.OpenTrnsysoutput;
import tools.RandomTool;
import tools.ReplaceInTextFile;
import tools.RunTrnSys;


public class MainAdjusting {

	public static class TurnSolution {
		Double bestSavingsOnThisRun = null;
		Double[] bestParametersOnThisRun = null;
		
		public TurnSolution(Double bestSavingsOnThisRun,
				Double[] bestParametersOnThisRun) {
			super();
			this.bestSavingsOnThisRun = bestSavingsOnThisRun;
			this.bestParametersOnThisRun = bestParametersOnThisRun;
		}
		
	}

	public static class CanNotFindBetterSolutionFromThisPoint extends Exception {
		private static final long serialVersionUID = 4380091002477870998L;

	}

	/**
	 * 
	 * Ca c'est les paramètres d'origine de la simulation, je tourne 
	 * autour afin de creuser le minimum.
	 * Solution at : 12.0°, saving : -1.0265 % 
solution/25094328_building1.b17 


     6000.0 
     9.0 
     9.0 
     6.0 
     6.0 
     45.0 
     18.0 
	 */

	public static TurnSolution turnAround  (
			Double[] originalParameters ,
			int paramID,
			double originalSavings,
			double step,
			String filePath) {

		double bestSavingsOnThisRun = originalSavings;
		Double[] bestParametersOnThisRun = null;

		Double[] previousStepParameters = originalParameters.clone();

		for(int i = 0 ; i< 5; i++){
			Double[] newParameters = previousStepParameters.clone();
			newParameters[paramID] = previousStepParameters[paramID] + step;

			//lancer trnsys avec les nouveau parameters
			ReplaceInTextFile replace = new ReplaceInTextFile(
					"template/building1.b17.template",
					"template/building1.b17",newParameters) ;
			replace.doIt();
			RunTrnSys.runTrnSys(filePath,false);


			// open the outputFile and read its data content.
			// find the minimum needed power and corresponding night time order
			// if minimum is acceptable then save the b17file in a special folder
			Map<String,Number> newsolution  = OpenTrnsysoutput.getSolution();

			if(OpenTrnsysoutput.isSolutionValid(newsolution)){
				Double pricefor19 = (Double) newsolution.get("price19" );
				Double bestPricevalue = (Double) newsolution.get("price" );
				Double bestPriceorder = (Double) newsolution.get("temp" );

				double saving = ((bestPricevalue - pricefor19 )/ pricefor19) * 100;

				System.out.println("Solution found for " + bestPriceorder + " saving is "+ saving);
				if(saving < bestSavingsOnThisRun){
					//fuck YEAHHHH
					System.out.println("Solution is better than the last solution !!! ");
					bestSavingsOnThisRun = saving;
					bestParametersOnThisRun = newParameters;
					// dump the parameters in the file.
					Main.dumpParametersInTheEndOfTheFile("adjusting.txt", newsolution, newParameters);
				}

			}
			
			previousStepParameters = newParameters;
		}//end while

		return new TurnSolution(bestSavingsOnThisRun, bestParametersOnThisRun);
	}





	public static void main(String[] args) {
		Double[] originParameters = new Double[]{6000d,9d,9d,6d,6d,45d,18d};
		Double[] stepArray = new Double[]{500d,1d,1d,1d,1d,1d,1d};
		Double[] minValueArray = new Double[]{6000d,3d,3d,3d,3d,45d,1d};
		Double[] maxValueArray = new Double[]{6000000d,300d,300d,300d,300d,1e10d,1e6d};
		//allParameters.add(all);

		double bestSavings = 0d;
		//lancer trnsys une fois avec le setOriginal
		ReplaceInTextFile replace = new ReplaceInTextFile(
				"template/building1.b17.template",
				"template/building1.b17",originParameters) ;
		replace.doIt();
		String currentPath = System.getProperty("user.dir");
		String separator = System.getProperty("file.separator");
		String filePath = currentPath+separator+"template"+separator+ "onetype56.dck";
		RunTrnSys.runTrnSys(filePath,false);
		Map<String,Number> solution  = OpenTrnsysoutput.getSolution();
		if(OpenTrnsysoutput.isSolutionValid(solution)){
			Double pricefor19 = (Double) solution.get("price19" );
			Double bestPricevalue = (Double) solution.get("price" );
			Double bestPriceorder = (Double) solution.get("temp" );

			double saving = ((bestPricevalue - pricefor19 )/ pricefor19) * 100;
			bestSavings = saving;
		}else{
			System.out.println("DAAAAAAAAAAAFUCK");
		}
		
		System.out.println("end of the initial TRNSYS process");
		Random rand = new Random();
		while(true){
			//choisir un paramètres au hazard
			int parametersSetSize = originParameters.length;
			int parameterID = rand.nextInt(parametersSetSize);
			
			
			System.out.println("chosenParameter is "+parameterID);
			TurnSolution turnSolution = turnAround(originParameters, parameterID, bestSavings, stepArray[parameterID], filePath);
			
			if(turnSolution.bestParametersOnThisRun == null){
				//nothing betterFound, 
				System.out.println("no better solution Found :(  hope random will help");
			}else{
				System.out.println("A better origin solution found, !!");
				originParameters = turnSolution.bestParametersOnThisRun;
				bestSavings = turnSolution.bestSavingsOnThisRun;
			}
			
		}

	}

}
