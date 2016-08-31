package com.example.v.ullapp.reserves;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.example.v.ullapp.PrefUtils;
import com.example.v.ullapp.news.AsyncResponse;
import com.example.v.ullapp.service.ServiceItem;
import com.example.v.ullapp.service.ServiceXmlParser;
import com.facebook.AccessToken;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by Usuario on 13/08/2016.
 */
public class DownloadXmlReserves extends AsyncTask<String, Void, List> {
    public AsyncResponse delegate = null;
    private Context context;

    public DownloadXmlReserves(Context c){
        context = c;
    }
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
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        String token;
        if(AccessToken.getCurrentAccessToken() != null)
            token = AccessToken.getCurrentAccessToken().getToken();
        else
            token = PrefUtils.getCurrentUser(context).token;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("access_token", token));

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();

        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
