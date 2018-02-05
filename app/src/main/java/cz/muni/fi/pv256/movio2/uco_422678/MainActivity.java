package cz.muni.fi.pv256.movio2.uco_422678;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import cz.muni.fi.pv256.movio2.R;
import cz.muni.fi.pv256.movio2.uco_422678.sync.UpdaterSyncAdapter;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnMovieSelectListener {
    //private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;
    //private static final String PREFSTRING = "currentTheme";
    //protected static ArrayList<Object> mMovieList;
    private boolean mTwoPane;
    private SwitchCompat mSwitchButton;
    private Toolbar toolbar;
    protected MovieListFragment mListFragment;
    private static final String TAG = MainActivity.class.getSimpleName();

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

        UpdaterSyncAdapter.initializeSyncAdapter(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menuSwitch);
        item.setActionView(R.layout.menu_switch);
        mSwitchButton = item.getActionView().findViewById(R.id.switchForActionBar);
        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    compoundButton.setText("Favourites");
                    compoundButton.setChecked(true);
                    mListFragment = MovieListFragment.newInstance(true);
                } else {
                    mListFragment.setMenuVisibility(false);
                    compoundButton.setText("Discover");
                    compoundButton.setChecked(false);
                    mListFragment = MovieListFragment.newInstance(false);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main, mListFragment)
                        .commit();

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.synchronize:
                Log.d(TAG, "sync button clicked");
                UpdaterSyncAdapter.syncImmediately(getApplicationContext());
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
