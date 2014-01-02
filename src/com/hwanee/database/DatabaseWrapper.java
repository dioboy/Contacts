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

	private static DatabaseWrapper mWrapper;

	private static final String NAME_KEY = "name";
	private static final String TYPE_KEY = "type";
	private static final String PK_KEY = "pk";
	private static final String NOT_NULL_KEY = "notnull";
	private static final String TABLE_NAME_PLACE = "###NAME###";
	private static final String TYPE_PLACE = "###TYPE###";
	private static final String COLUMNS_PLACE = "###COLUMNS###";
	private static final String VALUES_PLACE = "###VALUES###";
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
	private static final String INSERT_INTO_TABLE = "insert into "
			+ TABLE_NAME_PLACE + " (" + COLUMNS_PLACE + ") values ("
			+ VALUES_PLACE + ")";
	private static final String INSERT_INTO_TABLE_OLD = "INSERT INTO "
			+ TABLE_NAME_PLACE + "(" + COLUMNS_PLACE + ") SELECT "
			+ COLUMNS_PLACE + " FROM " + TABLE_NAME_PLACE + "_old";
	private SQLiteDatabase mDB;

	public static DatabaseWrapper getWrapper() {
		if (mWrapper == null) {
			mWrapper = new DatabaseWrapper();
		}
		return mWrapper;
	}

	public DatabaseWrapper() {

	}

	public boolean openOrCreateDB(Context context, String name) {
		try {
			mDB = context.openOrCreateDatabase(name,
					context.MODE_WORLD_WRITEABLE, null);
		} catch (SQLException e) {
			return false;
		}
		if (mDB == null) {
			return false;
		}
		return false;
	}

	public boolean deleteDB(Context context, String dbName) {
		String path = DatabaseUtil.getDBPath(context) + dbName;
		if (path != null) {
			File file = new File(path);
			if (file != null || file.exists()) {
				try {
					return SQLiteDatabase.deleteDatabase(file);
				} catch (SQLException e) {
				}
			}
		}
		return false;
	}

	public boolean creatTable(String table, ArrayList<Column> values)
			throws SQLException {
		if (mDB == null) {
			return false;
		}

		if (existsTable(table)) {
			return false;
		}
		String sql = CREATE_TABLE.replace(TABLE_NAME_PLACE, table);
		ArrayList<String> column = new ArrayList<String>();
		for (int i = 0; i < values.size(); i++) {
			String tmp = values.get(i).getSQL();
			if (tmp != null) {
				column.add(tmp);
			}
		}
		sql += TextUtils.join(", ", column) + ");";
		try {
			mDB.execSQL(sql);
		} catch (SQLException e) {
			return false;
		}
		return true;
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
			String name = cursor.getString(cursor.getColumnIndex("name"));
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
					.getColumnIndex(NAME_KEY));
			String column_type = cursor.getString(cursor
					.getColumnIndex(TYPE_KEY));
			boolean column_pk = DatabaseUtil.converIntToBoolean(cursor
					.getInt(cursor.getColumnIndex(PK_KEY)));
			boolean column_notnull = DatabaseUtil.converIntToBoolean(cursor
					.getInt(cursor.getColumnIndex(NOT_NULL_KEY)));
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
			columns.add(cursor.getString(cursor.getColumnIndex(NAME_KEY)));
		}
		cursor.close();

		return columns;
	}

	public boolean deleteTable(String tableName) {
		if (mDB == null) {
			return false;
		}
		try {
			mDB.execSQL(DROP_TABLE + tableName);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public boolean addColumn(String tableName, String name, String type) {
		if (mDB == null || tableName == null || name == null && type == null) {
			return false;
		}
		String sql = ALTER_TABLE_ADD_COLUMN
				.replace(TABLE_NAME_PLACE, tableName);
		sql = sql.replace(COLUMNS_PLACE, name);
		sql = sql.replace(TYPE_PLACE, type);
		try {
			mDB.execSQL(sql);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public boolean deleteColumn(String tableName, String[] colsToRmove) {
		ArrayList<String> updatedTableColumns = getTableColumnsName(tableName);
		ArrayList<String> updatedValues = new ArrayList<String>();
		ArrayList<Column> oldTableColumns = getColumnType(tableName);
		updatedTableColumns.removeAll(Arrays.asList(colsToRmove));
		String columnsSeperated = TextUtils.join(",", updatedTableColumns);
		try {
			mDB.execSQL(ALTER_TABLE_RENAME.replace(TABLE_NAME_PLACE, tableName)
					+ tableName + "_old;");
		} catch (SQLException e) {
			return false;
		}
		for (int i = 0; i < updatedTableColumns.size(); i++) {
			for (int j = 0; j < oldTableColumns.size(); j++) {
				if (updatedTableColumns.get(i).equals(
						oldTableColumns.get(j).getName())) {
					updatedValues.add(oldTableColumns.get(j).getSQL());
				}
			}
		}

		String createTable = CREATE_TABLE + TextUtils.join(", ", updatedValues)
				+ ")";
		try {
			mDB.execSQL(createTable);
		} catch (SQLException e) {
			return false;
		}
		String insertSQL = INSERT_INTO_TABLE_OLD.replace(TABLE_NAME_PLACE,
				tableName);
		insertSQL = insertSQL.replaceAll(COLUMNS_PLACE, columnsSeperated);
		insertSQL = insertSQL.replace(tableName, tableName);
		mDB.beginTransaction();
		try {
			mDB.execSQL(insertSQL);
			mDB.setTransactionSuccessful();
		} catch (IllegalStateException e) {
			return false;
		} finally {
			mDB.endTransaction();
		}
		try {
			mDB.execSQL(DROP_TABLE.replace(TABLE_NAME_PLACE, tableName)
					+ "_old;");
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public boolean insertData(String tableName, ContentValues values) {
		long result = -1;
		if (mDB == null) {
			return false;
		}
		try {
			mDB.beginTransaction();
			result = mDB.insert(tableName, null, values);
			mDB.setTransactionSuccessful();

		} catch (SQLException e) {
			return false;
		} catch (IllegalStateException e) {
			return false;
		} finally {
			mDB.endTransaction();
		}
		if (result == -1) {
			return false;
		}

		return true;
	}

	public boolean updataData(String tableName, String[] column,
			String[] columData, ContentValues values) {
		if (mDB == null) {
			return false;
		}

		String where = convertArrayToWhereClause(column, columData);
		if (where == null) {
			return false;
		}
		try {
			return mDB.update(tableName, values, where, null) > 0;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean deleteData(String tableName, String column, String columData) {
		if (mDB == null) {
			return false;
		}
		try {
			return mDB.delete(tableName, column + "=" + columData, null) > 0;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean deleteData(String tableName, String[] column,
			String[] columData) throws SQLException {
		if (mDB == null) {
			return false;
		}
		String where = convertArrayToWhereClause(column, columData);
		if (where == null) {
			return false;
		}
		try {
			return mDB.delete(tableName, where, null) > 0;
		} catch (SQLException e) {
			return false;
		}
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

	public String convertArrayToWhereClause(String[] column, String[] values) {
		if (column == null || column.length == 0 || values == null
				|| values.length == 0) {
			return null;
		}
		String whereClause = "";
		int count = column.length;
		;

		if (count != values.length && count > values.length) {
			count = values.length;
		}

		for (int i = 0; i < count; i++) {
			whereClause += column[i] + "=" + values[i];
			if (i < count - 1) {
				whereClause += " AND ";
			}
		}

		return whereClause;
	}

	public String convertArrayToSelectionStr(String[] selection) {
		if (selection == null || selection.length == 0) {
			return null;
		}
		String whereClause = "";
		int count = selection.length;
		;

		for (int i = 0; i < count; i++) {
			whereClause += selection[i] + "= ?";
			if (i < count - 1) {
				whereClause += " AND ";
			}
		}

		return whereClause;
	}

	public int getTableItemCount(String tableName) {
		Cursor cursor = null;
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
}
