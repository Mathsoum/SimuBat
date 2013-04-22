package jfsma;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;


import panel.MultipleTimeTrace;
import tools.OpenTrnsysoutput;

public class OpenResults {
	public static void main(String[] args) throws IOException {
		
		JFrame frame = new JFrame();
		MultipleTimeTrace mtt = new MultipleTimeTrace();
		frame.add(mtt);
		frame.setVisible(true);
		frame.pack();
		
		Map<Long, Double> price_out0 = OpenTrnsysoutput.getEveyPeak(OpenTrnsysoutput.collectData("C:\\Users\\fanf\\Desktop\\output0.out",1));
		Map<Long, Double> price_out5 = OpenTrnsysoutput.getEveyPeak(OpenTrnsysoutput.collectData("C:\\Users\\fanf\\Desktop\\output5.out",1));
		Map<Long, Double> price_out10 = OpenTrnsysoutput.getEveyPeak(OpenTrnsysoutput.collectData("C:\\Users\\fanf\\Desktop\\output10.out",1));
		Map<Long, Double> price_out15 = OpenTrnsysoutput.getEveyPeak(OpenTrnsysoutput.collectData("C:\\Users\\fanf\\Desktop\\output15.out",1));
		
		System.out.println("coll1size is "+price_out0.size());
		for(Entry<Long,Double> entry : price_out0.entrySet()){
			mtt.addValue(entry.getKey(), entry.getValue(), "peak0");
		}
		for(Entry<Long,Double> entry : price_out5.entrySet()){
			mtt.addValue(entry.getKey(), entry.getValue(), "peak5");	
		}
		for(Entry<Long,Double> entry : price_out10.entrySet()){
			mtt.addValue(entry.getKey(), entry.getValue(), "peak10");
		}
		for(Entry<Long,Double> entry : price_out15.entrySet()){
			mtt.addValue(entry.getKey(), entry.getValue(), "peak15");
		}
		
		Map<Long, Double> temp_Order = OpenTrnsysoutput.collectData("C:\\Users\\fanf\\Desktop\\output0.out",2);

		
		
		FileWriter out = new FileWriter(new File("thefuckingbigcsv.csv"), false);
		
		out.write(String.format("ID;tempOrder ; price0 ; price5 ; price10 ; price 15 \n"));
		for(Entry<Long,Double> entry : price_out0.entrySet()){
			//mtt.addValue(entry.getKey(), entry.getValue(), "nightTimeTemp");
			Long timeStep = entry.getKey();
			out.write(String.format(" %s ; %s ; %s ; %s ; %s ; %s \n",
					timeStep, // time Used as ID
					temp_Order.get(timeStep),
					price_out0.get(timeStep),
					price_out5.get(timeStep),
					price_out10.get(timeStep),
					price_out15.get(timeStep)
					));
		}
		
		out.close();
		
		/*
		
		Map<String,Number> solution = OpenTrnsysoutput.getSolution();
		
		if(OpenTrnsysoutput.isSolutionValid(solution)){
			System.out.println("VALID,FUCK YEAHHH");
			
			
		}else{
			System.out.println("Not Validj");
		}
		
		*/
		
	}

}
