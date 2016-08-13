package com.example.v.ullapp.reserves;

import android.os.AsyncTask;

import com.example.v.ullapp.news.AsyncResponse;
import com.example.v.ullapp.service.ServiceItem;
import com.example.v.ullapp.service.ServiceXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Usuario on 13/08/2016.
 */
public class DownloadXmlReserves extends AsyncTask<String, Void, List> {
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
        ReservesXmlParser reservesXmlParser = new ReservesXmlParser();
        List<ReserveItem> reserveItems = null;

        try {
            stream = downloadUrl(urlString);
            reserveItems = reservesXmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return reserveItems;
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
