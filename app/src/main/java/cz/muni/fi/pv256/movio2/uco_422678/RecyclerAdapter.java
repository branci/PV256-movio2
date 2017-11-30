package cz.muni.fi.pv256.movio2.uco_422678;

/**
 * Created by Branci on 11/2/2017.
 */


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import com.squareup.picasso.Picasso;

import cz.muni.fi.pv256.movio2.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NODATA = 0;
    private static final int TYPE_MOVIE = 1;
    private static final int TYPE_CATEGORY = 2;
    private static final String IMAGE_PATH = "https://image.tmdb.org/t/p/w500/";

    private Context mContext;
    protected static List<Object> mMovieList;

    public RecyclerAdapter(Context context, List<Object> data) {
        mContext = context;
        mMovieList = data;
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.nodata_textview);
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.category_name);
        }
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView movie_title;
        public TextView movie_rating;
        public ImageView movie_image;
        private RelativeLayout mLayout;

        public MovieViewHolder(View itemView, final Context context) {
            super(itemView);
            movie_title = (TextView) itemView.findViewById(R.id.movie_title);
            movie_image = (ImageView) itemView.findViewById(R.id.movie_image);
            movie_rating = (TextView) itemView.findViewById(R.id.movie_rating);
            mLayout = (RelativeLayout) itemView.findViewById(R.id.layout);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context != null) {
                        //((MainActivity) context).onMovieSelect(getAdapterPosition());
                        ((MainActivity) context).onMovieSelect((Movie)mMovieList.get(getAdapterPosition()));
                    }
                }
            };

            mLayout.setOnClickListener(clickListener);

        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_movie_list, parent, false);
        switch(viewType) {
            case TYPE_MOVIE:
                view = inflater.inflate(R.layout.list_item_movie, parent, false);
                return new MovieViewHolder(view, mContext);
            case TYPE_CATEGORY:
                view = inflater.inflate(R.layout.list_item_category, parent,false);
                return new CategoryViewHolder(view);
            case TYPE_NODATA:
                view = inflater.inflate(R.layout.list_nodata, parent, false);
                return new EmptyViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return mMovieList.get(position) instanceof Movie ? TYPE_MOVIE : TYPE_CATEGORY;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int listItemType = getItemViewType(position);

        switch(listItemType){
            case TYPE_MOVIE:
                MovieViewHolder movieHolder = (MovieViewHolder) viewHolder;
                Movie movie = (Movie) mMovieList.get(position);
                movieHolder.movie_title.setText(movie.getTitle());
                movieHolder.movie_rating.setText(Float.toString(movie.getPopularity()));
                Picasso.with(mContext).load(IMAGE_PATH + movie.getCoverPath()).into(movieHolder.movie_image);
                break;
            case TYPE_CATEGORY:
                CategoryViewHolder categoryHolder = (CategoryViewHolder) viewHolder;
                categoryHolder.text.setText(mMovieList.get(position).toString());
                break;
            case TYPE_NODATA :
                EmptyViewHolder emptyView = (EmptyViewHolder) viewHolder;
                emptyView.text.setText(mMovieList.get(position).toString());
                break;
        }

    }

    public void dataUpdate(List<Object> data) {
        this.mMovieList = data;
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMovieList.size();
    }
}
