package cz.muni.fi.pv256.movio2.uco_422678;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cz.muni.fi.pv256.movio2.R;

/**
 * Created by BranislavSmik on 10/19/2017.
 */

public class MovieDetailActivity extends AppCompatActivity{
    public static final String EXTRA_MOVIE = "extra_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.description_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        */

        if(savedInstanceState == null){
            Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            FragmentManager fm = getSupportFragmentManager();
            DetailFragment fragment = (DetailFragment) fm.findFragmentById(R.id.movie_detail_container);

            if (fragment == null) {
                fragment = DetailFragment.newInstance(movie);
                fm.beginTransaction()
                        .add(R.id.movie_detail_container, fragment)
                        .commit();
            }
        }
    }


}