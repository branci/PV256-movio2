package cz.muni.fi.pv256.movio2.uco_422678;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by BranislavSmik on 12/7/2017.
 */

public abstract class MovieManager<T> {

    protected SQLiteDatabase mDatabase;

    public MovieManager(SQLiteDatabase database) {
        mDatabase = database;
    }

    public abstract void createMovie(Movie movie);
    //public abstract void updateMovie(Movie movie);
    public abstract void deleteMovie(Movie movie);
    public abstract Movie getMovie(Cursor cursor);
    public abstract List<Movie> getSavedMovies();
    public abstract boolean containsMovie(Long id);
}