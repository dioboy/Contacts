package com.hwanee.data;

import android.content.ContentValues;

import com.delegate.database.DatabaseInfo;

public class DefaultData {
	public static int[] mDefaultGroup = { 0, 1, 1, 1 };
	public static String[] mGroupName = new String[] { "������", "����", "ģ��", "����", };
	public static String[] mName = new String[] { "ȫ�浿", "��浿", "��ö��", "�迵��",
			"�谩��", "��ϰ�", "�ڴ��", "�迤��", "�����", "ȫ��" };
	public static String[] mGroup = new String[] { "ģ��", "ģ��", "����", "����",
			"����", "ģ��", "ģ��", "ģ��", "�׷� ������", "�׷� ������" };
	public static String[] mMobile = new String[] { "01034345566",
			"0118265809", "0165458907", "01023267641", "01045448981",
			"01023456789", "01010203879", "01087650987", "01008649753", "" };
	public static String[] mPhone = new String[] { "", "021323434", "",
			"054343949", "051343633", "0263332323", "", "", "", "" };
	public static String[] mEmail = new String[] { "gil@gmail.com",
			"gogd@gmail.com", "chul@gmail.com", "kyh@naver.com",
			"kks@hanmail.net", "k1212@gmail.com", "parkds@gmail.com",
			"klg@naver.com", "dbkang@hanmail.com", "hongu@gmail.com" };
	public static String[] mAddress = new String[] { "����� ������ ������ 11����",
			"����� ������", "��⵵ �Ȼ��", "��� ���׽�", "�λ�� �ؿ�뱸", "����� ��õ�� ���굿",
			"�뱸������ �ϱ�", "����� ��õ�� ���굿", "��⵵ �ϻ��", "��õ��" };

	public static void setDefaultGroups() {
		ContentValues[] values = new ContentValues[mGroupName.length];
		if (DBWrapper.getIstance().beginTransaction() == DatabaseInfo.SUCCESS) {
			for (int i = 0; i < mGroupName.length; i++) {
				values[i] = new ContentValues();
				values[i].put(ContactsData.CONTACT_GROUP_KEY, mGroupName[i]);
				values[i].put(ContactsData.CONTACT_DEFAULT_GROUP_KEY,
						mDefaultGroup[i]);
			}
			if (DBWrapper.getIstance().insertData(
					ContactsData.GROUPS_TABLE, values) == DatabaseInfo.SUCCESS) {
				DBWrapper.getIstance().setTransactionSuccessful();
			}
		}
		DBWrapper.getIstance().endTransaction();
	}

	public static void setDefaultContacts() {
		ContentValues[] values = new ContentValues[mName.length];
		if (DBWrapper.getIstance().beginTransaction() == DatabaseInfo.SUCCESS) {
			for (int i = 0; i < mName.length; i++) {
				values[i] = new ContentValues();
				values[i].put(ContactsData.CONTACT_NAME_KEY, mName[i]);
				values[i].put(ContactsData.CONTACT_GROUP_KEY, mGroup[i]);
				values[i].put(ContactsData.CONTACT_MOBILE_KEY, mMobile[i]);
				values[i].put(ContactsData.CONTACT_PHONE_KEY, mPhone[i]);
				values[i].put(ContactsData.CONTACT_EMAIL_KEY, mEmail[i]);
				values[i].put(ContactsData.CONTACT_ADDRESS_KEY, mAddress[i]);
			}

			if (DBWrapper.getIstance().insertData(
					ContactsData.CONTACTS_TABLE, values) == DatabaseInfo.SUCCESS) {
				DBWrapper.getIstance().setTransactionSuccessful();
			}
		}
		DBWrapper.getIstance().endTransaction();
	}
}
