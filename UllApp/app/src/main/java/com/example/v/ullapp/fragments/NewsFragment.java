package com.example.v.ullapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.v.ullapp.R;
import com.example.v.ullapp.news.NewsAdapter;
import com.example.v.ullapp.news.NewsActivity;
import com.example.v.ullapp.news.NewsItem;
import com.example.v.ullapp.news.RecyclerItemClickListener;

import java.util.List;

/**
 * Created by Usuario on 03/08/2016.
 */
public class NewsFragment extends Fragment {
    private List<NewsItem> newsList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_news, container, false);
        displayNews(view);
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
                    new RecyclerItemClickListener(view.getContext(), new RecyclerItemClickListener.OnItemClickListener(){
                        @Override public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            showActivityNew(view, position);
                        }
                    })
            );
            //Toast.makeText(MainActivity.this, "Noticias actualizadas", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(view.getContext(), "No se han podido cargar las noticias", Toast.LENGTH_SHORT).show();
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
