package com.example.chasejacobs.scripturereference;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void share(View a){
        if (a.getId() == R.id.share1){
            EditText yourBook = (EditText)findViewById(R.id.textBook);
            EditText yourChapter = (EditText)findViewById(R.id.textChapter);
            EditText yourVerse = (EditText)findViewById(R.id.textVerse);

            String book, chapter, verse, full;
            book = yourBook.getText().toString();
            chapter = yourChapter.getText().toString();
            verse = yourVerse.getText().toString();
            full = book + " " + chapter + ":" + verse;

            Intent share = new Intent(this, secondPage.class);
            share.putExtra(EXTRA_MESSAGE, full);
            startActivity(share);
        }
    }
    public final static String EXTRA_MESSAGE = "";
}
