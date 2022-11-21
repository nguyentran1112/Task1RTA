package com.example.androidassignment12015.Task;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.androidassignment12015.R;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class CopyFileTask extends AsyncTask<String, Integer, String> {
    Activity contextParent;
    Dialog context;

    public CopyFileTask(Activity contextParent, Dialog context) {
        this.contextParent = contextParent;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(contextParent, "Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String ...params){
        String pathItem = "";
        int count = 0;
        AssetManager assetManager = contextParent.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open("data/"+params[0]);
            String newFileName = "/data/data/" + contextParent.getPackageName() + "/" + params[0];
            //String path = "//android_asset/official-data/"+filename;
            //String pathNewFile = Environment.getExternalStorageDirectory().toString()+"/Documents/official-data/"+ filename;
            pathItem = newFileName;
            out = new FileOutputStream(newFileName);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            count = pathItem.length();
            for (int i = 0;i<=count;i++) {
                SystemClock.sleep(1);
                publishProgress((int) ((i / (float) count) * 100));
            }
            return pathItem;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
            //confirm("Error",e.getMessage());
        }
        return pathItem;

    }



    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        ProgressBar progressBar = (ProgressBar) context.findViewById(R.id.idLoadingCopy);
        int number = values[0];
        progressBar.setProgress(number);
        TextView textView = (TextView) context.findViewById(R.id.statusCopy);
        textView.setText("Coping File: " + number + "%");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(contextParent, "Okie, Finished", Toast.LENGTH_SHORT).show();
    }
}
