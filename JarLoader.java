package jarLoader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * loading jars into the project to access and invoke classes
 * 
 * @author Thomas Peikert
 */
public class JarLoader {
	/**
	 * the package where the main package is located
	 */
	final String PACKAGE_STRUCTURE = "packagename";

	/**
	 * name of the class to execute
	 */
	final String CLASS_NAME = "ClassName";

	/**
	 * file type to filter for in directory
	 */
	final String FILE_TYPE = ".jar";

	/**
	 * counter when executing jars queue style
	 * 
	 * @todo maybe update when executing jars by name or index?
	 */
	private int countIndex = 0;

	/**
	 * string array of all jar paths that can be accessed
	 */
	private List<URL> urlList = new ArrayList<URL>();

	/**
	 * all preloaded classes
	 */
	private List<Class> classes = new ArrayList<Class>();

	/**
	 * given directory
	 */
	private String directory;

	/**
	 * load all jars from directory
	 * 
	 * @param directory
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public JarLoader(String directory) throws ClassNotFoundException,
			IOException {
		this.directory = directory;

		// get all files from path
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();
		ArrayList<File> files = new ArrayList<File>();

		// add all .jar files to files list
		for (File file : listOfFiles) {
			if (file.isFile()) {

				if (getFileType(file.getName()).equals(FILE_TYPE))
					urlList.add(file.toURI().toURL());
			}
		}

		this.loadAll();

	}

	/**
	 * JarLoader with just one jar
	 * 
	 * @param f
	 *            single jar
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public JarLoader(File f) throws IOException, ClassNotFoundException {
		urlList.add(f.toURI().toURL());

		this.loadAll();
	}

	/**
	 * JarLoader with a bunch of jar paths
	 * 
	 * @param f
	 *            number of jars
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public JarLoader(ArrayList<File> f) throws IOException,
			ClassNotFoundException {
		for (int i = 0; i < f.size(); i++)
			urlList.add(f.get(i).toURI().toURL());

		this.loadAll();
	}

	/**
	 * execute class by jar path
	 * 
	 * @param filename
	 *            jar path
	 * @throws IOException
	 */
	public void execute(String filename) {

		/**
		 * @todo properly map URLs to loaded classes
		 */
		int index;
		try {
			if (!filename.substring(filename.length() - FILE_TYPE.length())
					.equals(FILE_TYPE))
				filename += FILE_TYPE;

			index = urlList.indexOf(new File(directory + filename).toURI()
					.toURL());
			execute(index);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * execute nth class
	 * 
	 * @param index
	 *            of class to be executed
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void execute(int index) {

		if (index >= urlList.size())
			index = 0;

		try {
			classes.get(index).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * execute the next class in "queue"
	 * 
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void execute() {
		if (countIndex >= urlList.size())
			countIndex = 0;

		try {
			classes.get(countIndex).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		countIndex++;

	}

	/**
	 * load all jars to project
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadAll() throws IOException, ClassNotFoundException {

		for (URL url : urlList) {
			String file = url.toString();
			URL[] urlCast = { url };

			URLClassLoader sysloader = (URLClassLoader) ClassLoader
					.getSystemClassLoader();
			Class sysclass = URLClassLoader.class;

			URLClassLoader child = new URLClassLoader(urlCast,
					JarLoader.class.getClassLoader());
			Class classToLoad = Class.forName(PACKAGE_STRUCTURE + "."
					+ CLASS_NAME, true, child);

			classes.add(classToLoad);

			// execute functions
			// Method method = classToLoad.getDeclaredMethod ("myMethod");
			// Object result = method.invoke (instance);

		}
	}

	/**
	 * load one jar to project
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void loadOne(File f) throws IOException, ClassNotFoundException {
		URL url = f.toURI().toURL();

		String file = url.toString();
		URL[] urlCast = { url };

		URLClassLoader sysloader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		Class sysclass = URLClassLoader.class;

		URLClassLoader child = new URLClassLoader(urlCast,
				JarLoader.class.getClassLoader());
		Class classToLoad = Class.forName(PACKAGE_STRUCTURE + "." + CLASS_NAME,
				true, child);

		classes.add(classToLoad);

		// execute functions
		// Method method = classToLoad.getDeclaredMethod ("myMethod");
		// Object result = method.invoke (instance);

	}

	/**
	 * get filetype (last "." and what follows) "bla.jpeg" => ".jpeg"
	 * 
	 * @param name
	 *            name of file
	 * @return filetype
	 */
	private String getFileType(String name) {
		int fileEndingStart = name.toString().lastIndexOf(".");
		return name.toString().substring(fileEndingStart);
	}
}
