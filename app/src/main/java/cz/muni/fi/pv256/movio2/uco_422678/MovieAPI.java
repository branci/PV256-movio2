package cz.muni.fi.pv256.movio2.uco_422678;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by BranislavSmik on 12/1/2017.
 */

public interface MovieAPI {
    @GET(Constants.PATH + Constants.APIKEYv3) //+ "{query_param}"
    public Call<MovieList> getMovieList(@Query("query_param") String query_param);
}
