public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<NewsItem> newsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, pubDate;
        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            pubDate = (TextView) view.findViewById(R.id.pubDate);
        }
    }
    public NewsAdapter(List<NewsItem> myDataset) {
        this.newsList = myDataset;
    }
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        NewsItem n = newsList.get(position);
        holder.title.setText(n.getTitle());
        holder.description.setText(n.getDescription());
        holder.pubDate.setText(n.getPubDate());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}