package com.luan_g.drawerlowsiv.constants;

public class MQTTConstants {
    private MQTTConstants() {
    }

    public static final String MQTT_THING = "APP";
    public static final String MQTT_ENDPOINT = "a2vz5gl3tw6yfx-ats.iot.sa-east-1.amazonaws.com";
    public static final int MILLIS_MESSAGE_TIMEOUT = 10000;

    public static final class COMMANDS {
        public static final String BEGIN_STREAM = "sb";
        public static final String END_STREAM = "se";
        public static final String GET_FILE_TREE = "file";
    }

    public static final class TOPICS {
        public static final String SUB_DETECTED_IMGS = "saver/detect/pic";
        public static final String SUB_FILES = "app/file";
        public static final String SUB_IMG_FROM_FILE = "app/img";
        public static final String SUB_CAM0_IMGS = "cam/0";
        public static final String SUB_CAM1_IMGS = "cam/1";
        public static final String SUB_CAM2_IMGS = "cam/2";
        public static final String SUB_CAM3_IMGS = "cam/3";

        public static final String SAVER_MSG = "saver/msg";
        public static final String CAM0_MSG = "cam/0/msg";
        public static final String CAM1_MSG = "cam/1/msg";
        public static final String CAM2_MSG = "cam/2/msg";
        public static final String CAM3_MSG = "cam/3/msg";
    }
}
