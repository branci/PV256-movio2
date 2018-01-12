package cz.muni.fi.pv256.movio2.uco_422678;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by BranislavSmik on 11/23/2017.
 */

//THIS CLASS IS OBSOLETE
public class Networking {

    //private static String URL = "http://api.themoviedb.org/";
    //private static String PATH = "3/discover/movie?api_key=";
    //private static String QUERY_PARAM_INTHEATRES = "&primary_release_date.gte=2017-12-01&primary_release_date.lte=2017-12-24"; //Just MVP solution
    //private static String QUERY_PARAM_DRAMA = "&with_genres=18&sort_by=popularity.desc";

    private static OkHttpClient client = new OkHttpClient();
    private Networking() {}

    public static ArrayList<Movie> getMovieList(String query_param) throws IOException {

        Request request = new Request.Builder()
                .url(Constants.URL + Constants.PATH + Constants.APIKEYv3 + query_param)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        return getData(response.body().string());
    }

    private static ArrayList<Movie> getData(String data) {
        ArrayList<Movie> mData = new ArrayList();

        try {
            JSONObject json = new JSONObject(data);
            Gson gson = new Gson();
                ArrayList<MovieDTO> movies = gson.fromJson(json.getJSONArray("results").toString(), new TypeToken<List<MovieDTO>>() {
            }.getType());

            for (MovieDTO m : movies) {
                Movie movie = new Movie(m.getId() ,m.getRealeaseDateAsLong(), m.getCoverPath(), m.getBackdrop(), m.getTitle(), m.getOverview(), m.getPopularityAsFloat());
                mData.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList();
        }
        return mData;
    }
}