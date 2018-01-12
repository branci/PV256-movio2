package cz.muni.fi.pv256.movio2.uco_422678;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.muni.fi.pv256.movio2.BuildConfig;

/**
 * Created by BranislavSmik on 12/7/2017.
 */

public class MovieManagerImpl extends MovieManager<Movie>{

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_RELEASE_DATE = 1;
    public static final int COL_MOVIE_COVER_PATH = 2;
    public static final int COL_MOVIE_TITLE = 3;
    public static final int COL_MOVIE_BACKDROP_PATH = 4;
    public static final int COL_MOVIE_OVERVIEW = 5;
    public static final int COL_MOVIE_POPULARITY = 6;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_COVER_PATH,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
    };

    //String WHERE_ID = MovieContract.MovieEntry._ID + " = ?";

    public MovieManagerImpl(SQLiteDatabase database) {
        super(database);
    }

    @Override
    public void createMovie(Movie movie) {
        if (movie == null) {
            throw new NullPointerException("movie is null");
        }
        if (movie.getTitle() == null) {
            throw new IllegalStateException("movie title is null");
        }
        if (movie.getCoverPath() == null) {
            throw new IllegalStateException("movie coverpath is null");
        }

//        movie.setId(ContentUris.parseId(mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
//                prepareMovieValues(movie))));
        mDatabase.insert(MovieContract.MovieEntry.MOVIE_TABLE_NAME, null, prepareMovieValues(movie));
    }


    @Override
    public void deleteMovie(Movie movie) {
        if (movie == null) {
            throw new NullPointerException("movie is nulll");
        }
        if (movie.getId() == null) {
            throw new IllegalStateException("movie id is null");
        }

//        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
//                MovieContract.MovieEntry._ID + " = ?", new String[]{String.valueOf(movie.getId())});
        mDatabase.delete(MovieContract.MovieEntry.MOVIE_TABLE_NAME, MovieContract.MovieEntry._ID + " = " + movie.getId(), null);
    }

    /*
    @Override
    public void updateMovie(Movie movie) {
        if (movie == null) {
            throw new NullPointerException("movie is null");
        }
        if (movie.getTitle() == null) {
            throw new IllegalStateException("movie title is null");
        }
        if (movie.getCoverPath() == null) {
            throw new IllegalStateException("movie coverpath is null");
        }
        if (movie.getId() == null) {
            throw new IllegalStateException("movie id is null");
        }

        mContext.getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, prepareMovieValues(movie),
                MovieContract.MovieEntry._ID + " = ?", new String[]{String.valueOf(movie.getId())});

    }
    */


    @Override
    public List<Movie> getSavedMovies() {

//        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, MOVIE_COLUMNS, null, null, null);

        Cursor cursor = mDatabase.query(MovieContract.MovieEntry.MOVIE_TABLE_NAME, MOVIE_COLUMNS, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            List<Movie> movies = new ArrayList<>(cursor.getCount());
            try {
                while (!cursor.isAfterLast()) {
                    movies.add(getMovie(cursor));
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return movies;
        }

        return Collections.emptyList();
    }

    @Override
    public Movie getMovie(Cursor cursor) {
        Movie movie = new Movie(null, null, null, null, null, null, null);
        movie.setId(cursor.getLong(COL_MOVIE_ID));
        movie.setTitle(cursor.getString(COL_MOVIE_TITLE));
        movie.setRealeaseDate(MovieContract.getDateFromDb(cursor.getString(COL_MOVIE_RELEASE_DATE)));
        movie.setOverview(cursor.getString(COL_MOVIE_OVERVIEW));
        movie.setCoverPath(cursor.getString(COL_MOVIE_COVER_PATH));
        movie.setBackdrop(cursor.getString(COL_MOVIE_BACKDROP_PATH));
        movie.setPopularity(cursor.getFloat(COL_MOVIE_POPULARITY));

        return movie;
    }


    @Override
    public boolean containsMovie(Long id) {
        Log.d("XXX", BuildConfig.APPLICATION_ID + ".provider");
//        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
//                new String[]{MovieContract.MovieEntry._ID}, WHERE_ID,
//                new String[]{String.valueOf(id)}, null);
        String Query = "Select * from " + MovieContract.MovieEntry.MOVIE_TABLE_NAME + " where " + MovieContract.MovieEntry._ID + " = " + id;
        Cursor cursor = mDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    private ContentValues prepareMovieValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry._ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, MovieContract.insertDateToDb(movie.getRealeaseDate()));
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_COVER_PATH, movie.getCoverPath());
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdrop());
        values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());

        return values;
    }

}
