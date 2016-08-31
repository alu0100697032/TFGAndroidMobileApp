package com.example.v.ullapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Usuario on 16/06/2016.
 */
public class LogoutActivity extends AppCompatActivity {
    private TextView btnLogout;
    private User user;
    private ImageView profileImage;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Cerrar sesión");
        setContentView(R.layout.log_out);
        user=PrefUtils.getCurrentUser(LogoutActivity.this);
        profileImage= (ImageView) findViewById(R.id.profileImage);
        if(user.image != null)
            profileImage.setImageBitmap(decodeBase64(user.image));

        btnLogout = (TextView) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.clearCurrentUser(LogoutActivity.this);
                // We can logout from facebook by calling following method
                LoginManager.getInstance().logOut();
                Intent i= new Intent(LogoutActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
