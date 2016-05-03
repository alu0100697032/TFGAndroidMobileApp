package com.example.v.ullapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by v on 08/04/2016.
 */
public class Directory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory);
    }
    //go to campus activity
    public void showCampusActivity(View view){
        Intent intent = new Intent(this, Campus.class);
        startActivity(intent);
    }
}
