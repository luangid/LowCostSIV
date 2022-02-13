package com.luan_g.drawerlowsiv.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.luan_g.drawerlowsiv.R;
import com.luan_g.drawerlowsiv.entity.FileEntity;
import com.luan_g.drawerlowsiv.entity.OnListClick;

public class FilesViewHolder extends RecyclerView.ViewHolder {
    private TextView fileName;
    private ImageView imageIcon;
    private Context context;

    public FilesViewHolder(@NonNull View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        fileName = itemView.findViewById(R.id.text_item_file);
        imageIcon = itemView.findViewById(R.id.image_item_file);
    }
    public void bind(final FileEntity file, final OnListClick listener){
        this.fileName.setText(file.getName());
        if(file.getRootId() >= 0){
            imageIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_image_512));
        }
        this.fileName.setOnClickListener(v -> {
            listener.onClick(file.getId(), file.getRootId(), file.getLevel());
        });
    }
}
