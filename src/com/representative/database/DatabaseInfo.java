package com.representative.database;

public class DatabaseInfo {
	public static String INTEGER_TYPE = "INTEGER";
	public static String TEXT_TYPE = "TEXT";
	public static String REAL_TYPE = "REAL";
	public static String BLOB_TYPE = "BLOB";

	public static final String NAME_KEY = "name";
	public static final String TYPE_KEY = "type";
	public static final String PK_KEY = "pk";
	public static final String NOT_NULL_KEY = "notnull";

	public static final String INFO_TABLE = "info";
	public static final String INFO_ID = "_id";
	public static final String INFO_LEVEL = "level";

	// Exception Code
	public static int SUCCESS = 1;
	public static int FAILURE = 2;
	public static int ERR_DB_NOT_OPEN = 3;
	public static int ERR_TABLE_NAME = 4;
	public static int ERR_DB_FILE_NAME = 5;
	public static int ERR_VALUES = 6;
	public static int ERR_DB_PATH = 7;
	public static int ERR_DOUBLE_PK = 8;
	public static int ERR_WHERE_STR = 9;
	public static int ILLEGAL_STATE_EXCEPTION = 1000;
	public static int SQLITEEXCEPTION = 1001;
	public static int SQLEXCEPTION = 1002;
	
	//DB PATH FLAG
	public static int USE_APPDATA_PATH = 0;
	public static int USE_CUSTOM_PATH = 1;
}