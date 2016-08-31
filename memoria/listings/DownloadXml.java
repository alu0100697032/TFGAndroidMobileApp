public class DownloadXmlNews extends AsyncTask<String, Void, List> {
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
        UllNewsXmlParser ullNewsXmlParser = new UllNewsXmlParser();
        List<NewsItem> newsItems = null;
        try {
            stream = downloadUrl(urlString);
            newsItems = ullNewsXmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return newsItems;
    }

    private InputStream downloadUrl(final String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }
}