package com.luan_g.drawerlowsiv.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luan_g.drawerlowsiv.R;
import com.luan_g.drawerlowsiv.entity.FileEntity;
import com.luan_g.drawerlowsiv.entity.OnListClick;
import com.luan_g.drawerlowsiv.viewholder.FilesViewHolder;

import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesViewHolder> {

    private List<FileEntity> mList;
    private OnListClick mListener;
    @NonNull
    @Override
    public FilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_file_layout, parent,false);
        return new FilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesViewHolder holder, int position) {
        holder.bind(this.mList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<FileEntity> list){
        this.mList = list;
        notifyDataSetChanged();
    }
    public void setListener(OnListClick listener){
       this.mListener = listener;
    }
}
