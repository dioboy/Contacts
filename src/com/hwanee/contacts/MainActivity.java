package com.hwanee.contacts;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

import com.hwanee.data.DefaultData;
import com.hwanee.database.Column;
import com.hwanee.database.DatabaseInfo;
import com.hwanee.database.DatabaseWrapper;

public class MainActivity extends TabActivity {
	private static int DIALOG_ADD = 0;
	private Dialog mDialog = null;
	TabHost mTabHost = null;
	private Button mSaveBtn = null;
	private Button mCancelBtn = null;
	private EditText mInputGroupName = null;

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
			showDialog(DIALOG_ADD);
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_ADD) {
			return createAddDialog();
		}
		return super.onCreateDialog(id);
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
			boolean result = false;
			String groupName = null;
			if (mInputGroupName != null) {
				groupName = mInputGroupName.getText().toString();
			}

			if (groupName != null) {
				ContentValues values = new ContentValues();
				values.put(DatabaseInfo.CONTACT_GROUP_KEY, groupName);
				values.put(DatabaseInfo.CONTACT_DEFAULT_GROUP_KEY, 1);
				result = DatabaseWrapper.getWrapper().insertData(DatabaseInfo.GROUPS_TABLE, values);
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
	ArrayList<Column> mContactsColumn = new ArrayList<Column>();
	ArrayList<Column> mGroupsColumn = new ArrayList<Column>();

	private void initDatabase() {
		DatabaseWrapper.getWrapper().openOrCreateDB(this,
				DatabaseInfo.DATABASE_FILE_NAME);
		Cursor cursor = DatabaseWrapper.getWrapper().getTableList();
		if (cursor == null || cursor.getCount() == 0) {
			initColumnArrayList();
			DatabaseWrapper.getWrapper().creatTable(
					DatabaseInfo.CONTACTS_TABLE, mContactsColumn);
			DatabaseWrapper.getWrapper().creatTable(
					DatabaseInfo.GROUPS_TABLE, mGroupsColumn);
			DefaultData.setDefaultGroups();
			DefaultData.setDefaultContacts();
		}
	}

	private void initColumnArrayList() {
		mContactsColumn.add(new Column(DatabaseInfo.CONTACT_ID_KEY, DatabaseInfo.INTEGER_TYPE, true));
		mContactsColumn.add(new Column(DatabaseInfo.CONTACT_NAME_KEY, DatabaseInfo.TEXT_TYPE, false));
		mContactsColumn.add(new Column(DatabaseInfo.CONTACT_GROUP_KEY, DatabaseInfo.TEXT_TYPE, false));
		mContactsColumn.add(new Column(DatabaseInfo.CONTACT_MOBILE_KEY, DatabaseInfo.TEXT_TYPE, false));
		mContactsColumn.add(new Column(DatabaseInfo.CONTACT_PHONE_KEY, DatabaseInfo.TEXT_TYPE, false));
		mContactsColumn.add(new Column(DatabaseInfo.CONTACT_EMAIL_KEY, DatabaseInfo.TEXT_TYPE, false));
		mContactsColumn.add(new Column(DatabaseInfo.CONTACT_ADDRESS_KEY, DatabaseInfo.TEXT_TYPE, false));
		mGroupsColumn.add(new Column(DatabaseInfo.CONTACT_GROUP_ID_KEY, DatabaseInfo.INTEGER_TYPE, true));
		mGroupsColumn.add(new Column(DatabaseInfo.CONTACT_GROUP_KEY, DatabaseInfo.TEXT_TYPE, false));
		mGroupsColumn.add(new Column(DatabaseInfo.CONTACT_DEFAULT_GROUP_KEY, DatabaseInfo.INTEGER_TYPE, false));
	}

	private void showMsg(boolean result) {
		int msg = R.string.save_failed;
		if (result == true) {
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
	
	private void setDefaultContacts() {
		Cursor cursor = DatabaseWrapper.getWrapper().selectAllData(DatabaseInfo.CONTACTS_TABLE);	
		if(cursor.getCount() == 0) {
			DefaultData.setDefaultContacts();
		}
	}
}
