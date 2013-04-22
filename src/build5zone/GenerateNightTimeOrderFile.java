package build5zone;

import java.io.IOException;
import java.util.LinkedList;

import tools.WriteListToFile;

public class GenerateNightTimeOrderFile {

	/**
	 * @param args
	 */
	static String basePathwithending = "C:\\Trnsys17\\MyProjects\\5ZonesModel_20130301\\";
	static String fileName = "Profils\\TempConsigne.txt";
	
	
	
	public static LinkedList<Double> generateWeekelyTempControl(double[] nightTemps ,
			double dayTimeTemp ,
			int dayStartTime,int nightStartTimes){
		
		LinkedList<Double> tempControl = new LinkedList<Double>();

		for(Double night : nightTemps){
			tempControl.addAll(Main.generateOneWeekTempOrder(night, dayTimeTemp, dayStartTime, nightStartTimes));
		}
		
		return tempControl;
	}
	
	
	public static void randomEveryWeek(){
		LinkedList<Double> list = new LinkedList<Double>();
		
		for(int i = 0 ; i<52; i++){
			list.addAll(Main.generateOneWeekTempOrder(10,20d,7,18));
		}
		
		writeList(list);
	}
	
	public static void constantOneYear(){
		
		LinkedList<Double> list =Main.generateTempOrder(17d,20d,7,18);
		
		writeList(list);
	}
	
	public static void writeList(LinkedList<Double> list){
		System.out.println("list size "+list.size());
		try {
			//C:\Trnsys17\MyProjects\5ZonesModel_20130301\Profils
			WriteListToFile.writeToFile(basePathwithending+fileName,"nightTempOrder", list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		
		randomEveryWeek();
		
	}

}
