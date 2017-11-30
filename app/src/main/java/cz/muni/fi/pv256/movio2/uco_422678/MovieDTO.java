package cz.muni.fi.pv256.movio2.uco_422678;

/**
 * Created by BranislavSmik on 11/27/2017.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class MovieDTO {
    @SerializedName("release_date")
    private String mRealeaseDate;
    @SerializedName("poster_path")
    private String mCoverPath;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("vote_average")
    private String mPopularity;
    @SerializedName("backdrop_path")
    private String mBackdrop;

    public MovieDTO(String realeaseDate, String coverPath, String backdrop, String title, String popularity) {
        mRealeaseDate = realeaseDate;
        mCoverPath = coverPath;
        mTitle = title;
        mBackdrop = backdrop;
        mPopularity = popularity;
    }

    public String getRealeaseDate() {
        return mRealeaseDate;
    }

    public long getRealeaseDateAsLong() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(getRealeaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public String getCoverPath() {
        return mCoverPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public String getPopularity() {
        return mPopularity;
    }

    public Float getPopularityAsFloat() {
        return Float.parseFloat(getPopularity());
    }
}

