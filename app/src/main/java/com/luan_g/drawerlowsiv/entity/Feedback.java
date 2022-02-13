package com.luan_g.drawerlowsiv.entity;

public class Feedback {
   private boolean isSuccesses;
   private String info;

    public Feedback(boolean isSuccesses, String info) {
        this.isSuccesses = isSuccesses;
        this.info = info;
    }

    public boolean isSuccesses() {
        return isSuccesses;
    }

    public String getInfo() {
        return info;
    }
}
