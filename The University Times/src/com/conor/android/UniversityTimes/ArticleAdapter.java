package com.conor.android.UniversityTimes;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.conor.android.theUniversityTimes.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    // declaring our ArrayList of items
    private ArrayList<Article> objects;

    public ArticleAdapter(Context context, int textViewResourceId, ArrayList<Article> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.article_button, parent, false);
        }

        Article i = objects.get(position);

        if (i != null) {
            TextView heading = (TextView) v.findViewById(R.id.heading);
            // check to see if each individual textview is null.
            // if not, assign some text!
            if (heading != null) {
                heading.setText(i.getHeading());
            }

            ImageView featuredimage = (ImageView) v.findViewById(R.id.featuredimage);
            Drawable d = getContext().getResources().getDrawable(R.drawable.utblank);

            featuredimage.setImageDrawable(d) ;
            String imageUrl = i.getImageurl();

            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(imageUrl, featuredimage); // Default options will be used

            Typeface museo = Typeface.createFromAsset(getContext().getAssets(), "fonts/Museo_Slab_500.otf");
            heading.setTypeface(museo);
            heading.setTextSize(25);
        }
        // the view must be returned to our activity
        return v;
    }

}

