package com.hwanee.data;

import android.content.Context;

import com.delegate.database.DatabaseMaster;

public class DBWrapper extends DatabaseMaster {

	private static DBWrapper mDBWrapper = null;

	public static DBWrapper getIstance() {
		if (mDBWrapper == null) {
			mDBWrapper = new DBWrapper();
		}
		return mDBWrapper;
	}

	@Override
	protected int onUpdate(Context context) {
		// TODO Auto-generated method stub
		return 0;
	}

}
