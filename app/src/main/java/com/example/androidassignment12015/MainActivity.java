package com.example.androidassignment12015;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.androidassignment12015.Adapter.ItemListViewAdapter;
import com.example.androidassignment12015.Model.Item;
import com.example.androidassignment12015.Task.CopyFileTask;
import com.example.androidassignment12015.Task.ParseXMLTask;
import com.example.androidassignment12015.sqlite.ItemDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
public class MainActivity extends AppCompatActivity {
    private Button btnLoadDataLocal, btnListImported;
    private ListView listView;
    private List<Item> arrayItem;
    private ItemListViewAdapter itemListViewAdapter;
    protected TextView instanceID;
    private LinearLayout titleListView, viewBg;
    private ProgressBar loading;
    public ItemDao itemDao;
    public ParseXMLTask parseXMLTask;
    public CopyFileTask copyFileTask;
    public Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_item);
        //
        titleListView = findViewById(R.id.titleListView);
        viewBg = findViewById(R.id.viewBG);
        btnListImported = findViewById(R.id.btnListImported);
        arrayItem = new ArrayList<>();
        listView = findViewById(R.id.listView);
        loading = findViewById(R.id.idLoadingPB);
        itemListViewAdapter = new ItemListViewAdapter((ArrayList<Item>) arrayItem);
        registerForContextMenu(listView);
        btnLoadDataLocal = findViewById(R.id.btnLoadDataLocal);
        btnLoadDataLocal.setOnClickListener(v -> {
            viewBg.setVisibility(View.GONE);
            loadDataIternal();
        });
        btnListImported.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListImportedActivity.class);
            startActivity(intent);
        });
        listView.setAdapter(itemListViewAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Item item = arrayItem.get(i);
            openDialogItem(item.getName(), item.getId());
            parseXMLTask = new ParseXMLTask(MainActivity.this,dialog);
            parseXMLTask.execute(item.getName());
        });
        //DB
        itemDao = new ItemDao(this);
    }
    private void loadDataIternal() {
        final AssetManager assetManager = getAssets();
        arrayItem.clear();
        itemListViewAdapter.notifyDataSetChanged();
        loading.setVisibility(View.VISIBLE);
        try {
            String[] filelistInSubfolder = assetManager.list("data");
            if (filelistInSubfolder.length == 0) {
                confirm("Notification","Data does not exist");
            } else {
                for (int i = 0; i < filelistInSubfolder.length; i++) {
                    String filename = filelistInSubfolder[i];
                    Item item = new Item(i,filename);
                    arrayItem.add(i, item);
                    itemListViewAdapter.notifyDataSetChanged();
                    titleListView.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            confirm("Error",e.getMessage());
        }
    }
    public void confirm(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_warning_24);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void openDialogItem(String name, int id) {
        dialog.show();
        TextView idItem = dialog.findViewById(R.id.idItem);
        TextView nameItem = dialog.findViewById(R.id.itemName);
        instanceID = dialog.findViewById(R.id.instanceID);
        idItem.setText("ID: " + id);
        nameItem.setText("NAME: " + name);
        Button btnImport = dialog.findViewById(R.id.btnImport);
        Button btnCancle = dialog.findViewById(R.id.btnCancle);
        btnImport.setOnClickListener(v -> {
            copyFileTask = new CopyFileTask(MainActivity.this,dialog);
            if(itemDao.CheckIsDataAlreadyInDBorNot(id)>0) {
                confirm("Error","Already Item on DB");
            }
            else {
                Item item = null;
                try {
                    item = new Item(id,name,copyFileTask.execute(name).get(),1);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                itemDao.addItem(item);
            }
        });
        btnCancle.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }
}