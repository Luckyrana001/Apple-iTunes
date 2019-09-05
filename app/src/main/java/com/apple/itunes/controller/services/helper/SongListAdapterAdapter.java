package com.apple.itunes.controller.services.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apple.itunes.R;
import com.apple.itunes.model.SongListModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SongListAdapterAdapter extends RecyclerView.Adapter<SongListAdapterAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SongListModel> cardTypeList;


    public SongListAdapterAdapter(Context mContext, ArrayList<SongListModel> cardTypeList) {
        this.mContext = mContext;
        this.cardTypeList = cardTypeList;
    }

    @Override
    public SongListAdapterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_adapter, parent, false);

        return new SongListAdapterAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SongListAdapterAdapter.MyViewHolder holder, int position) {
        SongListModel model = cardTypeList.get(position);
        holder.collectionCensoredName.setText(model.getCollectionCensoredName());
        String[] dateString = model.getReleaseDate().split("T");
        String releaseDate = dateString[0];
        holder.releaseDate.setText("Release Date: "+releaseDate);
        holder.collectionPrice.setText("Price: $"+model.getCollectionPrice()+"");

        Glide.with(mContext)
                .load(model.getArtworkUrl100())
                .into(holder.thumbnailIv);
    }

    @Override
    public int getItemCount() {
        return cardTypeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView collectionCensoredName,collectionPrice,releaseDate;
        public ImageView thumbnailIv;
        public MyViewHolder(View view) {
            super(view);
            thumbnailIv = view.findViewById(R.id.thumbnail_song_iv);
            collectionCensoredName = view.findViewById(R.id.song_title_tv);

            releaseDate = view.findViewById(R.id.release_date_tv);
            collectionPrice = view.findViewById(R.id.price_tv);


        }
    }


    public void removeAt(int position) {
        cardTypeList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cardTypeList.size());
    }


    public void animateTo(List<SongListModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<SongListModel> newModels) {
        for (int i = cardTypeList.size() - 1; i >= 0; i--) {
            final SongListModel model = cardTypeList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<SongListModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final SongListModel model = newModels.get(i);
            if (!cardTypeList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<SongListModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final SongListModel model = newModels.get(toPosition);
            final int fromPosition = cardTypeList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public SongListModel removeItem(int position) {
        final SongListModel model = cardTypeList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, SongListModel model) {
        cardTypeList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final SongListModel model = cardTypeList.remove(fromPosition);
        cardTypeList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
