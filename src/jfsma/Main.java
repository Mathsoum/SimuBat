package jfsma;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tools.OpenTrnsysoutput;
import tools.ReplaceInTextFile;
import tools.RunTrnSys;



public class Main {

	public static String doubletoString(double d){
		String s = String.format("%.3f", d);

		return s;
	}

	public static Set<Double[]> generateParameters(){
		/**


		for(double first = 0 ; first <= 50 ; first += 1){
		}
                  L mur3
		+----------------------+
mur1    |                      | l  mur2
		|                      |  
		+----------------------+
		        mur4
		 */

		HashSet<Double[]> allParameters = new HashSet<>();
		for(double first = 1 ; first <= 10 ; first += 1){ // l
			for(double second = 1 ; second <= 10 ; second += 1 ){ // L 
				for(double fifth = 1 ; fifth <= 3 ; fifth += 0.5 ){ // capacitanceVolumique
					for(double maxPower = 3000 ; maxPower <= 6000 ; maxPower += 1000 ){ // max power

						double hauteur = 3d;
						double aireMur1 = first*hauteur;
						double aireMur2 = first*hauteur;

						double aireMur3 = second*hauteur;
						double aireMur4 = second*hauteur;

						double volume = first * second * hauteur;
						double capacitance = fifth * volume;
						Double[] all = new Double[]{maxPower,aireMur1,aireMur2,aireMur3,aireMur4,capacitance,volume};
						allParameters.add(all);
					}
				}
			}
		}

		return allParameters;
	}



	public static void process(Double[] parameters) throws IOException{
		//simulation parameters

		// open the model file
		//for each combination of parameter, do the process
		// replace in the model file wildcard by the given parameters
		ReplaceInTextFile replace = new ReplaceInTextFile(
				"template/building1.b17.template",
				"template/building1.b17",parameters) ;
		replace.doIt();

		// run the simulation
		String currentPath = System.getProperty("user.dir");
		String separator = System.getProperty("file.separator");
		String filePath = currentPath+separator+"template"+separator+ "onetype56.dck";

		System.out.println("filepath is : "+filePath);
		//String s = "C:\\Trnsys17\\MyProjects\\onetype56\\onetype56.dck";
		RunTrnSys.runTrnSys(filePath,false);


		// open the outputFile and read its data content.
		// find the minimum needed power and corresponding night time order
		// if minimum is acceptable then save the b17file in a special folder
		Map<String,Number> solution  = OpenTrnsysoutput.getSolution();

		if(OpenTrnsysoutput.isSolutionValid(solution)){


			System.out.println("VALID,FUCK YEAHHH");

			ReplaceInTextFile replaceer = new ReplaceInTextFile(
					"template/building1.b17.template",
					"solution/"+parameters.hashCode()+"_building1.b17",
					parameters) ;
			replaceer.doIt();



			FileWriter out = new FileWriter(new File("Results.txt"), true);

			Double pricefor19 = (Double) solution.get("price19" );
			Double bestPricevalue = (Double) solution.get("price" );
			Double bestPriceorder = (Double) solution.get("temp" );

			double saving = ((bestPricevalue - pricefor19 )/ pricefor19) * 100;
			out.write(String.format("Solution at : %s°, saving : %.4f %% \n",bestPriceorder, saving));
			out.write("solution/"+parameters.hashCode()+"_building1.b17 \n");
			for(Double d : parameters){
				out.write(String.format("     %s \n",d));
			}

			out.write(" \n ");
			out.close();



		}else{
			System.out.println("Not Valid");
		}


	}

	public static void dumpParametersInTheEndOfTheFile (String fileName,
			Map<String,Number> solution,
			Double[] parameters){
		try {
			FileWriter out = new FileWriter(new File("Results.txt"), true);



			Double pricefor19 = (Double) solution.get("price19" );
			Double bestPricevalue = (Double) solution.get("price" );
			Double bestPriceorder = (Double) solution.get("temp" );

			double saving = ((bestPricevalue - pricefor19 )/ pricefor19) * 100;
			out.write(String.format("Solution at : %s°, saving : %.4f %% \n",bestPriceorder, saving));
			out.write("solution/"+parameters.hashCode()+"_building1.b17 \n");
			for(Double d : parameters){
				out.write(String.format("     %s \n",d));
			}

			out.write(" \n ");
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {

		boolean runTrnsys = true;
		// generate every combination we want to try .
		Set<Double[]> allparams =  generateParameters();

		try {
			FileWriter out = new FileWriter(new File("Results.txt"), false);
			out.write("findbuilding " + new Date() + "\n");
			int count = allparams.size();
			out.write("simulationCount is : "+count + "\n");
			double estimatedDuration  =  count*11d/3600d;
			out.write("estimatedDuration is :"+ estimatedDuration + "hours \n");
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		for(Double[] params : allparams){
			try {
				if(runTrnsys) {
					process(params) ; 
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		try {
			FileWriter out = new FileWriter(new File("Results.txt"), true);
			out.write("end process " + new Date() + "\n");

			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
