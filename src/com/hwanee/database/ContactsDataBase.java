package com.hwanee.database;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

public class ContactsDataBase {
	// 연착처 추가
	public static boolean existContacts(Context context, String name,
			String groupsName, String mobile, String phone, String email,
			String address) {
		boolean bRet = false;
		Uri item_uri = getUriString(ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER);
		String selection = ContactsDataBaseMetaData.DB_COLUMN_NAME + "=? And "
				+ ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME + "=? And "
				+ ContactsDataBaseMetaData.DB_COLUMN_MOBILE + "=? And "
				+ ContactsDataBaseMetaData.DB_COLUMN_PHONE + "=? And "
				+ ContactsDataBaseMetaData.DB_COLUMN_EMAIL + "=? And "
				+ ContactsDataBaseMetaData.DB_COLUMN_ADDRESS + "=?";
		String[] selectionArg = new String[] { name, groupsName, mobile, phone,
				email, address };
		Cursor cursor = context.getContentResolver().query(item_uri,
				ContactsDataBaseMetaData.PROJECTION_CONTACTS, selection,
				selectionArg, ContactsDataBaseMetaData.SORT_ORDER_BY_NAME);
		if (cursor == null) {
			return false;
		}

		if (cursor.moveToFirst() == false) {
			cursor.close();
			return bRet;
		}
		if (cursor.getCount() != 0) {
			bRet = true;
		}
		cursor.close();
		return bRet;
	}

