package com.example.androidassignment12015;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.androidassignment12015.Adapter.ItemImportedListViewAdapter;
import com.example.androidassignment12015.Model.Item;
import com.example.androidassignment12015.sqlite.ItemDao;

import java.util.ArrayList;
import java.util.List;

public class ListImportedActivity extends AppCompatActivity {
    private ListView listView;
    private List<Item> arrayItem;
    public ItemImportedListViewAdapter itemImportedListViewAdapter;
    public ItemDao itemDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_imported);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        itemDao = new ItemDao(this);
        arrayItem = itemDao.getAllItems();
        listView = findViewById(R.id.listView);
        itemImportedListViewAdapter = new ItemImportedListViewAdapter((ArrayList<Item>) arrayItem);
        registerForContextMenu(listView);
        listView.setAdapter(itemImportedListViewAdapter);
    }
}