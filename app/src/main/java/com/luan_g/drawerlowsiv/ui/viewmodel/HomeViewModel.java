package com.luan_g.drawerlowsiv.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttSubscriptionStatusCallback;
import com.luan_g.drawerlowsiv.constants.MQTTConstants;
import com.luan_g.drawerlowsiv.database.MQTTHandler;
import com.luan_g.drawerlowsiv.entity.Feedback;
import com.luan_g.drawerlowsiv.entity.MessageListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeViewModel extends AndroidViewModel {

    private MQTTHandler mqttHandler;

    private MutableLiveData<String> mDateTimeText = new MutableLiveData<>();
    public LiveData<String> dateTimeText = mDateTimeText;
    private MutableLiveData<Bitmap> mImage = new MutableLiveData<>();
    public LiveData<Bitmap> image = mImage;
    private MutableLiveData<Feedback> mFeedback = new MutableLiveData();
    public LiveData<Feedback> feedback = mFeedback;

    private static boolean isSubscribed = false;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.mqttHandler = MQTTHandler.getINSTANCE();
        this.mDateTimeText.setValue("Sem imagens detectadas");
        this.subscribeMQTTTopic(MQTTConstants.TOPICS.SUB_DETECTED_IMGS, application.getApplicationContext());

    }
    public void getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/YYYY");
        this.mDateTimeText.setValue(sdf.format(Calendar.getInstance().getTime()));
    }
    private void subscribeMQTTTopic(String topic, Context context){
        if(!isSubscribed) {
            this.mqttHandler.subcribeToTopic(context, topic, new MessageListener() {
                @Override
                public void onMessageReceived(byte[] message) {
                    mImage.postValue(BitmapFactory.decodeByteArray(message, 0, message.length));
                }

                @Override
                public void onSuccess(boolean success) {
                    isSubscribed = true;
                    mFeedback.postValue(new Feedback(success, success ? "Subscribed success" : "Subscribed FAIL"));
                }
            });
        }
    }
}