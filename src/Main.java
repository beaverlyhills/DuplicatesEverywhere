import java.io.File;

import com.dkhromov.duplicates.HashManager;


public class Main {

	public static void main(String[] args) {

		System.out.print("Started\n");

		HashManager mgr = new HashManager();
		
		for (String path : args) {
			walk(path, mgr);
		}

		System.out.println("Possible duplicates");
		System.out.println(mgr.getPossibleDuplicates().toString());

		System.out.println("Real duplicates");
		System.out.println(mgr.getDuplicates().toString());
		
		System.out.println("False duplicates");
		System.out.println(mgr.getFalseDuplicates().toString());
	}

	public static void walk( String path, HashManager mgr ) {

		File root = new File( path );
		
		System.out.println("Walk "+root.getAbsolutePath());
		
		File[] list = root.listFiles();
		
		if (list == null) return;

		for ( File f : list ) {
			if ( f.isDirectory() ) {
				walk( f.getAbsolutePath(), mgr );
			} else {
				System.out.println("Add "+f.getAbsolutePath());
				mgr.addFile(f);
			}
		}
	}

}
