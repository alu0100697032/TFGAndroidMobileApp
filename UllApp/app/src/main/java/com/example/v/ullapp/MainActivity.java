package com.example.v.ullapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.v.ullapp.fragments.InfoFragment;
import com.example.v.ullapp.fragments.NewsFragment;
import com.example.v.ullapp.fragments.UserInfoFragment;
import com.example.v.ullapp.fragments.ServiceFragment;
import com.example.v.ullapp.news.AsyncResponse;
import com.example.v.ullapp.news.DownloadXmlNews;
import com.example.v.ullapp.news.NewsAdapter;
import com.example.v.ullapp.news.NewsItem;
import com.example.v.ullapp.news.NewsActivity;
import com.example.v.ullapp.news.RecyclerItemClickListener;
import com.example.v.ullapp.reserves.ReserveItem;
import com.example.v.ullapp.service.ServiceItem;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private User user;
    Bitmap bitmap;
    private List<NewsItem> newsItems = null;
    private List<ReserveItem> reservesItems = null;
    private List<ServiceItem> serviceItems = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            NewsFragment firstFragment = new NewsFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
        /**
         * Si usuario está autenticado
         */
        if(PrefUtils.getCurrentUser(MainActivity.this) != null){
            //getService(); //obtiene el servicio una vez el usuario esté autenticado
            //Header
            View headerView = navigationView.getHeaderView(0);
            user=PrefUtils.getCurrentUser(MainActivity.this);

            final TextView username = (TextView) headerView.findViewById(R.id.username);
            username.setText(user.name);
            TextView email = (TextView) headerView.findViewById(R.id.email);
            email.setText(user.email);

            final CircleImageView profile_image = (CircleImageView) headerView.findViewById(R.id.profile_image);
            if(user.image == null) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        URL imageURL = null;
                        try {
                            imageURL = new URL(user.imageURL);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        try {
                            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if(bitmap != null) {
                            user.image = encodeTobase64(bitmap);
                            PrefUtils.setCurrentUser(user, MainActivity.this);
                            profile_image.setImageBitmap(decodeBase64(user.image));
                        }
                    }
                }.execute();
            }else{
                profile_image.setImageBitmap(decodeBase64(user.image));
            }
            //Menu
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_news).setChecked(true);
        }//endif
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, LogoutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        FragmentTransaction transaction;

        /*item.setChecked(true);
        previousMenuItem.setChecked(false);*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Bundle arguments;
        switch (item.getItemId()){
            case R.id.nav_news:
                arguments = new Bundle();
                arguments.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) newsItems);
                NewsFragment newsFragment = new NewsFragment();
                newsFragment.setArguments(arguments);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.user_info:
                arguments = new Bundle();
                arguments.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) reservesItems);
                UserInfoFragment userInfoFragment = new UserInfoFragment();
                userInfoFragment.setArguments(arguments);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, userInfoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.nav_service:
                arguments = new Bundle();
                arguments.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) serviceItems);
                ServiceFragment serviceFragment = new ServiceFragment();
                serviceFragment.setArguments(arguments);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, serviceFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.nav_maps:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                return true;
            /*case R.id.nav_info:
                InfoFragment infoFragment = new InfoFragment();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, infoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.nav_directory:
                intent = new Intent(this, DirectoryActivity.class);
                startActivity(intent);
                return true;*/
            default:
                return true;
        }
    }

    public void setNewsItems(List<NewsItem> list){
        newsItems = list;
    }

    public void setReservesItems(List<ReserveItem> list){
        reservesItems = list;
    }

    public void setServiceItems(List<ServiceItem> list){
        serviceItems = list;
    }


    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
