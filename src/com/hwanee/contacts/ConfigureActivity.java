package com.hwanee.contacts;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hwanee.data.DBWrapper;

public class ConfigureActivity extends Activity {
	private EditText mSQLInput;
	private ListView mResultList;
	private Button mSend;
	private ArrayList<String> mResult = new ArrayList<String>();
	private ArrayAdapter<String> mAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configure_activity);
		initActivity();
	}

	private void initActivity() {
		mSQLInput = (EditText) findViewById(R.id.SQLInput);
		mResultList = (ListView) findViewById(R.id.SQLResultList);
		mSend = (Button) findViewById(R.id.SendSQL);
		mSend.setOnClickListener(mSendClick);
		if (mResultList != null && mResult != null) {
			mAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, mResult);
			mResultList.setAdapter(mAdapter);
		}
	}

	private OnClickListener mSendClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(mResult != null) {
				mResult.clear();
			}
			if (mSQLInput != null) {
				String sql = mSQLInput.getText().toString();
				if(sql == null || sql.length() <=0) {
					return;
				}
				Cursor cursor = DBWrapper.getIstance().selectData(sql);
				if (cursor == null) {
					Toast.makeText(getBaseContext(), R.string.error,
							Toast.LENGTH_SHORT).show();
					return;
				}
				do {
					String[] result = new String[cursor.getColumnCount()];
					for (int i = 0; i < cursor.getColumnCount(); i++) {
						result[i] = cursor.getString(i);
					}
					mResult.add(TextUtils.join(", ", result));
				} while (cursor.moveToNext());
				mAdapter.notifyDataSetChanged();
			}
		}
	};
}
