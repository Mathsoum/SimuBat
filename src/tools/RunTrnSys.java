package tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunTrnSys {

	
	/**
	 * @param args
	 */
	public static void runTrnSys(String dckfilefullpath , boolean block) {
		try {
			//ADD /N to not stop
			String process = "C:\\Trnsys17\\Exe\\TRNExe.exe "+dckfilefullpath;
			String torun = null;
			
			if(block){
				torun = process;
			}else{
				torun = process+" /N";
			}
			Process p = Runtime.getRuntime().exec(torun);
			
			//exec is locking until the end of the simulation.
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = null;
			while( (line = br.readLine() ) != null){
				System.out.println(line);
			}
			
			;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
