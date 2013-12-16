package com.hwanee.database;

public class ContactsDataBaseMetaData {
	public static String AUTHORITY = "";
	public static final String TABLENAME_CONTACTS = "contacts";
	public static final String TABLENAME_GROUPFOLDER = "groupsfolder";
	
	public static String DB_COLUMN_GROUPS_ID = "groupsfolderid";
	public static String DB_COLUMN_GROUPS_NAME = "groupsname";
	public static String DB_COLUMN_GROUPS_DELETE_FLAG = "groupsflag";
	
	public static String DB_COLUMN_ID = "_id";
	public static String DB_COLUMN_NAME = "name";
	public static String DB_COLUMN_MOBILE = "mobile";
	public static String DB_COLUMN_PHONE = "phone";
	public static String DB_COLUMN_EMAIL = "email";
	public static String DB_COLUMN_ADDRESS = "address";

	public static String SORT_ORDER_BY_GROUPS_NAME = "groupsname DESC";	
	public static String SORT_ORDER_BY_NAME = "name DESC";

	public static final int DBTYPE_CONTACTSFOLDER = 1;
	public static final int DBTYPE_DBTYPE_CONTACTSFOLDERID = 2;
	public static final int DBTYPE_GROUPFOLDER = 3;
	public static final int DBTYPE_GROUPFOLDERID = 4;

	public static final int DBStatus_Fail = 0;
	public static final int DBStatus_Success = 1;
	public static final int DBStatus_AlreadyExist = 2;
	public static final int DBStatus_LowMemory = 3;

	public static final String[] PROJECTION_CONTACTS = new String[] {
			DB_COLUMN_ID, DB_COLUMN_NAME, DB_COLUMN_GROUPS_NAME, DB_COLUMN_MOBILE,
			DB_COLUMN_PHONE, DB_COLUMN_EMAIL, DB_COLUMN_ADDRESS };
	public static final String[] PROJECTION_GROUPS = new String[] {
			DB_COLUMN_ID, DB_COLUMN_GROUPS_NAME, DB_COLUMN_GROUPS_DELETE_FLAG };
}
