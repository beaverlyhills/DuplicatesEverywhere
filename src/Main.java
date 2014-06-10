import java.io.File;
import java.util.ArrayList;

import com.dkhromov.duplicates.FileHash;
import com.dkhromov.duplicates.HashManager;


public class Main {

	public static void main(String[] args) {

		System.err.print("Started\n");

		HashManager mgr = new HashManager();
		
		boolean flagMaster = false;
		boolean flagDupsOnly = false;
		boolean flagPossible = false;
		boolean flagReal = false;
		boolean flagFalse = false;
		boolean flagAll = true;
		File masterPath = null;
		
		int arg = 0;
		for (int i=0; i<args.length; i++) {
			if (args[i].equals("-m")) {
				flagMaster = true;
			} else if (args[i].equals("-M")) {
				flagMaster = true;
				flagDupsOnly = true;
			} else if (args[i].equals("-p")) {
				flagPossible = true;
				flagAll = false;
			} else if (args[i].equals("-r")) {
				flagReal = true;
				flagAll = false;
			} else if (args[i].equals("-f")) {
				flagFalse = true;
				flagAll = false;
			} else if (args[i].equals("-a")) {
				flagPossible = true;
				flagReal = true;
				flagFalse = true;
			} else {
				walk(args[i], mgr, flagMaster && (arg++ > 0));
				if (flagMaster && arg == 1) {
					if (flagDupsOnly) {
						masterPath = new File(args[i]);
					}
					System.out.println("Got "+mgr.getCount()+" (supposedly) unique files with masters in "+args[i]+".");
				}
			}
		}

		if (flagPossible || flagAll) {
			System.out.println("* Possible duplicates");
			printDuplicates(mgr.getPossibleDuplicates(), "? ", masterPath);
		}

		if (flagReal || flagAll) {
			System.out.println("* Real duplicates (bit-exact copies)");
			printDuplicates(mgr.getDuplicates(), "+ ", masterPath);
		}
		
		if (flagFalse || flagAll) {
			System.out.println("* False duplicates (possibly corrupted copies)");
			printDuplicates(mgr.getFalseDuplicates(), "- ", masterPath);
		}
	}

	public static void printDuplicates(ArrayList<ArrayList<FileHash>> duplicateTuples, String prefix, File master) {
		for (ArrayList<FileHash> hash : duplicateTuples) {
			System.out.println(hash.size()+" files of "+hash.get(0).getSize()+" bytes");
			for (FileHash fileHash : hash) {
				if (master == null || fileHash.getPath().getAbsolutePath().startsWith(master.getAbsolutePath())) {
					System.out.println(prefix+fileHash.getPath());
				}
			}
		}
	}

	public static ArrayList<String> directories = new ArrayList<String>();
	
	public static void walk( String path, HashManager mgr, boolean appendOnly ) {

		File root = new File( path );
		
		System.err.println("Walk "+root.getAbsolutePath());
		
		File[] list = root.listFiles();
		
		if (list == null) return;

		for ( File f : list ) {
			String p = f.getAbsolutePath();
			try {
				if ( f.isDirectory() ) {
					if (directories.contains(p)) {
						continue;
					}
					walk( p, mgr, appendOnly );
					directories.add(p);
				} else {
					System.err.println("Add "+p);
					mgr.addFile(f, appendOnly);
				}
			} catch (Exception e) {
				System.err.println("Skip on error "+p);
			}
		}
	}

}
