package com.luan_g.drawerlowsiv.entity;

public interface MessageListener {
    void onMessageReceived(byte[] message);
    void onSuccess(boolean success);

}
