package com.hwanee.database;

public class Column {
	private String mName = null;
	private String mType = null;
	private boolean mIsPrimaryKey = false;
	private boolean mIsNotNull = false;

	public Column() {

	}

	public Column(String name, String attr) {
		mName = name;
		mType = attr;
		mIsPrimaryKey = false;
	}

	public Column(String name, String attr, boolean pk) {
		mName = name;
		mType = attr;
		mIsPrimaryKey = pk;
	}

	public Column(String name, String attr, boolean pk, boolean notnull) {
		mName = name;
		mType = attr;
		mIsPrimaryKey = pk;
		mIsNotNull = notnull;
	}

	public String getName() {
		return mName;
	}

	public String getType() {
		return mType;
	}

	public boolean isPrimaryKey() {
		return mIsPrimaryKey;
	}

	public boolean isNotNull() {
		return mIsNotNull;
	}

	public void setName(String name) {
		mName = name;
	}

	public void setType(String attr) {
		mType = attr;
	}

	public void setPrimaryKey(boolean value) {
		mIsPrimaryKey = value;
	}

	public void setNotNull(boolean value) {
		mIsNotNull = value;
	}

	public String getSQL() {
		StringBuilder sql = new StringBuilder();
		sql.append(mName);
		sql.append(" ");
		sql.append(mType);
		if (mIsPrimaryKey) {
			sql.append(" ");
			sql.append("PRIMARY KEY AUTOINCREMENT");
		}
		return sql.toString();
	}
}
