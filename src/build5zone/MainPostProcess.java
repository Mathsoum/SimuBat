package build5zone;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.util.LinkedList;
import java.util.TreeMap;

import tools.WriteListToFile;

public class MainPostProcess {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		FileInputStream fos = new FileInputStream("dataBYtempOrder.dat");
		ObjectInputStream oos = new ObjectInputStream(fos);

		SetOfMap weeklyZ1DataVSTempOrder = (SetOfMap) oos.readObject();


		for(String s : weeklyZ1DataVSTempOrder.getKeys()){
			//System.out.println(s);
		}

		
		LinkedList<Double> tempControl = new LinkedList<>();
		double dayTimeTemp = 20;

		for(double weekID = 1 ;weekID<=52;weekID++){
			TreeMap<Double, Double> coldInconf = weeklyZ1DataVSTempOrder.getMap("coldInconfortZ1W"+weekID);
			TreeMap<Double, Double> energy = weeklyZ1DataVSTempOrder.getMap("energyZ1W"+weekID);
			TreeMap<Double, Double> price = weeklyZ1DataVSTempOrder.getMap("priceZ1W"+weekID);

			//calc on price
			TreeMap<Double, Double> minimums = weeklyZ1DataVSTempOrder.getMinimums("energyZ1W"+weekID);

			double nightTime = minimums.firstKey();
			System.out.println("w"+weekID+"sol " +nightTime);
			for(Double d: Main.generateOneWeekTempOrder(nightTime, 20d, 7, 18)){
				tempControl.add(d);
			}
			
			
		}
		
		System.out.println("tempControlsize = "+tempControl.size());
		try {
			//C:\Trnsys17\MyProjects\5ZonesModel_20130301\Profils
			String basePathwithending = "C:\\Trnsys17\\MyProjects\\5ZonesModel_20130301\\";
			WriteListToFile.writeToFile(basePathwithending+"Profils\\TempConsigne.txt","nightTempOrder", tempControl);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
