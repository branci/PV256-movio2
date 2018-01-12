package cz.muni.fi.pv256.movio2;

import android.test.AndroidTestCase;
import android.util.Log;
import java.util.ArrayList;

import cz.muni.fi.pv256.movio2.uco_422678.Movie;
import cz.muni.fi.pv256.movio2.uco_422678.MovieContract;
import cz.muni.fi.pv256.movio2.uco_422678.MovieDBHelper;
import cz.muni.fi.pv256.movio2.uco_422678.MovieManagerImpl;

/**
 * Created by BranislavSmik on 1/11/2018.
 */

public class MovieManagerTest extends AndroidTestCase {
    private static final String TAG = MovieManagerImpl.class.getSimpleName();

    private MovieManagerImpl mManager;

    @Override
    protected void setUp() throws Exception {
        mManager = new MovieManagerImpl((new MovieDBHelper(mContext)).getWritableDatabase());
    }

    @Override
    public void tearDown() throws Exception {
        mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null
        );
    }

    public void testAddMovie() throws Exception {
        Movie expected = createMovie("testTitle", 1500260033l, "test-cover", "test-backdrop", 5.3f , "description of the test");
        mManager.createMovie(expected);
        ArrayList<Movie> saved = (ArrayList)mManager.getSavedMovies();

        Log.d(TAG, saved.get(0).toString());
        assertTrue(saved.size() == 1);
        assertEquals(expected, saved.get(0));
    }

    private Movie createMovie(String title, Long release, String cover, String backdrop, float rating, String overview) {
        Movie m = new Movie(123l, release, cover, backdrop, title, overview, rating);
        return m;
    }
}
