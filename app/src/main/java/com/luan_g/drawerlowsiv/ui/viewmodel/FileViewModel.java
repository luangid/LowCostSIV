package com.luan_g.drawerlowsiv.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.luan_g.drawerlowsiv.business.JSONHandler;
import com.luan_g.drawerlowsiv.constants.MQTTConstants;
import com.luan_g.drawerlowsiv.database.MQTTHandler;
import com.luan_g.drawerlowsiv.entity.Feedback;
import com.luan_g.drawerlowsiv.entity.FileEntity;
import com.luan_g.drawerlowsiv.entity.MessageListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileViewModel extends AndroidViewModel {
    private MQTTHandler mqttHandler;
    private static boolean isSubscribed = false;
    private String jsonString = "";

    private FileEntity root = new FileEntity(0, "root");
    private int level = 0;
    private int currentlyId = 0;

    private MutableLiveData<List<FileEntity>> mFileList = new MutableLiveData<List<FileEntity>>();
    public LiveData<List<FileEntity>> fileList = mFileList;
    private MutableLiveData<FileEntity> mFile = new MutableLiveData<FileEntity>();
    public LiveData<FileEntity> file = mFile;
    private MutableLiveData<Bitmap> mImage = new MutableLiveData<>();
    public LiveData<Bitmap> image = mImage;
    private MutableLiveData<Feedback> mFeedback = new MutableLiveData();
    public LiveData<Feedback> feedback = mFeedback;


    public FileViewModel(@NonNull Application application) {
        super(application);
        this.mqttHandler = MQTTHandler.getINSTANCE();
        this.mFileList.setValue(new ArrayList<>());
        this.topicGetFiles(application.getApplicationContext());
        this.topicGetImage(application.getApplicationContext());
    }

    public void setRootList(List<FileEntity> list) {
        this.root.setList(list);
    }

    public void regressLevel() {
        level--;
        mFile.setValue(root.getListChildren().get(mFile.getValue().getRootId()));
    }

    public int getLevel() {
        return level;
    }

    public FileEntity getRoot() {
        return this.root;
    }

    public void getItemFromFileList(int id, int rootId, int level) {
        switch (level) {
            case 1:
                mFile.setValue(root.getListChildren().get(id));
                break;
            case 2:
                mFile.setValue(root.getListChildren().get(rootId).getListChildren().get(id));
                break;
        }
        currentlyId = mFile.getValue().getId();
    }

    public FileEntity getFile() {
        return this.mFile.getValue();
    }
    public void setMFileList(List<FileEntity> list){
        this.root.getListChildren().get(mFile.getValue().getId()).setList(list);
    }
    private void topicGetFiles(Context context) {
        this.subscribeMQTTTopic(MQTTConstants.TOPICS.SUB_FILES, context, new MessageListener() {
            @Override
            public void onMessageReceived(byte[] message) {
                String s = new String(message);
                jsonString += s;
                if (jsonString.charAt(jsonString.length() - 1) == '}') {
                    level++;
                    int rootId = level <= 1 ? 0 : mFile.getValue().getId();
                    mFileList.postValue(JSONHandler.createFileList(jsonString, rootId, level));

                    jsonString = "";
                }

            }

            @Override
            public void onSuccess(boolean success) {
                mFeedback.postValue(new Feedback(success, success ? "Subscribed success" : "Subscribed FAIL"));
            }
        });

    }

    private void topicGetImage(Context context) {
        this.subscribeMQTTTopic(MQTTConstants.TOPICS.SUB_IMG_FROM_FILE, context, new MessageListener() {
            @Override
            public void onMessageReceived(byte[] message) {
                mImage.postValue(BitmapFactory.decodeByteArray(message, 0, message.length));
            }

            @Override
            public void onSuccess(boolean success) {
                mFeedback.postValue(new Feedback(success, success ? "Subscribed success" : "Subscribed FAIL"));
            }
        });

    }

    private void subscribeMQTTTopic(String topic, Context context, MessageListener listener) {
        if (!isSubscribed) {
            this.mqttHandler.subcribeToTopic(context, topic, listener);
        }
    }

    public void publishMessageFile(FileEntity fileEntity) {
        String message = "/i/";
        if (fileEntity.getLevel() == 1) {
            message += fileEntity.getName();
        } else {
            message += root.getListChildren().get(fileEntity.getRootId()).getName() + "/i" + fileEntity.getName();
        }
        publishMessage(message);
    }

    public void publishMessage(String message) {

        this.mqttHandler.publishToTopic(MQTTConstants.TOPICS.SAVER_MSG, message, (success, status) -> {
            this.mFeedback.postValue(new Feedback(success, status));
        });
    }

}
