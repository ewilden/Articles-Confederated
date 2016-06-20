package com.codepath.nytimessearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by evanwild on 6/20/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, R.layout.item_article_result, articles);
    }

    static class ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivImage) ImageView ivImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // get the data item for position
        Article article = getItem(position);

        ViewHolder holder;

        // check to see if the existing view is being reused, otherwise inflate the view
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view =
                    LayoutInflater.from(getContext()).inflate(R.layout.item_article_result, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        // Clear out recycled image from view from last time
        holder.ivImage.setImageResource(0);

        // Populate headline
        holder.tvTitle.setText(article.getHeadline());

        // Populate thumbnail image
        String thumbUrl = article.getThumbNail();
        if (!thumbUrl.equals(""))
        Picasso.with(getContext()).load(thumbUrl).fit().centerCrop().
                placeholder(R.drawable.placeholder_thumbnail).into(holder.ivImage);
        else {

        }
        return view;
    }
}
