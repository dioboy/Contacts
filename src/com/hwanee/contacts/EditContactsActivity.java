package com.hwanee.contacts;

import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hwanee.database.DatabaseInfo;
import com.hwanee.database.DatabaseWrapper;

public class EditContactsActivity extends Activity {
	private EditText mName;
	private Spinner mGroup;
	private EditText mMobile;
	private EditText mPhone;
	private EditText mEmail;
	private EditText mAddress;
	private ArrayList<String> mGroupList = new ArrayList<String>();
	private ArrayAdapter<String> mGroupAdapter = null;
	private Button mSaveBtn;
	private Button mCancelBtn;
	private int mId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contacts_layout);
		initActivity();
	}

	private void initActivity() {
		mName = (EditText) findViewById(R.id.EditContactsName);
		mGroup = (Spinner) findViewById(R.id.EditContactsGroup);
		mMobile = (EditText) findViewById(R.id.EditContactsMobile);
		mPhone = (EditText) findViewById(R.id.EditContactsPhone);
		mEmail = (EditText) findViewById(R.id.EditContactsEmail);
		mAddress = (EditText) findViewById(R.id.EditContactsAddress);
		mSaveBtn = (Button) findViewById(R.id.EditContactSave);
		mCancelBtn = (Button) findViewById(R.id.EditContactCancel);
		mSaveBtn.setOnClickListener(mClickListener);
		mCancelBtn.setOnClickListener(mClickListener);

		Intent intent = getIntent();
		if (intent != null) {
			mId = intent.getIntExtra(DatabaseInfo.CONTACT_ID_KEY, -1);
		}
		if (mId == -1) {
			finish();
		}
		String[] selection = { DatabaseInfo.CONTACT_ID_KEY };
		String[] selectionArgs = { String.valueOf(mId) };
		Cursor cursor = DatabaseWrapper.getWrapper().selectData(
				DatabaseInfo.CONTACTS_TABLE, DatabaseInfo.CONTACT_COLUMN_LIST,
				selection, selectionArgs, null, null, null);

		mName.setText(getData(DatabaseInfo.CONTACT_NAME_KEY, cursor));
		mMobile.setText(getData(DatabaseInfo.CONTACT_MOBILE_KEY, cursor));
		mPhone.setText(getData(DatabaseInfo.CONTACT_PHONE_KEY, cursor));
		mEmail.setText(getData(DatabaseInfo.CONTACT_EMAIL_KEY, cursor));
		mAddress.setText(getData(DatabaseInfo.CONTACT_ADDRESS_KEY, cursor));
		setGroupsSpinner();
	}

	private String getData(String key, Cursor cursor) {
		String tmp = cursor.getString(cursor.getColumnIndex(key));
		if (tmp != null) {
			return tmp;
		}

		return "";
	}

	private void setGroupsSpinner() {
		Cursor groupCursor = DatabaseWrapper.getWrapper().selectAllData(
				DatabaseInfo.GROUPS_TABLE);
		if (groupCursor != null && groupCursor.getCount() != 0) {

			for (int i = 0; i < groupCursor.getCount(); i++) {
				if (mGroupList == null) {
					mGroupList = new ArrayList<String>();
				}
				mGroupList.add(groupCursor.getString(groupCursor
						.getColumnIndex(DatabaseInfo.CONTACT_GROUP_KEY)));
				if (!groupCursor.moveToNext()) {
					break;
				}
			}
		}

		if (groupCursor != null) {
			groupCursor.close();
		}

		if (mGroupList.size() != 0) {
			mGroupAdapter = new ArrayAdapter<String>(getApplicationContext(),
					android.R.layout.simple_spinner_item, mGroupList);
			mGroupAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mGroup.setAdapter(mGroupAdapter);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mName.getWindowToken(), 0);
		}
		return super.onKeyDown(keyCode, event);
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = R.string.edit_successful;
			if (v.getId() == R.id.EditContactSave && mId != -1) {
				String[] column = { DatabaseInfo.CONTACT_ID_KEY };
				String[] columData = { String.valueOf(mId) };
				ContentValues values = new ContentValues();
				values.put(DatabaseInfo.CONTACT_NAME_KEY, mName.getText()
						.toString());
				values.put(DatabaseInfo.CONTACT_GROUP_KEY, mGroup
						.getSelectedItem().toString());
				values.put(DatabaseInfo.CONTACT_MOBILE_KEY, mMobile.getText()
						.toString());
				values.put(DatabaseInfo.CONTACT_PHONE_KEY, mPhone.getText()
						.toString());
				values.put(DatabaseInfo.CONTACT_EMAIL_KEY, mEmail.getText()
						.toString());
				values.put(DatabaseInfo.CONTACT_ADDRESS_KEY, mAddress.getText()
						.toString());
				boolean result = DatabaseWrapper.getWrapper().updataData(
						DatabaseInfo.CONTACTS_TABLE, column, columData, values);
				if (result == false) {
					id = R.string.edit_failed;
				}
			}
			Toast.makeText(getBaseContext(), id, Toast.LENGTH_SHORT).show();
			finish();
		}
	};
}
