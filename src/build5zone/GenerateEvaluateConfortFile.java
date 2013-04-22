package build5zone;

import java.io.IOException;
import java.util.LinkedList;

import tools.WriteListToFile;

public class GenerateEvaluateConfortFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String basePathwithending = "C:\\Trnsys17\\MyProjects\\5ZonesModel_20130301_winter\\";
		
		LinkedList<Double> list =Main.generateTempOrder(0d,1d,8,18);
		
		System.out.println("list size "+list.size());
		try {
			//C:\Trnsys17\MyProjects\5ZonesModel_20130301\Profils
			WriteListToFile.writeToFile(basePathwithending+"Profils\\occup.txt","nightTempOrder", list);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
