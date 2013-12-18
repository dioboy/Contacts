package com.hwanee.contacts;

import java.util.ArrayList;

import android.app.Activity;
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

import com.hwanee.data.ContactsInfo;
import com.hwanee.database.ContactsDataBase;
import com.hwanee.database.ContactsDataBaseMetaData;

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
	private String mId = null;

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
			mId = intent.getStringExtra(ContactsInfo.CONTACT_ID_KEY);
		}
		if (mId == null) {
			finish();
		}

		Cursor cursor = ContactsDataBase.getSearchById(getBaseContext(),
				String.valueOf(mId));

		mName.setText(getData(ContactsDataBaseMetaData.DB_COLUMN_NAME, cursor));
		mMobile.setText(getData(ContactsDataBaseMetaData.DB_COLUMN_MOBILE,
				cursor));
		mPhone.setText(getData(ContactsDataBaseMetaData.DB_COLUMN_PHONE, cursor));
		mEmail.setText(getData(ContactsDataBaseMetaData.DB_COLUMN_EMAIL, cursor));
		mAddress.setText(getData(ContactsDataBaseMetaData.DB_COLUMN_ADDRESS,
				cursor));
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
		Cursor groupCursor = ContactsDataBase
				.getGroupsCursor(getApplicationContext());
		if (groupCursor != null && groupCursor.getCount() != 0) {

			for (int i = 0; i < groupCursor.getCount(); i++) {
				if (mGroupList == null) {
					mGroupList = new ArrayList<String>();
				}
				mGroupList.add(groupCursor.getString(groupCursor.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME)));
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
			if (v.getId() == R.id.EditContactSave && mId != null) {
				String name = mName.getText().toString();
				String group = mGroup.getSelectedItem().toString();
				String mobile = mMobile.getText().toString();
				String phone = mPhone.getText().toString();
				String email = mEmail.getText().toString();
				String address = mAddress.getText().toString();
				int result = ContactsDataBase.updateContacts(getApplicationContext(), Integer.valueOf(mId),
						name, group, mobile, phone, email, address);
				if (result != ContactsDataBaseMetaData.DBStatus_Success) {
					id = R.string.edit_failed;
				}
			}
			Toast.makeText(getBaseContext(), id, Toast.LENGTH_SHORT).show();
			finish();
		}
	};
}
