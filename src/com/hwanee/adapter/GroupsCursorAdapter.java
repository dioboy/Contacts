package com.hwanee.adapter;

import com.hwanee.contacts.R;
import com.hwanee.database.ContactsDataBase;
import com.hwanee.database.ContactsDataBaseMetaData;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroupsCursorAdapter extends CursorAdapter {

	public GroupsCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView nameTv = (TextView) view.findViewById(R.id.GroupsListItemName);
		TextView countTv = (TextView) view
				.findViewById(R.id.GroupsListItemCount);

		String name = cursor
				.getString(cursor
						.getColumnIndex(ContactsDataBaseMetaData.DB_COLUMN_GROUPS_NAME));
		if (nameTv != null && name != null) {
			nameTv.setText(name);
		}

		int count = ContactsDataBase.getCountByGroupName(mContext, name);
		if (countTv != null && count != -1) {
			countTv.setText(String.valueOf(count));
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.contacts_groups_list_item,
				parent, false);
		return retView;
	}

}
