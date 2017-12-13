package com.hdarby.saspodcastplayer.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdarby.saspodcastplayer.R;
import com.hdarby.saspodcastplayer.model.FeedItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hdarby on 12/9/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private static final String DEFAULT_IMAGE = "https://www.android.com/static/2016/img/logo-android-green_1x.png";

    private Context mContext;
    private List<FeedItem> feedList;

    public FeedAdapter(Context context, List<FeedItem> feedItems) {

        mContext = context;
        feedList = feedItems;
    }

    public void add(int position, FeedItem feedItem) {
        feedList.add(position, feedItem);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        feedList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.feed_listview_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final FeedItem item = feedList.get(position);

        if (item.getThumbnail() == null) {
            Picasso.with(mContext).load(DEFAULT_IMAGE).into(holder.thumbnail);
        } else {
            Picasso.with(mContext).load(item.getThumbnail()).into(holder.thumbnail);
        }
        holder.title.setText(item.getTitle());
        holder.author.setText(item.getAuthor());
        holder.date.setText(item.getPublishedDate());

    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView thumbnail;
        public TextView title;
        public TextView author;
        public TextView date;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            thumbnail = v.findViewById(R.id.thumbnail);
            title = v.findViewById(R.id.title);
            author = v.findViewById(R.id.author);
            date = v.findViewById(R.id.pubDate);
        }
    }
}
