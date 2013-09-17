import java.io.File;
import java.util.ArrayList;

import com.dkhromov.duplicates.FileHash;
import com.dkhromov.duplicates.HashManager;


public class Main {

	public static void main(String[] args) {

		System.err.print("Started\n");

		HashManager mgr = new HashManager();
		
		for (String path : args) {
			walk(path, mgr);
		}

		System.out.println("* Possible duplicates");
		printDuplicates(mgr.getPossibleDuplicates(), "? ");

		System.out.println("* Real duplicates (bit-exact copies)");
		printDuplicates(mgr.getDuplicates(), "+ ");
		
		System.out.println("* False duplicates (possibly corrupted copies)");
		printDuplicates(mgr.getFalseDuplicates(), "- ");
	}

	public static void printDuplicates(ArrayList<ArrayList<FileHash>> duplicateTuples, String prefix) {
		for (ArrayList<FileHash> hash : duplicateTuples) {
			System.out.println(hash.size()+" files of "+hash.get(0).getSize()+" bytes");
			for (FileHash fileHash : hash) {
				System.out.println(prefix+fileHash.getPath());
			}
		}
	}

	public static void walk( String path, HashManager mgr ) {

		File root = new File( path );
		
		System.err.println("Walk "+root.getAbsolutePath());
		
		File[] list = root.listFiles();
		
		if (list == null) return;

		for ( File f : list ) {
			if ( f.isDirectory() ) {
				walk( f.getAbsolutePath(), mgr );
			} else {
				System.err.println("Add "+f.getAbsolutePath());
				mgr.addFile(f);
			}
		}
	}

}
