package com.example.android.digilib;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();


    public static final String REQUEST_URL_1 = "https://www.googleapis.com/books/v1/volumes?q=";
    public static final String REQUEST_ULR_2 = "&maxResults=10";
    public static String FINAL_URL = "";
    private String query;

    private Button   mButton;
    private EditText mEdit;
    Intent loadBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.button);
        mEdit   = (EditText)findViewById(R.id.search_text);

        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view) {
                        query = mEdit.getText().toString();


                        FINAL_URL = REQUEST_URL_1 + query + REQUEST_ULR_2;

                        loadBooks = new Intent(MainActivity.this, BookActivity.class);
                        loadBooks.putExtra("DOMAIN_QUERY", FINAL_URL);
                        startActivity(loadBooks);
                    }
        });




    }


    public void getDomain(View view) {
    }
}
