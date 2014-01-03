package com.hwanee.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.hwanee.adapter.CustomCursorAdapter;
import com.hwanee.data.ContactsData;
import com.hwanee.database.DatabaseWrapper;

public class ContactsListActivity extends Activity {
	private CustomCursorAdapter mCursorAdapter = null;
	private ListView mListView = null;
	private EditText mSearchInput = null;
	Cursor mCursor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_list_activity);
		initActivity();
	}

	@Override
	protected void onResume() {
		mCursor = DatabaseWrapper.getWrapper().selectAllData(ContactsData.CONTACTS_TABLE);
		
		if (mCursor != null && mCursorAdapter != null) {
			mCursorAdapter.changeCursor(mCursor);
			mCursorAdapter.notifyDataSetChanged();
		}
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mSearchInput.getWindowToken(), 0);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initActivity() {
		mCursor = DatabaseWrapper.getWrapper().selectAllData(ContactsData.CONTACTS_TABLE);
		mListView = (ListView) findViewById(R.id.ContactsList);
		mSearchInput = (EditText) findViewById(R.id.SearchInput);
		mSearchInput.addTextChangedListener(mSearchTextWatcher);
		if (mCursor != null) {
			mCursorAdapter = new CustomCursorAdapter(this, mCursor);
		}
		if (mCursorAdapter != null) {
			mListView.setAdapter(mCursorAdapter);
			mListView.setOnItemClickListener(mListItemClickListener);
		}
	}

	TextWatcher mSearchTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};
	
	protected void onDestroy() {
		if(mCursor != null) {
			mCursor.close();
		}
		super.onDestroy();
	};

	OnItemClickListener mListItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			Intent intent = new Intent(getBaseContext(),
					ContactsDetailActivity.class);
//			HashMap<String, String> item = Contacts.getContacts(
//					getBaseContext()).getItem(position);
			if(mCursorAdapter != null) {
				Cursor cursor = mCursorAdapter.getCursor();
				if(cursor != null) {
					if(cursor.moveToPosition(position)){
						int id = cursor.getInt(cursor.getColumnIndex(ContactsData.CONTACT_ID_KEY));
						if(id != -1) {
							intent.putExtra(ContactsData.CONTACT_ID_KEY, id);
							startActivity(intent);
						}
					}
					cursor.close();
				}
			}
		}
	};
}
