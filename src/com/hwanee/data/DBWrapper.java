package com.hwanee.data;

import android.database.sqlite.SQLiteDatabase;

import com.representative.database.DatabaseMaster;

public class DBWrapper extends DatabaseMaster {

	private static DBWrapper mDBWrapper = null;

	public static DBWrapper getIstance() {
		if (mDBWrapper == null) {
			mDBWrapper = new DBWrapper();
		}
		return mDBWrapper;
	}

	@Override
	protected int onUpdate(String sql) {
		// TODO Auto-generated method stub
		return 0;
	}

}
