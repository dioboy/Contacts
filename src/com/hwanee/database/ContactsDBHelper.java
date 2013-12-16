package com.hwanee.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class ContactsDBHelper extends SQLiteOpenHelper {
	final String[] mDefaultGroups = { "가족", "친구", "직장" };
	final static String CONTACTSDATANAME = "contacts.db";
	final static int CONTACTSDATAVERSION = 1;
	final static String QUERY_CHECK_START = "SELECT name FROM sqlite_master WHERE type='table' AND name='";
	final static String QUERY_CHECK_END = "'";

	public ContactsDBHelper(Context context) {
		super(context, CONTACTSDATANAME, null, CONTACTSDATAVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Cursor contactsCursor = db.rawQuery(
				QUERY_CHECK_START + ContactsDataBaseMetaData.TABLENAME_CONTACTS
						+ QUERY_CHECK_END, null);
		Cursor groupCursor = db.rawQuery(QUERY_CHECK_START
				+ ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER
				+ QUERY_CHECK_END, null);

		try {
			if (contactsCursor.getCount() == 0) {
				db.execSQL("CREATE TABLE "
						+ ContactsDataBaseMetaData.TABLENAME_CONTACTS + " ("
						+ ContactsDataBaseMetaData.DB_COLUMN_ID
						+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ ContactsDataBaseMetaData.DB_COLUMN_NAME + " TEXT, "
						+ ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME
						+ " INTEGER, "
						+ ContactsDataBaseMetaData.DB_COLUMN_MOBILE + " TEXT, "
						+ ContactsDataBaseMetaData.DB_COLUMN_PHONE + " TEXT, "
						+ ContactsDataBaseMetaData.DB_COLUMN_EMAIL + " TEXT, "
						+ ContactsDataBaseMetaData.DB_COLUMN_ADDRESS
						+ " TEXT);");
			}
			if (groupCursor.getCount() == 0) {
				db.execSQL("CREATE TABLE "
						+ ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER + " ("
						+ ContactsDataBaseMetaData.DB_COLUMN_ID
						+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME
						+ " TEXT, "
						+ ContactsDataBaseMetaData.DB_COLUMN_GROUPS_DELETE_FLAG
						+ " INTEGER);");
			}
		} catch (Exception e) {
			Log.e("contacts", "ContactsDBHelper onCreate error");
		} finally {
			contactsCursor.close();
			groupCursor.close();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "
				+ ContactsDataBaseMetaData.TABLENAME_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS "
				+ ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER);

		onCreate(db);
	}
}