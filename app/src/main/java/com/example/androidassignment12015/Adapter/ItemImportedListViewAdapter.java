package com.example.androidassignment12015.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidassignment12015.Model.Item;
import com.example.androidassignment12015.R;

import java.util.ArrayList;

public class ItemImportedListViewAdapter extends BaseAdapter {
    final ArrayList<Item> listItem;
    public ItemImportedListViewAdapter(ArrayList<Item> listItem) {
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (listItem.get(position).getId());
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewItem;
        if (convertView == null) {
            viewItem = View.inflate(parent.getContext(), R.layout.view_item_imported, null);
        } else viewItem = convertView;
        Item item = (Item) getItem(position);
        ((TextView) viewItem.findViewById(R.id.STT)).setText(String.valueOf(position));
        ((TextView) viewItem.findViewById(R.id.name)).setText(item.getName());
        ((TextView) viewItem.findViewById(R.id.location)).setText(item.getLocation());
        return viewItem;
    }
}
