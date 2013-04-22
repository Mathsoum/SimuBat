package tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.TreeMap;

import javax.swing.JFrame;

import panel.MultipleTimeTrace;

public class OpenTrnsysoutput {
	/**
	 * @param args
	 */
	public static TreeMap<Long, Double> collectData(
			String fileName ,
			int collumnToKeep) {

		TreeMap<Long, Double> alldata = new TreeMap<Long, Double>();

		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(fileName);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line

			long timeValue = 0;
			boolean firstLinePassed = false; 

			while ((strLine = br.readLine()) != null)   {

				if(firstLinePassed){
					String[] split = strLine.split("\t  ");
					//System.out.println("splitSize : "+split.length + " time : "+timeValue);

					String valueAsString = split[collumnToKeep];
					Double valueAsDouble = Double.valueOf(valueAsString);
					alldata.put(timeValue, valueAsDouble);


					//dlc.addLog(rawDataKey, timeValue, integralValueD);

					timeValue++;
				}else{
					// Print the content on the console
					//System.out.println (strLine);
					firstLinePassed = true;
				}
			}

			//System.out.println("endReading file");
			//Close the input stream
			in.close();


		}catch (Exception e){//Catch exception if any
			System.out.println("Error: " + e.getMessage());
			System.out.println(e.getLocalizedMessage());
		}


		return alldata;
	}


	public static TreeMap<Long, Double> getEveyPeak(TreeMap<Long, Double> alldata){

		TreeMap<Long, Double> peakmap = new TreeMap<Long, Double>();
		NavigableSet<Long> keyset = alldata.navigableKeySet();
		Long[] allkeys = new Long[keyset.size()];
		keyset.toArray(allkeys);

		for(int i = 1 ; i<allkeys.length-1 ; i++ ){
			Double previous = alldata.get(allkeys[i-1]);
			Double current = alldata.get(allkeys[i]);
			Double next = alldata.get(allkeys[i+1]);

			if(current > next )
			{
				peakmap.put(allkeys[i], current);	
			}

		}

		return peakmap;
	}

	public static TreeMap<Long, Double> getEveyValley(TreeMap<Long, Double> alldata){

		TreeMap<Long, Double> peakmap = new TreeMap<Long, Double>();
		NavigableSet<Long> keyset = alldata.navigableKeySet();
		Long[] allkeys = new Long[keyset.size()];
		keyset.toArray(allkeys);

		for(int i = 1 ; i<allkeys.length-1 ; i++ ){
			Double previous = alldata.get(allkeys[i-1]);
			Double current = alldata.get(allkeys[i]);
			Double next = alldata.get(allkeys[i+1]);

			if(previous>current && current < next ){
				//current is a peak
				peakmap.put(allkeys[i], current);
			}
		}

		return peakmap;
	}

	/***************
	 * all under should NOT be used
	 */


	public static TreeMap<Long, Double> collectDataInoutputout(int collumnToKeep) {
		return collectData("C:\\Users\\fanf\\Desktop\\output.out", collumnToKeep);
	}

	public static Map<String,Number> getSolution(){
		HashMap<String, Number> solution = new HashMap<>();

		TreeMap<Long, Double> peaks = getEveyPeak(collectDataInoutputout(1));
		System.out.println("peakcount  : " + peaks.size());
		for(Entry<Long, Double> peak : peaks.entrySet()){
			//System.out.println("peak : "+peak.getKey() + " value : "+peak.getValue());
		}

		TreeMap<Long, Double> bestSolution = getEveyValley(peaks);

		System.out.println("solutionSize = "+bestSolution.size());

		if(bestSolution.size() == 1){
			TreeMap<Long, Double> allorders = collectDataInoutputout(2);
			Entry<Long, Double> bestPrice = bestSolution.firstEntry();

			Double pricefor19 = priceFor19(peaks, allorders) ;
			Double bestPricevalue = bestPrice.getValue() ;
			Double bestPriceorder = allorders.get(bestPrice.getKey());
			if(pricefor19 != null){

				System.out.println("solution = "+allorders.get(bestPrice.getKey()));
				solution.put("price19",pricefor19 );
				solution.put("price", bestPricevalue);
				solution.put("temp", bestPriceorder);

			}
		}

		return solution;
	}

	public static Double priceFor19(TreeMap<Long, Double> peaks , TreeMap<Long, Double> allorders ){
		for(Entry<Long, Double> peak : peaks.entrySet()){
			Double tempconsigneForPeak  =  allorders.get(peak.getKey());
			if(tempconsigneForPeak != null && tempconsigneForPeak == 19D){
				//got it
				return peak.getValue();
			}
		}

		return null;
	}




	public static boolean isSolutionValid(Map<String,Number> solution){
		if(solution.size()==3){
			//solution is maybe valid
			Double temperature = (Double) solution.get("temp");
			if(temperature < 24 && temperature > 10){
				//solution IS VALID, FUCK YEAH
				return true;
			}
		}

		return false;
	}


	public static void main(String[] args) throws IOException {

		JFrame frame = new JFrame();
		MultipleTimeTrace mtt = new MultipleTimeTrace();
		frame.add(mtt);
		frame.setVisible(true);
		frame.pack();

		Map<Long, Double> coll1 = getEveyPeak(collectDataInoutputout(1));
		for(Entry<Long,Double> entry : coll1.entrySet()){
			mtt.addValue(entry.getKey(), entry.getValue(), "peak");
		}

		Map<Long, Double> coll2 = collectDataInoutputout(2);
		for(Entry<Long,Double> entry : coll2.entrySet()){
			mtt.addValue(entry.getKey(), entry.getValue(), "nightTimeTemp");
		}




		Map<String,Number> solution = getSolution();

		if(isSolutionValid(solution)){
			System.out.println("VALID,FUCK YEAHHH");
			FileWriter out = new FileWriter(new File("testopening.txt"), true);

			Double pricefor19 = (Double) solution.get("price19" );
			Double bestPricevalue = (Double) solution.get("price" );
			Double bestPriceorder = (Double) solution.get("temp" );

			double saving = ((bestPricevalue - pricefor19 )/ pricefor19) * 100;
			out.write(String.format("Solution at : %s°, saving : %.4f %% \n",bestPriceorder, saving));
			out.close();


		}else{
			System.out.println("Not Validj");
		}


	}



}
