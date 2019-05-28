package com.app.android.ideatapp.home.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.android.ideatapp.R;
import com.app.android.ideatapp.home.models.ItemModel;

import java.util.List;

public class QueueItemsAdapter extends RecyclerView.Adapter<QueueItemsAdapter.MyViewHolder> {

    private List<ItemModel> modelsList;

    public QueueItemsAdapter(List<ItemModel> modelsList) {
        this.modelsList = modelsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemModel itemModel = modelsList.get(position);
        holder.title.setText(itemModel.getTitle());
        holder.subtitle.setText(itemModel.getDate() + " " + itemModel.getTime());
        if (itemModel.getStatus().equals("SENT")){
            holder.status.setImageResource(R.drawable.ic_done);
        }
        if (itemModel.getSource().equals("EMAIL")){
            holder.source.setImageResource(R.drawable.ic_email);
            holder.source.setBackgroundResource(android.R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return modelsList.size();
    }

    public void setModelsList(List<ItemModel> data) {
        this.modelsList = data;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView status;
        public ImageView source;
        public TextView title;
        public TextView subtitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            status = itemView.findViewById(R.id.status);
            source = itemView.findViewById(R.id.source);
        }
    }
}
