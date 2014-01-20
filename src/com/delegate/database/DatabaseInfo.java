package com.delegate.database;

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
	
	//PrimaryKey
	public static int NOT_PRIMARY_KEY = 0;
	public static int PRIMARY_KEY = 1;

	// Exception Code
	public static int SUCCESS = 1001;
	public static int FAILURE = 1002;
	public static int ERR_DB_NOT_OPEN = 1003;
	public static int ERR_TABLE_NAME = 1004;
	public static int ERR_DB_FILE_NAME = 1005;
	public static int ERR_VALUES = 1006;
	public static int ERR_DB_PATH = 1007;
	public static int ERR_DOUBLE_PK = 1008;
	public static int ERR_WHERE_CLAUSE = 1009;
	public static int ILLEGAL_STATE_EXCEPTION = 2000;
	public static int SQLITEEXCEPTION = 2001;
	public static int SQLEXCEPTION = 2002;
	
	//DB PATH FLAG
	public static int USE_APPDATA_PATH = 0;
	public static int USE_CUSTOM_PATH = 1;
}