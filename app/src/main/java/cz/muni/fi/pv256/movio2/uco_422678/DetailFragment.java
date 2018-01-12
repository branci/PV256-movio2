package cz.muni.fi.pv256.movio2.uco_422678;

import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Context;
import android.widget.Toast;

import cz.muni.fi.pv256.movio2.R;
import com.squareup.picasso.Picasso;


/**
 * Created by BranislavSmik on 10/19/2017.
 */

public class DetailFragment extends Fragment {
    public static final String TAG = DetailFragment.class.getSimpleName();
    private static final String ARGS_MOVIE = "args_movie";
    public static final String DATE_FORMAT = "dd.MM.yyyy";

    private Context mContext;
    private Movie mMovie;

    private SQLiteDatabase mDatabase;
    private MovieManagerImpl mFilmManager;
    private MovieDBHelper mDbHelper;

    public static DetailFragment newInstance(Movie movie) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle args = getArguments();
        if (args != null) {
            mMovie = args.getParcelable(ARGS_MOVIE);
        }

        mDbHelper = new MovieDBHelper(getActivity());
        mDatabase = mDbHelper.getWritableDatabase();
        mFilmManager = new MovieManagerImpl(mDatabase);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView titleTextView = (TextView) view.findViewById(R.id.movie_title);
        TextView ratingTextView = (TextView) view.findViewById(R.id.movie_rating);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.movie_description);
        TextView releaseDateTextView = (TextView) view.findViewById(R.id.movie_release);
        ImageView movieImage = (ImageView) view.findViewById(R.id.movie_image);

        final FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.float_but);
        floatingActionButton.setImageResource(R.drawable.plus);

        if (mFilmManager.containsMovie(mMovie.getId())) {
            floatingActionButton.setImageResource(R.drawable.minus);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilmManager.containsMovie(mMovie.getId())) {
                    mFilmManager.deleteMovie(mMovie);
                    Toast.makeText(getActivity(), mMovie.getTitle() + " removed from favorites.", Toast.LENGTH_LONG).show();
                    floatingActionButton.setImageResource(R.drawable.plus);
                } else {
                    mFilmManager.createMovie(mMovie);
                    Toast.makeText(getActivity(), mMovie.getTitle() + " added to favorites.", Toast.LENGTH_LONG).show();
                    floatingActionButton.setImageResource(R.drawable.minus);
                }

            }
        });

        if (mMovie != null) {
            titleTextView.setText(mMovie.getTitle());
            ratingTextView.setText(Float.toString(mMovie.getPopularity()));
            descriptionTextView.setText(mMovie.getOverview());
            Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500/" + mMovie.getBackdrop()).into(movieImage);

            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            releaseDateTextView.setText(dateFormat.format(mMovie.getRealeaseDate()));
        }

        return view;
    }

}
