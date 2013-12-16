package com.hwanee.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ContactsProvider extends ContentProvider {
	static ContactsDBHelper mContactsHelper = null;
	static SQLiteDatabase mContactsDB = null;
	public UriMatcher sURIMatcher = null;

	@Override
	public boolean onCreate() {
		setURIMatcher();
		mContactsHelper = new ContactsDBHelper(getContext());
		try {
			mContactsDB = mContactsHelper.getWritableDatabase();
		} catch (SQLiteCantOpenDatabaseException e) {
			mContactsDB = null;
		}
		return (mContactsDB == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		switch (sURIMatcher.match(uri)) {
		case ContactsDataBaseMetaData.DBTYPE_CONTACTSFOLDER:
		case ContactsDataBaseMetaData.DBTYPE_DBTYPE_CONTACTSFOLDERID:
			builder.setTables(ContactsDataBaseMetaData.TABLENAME_CONTACTS);
			break;
		case ContactsDataBaseMetaData.DBTYPE_GROUPFOLDER:
		case ContactsDataBaseMetaData.DBTYPE_GROUPFOLDERID:
			builder.setTables(ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER);
			break;
		default:
			return null;
		}

		if (mContactsDB == null)
			return null;

		Cursor cursor = builder.query(mContactsDB, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int nRes = 0;
		nRes = mContactsDB.delete(getTableNameByUri(uri), selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return nRes;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (mContactsDB == null)
			return null;

		long rowID = mContactsDB.insert(getTableNameByUri(uri), null, values);
		getContext().getContentResolver().notifyChange(uri, null);

		return ContentUris.withAppendedId(uri, rowID);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int rowID = 0;
		rowID = mContactsDB.update(getTableNameByUri(uri), values, selection,
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return rowID;
	}
	

	public void setURIMatcher() {
		if (sURIMatcher != null) {
			return;
		}

		ContactsDataBaseMetaData.AUTHORITY = getContext().getPackageName()
				+ ".ContactsProvider";
		sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sURIMatcher.addURI(ContactsDataBaseMetaData.AUTHORITY,
				ContactsDataBaseMetaData.TABLENAME_CONTACTS,
				ContactsDataBaseMetaData.DBTYPE_CONTACTSFOLDER);
		sURIMatcher.addURI(ContactsDataBaseMetaData.AUTHORITY,
				ContactsDataBaseMetaData.TABLENAME_CONTACTS + "/#",
				ContactsDataBaseMetaData.DBTYPE_DBTYPE_CONTACTSFOLDERID);
		sURIMatcher.addURI(ContactsDataBaseMetaData.AUTHORITY,
				ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER,
				ContactsDataBaseMetaData.DBTYPE_GROUPFOLDER);
		sURIMatcher.addURI(ContactsDataBaseMetaData.AUTHORITY,
				ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER + "/#",
				ContactsDataBaseMetaData.DBTYPE_GROUPFOLDERID);
	}

	public String getTableNameByUri(Uri uri) {
		String table_name = null;
		switch (sURIMatcher.match(uri)) {
		case ContactsDataBaseMetaData.DBTYPE_CONTACTSFOLDER:
		case ContactsDataBaseMetaData.DBTYPE_DBTYPE_CONTACTSFOLDERID:
			table_name = ContactsDataBaseMetaData.TABLENAME_CONTACTS;
			break;
		case ContactsDataBaseMetaData.DBTYPE_GROUPFOLDER:
		case ContactsDataBaseMetaData.DBTYPE_GROUPFOLDERID:
			table_name = ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER;
			break;
		default:
			throw new IllegalArgumentException("Unknown Uri: " + uri);
		}
		return table_name;
	}
}
