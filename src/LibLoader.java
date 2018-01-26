import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;


public class LibLoader {
	public static final String NATIVE_LIB_PATH = "." + File.separator + "lib" + File.separator;

	private static void addLibraryPath(String s) throws Exception {
		final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		usrPathsField.setAccessible(true);

		final String[] paths = (String[]) usrPathsField.get(null);

		for (String path : paths) {
			if (path.equals(s)) {
				return;
			}
		}

		final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		newPaths[newPaths.length - 1] = s;
		usrPathsField.set(null, newPaths);
	}

	public static boolean is64Bit() {
		return System.getProperty("os.arch").contains("64");
	}
	public static void loadSerialLibs() throws Exception {
		System.out.println("[LIBLOADER] OS " + System.getProperty("os.name") + " - " + System.getProperty("os.version"));		
		System.out.println("[LIBLOADER] JAVA " + System.getProperty("java.vendor") + " - " + System.getProperty("java.version") + " - " + System.getProperty("java.home"));
		System.out.println("[LIBLOADER] ARCHITECTURE " + System.getProperty("os.arch"));
		String append = "";
		boolean x64 = is64Bit();
		if (x64) {
			append = File.separator + "64" + File.separator;
		}else{
			append = File.separator + "32" + File.separator;
		}
		if (System.getProperty("os.name").equals("Mac OS X")) {
			//mac doesent have x64/x86 libs
			System.out.println("[LIBLOADER] LOADED MacOS NATIVE LIBRARIES ");
			System.loadLibrary("librxtxSerial");
		} else if (System.getProperty("os.name").contains("Linux")) {
			throw new RuntimeException("linux currently not supported");
			
		} else if (System.getProperty("os.name").contains("FreeBSD")) {
			throw new RuntimeException("freeBSD currently not supported");
		} else {
			addLibraryPath(NATIVE_LIB_PATH + "Windows" + append);
			if (x64) {
				System.loadLibrary("rxtxSerial");
				System.out.println("[LIBLOADER] LOADED WINDOWS 64bit NATIVE LIBRARIES ");
			} else {
				System.loadLibrary("rxtxSerial");
				System.out.println("[LIBLOADER] LOADED WINDOWS 32bit NATIVE LIBRARIES ");
			}
		}
	}
	
}
