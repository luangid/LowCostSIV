package com.luan_g.drawerlowsiv.ui.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.luan_g.drawerlowsiv.R;
import com.luan_g.drawerlowsiv.adapter.FilesAdapter;
import com.luan_g.drawerlowsiv.constants.MQTTConstants;
import com.luan_g.drawerlowsiv.entity.OnListClick;
import com.luan_g.drawerlowsiv.ui.viewmodel.FileViewModel;

public class FilesActivity extends AppCompatActivity {

    private boolean auxBack = false;
    private FilesAdapter mAdapter = new FilesAdapter();
    private FileViewModel mViewModel;
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        getSupportActionBar().hide();

        this.mViewModel = new ViewModelProvider(this).get(FileViewModel.class);
        this.mViewHolder.linearWindow = findViewById(R.id.linear_window);
        this.mViewHolder.imageWindow = this.mViewHolder.linearWindow.findViewById(R.id.image_window);
        this.mViewHolder.backWindow = this.mViewHolder.linearWindow.findViewById(R.id.btn_window_back);
        this.mViewHolder.linearWindow.setVisibility(View.INVISIBLE);
        this.mViewHolder.btnFileBack = findViewById(R.id.btn_file_back);

        this.mViewHolder.mRecyclerView = findViewById(R.id.recycler_files);
        this.mViewHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mAdapter = new FilesAdapter();
        this.mViewHolder.mRecyclerView.setAdapter(this.mAdapter);

        this.setObservers();
        this.setListeners();

        this.mViewModel.publishMessage(MQTTConstants.COMMANDS.GET_FILE_TREE);

    }

    private void setObservers() {
        this.mViewModel.file.observe(this, fileEntity -> {
            if(fileEntity != null) {
                if(!auxBack) {
                    this.mViewModel.publishMessageFile(fileEntity);
                } else {
                    auxBack = false;
                }
            }
        });
        this.mViewModel.fileList.observe(this, fileEntities -> {
            this.mAdapter.setList(fileEntities);
            int level = this.mViewModel.getLevel();
            if(level == 1){
                this.mViewModel.setRootList(fileEntities);
            } else if(level == 2) {
                this.mViewModel.setMFileList(fileEntities);
            }
        });
        this.mViewModel.feedback.observe(this, feedback -> {
            if (!feedback.isSuccesses()) {
                Snackbar.make(this, getCurrentFocus(), feedback.getInfo(), Snackbar.LENGTH_LONG)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.snakeFail))
                        .show();
            }
        });
        this.mViewModel.image.observe(this, bitmap -> {
            this.mViewHolder.linearWindow.setVisibility(View.VISIBLE);
            this.mViewHolder.linearWindow.bringToFront();
            this.mViewHolder.imageWindow.setImageBitmap(bitmap);
        });
    }

    private void setListeners() {
        OnListClick listClick = (index, rootIndex, level) -> {
            this.mViewModel.getItemFromFileList(index, rootIndex, level);

        };
        this.mAdapter.setListener(listClick);
        this.mViewHolder.backWindow.setOnClickListener(v -> {
            this.mViewHolder.linearWindow.setVisibility(View.INVISIBLE);
            this.mViewHolder.mRecyclerView.bringToFront();
        });
        this.mViewHolder.btnFileBack.setOnClickListener(v -> {
                if(this.mViewModel.getLevel() <= 1){
                    finish();
                } else {
                    auxBack = true;
                    this.mViewModel.regressLevel();
                    this.mAdapter.setList(this.mViewModel.getRoot().getListChildren());

                }


        });

    }

    private class ViewHolder {
        RecyclerView mRecyclerView;
        LinearLayout linearWindow;
        Button backWindow;
        ImageView imageWindow;
        ImageView btnFileBack;
    }
}

