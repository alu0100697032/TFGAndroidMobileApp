package com.example.v.ullapp.service;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.v.ullapp.PrefUtils;
import com.example.v.ullapp.R;
import com.facebook.AccessToken;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Usuario on 12/08/2016.
 */
public class ServiceActivity extends AppCompatActivity {
    String selectedDate = "";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent intent = getIntent();
        final int pistaId = intent.getIntExtra("ID", 0);
        getReservadas(pistaId);
        Log.e("I", pistaId + "");
        final String name = intent.getStringExtra("NAME");
        String type = intent.getStringExtra("TYPE");

        TextView t = (TextView) findViewById(R.id.name);
        TextView c = (TextView) findViewById(R.id.type);
        t.setText(name);
        c.setText(type);

        //calendar
        final CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setMinDate(System.currentTimeMillis());

        //Dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Se efectuará la reserva de " + name +" el día seleccionado.")
                .setTitle("¿Confirmar reserva?");

        builder.setPositiveButton(R.string.action_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reserve(pistaId);
            }
        });
        builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();

        //Button
        final Button reserve = (Button)findViewById(R.id.button_reserve);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        selectedDate = String.valueOf(calendar.getDate());
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);
                selectedDate = String.valueOf(c.getTimeInMillis());
            }
        });
    }

    public void reserve(int id){
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String token = accessToken.getToken();
        if(token == null)
            token = PrefUtils.getCurrentUser(this).token;
        CalendarView cv = (CalendarView) findViewById(R.id.calendarView);
        final String finalToken = token;
        final String pista_pk = String.valueOf(id);
        final String date = selectedDate;

        if(finalToken != null) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = getResources().getString(R.string.reserve_url);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(ServiceActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", "Error posting for reserve!");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("pista_pk", pista_pk);
                    params.put("token", finalToken);
                    params.put("date", date);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

    public void getReservadas (int id) {
        // Instantiate the RequestQueue.
        final String pistaId = String.valueOf(id);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.ya_reservadas_url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        ArrayList<Long> reservadasLong = new ArrayList<Long>();
                        try {
                            JSONArray reservadas = new JSONArray(response);
                            String[] mArray = reservadas.join(",").split(",");
                            for (int i = 0; i < mArray.length; i++){
                                reservadasLong.add(Long.parseLong(mArray[i]));
                            }
                            Log.e("Response", mArray.toString() + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "That didn't work!" + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pista_id", pistaId);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
