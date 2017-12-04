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
import android.os.Build;
import android.view.ViewStub;
import java.io.IOException;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import cz.muni.fi.pv256.movio2.R;
import cz.muni.fi.pv256.movio2.uco_422678.DownloadService;

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
    private ViewStub mEmptyView;
    private MovieBroadcastReceiver mReceiver;

    private ArrayList<Object> items = new ArrayList<>();

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

            initChannels(getActivity());

            mRecyclerAdapter = new RecyclerAdapter(getContext(), new ArrayList<>());
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            Intent intent = new Intent(getActivity(), DownloadService.class);
            intent.setAction(DownloadService.ACTION_NEW);
            getActivity().startService(intent);

            intent = new Intent(getActivity(), DownloadService.class);
            intent.setAction(DownloadService.ACTION_DRAMA);
            getActivity().startService(intent);

            mReceiver = new MovieBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter(DownloadService.INTENT);
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
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

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(DownloadService.INTENT);
        mReceiver = new MovieBroadcastReceiver();
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    public class MovieBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String error = intent.getStringExtra(DownloadService.RESPONSE);
            if (error != null) {
                switch (error) {
                    case DownloadService.ERROR_CONN:
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        break;
                    case DownloadService.ERROR_PARSE:
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        break;
                }
            }

            String action = intent.getStringExtra(DownloadService.ACTION);
            ArrayList<MovieDTO> movieList = (ArrayList<MovieDTO>) intent.getSerializableExtra(DownloadService.RESPONSE);
            if(action == DownloadService.ACTION_NEW) {
                //items = new ArrayList<>();
                items.add("In theatres now");
                addMovies(movieList);

                if (mRecyclerAdapter != null) mRecyclerAdapter.dataUpdate(items);
            } else if(action == DownloadService.ACTION_DRAMA) {
                //items = new ArrayList<>();
                items.add("Drama movies");
                addMovies(movieList);

                if (mRecyclerAdapter != null) mRecyclerAdapter.dataUpdate(items);
            }
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void addMovies(ArrayList<MovieDTO> movieList){
        for (MovieDTO m : movieList) {
            Movie movie = new Movie(m.getRealeaseDateAsLong(), m.getCoverPath(), m.getBackdrop(), m.getTitle(), m.getPopularityAsFloat());
            items.add(movie);
        }
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(DownloadService.CHANNEL, "Download", NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(channel);
    }
}
