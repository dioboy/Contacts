package com.hwanee.data;

import android.content.ContentValues;
import android.content.Context;

import com.hwanee.database.DatabaseInfo;
import com.hwanee.database.DatabaseWrapper;


public class DefaultData {
	public static int[] mDefaultGroup = {0, 1, 1, 1};
	public static String[] mGroupName = new String[]{
		"������",
		"����",
		"ģ��",
		"����",
		};
	public static String[] mName = new String[] {
		"ȫ�浿",
		"��浿",
		"��ö��",
		"�迵��",
		"�谩��",
		"��ϰ�",
		"�ڴ��",
		"�迤��",
		"�����",
		"ȫ��"
	};
	public static String[] mGroup = new String[] {
		"ģ��",
		"ģ��",
		"����",
		"����",
		"����",
		"ģ��",
		"ģ��",
		"ģ��",
		"�׷� ������",
		"�׷� ������"
		};
	public static String[] mMobile = new String[] {
		"01034345566",
		"0118265809",
		"0165458907",
		"01023267641",
		"01045448981",
		"01023456789",
		"01010203879",
		"01087650987",
		"01008649753",
		""
	};
	public static String[] mPhone = new String[] {
		"",
		"021323434",
		"",
		"054343949",
		"051343633",
		"0263332323",
		"",
		"",
		"",
		""
		};
	public static String[] mEmail = new String[] {
		"gil@gmail.com",
		"gogd@gmail.com",
		"chul@gmail.com",
		"kyh@naver.com",
		"kks@hanmail.net",
		"k1212@gmail.com",
		"parkds@gmail.com",
		"klg@naver.com",
		"dbkang@hanmail.com",
		"hongu@gmail.com"
		};
	public static String[] mAddress= new String[] {
		"����� ������ ������ 11����",
		"����� ������",
		"��⵵ �Ȼ��",
		"��� ���׽�",
		"�λ�� �ؿ�뱸",
		"����� ��õ�� ���굿",
		"�뱸������ �ϱ�",
		"����� ��õ�� ���굿",
		"��⵵ �ϻ��",
		"��õ��"
		};
	public static void setDefaultGroups() {
		for(int i=0; i<mGroupName.length; i++) {
			ContentValues values = new ContentValues();
			values.put(DatabaseInfo.CONTACT_GROUP_KEY, mGroupName[i]);
			values.put(DatabaseInfo.CONTACT_DEFAULT_GROUP_KEY, mDefaultGroup[i]);
			DatabaseWrapper.getWrapper().insertData(DatabaseInfo.GROUPS_TABLE, values);
		}
	}
	
	public static void setDefaultContacts() {
		for(int i=0; i<mName.length; i++) {
			ContentValues values = new ContentValues();
			values.put(DatabaseInfo.CONTACT_NAME_KEY, mName[i]);
			values.put(DatabaseInfo.CONTACT_GROUP_KEY, mGroup[i]);
			values.put(DatabaseInfo.CONTACT_MOBILE_KEY, mMobile[i]);
			values.put(DatabaseInfo.CONTACT_PHONE_KEY, mPhone[i]);
			values.put(DatabaseInfo.CONTACT_EMAIL_KEY, mEmail[i]);
			values.put(DatabaseInfo.CONTACT_ADDRESS_KEY, mAddress[i]);
			DatabaseWrapper.getWrapper().insertData(DatabaseInfo.CONTACTS_TABLE, values);
		}
	}
}
