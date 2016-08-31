package com.example.v.ullapp.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.v.ullapp.MainActivity;
import com.example.v.ullapp.R;
import com.example.v.ullapp.news.AsyncResponse;
import com.example.v.ullapp.news.RecyclerItemClickListener;
import com.example.v.ullapp.service.DownloadXmlService;
import com.example.v.ullapp.service.ReserveActivity;
import com.example.v.ullapp.service.ServiceAdapter;
import com.example.v.ullapp.service.ServiceItem;

import java.util.List;

/**
 * Created by Usuario on 11/08/2016.
 */
public class ServiceFragment extends Fragment implements AsyncResponse{
    private List<ServiceItem> serviceList = null;
    private View view;
    DownloadXmlService downloadXmlService;
    private SwipeRefreshLayout refreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.service));
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.content_service, container, false);
        if(serviceList == null) {
            downloadXmlService = new DownloadXmlService();
            downloadXmlService.delegate = this;
            downloadXmlService.execute(getResources().getString(R.string.service_url));
        }else
            displayService(view);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (downloadXmlService.getStatus() == AsyncTask.Status.FINISHED) {
                            downloadXmlService = new DownloadXmlService();
                            downloadXmlService.delegate = ServiceFragment.this;
                            downloadXmlService.execute(getResources().getString(R.string.service_url));
                        }
                    }
                }
        );
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!= null && getArguments().containsKey("list")) {
            //Cargamos el contenido de la entrada con cierto ID seleccionado en la lista. Se recomiendo usar un Loader para cargar el contenido
            serviceList = getArguments().getParcelableArrayList("list");
        }
    }

    public void displayService(View view){
        ProgressBar pb = (ProgressBar)view.findViewById(R.id.progressBar);
        if(pb != null)
            pb.setVisibility(View.INVISIBLE);

        if(serviceList != null) {
            RecyclerView mRecyclerView;
            RecyclerView.Adapter mAdapter;
            RecyclerView.LayoutManager mLayoutManager;
            mRecyclerView = (RecyclerView) view.findViewById(R.id.service_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new ServiceAdapter(serviceList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(view.getContext(), new RecyclerItemClickListener.OnItemClickListener(){
                        @Override public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            showActivityService(view, position);
                        }
                    })
            );
            //Toast.makeText(MainActivity.this, "Noticias actualizadas", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(view.getContext(), "No se ha podido cargar el servicio", Toast.LENGTH_SHORT).show();
    }

    public void showActivityService(View view, int position){
        Intent intent = new Intent(view.getContext(), ReserveActivity.class);
        intent.putExtra("ID", serviceList.get(position).getCourtId());
        intent.putExtra("NAME", serviceList.get(position).getCourtName());
        intent.putExtra("TYPE", serviceList.get(position).getCourtType());
        startActivity(intent);
    }

    @Override
    public void processFinish(List output) {
        serviceList = output;
        ((MainActivity)getActivity()).setServiceItems(serviceList);
        displayService(view);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(downloadXmlService != null) {
            if (downloadXmlService.getStatus() == AsyncTask.Status.RUNNING || downloadXmlService.getStatus() == AsyncTask.Status.PENDING)
                downloadXmlService.cancel(true);
        }
    }
}
