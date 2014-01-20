package com.delegate.database;

public class Column {
	private String mName = null;
	private String mType = null;
	private int mIsPrimaryKey = DatabaseInfo.NOT_PRIMARY_KEY;
	
	public Column() {

	}

	public Column(String name, String attr) {
		mName = name;
		mType = attr;
		mIsPrimaryKey = DatabaseInfo.NOT_PRIMARY_KEY;
	}

	public Column(String name, String attr, int pk) {
		mName = name;
		mType = attr;
		mIsPrimaryKey = pk;
	}
	
	public String getName() {
		return mName;
	}

	public String getType() {
		return mType;
	}

	public int isPrimaryKey() {
		return mIsPrimaryKey;
	}

	public void setName(String name) {
		mName = name;
	}

	public void setType(String attr) {
		mType = attr;
	}

	public void setPrimaryKey(int value) {
		mIsPrimaryKey = value;
	}
	public String getSQL() {
		StringBuilder sql = new StringBuilder();
		sql.append(mName);
		sql.append(" ");
		sql.append(mType);
		if (mIsPrimaryKey == DatabaseInfo.PRIMARY_KEY) {
			sql.append(" ");
			sql.append("PRIMARY KEY AUTOINCREMENT");
		}
		return sql.toString();
	}
}
