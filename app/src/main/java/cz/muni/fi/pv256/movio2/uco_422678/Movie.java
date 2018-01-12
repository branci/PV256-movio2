package cz.muni.fi.pv256.movio2.uco_422678;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BranislavSmik on 10/19/2017.
 */

public class Movie implements Parcelable{

    private Long mId;
    private Long mRealeaseDate;
    private String mCoverPath;
    private String mTitle;
    private String mBackdrop;
    private String mOverview;
    private Float mPopularity;

    public Movie(Long id, Long realeaseDate, String coverPath, String backdrop, String title, String overview, Float popularity) {
        mId = id;
        mRealeaseDate = realeaseDate;
        mCoverPath = coverPath;
        mTitle = title;
        mBackdrop = backdrop;
        mOverview = overview;
        mPopularity = popularity;
    }

    public Long getId() { return mId; }

    public void setId(Long id) { mId = id; }

    public Long getRealeaseDate() {
        return mRealeaseDate;
    }

    public void setRealeaseDate(Long realeaseDate) {
        mRealeaseDate = realeaseDate;
    }

    public String getCoverPath() {
        return mCoverPath;
    }

    public void setCoverPath(String coverPath) {
        mCoverPath = coverPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public void setBackdrop(String backdrop) {
        mBackdrop = backdrop;
    }

    public Float getPopularity() {
        return mPopularity;
    }

    public void setPopularity(Float popularity) {
        mPopularity = popularity;
    }

    public void setOverview(String overview){ mOverview = overview;}

    public String getOverview() { return  mOverview;}

    @Override
    public int describeContents() {
        return 0;
    }

    public Movie(Parcel in) {
        mId = in.readLong();
        mRealeaseDate = in.readLong();
        mCoverPath = in.readString();
        mTitle = in.readString();
        mBackdrop = in.readString();
        mOverview = in.readString();
        mPopularity = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mRealeaseDate);
        dest.writeString(mCoverPath);
        dest.writeString(mTitle);
        dest.writeString(mBackdrop);
        dest.writeString(mOverview);
        dest.writeFloat(mPopularity);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel sourceParcel) {
            return new Movie(sourceParcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
