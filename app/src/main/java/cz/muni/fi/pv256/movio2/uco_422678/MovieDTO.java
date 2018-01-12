package cz.muni.fi.pv256.movio2.uco_422678;

/**
 * Created by BranislavSmik on 11/27/2017.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class MovieDTO {
    @SerializedName("id")
    private Long mId;
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
    @SerializedName("overview")
    private String mOverview;


    public MovieDTO(Long id, String realeaseDate, String coverPath, String backdrop, String title, String overview, String popularity) {
        mId = id;
        mRealeaseDate = realeaseDate;
        mCoverPath = coverPath;
        mTitle = title;
        mBackdrop = backdrop;
        mOverview = overview;
        mPopularity = popularity;
    }

    public Long getId() { return mId; }

    public String getRealeaseDate() {
        return mRealeaseDate;
    }

    public Long getRealeaseDateAsLong() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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

    public String getOverview() {return mOverview;}

    public Float getPopularityAsFloat() {
        return Float.parseFloat(getPopularity());
    }
}

