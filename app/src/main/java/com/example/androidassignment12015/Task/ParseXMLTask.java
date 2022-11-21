package com.example.androidassignment12015.Task;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidassignment12015.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ParseXMLTask extends AsyncTask<String, Integer, String> {
    Activity contextParent;
    Dialog context;

    public ParseXMLTask(Activity contextParent,Dialog context) {
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
        String instanceID = "";
        int count = 0;
            try {
                AssetManager assetManager = contextParent.getAssets();
                InputStream is = assetManager.open("data/" + params[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(is);
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("meta");
                for (int itr = 0; itr < nodeList.getLength(); itr++) {
                    Node node = nodeList.item(itr);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        if(eElement.getElementsByTagName("instanceID").item(0)==null) {
                            instanceID = eElement.getElementsByTagName("instanceName").item(0).getTextContent();
                            count = instanceID.length();
                            for (int i = 0;i<=count;i++) {
                                SystemClock.sleep(1);
                                publishProgress((int) ((i / (float) count) * 100));
                            }
                            return instanceID;
                        }
                        else {
                            Log.d("instanceID: ", eElement.getElementsByTagName("instanceID").item(0).getTextContent());
                            instanceID = eElement.getElementsByTagName("instanceID").item(0).getTextContent();
                            count = instanceID.length();
                            for (int i = 0; i <= count; i++) {
                                SystemClock.sleep(1);
                                publishProgress((int) ((i / (float) count) * 100));
                            }
                            return instanceID;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return instanceID;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        ProgressBar progressBar = (ProgressBar) context.findViewById(R.id.idLoadingParse);
        int number = values[0];
        progressBar.setProgress(number);
        TextView textView = (TextView) context.findViewById(R.id.statusParse);
        textView.setText("XML parsing: " + number + "%");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        TextView textView = (TextView) context.findViewById(R.id.instanceID);
        textView.setText(s);
        Toast.makeText(contextParent, "Okie, Finished", Toast.LENGTH_SHORT).show();
    }
}
