package com.hwanee.data;

public class ContactsData {
	public static final String DATABASE_NAME = "database_name";
	public static final String TABLE_NAME = "table_name";
	public static final String DATABASE_FILE_NAME = "database";
	public static final String CONTACTS_TABLE = "연락처";
	public static final String GROUPS_TABLE = "그룹";

	public static String CONTACT_ID_KEY = "_id";
	public static String CONTACT_NAME_KEY = "contact_name";
	public static String CONTACT_MOBILE_KEY = "contact_mobile";
	public static String CONTACT_PHONE_KEY = "contact_phone";
	public static String CONTACT_EMAIL_KEY = "contact_email";
	public static String CONTACT_ADDRESS_KEY = "contact_address";
	public static String CONTACT_GROUP_KEY = "contact_group";
	public static String CONTACT_GROUP_ID_KEY = "_id";
	public static String CONTACT_DEFAULT_GROUP_KEY = "contact_default_group";
	public static String[] CONTACT_COLUMN_LIST = { CONTACT_ID_KEY,
		CONTACT_NAME_KEY, CONTACT_GROUP_KEY, CONTACT_MOBILE_KEY,
		CONTACT_PHONE_KEY, CONTACT_EMAIL_KEY, CONTACT_ADDRESS_KEY };
public static String[] GROUPS_COLUMN_LIST = { CONTACT_GROUP_ID_KEY,
		CONTACT_GROUP_KEY };

}
