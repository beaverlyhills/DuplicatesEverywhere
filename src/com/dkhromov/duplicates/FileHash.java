package com.dkhromov.duplicates;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;

public class FileHash {

	public static final int SMALL_HASH_SIZE = 64*1024;
	public static final int LARGE_HASH_SIZE = 4*1024*1024;
	public static final int HASH_MULTIPLIER = 4;
	
	public static byte[] smallBuffer = new byte[SMALL_HASH_SIZE];
	public static byte[] largeBuffer = new byte[LARGE_HASH_SIZE];

	private File path;
	String shortHash;
	ArrayList<String> longHash = new ArrayList<String>();
	private long size;

	public FileHash(File path) {
		this.path = path;
		this.size = path.length();
		this.shortHash = createDigest(path, smallBuffer, 0, 1);
	}
	
	protected String createDigest(File f, byte[] buf, long offset, long counter) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			InputStream is = new FileInputStream(f);
			DigestInputStream dis = new DigestInputStream(is, md);
			dis.skip(offset);
			while (counter-- > 0) {
				if (dis.read(buf) == -1) {
					break;
				}
			}
			dis.close();
			return bytesToHex(md.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FileHash) {
			FileHash fo = (FileHash)o;
			if (getSize() == fo.getSize() && shortHash.equals(fo.shortHash)) {
				return compareLongHashes(fo);
			}
		}
		return false;
	}

	protected String getLongHash(int index, long offset, long counter) {
		if (longHash.size() > index) {
			return longHash.get(index);
		}
		if (longHash.size() < index) {
			throw new RuntimeException("Invalid index");
		}
		String result = createDigest(getPath(), largeBuffer, offset, counter);
		longHash.add(result);
		return result;
	}
	
	protected boolean compareLongHashes(FileHash fo) {
		int index = 0;
		long offset = smallBuffer.length;
		long counter = 1;
		while (offset < getSize()) {
			String hash1 = this.getLongHash(index, offset, counter);
			String hash2 = fo.getLongHash(index, offset, counter);
			if (!hash1.equals(hash2)) {
				return false;
			}
			offset += largeBuffer.length*counter;
			counter *= HASH_MULTIPLIER;
			index++;
		}
		return true;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	@Override
	public String toString() {
		return getPath().getAbsolutePath()+" "+shortHash+" "+longHash.toString();
	}

	public long getSize() {
		return size;
	}

	public File getPath() {
		return path;
	}
}
