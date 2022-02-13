package com.luan_g.drawerlowsiv.ui.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.luan_g.drawerlowsiv.database.MQTTHandler;
import com.luan_g.drawerlowsiv.entity.Feedback;
import com.luan_g.drawerlowsiv.entity.User;

public class MainViewModel extends AndroidViewModel{

    private MQTTHandler mqttHandler;
    private MutableLiveData<User> mUser = new MutableLiveData<>();
    public LiveData<User> user = mUser;
    private MutableLiveData<Feedback> mFeedback = new MutableLiveData();
    public LiveData<Feedback> feedback = mFeedback;
    private MutableLiveData<Bitmap> mImage = new MutableLiveData();
    public LiveData<Bitmap> image = mImage;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void setUser(User user){
        this.mUser.setValue(user);
    }



}
