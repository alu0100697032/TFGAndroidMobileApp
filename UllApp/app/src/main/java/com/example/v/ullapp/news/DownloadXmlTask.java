package com.example.v.ullapp.news;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import com.example.v.ullapp.MainActivity;
import com.example.v.ullapp.R;
import com.google.android.gms.location.internal.LocationRequestUpdateData;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Usuario on 05/08/2016.
 */
public class DownloadXmlTask extends AsyncTask<String, Void, List> {
    public AsyncResponse delegate = null;
    @Override
    protected List doInBackground(String... urls) {
        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List result) {
        delegate.processFinish(result);
    }

    private List loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        UllNewsXmlParser ullNewsXmlParser = new UllNewsXmlParser();
        List<New> news = null;

        try {
            stream = downloadUrl(urlString);
            news = ullNewsXmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        /*String result = "";
        for (New entry : news) {
            result = result + " " + entry.title + " ";
        }*/
        return news;
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
