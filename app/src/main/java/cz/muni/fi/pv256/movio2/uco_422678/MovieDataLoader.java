package cz.muni.fi.pv256.movio2.uco_422678;

import java.util.List;
import android.content.Context;

/**
 * Created by BranislavSmik on 12/13/2017.
 */

public class MovieDataLoader extends AbstractDataLoader<List<Movie>> {

    private MovieManager<Movie> mMovieManager;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mGroupBy;
    private String mHaving;
    private String mOrderBy;

    public MovieDataLoader(Context context, MovieManager movieManager, String selection, String[] selectionArgs,
                                String groupBy, String having, String orderBy) {
        super(context);
        mMovieManager = movieManager;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mGroupBy = groupBy;
        mHaving = having;
        mOrderBy = orderBy;
    }

    @Override
    protected List<Movie> buildList() {
        List<Movie> movieList = mMovieManager.getSavedMovies();
        return movieList;
    }

    public void create(Movie movie) {
        new InsertTask(this).execute(movie);
    }

    public void delete(Movie movie) {
        new DeleteTask(this).execute(movie);
    }


    private class InsertTask extends ContentChangingTask<Movie, Void, Void> {
        InsertTask(MovieDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(Movie... params) {
            mMovieManager.createMovie(params[0]);
            return (null);
        }
    }


    private class DeleteTask extends ContentChangingTask<Movie, Void, Void> {
        DeleteTask(MovieDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(Movie... params) {
            mMovieManager.deleteMovie(params[0]);
            return (null);
        }
    }
}