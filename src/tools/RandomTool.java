package tools;

import java.util.Random;

public class RandomTool {
	Random rand = new Random();

	public double randomDouble(double min,double max){
		if(min==max){
			return min;
		}
		return ((rand.nextDouble())*(max-min))+min;
	}
}
