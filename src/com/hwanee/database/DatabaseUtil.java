package com.hwanee.database;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;

public class DatabaseUtil {
	private static String DATABASE_FOLDER = "/databases/";

	public static boolean converIntToBoolean(int value) {
		return (value == 0) ? false : true;
	}

	public static String[] getDBFileList(Context context) {
		String path = getDBPath(context);
		File file = new File(path);

		if (file.exists()) {
			String[] list = file.list(mDBFilter);
			return list;
		}

		return null;
	}

	public static String getDBPath(Context context) {
		return context.getFilesDir().getParent() + DATABASE_FOLDER;
	}
	
	public static FilenameFilter mDBFilter = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String filename) {
			if (filename.endsWith(".db")) {
				return true;
			}
			return false;
		}
	};
}
