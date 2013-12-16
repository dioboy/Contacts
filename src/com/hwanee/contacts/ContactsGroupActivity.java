package com.hwanee.contacts;

import com.hwanee.adapter.GroupsCursorAdapter;
import com.hwanee.data.ContactsInfo;
import com.hwanee.database.ContactsDataBase;
import com.hwanee.database.ContactsDataBaseMetaData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContactsGroupActivity extends Activity {
	private ListView mGroupsList;
	private GroupsCursorAdapter mGroupCursorAdapter = null;
	private Cursor mCursor = null;
	private int mDeleteGroupId = -1;
	private AlertDialog mDeleteDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_group_activity);
		initActivity();
	}

	@Override
	protected void onResume() {
		mCursor = ContactsDataBase.getGroupsCursor(this);
		if (mCursor != null && mGroupCursorAdapter != null) {
			mGroupCursorAdapter.changeCursor(mCursor);
			mGroupCursorAdapter.notifyDataSetChanged();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mCursor != null) {
			mCursor.close();
		}
		super.onDestroy();
	}

	private void initActivity() {
		mCursor = ContactsDataBase.getGroupsCursor(this);
		if (mCursor != null) {
			mGroupCursorAdapter = new GroupsCursorAdapter(this, mCursor);
		}
		mGroupsList = (ListView) findViewById(R.id.ContactsGroupsList);
		if (mGroupsList != null && mGroupCursorAdapter != null) {
			mGroupsList.setAdapter(mGroupCursorAdapter);
			mGroupsList.setOnItemClickListener(mListItemClickListener);
			mGroupsList.setOnItemLongClickListener(mListItemDeleteListener);
		}
	}

	private void createDeleteDialog() {
		if (mDeleteDialog != null) {
			mDeleteDialog.dismiss();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(
						this,
						android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth));
		if (builder != null) {
			builder.setMessage(R.string.delete_dialog_msg);
			builder.setPositiveButton(getString(R.string.ok), mDeleteOkListener);
			builder.setNegativeButton(getString(R.string.cancel),
					mDeleteCancelListener);
		}

		mDeleteDialog = builder.create();

		mDeleteDialog.show();
	}

	private void showMsg(boolean result) {
		int msg = R.string.delete_failed;
		if (result) {
			msg = R.string.delete_successful;
		}
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	OnClickListener mDeleteOkListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			boolean result = false;
			if (mDeleteGroupId > 0) {
				String name = ContactsDataBase.getGroupNameByGroupId(getBaseContext(), mDeleteGroupId);
				result = ContactsDataBase.deleteGroups(getBaseContext(),
						mDeleteGroupId);
				if(result || name != null) {
					ContactsDataBase.updateContactsAfterDeleteGroup(getBaseContext(), name);
				}
				mDeleteGroupId = -1;
			}

			showMsg(result);
		}
	};

	OnClickListener mDeleteCancelListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (mDeleteDialog != null && mDeleteDialog.isShowing()) {
				mDeleteDialog.dismiss();
				mDeleteGroupId = -1;
			}
		}
	};

	OnItemLongClickListener mListItemDeleteListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			if (mCursor != null) {
				if (mCursor.moveToPosition(position)) {
					int isDelete = mCursor.getInt(mCursor.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_GROUPS_DELETE_FLAG));
					if (isDelete == 0) {
						return false;
					}
					mDeleteGroupId = mCursor.getInt(mCursor.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_ID));
					if (mDeleteGroupId > 0) {
						createDeleteDialog();
						return true;
					}
				}
			}
			return false;
		}
	};

	OnItemClickListener mListItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			Intent intent = new Intent(getBaseContext(),
					GroupDetailActivity.class);
			if (mGroupCursorAdapter != null) {
				Cursor cursor = mGroupCursorAdapter.getCursor();
				if (cursor != null) {
					if (cursor.moveToPosition(position)) {
						
						String name = cursor
								.getString(cursor
										.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME));
						if (name != null) {
							intent.putExtra(ContactsInfo.CONTACT_GROUP_KEY,
									name);
							startActivity(intent);
						}
					}
					cursor.close();
				}
			}
		}
	};
}
