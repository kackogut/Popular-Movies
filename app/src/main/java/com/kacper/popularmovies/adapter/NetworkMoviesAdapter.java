package com.kacper.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kacper.popularmovies.R;
import com.kacper.popularmovies.data.model.Movie;
import com.kacper.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import it.sephiroth.android.library.picasso.Picasso;


public class NetworkMoviesAdapter extends RecyclerView.Adapter<NetworkMoviesAdapter.PosterAdapterViewHolder> {

    private ArrayList<Movie> allMoviesOnPage;

    private Context context;
    private final PosterAdapterOnClickHandler onClickHandler;

    public interface PosterAdapterOnClickHandler{
        void onClick(Movie clickedMovie);
    }
    public NetworkMoviesAdapter(Context context, PosterAdapterOnClickHandler clickHandler){
        this.context = context;
        this.onClickHandler = clickHandler;
    }

    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView singlePosterImage;
        public PosterAdapterViewHolder(View itemView) {
            super(itemView);
            singlePosterImage=(ImageView)itemView.findViewById(R.id.single_poster);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie tmpMovie = allMoviesOnPage.get(adapterPosition);
            onClickHandler.onClick(tmpMovie);
        }
        public void bind(){
            if(allMoviesOnPage.size()>0) {
                URL imageUrl = NetworkUtils.buildImageURL(allMoviesOnPage.get(getAdapterPosition()).getImageURI(), NetworkUtils.MEDIUM_SIZE_POSTER);
                Picasso.with(context).load(imageUrl.toString()).into(singlePosterImage);
            }
        }
    }

    @Override
    public PosterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.poster_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder holder, int position) {
        holder.bind();
    }


    @Override
    public int getItemCount() {
        if(allMoviesOnPage==null) return 0;
        else return allMoviesOnPage.size();
    }

    public void setAllMoviesOnPage(ArrayList<Movie> allMoviesOnPage) {
        this.allMoviesOnPage = allMoviesOnPage;
        notifyDataSetChanged();
    }
}
