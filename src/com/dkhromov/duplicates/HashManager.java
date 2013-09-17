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

	public ArrayList<ArrayList<String>> getPossibleDuplicates() {
		ArrayList<ArrayList<String>> duplicates = new ArrayList<ArrayList<String>>();
		for (ArrayList<FileHash> list : files.values()) {
			if (list.size() > 1) {
				FileHash[] items = list.toArray(new FileHash[list.size()]);
				for (int i=0; i<items.length; i++) {
					FileHash file1 = items[i];
					if (file1 != null) {
						ArrayList<String> dups = null;
						for (int j=i+1; j<items.length; j++) {
							FileHash file2 = items[j];
							if (file2 != null && file1.size == file2.size) {
								if (dups == null) {
									dups = new ArrayList<String>();
									dups.add(file1.path.getAbsolutePath());
								}
								items[j] = null;
								dups.add(file2.path.getAbsolutePath());
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

	public ArrayList<ArrayList<String>> getDuplicates() {
		ArrayList<ArrayList<String>> duplicates = new ArrayList<ArrayList<String>>();
		for (ArrayList<FileHash> list : files.values()) {
			if (list.size() > 1) {
				FileHash[] items = list.toArray(new FileHash[list.size()]);
				for (int i=0; i<items.length; i++) {
					FileHash file1 = items[i];
					if (file1 != null) {
						ArrayList<String> dups = null;
						for (int j=i+1; j<items.length; j++) {
							FileHash file2 = items[j];
							if (file2 != null && file1.equals(file2)) {
								if (dups == null) {
									dups = new ArrayList<String>();
									dups.add(file1.path.getAbsolutePath());
								}
								items[j] = null;
								dups.add(file2.path.getAbsolutePath());
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

	public ArrayList<ArrayList<String>> getFalseDuplicates() {
		ArrayList<ArrayList<String>> duplicates = new ArrayList<ArrayList<String>>();
		for (ArrayList<FileHash> list : files.values()) {
			if (list.size() > 1) {
				FileHash[] items = list.toArray(new FileHash[list.size()]);
				for (int i=0; i<items.length; i++) {
					FileHash file1 = items[i];
					ArrayList<String> dups = null;
					for (int j=i+1; j<items.length; j++) {
						FileHash file2 = items[j];
						if (file1.size == file2.size && !file1.equals(file2)) {
							if (dups == null) {
								dups = new ArrayList<String>();
								dups.add(file1.path.getAbsolutePath());
							}
							dups.add(file2.path.getAbsolutePath());
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
