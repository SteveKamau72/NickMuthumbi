package com.softtech.stevekamau.nickmuthumbi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.softtech.stevekamau.nickmuthumbi.R;
import com.softtech.stevekamau.nickmuthumbi.app.AppController;
import com.softtech.stevekamau.nickmuthumbi.model.Model;

import java.util.List;

/**
 * Created by Steve Kamau on 18-Aug-15.
 */
public class CustomGridAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Model> modelItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomGridAdapter(Activity activity, List<Model> modelItems) {
        this.activity = activity;
        this.modelItems = modelItems;
    }

    @Override
    public int getCount() {
        return modelItems.size();
    }

    @Override
    public Object getItem(int location) {
        return modelItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.grid_cell, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

        // getting movie data for the row
        Model m = modelItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());

        // rating
        rating.setText("Kshs: " + String.valueOf(m.getRating()+"0"));

        // genre
        String genreStr = "";
        for (String str : m.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        genre.setText("*"+genreStr);
        // release year
        year.setText(String.valueOf(m.getYear()));
        year.setVisibility(View.GONE);
        rating.setVisibility(View.GONE);
        thumbNail.setVisibility(View.GONE);
        genre.setVisibility(View.GONE);
        return convertView;
    }

}
