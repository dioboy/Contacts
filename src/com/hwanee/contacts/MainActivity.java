package com.hwanee.contacts;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.hwanee.data.ContactsData;
import com.hwanee.data.DBWrapper;
import com.hwanee.data.DefaultData;
import com.representative.database.Column;
import com.representative.database.DatabaseInfo;

public class MainActivity extends TabActivity {
	private Dialog mDialog = null;
	TabHost mTabHost = null;
	private Button mSaveBtn = null;
	private Button mCancelBtn = null;
	private EditText mInputGroupName = null;
	private ArrayList<Column> mContactsColumn = new ArrayList<Column>();
	private ArrayList<Column> mGroupsColumn = new ArrayList<Column>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initDatabase();
		mTabHost = getTabHost();
		LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,
				mTabHost.getTabContentView(), true);

		setTab(ContactsListActivity.class, R.string.contacts,
				R.drawable.profile);
		setTab(ContactsGroupActivity.class, R.string.group, R.drawable.group);
		setTab(ConfigureActivity.class, R.string.setting, R.drawable.configure);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public void setTab(Class classItem, int name, int icon) {
		Intent intent = new Intent().setClass(this, classItem);
		TabSpec tabSpec = mTabHost
				.newTabSpec(getString(name))
				.setIndicator(getString(name), getResources().getDrawable(icon))
				.setContent(intent);
		mTabHost.addTab(tabSpec);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.menu_item_add_contacts) {
			Intent intent = new Intent(getApplicationContext(),
					AddContactsActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.menu_item_add_group) {
			createAddDialog();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private Dialog createAddDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mInputGroupName = null;
		}

		mDialog = new Dialog(this);
		mDialog.setCanceledOnTouchOutside(true);

		try {
			mDialog.setContentView(R.layout.add_group_dialog);
		} catch (InflateException e) {

		}
		mDialog.setTitle(R.string.add_group);
		mInputGroupName = (EditText) mDialog.findViewById(R.id.AddGroupInput);
		mSaveBtn = (Button) mDialog.findViewById(R.id.AddGroupSaveBtn);
		if (mSaveBtn != null) {
			mSaveBtn.setOnClickListener(mSaveClickListener);
		}
		mCancelBtn = (Button) mDialog.findViewById(R.id.AddGroupCancelBtn);
		if (mCancelBtn != null) {
			mCancelBtn.setOnClickListener(mCancelClickListener);
		}

		return mDialog;
	}

	OnClickListener mSaveClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int result = DatabaseInfo.FAILURE;
			String groupName = null;
			if (mInputGroupName != null) {
				groupName = mInputGroupName.getText().toString();
			}

			if (groupName != null) {
				ContentValues values = new ContentValues();
				values.put(ContactsData.CONTACT_GROUP_KEY, groupName);
				values.put(ContactsData.CONTACT_DEFAULT_GROUP_KEY, 1);
				result = DBWrapper.getIstance().insertData(
						ContactsData.GROUPS_TABLE, values);
			}
			showMsg(result);
			if (mDialog != null) {
				mDialog.dismiss();
			}
		}
	};

	OnClickListener mCancelClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mDialog != null) {
				mDialog.dismiss();
			}

		}
	};

	private void initDatabase() {
		String path = Environment.getExternalStorageDirectory().getPath();
		DBWrapper.getIstance().setDBPath(path);
		int result = DBWrapper.getIstance().openOrCreateDB(this,
				ContactsData.DATABASE_FILE_NAME);
		if (result == DatabaseInfo.SUCCESS) {
			Cursor cursor = DBWrapper.getIstance().getTableList();
			if (cursor == null || cursor.getCount() > 2) {
				initColumnArrayList();
				DBWrapper.getIstance().creatTable(
						ContactsData.CONTACTS_TABLE, mContactsColumn);
				DBWrapper.getIstance().creatTable(
						ContactsData.GROUPS_TABLE, mGroupsColumn);
				DefaultData.setDefaultGroups();
				DefaultData.setDefaultContacts();
			}

			if (cursor != null) {
				cursor.close();
			}
		}
	}

	private void initColumnArrayList() {
		mContactsColumn.add(new Column(ContactsData.CONTACT_ID_KEY,
				DatabaseInfo.INTEGER_TYPE, true));
		mContactsColumn.add(new Column(ContactsData.CONTACT_NAME_KEY,
				DatabaseInfo.TEXT_TYPE, false));
		mContactsColumn.add(new Column(ContactsData.CONTACT_GROUP_KEY,
				DatabaseInfo.TEXT_TYPE, false));
		mContactsColumn.add(new Column(ContactsData.CONTACT_MOBILE_KEY,
				DatabaseInfo.TEXT_TYPE, false));
		mContactsColumn.add(new Column(ContactsData.CONTACT_PHONE_KEY,
				DatabaseInfo.TEXT_TYPE, false));
		mContactsColumn.add(new Column(ContactsData.CONTACT_EMAIL_KEY,
				DatabaseInfo.TEXT_TYPE, false));
		mContactsColumn.add(new Column(ContactsData.CONTACT_ADDRESS_KEY,
				DatabaseInfo.TEXT_TYPE, false));
		mGroupsColumn.add(new Column(ContactsData.CONTACT_GROUP_ID_KEY,
				DatabaseInfo.INTEGER_TYPE, true));
		mGroupsColumn.add(new Column(ContactsData.CONTACT_GROUP_KEY,
				DatabaseInfo.TEXT_TYPE, false));
		mGroupsColumn.add(new Column(ContactsData.CONTACT_DEFAULT_GROUP_KEY,
				DatabaseInfo.INTEGER_TYPE, false));
	}

	private void showMsg(int result) {
		int msg = R.string.save_failed;
		if (result == DatabaseInfo.SUCCESS) {
			msg = R.string.save_successful;
		}
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		super.onDestroy();
	}
}
