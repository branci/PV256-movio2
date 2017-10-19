package cz.muni.fi.pv256.movio2.uco_422678;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Button;
import android.content.Context;
import java.util.ArrayList;

import cz.muni.fi.pv256.movio2.R;

/**
 * Created by BranislavSmik on 10/19/2017.
 */

public class MovieListFragment extends Fragment {
    private static final String TAG = MovieListFragment.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";

    private int mPosition = ListView.INVALID_POSITION;
    private OnMovieSelectListener mListener;
    private Context mContext;
    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        mListener = (OnMovieSelectListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        //Avoid Activity leaking
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list , container, false);

        mMovieList.add(new Movie(1234, "",  "movie1", "This is a movie 1", 0.1f));
        mMovieList.add(new Movie(5678, "",  "movie2", "This is a movie 2", 0.2f));
        mMovieList.add(new Movie(91011, "",  "movie3", "This is a movie 3", 0.3f));

        mButton1 = (Button) view.findViewById(R.id.movie1);
        mButton2 = (Button) view.findViewById(R.id.movie2);
        mButton3 = (Button) view.findViewById(R.id.movie3);

        View.OnClickListener clickListener =  new View.OnClickListener(){

            @Override
            public void onClick(View myView) {
                switch(myView.getId()) {
                    case R.id.movie1:
                        mListener.onMovieSelect(mMovieList.get(0));
                        break;
                    case R.id.movie2:
                        mListener.onMovieSelect(mMovieList.get(1));
                        break;
                    case R.id.movie3:
                        mListener.onMovieSelect(mMovieList.get(2));
                        break;
                }
            }
        };
        mButton1.setOnClickListener(clickListener);
        mButton2.setOnClickListener(clickListener);
        mButton3.setOnClickListener(clickListener);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public interface OnMovieSelectListener {
        void onMovieSelect(Movie movie);
    }

}
