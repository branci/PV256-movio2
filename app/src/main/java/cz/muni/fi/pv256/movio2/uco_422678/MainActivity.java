package cz.muni.fi.pv256.movio2.uco_422678;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;


import java.util.ArrayList;
import java.util.Locale;

import cz.muni.fi.pv256.movio2.R;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnMovieSelectListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String PREFSTRING = "currentTheme";
    private boolean mTwoPane;
    private ArrayList<Object> mMovieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment(), DetailFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        mMovieList = new ArrayList<>();

        mMovieList.add(getResources().getString(R.string.section_in_theatres));
        if(Locale.getDefault().getLanguage().equals("sk") == true){
            mMovieList.add(new Movie(1234, "",  "film1", "Toto je film 1", 0.1f));
            mMovieList.add(new Movie(5678, "",  "film2", "Toto je film 2", 0.2f));
            mMovieList.add(new Movie(91011, "",  "film3", "Toto je film 3", 0.3f));
        }else {
            mMovieList.add(new Movie(1234, "",  "movie1", "This is a movie 1", 0.1f));
            mMovieList.add(new Movie(5678, "",  "movie2", "This is a movie 2", 0.2f));
            mMovieList.add(new Movie(91011, "",  "movie3", "This is a movie 3", 0.3f));
        }
        mMovieList.add(getResources().getString(R.string.section_drama));
        if(Locale.getDefault().getLanguage().equals("sk") == true){
            mMovieList.add(new Movie(1213, "",  "film4", "Toto je film 4", 0.4f));
            mMovieList.add(new Movie(1415, "",  "film5", "Toto je film 5", 0.5f));
            mMovieList.add(new Movie(1617, "",  "film6", "Toto je film 6", 0.6f));
        }else {
            mMovieList.add(new Movie(1213, "",  "movie4", "This is a movie 4", 0.4f));
            mMovieList.add(new Movie(1415, "",  "movie5", "This is a movie 5", 0.5f));
            mMovieList.add(new Movie(1617, "",  "movie6", "This is a movie 6", 0.6f));
        }

        RecyclerAdapter adapter = new RecyclerAdapter(this,mMovieList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    //@Override
    public void onMovieSelect(int movieListPosition) {
        Movie movie = (Movie)mMovieList.get(movieListPosition);

        if (mTwoPane) {
            FragmentManager myManager = getSupportFragmentManager();

            DetailFragment fragment = DetailFragment.newInstance(movie);
            myManager.beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DetailFragment.TAG)
                    .commit();

        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
            startActivity(intent);
        }
    }

}
