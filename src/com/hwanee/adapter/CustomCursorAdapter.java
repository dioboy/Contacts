package com.hwanee.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hwanee.contacts.R;
import com.hwanee.data.ContactsData;

public class CustomCursorAdapter extends CursorAdapter {

	public CustomCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView nameTv = (TextView) view.findViewById(R.id.ContactsItemName);
		TextView phoneTv = (TextView) view.findViewById(R.id.ContactsItemPhone);

		String name = cursor.getString(cursor
				.getColumnIndex(ContactsData.CONTACT_NAME_KEY));
		if (nameTv != null && name != null) {
			nameTv.setText(name);
		}

		String number = cursor.getString(cursor
				.getColumnIndex(ContactsData.CONTACT_MOBILE_KEY));
		if (phoneTv != null) {
			if (number == null) {
				number = cursor.getString(cursor
						.getColumnIndex(ContactsData.CONTACT_PHONE_KEY));
			}

			if (number != null) {
				phoneTv.setText(number);
			}
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.contacts_list_item, parent,
				false);
		return retView;
	}

}
