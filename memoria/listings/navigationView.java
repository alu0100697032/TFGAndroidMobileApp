public boolean onNavigationItemSelected(MenuItem item) {
    Intent intent;
    FragmentTransaction transaction;

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    Bundle arguments;
    switch (item.getItemId()){
        case R.id.nav_news:
            arguments = new Bundle();
            arguments.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) newsItems);
            NewsFragment newsFragment = new NewsFragment();
            newsFragment.setArguments(arguments);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        ...
        
        case R.id.nav_maps:
            intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            return true;
        ...
    }
}