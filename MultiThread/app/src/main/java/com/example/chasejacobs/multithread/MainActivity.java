package com.example.chasejacobs.multithread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private String [] numString = new String[10];
    private String [] newString = new String[10];
    private ListView mainList;
    private boolean created = false;
    private boolean loaded = false;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void create(View a) throws InterruptedException {
        if ((a.getId() == R.id.bCreate) && !loaded) {
            progressStatus = 0;

            Runnable creation = new Runnable() {
                @Override
                public void run() {
                    String filename = "numbers";

                    for(int i = 0; i < 10; i++){
                        int tempInt = i + 1;
                        numString[i] = tempInt + "\n";
                        try{
                            Thread.sleep(250);
                        }
                        catch (InterruptedException o) {
                            System.out.println(o);
                        }
                        progressStatus = i + 1;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progressStatus);
                            }
                        });
                    }
                    String tempS = "";
                    for (int s = 0; s < numString.length; s++){
                        tempS = tempS + numString[s];
                    }
                    try {
                        FileOutputStream outputStream = openFileOutput(filename,MODE_PRIVATE);
                        outputStream.write(tempS.getBytes());
                        outputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch(IOException o){
                        o.printStackTrace();
                    }
                }
            };
            Thread createThread = new Thread(creation);
            createThread.start();
            created = true;
        }
    }

    public void load(View a) throws InterruptedException {
        if ((a.getId() == R.id.bLoad) && created){
            String message = "";
            progressStatus = 0;
            try{
                FileInputStream fileInput = openFileInput("numbers");
                InputStreamReader readString = new InputStreamReader(fileInput);
                BufferedReader bReader = new BufferedReader(readString);
                StringBuffer sBuffer = new StringBuffer();
                while ((message=bReader.readLine()) != null){
                    sBuffer.append(message + "\n");
                }
                message = sBuffer.toString();
            }
            catch (FileNotFoundException o){
                o.printStackTrace();
            }
            catch (IOException c){
                c.printStackTrace();
            }
            newString = message.split("\n");
            mainList = (ListView) findViewById(R.id.listView1);
            mainList.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
            Runnable loadRunning = new Runnable() {
                public void run(){
                    new myTask().execute();
                }
            };
            Thread loadThread = new Thread (loadRunning);
            loadThread.start();
            loadThread.join();
            loaded = true;
        }
    }

    public void clear(View a){
        if (loaded){
            created = false;
            loaded = false;
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        mainList.setAdapter(adapter);

        progressStatus = 0;
        progressBar.setProgress(progressStatus);
    }

    class myTask extends AsyncTask<Void,String,Void>{

        private ArrayAdapter<String> adapter;
        private int count = 0;

        @Override
        protected Void doInBackground(Void... params) {
            for (String item: newString) {
                publishProgress(item);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException o) {
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            adapter = (ArrayAdapter<String>) mainList.getAdapter();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            System.out.println("Added items successfully");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.add(values[0]);
            count++;
            progressStatus++;
            progressBar.setProgress(progressStatus);
        }
    }
}
