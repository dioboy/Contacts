package com.hwanee.database;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//import java.sql.SQLException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class DatabaseWrapper {
	private static final String TABLE_NAME_PLACE = "###NAME###";
	private static final String TYPE_PLACE = "###TYPE###";
	private static final String COLUMNS_PLACE = "###COLUMNS###";
	private static final String SELECT_ALL_TABLE = "SELECT name FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata' AND name !='sqlite_sequence'";
	private static final String PRAGMA = "PRAGMA table_info("
			+ TABLE_NAME_PLACE + ")";
	private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME_PLACE + " (";
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS";
	private static final String ALTER_TABLE_ADD_COLUMN = "ALTER TABLE "
			+ TABLE_NAME_PLACE + " ADD COLUMN " + COLUMNS_PLACE + " "
			+ TYPE_PLACE;
	private static final String ALTER_TABLE_RENAME = "ALTER TABLE "
			+ TABLE_NAME_PLACE + " RENAME TO ";
	private static final String SELECT_ALL_SQL = "SELECT * FROM "
			+ TABLE_NAME_PLACE;
	private static final String INSERT_INTO_TABLE_OLD = "INSERT INTO "
			+ TABLE_NAME_PLACE + "(" + COLUMNS_PLACE + ") SELECT "
			+ COLUMNS_PLACE + " FROM " + TABLE_NAME_PLACE + "_old";
	private SQLiteDatabase mDB;

	private static DatabaseWrapper mWrapper;

	public static DatabaseWrapper getWrapper() {
		if (mWrapper == null) {
			mWrapper = new DatabaseWrapper();
		}
		return mWrapper;
	}

	public DatabaseWrapper() {

	}

	public int openOrCreateDB(Context context, String name) {
		if (name == null) {
			return DatabaseInfo.DATABASE_FILE_NAME_ERROR;
		}
		try {
			mDB = context.openOrCreateDatabase(name,
					Context.MODE_WORLD_WRITEABLE, null);
		} catch (SQLException e) {
			return DatabaseInfo.SQLEXCEPTION;
		}
		if (mDB == null) {
			return DatabaseInfo.FAILURE;
		}
		return DatabaseInfo.SUCCESS;
	}

	public int deleteDB(Context context, String dbName) {
		String path = DatabaseUtil.getDBPath(context) + dbName;
		if (path != null) {
			File file = new File(path);
			if (file != null || file.exists()) {
				try {
					if (SQLiteDatabase.deleteDatabase(file)) {
						return DatabaseInfo.SUCCESS;
					}
				} catch (SQLException e) {
					return DatabaseInfo.SQLEXCEPTION;
				}
			}
		}
		return DatabaseInfo.FAILURE;
	}

	public int creatTable(String tableName, ArrayList<Column> values) {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}

		if (tableName == null) {
			return DatabaseInfo.TABLE_NAME_ERROR;
		}

		if (values == null || values.size() == 0) {
			return DatabaseInfo.VALUES_ERROR;
		}
		StringBuilder sql = new StringBuilder();
		sql.append(CREATE_TABLE.replace(TABLE_NAME_PLACE, tableName));
		ArrayList<String> column = new ArrayList<String>();
		for (int i = 0; i < values.size(); i++) {
			String tmp = values.get(i).getSQL();
			if (tmp != null) {
				column.add(tmp);
			}
		}
		sql.append(TextUtils.join(", ", column));
		sql.append(");");
		try {
			mDB.execSQL(sql.toString());
		} catch (SQLException e) {
			return DatabaseInfo.SQLEXCEPTION;
		}
		return DatabaseInfo.SUCCESS;
	}

	public boolean existsTable(String table) {
		Cursor cursor = getTableList();

		if (cursor == null) {
			return false;
		}

		if (!cursor.moveToFirst()) {
			return false;
		}

		do {
			String name = cursor.getString(cursor
					.getColumnIndex(DatabaseInfo.NAME_KEY));
			if (name.equals(table)) {
				if (cursor != null) {
					cursor.close();
				}

				return true;
			}
		} while (cursor.moveToNext());

		if (cursor != null) {
			cursor.close();
		}

		return false;
	}

	public Cursor getTableList() {
		Cursor cursor = null;
		if (mDB == null) {
			return null;
		}
		try {
			cursor = mDB.rawQuery(SELECT_ALL_TABLE, null);
		} catch (SQLException e) {
			return null;
		}

		if (cursor == null) {
			return null;
		}

		if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	public ArrayList<Column> getColumnType(String table) throws SQLException {
		if (mDB == null) {
			return null;
		}

		ArrayList<Column> list = new ArrayList<Column>();
		Cursor cursor = null;
		try {
			cursor = mDB
					.rawQuery(PRAGMA.replace(TABLE_NAME_PLACE, table), null);
		} catch (SQLException e) {
			return null;
		}

		if (cursor == null) {
			return null;
		}

		if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
			return null;
		}

		do {
			String column_name = cursor.getString(cursor
					.getColumnIndex(DatabaseInfo.NAME_KEY));
			String column_type = cursor.getString(cursor
					.getColumnIndex(DatabaseInfo.TYPE_KEY));
			boolean column_pk = DatabaseUtil.converIntToBoolean(cursor
					.getInt(cursor.getColumnIndex(DatabaseInfo.PK_KEY)));
			boolean column_notnull = DatabaseUtil.converIntToBoolean(cursor
					.getInt(cursor.getColumnIndex(DatabaseInfo.NOT_NULL_KEY)));
			list.add(new Column(column_name, column_type, column_pk,
					column_notnull));
		} while (cursor.moveToNext());

		cursor.close();

		return list;
	}

	public ArrayList<String> getTableColumnsName(String tableName) {
		if (mDB == null) {
			return null;
		}

		ArrayList<String> columns = new ArrayList<String>();
		String cmd = PRAGMA.replace(TABLE_NAME_PLACE, tableName);
		Cursor cursor = null;
		try {
			cursor = mDB.rawQuery(cmd, null);
		} catch (SQLException e) {
			return null;
		}
		while (cursor.moveToNext()) {
			columns.add(cursor.getString(cursor
					.getColumnIndex(DatabaseInfo.NAME_KEY)));
		}
		cursor.close();

		return columns;
	}

	public int deleteTable(String tableName) {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}
		try {
			mDB.execSQL(DROP_TABLE + tableName);
		} catch (SQLException e) {
			return DatabaseInfo.SQLEXCEPTION;
		}
		return DatabaseInfo.SUCCESS;
	}

	public int addColumn(String tableName, String name, String type) {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}

		if (tableName == null) {
			return DatabaseInfo.TABLE_NAME_ERROR;
		}
		if (name == null && type == null) {
			return DatabaseInfo.VALUES_ERROR;
		}
		String sql = ALTER_TABLE_ADD_COLUMN
				.replace(TABLE_NAME_PLACE, tableName);
		sql = sql.replace(COLUMNS_PLACE, name);
		sql = sql.replace(TYPE_PLACE, type);
		try {
			mDB.execSQL(sql);
		} catch (SQLException e) {
			return DatabaseInfo.SQLEXCEPTION;
		}
		return DatabaseInfo.SUCCESS;
	}

	public int deleteColumn(String tableName, String[] colsToRmove) {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}
		if (tableName == null) {
			return DatabaseInfo.TABLE_NAME_ERROR;
		}
		if (colsToRmove == null || colsToRmove.length == 0) {
			return DatabaseInfo.VALUES_ERROR;
		}

		int result = DatabaseInfo.SUCCESS;
		ArrayList<String> updatedTableColumns = getTableColumnsName(tableName);
		ArrayList<String> updatedValues = new ArrayList<String>();
		ArrayList<Column> oldTableColumns = getColumnType(tableName);
		updatedTableColumns.removeAll(Arrays.asList(colsToRmove));
		String columnsSeperated = TextUtils.join(",", updatedTableColumns);
		try {
			mDB.execSQL(ALTER_TABLE_RENAME.replace(TABLE_NAME_PLACE, tableName)
					+ tableName + "_old;");
		} catch (SQLException e) {
			result = DatabaseInfo.SQLEXCEPTION;
		}
		for (int i = 0; i < updatedTableColumns.size(); i++) {
			for (int j = 0; j < oldTableColumns.size(); j++) {
				if (updatedTableColumns.get(i).equals(
						oldTableColumns.get(j).getName())) {
					updatedValues.add(oldTableColumns.get(j).getSQL());
				}
			}
		}

		StringBuilder createTable = new StringBuilder();
		createTable.append(CREATE_TABLE.replace(TABLE_NAME_PLACE, tableName));
		createTable.append(TextUtils.join(", ", updatedValues));
		createTable.append(")");
		try {
			mDB.execSQL(createTable.toString());
		} catch (SQLException e) {
			result = DatabaseInfo.SQLEXCEPTION;
		}
		String insertSQL = INSERT_INTO_TABLE_OLD.replace(TABLE_NAME_PLACE,
				tableName);
		insertSQL = insertSQL.replaceAll(COLUMNS_PLACE, columnsSeperated);
		insertSQL = insertSQL.replace(tableName, tableName);
		mDB.beginTransaction();
		try {
			mDB.execSQL(insertSQL);
			mDB.setTransactionSuccessful();
		} catch (SQLException e) {
			result = DatabaseInfo.SQLEXCEPTION;
		} catch (IllegalStateException e) {
			result = DatabaseInfo.ILLEGAL_STATE_EXCEPTION;
		} finally {
			mDB.endTransaction();
		}
		if (result == DatabaseInfo.SUCCESS) {
			try {
				mDB.execSQL(DROP_TABLE.replace(TABLE_NAME_PLACE, tableName)
						+ "_old;");
			} catch (SQLException e) {
				result = DatabaseInfo.SQLEXCEPTION;
			}
		}
		return result;
	}

	public int insertData(String tableName, ContentValues values) {
		int result = DatabaseInfo.SUCCESS;
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}
		if (tableName == null) {
			return DatabaseInfo.TABLE_NAME_ERROR;
		}
		if (values == null) {
			return DatabaseInfo.VALUES_ERROR;
		}
		try {
			if (mDB.insertOrThrow(tableName, null, values) == -1) {
				result = DatabaseInfo.FAILURE;
			}
		} catch (SQLException e) {
			result = DatabaseInfo.SQLEXCEPTION;
		} catch (IllegalStateException e) {
			result = DatabaseInfo.ILLEGAL_STATE_EXCEPTION;
		}

		return result;
	}

	/*
	 * 다수의 데이터 저장용이며 해당 함수는 내부적으로 Transaction이 구현되어 잇지 않으므로 호출 전에
	 * beginTransaction을 해주고 끝나면 setTransactionSuccessful과 endTransaction을 호출할것
	 */
	public int insertData(String tableName, ContentValues[] values) {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}
		if (tableName == null) {
			return DatabaseInfo.TABLE_NAME_ERROR;
		}
		if (values == null) {
			return DatabaseInfo.VALUES_ERROR;
		}
		try {
			for (int i = 0; i < values.length; i++) {
				if (mDB.insert(tableName, null, values[i]) == -1) {
					return DatabaseInfo.FAILURE;
				}
			}
		} catch (SQLException e) {
			return DatabaseInfo.SQLEXCEPTION;
		} catch (IllegalStateException e) {
			return DatabaseInfo.ILLEGAL_STATE_EXCEPTION;
		}

		return DatabaseInfo.SUCCESS;
	}

	public int updataData(String tableName, String[] column,
			String[] columData, ContentValues values) {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}
		if (tableName == null) {
			return DatabaseInfo.TABLE_NAME_ERROR;
		}
		if (columData == null || columData == null || values == null) {
			return DatabaseInfo.VALUES_ERROR;
		}

		String where = convertArrayToWhereClause(column, columData);
		if (where == null) {
			return DatabaseInfo.FAILURE;
		}
		try {
			if (mDB.update(tableName, values, where, null) <= 0) {
				return DatabaseInfo.FAILURE;
			}
		} catch (SQLException e) {
			return DatabaseInfo.SQLEXCEPTION;
		}

		return DatabaseInfo.SUCCESS;
	}

	public int deleteData(String tableName, String column, String columData) {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}
		if (tableName == null) {
			return DatabaseInfo.TABLE_NAME_ERROR;
		}
		if (column == null || columData == null) {
			return DatabaseInfo.VALUES_ERROR;
		}
		try {
			StringBuilder whereClause = new StringBuilder();
			whereClause.append(column);
			whereClause.append("=");
			whereClause.append(columData);
			if (mDB.delete(tableName, whereClause.toString(), null) <= 0) {
				return DatabaseInfo.FAILURE;
			}
		} catch (SQLException e) {
			return DatabaseInfo.SQLEXCEPTION;
		}
		return DatabaseInfo.SUCCESS;
	}

	public int deleteData(String tableName, String[] column, String[] columData) {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}
		if (tableName == null) {
			return DatabaseInfo.TABLE_NAME_ERROR;
		}
		if (columData == null || columData == null) {
			return DatabaseInfo.VALUES_ERROR;
		}
		String where = convertArrayToWhereClause(column, columData);
		if (where == null) {
			return DatabaseInfo.FAILURE;
		}
		try {
			if (mDB.delete(tableName, where, null) <= 0) {
				return DatabaseInfo.FAILURE;
			}
		} catch (SQLException e) {
			return DatabaseInfo.SQLEXCEPTION;
		}

		return DatabaseInfo.SUCCESS;
	}

	public Cursor selectData(String tableName, String[] columns,
			String[] selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		if (mDB == null) {
			return null;
		}
		String select = convertArrayToSelectionStr(selection);
		Cursor cursor = null;
		try {
			cursor = mDB.query(tableName, columns, select, selectionArgs,
					groupBy, having, orderBy);
		} catch (SQLException e) {
			return null;
		}
		if (cursor == null) {
			return null;
		}

		if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
			return null;
		}

		return cursor;
	}

	public Cursor selectAllData(String tableName) {
		if (mDB == null || tableName == null) {
			return null;
		}

		Cursor cursor = null;
		try {
			cursor = mDB.rawQuery(
					SELECT_ALL_SQL.replace(TABLE_NAME_PLACE, tableName), null);
		} catch (SQLException e) {
			return null;
		}
		if (cursor == null) {
			return null;
		}

		if (cursor.getCount() == 0 || !cursor.moveToFirst()) {
			cursor.close();
			return null;
		}

		return cursor;
	}

	private String convertArrayToWhereClause(String[] column, String[] values) {
		if (column == null || column.length == 0 || values == null
				|| values.length == 0) {
			return null;
		}
		StringBuilder whereClause = new StringBuilder();
		int count = column.length;
		;

		if (count != values.length && count > values.length) {
			count = values.length;
		}

		for (int i = 0; i < count; i++) {
			whereClause.append(column[i]);
			whereClause.append("=");
			whereClause.append(values[i]);
			if (i < count - 1) {
				whereClause.append(" AND ");
			}
		}

		return whereClause.toString();
	}

	private String convertArrayToSelectionStr(String[] selection) {
		if (selection == null || selection.length == 0) {
			return null;
		}
		StringBuilder whereClause = new StringBuilder();
		int count = selection.length;

		for (int i = 0; i < count; i++) {
			whereClause.append(selection[i]);
			whereClause.append("= ?");
			if (i < count - 1) {
				whereClause.append(" AND ");
			}
		}

		return whereClause.toString();
	}

	public int getTableItemCount(String tableName) {
		Cursor cursor = null;
		if (mDB == null || tableName == null) {
			return -1;
		}
		try {
			cursor = mDB.rawQuery(
					SELECT_ALL_SQL.replace(TABLE_NAME_PLACE, tableName), null);
		} catch (SQLException e) {
			return -1;
		}

		if (cursor == null) {
			return -1;
		}

		if (!cursor.moveToFirst()) {
			return -1;
		}

		return cursor.getCount();
	}

	public int beginTransaction() {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}
		mDB.beginTransaction();
		return DatabaseInfo.SUCCESS;
	}

	public int setTransactionSuccessful() {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}
		mDB.setTransactionSuccessful();
		return DatabaseInfo.SUCCESS;
	}

	public int endTransaction() {
		if (mDB == null) {
			return DatabaseInfo.DATABASE_NOT_OPEN;
		}
		mDB.endTransaction();

		return DatabaseInfo.SUCCESS;
	}
}
