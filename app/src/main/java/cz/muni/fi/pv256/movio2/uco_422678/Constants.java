package cz.muni.fi.pv256.movio2.uco_422678;

/**
 * Created by BranislavSmik on 11/26/2017.
 */

public class Constants {
    public static final String APIKEYv3 = "80606f918fbd89549fcbb4a0e2782925";
    public static final String APIKEYv4 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDYwNmY5MThmYmQ4OTU0OWZjYmI0YTBlMjc4MjkyNSIsInN1YiI6IjU5ZDU0NDg4YzNhMzY4NDU3MjAxYmFjYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.KCTMoy6eS9chRIUFdGV0KYy6d7Ebx2seEtHL4mrEzHg";

    public static final String QUERY_PARAM_INTHEATRES = "&primary_release_date.gte=2018-01-01&primary_release_date.lte=2018-01-31"; //Just MVP solution
    public static final String QUERY_PARAM_DRAMA = "&with_genres=18&sort_by=popularity.desc";
    public static final String URL = "http://api.themoviedb.org/";
    public static final String PATH = "3/discover/movie?api_key=";
}