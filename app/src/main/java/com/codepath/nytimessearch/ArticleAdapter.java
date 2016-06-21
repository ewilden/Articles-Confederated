package com.codepath.nytimessearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by evanwild on 6/20/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articles;

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.ivImage) ImageView ivImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new ViewHolder instance
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);

        // Clear out recycled image from view from last time
        holder.ivImage.setImageResource(0);

        // Populate headline
        holder.tvTitle.setText(article.getHeadline());

        // Populate thumbnail image
        String thumbUrl = article.getThumbNail();
        Context c = holder.ivImage.getContext();
            Glide.with(c).load(thumbUrl).//placeholder(R.drawable.placeholder_thumbnail).
                    error(R.drawable.placeholder_thumbnail).
                    bitmapTransform(new CropCircleTransformation(c))
                                        .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
