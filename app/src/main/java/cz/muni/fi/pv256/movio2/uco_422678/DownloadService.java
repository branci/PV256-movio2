package cz.muni.fi.pv256.movio2.uco_422678;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Notification;
import android.app.NotificationManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.content.Intent;
import android.content.Context;
import android.util.MalformedJsonException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import cz.muni.fi.pv256.movio2.R;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Created by BranislavSmik on 12/1/2017.
 */

public class DownloadService extends IntentService {
    public static final String ACTION_NEW = "download_new_movies";
    public static final String ACTION_DRAMA = "download_drama_movies";
    public static final String ACTION = "download_action";
    public static final String ERROR = "download_error";
    public static final String ERROR_PARSE = "parsing_error";
    public static final String ERROR_CONN = "connection_error";
    public static final String RESPONSE = "response";
    public static final String INTENT = "DonwloadService";
    public static final String CHANNEL = "download_notify";

    private MovieAPI APIservice;

    public DownloadService() {
        super(DownloadService.class.getSimpleName());
    }

    public DownloadService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        APIservice = getMovieAPI();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        downloadingNotification();

        try {
            if(action == ACTION_NEW) {
                Call<MovieList> requestNew = APIservice.getMovieList(Constants.QUERY_PARAM_INTHEATRES);
                MovieList newMovies = requestNew.execute().body();
                broadcastMovieList(new ArrayList<MovieDTO>(newMovies.getResults()), action);

            } else if(action == ACTION_DRAMA) {
                Call<MovieList> requestDrama = APIservice.getMovieList(Constants.QUERY_PARAM_DRAMA);
                MovieList dramaMovies = requestDrama.execute().body();
                broadcastMovieList(new ArrayList<MovieDTO>(dramaMovies.getResults()), action);
            }
        } catch (MalformedJsonException e) {
            broadcastMovieListError(ERROR_PARSE);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            broadcastMovieListError(ERROR_CONN);
            e.printStackTrace();
            return;
        }

        downloadingEndNotification();
    }

    private void broadcastMovieList(ArrayList<MovieDTO> movieList, String action) {
        Intent broadcastIntent = new Intent(INTENT);
        broadcastIntent.putExtra(ACTION, action);
        broadcastIntent.putExtra(RESPONSE, movieList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    private void broadcastMovieListError(String error) {
        Intent broadcastIntent = new Intent(INTENT);
        broadcastIntent.putExtra(ERROR, error);
        downloadingErrorNotification(error);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    private MovieAPI getMovieAPI() {
        MovieAPI sMovieAPI;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sMovieAPI= retrofit.create(MovieAPI.class);

        return  sMovieAPI;
    }

    public void downloadingErrorNotification(String error) {
        Notification.Builder n = prepareNotification();

        if(error == ERROR_CONN) {
            n.setContentText(getResources().getText(R.string.no_data_warning)).setSmallIcon(R.mipmap.ic_download);
        } else if(error == ERROR_PARSE) {
            n.setContentText(getResources().getText(R.string.parse_error)).setSmallIcon(R.mipmap.ic_download);
        }

        NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n.build()); //0 udává číslo notifikace. Na některých zařízeních nefunguje jinačí int než 0.
    }

    private void downloadingNotification() {
        Notification.Builder n = prepareNotification();
        n.setContentText("Downloading movies").setSmallIcon(R.mipmap.ic_download);

        NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n.build()); //0 udává číslo notifikace. Na některých zařízeních nefunguje jinačí int než 0.
    }

    private void downloadingEndNotification() {
        Notification.Builder n = prepareNotification();
        n.setContentText("Movies downloaded successfully").setSmallIcon(R.mipmap.ic_download);

        NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n.build()); //0 udává číslo notifikace. Na některých zařízeních nefunguje jinačí int než 0.
    }

    private Notification.Builder prepareNotification() {
        Intent intent = new Intent(this, MovieListFragment.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder n;
        if (Build.VERSION.SDK_INT < 26) {
            n  = new Notification.Builder(this);
        }
        else {
            n = new Notification.Builder(this, CHANNEL);
        }
        n.setContentTitle(getResources().getString(R.string.app_name))
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
        return n;
    }
}
