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


import cz.muni.fi.pv256.movio2.R;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnMovieSelectListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String PREFSTRING = "currentTheme";
    private boolean mTwoPane;

    //private Button mSwitchThemeButton;
    //private SharedPreferences myPreferences;
    //private SharedPreferences.Editor myPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ukol2
        /*
        myPreferences = getSharedPreferences("movio2preferences", MODE_PRIVATE);
        final boolean isCurrentThemeAppTheme = myPreferences.getBoolean(PREFSTRING, false);
        if(isCurrentThemeAppTheme){
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.SecondTheme);
        }
        myPreferencesEditor = myPreferences.edit();


        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        mSwitchThemeButton = (Button) findViewById(R.id.switchtheme_button);

        mSwitchThemeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myPreferencesEditor.putBoolean(PREFSTRING, !isCurrentThemeAppTheme);
                myPreferencesEditor.apply();

                Intent intent = new Intent(getApplicationContext() ,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //improving performance if changes
        //in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        */

        //TODO: specify an adapter

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
    }

    @Override
    public void onMovieSelect(Movie movie) {
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
