package com.example.v.ullapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.example.v.ullapp.news.AsyncResponse;
import com.example.v.ullapp.news.RecyclerItemClickListener;
import com.example.v.ullapp.reserves.DownloadXmlReserves;
import com.example.v.ullapp.reserves.ReserveItem;
import com.example.v.ullapp.reserves.ReservesAdapter;
import com.example.v.ullapp.service.DownloadXmlService;
import com.example.v.ullapp.service.ServiceAdapter;
import com.example.v.ullapp.service.ServiceItem;
import com.facebook.AccessToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Usuario on 08/08/2016.
 */
public class UserInfoFragment extends Fragment implements AsyncResponse {
    private List<ReserveItem> reservesList = null;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.content_personal_info, container, false);
        DownloadXmlReserves downloadXmlReserves= new DownloadXmlReserves();
        downloadXmlReserves.delegate = this;
        downloadXmlReserves.execute(getResources().getString(R.string.my_data_url));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!= null && getArguments().containsKey("list")) {
            //Cargamos el contenido de la entrada con cierto ID seleccionado en la lista. Se recomiendo usar un Loader para cargar el contenido
            reservesList = getArguments().getParcelableArrayList("list");
        }
    }

    public void displayReserves(View view){
        ProgressBar pb = (ProgressBar)view.findViewById(R.id.progressBar);
        if(pb != null)
            pb.setVisibility(View.INVISIBLE);

        if(reservesList != null) {
            RecyclerView mRecyclerView;
            RecyclerView.Adapter mAdapter;
            RecyclerView.LayoutManager mLayoutManager;
            mRecyclerView = (RecyclerView) view.findViewById(R.id.reserves_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new ReservesAdapter(reservesList);
            mRecyclerView.setAdapter(mAdapter);

        }else
            Toast.makeText(view.getContext(), "No se han podido cargar tus reservas", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void processFinish(List output) {
        reservesList = output;
        displayReserves(view);
    }

    /*public void getService (View view) {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final TextView display = (TextView) view.findViewById(R.id.display);
        if(accessToken != null || PrefUtils.getCurrentUser(getContext()).token != null) {

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(view.getContext());
            String url = getResources().getString(R.string.my_data_url);
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
            Toast.makeText(view.getContext(),"Se requiere autenticación. Por favor, inicie sesión.", Toast.LENGTH_LONG).show();
        }
    }*/
}
