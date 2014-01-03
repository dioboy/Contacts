package com.hwanee.database;

public class DatabaseInfo {
	public static String INTEGER_TYPE = "INTEGER";
	public static String TEXT_TYPE = "TEXT";
	public static String REAL_TYPE = "REAL";
	public static String BLOB_TYPE = "BLOB";
	
	public static final String NAME_KEY = "name";
	public static final String TYPE_KEY = "type";
	public static final String PK_KEY = "pk";
	public static final String NOT_NULL_KEY = "notnull";
	
	//Exception Code
	public static int SUCCESS = 1;
	public static int FAILURE = 2;
	public static int DATABASE_NOT_OPEN = 3;
	public static int TABLE_NAME_ERROR = 4;
	public static int DATABASE_FILE_NAME_ERROR = 5;
	public static int VALUES_ERROR = 5;
	public static int ILLEGAL_STATE_EXCEPTION = 1000;
	public static int SQLITEEXCEPTION = 1001;
	public static int SQLEXCEPTION = 1002;
}