package com.hwanee.contacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.hwanee.database.DatabaseInfo;
import com.hwanee.database.DatabaseWrapper;

public class ContactsDetailActivity extends Activity {
	private int mId = -1;
	private static int[] mItemArray = { R.id.ContactsDetailName,
			R.id.ContactsDetailGroup, R.id.ContactsDetailMobile,
			R.id.ContactsDetailPhone, R.id.ContactsDetailEmail,
			R.id.ContactsDetailAddress };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_detail_activity);
		initDetailActivity();

	}
	
	@Override
	protected void onResume() {
		setContact();
		super.onResume();
	}

	private void initDetailActivity() {
		Intent intent = getIntent();
		if (intent != null) {
			mId = intent.getIntExtra(DatabaseInfo.CONTACT_ID_KEY, -1);
		}
		if (mId == -1) {
			finish();
		}
		
		setContact();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contacts_detail_act_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.menu_item_delete) {
			if(mId != -1) {
				boolean result = DatabaseWrapper.getWrapper().deleteData(DatabaseInfo.CONTACTS_TABLE, DatabaseInfo.CONTACT_ID_KEY, String.valueOf(mId));
				showMsg(result);
				if(result) {
					finish();
				}
			}
		} else {
			Intent intent = new Intent(getApplicationContext(),
					EditContactsActivity.class);
			intent.putExtra(DatabaseInfo.CONTACT_ID_KEY, mId);
			startActivity(intent);
		}
		return true;
	}
	
	public void setContact() {
		if (mId != -1) {
			String[] selection = {DatabaseInfo.CONTACT_GROUP_ID_KEY};
			String[] selectionArgs = {String.valueOf(mId)};
			Cursor cursor = DatabaseWrapper.getWrapper().selectData(DatabaseInfo.CONTACTS_TABLE, DatabaseInfo.CONTACT_COLUMN_LIST, selection, selectionArgs, null, null, null);

			for (int i = 0; i < mItemArray.length; i++) {
				getData(DatabaseInfo.CONTACT_COLUMN_LIST[i + 1],
						mItemArray[i], cursor);
			}
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public void getData(String key, int id, Cursor cursor) {
		TextView tv = (TextView) findViewById(id);
		String tmp = cursor.getString(cursor.getColumnIndex(key));
		if (tv != null && tmp != null) {
			tv.setText(tmp);
		}
	}
	
	public void showMsg(boolean result){
		int resId = R.string.delete_successful;
		if(result == false){
			resId = R.string.delete_failed;
		}
		Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
	}
}
