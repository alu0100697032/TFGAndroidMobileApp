package com.example.v.ullapp.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.v.ullapp.R;

/**
 * Created by Usuario on 06/08/2016.
 */
public class NewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String link = intent.getStringExtra("LINK");
        String content = intent.getStringExtra("CONT");
        String date = intent.getStringExtra("DATE");

        TextView t = (TextView) findViewById(R.id.title);
        TextView c = (TextView) findViewById(R.id.content);
        TextView d = (TextView) findViewById(R.id.date);

        t.setText(title);
        c.setText(content);
        d.setText(date);
    }
}
