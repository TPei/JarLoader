package jarLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	/**
	 * path to where the target files are located
	 */
	final static String DIRECTORY = "/home/user/Documents/jars/";

	public static void main(String[] args) {

		
		try {
			// create new loader with all jars in a directory
			JarLoader loader = new JarLoader(DIRECTORY);
			
			// create a new loader with just one jar
			//JarLoader loader = new JarLoader(new File("/home/user/Documents/jars/filename.jar"));
			
			System.out.println("DEMO");

			// execute by jarname
			System.out.println("=========\nexecute by jar  name:");
			loader.execute("filename.jar");
			loader.execute("filename");

			// execute by index in given jarpath name string array
			System.out
					.println("=========\nexecute by index in given list:");
			loader.execute(1);
			loader.execute(0);
			loader.execute(2);

			// execute queue-style
			System.out.println("=========\nexecute queue-style:");
			loader.execute();
			loader.execute();
			loader.execute();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
