package com.example.v.ullapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.v.ullapp.news.AsyncResponse;
import com.example.v.ullapp.news.DownloadXmlTask;
import com.example.v.ullapp.news.MyAdapter;
import com.example.v.ullapp.news.New;
import com.example.v.ullapp.news.NewsActivity;
import com.example.v.ullapp.news.RecyclerItemClickListener;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncResponse {
    private User user;
    Bitmap bitmap;
    MenuItem previousMenuItem;
    List<New> news = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            NewsFragment firstFragment = new NewsFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
            //news
            DownloadXmlTask downloadXmlTask = new DownloadXmlTask();
            downloadXmlTask.delegate = this;
            downloadXmlTask.execute(getResources().getString(R.string.newsFeed));
        }
        /**
         * Si usuario está autenticado
         */
        if(PrefUtils.getCurrentUser(MainActivity.this) != null){
            //getService(); //obtiene el servicio una vez el usuario esté autenticado
            //Header
            View headerView = navigationView.getHeaderView(0);
            user=PrefUtils.getCurrentUser(MainActivity.this);

            TextView username = (TextView) headerView.findViewById(R.id.username);
            username.setText(user.name);
            TextView email = (TextView) headerView.findViewById(R.id.email);
            email.setText(user.email);

            final CircleImageView profile_image = (CircleImageView) headerView.findViewById(R.id.profile_image);
            //profile_image.setImageBitmap(user.image);
            // fetching facebook's profile picture
            /*if(user.image == null) {
                Log.e("I", "Requested");*/
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        URL imageURL = null;
                        try {
                            imageURL = new URL("https://graph.facebook.com/" + user.facebookID + "/picture?type=large");
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
                        profile_image.setImageBitmap(bitmap);
                    }
                }.execute();
            /*}else{
                Log.e("I", "Stored");
                profile_image.setImageBitmap(user.image);
            }*/
            //Menu
            Menu menu = navigationView.getMenu();
            //menu.setGroupVisible(R.id.auth, true);*/
            menu.findItem(R.id.nav_news).setChecked(true);
            /*previousMenuItem = menu.findItem(R.id.nav_news);
            previousMenuItem.setChecked(true);*/
        }//endif
    }
    @Override
    public void processFinish(List result){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        news = result;
        displayNews(result);
    }
    public void displayNews(List<New> news){
        if(news != null) {
            RecyclerView mRecyclerView;
            RecyclerView.Adapter mAdapter;
            RecyclerView.LayoutManager mLayoutManager;
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new MyAdapter(news);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener(){
                        @Override public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            showActivityNew(view, position);
                        }
                    })
            );
            //Toast.makeText(MainActivity.this, "Noticias actualizadas", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(MainActivity.this, "No se han podido cargar las noticias", Toast.LENGTH_SHORT).show();
    }
    public void showActivityNew(View view, int position){
        Intent intent = new Intent(this, NewsActivity.class);
        intent.putExtra("TITLE", news.get(position).getTitle());
        intent.putExtra("LINK", news.get(position).getLink());
        intent.putExtra("DATE", news.get(position).getPubDate());
        intent.putExtra("CONT", news.get(position).getContent());
        startActivity(intent);
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

        switch (item.getItemId()){
            case R.id.nav_news:
                NewsFragment newsFragment = new NewsFragment();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                //displayNews(news);
                return true;
            case R.id.nav_maps:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_info:
                InfoFragment infoFragment = new InfoFragment();
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, infoFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.nav_directory:
                intent = new Intent(this, DirectoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return true;
        }
    }
    /**
     * get some data from the server
     *
     */
    public void getService () {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final TextView display = (TextView) findViewById(R.id.display);
        if(accessToken != null) {

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://tfgserver.herokuapp.com/cv/";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            display.setText("Response is: " + response);
                            Log.e("Response", response + "");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    display.setText("That didn't work!" + error);
                    Log.e("Error", "That didn't work!" + "");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("access_token", accessToken.getToken());
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else{
            Toast.makeText(MainActivity.this,"Se requiere autenticación. Por favor, inicie sesión.", Toast.LENGTH_LONG).show();
        }
    }
}
