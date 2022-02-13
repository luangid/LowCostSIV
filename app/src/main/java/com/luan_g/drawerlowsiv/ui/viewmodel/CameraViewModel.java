package com.luan_g.drawerlowsiv.ui.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.luan_g.drawerlowsiv.constants.AppConstants;
import com.luan_g.drawerlowsiv.constants.MQTTConstants;
import com.luan_g.drawerlowsiv.database.MQTTHandler;
import com.luan_g.drawerlowsiv.entity.Feedback;
import com.luan_g.drawerlowsiv.entity.MessageListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CameraViewModel extends AndroidViewModel {
    private MQTTHandler mqttHandler;
    private static List<Integer> mSubscribedTopics = new ArrayList<>();

    private MutableLiveData<Bitmap> mImage = new MutableLiveData<>();
    public LiveData<Bitmap> image = mImage;
    private MutableLiveData<Feedback> mFeedback = new MutableLiveData();
    public LiveData<Feedback> feedback = mFeedback;
    private MutableLiveData<String> mDateTimeText = new MutableLiveData<>();
    public LiveData<String> dateTimeText = mDateTimeText;


    public CameraViewModel(@NonNull Application application) {
        super(application);
        this.mqttHandler = MQTTHandler.getINSTANCE();
    }
    private void setImage(Bitmap bitmap){
        this.mImage.setValue(bitmap);
    }
    public void subscribeMQTTTopic(int filter) {
        boolean b = true;
        for (Integer i : mSubscribedTopics) {
            if (i == filter) {
                b = false;
                break;
            }
        }
        if (b) {
            String topic;
            switch (filter) {
                case AppConstants.FRAGMENT_CAM0:
                    topic = MQTTConstants.TOPICS.SUB_CAM0_IMGS;
                    break;
                case AppConstants.FRAGMENT_CAM1:
                    topic = MQTTConstants.TOPICS.SUB_CAM1_IMGS;
                    break;
                case AppConstants.FRAGMENT_CAM2:
                    topic = MQTTConstants.TOPICS.SUB_CAM2_IMGS;
                    break;
                case AppConstants.FRAGMENT_CAM3:
                    topic = MQTTConstants.TOPICS.SUB_CAM3_IMGS;
                    break;
                default:
                    topic = "";
            }
            this.mqttHandler.subcribeToTopic(getApplication().getApplicationContext(), topic, new MessageListener() {
                @Override
                public void onMessageReceived(byte[] message) {
                    mImage.postValue(BitmapFactory.decodeByteArray(message, 0, message.length));
                }

                @Override
                public void onSuccess(boolean success) {
                    if (success) {
                        mSubscribedTopics.add(filter);
                    }
                    mFeedback.postValue(new Feedback(success, success ? "Subscribed success" : "Subscribed FAIL"));
                }
            });
        }
    }

    public void beginStreaming(int filter) {
        sendMessageToCam(MQTTConstants.COMMANDS.BEGIN_STREAM, filter);
    }

    public void endStreaming(int filter) {
        sendMessageToCam(MQTTConstants.COMMANDS.END_STREAM, filter);
    }

    private void sendMessageToCam(String msg, int filter) {
        String topic;
        switch (filter) {
            case AppConstants.FRAGMENT_CAM0:
                topic = MQTTConstants.TOPICS.CAM0_MSG;
                break;
            case AppConstants.FRAGMENT_CAM1:
                topic = MQTTConstants.TOPICS.CAM1_MSG;
                break;
            case AppConstants.FRAGMENT_CAM2:
                topic = MQTTConstants.TOPICS.CAM2_MSG;
                break;
            case AppConstants.FRAGMENT_CAM3:
                topic = MQTTConstants.TOPICS.CAM3_MSG;
                break;
            default:
                topic = "";
        }
        if (topic.equals("")) {
            this.mFeedback.setValue(new Feedback(false, "Find topic error"));
        } else {
            this.mqttHandler.publishToTopic(topic, msg, (success, status) -> {
                this.mFeedback.postValue(new Feedback(success, status));
            });
        }
    }

    public void getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/YYYY");
        this.mDateTimeText.setValue("Streaming begin"+sdf.format(Calendar.getInstance().getTime()));
    }


}