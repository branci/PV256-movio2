package cz.muni.fi.pv256.movio2.uco_422678;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.widget.ListView;
import android.widget.Button;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.ArrayList;
import android.os.AsyncTask;
import android.view.ViewStub;
import java.io.IOException;
import android.widget.Toast;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    private RecyclerView mRecyclerView;
    protected RecyclerAdapter mRecyclerAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    //protected AsyncTask downloadTask;
    private ViewStub mEmptyView;
    private Downloader mDownloader;


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
        //mEmptyView = (ViewStub) view.findViewById(R.id.nodata_textview);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        if(!isConnected(this.getActivity())) {
            Toast.makeText(getActivity(), "NO CONNECTION", Toast.LENGTH_LONG).show();
        }
        else {
            if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
                mPosition = savedInstanceState.getInt(SELECTED_KEY);
            }
            mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

            mRecyclerAdapter = new RecyclerAdapter(getContext(), new ArrayList<>());
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mDownloader = new Downloader();
            mDownloader.execute();
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

    private class Downloader extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ArrayList<Movie> movieListNew = Networking.getMovieList(Constants.QUERY_PARAM_INTHEATRES);
                ArrayList<Movie> movieListDrama = Networking.getMovieList(Constants.QUERY_PARAM_DRAMA);
                final ArrayList<Object> items = new ArrayList<>();

                items.add("In theatres now");
                items.addAll(movieListNew);
                items.add("Drama movies");
                items.addAll(movieListDrama);

                MovieListFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerAdapter.dataUpdate(items);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
                Toast.makeText(getActivity().getApplicationContext(), "New movies updated", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity().getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
