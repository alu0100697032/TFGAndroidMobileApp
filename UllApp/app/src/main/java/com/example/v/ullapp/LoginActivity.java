package com.example.v.ullapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.v.ullapp.casclient.CasAuthenticationException;
import com.example.v.ullapp.casclient.CasClient;
import com.example.v.ullapp.casclient.CasProtocolException;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.impl.client.DefaultRedirectStrategy;
import ch.boye.httpclientandroidlib.impl.client.HttpClientBuilder;
import ch.boye.httpclientandroidlib.impl.client.cache.ManagedHttpCacheStorage;


/**
 * Created by v on 07/04/2016.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{
    private CallbackManager callbackManager;
    private LoginButton facebookButton;
    private TextView btnLogin;
    private ProgressDialog progressDialog;
    User user;

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.log_in);

        if(PrefUtils.getCurrentUser(LoginActivity.this) != null){
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }

        //Facebook
        callbackManager=CallbackManager.Factory.create();
        facebookButton = (LoginButton)findViewById(R.id.login_button);
        facebookButton.setReadPermissions("public_profile", "email","user_friends");
        facebookButton.registerCallback(callbackManager, mCallBack);

        //Google
        findViewById(R.id.google_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //google
    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully
            GoogleSignInAccount acct = result.getSignInAccount();
            try {
                user = new User();
                user.id = acct.getId();
                user.email = acct.getEmail();
                user.name = acct.getDisplayName();
                user.token = acct.getIdToken();
                if(acct.getPhotoUrl() != null)
                    user.imageURL = acct.getPhotoUrl().toString();
                PrefUtils.setCurrentUser(user,LoginActivity.this);
            }catch (Exception e){
                e.printStackTrace();
            }
            //loginServer();
            Toast.makeText(LoginActivity.this,"Bienvenido "+ user.name, Toast.LENGTH_LONG).show();
            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // Signed out, show unauthenticated UI.
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_button:
                googleSignIn();
                break;
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Google", "onConnectionFailed:" + connectionResult);
    }
    //Facebook
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            //progressDialog.dismiss();
            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {
                                user = new User();
                                user.id = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.imageURL = "https://graph.facebook.com/" + user.id + "/picture?type=large";
                                PrefUtils.setCurrentUser(user,LoginActivity.this);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            //login server
                            loginServer();

                            Toast.makeText(LoginActivity.this,"Bienvenido "+user.name, Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };

    /**
     * Send token
     */
    public void loginServer() {

        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = getResources().getString(R.string.facebook_login_url);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response + "It works!");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", "Error sending token!");
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
        }
    }
    /**
     * Ull user and password
     * @param view
     */
   /* public void authenticate (View view) {
        EditText u = (EditText)findViewById(R.id.username);
        EditText p = (EditText)findViewById(R.id.password);
        final String username = u.getText().toString();
        final String password = p.getText().toString();
        final TextView display = (TextView)findViewById(R.id.display);
        //display.setText("Username: " + username + " - Password: " + password);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url ="http://192.168.1.101:8080/cas/login";
        String url ="http://192.168.1.105:8000/account/login";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        display.setText("Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                display.setText("That didn't work!" + error);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username","prueba");
                params.put("password","desarrollo");
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }*/

    public void auth(View view){
        HttpClient client = HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy()).build();;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        //CasClient c = new CasClient(client,"https://login.ull.es/cas-1/");
        CasClient c = new CasClient(client,"http://192.168.1.101:8080/cas/");
        try {
            //c.login("http://192.168.1.101:8080/app1/", "victor", "victor");
            //c.login("https://campusvirtual.ull.es/index.php?authCAS=CAS", "alu0100697032", "Baloncesto616");
            c.validate("http://192.168.1.101:8080/app1/","ST-11-dsRteOFNA3MadUmhZdpr-cas01.example.org");
            //c.login2("victor", "victor");
        } catch (CasAuthenticationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CasProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loginWeb(View view){
        Intent intent = new Intent(this, LoginWebActivity.class);
        startActivity(intent);
    }
}
