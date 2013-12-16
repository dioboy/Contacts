package com.hwanee.contacts;

import java.sql.DatabaseMetaData;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.hwanee.data.DefaultContacts;
import com.hwanee.database.ContactsDataBase;
import com.hwanee.database.ContactsDataBaseMetaData;

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
		mTabHost = getTabHost();
		setDefaultContacts();
		LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,
				mTabHost.getTabContentView(), true);

		setTab(ContactsListActivity.class, R.string.contacts,
				R.drawable.profile);
		setTab(ContactsGroupActivity.class, R.string.group, R.drawable.group);
		setTab(ConfigureActivity.class, R.string.setting, R.drawable.configure);

		if (ContactsDataBase.getGroupsCount(getBaseContext()) == 0) {
			ContactsDataBase.setDefaultGroups(getBaseContext());
		}
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
			int result = -1;
			String groupName = null;
			if (mInputGroupName != null) {
				groupName = mInputGroupName.getText().toString();
			}

			if (groupName != null) {
				result = ContactsDataBase
						.addGroups(getBaseContext(), groupName, 1);
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

	private void showMsg(int result) {
		int msg = R.string.save_failed;
		if (result == ContactsDataBaseMetaData.DBStatus_Success) {
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
		if(ContactsDataBase.getContactsCount(this) == 0) {
			DefaultContacts.setDefaultContacts(this);
		}
	}
}
