package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import jfsma.Main;


public class ReplaceInTextFile {

	protected String inputFileName = null;
	protected String outputFileName = null;
	protected Double[] allParams = null;
	
	public ReplaceInTextFile(String inputFileName, String outputFileName,
			Double[] allParams) {
		super();
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.allParams = allParams;
	}

	int count = 0;
	public void doIt(){
		try{
			count = 0;
			FileInputStream infstream = new FileInputStream(inputFileName);
			DataInputStream indatainput = new DataInputStream(infstream);
			BufferedReader inbr = new BufferedReader(new InputStreamReader(indatainput));
			
			FileOutputStream outfstream = new FileOutputStream(outputFileName);
			DataOutputStream outdataStream = new DataOutputStream(outfstream);
			BufferedWriter outbr = new BufferedWriter(new OutputStreamWriter(outdataStream));
			
			String strLine;
			
			while ((strLine = inbr.readLine()) != null)   {
				String modifiedLine = processLine(strLine);
				
				outbr.write(modifiedLine);
			}
			
			indatainput.close();
			outbr.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public String processLine(String line) {
		String newLine = line;
		while (newLine.contains("<>")) {
			newLine = newLine.replaceFirst("<>", Main.doubletoString(allParams[count]));
			count++;
		}
		return newLine+"\n";
	}
}
