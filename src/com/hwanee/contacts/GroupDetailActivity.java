package com.hwanee.contacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hwanee.adapter.CustomCursorAdapter;
import com.hwanee.data.ContactsInfo;
import com.hwanee.database.ContactsDataBase;
import com.hwanee.database.ContactsDataBaseMetaData;

public class GroupDetailActivity extends Activity {
	private String mName = null;
	private CustomCursorAdapter mCursorAdapter = null;
	private ListView mContactsList = null;
	private TextView mTitle = null;
	private Cursor mCursor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_list_layout);
		initActivity();
	}

	@Override
	protected void onResume() {
		if (mName != null) {
			mCursor = ContactsDataBase.getSearchByGroupName(this, mName);
		}

		if (mCursor != null && mCursorAdapter != null) {
			mCursorAdapter.changeCursor(mCursor);
			mCursorAdapter.notifyDataSetChanged();
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
		Intent intent = getIntent();
		LinearLayout layout = (LinearLayout) findViewById(R.id.ContactListTitleLayout);
		mContactsList = (ListView) findViewById(R.id.ContactsList);
		mTitle = (TextView) findViewById(R.id.ContactsGroupTitle);
		if (intent != null) {
			mName = intent.getStringExtra(ContactsInfo.CONTACT_GROUP_KEY);
		}

		if (mName == null) {
			finish();
		}

		int count = ContactsDataBase.getCountByGroupName(this, mName);
		mCursor = ContactsDataBase.getSearchByGroupName(this, mName);

		mTitle.setText(mName + " (" + count + ")");

		if (layout != null) {
			layout.setVisibility(View.VISIBLE);
		}
		if (mCursor != null) {
			mCursorAdapter = new CustomCursorAdapter(this, mCursor);
		}

		if (mCursorAdapter != null && mContactsList != null) {
			mContactsList.setAdapter(mCursorAdapter);
			mContactsList.setOnItemClickListener(mListItemClickListener);
		}
	}

	OnItemClickListener mListItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			Intent intent = new Intent(getBaseContext(),
					ContactsDetailActivity.class);
			if (mCursorAdapter != null) {
				Cursor cursor = mCursorAdapter.getCursor();
				if (cursor != null) {
					if (cursor.moveToPosition(position)) {
						String id = cursor
								.getString(cursor
										.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_ID));
						if (id != null) {
							intent.putExtra(ContactsInfo.CONTACT_ID_KEY, id);
							startActivity(intent);
						}
					}
					cursor.close();
				}
			}
		}
	};

}
