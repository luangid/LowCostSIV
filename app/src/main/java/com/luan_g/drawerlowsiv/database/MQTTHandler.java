package com.luan_g.drawerlowsiv.database;

import android.content.Context;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.amplifyframework.core.Amplify;
import com.luan_g.drawerlowsiv.constants.MQTTConstants;
import com.luan_g.drawerlowsiv.entity.ConnectionListener;
import com.luan_g.drawerlowsiv.entity.MessageListener;

public class MQTTHandler {
    private static MQTTHandler INSTANCE;
    private boolean mqttStarted = false;
    private AWSMobileClient mobileClient;
    private AWSIotMqttManager mqttManager;

    private MQTTHandler() {
        mobileClient = (AWSMobileClient) Amplify.Auth.getPlugin("awsCognitoAuthPlugin").getEscapeHatch();
        mqttManager = new AWSIotMqttManager(MQTTConstants.MQTT_THING, MQTTConstants.MQTT_ENDPOINT);
    }

    public static MQTTHandler getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new MQTTHandler();
        }
        return INSTANCE;
    }

    public void subcribeToTopic(Context context, String topic, MessageListener listener) {
        mobileClient.initialize(context, new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {
                mqttManager.connect(mobileClient, (status, throwable) -> {
                    if (AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected.equals(status)) {
                        mqttManager.subscribeToTopic(topic, AWSIotMqttQos.QOS0, (topic1, data) -> {
                            listener.onMessageReceived(data);
                        });
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();

            }
        });
    }

    public void publishToTopic(String topic, String message, ConnectionListener listener) {
        try {
            this.mqttManager.publishString(message, topic, AWSIotMqttQos.QOS0);
            listener.onConnectionSuccess(true, "Message sent");
        } catch (Exception e) {
            listener.onConnectionSuccess(false, "Send message fail");
        }
    }


}
