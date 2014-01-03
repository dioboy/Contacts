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
import com.hwanee.database.DatabaseWrapper;

public class GroupsCursorAdapter extends CursorAdapter {

	public GroupsCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int count = 0;
		TextView nameTv = (TextView) view.findViewById(R.id.GroupsListItemName);
		TextView countTv = (TextView) view
				.findViewById(R.id.GroupsListItemCount);

		String name = cursor.getString(cursor
				.getColumnIndex(ContactsData.CONTACT_GROUP_KEY));
		if (nameTv != null && name != null) {
			nameTv.setText(name);
		}
		String[] selection = { ContactsData.CONTACT_GROUP_KEY };
		String[] selectionArgs = { name };
		Cursor gCursor = DatabaseWrapper.getWrapper().selectData(
				ContactsData.CONTACTS_TABLE, ContactsData.CONTACT_COLUMN_LIST,
				selection, selectionArgs, null, null, null);
		if (gCursor != null) {
			count = gCursor.getCount();
		}
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
