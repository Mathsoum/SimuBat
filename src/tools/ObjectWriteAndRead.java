package tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class ObjectWriteAndRead {
	
	
	public static Object loadFromFile(String fileName) throws
	IOException, ClassNotFoundException, FileNotFoundException{
	
		FileInputStream fos = new FileInputStream(fileName);
		ObjectInputStream oos = new ObjectInputStream(fos);

		Object o = oos.readObject();
		oos.close();
		fos.close();
		return o;
	}
	
	public static void saveToFile (String fileName,Object o) throws IOException{
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject( o);
		oos.flush();
		oos.close();
	}
}
