package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class WriteListToFile {
	public static void writeToFile(String fileFullPath ,String name, LinkedList<Double> list) throws IOException{
		
		
		String endline = System.getProperty("line.separator");
		
		FileWriter fileWriter = new FileWriter(new File(fileFullPath));
		fileWriter.append(name+endline);
		
		for(Double d : list){
			fileWriter.append(""+d+endline);
		}
		
		fileWriter.close();
	}
}
