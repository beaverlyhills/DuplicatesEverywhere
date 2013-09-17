package com.dkhromov.duplicates;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class HashManager {

	HashMap<String, ArrayList<FileHash>> files = new HashMap<String, ArrayList<FileHash>>();

	public HashManager() {

	}

	public void save() {
		// TODO: 
	}

	public void restore() {
		// TODO:
	}

	public void addFile(File path) {
		FileHash hash = new FileHash(path);
		ArrayList<FileHash> list = files.get(hash.shortHash);
		if (list == null) {
			list = new ArrayList<FileHash>();
			files.put(hash.shortHash, list);
		}
		list.add(hash);
	}

	public ArrayList<ArrayList<FileHash>> getPossibleDuplicates() {
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
							if (file2 != null && file1.getSize() == file2.getSize()) {
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

	public ArrayList<ArrayList<FileHash>> getDuplicates() {
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
							if (file2 != null && file1.equals(file2)) {
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

	public ArrayList<ArrayList<FileHash>> getFalseDuplicates() {
		ArrayList<ArrayList<FileHash>> duplicates = new ArrayList<ArrayList<FileHash>>();
		for (ArrayList<FileHash> list : files.values()) {
			if (list.size() > 1) {
				FileHash[] items = list.toArray(new FileHash[list.size()]);
				for (int i=0; i<items.length; i++) {
					FileHash file1 = items[i];
					ArrayList<FileHash> dups = null;
					for (int j=i+1; j<items.length; j++) {
						FileHash file2 = items[j];
						if (file1.getSize() == file2.getSize() && !file1.equals(file2)) {
							if (dups == null) {
								dups = new ArrayList<FileHash>();
								dups.add(file1);
							}
							dups.add(file2);
						}
					}
					if (dups != null) {
						duplicates.add(dups);
					}
				}
			}
		}
		return duplicates;
	}
}
