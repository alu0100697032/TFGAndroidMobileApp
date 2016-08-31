public class NewsXmlParser extends XmlParser{
    protected List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List news = new ArrayList();
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    name = parser.getName();
                    if(name.equals("item"))
                        news.add(readEntry(parser));
                    else
                        skip(parser);
                }
            } else {
                skip(parser);
            }
        }
        return news;
    }

    ...
}