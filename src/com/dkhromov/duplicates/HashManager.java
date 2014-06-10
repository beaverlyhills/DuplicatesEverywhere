package com.dkhromov.duplicates;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class HashManager {

	HashMap<String, ArrayList<FileHash>> files = new HashMap<String, ArrayList<FileHash>>();

	public static abstract class Comparator {
		public abstract boolean match(FileHash file1, FileHash file2);
	}
	
	public static Comparator QUICK_MATCH = new Comparator() {

		@Override
		public boolean match(FileHash file1, FileHash file2) {
			return file1.getSize() == file2.getSize();
		}
		
	};

	public static Comparator FULL_MATCH = new Comparator() {

		@Override
		public boolean match(FileHash file1, FileHash file2) {
			return file1.equals(file2);
		}
		
	};

	public static Comparator FALSE_MATCH = new Comparator() {

		@Override
		public boolean match(FileHash file1, FileHash file2) {
			return file1.getSize() == file2.getSize() && !file1.equals(file2);
		}
		
	};
	
	public HashManager() {

	}

	public void save() {
		// TODO: 
	}

	public void restore() {
		// TODO:
	}

	public void addFile(File path, boolean appendOnly) {
		FileHash hash = new FileHash(path);
		ArrayList<FileHash> list = files.get(hash.shortHash);
		if (list == null) {
			if (appendOnly) {
				return;
			}
			list = new ArrayList<FileHash>();
			files.put(hash.shortHash, list);
		}
		list.add(hash);
	}

	public ArrayList<ArrayList<FileHash>> getDuplicates(Comparator type) {
		ArrayList<ArrayList<FileHash>> duplicates = new ArrayList<ArrayList<FileHash>>();
		for (ArrayList<FileHash> list : files.values()) {
			if (list.size() > 1) {
				FileHash[] items = list.toArray(new FileHash[list.size()]);
				for (int i=0; i<items.length; i++) {
					FileHash file1 = items[i];
					if (file1 != null) {
						ArrayList<FileHash> dups = null;
						for (int j=i+1; j<items.length; j++) {
							FileHash file2 = items[j];
							if (file2 != null && type.match(file1, file2)) {
								if (dups == null) {
									dups = new ArrayList<FileHash>();
									dups.add(file1);
								}
								items[j] = null;
								dups.add(file2);
							}
						}
						if (dups != null) {
							duplicates.add(dups);
						}
					}
				}
			}
		}
		return duplicates;
	}
	
	public ArrayList<ArrayList<FileHash>> getPossibleDuplicates() {
		return getDuplicates(QUICK_MATCH);
	}

	public ArrayList<ArrayList<FileHash>> getDuplicates() {
		return getDuplicates(FULL_MATCH);
	}

	public ArrayList<ArrayList<FileHash>> getFalseDuplicates() {
		return getDuplicates(FALSE_MATCH);
	}

	public int getCount() {
		return files.size();
	}
}