	public static int addContacts(Context context, String name,
			String groupsName, String mobile, String phone, String email,
			String address) {
		try {
			ContentValues cv = new ContentValues();
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_NAME, name);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME, groupsName);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_MOBILE, mobile);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_PHONE, phone);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_EMAIL, email);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_ADDRESS, address);
			Uri contactsUri = getUriString(ContactsDataBaseMetaData.TABLENAME_CONTACTS);
			context.getContentResolver().insert(contactsUri, cv);
			return ContactsDataBaseMetaData.DBStatus_Success;
		} catch (SQLiteException e) {
			if (ContactsDataBaseUtil.isLowMemory(e)) {
				return ContactsDataBaseMetaData.DBStatus_LowMemory;
			}
		}
		return ContactsDataBaseMetaData.DBStatus_Fail;
	}

	public static Cursor getContactsCursor(Context context) {
		Uri item_uri = getUriString(ContactsDataBaseMetaData.TABLENAME_CONTACTS);
		String selection = null;
		String[] selectionArg = null;
		Cursor tmpCursor = context.getContentResolver().query(item_uri,
				ContactsDataBaseMetaData.PROJECTION_CONTACTS, selection,
				selectionArg, null);
		if (tmpCursor == null) {
			return null;
		}
		if (tmpCursor.moveToFirst() == false) {
			tmpCursor.close();
			return null;
		}
		if (tmpCursor.getCount() == 0) {
			tmpCursor.close();
			return null;
		}
		return tmpCursor;
	}

	public static boolean deleteContacts(Context context, String id) {
		int result = -1;
		try {
			Uri contactsUri = getUriString(ContactsDataBaseMetaData.TABLENAME_CONTACTS);
			String selection = ContactsDataBaseMetaData.DB_COLUMN_ID
					+ "=?";
			String[] selectionArg = new String[] { id };
			result = context.getContentResolver().delete(contactsUri, selection,
					selectionArg);
		} catch (SQLiteException e) {
			if (ContactsDataBaseUtil.isLowMemory(e)) {
				return false;
			}
		}
		return (result == -1) ? false : true;
	}

	public static int updateContacts(Context context, int id, String name,
			String groupsName, String mobile, String phone, String email,
			String address) {
		int result = -1;
		try {
			Uri contactsUri = getUriString(ContactsDataBaseMetaData.TABLENAME_CONTACTS);
			String selection = null;
			String[] selectionArg = null;
			selection = ContactsDataBaseMetaData.DB_COLUMN_ID + "=?";
			selectionArg = new String[] { String.valueOf(id) };

			ContentValues cv = new ContentValues();
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_NAME, name);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME, groupsName);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_MOBILE, mobile);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_PHONE, phone);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_EMAIL, email);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_ADDRESS, address);
			result = context.getContentResolver().update(contactsUri, cv, selection,
					selectionArg);
			return (result == -1) ? ContactsDataBaseMetaData.DBStatus_Fail : ContactsDataBaseMetaData.DBStatus_Success;
		} catch (SQLiteException e) {
			if (ContactsDataBaseUtil.isLowMemory(e)) {
				return ContactsDataBaseMetaData.DBStatus_LowMemory;
			}
		}
		return ContactsDataBaseMetaData.DBStatus_Fail;
	}

	public static Cursor getSearchById(Context context, String id) {
		Uri item_uri = getUriString(ContactsDataBaseMetaData.TABLENAME_CONTACTS);
		String selection = ContactsDataBaseMetaData.DB_COLUMN_ID + "=?";
		String[] selectionArg = new String[] { id };
		Cursor cursor = context.getContentResolver().query(item_uri,
				ContactsDataBaseMetaData.PROJECTION_CONTACTS, selection, selectionArg,
				ContactsDataBaseMetaData.SORT_ORDER_BY_NAME);
		if (cursor == null) {
			return cursor;
		}

		if (cursor.moveToFirst() == false) {
			cursor.close();
			return cursor;
		}
		
		return cursor;
	}

	public static int getContactsCount(Context context) {
		int count = 0;
		Uri uri = getUriString(ContactsDataBaseMetaData.TABLENAME_CONTACTS);
		Cursor cursor = context.getContentResolver().query(uri,
				ContactsDataBaseMetaData.PROJECTION_CONTACTS, null, null, null);
		if (cursor == null) {
			return count;
		}

		if (cursor.moveToFirst() == false) {
			cursor.close();
			return count;
		}

		count = cursor.getCount();
		cursor.close();
		return count;
	}

	// 그룹 추가

	public static boolean existGroups(Context context, String groupsName) {
		boolean bRet = false;
		Uri item_uri = getUriString(ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER);
		String selection = ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME
				+ "=?";
		String[] selectionArg = new String[] { groupsName };
		Cursor cursor = context.getContentResolver().query(item_uri,
				ContactsDataBaseMetaData.PROJECTION_GROUPS, selection,
				selectionArg,
				ContactsDataBaseMetaData.SORT_ORDER_BY_GROUPS_NAME);
		if (cursor == null) {
			return false;
		}

		if (cursor.moveToFirst() == false) {
			cursor.close();
			return bRet;
		}
		if (cursor.getCount() != 0) {
			bRet = true;
		}
		cursor.close();
		return bRet;
	}

	public static int addGroups(Context context, String groupsName, int isDelete) {
		if (existGroups(context, groupsName)) {
			return ContactsDataBaseMetaData.DBStatus_AlreadyExist;
		}
		try {
			ContentValues cv = new ContentValues();
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME, groupsName);
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_GROUPS_DELETE_FLAG, isDelete);
			Uri contactsUri = getUriString(ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER);
			context.getContentResolver().insert(contactsUri, cv);
			return ContactsDataBaseMetaData.DBStatus_Success;
		} catch (SQLiteException e) {
			if (ContactsDataBaseUtil.isLowMemory(e)) {
				// internal memory full 상황에 대한 Application 별 처리 코드
				return ContactsDataBaseMetaData.DBStatus_LowMemory;
			}
		}
		return ContactsDataBaseMetaData.DBStatus_Fail;
	}

	public static Cursor getGroupsCursor(Context context) {
		Uri item_uri = getUriString(ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER);
		String selection = null;
		String[] selectionArg = null;
		Cursor tmpCursor = context.getContentResolver().query(item_uri,
				ContactsDataBaseMetaData.PROJECTION_GROUPS, selection,
				selectionArg, null);
		if (tmpCursor == null) {
			return null;
		}
		if (tmpCursor.moveToFirst() == false) {
			tmpCursor.close();
			return null;
		}
		if (tmpCursor.getCount() == 0) {
			tmpCursor.close();
			return null;
		}
		return tmpCursor;
	}

	public static boolean deleteGroups(Context context, int groupsId) {
		int deleteResult = -1;
		try {
			Uri contactsUri = getUriString(ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER);
			String selection = ContactsDataBaseMetaData.DB_COLUMN_ID
					+ "=?";
			String[] selectionArg = new String[] { String.valueOf(groupsId) };
			deleteResult = context.getContentResolver().delete(contactsUri,
					selection, selectionArg);
		} catch (SQLiteException e) {
			if (ContactsDataBaseUtil.isLowMemory(e)) {
				return false;
			}
		}
		return (deleteResult > 0) ? true : false;
	}

	public static int updateGroups(Context context, String groupsName,
			int groupsId) {
		try {
			Uri contactsUri = getUriString(ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER);
			String selection = null;
			String[] selectionArg = null;
			selection = ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME + "=?";
			selectionArg = new String[] { groupsName };

			ContentValues cv = new ContentValues();
			cv.put(ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME, groupsName);
			context.getContentResolver().delete(contactsUri, selection,
					selectionArg);
			return ContactsDataBaseMetaData.DBStatus_Success;
		} catch (SQLiteException e) {
			if (ContactsDataBaseUtil.isLowMemory(e)) {
				// internal memory full 상황에 대한 Application 별 처리 코드
				return ContactsDataBaseMetaData.DBStatus_LowMemory;
			}
		}
		return ContactsDataBaseMetaData.DBStatus_Fail;
	}

	public static int getGroupsCount(Context context) {
		int count = 0;
		Uri uri = getUriString(ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER);
		Cursor cursor = context.getContentResolver().query(uri,
				ContactsDataBaseMetaData.PROJECTION_GROUPS, null, null, null);
		if (cursor == null) {
			return count;
		}

		if (cursor.moveToFirst() == false) {
			cursor.close();
			return count;
		}

		count = cursor.getCount();
		cursor.close();
		return count;
	}
	
	public static Cursor getSearchByGroupName(Context context, String name) {
		Uri item_uri = getUriString(ContactsDataBaseMetaData.TABLENAME_CONTACTS);
		String selection = ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME + "=?";
		String[] selectionArg = new String[] { name };
		Cursor cursor = context.getContentResolver().query(item_uri,
				ContactsDataBaseMetaData.PROJECTION_CONTACTS, selection, selectionArg,
				ContactsDataBaseMetaData.SORT_ORDER_BY_NAME);
		if (cursor == null) {
			return cursor;
		}

		if (cursor.moveToFirst() == false) {
			cursor.close();
			return null;
		}
		
		return cursor;
	}
	
	public static String getGroupNameByGroupId(Context context, int id) {
		Uri item_uri = getUriString(ContactsDataBaseMetaData.TABLENAME_GROUPFOLDER);
		String selection = ContactsDataBaseMetaData.DB_COLUMN_ID + "=?";
		String[] selectionArg = new String[] { String.valueOf(id) };
		Cursor cursor = context.getContentResolver().query(item_uri,
				ContactsDataBaseMetaData.PROJECTION_GROUPS, selection, selectionArg,
				null);
		if (cursor == null) {
			return null;
		}

		if (cursor.moveToFirst() == false) {
			cursor.close();
			return null;
		}
		String name = cursor.getString(cursor.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME));
		cursor.close();
		if(name != null) {
			return name;
		}
		return null;
	}
	
	public static int getCountByGroupName(Context context, String name) {
		Uri item_uri = getUriString(ContactsDataBaseMetaData.TABLENAME_CONTACTS);
		String selection = ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME + "=?";
		String[] selectionArg = new String[] { name };
		Cursor cursor = context.getContentResolver().query(item_uri,
				ContactsDataBaseMetaData.PROJECTION_CONTACTS, selection, selectionArg,
				ContactsDataBaseMetaData.SORT_ORDER_BY_NAME);
		if (cursor == null) {
			return 0;
		}

		if (cursor.moveToFirst() == false) {
			cursor.close();
			return 0;
		}
		
		int count = cursor.getCount();
		cursor.close();
		
		return count;
	}
	
	public static void updateContactsAfterDeleteGroup(Context context,
			String groupName) {
		Uri item_uri = getUriString(ContactsDataBaseMetaData.TABLENAME_CONTACTS);
		String selection = ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME
				+ "=?";
		String[] selectionArg = new String[] { groupName };
		Cursor cursor = context.getContentResolver().query(item_uri,
				ContactsDataBaseMetaData.PROJECTION_CONTACTS, selection,
				selectionArg, ContactsDataBaseMetaData.SORT_ORDER_BY_NAME);
		if (cursor == null || !cursor.moveToFirst() || cursor.getCount() == 0) {
			return;
		}

		do {
			int id = cursor.getInt(cursor
					.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_ID));
			String name = cursor.getString(cursor
					.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_NAME));
			String mobile = cursor.getString(cursor
					.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_MOBILE));
			String phone = cursor.getString(cursor
					.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_PHONE));
			String email = cursor.getString(cursor
					.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_EMAIL));
			String address = cursor
					.getString(cursor
							.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_ADDRESS));

			updateContacts(context, id, name, "그룹 미지정", mobile, phone, email,
					address);
		} while (cursor.moveToNext());

		cursor.close();

	}

	public static void setDefaultGroups(Context context) {
		String[] name = new String[] {"그룹 미지정", "가족", "친구", "직장" };
		int[] isDelete = new int[] {0, 1, 1, 1};
		for (int i=0; i < name.length; i++) {
			addGroups(context, name[i], isDelete[i]);
		}
	}

	public static Uri getUriString(String tablename) {
		final Uri.Builder uribuilder = new Uri.Builder();
		uribuilder.scheme("content");
		uribuilder.authority(ContactsDataBaseMetaData.AUTHORITY);
		uribuilder.path(tablename);

		return uribuilder.build();
	}
}
