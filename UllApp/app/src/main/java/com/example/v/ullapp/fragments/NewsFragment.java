package com.example.v.ullapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.v.ullapp.MainActivity;
import com.example.v.ullapp.R;
import com.example.v.ullapp.news.AsyncResponse;
import com.example.v.ullapp.news.DownloadXmlNews;
import com.example.v.ullapp.news.NewsAdapter;
import com.example.v.ullapp.news.NewsActivity;
import com.example.v.ullapp.news.NewsItem;
import com.example.v.ullapp.news.RecyclerItemClickListener;
import com.example.v.ullapp.reserves.DownloadXmlReserves;

import java.util.List;

/**
 * Created by Usuario on 03/08/2016.
 */
public class NewsFragment extends Fragment implements AsyncResponse {
    private List<NewsItem> newsList = null;
    private View view;
    DownloadXmlNews downloadXmlNews;
    private SwipeRefreshLayout refreshLayout;
    Context c;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.news));
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.content_news, container, false);
        if(newsList == null) {
            downloadXmlNews = new DownloadXmlNews();
            downloadXmlNews.delegate = this;
            downloadXmlNews.execute(getResources().getString(R.string.newsFeed));
        }else{
            displayNews(view);
        }

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (downloadXmlNews.getStatus() == AsyncTask.Status.FINISHED) {
                            downloadXmlNews = new DownloadXmlNews();
                            downloadXmlNews.delegate = NewsFragment.this;
                            downloadXmlNews.execute(getResources().getString(R.string.newsFeed));
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
            newsList = getArguments().getParcelableArrayList("list");
        }
    }

    public void displayNews(View view){
        ProgressBar pb = (ProgressBar) view.findViewById(R.id.progressBar);
        if(pb != null)
            pb.setVisibility(View.INVISIBLE);

        if(newsList != null) {
            RecyclerView mRecyclerView;
            RecyclerView.Adapter mAdapter;
            RecyclerView.LayoutManager mLayoutManager;
            mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new NewsAdapter(newsList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(view.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            showActivityNew(view, position);
                        }
                    })
            );
        }else
            Toast.makeText(view.getContext(), "No se han podido cargar las noticias", Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this, "Noticias actualizadas", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPause(){
        super.onPause();
        if(downloadXmlNews != null) {
            if (downloadXmlNews.getStatus() == AsyncTask.Status.RUNNING || downloadXmlNews.getStatus() == AsyncTask.Status.PENDING)
                downloadXmlNews.cancel(true);
        }
    }
    @Override
    public void processFinish(List output) {
        newsList = output;
        ((MainActivity)getActivity()).setNewsItems(newsList);
        displayNews(view);
        refreshLayout.setRefreshing(false);
    }
    public void showActivityNew(View view, int position){
        Intent intent = new Intent(view.getContext(), NewsActivity.class);
        intent.putExtra("TITLE", newsList.get(position).getTitle());
        intent.putExtra("LINK", newsList.get(position).getLink());
        intent.putExtra("DATE", newsList.get(position).getPubDate());
        intent.putExtra("CONT", newsList.get(position).getContent());
        startActivity(intent);
    }
}
