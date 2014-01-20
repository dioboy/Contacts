package com.hwanee.contacts;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.delegate.database.DatabaseInfo;
import com.hwanee.data.ContactsData;
import com.hwanee.data.DBWrapper;

public class AddContactsActivity extends Activity {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contacts_layout);
		initActivity();
	}

	private void initActivity() {
		mName = (EditText) findViewById(R.id.AddContactsName);
		mGroup = (Spinner) findViewById(R.id.AddContactsGroup);
		mMobile = (EditText) findViewById(R.id.AddContactsMobile);
		mPhone = (EditText) findViewById(R.id.AddContactsPhone);
		mEmail = (EditText) findViewById(R.id.AddContactsEmail);
		mAddress = (EditText) findViewById(R.id.AddContactsAddress);
		mSaveBtn = (Button) findViewById(R.id.AddContactSave);
		mCancelBtn = (Button) findViewById(R.id.AddContactCancel);
		mSaveBtn.setOnClickListener(mClickListener);
		mCancelBtn.setOnClickListener(mClickListener);
		setGroupsSpinner();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_contacts_act_menu, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mName.getWindowToken(), 0);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setGroupsSpinner() {
		Cursor groupCursor = DBWrapper.getIstance().selectAllData(ContactsData.GROUPS_TABLE);
		if (groupCursor != null && groupCursor.getCount() != 0) {
			for (int i = 0; i < groupCursor.getCount(); i++) {
				if (mGroupList == null) {
					mGroupList = new ArrayList<String>();
				}
				mGroupList
						.add(groupCursor.getString(groupCursor
								.getColumnIndex(ContactsData.CONTACT_GROUP_KEY)));
				if (!groupCursor.moveToNext()) {
					break;
				}
			}
		}

		if (groupCursor != null) {
			groupCursor.close();
		}

		if (mGroupList.size() != 0) {
			mGroupAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, mGroupList);
			mGroupAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mGroup.setAdapter(mGroupAdapter);
		}
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int resId = R.string.save_successful;
			if (v.getId() == R.id.AddContactSave) {
				ContentValues values = new ContentValues();
				values.put(ContactsData.CONTACT_NAME_KEY, mName.getText().toString());
				values.put(ContactsData.CONTACT_GROUP_KEY, mGroup.getSelectedItem().toString());
				values.put(ContactsData.CONTACT_MOBILE_KEY, mMobile.getText().toString());
				values.put(ContactsData.CONTACT_PHONE_KEY, mPhone.getText().toString());
				values.put(ContactsData.CONTACT_EMAIL_KEY, mEmail.getText().toString());
				values.put(ContactsData.CONTACT_ADDRESS_KEY, mAddress.getText().toString());
				int result = DBWrapper.getIstance().insertData(ContactsData.CONTACTS_TABLE, values);
				if(result == DatabaseInfo.FAILURE) {
					resId = R.string.save_failed;
				}
				
				Toast.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	};
}
