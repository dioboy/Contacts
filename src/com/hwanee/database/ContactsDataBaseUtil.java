package com.hwanee.database;

import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteFullException;
import android.os.StatFs;

public class ContactsDataBaseUtil {
	private static final int MEM_THRESHOLD = 1024 * 8;

	// isLowMemory Á¤ÀÇ
	@SuppressWarnings("deprecation")
	public static boolean isLowMemory(SQLiteException e) {
		if (e instanceof SQLiteFullException) {
			return true;
		} else if (e.getMessage().contains("no transaction is active")) {
			StatFs stat = new StatFs("/data");
			long availBlocks = stat.getAvailableBlocks();
			long blockSize = stat.getBlockSize();
			if (availBlocks * blockSize < MEM_THRESHOLD)
				return true;
		}
		return false;
	}
}
